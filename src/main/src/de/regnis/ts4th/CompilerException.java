package de.regnis.ts4th;

/**
 * @author Thomas Singer
 */
public class CompilerException extends RuntimeException {

	private final Location location;

	public CompilerException(String message) {
		super(message);
		location = null;
	}

	public CompilerException(Location location, String message) {
		super(message);
		this.location = location;
	}

	@Override
	public String getMessage() {
		final String message = super.getMessage();
		return location != null
				? location + ": " + message
				: message;
	}
}
