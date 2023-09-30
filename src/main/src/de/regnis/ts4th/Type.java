package de.regnis.ts4th;

import java.util.*;

/**
 * @author Thomas Singer
 */
@SuppressWarnings("PointlessBitwiseExpression")
public enum Type {
	Bool(1),
	I8(1 << 0),
	I16(1 << 1),
	I32(1 << 2),
	I64(1 << 3),
	U8(1 << 0),
	U16(1 << 1),
	U32(1 << 2),
	U64(1 << 3),
	Ptr(0);

	public static final List<Type> INT_TYPES = List.of(I8, I16, I32, I64, U8, U16, U32, U64);

	private final int byteCount;

	Type(int byteCount) {
		this.byteCount = byteCount;
	}

	@Override
	public String toString() {
		return name().toLowerCase(Locale.ROOT);
	}

	public int getByteCount(int ptrSize) {
		return byteCount == 0 ? ptrSize : byteCount;
	}
}
