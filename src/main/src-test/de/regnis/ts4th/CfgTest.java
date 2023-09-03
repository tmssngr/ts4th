package de.regnis.ts4th;

import org.junit.*;

import java.util.*;

import static de.regnis.ts4th.InstructionFactory.*;
import static org.junit.Assert.*;

/**
 * @author Thomas Singer
 */
public class CfgTest {

	@Test
	public void testSimple() {
		final CfgFunction cfgFunction = new CfgFunction(new Function("inc", TypeList.INT, TypeList.INT, false, List.of(
				literal(1),
				add(),
				ret()
		)));
		assertEquals("inc", cfgFunction.getName());
		assertEquals(new TypesInOut(TypeList.INT, TypeList.INT), cfgFunction.getTypesInOut());
		assertEquals(List.of(
				new CfgBlock("inc_0", List.of(
						literal(1),
						add(),
						ret()
				))
		), cfgFunction.getBlocks());
		assertPredecessorsSuccessors(List.of(), List.of(), "inc_0", cfgFunction);
		assertEquals(List.of(
				label("inc_0"),
				literal(1),
				add(),
				ret()
		), cfgFunction.flatten());
	}

	@Test
	public void testIf() {
		final CfgFunction cfgFunction = new CfgFunction(new Function("max", TypeList.INT2, TypeList.INT, false, List.of(
				dup2Int(),
				isLT(),
				branch("if-1", "endif-1"),

				label("if-1"),
				swapInt(),
				jump("endif-1"),

				label("endif-1"),
				dropInt(),
				ret()
		)));
		assertEquals("max", cfgFunction.getName());
		assertEquals(new TypesInOut(TypeList.INT2, TypeList.INT), cfgFunction.getTypesInOut());
		assertEquals(List.of(
				new CfgBlock("max_0", List.of(
						dup2Int(),
						isLT(),
						branch("if-1", "endif-1")
				)),
				new CfgBlock("if-1", List.of(
						swapInt(),
						jump("endif-1")
				)),
				new CfgBlock("endif-1", List.of(
						dropInt(),
						ret()
				))
		), cfgFunction.getBlocks());
		assertPredecessorsSuccessors(List.of(), List.of("if-1", "endif-1"), "max_0", cfgFunction);
		assertPredecessorsSuccessors(List.of("max_0"), List.of("endif-1"), "if-1", cfgFunction);
		assertPredecessorsSuccessors(List.of("max_0", "if-1"), List.of(), "endif-1", cfgFunction);
		assertEquals(List.of(
				label("max_0"),
				dup2Int(),
				isLT(),
				branch("if-1", "endif-1"),

				label("if-1"),
				swapInt(),
				jump("endif-1"),

				label("endif-1"),
				dropInt(),
				ret()
		), cfgFunction.flatten());
	}

	@Test
	public void testLoop() {
		final CfgFunction cfgFunction = new CfgFunction(new Function("loopTest", TypeList.INT2, TypeList.EMPTY, false, List.of(
				label("loop-2"),
				overInt(),
				overInt(),
				isGE(),
				branch("if-3", "endif-3"),

				label("if-3"),
				dropInt(),
				dropInt(),
				jump("endloop-2"),

				label("endif-3"),
				swapInt(),
				dup(),
				command("."),
				literal(1),
				add(),
				swapInt(),
				jump("loop-2"),

				label("endloop-2"),
				ret()
		)));
		assertEquals("loopTest", cfgFunction.getName());
		assertEquals(new TypesInOut(TypeList.INT2, TypeList.EMPTY), cfgFunction.getTypesInOut());
		assertEquals(List.of(
				new CfgBlock("loopTest_0", List.of(
						jump("loop-2")
				)),
				new CfgBlock("loop-2", List.of(
						overInt(),
						overInt(),
						isGE(),
						branch("if-3", "endif-3")
				)),
				new CfgBlock("if-3", List.of(
						dropInt(),
						dropInt(),
						jump("endloop-2")
				)),
				new CfgBlock("endif-3", List.of(
						swapInt(),
						dup(),
						command("."),
						literal(1),
						add(),
						swapInt(),
						jump("loop-2")
				)),
				new CfgBlock("endloop-2", List.of(
						ret()
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
				label("while_1"),
				literal(true),
				branch("whilebody_1", "endwhile_1"),

				label("whilebody_1"),
				dup2Int(),
				isLT(),
				branch("if_2", "else_2"),

				label("if_2"),
				overInt(),
				sub(),
				jump("endif_2"),

				label("else_2"),
				dup2Int(),
				isGT(),
				branch("if_3", "else_3"),

				label("if_3"),
				swapInt(),
				overInt(),
				sub(),
				jump("endif_3"),

				label("else_3"),
				jump("endwhile_1"),

				label("endif_3"),

				label("endif_2"),
				jump("while_1"),

				label("endwhile_1"),
				dropInt(),
				ret()
		)));
		assertEquals("gcd", cfgFunction.getName());
		assertEquals(new TypesInOut(TypeList.INT2, TypeList.INT), cfgFunction.getTypesInOut());
		assertEquals(List.of(
				new CfgBlock("gcd_0", List.of(
						jump("while_1")
				)),
				new CfgBlock("while_1", List.of(
						literal(true),
						branch("whilebody_1", "endwhile_1")
				)),
				new CfgBlock("whilebody_1", List.of(
						dup2Int(),
						isLT(),
						branch("if_2", "else_2")
				)),
				new CfgBlock("if_2", List.of(
						overInt(),
						sub(),
						jump("endif_2")
				)),
				new CfgBlock("else_2", List.of(
						dup2Int(),
						isGT(),
						branch("if_3", "else_3")
				)),
				new CfgBlock("if_3", List.of(
						swapInt(),
						overInt(),
						sub(),
						jump("endif_3")
				)),
				new CfgBlock("else_3", List.of(
						jump("endwhile_1")
				)),
				new CfgBlock("endif_3", List.of(
						jump("endif_2")
				)),
				new CfgBlock("endif_2", List.of(
						jump("while_1")
				)),
				new CfgBlock("endwhile_1", List.of(
						dropInt(),
						ret()
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
