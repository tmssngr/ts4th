package de.regnis.ts4th;

import org.jetbrains.annotations.*;

import java.util.*;

/**
 * @author Thomas Singer
 */
public class AsmIR {

	public static AsmIR label(String name) {
		return new AsmIR(Kind.label, name, null, 0, 0);
	}

	public static AsmIR literal(int value) {
		return new AsmIR(Kind.literal, null, null, value, 0);
	}

	public static AsmIR literal(boolean value) {
		return new AsmIR(Kind.boolLiteral, null, null, value ? 1 : 0, 0);
	}

	public static AsmIR stringLiteral(int constantIndex) {
		return new AsmIR(Kind.stringLiteral, null, null, constantIndex, 0);
	}

	public static AsmIR push(int reg, int size) {
		return new AsmIR(Kind.push, null, null, reg, size);
	}

	public static AsmIR pop(int reg, int size) {
		return new AsmIR(Kind.pop, null, null, reg, size);
	}

	public static AsmIR load(int valueReg, int pointerReg, int valueSize) {
		return new AsmIR(Kind.load, null, null, valueReg + (valueSize << 16), pointerReg);
	}

	public static AsmIR store(int pointerReg, int valueReg, int valueSize) {
		return new AsmIR(Kind.store, null, null, pointerReg, valueReg + (valueSize << 16));
	}

	public static AsmIR jump(String target) {
		return new AsmIR(Kind.jump, target, null, 0, 0);
	}

	public static AsmIR jump(Condition condition, String target) {
		return new AsmIR(Kind.jump, target, condition, 0, 0);
	}

	public static AsmIR command(String name, int reg1, int reg2) {
		return new AsmIR(Kind.command, name, null, reg1, reg2);
	}

	public static AsmIR ret() {
		return new AsmIR(Kind.ret, null, null, 0, 0);
	}

	private final Kind kind;
	private final String s;
	private final Condition condition;
	private final int i1;
	private final int i2;

	private AsmIR(Kind kind, String s, Condition condition, int i1, int i2) {
		this.kind = kind;
		this.s = s;
		this.condition = condition;
		this.i1 = i1;
		this.i2 = i2;
	}

	@Override
	public String toString() {
		return switch (kind) {
			case label -> "label " + s;
			case literal -> "literal " + i1;
			case boolLiteral -> "literal " + (i1 == 1);
			case stringLiteral -> "constant " + i1;
			case push -> "push " + i1 + ", " + i2;
			case pop -> "pop "  + i1 + ", " + i2;
			case load -> "load " + (i1 & 0xFF) + "(" + (i1 >> 16) + "), @" + i2;
			case store -> "store @" + i1 + ", " + (i2 & 0xFF) + "(" + (i2 >> 16) + ")";
			case jump -> condition != null ? "jump " + condition + ", " + s : "jump " + s;
			case ret -> "ret";
			case command -> "command " + s + ", " + i1 + ", " + i2;
		};
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		AsmIR asmIR = (AsmIR) o;
		return i1 == asmIR.i1 && i2 == asmIR.i2 && kind == asmIR.kind && Objects.equals(s, asmIR.s) && condition == asmIR.condition;
	}

	@Override
	public int hashCode() {
		return Objects.hash(kind, s, condition, i1, i2);
	}

	@Nullable
	public String getLabel() {
		return kind == Kind.label ? s : null;
	}

	@Nullable
	public String getTarget() {
		return kind == Kind.jump ? s : null;
	}

	@Nullable
	public Condition getCondition() {
		return kind == Kind.jump ? condition : null;
	}

	public boolean isPush() {
		return kind == Kind.push;
	}

	public boolean isPop() {
		return kind == Kind.pop;
	}

	public int getPushPopReg() {
		return i1;
	}

	public int getPushPopSize() {
		return i2;
	}

	private enum Kind {
		label, literal, boolLiteral, stringLiteral, push, pop, jump, ret, load, store, command
	}

	public enum Condition {
		z, nz, lt, le, ge, gt
	}

	public <T> T visit(Visitor<T> visitor) {
		return switch (kind) {
			case label -> visitor.label(s);
			case literal -> visitor.literal(i1);
			case boolLiteral -> visitor.literal(i1 == 1);
			case stringLiteral -> visitor.stringLiteral(i1);
			case push -> visitor.push(i1, i2);
			case pop -> visitor.pop(i1, i2);
			case load -> visitor.load(i1 & 0xFF, i1 >> 16, i2);
			case store -> visitor.store(i1, i2 & 0xFF, i2 >> 16);
			case jump -> condition != null ? visitor.jump(condition, s) : visitor.jump(s);
			case ret -> visitor.ret();
			case command -> visitor.command(s, i1, i2);
		};
	}

	public interface Visitor<T> {

		T label(String name);

		T literal(int value);

		T literal(boolean value);

		T stringLiteral(int constantIndex);

		T jump(Condition condition, String target);

		T jump(String target);

		T push(int reg, int size);

		T pop(int reg, int size);

		T load(int valueReg, int valueSize, int pointerReg);

		T store(int pointerReg, int valueReg, int valueSize);

		T ret();

		T command(String name, int reg1, int reg2);
	}
}
