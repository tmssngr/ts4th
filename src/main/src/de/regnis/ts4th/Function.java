package de.regnis.ts4th;

import java.util.*;

/**
 * @author Thomas Singer
 */
public record Function(String name, TypesInOut typesInOut, boolean isInline, List<Instruction> instructions) {

	public Function(String name, TypeList typeIn, TypeList typeOut, boolean isInline, List<Instruction> commands) {
		this(name, new TypesInOut(typeIn, typeOut), isInline, commands);
	}
}
