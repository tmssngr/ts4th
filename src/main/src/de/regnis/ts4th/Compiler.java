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
		Path forthFile = null;
		boolean outputIr = false;
		boolean outputTypes = false;
		boolean runExe = false;

		for (String arg : args) {
			if (forthFile != null) {
				error("not more than 1 file excepted as last argument");
			}

			if (arg.equals("-ir")) {
				outputIr = true;
			}
			else if (arg.equals("-types")) {
				outputTypes = true;
			}
			else if (arg.equals("-run")) {
				runExe = true;
			}
			else {
				try {
					forthFile = Paths.get(arg);
				}
				catch (InvalidPathException e) {
					error(arg + " is no valid file path");
				}
			}
		}

		if (forthFile == null) {
			error("the file is missing");
			return;
		}

		final List<Declaration> declarations = Parser.parseFile(forthFile);
		final Program program = Program.fromDeclarations(declarations);

		final OutputStream loggingStream = outputTypes
				? Files.newOutputStream(resolveSiblingWithSuffix(forthFile, ".types")) : null;
		final AsmIRConverter.Logging logging = loggingStream != null
				? new TypeLogging(loggingStream)
				: AsmIRConverter.DEFAULT_LOGGING;
		final AsmIRProgram irProgram;
		try {
			irProgram = compile(program, logging);
		}
		finally {
			if (loggingStream != null) {
				loggingStream.close();
			}
		}

		if (outputIr) {
			final Path irFile = resolveSiblingWithSuffix(forthFile, ".ir");
			System.out.println("writing intermediate represenation to " + irFile);
			irProgram.write(irFile);
		}

		final Path asmFile = resolveSiblingWithSuffix(forthFile, ".asm");
		System.out.println("writing asm to " + asmFile);
		writeAsmFile(asmFile, irProgram);

		launchFasm(asmFile);

		if (runExe) {
			final Path exeFile = resolveSiblingWithSuffix(forthFile, ".exe");
			runExe(exeFile);
		}
	}

	@NotNull
	public static AsmIRProgram compile(Program program) {
		return compile(program, AsmIRConverter.DEFAULT_LOGGING);
	}

	@NotNull
	public static AsmIRProgram compile(Program program, AsmIRConverter.Logging logging) {
		final List<Function> inlinedFunctions = Inliner.process(program.functions());
		final NameToFunction nameToFunction = new NameToFunction(inlinedFunctions);

		final Function main = nameToFunction.get("main");
		if (main == null) {
			throw new CompilerException("no `main`-function found");
		}

		final AsmIRStringLiteralsImpl stringLiterals = new AsmIRStringLiteralsImpl();
		final List<AsmIRFunction> processedFunctions = new ArrayList<>();
		for (Function function : inlinedFunctions) {
			final AsmIRFunction irFunction = AsmIRConverter.convertToIR(function, nameToFunction, stringLiterals, logging);
			processedFunctions.add(irFunction);
		}

		return new AsmIRProgram(processedFunctions, stringLiterals.getConstants(), program.vars());
	}

	public static int launchFasm(Path asmFile) throws IOException {
		final String fasmHome = System.getenv("FASM_HOME");
		final Path fasmExe = fasmHome != null ? Paths.get(fasmHome, "fasm.exe") : Paths.get("fasm.exe");

		System.out.println("starting " + fasmExe);

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

	private static void error(String message) {
		System.out.println(message);
		System.exit(1);
	}

	private static void writeAsmFile(Path asmFile, AsmIRProgram program) throws IOException {
		try (Writer writer = Files.newBufferedWriter(asmFile)) {
			final X86Win64 x86Win64 = new X86Win64(writer);
			x86Win64.write(program);
		}
	}

	private static void runExe(Path exeFile) throws IOException, InterruptedException {
		System.out.println("starting " + exeFile);

		final ProcessBuilder processBuilder = new ProcessBuilder(exeFile.toString());
		processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
		processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
		final Process process = processBuilder.start();
		final int exitCode = process.waitFor();
		if (exitCode != 0) {
			System.out.println(exeFile + " failed with exit code " + exitCode);
		}
	}

	@NotNull
	private static Path resolveSiblingWithSuffix(Path file, String suffix) {
		String name = file.getFileName().toString();
		int dotIndex = name.lastIndexOf('.');
		if (dotIndex >= 0) {
			name = name.substring(0, dotIndex);
		}
		return file.resolveSibling(name + suffix);
	}

	private static class TypeLogging implements AsmIRConverter.Logging {

		private final PrintStream out;

		public TypeLogging(OutputStream stream) {
			out = new PrintStream(stream, true);
		}

		@Override
		public void beforeFunction(Function function) {
			out.println("fn " + function.name() + "(" + function.typesInOut().in() + " -- " + function.typesInOut().out() + ")");
		}

		@Override
		public void beforeInstruction(Instruction instruction, TypeList input, LocalVarStack localVarStackInput) {
			if (!(instruction instanceof Instruction.Label)) {
				out.print("\t");
			}
			out.print(instruction);
			out.print("\t// ");
			if (instruction instanceof Instruction.LocationProvider locP) {
				out.print(locP.location());
			}
		}

		@Override
		public void handleIR(AsmIR asmIR) {
		}

		@Override
		public void afterInstruction(Instruction instruction, TypeList output, LocalVarStack localVarStack) {
			out.println(" -> (" + output + ")");
		}

		@Override
		public void afterFunction(Function function) {
			out.println();
		}
	}
}
