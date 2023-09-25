package de.regnis.ts4th;

import java.util.*;

/**
 * @author Thomas Singer
 */
public record Program(List<Function> functions, List<Var> vars) {

	public static Program fromDeclarations(List<Declaration> declarations) {
		final Map<String, Location> names = new HashMap<>();
		final Map<String, Instruction> nameToConst = new HashMap<>();
		final Map<String, Var> nameToVar = new HashMap<>();
		final List<Var> vars = new ArrayList<>();
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
				nameToConst.put(name, c.evaluate(nameToConst::get));
				continue;
			}

			if (declaration instanceof VarDeclaration v) {
				final Var var = Var.evaluate(v, vars.size(), nameToConst::get);
				nameToVar.put(name, var);
				vars.add(var);
				continue;
			}

			throw new IllegalStateException("not implemented");
		}

		final List<Function> functionsWithInlinedConsts = new ArrayList<>();
		for (Function function : functions) {
			final List<Instruction> instructions = inlineConstsAndVars(function.instructions(), nameToConst, nameToVar);
			functionsWithInlinedConsts.add(new Function(function.locationStart(), function.locationEnd(), function.name(), function.typesInOut(), function.isInline(), instructions));
		}

		return new Program(functionsWithInlinedConsts, vars);
	}

	private static List<Instruction> inlineConstsAndVars(List<Instruction> instructions, Map<String, Instruction> nameToConst, Map<String, Var> nameToVar) {
		final List<Instruction> resultingInstructions = new ArrayList<>(instructions.size());
		for (Instruction instruction : instructions) {
			if (instruction instanceof Instruction.Command command) {
				final String name = command.name();
				final Instruction constant = nameToConst.get(name);
				if (constant != null) {
					instruction = constant;
				}
				else {
					final Var var = nameToVar.get(name);
					if (var != null) {
						instruction = var.createInstruction();
					}
				}
			}
			resultingInstructions.add(instruction);
		}
		return resultingInstructions;
	}
}
