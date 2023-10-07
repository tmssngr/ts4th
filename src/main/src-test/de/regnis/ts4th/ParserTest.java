package de.regnis.ts4th;

import java.util.*;

import org.junit.*;

import static de.regnis.ts4th.InstructionFactory.*;
import static org.junit.Assert.*;

/**
 * @author Thomas Singer
 */
public final class ParserTest {

	@Test
	public void testBasic() {
		TestUtils.assertFunctionsEquals(List.of(
				new Function("inc", TypeList.INT, TypeList.INT,
				             false, List.of(
						literal(1),
						add(),
						ret()
				)
				),
				new Function("dec", TypeList.INT, TypeList.INT,
				             false, List.of(
						literal(1),
						sub(),
						ret()
				)
				),
				new Function("max", TypeList.INT2, TypeList.INT,
				             false, List.of(
						dup2Int(),
						isLT(),
						branch("if_1", "if_1_end"),

						label("if_1"),
						swapInt(),
						jump("if_1_end"),

						label("if_1_end"),
						dropInt(),
						ret()
				)
				)
		), Parser.parseString("""
				                      fn inc (int -- int)
				                        1 +
				                      end

				                      fn dec (int -- int)
				                        1 -
				                      end

				                      fn max (int int -- int)
				                        dup2
				                        < if
				                            swap
				                        end
				                        drop
				                      end
				                      """));
	}

	@SuppressWarnings("UnnecessaryUnicodeEscape")
	@Test
	public void testUnescape() {
		assertEquals("\t", Parser.unescape("\\t", Location.DUMMY));
		assertEquals("\n", Parser.unescape("\\n", Location.DUMMY));
		assertEquals("\r", Parser.unescape("\\r", Location.DUMMY));
		assertEquals("\\", Parser.unescape("\\\\", Location.DUMMY));
		assertEquals("\u0001", Parser.unescape("\\x1", Location.DUMMY));
		assertEquals("\u0012", Parser.unescape("\\x12", Location.DUMMY));
		assertEquals("\u0123", Parser.unescape("\\x123", Location.DUMMY));
		assertEquals("\u1234", Parser.unescape("\\x1234", Location.DUMMY));
		assertEquals("\u1234", Parser.unescape("\\x1234", Location.DUMMY));
		assertEquals("\u2345", Parser.unescape("\\x12345", Location.DUMMY));
		assertEquals("\ucafe", Parser.unescape("\\xcafe", Location.DUMMY));
		assertEquals("\ucafeg", Parser.unescape("\\xcafeg", Location.DUMMY));
		assertEquals("\ubabeg", Parser.unescape("\\xBABEg", Location.DUMMY));

		try {
			Parser.unescape("\\", Location.DUMMY);
			fail();
		}
		catch (CompilerException _) {
		}

		try {
			Parser.unescape("\\x", Location.DUMMY);
			fail();
		}
		catch (CompilerException _) {
		}

		try {
			Parser.unescape("\\xg", Location.DUMMY);
			fail();
		}
		catch (CompilerException _) {
		}
	}

	@SuppressWarnings("UnnecessaryUnicodeEscape")
	@Test
	public void testCharEscapes() {
		TestUtils.assertFunctionsEquals(List.of(
				new Function("charEscapes", TypeList.EMPTY, TypeList.EMPTY, false, List.of(
						literal("\\\t\n\r\u0001\u009a\u0DEFg\uCDEFg"),
						command("drop"),
						command("drop"),
						ret()
				))
		), Parser.parseString("""
				                      fn charEscapes()
				                        "\\\\\\t\\n\\r\\x1\\x9a\\xdefg\\xabcdefg"
				                        drop drop
				                      end"""));

		try {
			Parser.parseString("fn invalidCharEscapes() \"\\x\" end");
			fail();
		}
		catch (CompilerException _) {
		}
	}

	@Test
	public void testNestedIf() {
		TestUtils.assertFunctionsEquals(List.of(
				new Function("foo", TypeList.INT2, TypeList.EMPTY,
				             false, List.of(
						literal(0),
						command(">="),
						branch("if_1", "if_1_else"),

						label("if_1"),
						literal(0),
						command(">="),
						branch("if_2", "if_2_end"),

						label("if_2"),
						literal("both greater 0"),
						command("printString"),
						jump("if_2_end"),

						label("if_2_end"),
						jump("if_1_end"),

						label("if_1_else"),
						literal("tos < 0"),
						command("printString"),
						jump("if_1_end"),

						label("if_1_end"),
						ret()
				)
				)
		), Parser.parseString("""
				                      fn foo(int int)
				                         0 >= if
				                            0 >= if
				                               "both greater 0" printString
				                            end
				                         else
				                            "tos < 0" printString
				                         end
				                      end
				                      """));
	}

	@Test
	public void testWhileDoEnd() {
		TestUtils.assertFunctionsEquals(List.of(
				new Function("loopTest", TypeList.EMPTY, TypeList.EMPTY,
				             false, List.of(
						literal(0),
						label("while_1"),
						dup(),
						literal(10),
						isLT(),
						branch("while_1_body", "while_1_end"),

						label("while_1_body"),
						dup(),
						command("print"),
						literal(1),
						add(),
						jump("while_1"),

						label("while_1_end"),
						dropInt(),
						ret()
				)
				)
		), Parser.parseString(
				"""
						fn loopTest(--)
						  0 while dup 10 < do
						    dup print
						    1 +
						  end drop
						end"""
		));

		TestUtils.assertFunctionsEquals(List.of(
				new Function("loopTest", TypeList.INT2, TypeList.EMPTY,
				             false, List.of(
						label("while_1"),
						literal(true),
						branch("while_1_body", "while_1_end"),

						label("while_1_body"),
						dup2Int(),
						isGE(),
						branch("if_2", "if_2_end"),

						label("if_2"),
						dropInt(),
						dropInt(),
						jump("while_1_end"),

						label("if_2_end"),
						swapInt(),
						dup(),
						command("."),
						literal(1),
						add(),
						swapInt(),
						jump("while_1"),

						label("while_1_end"),
						ret()
				)
				)
		), Parser.parseString(
				"""
						fn loopTest(int int --)
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

		TestUtils.assertFunctionsEquals(List.of(
				new Function("loopTest", TypeList.EMPTY, TypeList.EMPTY,
				             false, List.of(
						literal(0),

						label("while_1"),
						literal(true),
						branch("while_1_body", "while_1_end"),

						label("while_1_body"),
						dup(),
						literal(10),
						isLT(),
						branch("if_2", "if_2_else"),

						label("if_2"),
						dup(),
						print(),
						literal(1),
						add(),
						jump("if_2_end"),

						label("if_2_else"),
						jump("while_1_end"),

						label("if_2_end"),
						jump("while_1"),

						label("while_1_end"),
						ret()
				)
				)
		), Parser.parseString(
				"""
						fn loopTest(--)
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

		TestUtils.assertFunctionsEquals(List.of(
				new Function("gcd", TypeList.INT2, TypeList.INT,
				             false, List.of(
						label("while_1"),
						literal(true),
						branch("while_1_body", "while_1_end"),

						label("while_1_body"),
						dup2Int(),
						isLT(),
						branch("if_2", "if_2_else"),

						label("if_2"),
						overInt(),
						sub(),
						jump("if_2_end"),

						label("if_2_else"),
						dup2Int(),
						isGT(),
						branch("if_3", "if_3_else"),

						label("if_3"),
						swapInt(),
						overInt(),
						sub(),
						jump("if_3_end"),

						label("if_3_else"),
						jump("while_1_end"),

						label("if_3_end"),
						jump("if_2_end"),

						label("if_2_end"),
						jump("while_1"),

						label("while_1_end"),
						dropInt(),
						ret()
				)
				)
		), Parser.parseString(
				"""
						fn gcd(int int -- int)
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
	public void testWhileContinue() {
		TestUtils.assertFunctionsEquals(List.of(
				new Function("main", TypeList.EMPTY, TypeList.EMPTY,
				             false, List.of(
						literal(0),

						label("while_1"),
						dup(),
						literal(10),
						isLT(),
						branch("while_1_body", "while_1_end"),

						label("while_1_body"),
						dup(),
						literal(3),
						command("=="),
						branch("if_2", "if_2_end"),

						label("if_2"),
						literal("three "),
						command("printString"),
						command("1+"),
						jump("while_1"),

						label("if_2_end"),
						dup(),
						command("print"),
						command("1+"),
						jump("while_1"),

						label("while_1_end"),
						command("drop"),
						ret()
				)
				)
		), Parser.parseString(
				"""
						fn main(--)
						   0
						   while dup 10 < do
						       dup 3 == if
						           "three " printString
								   1+
						           continue
						       end

						       dup print

							   1+
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
							fn loop(int int --)
							  0 while
							    1 < if
							      break
							    end
							  do
							    +
							  end
							end"""
			);
			fail();
		}
		catch (CompilerException ignored) {
		}
	}

	@Test
	public void testConstDeclaration() {
		TestUtils.assertFunctionsEquals(List.of(
				new ConstDeclaration("width", List.of(
						literal(40)
				)),
				new ConstDeclaration("height", List.of(
						literal(24)
				)),
				new ConstDeclaration("size", List.of(
						command("width"),
						command("height"),
						mul()
				)),
				new Function("main", TypeList.EMPTY, TypeList.EMPTY, false, List.of(
						command("height"),
						command("print"),
						ret()
				))
		), Parser.parseString(
				"""
						const width 40 end
						const height 24 end
						const size width height * end

						fn main()
							height print
						end"""));
	}

	@Test
	public void testVarDeclaration() {
		TestUtils.assertFunctionsEquals(List.of(
				new VarDeclaration("width", List.of(
						literal(2)
				)),
				new VarDeclaration("buffer", List.of(
						literal(256)
				))
		), Parser.parseString(
				"""
						var width 2 end
						var buffer 256 end"""));
	}

	@Test
	public void testLocalVars() {
		TestUtils.assertFunctionsEquals(List.of(
				new Function("swap", TypeList.INT2, TypeList.INT2,
				             false, List.of(
						bindVars(List.of("a", "b")),
						command("b"),
						command("a"),
						releaseVars(2),
						ret()
				)
				)
		), Parser.parseString(
				"""
						fn swap(int int -- int int)
							var a b do
								b a
							end
						end"""
		));

		TestUtils.assertFunctionsEquals(List.of(
				new Function("foo", TypeList.EMPTY, TypeList.EMPTY,
				             false, List.of(
						literal(1),

						label("while_1"),
						command("dup"),
						literal(10),
						command("<"),
						branch("while_1_body", "while_1_end"),

						label("while_1_body"),
						bindVars(List.of("i")),
						command("i"),
						command("i"),
						literal(5),
						command("=="),
						branch("if_2", "if_2_end"),

						label("if_2"),
						releaseVars(1), // << this is important here
						jump("while_1"),

						label("if_2_end"),
						releaseVars(1),
						jump("while_1"),

						label("while_1_end"),
						ret()
				)
				)
		), Parser.parseString(
				"""
						fn foo()
							1 while dup 10 < do
								var i do
									i
									i 5 == if
										continue
									end
								end
							end
						end"""
		));
	}

	@Test
	public void testFor() {
		TestUtils.assertFunctionsEquals(List.of(
				new Function("main", TypeList.EMPTY, TypeList.EMPTY,
				             false, List.of(
						literal(0),
						literal(10),
						bindVars(List.of("i", "max.1")),

						label("for_1"),
						command("i"),
						command("max.1"),
						isLT(),
						branch("for_1_body", "for_1_end"),

						label("for_1_body"),
						command("i"),
						literal(5),
						command("=="),
						branch("if_2", "if_2_end"),

						label("if_2"),
						jump("for_1_next"),

						label("if_2_end"),
						command("i"),
						command("print"),

						label("for_1_next"),
						command("i"),
						inc(),
						command("i!"),
						jump("for_1"),

						label("for_1_end"),
						releaseVars(2),
						ret()
				)
				)
		), Parser.parseString(
				"""
						fn main()
							0 10 for i do
								i 5 == if
									continue
								end

								i print
							end
						end"""
		));

		TestUtils.assertFunctionsEquals(List.of(
				new Function("main", TypeList.EMPTY, TypeList.EMPTY,
				             false, List.of(
						literal(0),
						literal(10),
						bindVars(List.of("i", "max.1")),

						label("for_1"),
						command("i"),
						command("max.1"),
						isLT(),
						branch("for_1_body", "for_1_end"),

						label("for_1_body"),
						command("i"),
						command("print"),

						label("for_1_next"),
						command("i"),
						literal(2),
						add(),
						command("i!"),
						jump("for_1"),

						label("for_1_end"),
						releaseVars(2),
						ret()
				)
				)
		), Parser.parseString(
				"""
						fn main()
							0 10 for i step 2 do
								i print
							end
						end"""
		));

		TestUtils.assertFunctionsEquals(List.of(
				new Function("main", TypeList.EMPTY, TypeList.EMPTY,
				             false, List.of(
						literal(10),
						literal(0),
						bindVars(List.of("i", "max.1")),

						label("for_1"),
						command("i"),
						command("max.1"),
						isGT(),
						branch("for_1_body", "for_1_end"),

						label("for_1_body"),
						command("i"),
						literal(5),
						command("=="),
						branch("if_2", "if_2_end"),

						label("if_2"),
						jump("for_1_next"),

						label("if_2_end"),
						command("i"),
						command("print"),

						label("for_1_next"),
						command("i"),
						dec(),
						command("i!"),
						jump("for_1"),

						label("for_1_end"),
						releaseVars(2),
						ret()
				)
				)
		), Parser.parseString(
				"""
						fn main()
							10 0 for i step -1 do
								i 5 == if
									continue
								end

								i print
							end
						end"""
		));
	}
}
