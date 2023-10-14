package de.regnis.ts4th;

import org.jetbrains.annotations.*;

/**
 * @author Thomas Singer
 */
public record LocalVarStack(@NotNull String name, @NotNull Type type, @Nullable LocalVarStack prev) {
	@Override
	public String toString() {
		final StringBuilder buffer = new StringBuilder();
		appendString(buffer);
		return buffer.toString();
	}

	public int size() {
		int count = 0;
		for (LocalVarStack stack = this; stack != null; stack = stack.prev) {
			count++;
		}
		return count;
	}

	private void appendString(StringBuilder buffer) {
		if (prev != null) {
			prev.appendString(buffer);
			buffer.append(", ");
		}
		buffer.append(name);
		buffer.append("(");
		buffer.append(type);
		buffer.append(')');
	}
}
