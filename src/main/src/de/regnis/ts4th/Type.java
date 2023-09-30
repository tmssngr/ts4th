package de.regnis.ts4th;

import java.util.*;

/**
 * @author Thomas Singer
 */
public enum Type {
	Bool(1),
	I16(2),
	Ptr(0);

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
