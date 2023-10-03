package de.regnis.ts4th;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;

/**
 * @author Thomas Singer
 */
public record AsmIRProgram(List<AsmIRFunction> functions, List<Supplier<byte[]>> stringLiterals, List<Var> vars) {
	public void write(Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			writeIrConstants(stringLiterals(), writer);

			for (AsmIRFunction function : functions()) {
				writeIrFunction(function, writer);
			}

			writeIrVars(vars(), writer);
		}
	}

	private static void writeIrConstants(List<Supplier<byte[]>> stringLiterals, BufferedWriter writer) throws IOException {
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

	private static void writeIrVars(List<Var> vars, BufferedWriter writer) throws IOException {
		if (vars.size() > 0) {
			writer.newLine();
		}

		for (Var var : vars) {
			writer.write("var " + var.name() + ", " + var.size());
			writer.newLine();
		}
	}

	private static void writeIrFunction(AsmIRFunction function, BufferedWriter writer) throws IOException {
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
}
