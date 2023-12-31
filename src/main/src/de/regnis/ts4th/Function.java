package de.regnis.ts4th;

import java.util.*;

/**
 * @author Thomas Singer
 */
public record Function(Location locationStart, Location locationEnd, String name, TypesInOut typesInOut, boolean isInline, List<Instruction> instructions) implements Declaration {

	public static void printInstructions(List<Instruction> instructions) {
		for (Instruction instruction : instructions) {
			if (!(instruction instanceof Instruction.Label)) {
				System.out.print("    ");
			}
			System.out.println(instruction);
		}
	}

	public Function(String name, TypeList typeIn, TypeList typeOut, boolean isInline, List<Instruction> commands) {
		this(Location.DUMMY, Location.DUMMY, name, new TypesInOut(typeIn, typeOut), isInline, commands);
	}

	@Override
	public Location location() {
		return locationStart;
	}

	public void printInstructions() {
		printInstructions(instructions);
	}
}
