package de.regnis.ts4th;

import java.util.*;

import org.jetbrains.annotations.*;

import static de.regnis.ts4th.AsmIR.BinOperation.boolTest;

/**
 * @author Thomas Singer
 */
public class AsmIRConverter implements InstructionTypeEvaluator.Handler {

	public static final Logging DEFAULT_LOGGING = new ConsoleLogging(false);
	public static final int REG_0 = 0;
	public static final int REG_1 = 1;
	public static final int REG_2 = 2;

	@NotNull
	public static AsmIRFunction convertToIR(@NotNull Function function, @NotNull NameToFunction nameToFunction, @NotNull AsmIRStringLiterals stringLiterals, @NotNull Logging logging) {
		logging.beforeFunction(function);

		final List<Instruction> instructions = InstructionSimplifier.simplify(function.instructions());

		final AsmIRConverter converter = new AsmIRConverter(nameToFunction, function, stringLiterals, logging);

		InstructionTypeEvaluator.iterate(instructions, function.typesInOut().in(), converter);

		logging.afterFunction(function);

		final List<AsmIR> irInstructions = AsmIRSimplifier.simplify(converter.asmInstructions);
		return new AsmIRFunction(function.name(), stringLiterals, irInstructions);
	}

	private final List<AsmIR> asmInstructions = new ArrayList<>();
	private final NameToFunction nameToFunction;
	private final Function function;
	private final AsmIRStringLiterals stringLiterals;
	private final Logging logging;

	private LocalVarStack localVarStack;

	private AsmIRConverter(@NotNull NameToFunction nameToFunction, @NotNull Function function, @NotNull AsmIRStringLiterals stringLiterals, @NotNull Logging logging) {
		this.nameToFunction = nameToFunction;
		this.function = function;
		this.stringLiterals = stringLiterals;
		this.logging = logging;
	}

	@Override
	public Pair<TypeList, LocalVarStack> handle(Instruction instruction, TypeList input, LocalVarStack localVarStackInput) {
		logging.beforeInstruction(instruction, input, localVarStackInput);

		this.localVarStack = localVarStackInput;
		final TypeList output = process(instruction, input);

		logging.afterInstruction(instruction, output, localVarStack);

		return new Pair<>(output, localVarStack);
	}

	private TypeList process(Instruction instruction, TypeList input) {
		return switch (instruction) {
			case Instruction.Label(String name, _) -> {
				emit(AsmIRFactory.label(name));
				yield input;
			}
			case Instruction.IntLiteral(long value, Type type) -> {
				emit(AsmIRFactory.literal(REG_0, value, type));
				emit(AsmIRFactory.push(REG_0, type));
				yield input
						.append(type);
			}
			case Instruction.BoolLiteral(boolean value) -> {
				emit(AsmIRFactory.literal(REG_0, value));
				emit(AsmIRFactory.push(REG_0, Type.Bool));
				yield input
						.append(Type.Bool);
			}
			case Instruction.PtrLiteral(int index, String varName) -> {
				emit(AsmIRFactory.ptrLiteral(REG_0, index, varName));
				emit(AsmIRFactory.push(REG_0, Type.Ptr));
				yield input
						.append(Type.Ptr);
			}
			case Instruction.StringLiteral(String text) -> {
				final AsmIRStringLiterals.Entry entry = stringLiterals.addEntry(text);
				emit(AsmIRFactory.stringLiteral(REG_0, entry.index()));
				emit(AsmIRFactory.push(REG_0, Type.Ptr));
				emit(AsmIRFactory.literal(REG_0, entry.length(), Type.I16));
				emit(AsmIRFactory.push(REG_0, Type.I16));
				yield input
						.append(Type.Ptr)
						.append(Type.I16);
			}
			case Instruction.Jump(String target, _) -> {
				emit(AsmIRFactory.jump(target));
				yield input;
			}
			case Instruction.Branch(String ifTarget, String elseTarget, Location location) -> {
				if (input.type() != Type.Bool) {
					throw new InvalidTypeException(location, "Invalid types! Expected " + TypeList.BOOL + ", but got " + input);
				}

				emit(AsmIRFactory.pop(REG_0, Type.Bool));
				emit(AsmIRFactory.binCommand(boolTest, REG_0, REG_0, Type.I16));
				emit(AsmIRFactory.jump(AsmIR.Condition.z, elseTarget));
				emit(AsmIRFactory.jump(ifTarget));
				yield input.prev();
			}
			case Instruction.Ret _ -> {
				final TypeList expectedOut = function.typesInOut().out();
				if (!expectedOut.equals(input)) {
					throw new InvalidTypeException(function.locationEnd(), "Function `" + function.name() + "` returns " + input + " but is expected to return " + expectedOut);
				}

				emit(AsmIRFactory.ret());
				yield input;
			}
			case Instruction.Command(String name, Location location) -> {
				final Intrinsics.Command command = Intrinsics.get(name);
				if (command != null) {
					final TypeList out = command.process(name, location, input);
					command.toIR(name, input, this::emit);
					yield out;
				}

				final Function function = nameToFunction.get(name);
				if (function != null) {
					final TypesInOut types = function.typesInOut();
					final TypeList out = input.transform(types.in(), types.out());
					if (out == null) {
						throw new InvalidTypeException(location, "Invalid types for command " + name + "! Expected " + types.in() + " but got " + input);
					}

					emit(AsmIRFactory.call(name));
					yield out;
				}

				final String writeSuffix = "!";
				final boolean isWrite = name.endsWith(writeSuffix);
				if (isWrite) {
					name = name.substring(0, name.length() - 1);
				}

				TypeList offset = TypeList.EMPTY;
				for (LocalVarStack var = localVarStack; var != null; var = var.prev()) {
					final Type type = var.type();
					if (var.name().equals(name)) {
						if (isWrite) {
							if (input.size() == 0) {
								throw new InvalidTypeException(location, "Variable " + name + " is of type " + type + ", but the stack is empty.");
							}
							if (!Objects.equals(input.type(), type)) {
								throw new InvalidTypeException(location, "Variable " + name + " is of type " + type + ", but got " + input);
							}

							emit(AsmIRFactory.pop(REG_0, type));
							emit(AsmIRFactory.localVarWrite(REG_0, type, offset));
							yield input.prev();
						}
						else {
							emit(AsmIRFactory.localVarRead(REG_0, type, offset));
							emit(AsmIRFactory.push(REG_0, type));
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
					localVarStack = new LocalVarStack(name, type, localVarStack);

					emit(AsmIRFactory.pop(REG_0, type));
					emit(AsmIRFactory.pushVar(REG_0, type));

					types = types.prev();
				}

				yield types;
			}
			case Instruction.ReleaseVars(int count, Location location) -> {
				if (localVarStack == null) {
					throw new IllegalStateException(STR. "\{ location } Should release \{ count } local variables, but don't have one" );
				}

				final int originalCount = count;
				final LocalVarStack originalLocalVarStack = localVarStack;
				final int localVarCount = originalLocalVarStack.size();

				TypeList types = TypeList.EMPTY;
				while (count-- > 0) {
					if (localVarStack == null) {
						throw new IllegalStateException(STR. "\{ location } Should release \{ originalCount } local variables, but only have \{ localVarCount } (\{ originalLocalVarStack })" );
					}
					types = types.append(localVarStack.type());
					localVarStack = localVarStack.prev();
				}
				emit(AsmIRFactory.dropVar(types));
				yield input;
			}
		};
	}

	private void emit(AsmIR ir) {
		logging.handleIR(ir);
		asmInstructions.add(ir);
	}

	public interface Logging {
		void beforeFunction(Function function);

		void beforeInstruction(Instruction instruction, TypeList input, @Nullable LocalVarStack localVarStackInput);

		void handleIR(AsmIR asmIR);

		void afterInstruction(Instruction instruction, TypeList output, @Nullable LocalVarStack localVarStack);

		void afterFunction(Function function);
	}

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
		public void beforeInstruction(Instruction instruction, TypeList input, LocalVarStack localVarStackInput) {
			if (logging) {
				printIndentation();
				System.out.print("; [");
				System.out.print(input);
				System.out.print("]");
				if (localVarStackInput != null) {
					System.out.print(" ");
					System.out.print(localVarStackInput);
				}
				System.out.println();

				if (!(instruction instanceof Instruction.Label)) {
					printIndentation();
					System.out.print("; ");
					System.out.print(instruction);
					System.out.println();
				}
			}
		}

		@Override
		public void handleIR(AsmIR asmIR) {
			if (logging) {
				if (asmIR instanceof AsmIR.Label) {
					System.out.println(asmIR);
				}
				else {
					printIndentation();
					System.out.println(asmIR);

					if (asmIR instanceof AsmIR.Jump(AsmIR.Condition condition, _) && condition == null) {
						System.out.println();
					}
				}
			}
		}

		@Override
		public void afterInstruction(Instruction instruction, TypeList output, LocalVarStack localVarStack) {
		}

		@Override
		public void afterFunction(Function function) {
			if (logging) {
				System.out.println();
			}
		}

		private void printIndentation() {
			System.out.print("    ");
		}
	}
}
