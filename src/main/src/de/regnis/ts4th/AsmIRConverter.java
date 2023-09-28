package de.regnis.ts4th;

import java.util.*;
import java.util.function.*;

import org.jetbrains.annotations.*;

import static de.regnis.ts4th.AsmIR.BinOperation.boolTest;

/**
 * @author Thomas Singer
 */
public class AsmIRConverter {

	public static final int REG_0 = 0;
	public static final int REG_1 = 1;
	public static final int REG_2 = 2;
	public static final int PTR_SIZE = 8;

	@NotNull
	public static AsmIRFunction convertToIR(@NotNull Function function, @NotNull NameToFunction nameToFunction, @NotNull AsmIRStringLiterals stringLiterals) {
		final List<Instruction> instructions = InstructionSimplifier.simplify(function.instructions());

		final List<AsmIR> asmInstructions = new ArrayList<>();
		final AsmIRConverter converter = new AsmIRConverter(nameToFunction, function, stringLiterals, asmInstructions::add);

		InstructionTypeEvaluator.iterate(instructions, function.typesInOut().in(), converter::process);

		final List<AsmIR> irInstructions = AsmIRSimplifier.simplify(asmInstructions);
		return new AsmIRFunction(function.name(), stringLiterals, irInstructions);
	}

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
			case Instruction.IntLiteral(int value) -> {
				output.accept(AsmIRFactory.literal(REG_0, value));
				output.accept(AsmIRFactory.push(REG_0, 2));
				yield input
						.append(Type.Int);
			}
			case Instruction.BoolLiteral(boolean value) -> {
				output.accept(AsmIRFactory.literal(REG_0, value));
				output.accept(AsmIRFactory.push(REG_0, 1));
				yield input
						.append(Type.Bool);
			}
			case Instruction.PtrLiteral(int index, String varName) -> {
				output.accept(AsmIRFactory.ptrLiteral(REG_0, index, varName));
				output.accept(AsmIRFactory.push(REG_0, PTR_SIZE));
				yield input
						.append(Type.Ptr);
			}
			case Instruction.StringLiteral(String text) -> {
				final AsmIRStringLiterals.Entry entry = stringLiterals.addEntry(text);
				output.accept(AsmIRFactory.stringLiteral(REG_0, entry.index()));
				output.accept(AsmIRFactory.push(REG_0, PTR_SIZE));
				output.accept(AsmIRFactory.literal(REG_0, entry.length()));
				output.accept(AsmIRFactory.push(REG_0, 2));
				yield input
						.append(Type.Ptr)
						.append(Type.Int);
			}
			case Instruction.Jump(String target, _) -> {
				output.accept(AsmIRFactory.jump(target));
				yield input;
			}
			case Instruction.Branch(String ifTarget, String elseTarget, Location location) -> {
				if (input.type() != Type.Bool) {
					throw new InvalidTypeException(location, "Invalid types! Expected " + TypeList.BOOL + ", but got " + input);
				}

				output.accept(AsmIRFactory.pop(REG_0, 1));
				output.accept(AsmIRFactory.binCommand(boolTest, REG_0, REG_0));
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
				final BuiltinCommands.Command command = BuiltinCommands.get(name);
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

				throw new CompilerException(location, STR. "Unknown command \{ name }" );
			}
		};
	}
}
