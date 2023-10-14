package de.regnis.ts4th;

import java.util.*;

import org.jetbrains.annotations.*;

/**
 * @author Thomas Singer
 */
public class InstructionTypeEvaluator {

	public static void iterate(List<Instruction> instructions, TypeList input, Handler handler) {
		final Map<String, State> labelToState = new HashMap<>();
		LocalVarStack localVarStack = null;

		for (Instruction instruction : instructions) {
			if (instruction instanceof Instruction.Label(String name, Location location)) {
				final State existing = labelToState.get(name);
				if (input == null) {
					// after a jump or branch...
					if (existing == null) {
						throw new IllegalStateException("Expected types at " + location);
					}

					input = existing.types;
					localVarStack = existing.localVarStack;
				}
				else if (existing != null) {
					checkExisting(input, localVarStack, location, existing);
				}
			}

			final Pair<TypeList, LocalVarStack> result = handler.handle(instruction, input, localVarStack);
			input = result.first();
			localVarStack = result.second();

			if (instruction instanceof Instruction.Jump(String target, Location location)) {
				setTargetTypes(input, localVarStack, target, location, labelToState);
				input = null;
			}
			else if (instruction instanceof Instruction.Branch(String ifTarget, String elseTarget, Location location)) {
				setTargetTypes(input, localVarStack, ifTarget, location, labelToState);
				setTargetTypes(input, localVarStack, elseTarget, location, labelToState);
				input = null;
			}
		}
	}

	private static void setTargetTypes(TypeList input, @Nullable LocalVarStack localVarStack, String target, Location location, Map<String, State> labelToType) {
		final State existing = labelToType.get(target);
		if (existing != null) {
			checkExisting(input, localVarStack, location, existing);
		}
		else {
			labelToType.put(target, new State(input, localVarStack, location));
		}
	}

	private static void checkExisting(TypeList input, LocalVarStack localVarStack, Location location, State existing) {
		if (!Objects.equals(input, existing.types)) {
			throw new InvalidTypeException(location, STR. "Invalid types: \{ existing.types } (from: \{ existing.location }) vs. \{ input }" );
		}

		if (!Objects.equals(localVarStack, existing.localVarStack)) {
			throw new InvalidTypeException(location, STR. "Invalid local vars: \{ existing.localVarStack } (from: \{ existing.location }) vs. \{ localVarStack }" );
		}
	}

	public interface Handler {
		Pair<TypeList, LocalVarStack> handle(Instruction instruction, TypeList input, LocalVarStack localVarStackInput);
	}

	private record State(@NotNull TypeList types, @Nullable LocalVarStack localVarStack, @NotNull Location location) {
	}
}
