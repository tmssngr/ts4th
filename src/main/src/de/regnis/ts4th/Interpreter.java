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
			final int a = popInt();
			final int b = popInt();
			push(a + b);
			return true;
		}
		case Intrinsics.MUL -> {
			final int a = popInt();
			final int b = popInt();
			push(a * b);
			return true;
		}
		default -> {
			return false;
		}
		}
	}

	protected final void push(int value) {
		stack.addLast(value);
	}

	protected final void push(boolean value) {
		stack.addLast(value);
	}

	protected void process(Instruction instruction) {
		if (instruction instanceof Instruction.IntLiteral(int value)) {
			push(value);
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

	private int popInt() {
		if (stack.isEmpty()) {
			throw new InterpretingFailedException("empty stack");
		}

		final Object o = stack.removeLast();
		if (o instanceof Integer i) {
			return i;
		}

		throw new InterpretingFailedException("expected int, but got " + o.getClass());
	}
}
