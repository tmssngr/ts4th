package de.regnis.ts4th;

import java.util.*;

/**
 * @author Thomas Singer
 */
public record Program(List<Function> functions) {

	public static Program fromDeclarations(List<Declaration> declarations) {
		final Map<String, Location> names = new HashMap<>();
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

			throw new IllegalStateException("not implemented");
		}
		return new Program(functions);
	}
}
