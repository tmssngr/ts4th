package de.regnis.ts4th;

import java.util.*;

import org.junit.*;

import static de.regnis.ts4th.InstructionFactory.*;
import static org.junit.Assert.*;

/**
 * @author Thomas Singer
 */
public class ConstTest {

	@Test
	public void testEvaluation() {
		assertEquals(literal(1),
		             new ConstDeclaration("a", List.of(
				             literal(1)
		             )).evaluate(name -> null));
		assertEquals(literal(3),
		             new ConstDeclaration("b", List.of(
				             literal(1),
				             literal(2),
				             add()
		             )).evaluate(name -> null));
		assertEquals(literal(12),
		             new ConstDeclaration("c", List.of(
				             literal(3),
				             literal(4),
				             mul()
		             )).evaluate(name -> null));
		assertEquals(literal(6),
		             new ConstDeclaration("d", List.of(
				             command("a"),
				             command("b"),
				             mul()
		             )).evaluate(Map.of("a", literal(2),
		                                "b", literal(3))::get));
		assertEquals(literal(true),
		             new ConstDeclaration("e", List.of(
				             literal(true)
		             )).evaluate(Map.of("a", literal(2),
		                                "b", literal(3))::get));

		try {
			new ConstDeclaration("c", List.of(
					literal(3),
					mul()
			)).evaluate(name -> null);
			fail();
		}
		catch (InterpretingFailedException ignored) {
		}

		try {
			new ConstDeclaration("d", List.of(
					command("a"),
					command("b"),
					mul()
			)).evaluate(Map.of("a", literal(2))::get);
		}
		catch (InterpretingFailedException ignored) {
		}
	}
}
