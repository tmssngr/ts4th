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
		Location location = function.locationStart();
		List<Instruction> blockInstructions = new ArrayList<>();
		for (Instruction instruction : instructions) {
			if (instruction instanceof Instruction.Label l) {
				final String label = l.name();
				if (blockName != null) {
					ensureLastInstructionIsJumpOrBranch(blockInstructions, label);
					addBlock(blockName, blockInstructions, location, l.location());
					connectBlocks(blockName, label);
				}
				blockName = label;
				blockInstructions = new ArrayList<>();
				location = l.location();
				continue;
			}

			if (blockInstructions.isEmpty() && blockName == null) {
				throw new IllegalStateException("Expected label, but got " + instruction);
			}

			blockInstructions.add(instruction);
			if (instruction instanceof Instruction.Jump j) {
				addBlock(blockName, blockInstructions, location, j.location());
				connectBlocks(blockName, j.target());
				blockInstructions = new ArrayList<>();
				blockName = null;
			}
			else if (instruction instanceof Instruction.Branch b) {
				addBlock(blockName, blockInstructions, location, b.location());
				connectBlocks(blockName, b.ifTarget());
				connectBlocks(blockName, b.elseTarget());
				blockInstructions = new ArrayList<>();
				blockName = null;
			}
		}
		if (blockName != null) {
			addBlock(blockName, blockInstructions, location, function.locationEnd());
		}

		Utils.assertTrue(blocks.size() == nameToPredecessors.size());
		Utils.assertTrue(blocks.size() == nameToSuccessors.size());

		for (CfgBlock block : blocks) {
			final String name = block.name();
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
			instructions.add(new Instruction.Label(block.name()));
			instructions.addAll(block.instructions());
		}
		return instructions;
	}

	public void debugPrint() {
		final List<Instruction> instructions = flatten();
		for (Instruction instruction : instructions) {
			if (instruction instanceof Instruction.Label l) {
				System.out.print(instruction);
				final List<String> predecessors = nameToPredecessors.get(l.name());
				if (predecessors.size() > 0) {
					System.out.print("   // from: ");
					System.out.print(Utils.join(predecessors, ", "));
				}
				System.out.println();
			}
			else {
				System.out.print("    ");
				System.out.println(instruction);
			}
		}
	}

	public void checkTypes(TypeChecker typeChecker) {
		final Map<String, TypeList> blockInputs = new HashMap<>();
		final CfgBlock firstBlock = blocks.get(0);
		blockInputs.put(firstBlock.name(), typesInOut.in());

		final Set<String> processed = new HashSet<>();
		final Deque<String> pendingNames = new ArrayDeque<>();
		pendingNames.add(firstBlock.name());

		while (!pendingNames.isEmpty()) {
			final String name = pendingNames.removeFirst();
			if (!processed.add(name)) {
				continue;
			}

			final CfgBlock block = Objects.requireNonNull(nameToBlock.get(name), name);
			final TypeList input = Objects.requireNonNull(blockInputs.get(name), block.locationStart().toString());

			final TypeList output = Objects.requireNonNull(block.checkTypes(input, typeChecker), name);

			final List<String> successors = getSuccessors(name);
			if (successors.isEmpty()) {
				final TypeList expectedOut = typesInOut.out();
				if (!expectedOut.equals(output)) {
					throw new InvalidTypeException(block.locationEnd() + ": Invalid types! Expected " + expectedOut + ", but got " + output);
				}
				continue;
			}

			for (String successor : successors) {
				final TypeList prevInput = blockInputs.put(successor, output);
				if (prevInput != null) {
					if (!prevInput.equals(output)) {
						throw new InvalidTypeException(block.locationStart() + ": Invalid types! Expected " + prevInput + ", but got " + output);
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
			if (lastInstruction instanceof Instruction.Jump || lastInstruction instanceof Instruction.Branch) {
				return;
			}
		}

		blockInstructions.add(new Instruction.Jump(label));
	}

	private void addBlock(String name, List<Instruction> blockInstructions, Location locationStart, Location locationEnd) {
		final CfgBlock block = new CfgBlock(name, blockInstructions, locationStart, locationEnd);
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
