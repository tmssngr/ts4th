package de.regnis.ts4th;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

import org.jetbrains.annotations.*;

/**
 * @author Thomas Singer
 */
public class Compiler {

	public static void main(String[] args) throws IOException, InterruptedException {
		final Path forthFile = Paths.get(args[0]);

		final List<Declaration> declarations = Parser.parseFile(forthFile);
		final Program program = Program.fromDeclarations(declarations);
		final AsmIRProgram irProgram = compile(program);

		final Path asmFile = forthFile.resolveSibling(getAsmName(forthFile));
		writeAsmFile(asmFile, irProgram);

		launchFasm(asmFile);
	}

	@NotNull
	public static AsmIRProgram compile(Program program) {
		final NameToFunction nameToFunction = new NameToFunction(program);

		final Function main = nameToFunction.get("main");
		if (main == null) {
			throw new CompilerException("no `main`-function found");
		}

		final List<Function> inlinedFunctions = Inliner.process(program.functions());

		final AsmIRStringLiteralsImpl stringLiterals = new AsmIRStringLiteralsImpl();
		final List<AsmIRFunction> processedFunctions = new ArrayList<>();
		for (Function function : inlinedFunctions) {
			final AsmIRFunction irFunction = AsmIRConverter.convertToIR(function, nameToFunction, stringLiterals);
			processedFunctions.add(irFunction);
		}

		return new AsmIRProgram(processedFunctions, stringLiterals.getConstants(), program.vars());
	}

	public static int launchFasm(Path asmFile) throws IOException {
		final String fasmHome = System.getenv("FASM_HOME");
		final Path fasmExe = fasmHome != null ? Paths.get(fasmHome, "fasm.exe") : Paths.get("fasm.exe");

		final ProcessBuilder processBuilder = new ProcessBuilder(fasmExe.toString(), asmFile.toString());
		processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
		processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
		if (fasmHome != null) {
			processBuilder.environment().put("INCLUDE",
			                                 Paths.get(fasmHome, "INCLUDE").toString());
		}
		final Process process = processBuilder.start();
		try {
			process.waitFor(10, TimeUnit.SECONDS);
		}
		catch (InterruptedException e) {
			process.destroy();
		}
		return process.exitValue();
	}

	private static void writeAsmFile(Path asmFile, AsmIRProgram program) throws IOException {
		try (Writer writer = Files.newBufferedWriter(asmFile)) {
			final X86Win64 x86Win64 = new X86Win64(writer);
			x86Win64.write(program);
		}
	}

	@NotNull
	private static String getAsmName(Path forthFile) {
		String name = forthFile.getFileName().toString();
		int dotIndex = name.lastIndexOf('.');
		if (dotIndex >= 0) {
			name = name.substring(0, dotIndex);
		}
		name = name + ".asm";
		return name;
	}
}
