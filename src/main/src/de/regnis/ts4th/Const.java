package de.regnis.ts4th;

import java.util.*;
import java.util.function.Function;

/**
 * @author Thomas Singer
 */
public record Const(String name, Type type, Object value) {

	public static Const intConst(String name, int value) {
		return new Const(name, Type.Int, value);
	}

	public static Const evaluate(ConstDeclaration declaration, Function<String, Const> existingConsts) {
		final Interpreter interpreter = new Interpreter(declaration.instructions()) {
			@Override
			protected boolean process(String name) {
				if (super.process(name)) {
					return true;
				}

				final Const existingConst = existingConsts.apply(name);
				if (existingConst != null) {
					if (existingConst.type() == Type.Int) {
						push((Integer)existingConst.value);
						return true;
					}

					throw new CompilerException("unsupported type " + existingConst.type);
				}

				return false;
			}
		};
		final List<Object> results = interpreter.evaluate();
		if (results.size() != 1) {
			throw new CompilerException(declaration.location() + ": expected 1 resulting value, but got " + results.size());
		}

		final Object result = results.get(0);
		if (result instanceof Integer i) {
			return intConst(declaration.name(), i);
		}

		throw new CompilerException(declaration.location() + ": unsupported const type " + result);
	}

	public Instruction createInstruction() {
		Utils.assertTrue(type == Type.Int);

		return new Instruction.IntLiteral((Integer)value);
	}
}
