package de.regnis.ts4th;

import java.util.*;

/**
 * @author Thomas Singer
 */
public sealed interface Instruction permits
		Instruction.Label,
		Instruction.IntLiteral, Instruction.BoolLiteral, Instruction.PtrLiteral, Instruction.StringLiteral,
		Instruction.Command, Instruction.Jump, Instruction.Branch, Instruction.Ret,
		Instruction.BindVars, Instruction.ReleaseVars {

	record Label(String name, Location location) implements Instruction {
		public Label(String name) {
			this(name, Location.DUMMY);
		}

		@Override
		public String toString() {
			return name + ":";
		}
	}

	record IntLiteral(int value, Type type) implements Instruction {
		@Override
		public String toString() {
			return String.valueOf(value);
		}
	}

	record BoolLiteral(boolean value) implements Instruction {
		@Override
		public String toString() {
			return String.valueOf(value);
		}
	}

	record PtrLiteral(int index, String varName) implements Instruction {
		@Override
		public String toString() {
			return "@" + index + ":" + varName;
		}
	}

	record StringLiteral(String value) implements Instruction {
		@Override
		public String toString() {
			return "\"" + value + "\"";
		}
	}

	record Command(String name, Location location) implements Instruction {
		public Command(String command) {
			this(command, Location.DUMMY);
		}

		@Override
		public String toString() {
			return name;
		}
	}

	record Jump(String target, Location location) implements Instruction {
		public Jump(String target) {
			this(target, Location.DUMMY);
		}

		@Override
		public String toString() {
			return "jmp " + target;
		}
	}

	record Branch(String ifTarget, String elseTarget, Location location) implements Instruction {
		public Branch(String ifTarget, String elseTarget) {
			this(ifTarget, elseTarget, Location.DUMMY);
		}

		@Override
		public String toString() {
			return "branch " + ifTarget + ", " + elseTarget;
		}
	}

	record Ret() implements Instruction {
		@Override
		public String toString() {
			return "ret";
		}
	}

	record BindVars(List<String> varNames, Location location) implements Instruction {
		@Override
		public String toString() {
			return "bindVars " + varNames;
		}
	}

	record ReleaseVars(int count) implements Instruction {
		@Override
		public String toString() {
			return "releaseVars " + count;
		}
	}
}
