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
			newInstructions = removeLiteralMove(newInstructions);
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
				    && i2 instanceof AsmIR.Pop pop) {
					Utils.assertTrue(push.size() == pop.size());
					if (push.reg() == pop.reg()) {
						removeNext();
						remove();
						again();
					}
					else {
						removeNext();
						replace(new AsmIR.Move(pop.reg(), push.reg(), push.size()));
					}
				}
			}
		}.process();

		return newInstructions;
	}

	private static List<AsmIR> removeLiteralMove(List<AsmIR> instructions) {
		final List<AsmIR> newInstructions = new ArrayList<>(instructions);

		new DualPeepHoleSimplifier<>(newInstructions) {
			@Override
			protected void handle(AsmIR i1, AsmIR i2) {
				if (i2 instanceof AsmIR.Move m) {
					if (i1 instanceof AsmIR.IntLiteral l
					    && m.source() == l.target()) {
						Utils.assertTrue(m.size() == 2);
						removeNext();
						replace(new AsmIR.IntLiteral(m.target(), l.value()));
					}
					else if (i1 instanceof AsmIR.BoolLiteral l
					    && m.source() == l.target()) {
						Utils.assertTrue(m.size() == 1);
						removeNext();
						replace(new AsmIR.BoolLiteral(m.target(), l.value()));
					}
					else if (i1 instanceof AsmIR.PtrLiteral l
					    && m.source() == l.target()) {
						Utils.assertTrue(m.size() == 8);
						removeNext();
						replace(new AsmIR.PtrLiteral(m.target(), l.varIndex(), l.varName()));
					}
					else if (i1 instanceof AsmIR.StringLiteral l
					    && m.source() == l.target()) {
						Utils.assertTrue(m.size() == 8);
						removeNext();
						replace(new AsmIR.StringLiteral(m.target(), l.constantIndex()));
					}
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
