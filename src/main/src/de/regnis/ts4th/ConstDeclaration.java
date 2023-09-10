package de.regnis.ts4th;

import java.util.*;

/**
 * @author Thomas Singer
 */
public record ConstDeclaration(Location location, String name, List<Instruction> instructions) implements Declaration {

	public ConstDeclaration(String name, List<Instruction> instructions) {
		this(Location.DUMMY, name, instructions);
	}
}
