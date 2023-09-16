package de.regnis.ts4th;

/**
 * @author Thomas Singer
 */
public sealed interface Instruction permits
		Instruction.Label,
		Instruction.IntLiteral, Instruction.BoolLiteral, Instruction.PtrLiteral, Instruction.StringLiteral,
		Instruction.Command, Instruction.Jump, Instruction.Branch, Instruction.Ret {

	record Label(String name) implements Instruction {
		@Override
		public String toString() {
			return name + ":";
		}
	}

	record IntLiteral(int value) implements Instruction {
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

	record Jump(String target) implements Instruction {
		@Override
		public String toString() {
			return "jmp " + target;
		}
	}

	record Branch(String ifTarget, String elseTarget) implements Instruction {
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
}
