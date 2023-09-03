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
				if (i1 instanceof AsmIR.Jump j
				    && i2 instanceof AsmIR.Label l
				    && Objects.equals(j.target(), l.name())) {
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
				if (i1 instanceof AsmIR.Label l
				    && i2 instanceof AsmIR.Jump j
				    && !Objects.equals(l.name(), j.target())) {
					fromTo.put(l.name(), j.target());
				}
			}
		}.process();

		if (fromTo.isEmpty()) {
			return instructions;
		}

		final List<AsmIR> newInstructions = new ArrayList<>();
		for (AsmIR instruction : instructions) {
			if (instruction instanceof AsmIR.Jump j) {
				final String newTarget = getNewTarget(j.target(), fromTo);
				final AsmIR.Condition condition = j.condition();
				newInstructions.add(condition != null
						                    ? AsmIRFactory.jump(condition, newTarget)
						                    : AsmIRFactory.jump(newTarget));
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
				if (i1 instanceof AsmIR.Jump j
				    && j.condition() == null
				    && !(i2 instanceof AsmIR.Label)) {
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
				if (i1 instanceof AsmIR.Push push
				    && i2 instanceof AsmIR.Pop pop
				    && push.reg() == pop.reg()) {
					Utils.assertTrue(push.size() == pop.size());
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
		instructions.removeIf(instruction -> instruction instanceof AsmIR.Label l && !usedTargets.contains(l.name()));
	}

	private static Set<String> getUsedTargets(List<AsmIR> instructions) {
		final Set<String> usedTargets = new HashSet<>();
		for (AsmIR instruction : instructions) {
			if (instruction instanceof AsmIR.Jump j) {
				usedTargets.add(j.target());
			}
		}
		return usedTargets;
	}
}
