package de.regnis.ts4th;

import java.util.*;
import java.util.function.*;

import org.jetbrains.annotations.*;

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
			if (!(instruction instanceof Instruction.Command(String command, _))) {
				continue;
			}

			if (Intrinsics.get(command) != null) {
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
		inline(function, false, inlinedInstructions::add, new LabelMapping());
		return new Function(function.locationStart(), function.locationEnd(), function.name(), function.typesInOut(), function.isInline(), inlinedInstructions);
	}

	private void inline(Function function, boolean skipRet, Consumer<Instruction> consumer, LabelMapping labelMapping) {
		final List<Instruction> instructions = function.instructions();
		for (Instruction instruction : instructions) {
			if (instruction instanceof Instruction.Label(String oldLabel, Location location)) {
				final String newLabel = labelMapping.getNew(oldLabel);
				instruction = new Instruction.Label(newLabel, location);
			}
			else if (instruction instanceof Instruction.Jump(String oldLabel, Location location)) {
				final String newLabel = labelMapping.getNew(oldLabel);
				instruction = new Instruction.Jump(newLabel, location);
			}
			else if (instruction instanceof Instruction.Branch(String oldIfTarget, String oldElseTarget, Location location)) {
				final String newIfLabel = labelMapping.getNew(oldIfTarget);
				final String newElseLabel = labelMapping.getNew(oldElseTarget);
				instruction = new Instruction.Branch(newIfLabel, newElseLabel, location);
			}
			else if (instruction instanceof Instruction.Command(String command, _)) {
				if (Intrinsics.get(command) == null) {
					final Function invokedFunction = nameToFunction.get(command);
					if (invokedFunction != null && invokedFunction.isInline()) {
						inline(invokedFunction, true, consumer, labelMapping.createChild());
						continue;
					}
				}
			}
			else if (instruction instanceof Instruction.Ret) {
				final int i = instructions.indexOf(instruction);
				Utils.assertTrue(i == instructions.size() - 1);

				if (skipRet) {
					continue;
				}
			}

			consumer.accept(instruction);
		}
	}

	private static final class LabelFactory {
		private int next;

		public String produce() {
			next++;
			return ".i" + next;
		}
	}

	private static final class LabelMapping {
		private final Map<String, String> oldToNew = new HashMap<>();
		private final LabelFactory labelFactory;

		public LabelMapping() {
			this.labelFactory = new LabelFactory();
		}

		private LabelMapping(@NotNull LabelFactory labelFactory) {
			this.labelFactory = labelFactory;
		}

		@NotNull
		public String getNew(@NotNull String oldLabel) {
			String newLabel = oldToNew.get(oldLabel);
			if (newLabel == null) {
				newLabel = labelFactory.produce();
				oldToNew.put(oldLabel, newLabel);
			}
			return newLabel;
		}

		public LabelMapping createChild() {
			return new LabelMapping(labelFactory);
		}
	}
}
