package de.regnis.ts4th;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.junit.*;

/**
 * @author Thomas Singer
 */
public class X86Win64Test extends AbstractFileTest {

	@Test
	public void testSimple() throws IOException {
		final AsmIRStringLiterals stringLiterals = new AsmIRStringLiterals();
		final Program program = new Program(List.of(
				new AsmIRFunction("main", stringLiterals, List.of(
						AsmIRFactory.call("subr"),
						AsmIRFactory.ret()
				)),
				new AsmIRFunction("subr", stringLiterals, List.of(
						AsmIRFactory.ret()
				))
		), stringLiterals);

		writeX86(program);
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
				             	-10 print
				             	1000 print
				             	mem print
				             end
				             """);
	}

	@Test
	public void testCompiler4() throws IOException {
		compileWrite("""
				             fn main(--)
				             	10 mem !8
				             	mem @8 print
				             end
				             """);
	}

	@Test
	public void testCompiler5() throws IOException {
		compileWrite("""
				             fn main(--)
				             	mem     0x31 !8
				             	mem 1 + 0x30 !8
				             	mem 2 + 0x32 !8
				             	mem 3 + 0x34 !8
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

				             fn appendChar(ptr int -- ptr)
				               over
				               !8
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
				             	dup 0x31 !8 1 +
				             	dup 0x30 !8 1 +
				             	dup 0x32 !8 1 +
				             	dup 0x34 !8 1 +
				             	dup 0x0d !8
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

				             fn appendChar(ptr int -- ptr)
				               over
				               !8
				               1 +
				             end
				             """);
	}

	@Test
	public void testCompiler7() throws IOException {
		compileWrite("""
				             fn strlen(ptr -- int)
				             	0 swap               // 0 ptr
				             	while dup @8 0 != do
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

	private void compileWrite(String s) throws IOException {
		final Program program = Compiler.compile(Parser.parseString(s));
		writeIr(program);
		writeX86(program);
	}

	private void writeIr(Program program) throws IOException {
		write(".ir", path -> writeIr(program, path));
	}

	private void writeIr(Program program, Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			writeIrConstants(program, writer);
			writer.newLine();

			for (AsmIRFunction function : program.functions()) {
				writeIrFunction(function, writer);
			}
		}
	}

	private void writeIrConstants(Program program, BufferedWriter writer) throws IOException {
		List<byte[]> constants = program.stringLiterals().getConstants();
		for (int i = 0; i < constants.size(); i++) {
			writer.write("const %" + i + ":");
			final byte[] bytes = constants.get(i);
			for (byte value : bytes) {
				writer.write(" ");
				writer.write(Utils.toHex(value, 2));
			}
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

	private void writeX86(Program program) throws IOException {
		write(".asm", path -> writeX86(program, path));
	}

	private void writeX86(Program program, Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			final X86Win64 x86Win64 = new X86Win64(writer);
			x86Win64.write(program);
		}
	}
}
