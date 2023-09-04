package de.regnis.ts4th;

/**
 * @author Thomas Singer
 */
public sealed interface AsmIR permits
		AsmIR.Label, AsmIR.IntLiteral, AsmIR.BoolLiteral, AsmIR.StringLiteral, AsmIR.Push,
		AsmIR.Pop, AsmIR.Load, AsmIR.Store, AsmIR.Jump, AsmIR.Command,
		AsmIR.BinCommand, AsmIR.PrintInt, AsmIR.PrintString, AsmIR.Mem, AsmIR.Ret {

	enum Condition {
		z, nz, lt, le, ge, gt
	}

	enum BinOperation {
		add, add_ptr, sub, imul, idiv, imod,
		and, or, xor,
		shl, shr,
		boolTest,
		lt, le, eq, neq, ge, gt
	}

	record Label(String name) implements AsmIR {
		@Override
		public String toString() {
			return name + ":";
		}
	}

	record IntLiteral(int value) implements AsmIR {
		@Override
		public String toString() {
			return String.valueOf(value);
		}
	}

	record BoolLiteral(boolean value) implements AsmIR {
		@Override
		public String toString() {
			return String.valueOf(value);
		}
	}

	record StringLiteral(int constantIndex) implements AsmIR {
		@Override
		public String toString() {
			return "constant " + constantIndex;
		}
	}

	record Push(int reg, int size) implements AsmIR {
		@Override
		public String toString() {
			return "push " + reg + ", " + size;
		}
	}

	record Pop(int reg, int size) implements AsmIR {
		@Override
		public String toString() {
			return "pop " + reg + ", " + size;
		}
	}

	record Load(int valueReg, int pointerReg, int valueSize) implements AsmIR {
		@Override
		public String toString() {
			return "load " + valueReg + "(" + valueSize + "), @" + pointerReg;
		}
	}

	record Store(int pointerReg, int valueReg, int valueSize) implements AsmIR {
		@Override
		public String toString() {
			return "store @" + pointerReg + ", " + valueReg + "(" + valueSize + ")";
		}
	}

	record Jump(AsmIR.Condition condition, String target) implements AsmIR {
		public Jump(String target) {
			this(null, target);
		}

		@Override
		public String toString() {
			return condition != null ? "jump " + condition + ", " + target : "jump " + target;
		}
	}

	record Command(String name) implements AsmIR {
		@Override
		public String toString() {
			return "command " + name;
		}
	}

	record BinCommand(BinOperation operation, int reg1, int reg2) implements AsmIR {
		@Override
		public String toString() {
			return operation + " " + reg1 + ", " + reg2;
		}
	}

	record PrintInt(int size) implements AsmIR {
		@Override
		public String toString() {
			return "printInt(" + size + ")";
		}
	}

	record PrintString(int ptrReg, int sizeReg) implements AsmIR {
		@Override
		public String toString() {
			return "printString " + ptrReg + ", " + sizeReg;
		}
	}

	record Mem() implements AsmIR {
		@Override
		public String toString() {
			return "mem";
		}
	}

	record Ret() implements AsmIR {
		@Override
		public String toString() {
			return "ret";
		}
	}
}
