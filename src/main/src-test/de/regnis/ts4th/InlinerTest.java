package de.regnis.ts4th;

import java.util.*;

import org.junit.*;

import static de.regnis.ts4th.InstructionFactory.*;
import static org.junit.Assert.*;

/**
 * @author Thomas Singer
 */
public class InlinerTest {

	@Test
	public void testInline() {
		assertEquals(List.of(
				             new Function("main", TypeList.EMPTY, TypeList.EMPTY, false, List.of(
						             literal(10),
						             literal(1),
						             command("+"),
						             literal(1),
						             command("+"),
						             command("print")
				             ))
		             ),
		             Inliner.process(List.of(
				             new Function("1+", TypeList.INT, TypeList.INT, true, List.of(
						             literal(1),
						             command("+")
				             )),
				             new Function("main", TypeList.EMPTY, TypeList.EMPTY, false, List.of(
						             literal(10),
						             command("1+"),
						             command("1+"),
						             command("print")
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
							command("1+")
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
