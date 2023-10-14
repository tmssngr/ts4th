package de.regnis.ts4th;

import java.util.*;
import java.util.function.Function;

/**
 * @author Thomas Singer
 */
public record Var(int index, String name, int size) {

	public static Var evaluate(VarDeclaration declaration, int index, Function<String, Instruction> consts) {
		final Interpreter interpreter = new Interpreter(declaration.instructions()) {
			@Override
			protected boolean process(String name) {
				if (super.process(name)) {
					return true;
				}

				final Instruction existingConst = consts.apply(name);
				if (existingConst != null) {
					process(existingConst);
					return true;
				}

				return false;
			}
		};
		final List<Object> results = interpreter.evaluate();
		if (results.size() != 1) {
			throw new CompilerException(declaration.location() + ": expected 1 resulting value, but got " + results.size());
		}

		final Object result = results.get(0);
		if (result instanceof Pair p) {
			return new Var(index, declaration.name(), ((Long)p.first()).intValue());
		}

		throw new CompilerException(declaration.location() + ": unsupported const type " + result);
	}

	public Instruction createInstruction() {
		return new Instruction.PtrLiteral(index, name);
	}
}
