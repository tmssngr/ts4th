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
				push(1, Type.Int),
				pop(1, Type.Int),
				call("print")
		));

		try {
			AsmIRSimplifier.simplify(List.of(
					literal(0, 1),
					push(1, Type.Int),
					pop(1, Type.Bool),
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
				             move(1, 0, Type.Int)
		             ),
		             List.of(
				             push(0, Type.Int),
				             pop(1, Type.Int)
		             ));

		testSimplify(List.of(
				             literal(1, 0)
		             ),
		             List.of(
				             literal(0, 0),
				             push(0, Type.Int),
				             pop(1, Type.Int)
		             ));
	}

	@Test
	public void testSwapLitPop() {
		testSimplify(List.of(
				             push(0, Type.Int),
				             print(Type.Int),
				             pop(0, Type.Int),
				             literal(1, 5)
		             ),
		             List.of(
				             push(0, Type.Int),
				             print(Type.Int),
				             literal(1, 5),
				             pop(0, Type.Int)
		             ));

		testSimplify(List.of(
				             literal(1, 5)
		             ),
		             List.of(
				             push(0, Type.Int),
				             literal(1, 5),
				             pop(0, Type.Int)
		             ));

		testSimplify(List.of(
				             stringLiteral(1, 0),
				             pop(0, Type.Int),
				             push(1, Type.Ptr),
				             push(0, Type.Int)
		             ),
		             List.of(
				             // " *"
				             stringLiteral(0, 0),
				             push(0, Type.Ptr),
				             literal(0, 2),
				             push(0, Type.Int),
				             // drop
				             pop(0, Type.Int),
				             // swap
				             pop(1, Type.Ptr),
				             pop(0, Type.Int),
				             push(1, Type.Ptr),
				             push(0, Type.Int)
		             ));
	}

	@Test
	public void testDropVars() {
		testSimplify(List.of(
				             dropVar(TypeList.INT2.append(Type.Bool))
		             ),
		             List.of(
				             dropVar(TypeList.INT2),
				             dropVar(TypeList.BOOL)
		             ));

		testSimplify(List.of(
				             dropVar(TypeList.INT.append(Type.Ptr).append(Type.Bool))
		             ),
		             List.of(
				             dropVar(TypeList.INT),
				             dropVar(TypeList.PTR),
				             dropVar(TypeList.BOOL)
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
