package de.regnis.ts4th;

import java.util.*;

/**
 * @author Thomas Singer
 */
public record CfgBlock(String name, List<Instruction> instructions, Location locationStart, Location locationEnd) {

	public CfgBlock(String name, List<Instruction> instructions) {
		this(name, instructions, Location.DUMMY, Location.DUMMY);
	}

	public CfgBlock(String name, List<Instruction> instructions, Location locationStart, Location locationEnd) {
		this.name = name;
		this.instructions = new ArrayList<>(instructions);
		this.locationStart = locationStart;
		this.locationEnd = locationEnd;
	}

	@Override
	public String toString() {
		return name + ": " + instructions;
	}

	public TypeList checkTypes(TypeList input, TypeChecker checker) {
		for (Instruction instruction : instructions) {
			input = checker.checkType(instruction, input);
		}
		return input;
	}
}
