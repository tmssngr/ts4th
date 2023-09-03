package de.regnis.ts4th;

import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

/**
 * @author Thomas Singer
 */
public class AsmIRConverter {

	public static final int REG_0 = 0;
	public static final int REG_1 = 1;
	public static final int REG_2 = 2;
	public static final int PTR_SIZE = 8;
	public static final String CMD_TEST = "test";
	public static final String CMD_ADD = "add";
	public static final String CMD_ADD_PTR = "addPtr";
	public static final String CMD_SUB = "sub";
	public static final String CMD_IMUL = "mul";
	public static final String CMD_IDIV = "div";
	public static final String CMD_IMOD = "mod";
	public static final String CMD_AND = "and";
	public static final String CMD_OR = "or";
	public static final String CMD_XOR = "xor";
	public static final String CMD_SHL = "shl";
	public static final String CMD_SHR = "shr";
	public static final String CMD_LT = "lt";
	public static final String CMD_LE = "le";
	public static final String CMD_EQ = "eq";
	public static final String CMD_NE = "neq";
	public static final String CMD_GE = "ge";
	public static final String CMD_GT = "gt";
	public static final String CMD_MEM = BuiltinCommands.MEM;
	public static final String CMD_PRINT = BuiltinCommands.PRINT;
	public static final String CMD_PRINT_STRING = BuiltinCommands.PRINT_STRING;

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
		if (instruction instanceof Instruction.Label label) {
			output.accept(AsmIRFactory.label(label.name()));
		}
		else if (instruction instanceof Instruction.IntLiteral literal) {
			output.accept(AsmIRFactory.literal(literal.value()));
			output.accept(AsmIRFactory.push(REG_0, 2));
		}
		else if (instruction instanceof Instruction.BoolLiteral literal) {
			output.accept(AsmIRFactory.literal(literal.value()));
			output.accept(AsmIRFactory.push(REG_0, 1));
		}
		else if (instruction instanceof Instruction.StringLiteral literal) {
			final String text = literal.value();
			output.accept(AsmIRFactory.stringLiteral(stringLiterals.getConstantIndex(text)));
			output.accept(AsmIRFactory.push(REG_0, PTR_SIZE));
			output.accept(AsmIRFactory.literal(stringLiterals.getLength(text)));
			output.accept(AsmIRFactory.push(REG_0, 2));
		}
		else if (instruction instanceof Instruction.Jump jump) {
			output.accept(AsmIRFactory.jump(jump.target()));
		}
		else if (instruction instanceof Instruction.Branch branch) {
			output.accept(AsmIRFactory.pop(REG_0, 1));
			output.accept(AsmIRFactory.command(CMD_TEST, REG_0, REG_0));
			output.accept(AsmIRFactory.jump(AsmIR.Condition.z, branch.elseTarget()));
			output.accept(AsmIRFactory.jump(branch.ifTarget()));
		}
		else if (instruction instanceof Instruction.Ret) {
			output.accept(AsmIRFactory.ret());
		}
		else if (instruction instanceof Instruction.Command c) {
			final BuiltinCommands.Command command = BuiltinCommands.get(c.name());
			if (command != null) {
				command.toIR(types, output);
			}
			else {
				output.accept(AsmIRFactory.command(c.name(), 0, 0));
			}
		}
	}
}
