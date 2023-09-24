package de.regnis.ts4th;

import java.util.*;
import java.util.function.*;

/**
 * @author Thomas Singer
 */
public class InstructionTypeEvaluator {

	public static void iterate(List<Instruction> instructions, TypeList input, BiFunction<Instruction, TypeList, TypeList> consumer) {
		final Map<String, TypeList> labelToType = new HashMap<>();

		for (Instruction instruction : instructions) {
			if (instruction instanceof Instruction.Label(String name, Location location)) {
				final TypeList labelInput = labelToType.get(name);
				if (input == null) {
					if (labelInput == null) {
						throw new IllegalStateException("Expected types at " + location);
					}
					input = labelInput;
				}
				if (labelInput != null && !labelInput.equals(input)) {
					throw new InvalidTypeException(location, "Invalid types: " + labelInput + " vs. " + input);
				}
			}

			input = consumer.apply(instruction, input);

			if (instruction instanceof Instruction.Jump(String target, Location location)) {
				setTargetTypes(input, target, location, labelToType);
				input = null;
			}
			else if (instruction instanceof Instruction.Branch(String ifTarget, String elseTarget, Location location)) {
				setTargetTypes(input, ifTarget, location, labelToType);
				setTargetTypes(input, elseTarget, location, labelToType);
				input = null;
			}
		}
	}

	private static void setTargetTypes(TypeList input, String target, Location location, Map<String, TypeList> labelToType) {
		final TypeList targetInput = labelToType.get(target);
		if (targetInput != null) {
			if (!targetInput.equals(input)) {
				throw new InvalidTypeException(location, "Invalid types: " + targetInput + " vs. " + input);
			}
		}
		else {
			labelToType.put(target, input);
		}
	}
}
