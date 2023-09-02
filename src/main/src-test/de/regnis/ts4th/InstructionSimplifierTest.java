package de.regnis.ts4th;

import org.junit.*;

import java.util.*;

import static de.regnis.ts4th.Instruction.*;
import static org.junit.Assert.*;

/**
 * @author Thomas Singer
 */
public class InstructionSimplifierTest {

	@Test
	public void testRemoveIndirectLabel() {
		testSimplify(List.of(
				label("loop"),
				branch("if", "else"),
				label("if"),
				literal(0),
				jump("loop"),
				label("else"),
				literal(1),
				jump("loop")
		), List.of(
				label("loop"),
				branch("if", "else"),
				label("if"),
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
				label("start"),
				command("foo"),
				branch("start", "end"),
				jump("start"),
				label("end")
		), List.of(
				label("start"),
				command("foo"),
				branch("start", "end"),
				jump("start"),
				command("foo"),
				literal(1),
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
				branch("if", "else"),
				label("if"),
				literal(1),
				jump("end"),
				label("else"),
				literal(2),
				label("end")
		));
	}

	@Test
	public void testBoolConstBranch() {
		testSimplify(List.of(
				literal(1)
		), List.of(
				literal(true),
				branch("if", "else"),
				label("if"),
				literal(1),
				jump("end"),
				label("else"),
				literal(2),
				label("end")
		));

		testSimplify(List.of(
				literal(2)
		), List.of(
				literal(false),
				branch("if", "else"),
				label("if"),
				literal(1),
				jump("end"),
				label("else"),
				literal(2),
				label("end")
		));
	}

	private static void testSimplify(List<Instruction> expected, List<Instruction> input) {
		assertEquals(expected,
		             InstructionSimplifier.simplify(input));
	}

	private static void testSimplifyExpectNoChange(List<Instruction> input) {
		testSimplify(input, input);
	}
}
