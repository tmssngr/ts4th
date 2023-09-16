package de.regnis.ts4th;

import java.util.*;

/**
 * @author Thomas Singer
 */
public record AsmIRProgram(List<AsmIRFunction> functions, AsmIRStringLiterals stringLiterals, List<Var> vars) {
}
