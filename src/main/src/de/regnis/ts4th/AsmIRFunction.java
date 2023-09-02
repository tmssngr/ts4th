package de.regnis.ts4th;

import java.util.*;

/**
 * @author Thomas Singer
 */
public record AsmIRFunction(String name, AsmIRStringLiterals stringLiterals, List<AsmIR> instructions) {
}
