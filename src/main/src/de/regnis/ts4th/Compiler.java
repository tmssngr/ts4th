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
		final TypeCheckerImpl typeChecker = new TypeCheckerImpl();
		program.functions().forEach(function -> typeChecker.add(function.name(), function.typesInOut()));

		final List<Function> usedFunctions = getUsedFunctions(program);

		for (Function function : usedFunctions) {
			final CfgFunction cfgFunction = new CfgFunction(function);
			cfgFunction.checkTypes(typeChecker);
		}

		final List<Function> inlinedFunctions = Inliner.process(usedFunctions);

		final AsmIRStringLiterals stringLiterals = new AsmIRStringLiterals();
		final List<AsmIRFunction> processedFunctions = new ArrayList<>();
		for (Function function : inlinedFunctions) {
			final AsmIRFunction irFunction = AsmIRConverter.convertToIR(function, typeChecker, stringLiterals);
			processedFunctions.add(irFunction);
		}

		return new AsmIRProgram(processedFunctions, stringLiterals);
	}

	private static List<Function> getUsedFunctions(Program program) {
		final List<Function> usedFunctions = new ArrayList<>();

		final Set<String> invoked = new HashSet<>();
		final Deque<String> pending = new ArrayDeque<>();
		pending.add("main");
		invoked.add("main");

		while (!pending.isEmpty()) {
			final String name = pending.removeFirst();
			final Function function = program.get(name);
			if (function == null) {
				throw new CompilerException("no function `" + name + "` found");
			}

			usedFunctions.add(function);

			for (Instruction instruction : function.instructions()) {
				if (instruction instanceof Instruction.Command c) {
					final String command = c.name();
					if (BuiltinCommands.get(command) == null
					    && invoked.add(command)) {
						pending.add(command);
					}
				}
			}
		}

		return usedFunctions;
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

	private static void launchFasm(Path asmFile) throws IOException, InterruptedException {
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
