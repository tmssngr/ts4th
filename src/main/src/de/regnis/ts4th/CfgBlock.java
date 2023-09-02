package de.regnis.ts4th;

import java.util.*;

/**
 * @author Thomas Singer
 */
public final class CfgBlock {

	private final String name;
	private final List<Instruction> instructions;

	public CfgBlock(String name, List<Instruction> instructions) {
		this.name = name;
		this.instructions = new ArrayList<>(instructions);
	}

	@Override
	public String toString() {
		return name + ": " + instructions;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		CfgBlock cfgBlock = (CfgBlock) o;
		return Objects.equals(name, cfgBlock.name) && Objects.equals(instructions, cfgBlock.instructions);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, instructions);
	}

	public String getName() {
		return name;
	}

	public List<Instruction> getInstructions() {
		return instructions;
	}

	public TypeList checkTypes(TypeList input, TypeChecker checker) {
		for (Instruction instruction : instructions) {
			input = checker.checkType(instruction, input);
		}
		return input;
	}
}
