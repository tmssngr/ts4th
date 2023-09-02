package de.regnis.ts4th;

/**
 * @author Thomas Singer
 */
public final class InvalidTypeException extends RuntimeException {
	private final Location location;

	public InvalidTypeException(String msg) {
		super(msg);
		this.location = null;
	}

	public InvalidTypeException(Location location, String msg) {
		super(msg);
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
