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
		assertEquals(InstructionFactory.literal(1),
		             new ConstDeclaration("a", List.of(
				             new Instruction.IntLiteral(1)
		             )).evaluate(name -> null));
		assertEquals(InstructionFactory.literal(3),
		             new ConstDeclaration("b", List.of(
				             new Instruction.IntLiteral(1),
				             new Instruction.IntLiteral(2),
				             InstructionFactory.add()
		             )).evaluate(name -> null));
		assertEquals(InstructionFactory.literal(12),
		             new ConstDeclaration("c", List.of(
				             new Instruction.IntLiteral(3),
				             new Instruction.IntLiteral(4),
				             InstructionFactory.mul()
		             )).evaluate(name -> null));
		assertEquals(InstructionFactory.literal(6),
		             new ConstDeclaration("d", List.of(
				             new Instruction.Command("a"),
				             new Instruction.Command("b"),
				             InstructionFactory.mul()
		             )).evaluate(Map.of("a", InstructionFactory.literal(2),
		                                "b", InstructionFactory.literal(3))::get));
		assertEquals(InstructionFactory.literal(true),
		             new ConstDeclaration("e", List.of(
				             InstructionFactory.literal(true)
		             )).evaluate(Map.of("a", InstructionFactory.literal(2),
		                                "b", InstructionFactory.literal(3))::get));

		try {
			new ConstDeclaration("c", List.of(
					new Instruction.IntLiteral(3),
					InstructionFactory.mul()
			)).evaluate(name -> null);
			fail();
		}
		catch (InterpretingFailedException ignored) {
		}

		try {
			new ConstDeclaration("d", List.of(
					new Instruction.Command("a"),
					new Instruction.Command("b"),
					InstructionFactory.mul()
			)).evaluate(Map.of("a", InstructionFactory.literal(2))::get);
		}
		catch (InterpretingFailedException ignored) {
		}
	}
}
