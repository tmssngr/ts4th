package de.regnis.ts4th;

import java.util.*;

/**
 * @author Thomas Singer
 */
public record VarDeclaration(Location location, String name, List<Instruction> instructions) implements Declaration {

	public VarDeclaration(String name, List<Instruction> instructions) {
		this(Location.DUMMY, name, instructions);
	}
}
