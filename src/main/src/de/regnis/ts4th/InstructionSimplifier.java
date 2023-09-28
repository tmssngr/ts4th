package de.regnis.ts4th;

import java.util.*;

/**
 * @author Thomas Singer
 */
public class InstructionSimplifier {
	public static List<Instruction> simplify(List<Instruction> instructions) {
		while (true) {
			List<Instruction> newInstructions = removeJumpToNext(instructions);
			newInstructions = removeIndirectLabel(newInstructions);
			newInstructions = removeCommandsAfterJump(newInstructions);
			newInstructions = removeBoolConstBranch(newInstructions);
			removeUnusedLabels(newInstructions);
			if (newInstructions.equals(instructions)) {
				return instructions;
			}
			instructions = newInstructions;
		}
	}

	private static List<Instruction> removeJumpToNext(List<Instruction> instructions) {
		final List<Instruction> newInstructions = new ArrayList<>(instructions);

		new DualPeepHoleSimplifier<>(newInstructions) {
			@Override
			protected void handle(Instruction i1, Instruction i2) {
				if (i1 instanceof Instruction.Jump(String target, _)
				    && i2 instanceof Instruction.Label(String name, _)
				    && Objects.equals(target, name)) {
					remove();
				}
			}
		}.process("remove jump to next");

		return newInstructions;
	}

	private static List<Instruction> removeIndirectLabel(List<Instruction> instructions) {
		final Map<String, String> fromTo = new HashMap<>();

		new DualPeepHoleSimplifier<>(instructions) {
			@Override
			protected void handle(Instruction i1, Instruction i2) {
				if (i1 instanceof Instruction.Label(String label, _)
				    && i2 instanceof Instruction.Jump(String target, _)
				    && !Objects.equals(label, target)) {
					fromTo.put(label, target);
				}
			}
		}.process("remove indirect label");

		if (fromTo.isEmpty()) {
			return instructions;
		}

		final List<Instruction> newInstructions = new ArrayList<>();
		for (Instruction instruction : instructions) {
			if (instruction instanceof Instruction.Jump j) {
				final String target = j.target();
				final String newTarget = getNewTarget(target, fromTo);
				newInstructions.add(InstructionFactory.jump(newTarget));
			}
			else if (instruction instanceof Instruction.Branch(String ifTarget, String elseTarget, Location location)) {
				final String newIfTarget = getNewTarget(ifTarget, fromTo);
				final String newElseTarget = getNewTarget(elseTarget, fromTo);
				newInstructions.add(new Instruction.Branch(newIfTarget, newElseTarget, location));
			}
			else {
				newInstructions.add(instruction);
			}
		}
		return newInstructions;
	}

	private static String getNewTarget(String target, Map<String, String> fromTo) {
		while (true) {
			final String newTarget = fromTo.get(target);
			if (newTarget == null) {
				return target;
			}
			target = newTarget;
		}
	}

	private static List<Instruction> removeCommandsAfterJump(List<Instruction> instructions) {
		final List<Instruction> newInstructions = new ArrayList<>(instructions);

		new DualPeepHoleSimplifier<>(newInstructions) {
			@Override
			protected void handle(Instruction i1, Instruction i2) {
				if (i1 instanceof Instruction.Jump && !(i2 instanceof Instruction.Label)) {
					removeNext();
					again();
				}
			}
		}.process("remove commands after jump");

		return newInstructions;
	}

	private static List<Instruction> removeBoolConstBranch(List<Instruction> instructions) {
		final List<Instruction> newInstructions = new ArrayList<>(instructions);

		new DualPeepHoleSimplifier<>(newInstructions) {
			@Override
			protected void handle(Instruction i1, Instruction i2) {
				if (i1 instanceof Instruction.BoolLiteral(boolean value)
				    && i2 instanceof Instruction.Branch(String ifTarget, String elseTarget, _)) {
					replace(InstructionFactory.jump(value ? ifTarget : elseTarget));
					removeNext();
				}
			}
		}.process("remove false-branch");

		return newInstructions;
	}

	private static void removeUnusedLabels(List<Instruction> instructions) {
		final Set<String> usedTargets = getUsedTargets(instructions);
		instructions.removeIf(instruction ->
				                      instruction instanceof Instruction.Label label
				                      && !usedTargets.contains(label.name()));
	}

	private static Set<String> getUsedTargets(List<Instruction> instructions) {
		final Set<String> usedTargets = new HashSet<>();
		for (Instruction instruction : instructions) {
			if (instruction instanceof Instruction.Jump(String target, _)) {
				usedTargets.add(target);
			}
			else if (instruction instanceof Instruction.Branch(String ifTarget, String elseTarget, _)) {
				usedTargets.add(ifTarget);
				usedTargets.add(elseTarget);
			}
		}
		return usedTargets;
	}
}
