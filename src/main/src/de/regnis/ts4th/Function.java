package de.regnis.ts4th;

import java.util.*;

/**
 * @author Thomas Singer
 */
public record Function(Location location, String name, TypesInOut typesInOut, boolean isInline, List<Instruction> instructions) implements Declaration {

	public Function(String name, TypeList typeIn, TypeList typeOut, boolean isInline, List<Instruction> commands) {
		this(Location.DUMMY, name, new TypesInOut(typeIn, typeOut), isInline, commands);
	}
}
