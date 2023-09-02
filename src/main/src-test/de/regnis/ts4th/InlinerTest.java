package de.regnis.ts4th;

import java.util.*;

import org.junit.*;

import static org.junit.Assert.*;

/**
 * @author Thomas Singer
 */
public class InlinerTest {

	@Test
	public void testInline() {
		assertEquals(List.of(
				             new Function("main", TypeList.EMPTY, TypeList.EMPTY, false, List.of(
						             Instruction.literal(10),
						             Instruction.literal(1),
						             Instruction.command("+"),
						             Instruction.literal(1),
						             Instruction.command("+"),
						             Instruction.command("print")
				             ))
		             ),
		             Inliner.process(List.of(
				             new Function("1+", TypeList.INT, TypeList.INT, true, List.of(
						             Instruction.literal(1),
						             Instruction.command("+")
				             )),
				             new Function("main", TypeList.EMPTY, TypeList.EMPTY, false, List.of(
						             Instruction.literal(10),
						             Instruction.command("1+"),
						             Instruction.command("1+"),
						             Instruction.command("print")
				             ))
		             )));
	}

	@Test
	public void testInlineFailure() {
		try {
			Inliner.process(List.of(
					new Function("1+", TypeList.INT, TypeList.INT, true, List.of(
					)),
					new Function("main", TypeList.EMPTY, TypeList.EMPTY, true, List.of(
					))
			));
			fail();
		}
		catch (IllegalStateException ignored) {
		}

		try {
			Inliner.process(List.of(
					new Function("1+", TypeList.INT, TypeList.INT, true, List.of(
							Instruction.command("1+")
					)),
					new Function("main", TypeList.EMPTY, TypeList.EMPTY, false, List.of(
					))
			));
			fail();
		}
		catch (IllegalStateException ignored) {
		}
	}
}
