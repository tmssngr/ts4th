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
				if (i1 instanceof Instruction.Jump j
				    && i2 instanceof Instruction.Label l
				    && Objects.equals(j.target(), l.name())) {
					remove();
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
				if (i1 instanceof Instruction.Label l
				    && i2 instanceof Instruction.Jump j) {
					final String label = l.name();
					final String target = j.target();
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
			if (instruction instanceof Instruction.Jump j) {
				final String target = j.target();
				final String newTarget = getNewTarget(target, fromTo);
				newInstructions.add(InstructionFactory.jump(newTarget));
			}
			else if (instruction instanceof Instruction.Branch b) {
				final String ifTarget = getNewTarget(b.ifTarget(), fromTo);
				final String elseTarget = getNewTarget(b.elseTarget(), fromTo);
				newInstructions.add(InstructionFactory.branch(ifTarget, elseTarget));
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
		}.process();

		return newInstructions;
	}

	private static List<Instruction> removeBoolConstBranch(List<Instruction> instructions) {
		final List<Instruction> newInstructions = new ArrayList<>(instructions);

		new DualPeepHoleSimplifier<>(newInstructions) {
			@Override
			protected void handle(Instruction i1, Instruction i2) {
				if (i1 instanceof Instruction.BoolLiteral lit
				    && i2 instanceof Instruction.Branch b) {
					replace(InstructionFactory.jump(lit.value()
							                             ? b.ifTarget()
							                             : b.elseTarget()));
					removeNext();
				}
			}
		}.process();

		return newInstructions;
	}

	private static void removeUnusedLabels(List<Instruction> instructions) {
		final Set<String> usedTargets = getUsedTargets(instructions);
		instructions.removeIf(instruction -> {
			return instruction instanceof Instruction.Label label
			       && !usedTargets.contains(label.name());
		});
	}

	private static Set<String> getUsedTargets(List<Instruction> instructions) {
		final Set<String> usedTargets = new HashSet<>();
		for (Instruction instruction : instructions) {
			if (instruction instanceof Instruction.Jump j) {
				usedTargets.add(j.target());
			}
			else if (instruction instanceof Instruction.Branch b) {
				usedTargets.add(b.ifTarget());
				usedTargets.add(b.elseTarget());
			}
		}
		return usedTargets;
	}
}
