package de.regnis.ts4th;

import org.junit.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * @author Thomas Singer
 */
public class X86Win64Test extends AbstractFileTest {

	@Test
	public void testSimple() throws IOException {
		final AsmIRStringLiterals stringLiterals = new AsmIRStringLiterals();
		final Program program = new Program(List.of(
				new AsmIRFunction("main", stringLiterals, List.of(
						AsmIRFactory.command("subr"),
						AsmIRFactory.ret()
				)),
				new AsmIRFunction("subr", stringLiterals, List.of(
						AsmIRFactory.ret()
				))
		), stringLiterals);

		write(program);
	}

	private void write(Program program) throws IOException {
		write(path -> writeProgram(program, path));
	}

	private void writeProgram(Program program, Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			final X86Win64 x86Win64 = new X86Win64(writer);
			x86Win64.write(program);
		}
	}
}
