package de.regnis.ts4th;

import org.jetbrains.annotations.*;

import java.util.*;

/**
 * @author Thomas Singer
 */
public class Utils {

	public static void assertTrue(boolean value) {
		assertTrue(value, "");
	}

	public static void assertTrue(boolean value, String msg) {
		if (!value) {
			throw new IllegalStateException(msg);
		}
	}

	@Nullable
	public static <E> E getLastOrNull(List<E> list) {
		return list.isEmpty() ? null : list.get(list.size() - 1);
	}

	public static String toHex(long value, int digits) {
		final StringBuilder buffer = new StringBuilder();
		toHex(value, digits, buffer);
		return buffer.toString();
	}

	public static void toHex(long value, int digits, StringBuilder buffer) {
		if (digits < 1) {
			return;
		}

		if (digits > 1) {
			toHex(value >> 4, digits - 1, buffer);
		}
		buffer.append("0123456789abcdef".charAt((int)value & 0xF));
	}

	public static String join(Iterable<String> iterable, String separator) {
		final StringBuilder buffer = new StringBuilder();
		for (String s : iterable) {
			if (buffer.length() > 0) {
				buffer.append(separator);
			}
			buffer.append(s);
		}
		return buffer.toString();
	}
}
