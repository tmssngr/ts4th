package de.regnis.ts4th;

import java.util.*;

/**
 * @author Thomas Singer
 */
abstract class DualPeepHoleSimplifier<I> {
	protected abstract void handle(I i1, I i2);

	private final List<I> items;

	private int i;

	protected DualPeepHoleSimplifier(List<I> items) {
		this.items = items;
	}

	public void process() {
		i = 0;
		for (; i < items.size() - 1; i++) {
			final I i1 = items.get(i);
			final I i2 = items.get(i + 1);
			handle(i1, i2);
		}
	}

	protected void remove() {
		items.remove(i);
	}

	protected void removeNext() {
		items.remove(i + 1);
	}

	protected void insert(I item) {
		items.add(i, item);
	}

	protected void replace(I item) {
		remove();
		insert(item);
	}

	protected void again() {
		i--;
	}

	protected void print() {
		for (I item : items) {
			System.err.println(item);
		}
	}
}
