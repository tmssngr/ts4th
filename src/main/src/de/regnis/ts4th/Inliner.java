package de.regnis.ts4th;

import java.util.*;
import java.util.function.*;

/**
 * @author Thomas Singer
 */
public class Inliner {

	public static List<Function> process(List<Function> functions) {
		final Inliner inliner = new Inliner(functions);
		inliner.checkCanInline(functions);
		inliner.inline(functions);

		final List<Function> resultingFunctions = new ArrayList<>();
		for (Function function : functions) {
			if (function.isInline()) {
				continue;
			}

			final Function possiblyReplaced = Objects.requireNonNull(inliner.nameToFunction.get(function.name()));
			resultingFunctions.add(possiblyReplaced);
		}
		return resultingFunctions;
	}

	private final Map<String, Function> nameToFunction = new HashMap<>();

	private Inliner(List<Function> functions) {
		for (Function function : functions) {
			final boolean duplicate = nameToFunction.put(function.name(), function) != null;
			Utils.assertTrue(!duplicate);
		}
	}

	private void checkCanInline(List<Function> functions) {
		for (Function function : functions) {
			if (function.isInline()) {
				checkCanInline(function);
			}
		}
	}

	private void checkCanInline(Function function) {
		if (function.name().equals("main")) {
			throw new IllegalStateException("Can't inline main function");
		}

		final List<String> callStack = new ArrayList<>();
		checkCanInline(function, callStack);
	}

	private void checkCanInline(Function function, List<String> callStack) {
		callStack.add(function.name());

		for (Instruction instruction : function.instructions()) {
			final String command = instruction.getCommand();
			if (command == null) {
				continue;
			}

			if (BuiltinCommands.get(command) != null) {
				continue;
			}

			if (callStack.contains(command)) {
				throw new IllegalStateException("Can't inline " + callStack.get(0) + " because " + command + " is invoked recursively");
			}

			final Function invokedFunction = Objects.requireNonNull(nameToFunction.get(command));
			checkCanInline(invokedFunction, callStack);
		}
	}

	private void inline(List<Function> functions) {
		for (Function function : functions) {
			final Function inlined = inline(function);
			nameToFunction.put(inlined.name(), inlined);
		}
	}

	private Function inline(Function function) {
		final List<Instruction> inlinedInstructions = new ArrayList<>();
		inline(function, inlinedInstructions::add);
		return new Function(function.name(), function.typesInOut(), function.isInline(), inlinedInstructions);
	}

	private void inline(Function function, Consumer<Instruction> consumer) {
		final List<Instruction> instructions = function.instructions();
		for (Instruction instruction : instructions) {
			final String command = instruction.getCommand();
			if (command != null) {
				if (BuiltinCommands.get(command) == null) {
					final Function invokedFunction = Objects.requireNonNull(nameToFunction.get(command));
					if (invokedFunction.isInline()) {
						inline(invokedFunction, consumer);
						continue;
					}
				}
			}

			consumer.accept(instruction);
		}
	}
}
