package de.regnis.ts4th;

import java.util.*;

import org.junit.*;

import static de.regnis.ts4th.InstructionFactory.*;

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
						branch("if_1", "endif_1"),

						label("if_1"),
						swapInt(),
						jump("endif_1"),

						label("endif_1"),
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
						branch("whilebody_1", "endwhile_1"),

						label("whilebody_1"),
						dup(),
						command("print"),
						literal(1),
						add(),
						jump("while_1"),

						label("endwhile_1"),
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
						branch("whilebody_1", "endwhile_1"),

						label("whilebody_1"),
						dup2Int(),
						isGE(),
						branch("if_2", "endif_2"),

						label("if_2"),
						dropInt(),
						dropInt(),
						jump("endwhile_1"),

						label("endif_2"),
						swapInt(),
						dup(),
						command("."),
						literal(1),
						add(),
						swapInt(),
						jump("while_1"),

						label("endwhile_1"),
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
						branch("whilebody_1", "endwhile_1"),

						label("whilebody_1"),
						dup(),
						literal(10),
						isLT(),
						branch("if_2", "else_2"),

						label("if_2"),
						dup(),
						print(),
						literal(1),
						add(),
						jump("endif_2"),

						label("else_2"),
						jump("endwhile_1"),

						label("endif_2"),
						jump("while_1"),

						label("endwhile_1"),
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
						branch("whilebody_1", "endwhile_1"),

						label("whilebody_1"),
						dup2Int(),
						isLT(),
						branch("if_2", "else_2"),

						label("if_2"),
						overInt(),
						sub(),
						jump("endif_2"),

						label("else_2"),
						dup2Int(),
						isGT(),
						branch("if_3", "else_3"),

						label("if_3"),
						swapInt(),
						overInt(),
						sub(),
						jump("endif_3"),

						label("else_3"),
						jump("endwhile_1"),

						label("endif_3"),

						label("endif_2"),
						jump("while_1"),

						label("endwhile_1"),
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
			Assert.fail();
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
						branch("whilebody_1", "endwhile_1"),
						label("whilebody_1"),
						bindVars(List.of("i")),
						command("i"),
						command("i"),
						literal(5),
						command("=="),
						branch("if_2", "endif_2"),
						label("if_2"),
						releaseVars(1), // << this is important here
						jump("while_1"),
						label("endif_2"),
						releaseVars(1),
						jump("while_1"),
						label("endwhile_1"),
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
}
