package de.regnis.ts4th;

import org.jetbrains.annotations.*;

/**
 * @author Thomas Singer
 */
public class InstructionFactory {

	public static Instruction label(String name) {
		return new Instruction.Label(name);
	}

	public static Instruction literal(int value) {
		return new Instruction.IntLiteral(value);
	}

	public static Instruction literal(boolean value) {
		return new Instruction.BoolLiteral(value);
	}

	public static Instruction jump(String target) {
		return new Instruction.Jump(target);
	}

	public static Instruction branch(String ifTarget, String elseTarget) {
		return new Instruction.Branch(ifTarget, elseTarget);
	}

	public static Instruction command(String name) {
		return new Instruction.Command(name);
	}

	public static Instruction ret() {
		return new Instruction.Ret();
	}

	@NotNull
	public static Instruction dup() {
		return command(BuiltinCommands.DUP);
	}

	public static Instruction dup2Int() {
		return command(BuiltinCommands.DUP2);
	}

	@NotNull
	public static Instruction dropInt() {
		return command(BuiltinCommands.DROP);
	}

	@NotNull
	public static Instruction swapInt() {
		return command(BuiltinCommands.SWAP);
	}

	@NotNull
	public static Instruction overInt() {
		return command(BuiltinCommands.OVER);
	}

	@NotNull
	public static Instruction add() {
		return command(BuiltinCommands.ADD);
	}

	@NotNull
	public static Instruction sub() {
		return command(BuiltinCommands.SUB);
	}

	@NotNull
	public static Instruction mul() {
		return command(BuiltinCommands.MUL);
	}

	@NotNull
	public static Instruction isLT() {
		return command(BuiltinCommands.IS_LT);
	}

	@NotNull
	public static Instruction isLE() {
		return command(BuiltinCommands.IS_LE);
	}

	@NotNull
	public static Instruction isEQ() {
		return command(BuiltinCommands.IS_EQ);
	}

	@NotNull
	public static Instruction isNE() {
		return command(BuiltinCommands.IS_NE);
	}

	@NotNull
	public static Instruction isGE() {
		return command(BuiltinCommands.IS_GE);
	}

	@NotNull
	public static Instruction isGT() {
		return command(BuiltinCommands.IS_GT);
	}

	@NotNull
	public static Instruction print() {
		return command(BuiltinCommands.PRINT);
	}
}
