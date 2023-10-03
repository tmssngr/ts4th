package de.regnis.ts4th;

import java.util.*;
import java.util.function.*;

/**
 * @author Thomas Singer
 */
public class InstructionTypeEvaluator {

	public static void iterate(List<Instruction> instructions, TypeList input, BiFunction<Instruction, TypeList, TypeList> consumer) {
		final Map<String, Pair<TypeList, Location>> labelToType = new HashMap<>();

		for (Instruction instruction : instructions) {
			if (instruction instanceof Instruction.Label(String name, Location location)) {
				final Pair<TypeList, Location> existing = labelToType.get(name);
				if (input == null) {
					// after a jump or branch...
					if (existing == null) {
						throw new IllegalStateException("Expected types at " + location);
					}

					input = existing.first();
				}
				else if (existing != null) {
					checkExisting(input, location, existing);
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

	private static void setTargetTypes(TypeList input, String target, Location location, Map<String, Pair<TypeList, Location>> labelToType) {
		final Pair<TypeList, Location> existing = labelToType.get(target);
		if (existing != null) {
			checkExisting(input, location, existing);
		}
		else {
			labelToType.put(target, new Pair<>(input, location));
		}
	}

	private static void checkExisting(TypeList input, Location location, Pair<TypeList, Location> existing) {
		final TypeList existingInput = existing.first();
		if (!existingInput.equals(input)) {
			throw new InvalidTypeException(location, STR."Invalid types: \{existingInput} (from: \{ existing.second()}) vs. \{ input }");
		}
	}
}
