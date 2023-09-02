package de.regnis.ts4th;

import org.junit.*;

import java.util.*;

import static de.regnis.ts4th.AsmIR.*;
import static org.junit.Assert.*;

/**
 * @author Thomas Singer
 */
public class AsmIRSimplifierTest {

	@Test
	public void testRemoveIndirectLabel() {
		testSimplify(List.of(
				label("loop"),
				command("foo", 0, 0),
				jump(Condition.lt, "else"),
				literal(0),
				jump("loop"),
				label("else"),
				literal(1),
				jump("loop")
		), List.of(
				label("loop"),
				command("foo", 0, 0),
				jump(Condition.lt, "else"),
				literal(0),
				jump("end"),
				label("else"),
				literal(1),
				label("end"),
				jump("loop")
		));
	}

	@Test
	public void testCommandAfterJump() {
		testSimplify(List.of(
				jump(Condition.nz, "else"),
				jump("end"),
				label("else"),
				literal(0),
				label("end")
		), List.of(
				jump(Condition.nz, "else"),
				jump("end"),
				literal(2),
				literal(1),
				label("else"),
				literal(0),
				label("end")
		));
	}

	@Test
	public void testJumpToNext() {
		testSimplify(List.of(
				literal(1)
		), List.of(
				jump("next"),
				label("next"),
				literal(1)
		));

		testSimplify(List.of(
				literal(1)
		), List.of(
				jump(Condition.nz, "next"),
				label("next"),
				literal(1)
		));
	}

	@Test
	public void testRemoveUnusedLabels() {
		testSimplify(List.of(
				literal(1)
		), List.of(
				label("start"),
				literal(1)
		));

		testSimplifyExpectNoChange(List.of(
				jump(Condition.z, "else"),
				literal(1),
				jump("end"),
				label("else"),
				literal(2),
				label("end")
		));
	}

	@Test
	public void testPushPop() {
		testSimplify(List.of(
				literal(1),
				command("print", 0, 0)
		), List.of(
				literal(1),
				push(1, 2),
				pop(1, 2),
				command("print", 0, 0)
		));

		try {
			AsmIRSimplifier.simplify(List.of(
					literal(1),
					push(1, 2),
					pop(1, 1),
					command("print", 0, 0)
			));
			fail();
		}
		catch (IllegalStateException ignored) {
		}
	}

	private static void testSimplify(List<AsmIR> expected, List<AsmIR> input) {
		assertEquals(expected,
		             AsmIRSimplifier.simplify(input));
	}

	private static void testSimplifyExpectNoChange(List<AsmIR> input) {
		testSimplify(input, input);
	}
}