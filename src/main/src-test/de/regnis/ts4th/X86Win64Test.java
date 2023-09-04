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
	public void testWin64() throws IOException {
		write(path -> {
			try (BufferedWriter writer = Files.newBufferedWriter(path)) {
				final AsmIRStringLiterals stringLiterals = new AsmIRStringLiterals();
				final X86Win64 x86Win64 = new X86Win64(writer);
				x86Win64.write(List.of(
						new AsmIRFunction("main", stringLiterals, List.of(
								AsmIRFactory.command("subr"),
								AsmIRFactory.ret()
						)),
						new AsmIRFunction("subr", stringLiterals, List.of(
								AsmIRFactory.ret()
						))
				), stringLiterals);
			}
		});
	}
}
