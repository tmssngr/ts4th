package de.regnis.ts4th;

import java.util.*;

/**
 * @author Thomas Singer
 */
public class AsmIRSimplifier {
	public static List<AsmIR> simplify(List<AsmIR> instructions) {
		while (true) {
			List<AsmIR> newInstructions = removeJumpToNext(instructions);
			newInstructions = removeIndirectLabel(newInstructions);
			newInstructions = removeCommandsAfterJump(newInstructions);
			newInstructions = removePushPop(newInstructions);
			removeUnusedLabels(newInstructions);
			if (newInstructions.equals(instructions)) {
				return instructions;
			}
			instructions = newInstructions;
		}
	}

	private static List<AsmIR> removeJumpToNext(List<AsmIR> instructions) {
		final List<AsmIR> newInstructions = new ArrayList<>(instructions);

		new DualPeepHoleSimplifier<>(newInstructions) {
			@Override
			protected void handle(AsmIR i1, AsmIR i2) {
				final String target = i1.getTarget();
				final String label = i2.getLabel();
				if (target != null && target.equals(label)) {
					remove();
				}
			}
		}.process();

		return newInstructions;
	}

	private static List<AsmIR> removeIndirectLabel(List<AsmIR> instructions) {
		final Map<String, String> fromTo = new HashMap<>();

		new DualPeepHoleSimplifier<>(instructions) {
			@Override
			protected void handle(AsmIR i1, AsmIR i2) {
				final String label = i1.getLabel();
				final String target = i2.getTarget();
				if (label != null && target != null && !label.equals(target)) {
					fromTo.put(label, target);
				}
			}
		}.process();

		if (fromTo.isEmpty()) {
			return instructions;
		}

		final List<AsmIR> newInstructions = new ArrayList<>();
		for (AsmIR instruction : instructions) {
			final String target = instruction.getTarget();
			if (target != null) {
				final String newTarget = getNewTarget(target, fromTo);
				final AsmIR.Condition condition = instruction.getCondition();
				newInstructions.add(condition != null
						                    ? AsmIR.jump(condition, newTarget)
						                    : AsmIR.jump(newTarget));
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

	private static List<AsmIR> removeCommandsAfterJump(List<AsmIR> instructions) {
		final List<AsmIR> newInstructions = new ArrayList<>(instructions);

		new DualPeepHoleSimplifier<>(newInstructions) {
			@Override
			protected void handle(AsmIR i1, AsmIR i2) {
				final String target = i1.getTarget();
				final AsmIR.Condition condition = i1.getCondition();
				final String label = i2.getLabel();
				if (target != null && condition == null && label == null) {
					removeNext();
					again();
				}
			}
		}.process();

		return newInstructions;
	}

	private static List<AsmIR> removePushPop(List<AsmIR> instructions) {
		final List<AsmIR> newInstructions = new ArrayList<>(instructions);

		new DualPeepHoleSimplifier<>(newInstructions) {
			@Override
			protected void handle(AsmIR i1, AsmIR i2) {
				if (i1.isPush() && i2.isPop()
				    && i1.getPushPopReg() == i2.getPushPopReg()) {
					Utils.assertTrue(i1.getPushPopSize() == i2.getPushPopSize());
					removeNext();
					remove();
					again();
				}
			}
		}.process();

		return newInstructions;
	}

	private static void removeUnusedLabels(List<AsmIR> instructions) {
		final Set<String> usedTargets = getUsedTargets(instructions);
		for (Iterator<AsmIR> it = instructions.iterator(); it.hasNext(); ) {
			final AsmIR instruction = it.next();
			final String label = instruction.getLabel();
			if (label != null && !usedTargets.contains(label)) {
				it.remove();
			}
		}
	}

	private static Set<String> getUsedTargets(List<AsmIR> instructions) {
		final Set<String> usedTargets = new HashSet<>();
		for (AsmIR instruction : instructions) {
			final String target = instruction.getTarget();
			if (target != null) {
				usedTargets.add(target);
			}
		}
		return usedTargets;
	}
}
