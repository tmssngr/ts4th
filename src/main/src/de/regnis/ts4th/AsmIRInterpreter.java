package de.regnis.ts4th;

import org.jetbrains.annotations.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * @author Thomas Singer
 */
public class AsmIRInterpreter {

	public static void main(String[] args) throws IOException {
		final Path forthFile = Paths.get(args[0]);

		final List<Function> functions = Parser.parseFile(forthFile);
		final TypeCheckerImpl typeChecker = new TypeCheckerImpl();
		functions.forEach(function -> typeChecker.add(function.name(), function.typesInOut()));

		final AsmIRStringLiterals stringLiterals = new AsmIRStringLiterals();
		final List<AsmIR> programInstructions = new ArrayList<>();
		int startIp = 0;
		for (Function function : functions) {
			final String name = function.name();
			System.out.println("Checking types for " + name);
			if (name.equals("main")) {
				startIp = programInstructions.size();
			}

			convertToIR(function, typeChecker, stringLiterals, programInstructions);
		}

		final AsmIRInterpreter interpreter = new AsmIRInterpreter(programInstructions, stringLiterals, startIp);
		final List<Object> result = interpreter.run(List.of());
		if (!result.isEmpty()) {
			System.out.println(result);
		}
	}

	@NotNull
	public static AsmIRInterpreter parseAndCreate(String code) {
		final List<Function> functions = Parser.parseString(code);

		final TypeCheckerImpl typeChecker = new TypeCheckerImpl();
		functions.forEach(function -> typeChecker.add(function.name(), function.typesInOut()));

		final AsmIRStringLiterals stringLiterals = new AsmIRStringLiterals();
		final List<AsmIR> programInstructions = new ArrayList<>();
		int startIp = 0;
		for (Function function : functions) {
			if (function.name().equals("main")) {
				startIp = programInstructions.size();
			}

			convertToIR(function, typeChecker, stringLiterals, programInstructions);
		}

		return new AsmIRInterpreter(programInstructions, stringLiterals, startIp);
	}

	private final Map<String, Integer> labelToIndex = new HashMap<>();
	private final Map<Integer, Integer> stringIndexToMem = new HashMap<>();
	private final Stack<Integer> dataStack = new Stack<>();
	private final Stack<Integer> callStack = new Stack<>();
	private final Map<Integer, Integer> memAddressToValue = new HashMap<>();
	private final StringBuilder buffer = new StringBuilder();
	private final List<AsmIR> instructions;
	private final int startIp;
	private final AsmIR.Visitor<String> visitor;

	private int mem;
	private int reg0;
	private int reg1;
	private int reg2;
	private boolean flagZ;

	private int ip;

	public AsmIRInterpreter(List<AsmIR> instructions, AsmIRStringLiterals stringLiterals, int startIp) {
		this.instructions = instructions;
		this.startIp = startIp;
		int index = 0;
		for (AsmIR instruction : instructions) {
			final String label = instruction.getLabel();
			if (label != null) {
				if (labelToIndex.put(label, index) != null) {
					throw new IllegalStateException("duplicate label " + label);
				}
			}
			index++;
		}

		final List<byte[]> constants = stringLiterals.getConstants();
		for (int i = 0; i < constants.size(); i++) {
			stringIndexToMem.put(i, mem);
			for (byte b : constants.get(i)) {
				memAddressToValue.put(mem++, (int)b);
			}
		}

		visitor = new AsmIR.Visitor<>() {
			@Override
			public String label(String name) {
				return null;
			}

			@Override
			public String literal(int value) {
				reg0 = value;
				return null;
			}

			@Override
			public String literal(boolean value) {
				reg0 = value ? 1 : 0;
				return null;
			}

			@Override
			public String stringLiteral(int constantIndex) {
				reg0 = stringIndexToMem.get(constantIndex);
				return null;
			}

			@Override
			public String jump(AsmIR.Condition condition, String target) {
				if (switch (condition) {
					case z -> flagZ;
					case nz -> !flagZ;
					default -> throw new IllegalStateException("unsupported condition " + condition);
				}) {
					setIpTo(target);
				}
				return null;
			}

			@Override
			public String jump(String target) {
				setIpTo(target);
				return null;
			}

			@Override
			public String push(int reg, int size) {
				int value = getRegValue(reg);
				AsmIRInterpreter.this.push(value, size);
				return null;
			}

			@Override
			public String pop(int reg, int size) {
				final int value = AsmIRInterpreter.this.pop(size);
				setReg(reg, value);
				return null;
			}

			@Override
			public String load(int valueReg, int valueSize, int pointerReg) {
				Utils.assertTrue(valueSize == 1);
				final Integer value = memAddressToValue.get(getRegValue(pointerReg));
				if (value == null) {
					throw new InterpretingFailedException("mem at " + reg1 + " read without writing");
				}
				setReg(valueReg, value);
				return null;
			}

			@Override
			public String store(int pointerReg, int valueReg, int valueSize) {
				Utils.assertTrue(valueSize == 1);
				memAddressToValue.put(getRegValue(pointerReg), getRegValue(valueReg));
				return null;
			}

			@Override
			public String ret() {
				if (callStack.isEmpty()) {
					ip = instructions.size();
				}
				else {
					ip = callStack.pop();
				}
				return null;
			}

			@Override
			public String command(String name, int reg1, int reg2) {
				switch (name) {
					case AsmIRConverter.CMD_ADD -> setReg(reg1, getRegValue(reg1) + getRegValue(reg2));
					case AsmIRConverter.CMD_SUB -> setReg(reg1, getRegValue(reg1) - getRegValue(reg2));
					case AsmIRConverter.CMD_IMUL -> setReg(reg1, getRegValue(reg1) * getRegValue(reg2));
					case AsmIRConverter.CMD_IDIV -> setReg(reg1, getRegValue(reg1) / getRegValue(reg2));
					case AsmIRConverter.CMD_IMOD -> setReg(reg1, getRegValue(reg1) % getRegValue(reg2));
					case AsmIRConverter.CMD_AND -> setReg(reg1, getRegValue(reg1) & getRegValue(reg2));
					case AsmIRConverter.CMD_SHR -> setReg(reg1, getRegValue(reg1) >> getRegValue(reg2));
					case AsmIRConverter.CMD_TEST -> flagZ = (getRegValue(reg1) & getRegValue(reg2) & 0xFF) == 0;
					case AsmIRConverter.CMD_LT -> setReg(reg1, getRegValue(reg1) < getRegValue(reg2) ? 1 : 0);
					case AsmIRConverter.CMD_LE -> setReg(reg1, getRegValue(reg1) <= getRegValue(reg2) ? 1 : 0);
					case AsmIRConverter.CMD_GE -> setReg(reg1, getRegValue(reg1) >= getRegValue(reg2) ? 1 : 0);
					case AsmIRConverter.CMD_GT -> setReg(reg1, getRegValue(reg1) > getRegValue(reg2) ? 1 : 0);
					case AsmIRConverter.CMD_MEM -> setReg(reg1, mem);
					case AsmIRConverter.CMD_PRINT -> {
						Utils.assertTrue(reg1 == AsmIRConverter.REG_0);
						buffer.append(getRegValue(reg1));
						buffer.append(" ");
					}
					case AsmIRConverter.CMD_PRINT_STRING -> {
						for (int ptr = getRegValue(reg1), size = getRegValue(reg2); size-- > 0;) {
							final Integer value = memAddressToValue.get(ptr);
							buffer.append((char) value.intValue());
							ptr++;
						}
					}
					default -> {
						final Integer ip = labelToIndex.get(name + "_0");
						if (ip == null) {
							throw new InterpretingFailedException("Unknown command " + name);
						}
						callStack.push(AsmIRInterpreter.this.ip);
						AsmIRInterpreter.this.ip = ip;
					}
				}
				return null;
			}
		};
	}

	private int pop(int size) {
		int value = 0;
		do {
			final int data = dataStack.pop();
			Utils.assertTrue(data == (data & 0xFF));
			value <<= 8;
			value |= data;
			size--;
		}
		while (size > 0);
		return value;
	}

	private void push(int value, int size) {
		do {
			dataStack.push(value & 0xFF);
			value >>= 8;
			size--;
		}
		while (size > 0);
	}

	private int getRegValue(int reg) {
		return switch (reg) {
			case AsmIRConverter.REG_0 -> reg0;
			case AsmIRConverter.REG_1 -> reg1;
			case AsmIRConverter.REG_2 -> reg2;
			default -> throw new IllegalStateException("unsupported reg " + reg);
		};
	}

	private void setReg(int reg, int value) {
		switch (reg) {
			case AsmIRConverter.REG_0 -> reg0 = value;
			case AsmIRConverter.REG_1 -> reg1 = value;
			case AsmIRConverter.REG_2 -> reg2 = value;
			default -> throw new IllegalStateException("unsupported reg " + reg);
		}
	}

	public List<Object> run(List<Object> args) {
		ip = startIp;
		buffer.setLength(0);

		dataStack.clear();
		for (Object arg : args) {
			if (arg instanceof Integer value) {
				push(value, 2);
			}
			else if (arg instanceof Boolean value) {
				push(value ? 1 : 0, 1);
			}
			else {
				throw new IllegalStateException("Unsupported arg " + arg);
			}
		}

		while (ip < instructions.size()) {
			final AsmIR instruction = instructions.get(ip);
			ip++;
			final String error = instruction.visit(visitor);
			if (error != null) {
				System.out.println("Error at " + ip + ": " + error);
				break;
			}
		}

		return new ArrayList<>(dataStack);
	}

	public String getOutput() {
		return buffer.toString();
	}

	private void setIpTo(String target) {
		if (!labelToIndex.containsKey(target)) {
			throw new InterpretingFailedException("Label " + target + " not found");
		}
		ip = labelToIndex.get(target);
	}

	private static void convertToIR(Function function, TypeCheckerImpl typeChecker, AsmIRStringLiterals stringLiterals, List<AsmIR> programInstructions) {
		final AsmIRFunction irFunction = AsmIRConverter.convertToIR(function, typeChecker, stringLiterals);
		programInstructions.add(AsmIR.label(irFunction.name() + "_0"));
		programInstructions.addAll(irFunction.instructions());
	}

	public static final class InterpretingFailedException extends RuntimeException {
		public InterpretingFailedException(String message) {
			super(message);
		}
	}
}
