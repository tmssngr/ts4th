package de.regnis.ts4th;

import java.util.*;

/**
 * @author Thomas Singer
 */
public class TypeCheckerImpl implements TypeChecker {

	private final Map<String, TypesInOut> nameToDef = new HashMap<>();

	public TypeCheckerImpl() {
	}

	public TypeList checkType(Instruction instruction, TypeList input) {
		return switch (instruction) {
			case Instruction.Label _,
					Instruction.Jump _,
					Instruction.Ret _ -> input;
			case Instruction.IntLiteral _ -> input.append(Type.Int);
			case Instruction.BoolLiteral _ -> input.append(Type.Bool);
			case Instruction.PtrLiteral _ -> input.append(Type.Ptr);
			case Instruction.StringLiteral _ -> input.append(Type.Ptr).append(Type.Int);
			case Instruction.Branch(_, _, Location location) -> {
				final TypeList output = input.transform(TypeList.BOOL, TypeList.EMPTY);
				if (output == null) {
					throw new InvalidTypeException(location, "Invalid types! Expected " + TypeList.BOOL + ", but got " + input);
				}
				yield output;
			}
			case Instruction.Command(String name, Location location) -> {
				final TypesInOut types = nameToDef.get(name);
				if (types != null) {
					final TypeList output = input.transform(types.in(), types.out());
					if (output == null) {
						throw new InvalidTypeException(location, "Invalid types for command " + name + "! Expected " + types.in() + " but got " + input);
					}
					yield output;
				}

				final BuiltinCommands.Command command = BuiltinCommands.get(name);
				if (command == null) {
					throw new InvalidTypeException(location, "Unknown command " + name);
				}

				yield command.process(name, location, input);
			}
		};
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
}
