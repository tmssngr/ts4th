package de.regnis.ts4th;

/**
 * @author Thomas Singer
 */
public interface TypeChecker {

	TypeList checkType(Instruction instruction, TypeList input);
}
