package de.regnis.ts4th;

import java.util.*;

/**
 * @author Thomas Singer
 */
public record Program(List<AsmIRFunction> functions, AsmIRStringLiterals stringLiterals) {
}
