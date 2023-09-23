package de.regnis.ts4th;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.jetbrains.annotations.*;

/**
 * @author Thomas Singer
 */
public class AsmIRInterpreter {

	public static void main(String[] args) throws IOException {
		final Path forthFile = Paths.get(args[0]);

		final List<Declaration> declarations = Parser.parseFile(forthFile);
		final Program program = Program.fromDeclarations(declarations);
		final TypeChecker typeChecker = createTypeChecker(program);

		final AsmIRStringLiterals stringLiterals = new AsmIRStringLiterals();
		final List<AsmIR> programInstructions = new ArrayList<>();
		int startIp = 0;
		for (Function function : program.functions()) {
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
		final List<Declaration> declarations = Parser.parseString(code);
		final Program program = Program.fromDeclarations(declarations);

		final TypeChecker typeChecker = createTypeChecker(program);

		final AsmIRStringLiterals stringLiterals = new AsmIRStringLiterals();
		final List<AsmIR> programInstructions = new ArrayList<>();
		int startIp = 0;
		for (Function function : program.functions()) {
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
			if (instruction instanceof AsmIR.Label(String name)) {
				if (labelToIndex.put(name, index) != null) {
					throw new IllegalStateException("duplicate label " + name);
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
			process(instruction);
		}

		return new ArrayList<>(dataStack);
	}

	public String getOutput() {
		return buffer.toString();
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

	private void process(AsmIR instruction) {
		if (instruction instanceof AsmIR.Label) {
			return;
		}

		if (instruction instanceof AsmIR.IntLiteral(int target, int value)) {
			setReg(target, value);
			return;
		}

		if (instruction instanceof AsmIR.BoolLiteral(int targetReg, boolean value)) {
			setReg(targetReg, value ? 1 : 0);
			return;
		}

		if (instruction instanceof AsmIR.StringLiteral(int targetReg, int index)) {
			setReg(targetReg, stringIndexToMem.get(index));
			return;
		}

		if (instruction instanceof AsmIR.Jump(AsmIR.Condition condition, String target)) {
			if (condition == null || switch (condition) {
				case z -> flagZ;
				case nz -> !flagZ;
				default -> throw new IllegalStateException("unsupported condition " + condition);
			}) {
				setIpTo(target);
			}
			return;
		}

		if (instruction instanceof AsmIR.Push(int sourceReg, int size)) {
			int value = getRegValue(sourceReg);
			AsmIRInterpreter.this.push(value, size);
			return;
		}

		if (instruction instanceof AsmIR.Pop(int targetReg, int size)) {
			final int value = AsmIRInterpreter.this.pop(size);
			setReg(targetReg, value);
			return;
		}

		if (instruction instanceof AsmIR.Move(int targetReg, int sourceReg, _)) {
			setReg(targetReg, getRegValue(sourceReg));
			return;
		}

		if (instruction instanceof AsmIR.Load(int valueReg, int pointerReg, int valueSize)) {
			Utils.assertTrue(valueSize == 1);
			final Integer value = memAddressToValue.get(getRegValue(pointerReg));
			if (value == null) {
				throw new InterpretingFailedException("mem at " + reg1 + " read without writing");
			}
			setReg(valueReg, value);
			return;
		}

		if (instruction instanceof AsmIR.Store(int pointerReg, int valueReg, int valueSize)) {
			Utils.assertTrue(valueSize == 1);
			memAddressToValue.put(getRegValue(pointerReg), getRegValue(valueReg));
			return;
		}

		if (instruction instanceof AsmIR.Ret) {
			if (callStack.isEmpty()) {
				ip = instructions.size();
			}
			else {
				ip = callStack.pop();
			}
			return;
		}

		//noinspection PatternVariableHidesField
		if (instruction instanceof AsmIR.BinCommand(AsmIR.BinOperation operation, int reg1, int reg2)) {
			switch (operation) {
			case add, add_ptr -> setReg(reg1, getRegValue(reg1) + getRegValue(reg2));
			case sub -> setReg(reg1, getRegValue(reg1) - getRegValue(reg2));
			case imul -> setReg(reg1, getRegValue(reg1) * getRegValue(reg2));
			case idiv -> setReg(reg1, getRegValue(reg1) / getRegValue(reg2));
			case imod -> setReg(reg1, getRegValue(reg1) % getRegValue(reg2));
			case and -> setReg(reg1, getRegValue(reg1) & getRegValue(reg2));
			case or -> setReg(reg1, getRegValue(reg1) | getRegValue(reg2));
			case xor -> setReg(reg1, getRegValue(reg1) ^ getRegValue(reg2));
			case shl -> setReg(reg1, getRegValue(reg1) << getRegValue(reg2));
			case shr -> setReg(reg1, getRegValue(reg1) >> getRegValue(reg2));
			case boolTest -> flagZ = (getRegValue(reg1) & getRegValue(reg2) & 0xFF) == 0;
			case lt -> setReg(reg1, getRegValue(reg1) < getRegValue(reg2) ? 1 : 0);
			case le -> setReg(reg1, getRegValue(reg1) <= getRegValue(reg2) ? 1 : 0);
			case eq -> setReg(reg1, getRegValue(reg1) == getRegValue(reg2) ? 1 : 0);
			case neq -> setReg(reg1, getRegValue(reg1) != getRegValue(reg2) ? 1 : 0);
			case ge -> setReg(reg1, getRegValue(reg1) >= getRegValue(reg2) ? 1 : 0);
			case gt -> setReg(reg1, getRegValue(reg1) > getRegValue(reg2) ? 1 : 0);
			default -> throw new IllegalStateException();
			}
			return;
		}

		if (instruction instanceof AsmIR.PrintInt) {
			buffer.append(reg0);
			buffer.append(" ");
			return;
		}

		if (instruction instanceof AsmIR.PrintString(int ptrReg, int sizeReg)) {
			for (int ptr = getRegValue(ptrReg), size = getRegValue(sizeReg); size-- > 0; ) {
				final Integer value = memAddressToValue.get(ptr);
				buffer.append((char)value.intValue());
				ptr++;
			}
			return;
		}

		if (instruction instanceof AsmIR.Mem) {
			setReg(AsmIRConverter.REG_0, mem);
			return;
		}

		if (instruction instanceof AsmIR.Call(String name)) {
			final Integer ip = labelToIndex.get(name + "_0");
			if (ip == null) {
				throw new InterpretingFailedException("Unknown command " + name);
			}
			callStack.push(AsmIRInterpreter.this.ip);
			AsmIRInterpreter.this.ip = ip;
			return;
		}

		throw new IllegalStateException("not implemented " + instruction);
	}

	private void setIpTo(String target) {
		if (!labelToIndex.containsKey(target)) {
			throw new InterpretingFailedException("Label " + target + " not found");
		}
		ip = labelToIndex.get(target);
	}

	@NotNull
	private static TypeChecker createTypeChecker(Program program) {
		final TypeCheckerImpl typeChecker = new TypeCheckerImpl();
		program.functions().forEach(function -> typeChecker.add(function.name(), function.typesInOut()));
		return typeChecker;
	}

	private static void convertToIR(Function function, TypeChecker typeChecker, AsmIRStringLiterals stringLiterals, List<AsmIR> programInstructions) {
		final AsmIRFunction irFunction = AsmIRConverter.convertToIR(function, typeChecker, stringLiterals);
		programInstructions.add(AsmIRFactory.label(irFunction.name() + "_0"));
		programInstructions.addAll(irFunction.instructions());
	}
}
