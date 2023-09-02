package de.regnis.ts4th;

import java.util.*;

import org.junit.*;

import static org.junit.Assert.assertEquals;

/**
 * @author Thomas Singer
 */
public class AsmIRInterpreterTest {

	@Test
	public void testSimple() {
		assertEquals(List.of(10, 0),
		             AsmIRInterpreter.parseAndCreate("""
				                                             def main( -- int)
				                                                 10
				                                             end""")
				             .run(List.of()));

		assertEquals(List.of(0, 1),
		             AsmIRInterpreter.parseAndCreate("""
				                                             def main( -- int)
				                                                 256
				                                             end""")
				             .run(List.of()));

		assertEquals(List.of(1),
		             AsmIRInterpreter.parseAndCreate("""
				                                             def main( -- bool)
				                                                 true
				                                             end""")
				             .run(List.of()));

		assertEquals(List.of(0),
		             AsmIRInterpreter.parseAndCreate("""
				                                             def main( -- bool)
				                                                 false
				                                             end""")
				             .run(List.of()));

		assertEquals(List.of(3, 0),
		             AsmIRInterpreter.parseAndCreate("""
				                                             def main( -- int)
				                                                1 2 +
				                                             end""").run(List.of()));

		assertEquals(List.of(1, 0),
		             AsmIRInterpreter.parseAndCreate("""
				                                             def main(int int -- int)
				                                                -
				                                             end""").run(List.of(4, 3)));

		assertEquals(List.of(3, 0, 4, 0),
		             AsmIRInterpreter.parseAndCreate("""
				                                             def main(int int -- int int)
				                                                swap
				                                             end""").run(List.of(4, 3)));

		assertEquals(List.of(1, 4, 0),
		             AsmIRInterpreter.parseAndCreate("""
				                                             def main(int bool -- bool int)
				                                                swap
				                                             end""").run(List.of(4, Boolean.TRUE)));
	}

	@Test
	public void testDup() {
		assertEquals(List.of(1, 0, 1, 0), AsmIRInterpreter.parseAndCreate("""
				                                                                  def main(int -- int int)
				                                                                      dup
				                                                                  end""")
				.run(List.of(1)));
		assertEquals(List.of(1, 1), AsmIRInterpreter.parseAndCreate("""
				                                                            def main(bool -- bool bool)
				                                                                dup
				                                                            end""")
				.run(List.of(Boolean.TRUE)));
	}

	@Test
	public void testDup2() {
		assertEquals(List.of(2, 0, 1, 2, 0, 1), AsmIRInterpreter.parseAndCreate("""
				                                                                  def main(int bool -- int bool int bool)
				                                                                      dup2
				                                                                  end""")
				.run(List.of(2, Boolean.TRUE)));
	}

	@Test
	public void testOver() {
		assertEquals(List.of(2, 0, 1, 2, 0), AsmIRInterpreter.parseAndCreate("""
				                                                                  def main(-- int bool int)
				                                                                      2 true over
				                                                                  end""")
				.run(List.of()));
	}

	@Test
	public void testRot() {
		assertEquals(List.of(0, 1, 0, 2, 0), AsmIRInterpreter.parseAndCreate("""
				                                                                  def main(-- bool int int)
				                                                                      2 false 1 rot
				                                                                  end""")
				.run(List.of()));
	}

	@Test
	public void testIf() {
		final AsmIRInterpreter interpreter = AsmIRInterpreter.parseAndCreate("""
				                                                                     def main(int int -- int)
				                                                                        dup2 < if
				                                                                            swap
				                                                                        end
				                                                                        drop
				                                                                     end""");
		assertEquals(List.of(5, 0), interpreter.run(List.of(4, 5)));
		assertEquals(List.of(5, 0), interpreter.run(List.of(5, 4)));
	}

	@Test
	public void testPrint() {
		AsmIRInterpreter interpreter = AsmIRInterpreter.parseAndCreate("""
				                                                                     def main(--)
				                                                                       10 print
				                                                                       mem print
				                                                                     end""");
		assertEquals(List.of(), interpreter.run(List.of()));
		assertEquals("10 0 ", interpreter.getOutput());

		interpreter = AsmIRInterpreter.parseAndCreate("""
				                                              def main(--)
				                                                mem
				                                                  'h' appendChar
				                                                  'e' appendChar
				                                                  'l' appendChar
				                                                  'l' appendChar
				                                                  'o' appendChar
				                                                drop
				                                                mem 5 printString
				                                              end

				                                              def appendChar(ptr int -- ptr)
				                                                over
				                                                !8
				                                                1 +
				                                              end""");
		assertEquals(List.of(), interpreter.run(List.of()));
		assertEquals("hello", interpreter.getOutput());

		interpreter = AsmIRInterpreter.parseAndCreate("""
				                                              def main(--)
				                                                "hello world" printString
				                                              end""");
		assertEquals(List.of(), interpreter.run(List.of()));
		assertEquals("hello world", interpreter.getOutput());
	}

	@Test
	public void testFunctions() {
		final AsmIRInterpreter interpreter = AsmIRInterpreter.parseAndCreate(
				"""
						def hex8(int -- int int)
							dup
								4 shr
								hex4
							swap
							hex4
						end

						def hex4(int -- int)
						    15 and
						    dup 10 >= if
						        0x40 0x39 - +
						    end
						    0x30 +
						end

						def main(int -- int int)
						    hex8
						end""");
		assertEquals(List.of((int)'0', 0, (int)'0', 0), interpreter.run(List.of(0)));
		assertEquals(List.of((int)'0', 0, (int)'9', 0), interpreter.run(List.of(9)));
		assertEquals(List.of((int)'0', 0, (int)'A', 0), interpreter.run(List.of(10)));
		assertEquals(List.of((int)'0', 0, (int)'F', 0), interpreter.run(List.of(15)));
		assertEquals(List.of((int)'1', 0, (int)'0', 0), interpreter.run(List.of(16)));
		assertEquals(List.of((int)'F', 0, (int)'F', 0), interpreter.run(List.of(255)));
	}

	@Test
	public void testMem() {
		final AsmIRInterpreter interpreter = AsmIRInterpreter.parseAndCreate(
				"""
						def a( -- ptr)
						    mem
						end
						def b(-- ptr)
						    mem 1 +
						end
						def main(-- int int)
						    10 a !8
						    20 b !8
						    a @8
						    b @8
						end""");
		assertEquals(List.of(10, 0, 20, 0), interpreter.run(List.of()));
	}
}
