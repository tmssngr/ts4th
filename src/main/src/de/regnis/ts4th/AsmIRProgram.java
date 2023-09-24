package de.regnis.ts4th;

import java.util.*;
import java.util.function.*;

/**
 * @author Thomas Singer
 */
public record AsmIRProgram(List<AsmIRFunction> functions, List<Supplier<byte[]>> stringLiterals, List<Var> vars) {
}
