package de.regnis.ts4th;

import org.junit.*;

import java.util.*;

import static org.junit.Assert.*;

/**
 * @author Thomas Singer
 */
public class CfgTest {

	@Test
	public void testSimple() {
		final CfgFunction cfgFunction = new CfgFunction(new Function("inc", TypeList.INT, TypeList.INT, false, List.of(
				Instruction.literal(1),
				BuiltinCommands.add(),
				Instruction.ret()
		)));
		assertEquals("inc", cfgFunction.getName());
		assertEquals(new TypesInOut(TypeList.INT, TypeList.INT), cfgFunction.getTypesInOut());
		assertEquals(List.of(
				new CfgBlock("inc_0", List.of(
						Instruction.literal(1),
						BuiltinCommands.add(),
						Instruction.ret()
				))
		), cfgFunction.getBlocks());
		assertPredecessorsSuccessors(List.of(), List.of(), "inc_0", cfgFunction);
		assertEquals(List.of(
				Instruction.label("inc_0"),
				Instruction.literal(1),
				BuiltinCommands.add(),
				Instruction.ret()
		), cfgFunction.flatten());
	}

	@Test
	public void testIf() {
		final CfgFunction cfgFunction = new CfgFunction(new Function("max", TypeList.INT2, TypeList.INT, false, List.of(
				BuiltinCommands.dup2Int(),
				BuiltinCommands.isLT(),
				Instruction.branch("if-1", "endif-1"),

				Instruction.label("if-1"),
				BuiltinCommands.swapInt(),
				Instruction.jump("endif-1"),

				Instruction.label("endif-1"),
				BuiltinCommands.dropInt(),
				Instruction.ret()
		)));
		assertEquals("max", cfgFunction.getName());
		assertEquals(new TypesInOut(TypeList.INT2, TypeList.INT), cfgFunction.getTypesInOut());
		assertEquals(List.of(
				new CfgBlock("max_0", List.of(
						BuiltinCommands.dup2Int(),
						BuiltinCommands.isLT(),
						Instruction.branch("if-1", "endif-1")
				)),
				new CfgBlock("if-1", List.of(
						BuiltinCommands.swapInt(),
						Instruction.jump("endif-1")
				)),
				new CfgBlock("endif-1", List.of(
						BuiltinCommands.dropInt(),
						Instruction.ret()
				))
		), cfgFunction.getBlocks());
		assertPredecessorsSuccessors(List.of(), List.of("if-1", "endif-1"), "max_0", cfgFunction);
		assertPredecessorsSuccessors(List.of("max_0"), List.of("endif-1"), "if-1", cfgFunction);
		assertPredecessorsSuccessors(List.of("max_0", "if-1"), List.of(), "endif-1", cfgFunction);
		assertEquals(List.of(
				Instruction.label("max_0"),
				BuiltinCommands.dup2Int(),
				BuiltinCommands.isLT(),
				Instruction.branch("if-1", "endif-1"),

				Instruction.label("if-1"),
				BuiltinCommands.swapInt(),
				Instruction.jump("endif-1"),

				Instruction.label("endif-1"),
				BuiltinCommands.dropInt(),
				Instruction.ret()
		), cfgFunction.flatten());
	}

	@Test
	public void testLoop() {
		final CfgFunction cfgFunction = new CfgFunction(new Function("loopTest", TypeList.INT2, TypeList.EMPTY, false, List.of(
				Instruction.label("loop-2"),
				BuiltinCommands.overInt(),
				BuiltinCommands.overInt(),
				BuiltinCommands.isGE(),
				Instruction.branch("if-3", "endif-3"),

				Instruction.label("if-3"),
				BuiltinCommands.dropInt(),
				BuiltinCommands.dropInt(),
				Instruction.jump("endloop-2"),

				Instruction.label("endif-3"),
				BuiltinCommands.swapInt(),
				BuiltinCommands.dup(),
				Instruction.command("."),
				Instruction.literal(1),
				BuiltinCommands.add(),
				BuiltinCommands.swapInt(),
				Instruction.jump("loop-2"),

				Instruction.label("endloop-2"),
				Instruction.ret()
		)));
		assertEquals("loopTest", cfgFunction.getName());
		assertEquals(new TypesInOut(TypeList.INT2, TypeList.EMPTY), cfgFunction.getTypesInOut());
		assertEquals(List.of(
				new CfgBlock("loopTest_0", List.of(
						Instruction.jump("loop-2")
				)),
				new CfgBlock("loop-2", List.of(
						BuiltinCommands.overInt(),
						BuiltinCommands.overInt(),
						BuiltinCommands.isGE(),
						Instruction.branch("if-3", "endif-3")
				)),
				new CfgBlock("if-3", List.of(
						BuiltinCommands.dropInt(),
						BuiltinCommands.dropInt(),
						Instruction.jump("endloop-2")
				)),
				new CfgBlock("endif-3", List.of(
						BuiltinCommands.swapInt(),
						BuiltinCommands.dup(),
						Instruction.command("."),
						Instruction.literal(1),
						BuiltinCommands.add(),
						BuiltinCommands.swapInt(),
						Instruction.jump("loop-2")
				)),
				new CfgBlock("endloop-2", List.of(
						Instruction.ret()
				))
		), cfgFunction.getBlocks());
		assertPredecessorsSuccessors(List.of(), List.of("loop-2"), "loopTest_0", cfgFunction);
		assertPredecessorsSuccessors(List.of("loopTest_0", "endif-3"), List.of("if-3", "endif-3"), "loop-2", cfgFunction);
		assertPredecessorsSuccessors(List.of("loop-2"), List.of("endloop-2"), "if-3", cfgFunction);
		assertPredecessorsSuccessors(List.of("loop-2"), List.of("loop-2"), "endif-3", cfgFunction);
		assertPredecessorsSuccessors(List.of("if-3"), List.of(), "endloop-2", cfgFunction);
	}

	@Test
	public void testLoop2() {
		final CfgFunction cfgFunction = new CfgFunction(new Function("gcd", TypeList.INT2, TypeList.INT, false, List.of(
				Instruction.label("while_1"),
				Instruction.literal(true),
				Instruction.branch("whilebody_1", "endwhile_1"),

				Instruction.label("whilebody_1"),
				BuiltinCommands.dup2Int(),
				BuiltinCommands.isLT(),
				Instruction.branch("if_2", "else_2"),

				Instruction.label("if_2"),
				BuiltinCommands.overInt(),
				BuiltinCommands.sub(),
				Instruction.jump("endif_2"),

				Instruction.label("else_2"),
				BuiltinCommands.dup2Int(),
				BuiltinCommands.isGT(),
				Instruction.branch("if_3", "else_3"),

				Instruction.label("if_3"),
				BuiltinCommands.swapInt(),
				BuiltinCommands.overInt(),
				BuiltinCommands.sub(),
				Instruction.jump("endif_3"),

				Instruction.label("else_3"),
				Instruction.jump("endwhile_1"),

				Instruction.label("endif_3"),

				Instruction.label("endif_2"),
				Instruction.jump("while_1"),

				Instruction.label("endwhile_1"),
				BuiltinCommands.dropInt(),
				Instruction.ret()
		)));
		assertEquals("gcd", cfgFunction.getName());
		assertEquals(new TypesInOut(TypeList.INT2, TypeList.INT), cfgFunction.getTypesInOut());
		assertEquals(List.of(
				new CfgBlock("gcd_0", List.of(
						Instruction.jump("while_1")
				)),
				new CfgBlock("while_1", List.of(
						Instruction.literal(true),
						Instruction.branch("whilebody_1", "endwhile_1")
				)),
				new CfgBlock("whilebody_1", List.of(
						BuiltinCommands.dup2Int(),
						BuiltinCommands.isLT(),
						Instruction.branch("if_2", "else_2")
				)),
				new CfgBlock("if_2", List.of(
						BuiltinCommands.overInt(),
						BuiltinCommands.sub(),
						Instruction.jump("endif_2")
				)),
				new CfgBlock("else_2", List.of(
						BuiltinCommands.dup2Int(),
						BuiltinCommands.isGT(),
						Instruction.branch("if_3", "else_3")
				)),
				new CfgBlock("if_3", List.of(
						BuiltinCommands.swapInt(),
						BuiltinCommands.overInt(),
						BuiltinCommands.sub(),
						Instruction.jump("endif_3")
				)),
				new CfgBlock("else_3", List.of(
						Instruction.jump("endwhile_1")
				)),
				new CfgBlock("endif_3", List.of(
						Instruction.jump("endif_2")
				)),
				new CfgBlock("endif_2", List.of(
						Instruction.jump("while_1")
				)),
				new CfgBlock("endwhile_1", List.of(
						BuiltinCommands.dropInt(),
						Instruction.ret()
				))
		), cfgFunction.getBlocks());
		assertPredecessorsSuccessors(List.of(), List.of("while_1"), "gcd_0", cfgFunction);
		assertPredecessorsSuccessors(List.of("gcd_0", "endif_2"), List.of("whilebody_1", "endwhile_1"), "while_1", cfgFunction);
		assertPredecessorsSuccessors(List.of("while_1"), List.of("if_2", "else_2"), "whilebody_1", cfgFunction);
		assertPredecessorsSuccessors(List.of("whilebody_1"), List.of("endif_2"), "if_2", cfgFunction);
		assertPredecessorsSuccessors(List.of("whilebody_1"), List.of("if_3", "else_3"), "else_2", cfgFunction);
		assertPredecessorsSuccessors(List.of("else_2"), List.of("endif_3"), "if_3", cfgFunction);
		assertPredecessorsSuccessors(List.of("else_2"), List.of("endwhile_1"), "else_3", cfgFunction);
		assertPredecessorsSuccessors(List.of("if_3"), List.of("endif_2"), "endif_3", cfgFunction);
		assertPredecessorsSuccessors(List.of("if_2", "endif_3"), List.of("while_1"), "endif_2", cfgFunction);
		assertPredecessorsSuccessors(List.of("while_1", "else_3"), List.of(), "endwhile_1", cfgFunction);
	}

	private void assertPredecessorsSuccessors(List<String> expectedPredecessors, List<Object> expectedSuccessors, String name, CfgFunction cfgFunction) {
		assertEquals(expectedPredecessors, cfgFunction.getPredecessors(name));
		assertEquals(expectedSuccessors, cfgFunction.getSuccessors(name));
	}
}
