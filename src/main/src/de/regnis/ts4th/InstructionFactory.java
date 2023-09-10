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
		return new Instruction.Command(BuiltinCommands.DUP);
	}

	public static Instruction dup2Int() {
		return new Instruction.Command(BuiltinCommands.DUP2);
	}

	@NotNull
	public static Instruction dropInt() {
		return new Instruction.Command(BuiltinCommands.DROP);
	}

	@NotNull
	public static Instruction swapInt() {
		return new Instruction.Command(BuiltinCommands.SWAP);
	}

	@NotNull
	public static Instruction overInt() {
		return new Instruction.Command(BuiltinCommands.OVER);
	}

	@NotNull
	public static Instruction add() {
		return new Instruction.Command(BuiltinCommands.ADD);
	}

	@NotNull
	public static Instruction sub() {
		return new Instruction.Command(BuiltinCommands.SUB);
	}

	@NotNull
	public static Instruction mul() {
		return new Instruction.Command(BuiltinCommands.MUL);
	}

	@NotNull
	public static Instruction isLT() {
		return new Instruction.Command(BuiltinCommands.IS_LT);
	}

	@NotNull
	public static Instruction isLE() {
		return new Instruction.Command(BuiltinCommands.IS_LE);
	}

	@NotNull
	public static Instruction isEQ() {
		return new Instruction.Command(BuiltinCommands.IS_EQ);
	}

	@NotNull
	public static Instruction isNE() {
		return new Instruction.Command(BuiltinCommands.IS_NE);
	}

	@NotNull
	public static Instruction isGE() {
		return new Instruction.Command(BuiltinCommands.IS_GE);
	}

	@NotNull
	public static Instruction isGT() {
		return new Instruction.Command(BuiltinCommands.IS_GT);
	}

	@NotNull
	public static Instruction print() {
		return new Instruction.Command(BuiltinCommands.PRINT);
	}
}
