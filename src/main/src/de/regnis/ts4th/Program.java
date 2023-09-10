package de.regnis.ts4th;

import java.util.*;

import org.jetbrains.annotations.*;

/**
 * @author Thomas Singer
 */
public record Program(List<Function> functions) {

	public static Program fromDeclarations(List<Declaration> declarations) {
		final Map<String, Location> names = new HashMap<>();
		final Map<String, Const> nameToConst = new HashMap<>();
		final List<Function> functions = new ArrayList<>();
		for (Declaration declaration : declarations) {
			final String name = declaration.name();
			final Location location = declaration.location();
			final Location prevLocation = names.get(name);
			if (prevLocation != null) {
				throw new CompilerException(location + ": '" + name + "' has already been declared at " + prevLocation);
			}

			names.put(name, location);

			if (declaration instanceof Function f) {
				functions.add(f);
				continue;
			}

			if (declaration instanceof ConstDeclaration c) {
				nameToConst.put(name, Const.evaluate(c, nameToConst::get));
				continue;
			}

			throw new IllegalStateException("not implemented");
		}

		final List<Function> functionsWithInlinedConsts = new ArrayList<>();
		for (Function function : functions) {
			final List<Instruction> instructions = inlineConsts(function.instructions(), nameToConst);
			functionsWithInlinedConsts.add(new Function(function.location(), function.name(), function.typesInOut(), function.isInline(), instructions));
		}

		return new Program(functionsWithInlinedConsts);
	}

	@Nullable
	public Function get(String name) {
		for (Function function : functions) {
			if (function.name().equals(name)) {
				return function;
			}
		}
		return null;
	}

	private static List<Instruction> inlineConsts(List<Instruction> instructions, Map<String, Const> nameToConst) {
		final List<Instruction> resultingInstructions = new ArrayList<>(instructions.size());
		for (Instruction instruction : instructions) {
			if (instruction instanceof Instruction.Command command) {
				final Const constant = nameToConst.get(command.name());
				if (constant != null) {
					instruction = constant.createInstruction();
				}
			}
			resultingInstructions.add(instruction);
		}
		return resultingInstructions;
	}
}
