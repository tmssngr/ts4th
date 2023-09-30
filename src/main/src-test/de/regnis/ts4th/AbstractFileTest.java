package de.regnis.ts4th;

import org.jetbrains.annotations.*;

import java.io.*;
import java.nio.file.*;

/**
 * @author Thomas Singer
 */
public class AbstractFileTest {

	@NotNull
	protected Path createPath(String name) throws IOException {
		final Path path = Paths.get("src/main/res-test/" + name);
		Files.createDirectories(path.getParent());
		return path;
	}

	@NotNull
	protected String getTestClassMethodName() {
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
}
