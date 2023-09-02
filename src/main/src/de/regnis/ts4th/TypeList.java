package de.regnis.ts4th;

import org.jetbrains.annotations.*;

/**
 * @author Thomas Singer
 */
public record TypeList(TypeList prev, Type type) {

	public static final TypeList EMPTY = new TypeList(null, null);
	public static final TypeList INT = new TypeList(EMPTY, Type.Int);
	public static final TypeList INT2 = new TypeList(INT, Type.Int);
	public static final TypeList INT3 = new TypeList(INT2, Type.Int);
	public static final TypeList INT4 = new TypeList(INT3, Type.Int);
	public static final TypeList BOOL = new TypeList(EMPTY, Type.Bool);
	public static final TypeList PTR = new TypeList(EMPTY, Type.Ptr);

	@Override
	public String toString() {
		if (type == null) {
			return "<empty>";
		}

		final StringBuilder buffer = new StringBuilder();
		append(buffer);
		return buffer.toString();
	}

	private void append(StringBuilder buffer) {
		if (prev.type != null) {
			prev.append(buffer);
		}
		if (buffer.length() > 0) {
			buffer.append(", ");
		}
		buffer.append(type);
	}

	public TypeList append(Type type) {
		return new TypeList(this, type);
	}

	public boolean canOperateOn(TypeList input) {
		return input.remove(this) != null;
	}

	@Nullable
	public TypeList transform(TypeList in, TypeList out) throws InvalidTypeException {
		final TypeList intermediate = remove(in);
		if (intermediate == null) {
			return null;
		}

		return intermediate.append(out);
	}

	public boolean isEmpty() {
		return type == null;
	}

	public int size() {
		if (type == null) {
			return 0;
		}
		return prev.size() + 1;
	}

	private TypeList remove(TypeList in) {
		if (in.type == null) {
			return this;
		}
		if (in.type == type) {
			return prev.remove(in.prev);
		}
		return null;
	}

	private TypeList append(TypeList list) {
		if (list.type == null) {
			return this;
		}

		final TypeList appended = append(list.prev);
		return appended.append(list.type);
	}
}
