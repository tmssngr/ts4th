package de.regnis.ts4th;

import java.util.*;

import org.jetbrains.annotations.*;

/**
 * @author Thomas Singer
 */
public final class NameToFunction {

	private final Map<String, Function> nameToFunction = new HashMap<>();

	public NameToFunction(@NotNull List<Function> functions) {
		for (Function function : functions) {
			final Function prevFunction = nameToFunction.put(function.name(), function);
			if (prevFunction != null) {
				throw new CompilerException(function.location(), STR. "Duplicate declaration of function \{ function.name() }. It was already defined at \{ prevFunction.location() }" );
			}
		}
	}

	@Nullable
	public Function get(@NotNull String name) {
		return nameToFunction.get(name);
	}
}
