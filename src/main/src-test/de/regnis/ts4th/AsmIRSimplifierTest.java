package de.regnis.ts4th;

import java.util.*;

import org.junit.*;

import static de.regnis.ts4th.AsmIR.Condition;
import static de.regnis.ts4th.AsmIRFactory.*;
import static org.junit.Assert.*;

/**
 * @author Thomas Singer
 */
public class AsmIRSimplifierTest {

	@Test
	public void testRemoveIndirectLabel() {
		testSimplify(List.of(
				label("loop"),
				call("foo"),
				jump(Condition.lt, "else"),
				literal(0, 0),
				jump("loop"),
				label("else"),
				literal(0, 1),
				jump("loop")
		), List.of(
				label("loop"),
				call("foo"),
				jump(Condition.lt, "else"),
				literal(0, 0),
				jump("end"),
				label("else"),
				literal(0, 1),
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
				literal(0, 0),
				label("end")
		), List.of(
				jump(Condition.nz, "else"),
				jump("end"),
				literal(0, 2),
				literal(0, 1),
				label("else"),
				literal(0, 0),
				label("end")
		));
	}

	@Test
	public void testJumpToNext() {
		testSimplify(List.of(
				literal(0, 1)
		), List.of(
				jump("next"),
				label("next"),
				literal(0, 1)
		));

		testSimplify(List.of(
				literal(0, 1)
		), List.of(
				jump(Condition.nz, "next"),
				label("next"),
				literal(0, 1)
		));
	}

	@Test
	public void testRemoveUnusedLabels() {
		testSimplify(List.of(
				literal(0, 1)
		), List.of(
				label("start"),
				literal(0, 1)
		));

		testSimplifyExpectNoChange(List.of(
				jump(Condition.z, "else"),
				literal(0, 1),
				jump("end"),
				label("else"),
				literal(0, 2),
				label("end")
		));
	}

	@Test
	public void testPushPop() {
		testSimplify(List.of(
				literal(0, 1),
				call("print")
		), List.of(
				literal(0, 1),
				push(1, 2),
				pop(1, 2),
				call("print")
		));

		try {
			AsmIRSimplifier.simplify(List.of(
					literal(0, 1),
					push(1, 2),
					pop(1, 1),
					call("print")
			));
			fail();
		}
		catch (IllegalStateException ignored) {
		}
	}

	@Test
	public void testPushPopToMove() {
		testSimplify(List.of(
				             move(1, 0, 2)
		             ),
		             List.of(
				             push(0, 2),
				             pop(1, 2)
		             ));

		testSimplify(List.of(
				             literal(1, 0)
		             ),
		             List.of(
				             literal(0, 0),
				             push(0, 2),
				             pop(1, 2)
		             ));
	}

	@Test
	public void testSwapLitPop() {
		testSimplify(List.of(
				             push(0, 2),
				             print(2),
				             pop(0, 2),
				             literal(1, 5)
		             ),
		             List.of(
				             push(0, 2),
				             print(2),
				             literal(1, 5),
				             pop(0, 2)
		             ));

		testSimplify(List.of(
				             literal(1, 5)
		             ),
		             List.of(
				             push(0, 2),
				             literal(1, 5),
				             pop(0, 2)
		             ));

		testSimplify(List.of(
				             stringLiteral(1, 0),
				             pop(0, 2),
				             push(1, 8),
				             push(0, 2)
		             ),
		             List.of(
				             // " *"
				             stringLiteral(0, 0),
				             push(0, 8),
				             literal(0, 2),
				             push(0, 2),
				             // drop
				             pop(0, 2),
				             // swap
				             pop(1, 8),
				             pop(0, 2),
				             push(1, 8),
				             push(0, 2)
		             ));
	}

	@Test
	public void testDropVars() {
		testSimplify(List.of(
				             dropVar(5)
		             ),
		             List.of(
				             dropVar(2),
				             dropVar(3)
		             ));

		testSimplify(List.of(
				             dropVar(9)
		             ),
		             List.of(
				             dropVar(2),
				             dropVar(3),
				             dropVar(4)
		             ));
	}

	private static void testSimplify(List<AsmIR> expected, List<AsmIR> input) {
		assertEquals(expected,
		             AsmIRSimplifier.simplify(input));
	}

	private static void testSimplifyExpectNoChange(List<AsmIR> input) {
		testSimplify(input, input);
	}
}
