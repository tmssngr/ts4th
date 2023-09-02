package de.regnis.ts4th;

import java.nio.charset.*;
import java.util.*;

/**
 * @author Thomas Singer
 */
public final class AsmIRStringLiterals {
	private final Map<String, Integer> stringToIndex = new HashMap<>();
	private final List<byte[]> bytes = new ArrayList<>();

	public AsmIRStringLiterals() {
	}

	public int getConstantIndex(String text) {
		final Integer i = stringToIndex.get(text);
		if (i != null) {
			return i;
		}

		final int index = bytes.size();
		stringToIndex.put(text, index);
		bytes.add(text.getBytes(StandardCharsets.UTF_8));
		return index;
	}

	public int getLength(String text) {
		final Integer index = stringToIndex.get(text);
		return bytes.get(index).length;
	}

	public List<byte[]> getConstants() {
		return Collections.unmodifiableList(bytes);
	}
}
