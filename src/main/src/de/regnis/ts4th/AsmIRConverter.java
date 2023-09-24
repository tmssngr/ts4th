package de.regnis.ts4th;

import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

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
	public static AsmIRFunction convertToIR(Function function, TypeChecker typeChecker, AsmIRStringLiterals stringLiterals) {
		final CfgFunction cfgFunction = new CfgFunction(function);
		cfgFunction.checkTypes(typeChecker);

		final List<Instruction> instructions = cfgFunction.flatten();
		final List<Instruction> simplifiedInstructions = InstructionSimplifier.simplify(instructions);

		final TypeList[] types = TypeCheckerImpl.determineTypes(simplifiedInstructions, function.typesInOut().in(), typeChecker);

		final List<AsmIR> asmInstructions = new ArrayList<>();
		final AsmIRConverter converter = new AsmIRConverter(stringLiterals, asmInstructions::add);
		for (int i = 0; i < simplifiedInstructions.size(); i++) {
			final Instruction instruction = simplifiedInstructions.get(i);
			converter.types = types[i];
			if (converter.types != null) {
				converter.process(instruction);
			}
			else {
				System.out.println("Skipping instruction " + i + ": " + instruction);
			}
		}

		final List<AsmIR> irInstructions = AsmIRSimplifier.simplify(asmInstructions);
		return new AsmIRFunction(function.name(), stringLiterals, irInstructions);
	}

	private final AsmIRStringLiterals stringLiterals;
	private final Consumer<AsmIR> output;

	private TypeList types;

	public AsmIRConverter(@NotNull AsmIRStringLiterals stringLiterals, @NotNull Consumer<AsmIR> output) {
		this.stringLiterals = stringLiterals;
		this.output = output;
	}

	public void process(Instruction instruction) {
		switch (instruction) {
		case Instruction.Label(String name, _) -> output.accept(AsmIRFactory.label(name));
		case Instruction.IntLiteral(int value) -> {
			output.accept(AsmIRFactory.literal(REG_0, value));
			output.accept(AsmIRFactory.push(REG_0, 2));
		}
		case Instruction.BoolLiteral(boolean value) -> {
			output.accept(AsmIRFactory.literal(REG_0, value));
			output.accept(AsmIRFactory.push(REG_0, 1));
		}
		case Instruction.PtrLiteral(int index, String varName) -> {
			output.accept(AsmIRFactory.ptrLiteral(REG_0, index, varName));
			output.accept(AsmIRFactory.push(REG_0, PTR_SIZE));
		}
		case Instruction.StringLiteral(String text) -> {
			final AsmIRStringLiterals.Entry entry = stringLiterals.addEntry(text);
			output.accept(AsmIRFactory.stringLiteral(REG_0, entry.index()));
			output.accept(AsmIRFactory.push(REG_0, PTR_SIZE));
			output.accept(AsmIRFactory.literal(REG_0, entry.length()));
			output.accept(AsmIRFactory.push(REG_0, 2));
		}
		case Instruction.Jump(String target, _) -> output.accept(AsmIRFactory.jump(target));
		case Instruction.Branch(String ifTarget, String elseTarget, _) -> {
			output.accept(AsmIRFactory.pop(REG_0, 1));
			output.accept(AsmIRFactory.binCommand(boolTest, REG_0, REG_0));
			output.accept(AsmIRFactory.jump(AsmIR.Condition.z, elseTarget));
			output.accept(AsmIRFactory.jump(ifTarget));
		}
		case Instruction.Ret _ -> output.accept(AsmIRFactory.ret());
		case Instruction.Command(String name, _) -> {
			final BuiltinCommands.Command command = BuiltinCommands.get(name);
			if (command != null) {
				command.toIR(name, types, output);
			}
			else {
				output.accept(AsmIRFactory.call(name));
			}
		}
		}
	}
}
