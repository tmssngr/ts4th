package de.regnis.ts4th;

/**
 * @author Thomas Singer
 */
public final class InvalidTypeException extends CompilerException {
	public InvalidTypeException(String msg) {
		super(msg);
	}

	public InvalidTypeException(Location location, String msg) {
		super(location, msg);
	}
}
