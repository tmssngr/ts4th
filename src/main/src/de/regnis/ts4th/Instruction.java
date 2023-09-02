package de.regnis.ts4th;

import org.jetbrains.annotations.*;

import java.util.*;

/**
 * @author Thomas Singer
 */
public class Instruction {

	public static Instruction label(String name) {
		return new Instruction(Kind.label, name, null, 0);
	}

	public static Instruction literal(int value) {
		return new Instruction(Kind.intLiteral, null, null, value);
	}

	public static Instruction literal(boolean value) {
		return new Instruction(Kind.boolLiteral, null, null, value ? 1 : 0);
	}

	public static Instruction literal(String text) {
		return new Instruction(Kind.stringLiteral, text, null, 0);
	}

	public static Instruction command(String name) {
		return new Instruction(Kind.command, name, null, 0);
	}

	public static Instruction jump(String target) {
		return new Instruction(Kind.jump, target, null, 0);
	}

	public static Instruction branch(String ifTarget, String elseTarget) {
		return new Instruction(Kind.branch, ifTarget, elseTarget, 0);
	}

	public static Instruction ret() {
		return new Instruction(Kind.ret, null, null, 0);
	}

	private final Kind kind;
	private final String s1;
	private final String s2;
	private final int value;

	private Instruction(Kind kind, String s1, String s2, int value) {
		this.kind = kind;
		this.s1 = s1;
		this.s2 = s2;
		this.value = value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(kind, s1, s2, value);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Instruction that = (Instruction) o;
		return value == that.value && kind == that.kind && Objects.equals(s1, that.s1) && Objects.equals(s2, that.s2);
	}

	@Override
	public String toString() {
		return switch (kind) {
			case label -> s1 + ":";
			case intLiteral -> String.valueOf(value);
			case boolLiteral -> String.valueOf(value != 0);
			case stringLiteral -> "\"" + s1 + "\"";
			case command -> s1;
			case jump -> "jump " + s1;
			case branch -> "branch " + s1 + ", " + s2;
			case ret -> "ret";
		};
	}

	public boolean isLabel() {
		return kind == Kind.label;
	}

	@Nullable
	public String getLabel() {
		return kind == Kind.label
				? s1 : null;
	}

	public boolean isJump() {
		return kind == Kind.jump;
	}

	public boolean isBranch() {
		return kind == Kind.branch;
	}

	public List<String> getTargets() {
		return switch (kind) {
			case jump -> List.of(s1);
			case branch -> List.of(s1, s2);
			default -> List.of();
		};
	}

	@Nullable
	public Boolean getBoolLiteral() {
		return kind == Kind.boolLiteral ? value != 0 : null;
	}

	@Nullable
	public String getCommand() {
		return kind == Kind.command ? s1 : null;
	}

	public <T> T visit(Visitor<T> visitor) {
		return switch (kind) {
			case label -> visitor.label(s1);
			case intLiteral -> visitor.literal(value);
			case boolLiteral -> visitor.literal(value != 0);
			case stringLiteral -> visitor.literal(s1);
			case command -> visitor.command(s1);
			case jump -> visitor.jump(s1);
			case branch -> visitor.branch(s1, s2);
			case ret -> visitor.ret();
		};
	}

	private enum Kind {
		label, intLiteral, boolLiteral, command, jump, branch, stringLiteral, ret
	}

	public interface Visitor<T> {

		T label(String name);

		T literal(int value);

		T literal(boolean value);

		T literal(String text);

		T command(String name);

		T jump(String target);

		T branch(String ifTarget, String elseTarget);

		T ret();
	}
}
