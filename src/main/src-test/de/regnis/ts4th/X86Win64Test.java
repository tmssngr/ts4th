package de.regnis.ts4th;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

import org.jetbrains.annotations.*;
import org.junit.*;

import static org.junit.Assert.*;

/**
 * @author Thomas Singer
 */
public class X86Win64Test extends AbstractFileTest {

	@Test
	public void testSimple() throws IOException {
		final AsmIRStringLiteralsImpl stringLiterals = new AsmIRStringLiteralsImpl();
		final AsmIRProgram program = new AsmIRProgram(List.of(
				new AsmIRFunction("main", stringLiterals, List.of(
						AsmIRFactory.call("subr"),
						AsmIRFactory.ret()
				)),
				new AsmIRFunction("subr", stringLiterals, List.of(
						AsmIRFactory.ret()
				))
		), stringLiterals.getConstants(), List.of());

		final Path path = createPath(getTestClassMethodName() + ".asm");
		writeX86(program, path);
	}

	@Test
	public void testCompiler1() throws IOException {
		compileWriteExecute("""
				             fn main(--)
				             	1024 2048 - print  '\\n' emit
				             	1024 not print     '\\n' emit

				             	true print      '\\n' emit
				             	false print     '\\n' emit
				             	false not print '\\n' emit
				             end
				             """);
	}

	@Test
	public void testLogical() throws IOException {
		compileWriteExecute("""
				             fn main(--)
				                "and: " printString
				             	false false and print ' ' emit
				             	false true  and print ' ' emit
				             	true  false and print ' ' emit
				             	true  true  and print '\\n' emit

				                "or:  " printString
				             	false false or print ' ' emit
				             	false true  or print ' ' emit
				             	true  false or print ' ' emit
				             	true  true  or print '\\n' emit

				                "xor: " printString
				             	false false xor print ' ' emit
				             	false true  xor print ' ' emit
				             	true  false xor print ' ' emit
				             	true  true  xor print '\\n' emit
				             end
				             """);
	}

	@Test
	public void testCompiler2() throws IOException {
		compileWriteExecute("""
				             fn main(--)
				             	1024 768
				             	dup2 gcd // w h gcd
				             	rot      // h gcd w
				             	over     // h gcd w gcd
				             	/ print  // h gcd
				             	' ' emit
				             	/ print
				             end

				             fn gcd(int int -- int)
				             	while true do
				             		dup2 < if
				             			// a b     with a < b
				             			over    // a b a
				             			-       // a b-a
				             		else
				             			dup2 > if
				             				swap    // b a
				             				over    // b a b
				             				-       // b a-b
				             			else
				             				break
				             			end
				             		end
				             	end
				             	drop
				             end
				             """);
	}

	@Test
	public void testCompiler3() throws IOException {
		compileWriteExecute("""
				             fn main(--)
				             	-8i8 print
				             	' ' emit
				             	-16 print
				             	' ' emit
				             	-16i16 print
				             	' ' emit
				             	-32i32 print
				             	' ' emit
				             	-64i64 print
				             	' ' emit
				             	8u8 print
				             	' ' emit
				             	16 print
				             	' ' emit
				             	16u16 print
				             	' ' emit
				             	32u32 print
				             	' ' emit
				             	64u64 print
				             end
				             """);
	}

	@Test
	public void testCompiler4() throws IOException {
		compileWriteExecute("""
				             fn main(--)
				             	mem 10u8 !8
				             	mem @8 print
				             end
				             """);
	}

	@Test
	public void testCompiler5() throws IOException {
		compileWriteExecute("""
				             fn main(--)
				             	mem     0x31u8 !8
				             	mem 1 + 0x30u8 !8
				             	mem 2 + 0x32u8 !8
				             	mem 3 + 0x34u8 !8
				             	mem 4u16 printString

				             	mem
				             	'h' appendChar
				             	'e' appendChar
				             	'l' appendChar
				             	'l' appendChar
				             	'o' appendChar
				             	drop
				             	mem 5u16 printString

				             	"\\nhello \\"world\\"\\n" printString
				             end

				             fn appendChar(ptr u8 -- ptr)
				               over over
				               !8
				               drop
				               1+
				             end
				             """);
	}

	@Test
	public void testCompiler6() throws IOException {
		compileWriteExecute("""
				             fn inline a(-- ptr)
				             	mem
				             end

				             fn inline b(-- ptr)
				             	a 10 +
				             end

				             fn main(--)
				             	a
				             	dup '1' !8 1 +
				             	dup '0' !8 1 +
				             	dup '2' !8 1 +
				             	dup '4' !8 1 +
				             	dup '\\n' !8
				             	drop

				             	b
				             	'h' appendChar
				             	'e' appendChar
				             	'l' appendChar
				             	'l' appendChar
				             	'o' appendChar
				             	drop
				             	b 5u16 printString

				             	a 5u16 printString
				             end

				             fn appendChar(ptr u8 -- ptr)
				               over over
				               !8
				               drop
				               1 +
				             end
				             """);
	}

	@Test
	public void testCompiler7() throws IOException {
		compileWriteExecute("""
				             fn strlen(ptr -- int)
				             	0 swap               // 0 ptr
				             	while dup @8 as_i16 0 != do
				             		swap 1 +
				             		swap 1 +
				             	end
				             	drop
				             end

				             fn main(--)
				             	"hello world\\x0" // ptr int
				             	drop // ptr
				             	strlen
				             	print
				             end
				             """);
	}

	@Test
	public void testWhileContinue() throws IOException {
		compileWriteExecute("""
				             fn main(--)
				                0
				                while dup 10 < do
				                    dup 3 == if
				                        "three and four " printString
				                        2 +
				                        continue
				                    end

				                    dup print ' ' emit

				             		1+
				             	end
				             	drop
				             end
				             """);
	}

	@Test
	public void testNestedWhile() throws IOException {
		compileWriteExecute("""
				             fn main(--)
				             	0
				             	while dup 10 < do
				             		0
				             		while dup 20 < do
				             			dup2 + print ' ' emit
				             			1 +
				             		end
				             		drop

				             		'\\n' emit

				             		1+
				             	end
				             	drop
				             end
				             """);
	}

	@Test
	public void testShift() throws IOException {
		compileWriteExecute("""
				             fn main(--)
				               1 2 shl print
				               ' ' emit
				               1024 1 shr print
				             end""");
	}

	@Test
	public void testConst() throws IOException {
		compileWriteExecute("""
				             const width 40 end
				             const height 24 end
				             const size width height * end
				             const dont false end
				             const doit true end
				             const space ' ' end

				             fn main(--)
				               size print
				               ' ' emit
				               doit if
				                 "doit" printString
				               end
				               dont if
				                 "dont" printString
				               end
				               space 20u8 == print
				             end""");
	}

	@Test
	public void testVar() throws IOException {
		compileWriteExecute("""
				             const width 40 end
				             const height 24 end
				             var buffer width height * end

				             fn main(--)
				               buffer 40u8 !8
				               buffer @8 print
				             end""");
	}

	@Test
	public void testLocalVar() throws IOException {
		compileWriteExecute("""
				             fn main(--)
				             	1 2
				             	var a b do
				             		a print
				             		' ' emit
				             		b print
				             	end
				             end""");

		try {
			compile("""
					        fn main(--)
					            1
					            var .a do
					                1 print
					            end
					        end""");
			fail();
		}
		catch (CompilerException _) {
		}

		try {
			compile("""
					        fn main(--)
					            1
					            var print do
					                1 print
					            end
					        end""");
			fail();
		}
		catch (CompilerException _) {
		}

		try {
			compile("""
					        fn fun(int)
					            1 drop
					        end

					        fn main(--)
					            1
					            var fun do
					                1 print
					            end
					        end""");
			fail();
		}
		catch (CompilerException _) {
		}
	}

	@Test
	public void testLocalVarBool() throws IOException {
		compileWriteExecute("""
				             fn main(--)
				                   1     true
				               var value isDebug do
				                 isDebug if
				                   "value: " printString
				                   1 print
				                 end
				               end
				             end""");
	}

	@Test
	public void testLocalVarWrite() throws IOException {
		compileWriteExecute("""
				             fn main(--)
				               0 var i do
				                 while i 10 < do
				                   i print ' ' emit
				                   i 1 + i!
				                 end
				               end
				             end""");
	}

	@Test
	public void testLocalVarBreak() throws IOException {
		compileWriteExecute("""
				             fn main(--)
				                0 while dup 10 < do
				                    dup print ' ' emit
				                    1 +
				                    dup var i do
				                        i 5 == if
					                        break
				                        end
				                    end
				                end
				                drop
				             end""");
	}

	@Test
	public void testCast() throws IOException {
		compileWriteExecute("""
				             fn main(--)
				                1000
				                    dup print
				                    '=' emit
				                    dup 8 shr print
				                    ' ' emit
				                    as_u8 print
				                '\\n' emit
				                -1
				                    dup print
				                    '=' emit
				                    dup as_u8 print
				                    ' ' emit
				                    dup as_u16 print
				                    ' ' emit
				                    as_u32 print
				             end""");
	}

	@Test
	public void testInline() throws IOException {
		compileWriteExecute("""
				             fn inline printBool(bool)
				                if
				                    "true\\n"
				                else
				                    "false\\n"
				                end
				                printString
				             end

				             fn main()
				                1 2 < printBool
				                1 2 > printBool
				             end""");
	}

	@Test
	public void testFor() throws IOException {
		compileWriteExecute("""
				             fn main()
				             	0 10 for i do
				             		i 5 == if
				             			continue
				             		end

				             		i print ' ' emit
				             	end
				             end""");
	}

	@Test
	public void testFibonacci() throws IOException {
		compileWriteExecute("""
				             fn main()
				                1 2 var a b do
				                   while a 100 < do
				                      a print
				                      ' ' emit
				                      b
				                         a b + b!
				                      a!
				                   end
				                end
				             end""");
	}

	@Test
	public void testFiles() throws IOException {
		compileFileWriteExecute("rule110-localvars");
		compileFileWriteExecute("print-ascii-listing");
		compileFileWriteExecute("prng-test");

		compileFileWrite("print-getchar");
	}

	@NotNull
	private AsmIRProgram compile(String s) {
		final List<Declaration> declarations = Parser.parseString(s);
		final Program program = Program.fromDeclarations(declarations);
		return Compiler.compile(program);
	}

	@NotNull
	private Path compileFileWrite(String name) throws IOException {
		final List<Declaration> declarations = Parser.parseFile(createPath(name + ".ts4"));
		final Program program = Program.fromDeclarations(declarations);
		final AsmIRProgram irProgram = Compiler.compile(program);
		return compileWrite(irProgram, name);
	}

	private void compileFileWriteExecute(String name) throws IOException {
		final Path exeFile = compileFileWrite(name);
		execute(exeFile, name);
	}

	private void compileWriteExecute(String s) throws IOException {
		final AsmIRProgram irProgram = compile(s);
		compileWriteExecute(irProgram, getTestClassMethodName());
	}

	@NotNull
	private Path compileWrite(AsmIRProgram irProgram, String name) throws IOException {
		irProgram.write(createPath(name + ".ir"));

		final Path asmFile = createPath(name + ".asm");
		writeX86(irProgram, asmFile);

		final Path exeFile = createPath(name + ".exe");
		Files.deleteIfExists(exeFile);

		invokeFasm(asmFile);
		return exeFile;
	}

	private void compileWriteExecute(AsmIRProgram irProgram, String name) throws IOException {
		final Path exeFile = compileWrite(irProgram, name);
		execute(exeFile, name);
	}

	private void execute(Path exeFile, String name) throws IOException {
		invokeExe(exeFile, createPath(name + ".out"));
	}

	private void invokeFasm(Path asmFile) throws IOException {
		final int exitValue = Compiler.launchFasm(asmFile);
		assertEquals(0, exitValue);
	}

	private void invokeExe(Path exeFile, Path outputFile) throws IOException {
		final ProcessBuilder processBuilder = new ProcessBuilder(exeFile.toString());
		processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
		processBuilder.redirectOutput(outputFile.toFile());
		final Process process = processBuilder.start();
		try {
			process.waitFor(2, TimeUnit.SECONDS);
		}
		catch (InterruptedException e) {
			process.destroy();
		}

		assertEquals(0, process.exitValue());
	}

	private void writeX86(AsmIRProgram program, Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			final X86Win64 x86Win64 = new X86Win64(writer);
			x86Win64.write(program);
		}
	}
}
