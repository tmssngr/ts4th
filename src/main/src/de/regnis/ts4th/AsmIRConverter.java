package de.regnis.ts4th;

import java.util.*;
import java.util.function.*;

import org.jetbrains.annotations.*;

import static de.regnis.ts4th.AsmIR.BinOperation.boolTest;

/**
 * @author Thomas Singer
 */
public class AsmIRConverter {

	public static final Logging DEFAULT_LOGGING = new ConsoleLogging(false);
	public static final int REG_0 = 0;
	public static final int REG_1 = 1;
	public static final int REG_2 = 2;

	@NotNull
	public static AsmIRFunction convertToIR(@NotNull Function function, @NotNull NameToFunction nameToFunction, @NotNull AsmIRStringLiterals stringLiterals, @NotNull Logging logging) {
		logging.beforeFunction(function);

		final List<Instruction> instructions = InstructionSimplifier.simplify(function.instructions());

		final List<AsmIR> asmInstructions = new ArrayList<>();
		final AsmIRConverter converter = new AsmIRConverter(nameToFunction, function, stringLiterals, ir -> {
			logging.handleIR(ir);
			asmInstructions.add(ir);
		});

		InstructionTypeEvaluator.iterate(instructions, function.typesInOut().in(), (instruction, input) -> {
			logging.beforeInstruction(instruction, input);
			final TypeList output = converter.process(instruction, input);
			logging.afterInstruction(instruction, output);
			return output;
		});

		logging.afterFunction(function);

		final List<AsmIR> irInstructions = AsmIRSimplifier.simplify(asmInstructions);
		return new AsmIRFunction(function.name(), stringLiterals, irInstructions);
	}

	private final List<LocalVar> vars = new ArrayList<>();
	private final NameToFunction nameToFunction;
	private final Function function;
	private final AsmIRStringLiterals stringLiterals;
	private final Consumer<AsmIR> output;

	private AsmIRConverter(@NotNull NameToFunction nameToFunction, @NotNull Function function, @NotNull AsmIRStringLiterals stringLiterals, @NotNull Consumer<AsmIR> output) {
		this.nameToFunction = nameToFunction;
		this.function = function;
		this.stringLiterals = stringLiterals;
		this.output = output;
	}

	public TypeList process(Instruction instruction, TypeList input) {
		return switch (instruction) {
			case Instruction.Label(String name, _) -> {
				output.accept(AsmIRFactory.label(name));
				yield input;
			}
			case Instruction.IntLiteral(int value, Type type) -> {
				output.accept(AsmIRFactory.literal(REG_0, value, type));
				output.accept(AsmIRFactory.push(REG_0, type));
				yield input
						.append(type);
			}
			case Instruction.BoolLiteral(boolean value) -> {
				output.accept(AsmIRFactory.literal(REG_0, value));
				output.accept(AsmIRFactory.push(REG_0, Type.Bool));
				yield input
						.append(Type.Bool);
			}
			case Instruction.PtrLiteral(int index, String varName) -> {
				output.accept(AsmIRFactory.ptrLiteral(REG_0, index, varName));
				output.accept(AsmIRFactory.push(REG_0, Type.Ptr));
				yield input
						.append(Type.Ptr);
			}
			case Instruction.StringLiteral(String text) -> {
				final AsmIRStringLiterals.Entry entry = stringLiterals.addEntry(text);
				output.accept(AsmIRFactory.stringLiteral(REG_0, entry.index()));
				output.accept(AsmIRFactory.push(REG_0, Type.Ptr));
				output.accept(AsmIRFactory.literal(REG_0, entry.length(), Type.I16));
				output.accept(AsmIRFactory.push(REG_0, Type.I16));
				yield input
						.append(Type.Ptr)
						.append(Type.I16);
			}
			case Instruction.Jump(String target, _) -> {
				output.accept(AsmIRFactory.jump(target));
				yield input;
			}
			case Instruction.Branch(String ifTarget, String elseTarget, Location location) -> {
				if (input.type() != Type.Bool) {
					throw new InvalidTypeException(location, "Invalid types! Expected " + TypeList.BOOL + ", but got " + input);
				}

				output.accept(AsmIRFactory.pop(REG_0, Type.Bool));
				output.accept(AsmIRFactory.binCommand(boolTest, REG_0, REG_0, Type.I16));
				output.accept(AsmIRFactory.jump(AsmIR.Condition.z, elseTarget));
				output.accept(AsmIRFactory.jump(ifTarget));
				yield input.prev();
			}
			case Instruction.Ret _ -> {
				final TypeList expectedOut = function.typesInOut().out();
				if (!expectedOut.equals(input)) {
					throw new InvalidTypeException(function.locationEnd(), "Function `" + function.name() + "` returns " + input + " but is expected to return " + expectedOut);
				}

				output.accept(AsmIRFactory.ret());
				yield input;
			}
			case Instruction.Command(String name, Location location) -> {
				final Intrinsics.Command command = Intrinsics.get(name);
				if (command != null) {
					final TypeList out = command.process(name, location, input);
					command.toIR(name, input, output);
					yield out;
				}

				final Function function = nameToFunction.get(name);
				if (function != null) {
					final TypesInOut types = function.typesInOut();
					final TypeList out = input.transform(types.in(), types.out());
					if (out == null) {
						throw new InvalidTypeException(location, "Invalid types for command " + name + "! Expected " + types.in() + " but got " + input);
					}

					output.accept(AsmIRFactory.call(name));
					yield out;
				}

				final String writeSuffix = "!";
				final boolean isWrite = name.endsWith(writeSuffix);
				if (isWrite) {
					name = name.substring(0, name.length() - 1);
				}

				TypeList offset = TypeList.EMPTY;
				for (ListIterator<LocalVar> it = vars.listIterator(vars.size()); it.hasPrevious(); ) {
					final LocalVar var = it.previous();
					final Type type = var.type();
					if (var.name().equals(name)) {
						if (isWrite) {
							if (input.size() == 0) {
								throw new InvalidTypeException(location, "Variable " + name + " is of type " + type + ", but the stack is empty.");
							}
							if (!Objects.equals(input.type(), type)) {
								throw new InvalidTypeException(location, "Variable " + name + " is of type " + type + ", but got " + input);
							}

							output.accept(AsmIRFactory.pop(REG_0, type));
							output.accept(AsmIRFactory.localVarWrite(REG_0, type, offset));
							yield input.prev();
						}
						else {
							output.accept(AsmIRFactory.localVarRead(REG_0, type, offset));
							output.accept(AsmIRFactory.push(REG_0, type));
							yield input.append(type);
						}
					}
					offset = offset.append(type);
				}
				throw new CompilerException(location, STR. "Unknown command \{ name }" );
			}
			case Instruction.BindVars(List<String> varNames, Location location) -> {
				int varCount = varNames.size();
				final int inputCount = input.size();
				if (inputCount < varCount) {
					throw new InvalidTypeException(location, STR. "Expected \{ varCount } stack entries, but got only \{ inputCount }: \{ input }" );
				}

				final ListIterator<String> it = varNames.listIterator(varNames.size());
				TypeList types = input;
				while (it.hasPrevious()) {
					final String name = it.previous();
					if (Intrinsics.get(name) != null) {
						throw new CompilerException(location, STR. "There already is a built-in command defined with the name \{ name }." );
					}
					if (nameToFunction.get(name) != null) {
						throw new CompilerException(location, STR. "There already is a function defined with the name \{ name }." );
					}

					final Type type = types.type();
					vars.addLast(new LocalVar(name, type));

					output.accept(AsmIRFactory.pop(REG_0, type));
					output.accept(AsmIRFactory.pushVar(REG_0, type));

					types = types.prev();
				}

				yield types;
			}
			case Instruction.ReleaseVars(int count) -> {
				TypeList types = TypeList.EMPTY;
				while (count-- > 0) {
					final LocalVar var = vars.removeLast();
					types = types.append(var.type);
				}
				output.accept(AsmIRFactory.dropVar(types));
				yield input;
			}
		};
	}

	public interface Logging {
		void beforeFunction(Function function);

		void beforeInstruction(Instruction instruction, TypeList input);

		void handleIR(AsmIR asmIR);

		void afterInstruction(Instruction instruction, TypeList output);

		void afterFunction(Function function);
	}

	private record LocalVar(@NotNull String name, @NotNull Type type) {}

	private static class ConsoleLogging implements Logging {
		private final boolean logging;

		public ConsoleLogging(boolean logging) {
			this.logging = logging;
		}

		@Override
		public void beforeFunction(Function function) {
			if (logging) {
				System.out.println("fn " + function.name() + "(" + function.typesInOut().in() + " -- " + function.typesInOut().out() + ")");
			}
		}

		@Override
		public void beforeInstruction(Instruction instruction, TypeList input) {
			if (logging) {
				if (!(instruction instanceof Instruction.Label)) {
					printIndentation();
				}
				System.out.println("; " + instruction + " " + input);
			}
		}

		@Override
		public void handleIR(AsmIR asmIR) {
			if (logging) {
				if (!(asmIR instanceof AsmIR.Label)) {
					printIndentation();
				}
				System.out.println(asmIR);
			}
		}

		private void printIndentation() {
			System.out.print("    ");
		}

		@Override
		public void afterInstruction(Instruction instruction, TypeList output) {
		}

		@Override
		public void afterFunction(Function function) {
			if (logging) {
				System.out.println();
			}
		}
	}
}
