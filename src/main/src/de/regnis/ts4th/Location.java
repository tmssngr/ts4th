package de.regnis.ts4th;

/**
 * @author Thomas Singer
 */
public record Location(int line, int column) {

	public static final Location DUMMY = new Location(-1, -1);

	@Override
	public String toString() {
		return line + ":" + (column + 1);
	}
}
