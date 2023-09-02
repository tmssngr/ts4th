package de.regnis.ts4th;

import org.jetbrains.annotations.*;

import java.util.*;

/**
 * @author Thomas Singer
 */
public class CfgFunction {

	private final List<CfgBlock> blocks = new ArrayList<>();
	private final Map<String, CfgBlock> nameToBlock = new HashMap<>();
	private final Map<String, List<String>> nameToPredecessors = new HashMap<>();
	private final Map<String, List<String>> nameToSuccessors = new HashMap<>();
	private final String name;
	private final TypesInOut typesInOut;

	public CfgFunction(Function function) {
		name = function.name();
		typesInOut = function.typesInOut();

		final List<Instruction> instructions = function.instructions();
		String blockName = name + "_" + 0;
		List<Instruction> blockInstructions = new ArrayList<>();
		for (Instruction instruction : instructions) {
			final String label = instruction.getLabel();
			if (label != null) {
				if (blockName != null) {
					ensureLastInstructionIsJumpOrBranch(blockInstructions, label);
					addBlock(blockName, blockInstructions);
					connectBlocks(blockName, label);
				}
				blockName = label;
				blockInstructions = new ArrayList<>();
				continue;
			}

			if (blockInstructions.isEmpty() && blockName == null) {
				throw new IllegalStateException("Expected label, but got " + instruction);
			}

			blockInstructions.add(instruction);
			if (instruction.isJump() || instruction.isBranch()) {
				addBlock(blockName, blockInstructions);
				final List<String> targets = instruction.getTargets();
				for (String target : targets) {
					connectBlocks(blockName, target);
				}
				blockInstructions = new ArrayList<>();
				blockName = null;
			}
		}
		if (blockName != null) {
			addBlock(blockName, blockInstructions);
		}

		Utils.assertTrue(blocks.size() == nameToPredecessors.size());
		Utils.assertTrue(blocks.size() == nameToSuccessors.size());

		for (CfgBlock block : blocks) {
			final String name = block.getName();
			Objects.requireNonNull(nameToPredecessors.get(name));
			Objects.requireNonNull(nameToSuccessors.get(name));
		}
	}

	public String getName() {
		return name;
	}

	public TypesInOut getTypesInOut() {
		return typesInOut;
	}

	public List<CfgBlock> getBlocks() {
		return Collections.unmodifiableList(blocks);
	}

	public List<String> getPredecessors(String blockName) {
		return Collections.unmodifiableList(nameToPredecessors.get(blockName));
	}

	public List<String> getSuccessors(String blockName) {
		return Collections.unmodifiableList(nameToSuccessors.get(blockName));
	}

	public List<Instruction> flatten() {
		final List<Instruction> instructions = new ArrayList<>();
		for (CfgBlock block : blocks) {
			instructions.add(Instruction.label(block.getName()));
			instructions.addAll(block.getInstructions());
		}
		return instructions;
	}

	public void checkTypes(TypeChecker typeChecker) {
		final Map<String, TypeList> blockInputs = new HashMap<>();
		final CfgBlock firstBlock = blocks.get(0);
		blockInputs.put(firstBlock.getName(), typesInOut.in());

		final Set<String> processed = new HashSet<>();
		final Deque<String> pendingNames = new ArrayDeque<>();
		pendingNames.add(firstBlock.getName());

		while (!pendingNames.isEmpty()) {
			final String name = pendingNames.removeFirst();
			if (!processed.add(name)) {
				continue;
			}

			final CfgBlock block = nameToBlock.get(name);
			Objects.requireNonNull(block, name);
			final TypeList input = blockInputs.get(name);
			final TypeList output = block.checkTypes(input, typeChecker);
			final List<String> successors = getSuccessors(name);
			if (successors.isEmpty()) {
				final TypeList expectedOut = typesInOut.out();
				if (!expectedOut.equals(output)) {
					throw new InvalidTypeException("Invalid types after block " + name + ", expected " + expectedOut + ", but got " + output);
				}
				continue;
			}

			for (String successor : successors) {
				final TypeList prevInput = blockInputs.put(successor, output);
				if (prevInput != null) {
					if (!prevInput.equals(output)) {
						throw new InvalidTypeException("Invalid types at begin of block " + successor + ", expected " + prevInput + ", but got " + output);
					}
				}
				else {
					pendingNames.add(successor);
				}
			}
		}
	}

	private void ensureLastInstructionIsJumpOrBranch(List<Instruction> blockInstructions, String label) {
		final Instruction lastInstruction = Utils.getLastOrNull(blockInstructions);
		if (lastInstruction != null) {
			if (lastInstruction.isJump() || lastInstruction.isBranch()) {
				return;
			}
		}

		blockInstructions.add(Instruction.jump(label));
	}

	private void addBlock(String name, List<Instruction> blockInstructions) {
		final CfgBlock block = new CfgBlock(name, blockInstructions);
		blocks.add(block);
		if (nameToBlock.put(name, block) != null) {
			throw new IllegalStateException("duplicate block " + name);
		}

		ensurePredecessors(name);
		ensureSuccessors(name);
	}

	private void connectBlocks(String prev, String next) {
		ensurePredecessors(next)
				.add(prev);
		ensureSuccessors(prev)
				.add(next);
	}

	@NotNull
	private List<String> ensurePredecessors(String blockName) {
		return nameToPredecessors.computeIfAbsent(blockName, s -> new ArrayList<>());
	}

	@NotNull
	private List<String> ensureSuccessors(String blockName) {
		return nameToSuccessors.computeIfAbsent(blockName, s -> new ArrayList<>());
	}
}
