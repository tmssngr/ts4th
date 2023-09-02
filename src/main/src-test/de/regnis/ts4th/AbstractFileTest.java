package de.regnis.ts4th;

import org.jetbrains.annotations.*;

import java.io.*;
import java.nio.file.*;

/**
 * @author Thomas Singer
 */
public class AbstractFileTest {

	protected void write(FileWriter writer) throws IOException {
		final Path path = Paths.get("src/main/res-test/" + getName() + ".txt");
		Files.createDirectories(path.getParent());
		writer.writeFile(path);
	}

	@NotNull
	private static String getName() {
		final StackTraceElement[] stackTrace = new Throwable().getStackTrace();
		for (StackTraceElement element : stackTrace) {
			final String className = element.getClassName();
			final String methodName = element.getMethodName();
			if (methodName.startsWith("test")) {
				return className.replace('.', '/') + "-" + methodName.substring(4);
			}
		}
		throw new IllegalStateException("no test method found");
	}

	protected interface FileWriter {
		void writeFile(Path path) throws IOException;
	}
}
