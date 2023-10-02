package de.regnis.ts4th;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

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
		compileWrite("""
				             fn main(--)
				             	1024 2048 -
				             	print
				             end
				             """);
	}

	@Test
	public void testCompiler2() throws IOException {
		compileWrite("""
				             fn main(--)
				             	1024 768
				             	dup2 gcd // w h gcd
				             	rot      // h gcd w
				             	over     // h gcd w gcd
				             	/ print  // h gcd
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
		compileWrite("""
				             fn main(--)
				             	-8i8 print
				             	-16 print
				             	-16i16 print
				             	-32i32 print
				             	-64i64 print
				             	8u8 print
				             	16 print
				             	16u16 print
				             	32u32 print
				             	64u64 print
				             end
				             """);
	}

	@Test
	public void testCompiler4() throws IOException {
		compileWrite("""
				             fn main(--)
				             	mem 10u8 !8
				             	mem @8 print
				             end
				             """);
	}

	@Test
	public void testCompiler5() throws IOException {
		compileWrite("""
				             fn main(--)
				             	mem     0x31u8 !8
				             	mem 1 + 0x30u8 !8
				             	mem 2 + 0x32u8 !8
				             	mem 3 + 0x34u8 !8
				             	mem 4 printString

				             	mem
				             	'h' appendChar
				             	'e' appendChar
				             	'l' appendChar
				             	'l' appendChar
				             	'o' appendChar
				             	drop
				             	mem 5 printString

				             	"\\nhello \\"world\\"\\n" printString
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
	public void testCompiler6() throws IOException {
		compileWrite("""
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
				             	b 5 printString

				             	a 5 printString
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
		compileWrite("""
				             fn strlen(ptr -- int)
				             	0 swap               // 0 ptr
				             	while dup @8 as_i16 0 != do
				             		swap 1 +
				             		swap 1 +
				             	end
				             	drop
				             end

				             fn main(--)
				             	"hello world\\0" // ptr int
				             	drop // ptr
				             	strlen
				             	print
				             end
				             """);
	}

	@Test
	public void testNestedWhile() throws IOException {
		compileWrite("""
				             fn main(--)
				             	0
				             	while dup 10 < do
				             		0
				             		while dup 20 < do
				             			dup2 + print
				             			1 +
				             		end
				             		drop

				             		'\\n' emit

				             		1 +
				             	end
				             	drop
				             end
				             """);
	}

	@Test
	public void testShift() throws IOException {
		compileWrite("""
				             fn main(--)
				               1 2 shl print
				               1024 1 shr print
				             end""");
	}

	@Test
	public void testConst() throws IOException {
		compileWrite("""
				             const width 40 end
				             const height 24 end
				             const size width height * end
				             const dont false end
				             const doit true end

				             fn main(--)
				               size print
				               doit if
				                 "doit" printString
				               end
				               dont if
				                 "dont" printString
				               end
				             end""");
	}

	@Test
	public void testVar() throws IOException {
		compileWrite("""
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
		compileWrite("""
				             fn main(--)
				             	1 2
				             	var a b do
				             		a print
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

		if (false) {
			try {
				compile("""
						        const a 1 end

						        fn main(--)
						            1
						            var a do
						                a print
						            end
						        end""");
				fail();
			}
			catch (CompilerException _) {
			}

			try {
				compile("""
						        var a 1 end

						        fn main(--)
						            1
						            var a do
						                a print
						            end
						        end""");
				fail();
			}
			catch (CompilerException _) {
			}
		}
	}

	@Test
	public void testLocalVarBool() throws IOException {
		compileWrite("""
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
		compileWrite("""
				             fn main(--)
				               0 var i do
				                 while i 10 < do
				                   i print
				                   i 1 + i!
				                 end
				               end
				             end""");
	}

	@Test
	public void testCast() throws IOException {
		compileWrite("""
				             fn main(--)
				                1000
				                    dup print
				                    '=' emit
				                    dup 8 shr print
				                    as_u8 print
				                '\\n' emit
				                -1
				                    dup print
				                    '=' emit
				                    dup as_u8 print
				                    dup as_u16 print
				                    as_u32 print
				             end""");
	}

	@Test
	public void testInline() throws IOException {
		compileWrite("""
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
	public void testFiles() throws IOException {
		compileFileWrite("rule110-localvars");
		compileFileWrite("print-ascii-listing");
		compileFileWrite("prng");
	}

	@NotNull
	private AsmIRProgram compile(String s) {
		final List<Declaration> declarations = Parser.parseString(s);
		final Program program = Program.fromDeclarations(declarations);
		return Compiler.compile(program);
	}

	private void compileFileWrite(String name) throws IOException {
		final List<Declaration> declarations = Parser.parseFile(createPath(name + ".ts4"));
		final Program program = Program.fromDeclarations(declarations);
		final AsmIRProgram irProgram = Compiler.compile(program);
		compileWrite(irProgram, name);
	}

	private void compileWrite(String s) throws IOException {
		final AsmIRProgram irProgram = compile(s);
		compileWrite(irProgram, getTestClassMethodName());
	}

	private void compileWrite(AsmIRProgram irProgram, String name) throws IOException {
		writeIr(irProgram, createPath(name + ".ir"));

		final Path asmFile = createPath(name + ".asm");
		writeX86(irProgram, asmFile);

		final Path exeFile = createPath(name + ".exe");
		Files.deleteIfExists(exeFile);

		invokeFasm(asmFile);

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

	private void writeIr(AsmIRProgram program, Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			writeIrConstants(program.stringLiterals(), writer);

			for (AsmIRFunction function : program.functions()) {
				writeIrFunction(function, writer);
			}

			writeIrVars(program.vars(), writer);
		}
	}

	private void writeIrConstants(List<Supplier<byte[]>> stringLiterals, BufferedWriter writer) throws IOException {
		for (int i = 0; i < stringLiterals.size(); i++) {
			writer.write("const %" + i + ":");
			final byte[] bytes = stringLiterals.get(i).get();
			for (byte value : bytes) {
				writer.write(" ");
				writer.write(Utils.toHex(value, 2));
			}
			writer.newLine();
		}

		if (stringLiterals.size() > 0) {
			writer.newLine();
		}
	}

	private void writeIrVars(List<Var> vars, BufferedWriter writer) throws IOException {
		if (vars.size() > 0) {
			writer.newLine();
		}

		for (Var var : vars) {
			writer.write("var " + var.name() + ", " + var.size());
			writer.newLine();
		}
	}

	private void writeIrFunction(AsmIRFunction function, BufferedWriter writer) throws IOException {
		writer.write(function.name());
		writer.write(":");
		writer.newLine();

		for (AsmIR instruction : function.instructions()) {
			if (!(instruction instanceof AsmIR.Label)) {
				writer.write("\t");
			}
			writer.write(instruction.toString());
			writer.newLine();
		}
	}

	private void writeX86(AsmIRProgram program, Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			final X86Win64 x86Win64 = new X86Win64(writer);
			x86Win64.write(program);
		}
	}
}
