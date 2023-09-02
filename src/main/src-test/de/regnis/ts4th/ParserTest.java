package de.regnis.ts4th;

import org.antlr.v4.runtime.misc.*;
import org.junit.*;

import java.util.*;
import java.util.function.*;

/**
 * @author Thomas Singer
 */
public final class ParserTest {

	@Test
	public void testBasic() {
		Assert.assertEquals(List.of(
				new Function("inc", TypeList.INT, TypeList.INT,
				             false, List.of(
						             Instruction.literal(1),
						             BuiltinCommands.add(),
						             Instruction.ret()
				             )
				),
				new Function("dec", TypeList.INT, TypeList.INT,
				             false, List.of(
						             Instruction.literal(1),
						             BuiltinCommands.sub(),
						             Instruction.ret()
				             )
				),
				new Function("max", TypeList.INT2, TypeList.INT,
				             false, List.of(
						             BuiltinCommands.dup2Int(),
						             BuiltinCommands.isLT(),
						             Instruction.branch("if_1", "endif_1"),

						             Instruction.label("if_1"),
						             BuiltinCommands.swapInt(),
						             Instruction.jump("endif_1"),

						             Instruction.label("endif_1"),
						             BuiltinCommands.dropInt(),
						             Instruction.ret()
				             )
				)
		), Parser.parseString("""
				                      def inc (int -- int)
				                        1 +
				                      end
				                                                                        
				                      def dec (int -- int)
				                        1 -
				                      end
				                                                                        
				                      def max (int int -- int)
				                        dup2
				                        < if
				                            swap
				                        end
				                        drop
				                      end
				                      """));
	}

	@Test
	public void testWhileDoEnd() {
		Assert.assertEquals(List.of(
				new Function("loopTest", TypeList.EMPTY, TypeList.EMPTY,
				             false, List.of(
						             Instruction.literal(0),
						             Instruction.label("while_1"),
						             BuiltinCommands.dup(),
						             Instruction.literal(10),
						             BuiltinCommands.isLT(),
						             Instruction.branch("whilebody_1", "endwhile_1"),

						             Instruction.label("whilebody_1"),
						             BuiltinCommands.dup(),
						             Instruction.command("print"),
						             Instruction.literal(1),
						             BuiltinCommands.add(),
						             Instruction.jump("while_1"),

						             Instruction.label("endwhile_1"),
						             BuiltinCommands.dropInt(),
						             Instruction.ret()
				             )
				)
		), Parser.parseString(
				"""
						def loopTest(--)
						  0 while dup 10 < do
						    dup print
						    1 +
						  end drop
						end"""
		));

		Assert.assertEquals(List.of(
				new Function("loopTest", TypeList.INT2, TypeList.EMPTY,
				             false, List.of(
						             Instruction.label("while_1"),
						             Instruction.literal(true),
						             Instruction.branch("whilebody_1", "endwhile_1"),

						             Instruction.label("whilebody_1"),
						             BuiltinCommands.dup2Int(),
						             BuiltinCommands.isGE(),
						             Instruction.branch("if_2", "endif_2"),

						             Instruction.label("if_2"),
						             BuiltinCommands.dropInt(),
						             BuiltinCommands.dropInt(),
						             Instruction.jump("endwhile_1"),

						             Instruction.label("endif_2"),
						             BuiltinCommands.swapInt(),
						             BuiltinCommands.dup(),
						             Instruction.command("."),
						             Instruction.literal(1),
						             BuiltinCommands.add(),
						             BuiltinCommands.swapInt(),
						             Instruction.jump("while_1"),

						             Instruction.label("endwhile_1"),
						             Instruction.ret()
				             )
				)
		), Parser.parseString(
				"""
						def loopTest(int int --)
						  while true do
						      dup2
						      >= if
						          drop drop
						          break
						      end

						      swap
						      dup .
						      1 +
						      swap
						  end
						end"""
		));

		Assert.assertEquals(List.of(
				new Function("loopTest", TypeList.EMPTY, TypeList.EMPTY,
				             false, List.of(
						             Instruction.literal(0),

						             Instruction.label("while_1"),
						             Instruction.literal(true),
						             Instruction.branch("whilebody_1", "endwhile_1"),

						             Instruction.label("whilebody_1"),
						             BuiltinCommands.dup(),
						             Instruction.literal(10),
						             BuiltinCommands.isLT(),
						             Instruction.branch("if_2", "else_2"),

						             Instruction.label("if_2"),
						             BuiltinCommands.dup(),
						             BuiltinCommands.print(),
						             Instruction.literal(1),
						             BuiltinCommands.add(),
						             Instruction.jump("endif_2"),

						             Instruction.label("else_2"),
						             Instruction.jump("endwhile_1"),

						             Instruction.label("endif_2"),
						             Instruction.jump("while_1"),

						             Instruction.label("endwhile_1"),
						             Instruction.ret()
				             )
				)
		), Parser.parseString(
				"""
						def loopTest(--)
						  0
						  while true do
						      dup 10 < if
						          dup print
						          1 +
						      else
						          break
						      end
						  end
						end"""
		));

		Assert.assertEquals(List.of(
				new Function("gcd", TypeList.INT2, TypeList.INT,
				             false, List.of(
						             Instruction.label("while_1"),
						             Instruction.literal(true),
						             Instruction.branch("whilebody_1", "endwhile_1"),

						             Instruction.label("whilebody_1"),
						             BuiltinCommands.dup2Int(),
						             BuiltinCommands.isLT(),
						             Instruction.branch("if_2", "else_2"),

						             Instruction.label("if_2"),
						             BuiltinCommands.overInt(),
						             BuiltinCommands.sub(),
						             Instruction.jump("endif_2"),

						             Instruction.label("else_2"),
						             BuiltinCommands.dup2Int(),
						             BuiltinCommands.isGT(),
						             Instruction.branch("if_3", "else_3"),

						             Instruction.label("if_3"),
						             BuiltinCommands.swapInt(),
						             BuiltinCommands.overInt(),
						             BuiltinCommands.sub(),
						             Instruction.jump("endif_3"),

						             Instruction.label("else_3"),
						             Instruction.jump("endwhile_1"),

						             Instruction.label("endif_3"),

						             Instruction.label("endif_2"),
						             Instruction.jump("while_1"),

						             Instruction.label("endwhile_1"),
						             BuiltinCommands.dropInt(),
						             Instruction.ret()
				             )
				)
		), Parser.parseString(
				"""
						def gcd(int int -- int)
						   	while true do
						   		dup2 < if
						   			// a b     with a < b
						   			over    // a b a
						   			-        // a b-a
						   		else
						   			dup2 > if
						   				swap    // b a
						   				over    // b a b
						   				-        // b a-b
						   			else
						   				break
						   			end
						   		end
						   	end
						   	drop
						   end
						   """
		));
	}

	@Test
	public void testBreakNotAllowedBetweenWhileAndDo() {
		try {
			Parser.parseString(
					"""
							def loop(int int --)
							  0 while
							    1 < if
							      break
							    end
							  do
							    +
							  end
							end"""
			);
			Assert.fail();
		}
		catch (ParseCancellationException ignored) {
		}
	}

	private static <I> void checkEquals(Collection<I> expected, Collection<I> actual, BiConsumer<I, I> consumer) {
		final Iterator<I> expIt = expected.iterator();
		final Iterator<I> actualIt = actual.iterator();
		while (true) {
			final boolean hasNext = expIt.hasNext();
			Assert.assertEquals(hasNext, actualIt.hasNext());
			if (!hasNext) {
				break;
			}

			consumer.accept(expIt.next(), actualIt.next());
		}
	}
}
