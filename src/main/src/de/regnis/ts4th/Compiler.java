package de.regnis.ts4th;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author Thomas Singer
 */
public class Compiler {

	public static void main(String[] args) throws IOException, InterruptedException {
		final Path forthFile = Paths.get(args[0]);

		final List<Function> functions = Parser.parseFile(forthFile);
		final TypeCheckerImpl typeChecker = new TypeCheckerImpl();
		functions.forEach(function -> typeChecker.add(function.name(), function.typesInOut()));

		for (Function function : functions) {
			final CfgFunction cfgFunction = new CfgFunction(function);
			cfgFunction.checkTypes(typeChecker);
		}

		Inliner.process(functions);

		final AsmIRStringLiterals stringLiterals = new AsmIRStringLiterals();
		final List<AsmIRFunction> processedFunctions = new ArrayList<>();
		for (Function function : functions) {
			final AsmIRFunction irFunction = AsmIRConverter.convertToIR(function, typeChecker, stringLiterals);
			processedFunctions.add(irFunction);
		}

		String name = forthFile.getFileName().toString();
		int dotIndex = name.lastIndexOf('.');
		if (dotIndex >= 0) {
			name = name.substring(0, dotIndex);
		}
		name = name + ".asm";
		final Path asmFile = forthFile.resolveSibling(name);

		try (Writer writer = Files.newBufferedWriter(asmFile)) {
			final X86Win64 x86Win64 = new X86Win64(writer);
			x86Win64.write(processedFunctions, stringLiterals);
		}

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
		process.waitFor(10, TimeUnit.SECONDS);
	}
}
