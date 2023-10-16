package de.regnis.ts4th;

import java.util.*;

/**
 * @author Thomas Singer
 */
public class Interpreter {
	private final Deque<Object> stack = new ArrayDeque<>();
	private final List<Instruction> instructions;

	public Interpreter(List<Instruction> instructions) {
		this.instructions = instructions;
	}

	public final List<Object> evaluate() {
		int ip = 0;
		while (ip < instructions.size()) {
			final Instruction instruction = instructions.get(ip);
			ip++;

			process(instruction);
		}
		return new ArrayList<>(stack);
	}

	protected boolean process(String name) {
		switch (name) {
		case Intrinsics.ADD -> {
			final Pair<Long, Type> b = popInt();
			final Pair<Long, Type> a = popInt();
			final Type bType = b.second();
			final Type aType = a.second();
			if (!Objects.equals(bType, aType)) {
				throw new InterpretingFailedException("Need same type, but got " + bType + "," + aType);
			}

			long result = a.first() + b.first();
			result = toType(result, aType);
			push(result, aType);
			return true;
		}
		case Intrinsics.MUL -> {
			final Pair<Long, Type> b = popInt();
			final Pair<Long, Type> a = popInt();
			final Type bType = b.second();
			final Type aType = a.second();
			if (!Objects.equals(bType, aType)) {
				throw new InterpretingFailedException("Need same type, but got " + bType + "," + aType);
			}

			long result = a.first() * b.first();
			result = toType(result, aType);
			push(result, aType);
			return true;
		}
		case Intrinsics.DIV -> {
			final Pair<Long, Type> b = popInt();
			final Pair<Long, Type> a = popInt();
			final Type bType = b.second();
			final Type aType = a.second();
			if (!Objects.equals(bType, aType)) {
				throw new InterpretingFailedException("Need same type, but got " + bType + "," + aType);
			}

			long result = a.first() / b.first();
			result = toType(result, aType);
			push(result, aType);
			return true;
		}
		default -> {
			return false;
		}
		}
	}

	protected void process(Instruction instruction) {
		if (instruction instanceof Instruction.IntLiteral(long value, Type type)) {
			push(value, type);
			return;
		}

		if (instruction instanceof Instruction.BoolLiteral(boolean value)) {
			push(value);
			return;
		}

		if (instruction instanceof Instruction.Command(String name, _)) {
			if (!process(name)) {
				throw new InterpretingFailedException("Unsupported command " + name);
			}
			return;
		}

		throw new InterpretingFailedException("unsupported instruction " + instruction);
	}

	private long toType(long result, Type type) {
		return switch (type.getByteCount(8)) {
			case 1 -> result & 0xFF;
			case 2 -> result & 0xFFFF;
			case 4 -> result & 0xFFFF_FFFFL;
			case 8 -> result;
			default -> throw new IllegalStateException("unsupported type " + type);
		};
	}

	private void push(long value, Type type) {
		stack.addLast(new Pair<>(value, type));
	}

	private void push(boolean value) {
		stack.addLast(value);
	}

	private Pair<Long, Type> popInt() {
		if (stack.isEmpty()) {
			throw new InterpretingFailedException("empty stack");
		}

		final Object o = stack.removeLast();
		if (o instanceof Pair p) {
			return p;
		}

		throw new InterpretingFailedException("expected int, but got " + o.getClass());
	}
}
