package de.regnis.ts4th;

import java.util.*;

import org.jetbrains.annotations.*;

/**
 * @author Thomas Singer
 */
public class InstructionFactory {

	public static Instruction label(String name) {
		return new Instruction.Label(name);
	}

	public static Instruction literal(long value) {
		return literal(value, Type.I16);
	}

	public static Instruction.IntLiteral literal(long value, Type type) {
		return new Instruction.IntLiteral(value, type);
	}

	public static Instruction literal(boolean value) {
		return new Instruction.BoolLiteral(value);
	}

	public static Instruction literal(String value) {
		return new Instruction.StringLiteral(value);
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

	public static Instruction bindVars(List<String> names) {
		return new Instruction.BindVars(names, Location.DUMMY);
	}

	public static Instruction releaseVars(int count) {
		return new Instruction.ReleaseVars(count, Location.DUMMY);
	}

	@NotNull
	public static Instruction dup() {
		return command(Intrinsics.DUP);
	}

	public static Instruction dup2Int() {
		return command(Intrinsics.DUP2);
	}

	@NotNull
	public static Instruction dropInt() {
		return command(Intrinsics.DROP);
	}

	@NotNull
	public static Instruction swapInt() {
		return command(Intrinsics.SWAP);
	}

	@NotNull
	public static Instruction overInt() {
		return command(Intrinsics.OVER);
	}

	@NotNull
	public static Instruction add() {
		return command(Intrinsics.ADD);
	}

	@NotNull
	public static Instruction sub() {
		return command(Intrinsics.SUB);
	}

	@NotNull
	public static Instruction mul() {
		return command(Intrinsics.MUL);
	}

	@NotNull
	public static Instruction isLT() {
		return command(Intrinsics.IS_LT);
	}

	@NotNull
	public static Instruction inc() {
		return command(Intrinsics.INC);
	}

	@NotNull
	public static Instruction dec() {
		return command(Intrinsics.DEC);
	}

	@NotNull
	public static Instruction isLE() {
		return command(Intrinsics.IS_LE);
	}

	@NotNull
	public static Instruction isEQ() {
		return command(Intrinsics.IS_EQ);
	}

	@NotNull
	public static Instruction isNE() {
		return command(Intrinsics.IS_NE);
	}

	@NotNull
	public static Instruction isGE() {
		return command(Intrinsics.IS_GE);
	}

	@NotNull
	public static Instruction isGT() {
		return command(Intrinsics.IS_GT);
	}

	@NotNull
	public static Instruction print() {
		return command(Intrinsics.PRINT);
	}
}
