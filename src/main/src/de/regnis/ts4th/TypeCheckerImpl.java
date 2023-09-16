package de.regnis.ts4th;

import java.util.*;

/**
 * @author Thomas Singer
 */
public class TypeCheckerImpl implements TypeChecker {

	public static TypeList[] determineTypes(List<Instruction> instructions, TypeList input, TypeChecker typeChecker) {
		final TypeList[] types = new TypeList[instructions.size()];
		if (types.length == 0) {
			return types;
		}

		final Map<String, Integer> labelToIndex = determineJumpTargets(instructions);

		final Deque<Integer> pending = new ArrayDeque<>();
		pending.add(0);
		types[0] = input;

		while (!pending.isEmpty()) {
			final int index = pending.removeFirst();
			final Instruction instruction = instructions.get(index);
			input = Objects.requireNonNull(types[index]);
			final TypeList output = typeChecker.checkType(instruction, input);
			final List<String> targets;
			if (instruction instanceof Instruction.Jump j) {
				targets = List.of(j.target());
			}
			else if (instruction instanceof Instruction.Branch b) {
				targets = List.of(b.ifTarget(), b.elseTarget());
			}
			else {
				if (index + 1 == types.length) {
					continue;
				}

				types[index + 1] = output;
				pending.add(index + 1);
				continue;
			}

			for (String target : targets) {
				final Integer targetIndex = labelToIndex.get(target);
				if (targetIndex == null) {
					throw new IllegalStateException("Target not found: " + target);
				}

				final TypeList existingTargetInput = types[targetIndex];
				if (existingTargetInput == null) {
					types[targetIndex] = output;
					pending.add(targetIndex);
					continue;
				}

				if (!existingTargetInput.equals(output)) {
					throw new IllegalStateException("Different types reach target " + target + ": (" + output + ") vs. (" + existingTargetInput + ")");
				}
			}
		}

		return types;
	}

	private final Map<String, TypesInOut> nameToDef = new HashMap<>();

	public TypeCheckerImpl() {
	}

	public void add(String name, TypesInOut typesInOut) {
		add(name, typesInOut.in(), typesInOut.out());
	}

	public void add(String name, TypeList in, TypeList out) {
		final TypesInOut inOut = nameToDef.get(name);
		if (inOut != null) {
			throw new IllegalStateException("Duplicate definition of function " + name);
		}

		if (BuiltinCommands.get(name) != null) {
			throw new IllegalStateException("Can't override built-in function " + name);
		}

		nameToDef.put(name, new TypesInOut(in, out));
	}

	public TypeList checkType(Instruction instruction, TypeList input) {
		if (instruction instanceof Instruction.Label
		|| instruction instanceof Instruction.Jump
		|| instruction instanceof Instruction.Ret) {
			return input;
		}

		if (instruction instanceof Instruction.IntLiteral) {
			return input.append(Type.Int);
		}

		if (instruction instanceof Instruction.BoolLiteral) {
			return input.append(Type.Bool);
		}

		if (instruction instanceof Instruction.PtrLiteral) {
			return input.append(Type.Ptr);
		}

		if (instruction instanceof Instruction.StringLiteral) {
			return input.append(Type.Ptr).append(Type.Int);
		}

		if (instruction instanceof Instruction.Branch) {
			return input.transform(TypeList.BOOL, TypeList.EMPTY);
		}

		if (instruction instanceof Instruction.Command c) {
			final String name = c.name();
			final Location location = c.location();

			final TypesInOut types = nameToDef.get(name);
			if (types != null) {
				final TypeList output = input.transform(types.in(), types.out());
				if (output == null) {
					throw new InvalidTypeException(location, "Invalid types for command " + name + "! Expected " + types.in() + " but got " + input);
				}
				return output;
			}

			final BuiltinCommands.Command command = BuiltinCommands.get(name);
			if (command == null) {
				throw new InvalidTypeException(location, "Unknown command " + name);
			}

			return command.process(name, location, input);
		}

		throw new IllegalStateException();
	}

	private static Map<String, Integer> determineJumpTargets(List<Instruction> instructions) {
		final Map<String, Integer> labelToIndex = new HashMap<>();
		for (int i = 0; i < instructions.size(); i++) {
			final Instruction instruction = instructions.get(i);
			if (instruction instanceof Instruction.Label l) {
				labelToIndex.put(l.name(), i);
			}
		}

		return labelToIndex;
	}
}
