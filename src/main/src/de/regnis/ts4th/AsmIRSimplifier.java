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
			newInstructions = removeLiteralOrVarRead_move(newInstructions);
			newInstructions = swapLiteralOrVarRead_Pop(newInstructions);
			newInstructions = squashDropVars(newInstructions);
			newInstructions = replaceLitBin_BinLit(newInstructions);
			newInstructions = combine2BinLit(newInstructions);
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
				if (i1 instanceof AsmIR.Push(int sourceReg, Type pushType)
				    && i2 instanceof AsmIR.Pop(int targetReg, Type popType)) {
					if (pushType != popType) {
						printError();
						throw new IllegalStateException("");
					}
					if (sourceReg == targetReg) {
						removeNext();
						remove();
						again();
					}
					else {
						removeNext();
						replace(new AsmIR.Move(targetReg, sourceReg, pushType));
					}
				}
			}
		}.process("remove/replace push pop");

		return newInstructions;
	}

	private static List<AsmIR> removeLiteralOrVarRead_move(List<AsmIR> instructions) {
		final List<AsmIR> newInstructions = new ArrayList<>(instructions);

		new DualPeepHoleSimplifier<>(newInstructions) {
			@Override
			protected void handle(AsmIR i1, AsmIR i2) {
				if (i2 instanceof AsmIR.Move(int targetReg, int sourceReg, Type type)) {
					if (i1 instanceof AsmIR.IntLiteral(int tmp, long value, Type litType)
					    && sourceReg == tmp) {
						Utils.assertTrue(type == litType);
						removeNext();
						replace(new AsmIR.IntLiteral(targetReg, value, type));
					}
					else if (i1 instanceof AsmIR.BoolLiteral(int tmpReg, boolean value)
					         && sourceReg == tmpReg) {
						Utils.assertTrue(type == Type.Bool);
						removeNext();
						replace(new AsmIR.BoolLiteral(targetReg, value));
					}
					else if (i1 instanceof AsmIR.PtrLiteral(int tmpReg, int index, String name)
					         && sourceReg == tmpReg) {
						Utils.assertTrue(type == Type.Ptr);
						removeNext();
						replace(new AsmIR.PtrLiteral(targetReg, index, name));
					}
					else if (i1 instanceof AsmIR.StringLiteral(int tmpReg, int index)
					         && sourceReg == tmpReg) {
						Utils.assertTrue(type == Type.Ptr);
						removeNext();
						replace(new AsmIR.StringLiteral(targetReg, index));
					}
					else if (i1 instanceof AsmIR.LocalVarRead(int tmpReg, Type typeRead, TypeList offset)
					         && tmpReg == sourceReg) {
						Utils.assertTrue(Objects.equals(typeRead, type));
						removeNext();
						replace(new AsmIR.LocalVarRead(targetReg, type, offset));
					}
				}
			}
		}.process("replace moved literal");

		return newInstructions;
	}

	private static List<AsmIR> swapLiteralOrVarRead_Pop(List<AsmIR> instructions) {
		final List<AsmIR> newInstructions = new ArrayList<>(instructions);

		new DualPeepHoleSimplifier<>(newInstructions) {
			@Override
			protected void handle(AsmIR i1, AsmIR i2) {
				if (i2 instanceof AsmIR.Pop(int targetRegPop, _)) {
					if (i1 instanceof AsmIR.IntLiteral(int targetRegLit, _, _)) {
						removeOrSwap(i1, i2, targetRegPop, targetRegLit);
					}
					else if (i1 instanceof AsmIR.BoolLiteral(int targetRegLit, _)) {
						removeOrSwap(i1, i2, targetRegPop, targetRegLit);
					}
					else if (i1 instanceof AsmIR.LocalVarRead(int targetReg, _, _)) {
						removeOrSwap(i1, i2, targetRegPop, targetReg);
					}
				}
			}

			private void removeOrSwap(AsmIR i1, AsmIR i2, int targetRegPop, int targetRegLit) {
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
		}.process("swap literal pop");

		return newInstructions;
	}

	private static List<AsmIR> squashDropVars(List<AsmIR> instructions) {
		final List<AsmIR> newInstructions = new ArrayList<>(instructions);

		new DualPeepHoleSimplifier<>(newInstructions) {
			@Override
			protected void handle(AsmIR i1, AsmIR i2) {
				if (i1 instanceof AsmIR.DropVars(TypeList types1)
				    && i2 instanceof AsmIR.DropVars(TypeList types2)) {
					removeNext();
					replace(new AsmIR.DropVars(types1.append(types2)));
					again();
				}
			}
		}.process("squash drop-vars");

		return newInstructions;
	}

	private static List<AsmIR> replaceLitBin_BinLit(List<AsmIR> instructions) {
		final List<AsmIR> newInstructions = new ArrayList<>(instructions);

		new DualPeepHoleSimplifier<>(newInstructions) {
			@Override
			protected void handle(AsmIR i1, AsmIR i2) {
				if (i1 instanceof AsmIR.IntLiteral(int targetReg, long value, Type litType)
				    && i2 instanceof AsmIR.BinCommand(AsmIR.BinOperation operation, int reg1, int reg2, Type binType)
				    && targetReg == reg2) {
					Utils.assertTrue(Objects.equals(litType, binType));
					remove();
					replace(new AsmIR.BinLiteralCommand(operation, reg1, value, binType));
				}
			}
		}.process("replace literal-binary with binary-literal");

		return newInstructions;
	}

	private static List<AsmIR> combine2BinLit(List<AsmIR> instructions) {
		final List<AsmIR> newInstructions = new ArrayList<>(instructions);

		new DualPeepHoleSimplifier<>(newInstructions) {
			@Override
			protected void handle(AsmIR i1, AsmIR i2) {
				if (i1 instanceof AsmIR.BinLiteralCommand(AsmIR.BinOperation operation, int reg, long value1, Type type)
				    && i2 instanceof AsmIR.BinLiteralCommand(AsmIR.BinOperation operation2, int reg2, long value2, Type type2)
				    && reg == reg2) {
					Utils.assertTrue(Objects.equals(type, type2));
					if (operation == AsmIR.BinOperation.add && operation2 == AsmIR.BinOperation.sub) {
						remove();
						replace(new AsmIR.BinLiteralCommand(operation, reg, value1 - value2, type));
					}
					else if (operation == AsmIR.BinOperation.imul && operation2 == AsmIR.BinOperation.imul) {
						remove();
						replace(new AsmIR.BinLiteralCommand(operation, reg, value1 * value2, type));
					}
				}
			}
		}.process("replace 2 binary-literals with 1 binary-literal");

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
