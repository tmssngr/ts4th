package de.regnis.ts4th;

import java.nio.charset.*;
import java.util.*;
import java.util.function.*;

/**
 * @author Thomas Singer
 */
public final class AsmIRStringLiteralsImpl implements AsmIRStringLiterals {
	private final Map<String, Entry> stringToEntry = new HashMap<>();
	private final List<Supplier<byte[]>> bytes = new ArrayList<>();

	public AsmIRStringLiteralsImpl() {
	}

	@Override
	public Entry addEntry(String text) {
		final Entry existingEntry = stringToEntry.get(text);
		if (existingEntry != null) {
			return existingEntry;
		}

		final byte[] textAsBytes = text.getBytes(StandardCharsets.UTF_8);
		final Entry newEntry = new Entry(bytes.size(), textAsBytes.length);
		stringToEntry.put(text, newEntry);
		bytes.add(() -> Arrays.copyOf(textAsBytes, textAsBytes.length));
		return newEntry;
	}

	public List<Supplier<byte[]>> getConstants() {
		return Collections.unmodifiableList(bytes);
	}
}
