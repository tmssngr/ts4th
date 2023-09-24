package de.regnis.ts4th;

/**
 * @author Thomas Singer
 */
public interface AsmIRStringLiterals {
	Entry addEntry(String text);

	public record Entry(int index, int length) {
	}
}
