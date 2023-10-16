package de.regnis.ts4th;

import java.io.*;
import java.util.*;

import org.jetbrains.annotations.*;

/**
 * @author Thomas Singer
 */
abstract class DualPeepHoleSimplifier<I> {

	protected abstract void handle(I i1, I i2);

	private final List<I> items;
	private final PrintStream debugOut;

	private int i;

	protected DualPeepHoleSimplifier(List<I> items, @Nullable PrintStream debugOut) {
		this.items = items;
		this.debugOut = debugOut;
	}

	public void process(String name) {
		debug("before " + name);

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

	protected void debug(String name) {
		print(name);
	}

	protected void printError() {
		print("error", true);
	}

	private void print(String name) {
		print(name, false);
	}

	private void print(String name, boolean printCurrentPosition) {
		if (debugOut == null) {
			return;
		}

		debugOut.println(name);

		int index = 0;
		for (I item : items) {
//			stream.print(index);
//			stream.print(" ");
			debugOut.print(item);
			if (printCurrentPosition) {
				if (index == i) {
					debugOut.print("   // <-- current");
				}
			}
			debugOut.println();
			index++;
		}
		debugOut.println();
	}
}
