package de.regnis.ts4th;

import java.util.*;
import java.util.function.Function;

/**
 * @author Thomas Singer
 */
public record ConstDeclaration(Location location, String name, List<Instruction> instructions) implements Declaration {

	public ConstDeclaration(String name, List<Instruction> instructions) {
		this(Location.DUMMY, name, instructions);
	}

	public Instruction evaluate(Function<String, Instruction> existingConsts) {
		final Interpreter interpreter = new Interpreter(instructions()) {
			@Override
			protected boolean process(String name) {
				if (super.process(name)) {
					return true;
				}

				final Instruction existingConst = existingConsts.apply(name);
				if (existingConst != null) {
					process(existingConst);
					return true;
				}

				return false;
			}
		};
		final List<Object> results = interpreter.evaluate();
		if (results.size() != 1) {
			throw new CompilerException(location() + ": expected 1 resulting value, but got " + results.size());
		}

		final Object result = results.get(0);
		if (result instanceof Pair p) {
			return InstructionFactory.literal((Long)p.first(), (Type)p.second());
		}

		if (result instanceof Boolean b) {
			return InstructionFactory.literal(b);
		}

		throw new CompilerException(location() + ": unsupported const type " + result);
	}
}
