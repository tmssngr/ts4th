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
			newInstructions = swapLitPop(newInstructions);
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
				if (i1 instanceof AsmIR.Jump(_, String target)
				    && i2 instanceof AsmIR.Label(String name)
				    && Objects.equals(target, name)) {
					remove();
				}
			}
		}.process("remove jump to next");

		return newInstructions;
	}

	private static List<AsmIR> removeIndirectLabel(List<AsmIR> instructions) {
		final Map<String, String> fromTo = new HashMap<>();

		new DualPeepHoleSimplifier<>(instructions) {
			@Override
			protected void handle(AsmIR i1, AsmIR i2) {
				if (i1 instanceof AsmIR.Label(String name)
				    && i2 instanceof AsmIR.Jump(_, String target)
				    && !Objects.equals(name, target)) {
					fromTo.put(name, target);
				}
			}
		}.process("remove indirect label");

		if (fromTo.isEmpty()) {
			return instructions;
		}

		final List<AsmIR> newInstructions = new ArrayList<>();
		for (AsmIR instruction : instructions) {
			if (instruction instanceof AsmIR.Jump(AsmIR.Condition condition, String target)) {
				final String newTarget = getNewTarget(target, fromTo);
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
		}.process("remove commands after jump");

		return newInstructions;
	}

	private static List<AsmIR> removePushPop(List<AsmIR> instructions) {
		final List<AsmIR> newInstructions = new ArrayList<>(instructions);

		new DualPeepHoleSimplifier<>(newInstructions) {
			@Override
			protected void handle(AsmIR i1, AsmIR i2) {
				if (i1 instanceof AsmIR.Push(int sourceReg, int sourceSize)
				    && i2 instanceof AsmIR.Pop(int targetReg, int targetSize)) {
					Utils.assertTrue(sourceSize == targetSize);
					if (sourceReg == targetReg) {
						removeNext();
						remove();
						again();
					}
					else {
						removeNext();
						replace(new AsmIR.Move(targetReg, sourceReg, sourceSize));
					}
				}
			}
		}.process("remove/replace push pop");

		return newInstructions;
	}

	private static List<AsmIR> removeLiteralMove(List<AsmIR> instructions) {
		final List<AsmIR> newInstructions = new ArrayList<>(instructions);

		new DualPeepHoleSimplifier<>(newInstructions) {
			@Override
			protected void handle(AsmIR i1, AsmIR i2) {
				if (i2 instanceof AsmIR.Move(int targetReg, int sourceReg, int size)) {
					if (i1 instanceof AsmIR.IntLiteral(int tmp, int value)
					    && sourceReg == tmp) {
						Utils.assertTrue(size == 2);
						removeNext();
						replace(new AsmIR.IntLiteral(targetReg, value));
					}
					else if (i1 instanceof AsmIR.BoolLiteral(int tmpReg, boolean value)
					         && sourceReg == tmpReg) {
						Utils.assertTrue(size == 1);
						removeNext();
						replace(new AsmIR.BoolLiteral(targetReg, value));
					}
					else if (i1 instanceof AsmIR.PtrLiteral(int tmpReg, int index, String name)
					         && sourceReg == tmpReg) {
						Utils.assertTrue(size == 8);
						removeNext();
						replace(new AsmIR.PtrLiteral(targetReg, index, name));
					}
					else if (i1 instanceof AsmIR.StringLiteral(int tmpReg, int index)
					         && sourceReg == tmpReg) {
						Utils.assertTrue(size == 8);
						removeNext();
						replace(new AsmIR.StringLiteral(targetReg, index));
					}
				}
			}
		}.process("replace moved literal");

		return newInstructions;
	}

	private static List<AsmIR> swapLitPop(List<AsmIR> instructions) {
		final List<AsmIR> newInstructions = new ArrayList<>(instructions);

		new DualPeepHoleSimplifier<>(newInstructions) {
			@Override
			protected void handle(AsmIR i1, AsmIR i2) {
				if (i1 instanceof AsmIR.IntLiteral(int targetRegLit, _)
				    && i2 instanceof AsmIR.Pop(int targetRegPop, _)) {
					if (targetRegLit == targetRegPop) {
						// might happen after pushing a string literal and dropping the size
						remove();
					}
					else {
						remove();
						remove();
						insert(i1);
						insert(i2);
					}
				}
				else if (i1 instanceof AsmIR.BoolLiteral(int targetRegLit, _)
				         && i2 instanceof AsmIR.Pop(int targetRegPop, _)) {
					if (targetRegLit == targetRegPop) {
						remove();
					}
					else {
						remove();
						remove();
						insert(i1);
						insert(i2);
					}
				}
			}
		}.process("swap literal pop");

		return newInstructions;
	}

	private static void removeUnusedLabels(List<AsmIR> instructions) {
		final Set<String> usedTargets = getUsedTargets(instructions);
		instructions.removeIf(instruction -> instruction instanceof AsmIR.Label(String name) && !usedTargets.contains(name));
	}

	private static Set<String> getUsedTargets(List<AsmIR> instructions) {
		final Set<String> usedTargets = new HashSet<>();
		for (AsmIR instruction : instructions) {
			if (instruction instanceof AsmIR.Jump(_, String target)) {
				usedTargets.add(target);
			}
		}
		return usedTargets;
	}
}
