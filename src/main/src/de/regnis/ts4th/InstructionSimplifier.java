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
				if (i1.isJump() && i2.isLabel()) {
					final List<String> targets = i1.getTargets();
					final String label = i2.getLabel();
					if (targets.contains(label)) {
						remove();
					}
				}
			}
		}.process();

		return newInstructions;
	}

	private static List<Instruction> removeIndirectLabel(List<Instruction> instructions) {
		final Map<String, String> fromTo = new HashMap<>();

		new DualPeepHoleSimplifier<>(instructions) {
			@Override
			protected void handle(Instruction i1, Instruction i2) {
				if (i1.isLabel() && i2.isJump()) {
					final String label = i1.getLabel();
					final String target = i2.getTargets().get(0);
					if (!Objects.equals(label, target)) {
						fromTo.put(label, target);
					}
				}
			}
		}.process();

		if (fromTo.isEmpty()) {
			return instructions;
		}

		final List<Instruction> newInstructions = new ArrayList<>();
		for (Instruction instruction : instructions) {
			if (instruction.isJump()) {
				final String target = instruction.getTargets().get(0);
				final String newTarget = getNewTarget(target, fromTo);
				newInstructions.add(Instruction.jump(newTarget));
			}
			else if (instruction.isBranch()) {
				final List<String> targets = instruction.getTargets();
				final String ifTarget = getNewTarget(targets.get(0), fromTo);
				final String elseTarget = getNewTarget(targets.get(1), fromTo);
				newInstructions.add(Instruction.branch(ifTarget, elseTarget));
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
				if (i1.isJump() && !i2.isLabel()) {
					removeNext();
					again();
				}
			}
		}.process();

		return newInstructions;
	}

	private static List<Instruction> removeBoolConstBranch(List<Instruction> instructions) {
		final List<Instruction> newInstructions = new ArrayList<>(instructions);

		new DualPeepHoleSimplifier<>(newInstructions) {
			@Override
			protected void handle(Instruction i1, Instruction i2) {
				final Boolean boolLiteral = i1.getBoolLiteral();
				if (boolLiteral != null && i2.isBranch()) {
					final List<String> targets = i2.getTargets();
					replace(Instruction.jump(targets.get(boolLiteral ? 0 : 1)));
					removeNext();
				}
			}
		}.process();

		return newInstructions;
	}

	private static void removeUnusedLabels(List<Instruction> instructions) {
		final Set<String> usedTargets = getUsedTargets(instructions);
		for (Iterator<Instruction> it = instructions.iterator(); it.hasNext(); ) {
			final Instruction instruction = it.next();
			final String label = instruction.getLabel();
			if (label != null && !usedTargets.contains(label)) {
				it.remove();
			}
		}
	}

	private static Set<String> getUsedTargets(List<Instruction> instructions) {
		final Set<String> usedTargets = new HashSet<>();
		for (Instruction instruction : instructions) {
			final List<String> targets = instruction.getTargets();
			usedTargets.addAll(targets);
		}
		return usedTargets;
	}
}
