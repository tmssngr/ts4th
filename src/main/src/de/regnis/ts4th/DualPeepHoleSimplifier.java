package de.regnis.ts4th;

import java.util.*;

/**
 * @author Thomas Singer
 */
abstract class DualPeepHoleSimplifier<I> {
	protected abstract void handle(I i1, I i2);

	private final List<I> instructions;

	private int i;

	protected DualPeepHoleSimplifier(List<I> instructions) {
		this.instructions = instructions;
	}

	public void process() {
		i = 0;
		for (; i < instructions.size() - 1; i++) {
			final I i1 = instructions.get(i);
			final I i2 = instructions.get(i + 1);
			handle(i1, i2);
		}
	}

	protected void remove() {
		instructions.remove(i);
	}

	protected void removeNext() {
		instructions.remove(i + 1);
	}

	protected void insert(I command) {
		instructions.add(i, command);
	}

	protected void replace(I command) {
		remove();
		insert(command);
	}

	protected void again() {
		i--;
	}
}
