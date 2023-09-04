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
				AsmIRFactory.label("loop"),
				AsmIRFactory.command("foo"),
				AsmIRFactory.jump(Condition.lt, "else"),
				AsmIRFactory.literal(0),
				AsmIRFactory.jump("loop"),
				AsmIRFactory.label("else"),
				AsmIRFactory.literal(1),
				AsmIRFactory.jump("loop")
		), List.of(
				AsmIRFactory.label("loop"),
				AsmIRFactory.command("foo"),
				AsmIRFactory.jump(Condition.lt, "else"),
				AsmIRFactory.literal(0),
				AsmIRFactory.jump("end"),
				AsmIRFactory.label("else"),
				AsmIRFactory.literal(1),
				AsmIRFactory.label("end"),
				AsmIRFactory.jump("loop")
		));
	}

	@Test
	public void testCommandAfterJump() {
		testSimplify(List.of(
				AsmIRFactory.jump(Condition.nz, "else"),
				AsmIRFactory.jump("end"),
				AsmIRFactory.label("else"),
				AsmIRFactory.literal(0),
				AsmIRFactory.label("end")
		), List.of(
				AsmIRFactory.jump(Condition.nz, "else"),
				AsmIRFactory.jump("end"),
				AsmIRFactory.literal(2),
				AsmIRFactory.literal(1),
				AsmIRFactory.label("else"),
				AsmIRFactory.literal(0),
				AsmIRFactory.label("end")
		));
	}

	@Test
	public void testJumpToNext() {
		testSimplify(List.of(
				AsmIRFactory.literal(1)
		), List.of(
				AsmIRFactory.jump("next"),
				AsmIRFactory.label("next"),
				AsmIRFactory.literal(1)
		));

		testSimplify(List.of(
				AsmIRFactory.literal(1)
		), List.of(
				AsmIRFactory.jump(Condition.nz, "next"),
				AsmIRFactory.label("next"),
				AsmIRFactory.literal(1)
		));
	}

	@Test
	public void testRemoveUnusedLabels() {
		testSimplify(List.of(
				AsmIRFactory.literal(1)
		), List.of(
				AsmIRFactory.label("start"),
				AsmIRFactory.literal(1)
		));

		testSimplifyExpectNoChange(List.of(
				AsmIRFactory.jump(Condition.z, "else"),
				AsmIRFactory.literal(1),
				AsmIRFactory.jump("end"),
				AsmIRFactory.label("else"),
				AsmIRFactory.literal(2),
				AsmIRFactory.label("end")
		));
	}

	@Test
	public void testPushPop() {
		testSimplify(List.of(
				AsmIRFactory.literal(1),
				AsmIRFactory.command("print")
		), List.of(
				AsmIRFactory.literal(1),
				AsmIRFactory.push(1, 2),
				AsmIRFactory.pop(1, 2),
				AsmIRFactory.command("print")
		));

		try {
			AsmIRSimplifier.simplify(List.of(
					AsmIRFactory.literal(1),
					AsmIRFactory.push(1, 2),
					AsmIRFactory.pop(1, 1),
					AsmIRFactory.command("print")
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
