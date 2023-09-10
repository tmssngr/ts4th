package de.regnis.ts4th;

import java.util.*;
import java.util.function.*;

import static org.junit.Assert.*;

/**
 * @author Thomas Singer
 */
public class TestUtils {

	public static <E> void assertCollEquals(Collection<? extends E> expectedColl, Collection<? extends E> currentColl, BiConsumer<E, E> consumer) {
		final Iterator<? extends E> expectedIt = expectedColl.iterator();
		final Iterator<? extends E> currentIt = currentColl.iterator();
		while (true) {
			final boolean expectedHasNext = expectedIt.hasNext();
			final boolean currentHasNext = currentIt.hasNext();
			assertEquals(expectedHasNext, currentHasNext);
			if (!expectedHasNext) {
				break;
			}

			final E expected = expectedIt.next();
			final E current = currentIt.next();
			consumer.accept(expected, current);
		}
	}

	public static void assertFunctionsEquals(Collection<? extends Declaration> expected, Collection<? extends Declaration> current) {
		assertCollEquals(expected, current, (expectedDeclaration, currentDeclaration) -> {
			if (expectedDeclaration instanceof Function expF
			    && currentDeclaration instanceof Function currF) {
				assertFunctionEquals(expF, currF);
				return;
			}

			fail("unexpected types " + expectedDeclaration + " vs. " + currentDeclaration);
		});
	}

	public static void assertFunctionEquals(Function expected, Function current) {
		assertEquals(expected.name(), current.name());
		assertEquals(expected.isInline(), current.isInline());
		assertEquals(expected.typesInOut(), current.typesInOut());
		assertInstructionsEquals(expected.instructions(), current.instructions());
	}

	public static void assertInstructionsEquals(Collection<Instruction> expected, Collection<Instruction> current) {
		assertCollEquals(expected, current, TestUtils::assertInstructionEquals);
	}

	private static void assertInstructionEquals(Instruction expected, Instruction current) {
		assertEquals(expected.toString(), current.toString());
	}
}
