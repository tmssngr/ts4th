package de.regnis.ts4th;

import java.util.*;

import org.junit.*;

import static org.junit.Assert.*;

/**
 * @author Thomas Singer
 */
public class ConstTest {

	@Test
	public void testEvaluation() {
		assertEquals(Const.intConst("a", 1),
		             Const.evaluate(new ConstDeclaration("a", List.of(
				             new Instruction.IntLiteral(1)
		             )), name -> null));
		assertEquals(Const.intConst("b", 3),
		             Const.evaluate(new ConstDeclaration("b", List.of(
				             new Instruction.IntLiteral(1),
				             new Instruction.IntLiteral(2),
				             InstructionFactory.add()
		             )), name -> null));
		assertEquals(Const.intConst("c", 12),
		             Const.evaluate(new ConstDeclaration("c", List.of(
				             new Instruction.IntLiteral(3),
				             new Instruction.IntLiteral(4),
				             InstructionFactory.mul()
		             )), name -> null));
		assertEquals(Const.intConst("d", 6),
		             Const.evaluate(new ConstDeclaration("d", List.of(
				             new Instruction.Command("a"),
				             new Instruction.Command("b"),
				             InstructionFactory.mul()
		             )), Map.of("a", Const.intConst("a", 2),
		                        "b", Const.intConst("b", 3))::get));

		try {
			Const.evaluate(new ConstDeclaration("c", List.of(
					new Instruction.IntLiteral(3),
					InstructionFactory.mul()
			)), name -> null);
			fail();
		}
		catch (InterpretingFailedException ignored) {
		}

		try {
			Const.evaluate(new ConstDeclaration("d", List.of(
					new Instruction.Command("a"),
					new Instruction.Command("b"),
					InstructionFactory.mul()
			)), Map.of("a", Const.intConst("a", 2))::get);
		}
		catch (InterpretingFailedException ignored) {
		}
	}
}
