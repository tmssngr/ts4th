package de.regnis.ts4th;

/**
 * @author Thomas Singer
 */
public sealed interface AsmIR permits
		AsmIR.Label,
		AsmIR.IntLiteral, AsmIR.BoolLiteral, AsmIR.PtrLiteral, AsmIR.StringLiteral,
		AsmIR.Push, AsmIR.Pop, AsmIR.Move, AsmIR.Cast,
		AsmIR.PushVar, AsmIR.LocalVarRead, AsmIR.LocalVarWrite, AsmIR.DropVars,
		AsmIR.Load, AsmIR.Store,
		AsmIR.Jump, AsmIR.Call,
		AsmIR.BinCommand, AsmIR.PrintInt, AsmIR.PrintString, AsmIR.Emit, AsmIR.Mem, AsmIR.Ret {

	enum Condition {
		z, nz, lt, le, ge, gt
	}

	enum BinOperation {
		add, sub, imul, idiv, imod,
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

	record IntLiteral(int targetReg, int value, Type type) implements AsmIR {
		@Override
		public String toString() {
			return "r" + targetReg + " = " + value + " (" + type + ")";
		}
	}

	record BoolLiteral(int targetReg, boolean value) implements AsmIR {
		@Override
		public String toString() {
			return "r" + targetReg + " = " + value;
		}
	}

	record PtrLiteral(int targetReg, int varIndex, String varName) implements AsmIR {
		@Override
		public String toString() {
			return "r" + targetReg + " = var " + varIndex + ":" + varName;
		}
	}

	record StringLiteral(int targetReg, int constantIndex) implements AsmIR {
		@Override
		public String toString() {
			return "r" + targetReg + " = constant " + constantIndex;
		}
	}

	record Push(int sourceReg, Type type) implements AsmIR {
		@Override
		public String toString() {
			return "push r" + sourceReg + " (" + type + ")";
		}
	}

	record Pop(int targetReg, Type type) implements AsmIR {
		@Override
		public String toString() {
			return "pop r" + targetReg + " (" + type + ")";
		}
	}

	record PushVar(int sourceReg, Type type) implements AsmIR {
		@Override
		public String toString() {
			return "pushVar r" + sourceReg + " (" + type + ")";
		}
	}

	record LocalVarRead(int targetReg, Type type, TypeList offset) implements AsmIR {
		@Override
		public String toString() {
			return "read r" + targetReg + ", var[" + offset.size() + "(" + type + ")]";
		}
	}

	record LocalVarWrite(int sourceReg, Type type, TypeList offset) implements AsmIR {
		@Override
		public String toString() {
			return "write [" + offset.size() + "(" + type + ")], r" + sourceReg;
		}
	}

	record DropVars(TypeList types) implements AsmIR {
		public DropVars {
			Utils.assertTrue(types.type() != null);
		}

		@Override
		public String toString() {
			return "dropVars " + types;
		}
	}

	record Move(int targetReg, int sourceReg, Type type) implements AsmIR {
		@Override
		public String toString() {
			return "move r" + targetReg + ", r" + sourceReg + " (" + type + ")";
		}
	}

	record Cast(int reg, Type sourceType, Type targetType) implements AsmIR {
		@Override
		public String toString() {
			return "cast r" + reg + " (" + sourceType + " -> " + targetType + ")";
		}
	}

	record Load(int valueReg, int pointerReg, Type type) implements AsmIR {
		@Override
		public String toString() {
			return "load r" + valueReg + "(" + type + "), @r" + pointerReg;
		}
	}

	record Store(int pointerReg, int valueReg, Type type) implements AsmIR {
		@Override
		public String toString() {
			return "store @r" + pointerReg + ", r" + valueReg + "(" + type + ")";
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

	record Call(String name) implements AsmIR {
		@Override
		public String toString() {
			return "call " + name;
		}
	}

	record BinCommand(BinOperation operation, int reg1, int reg2, Type type) implements AsmIR {
		@Override
		public String toString() {
			return operation + " r" + reg1 + ", r" + reg2 + " (" + type + ")";
		}
	}

	record PrintInt(Type type) implements AsmIR {
		@Override
		public String toString() {
			return "printInt r0(" + type + ")";
		}
	}

	record Emit() implements AsmIR {
		@Override
		public String toString() {
			return "emit";
		}
	}

	record PrintString(int ptrReg, int sizeReg) implements AsmIR {
		@Override
		public String toString() {
			return "printString r" + ptrReg + " (" + sizeReg + ")";
		}
	}

	record Mem() implements AsmIR {
		@Override
		public String toString() {
			return "r0 = mem";
		}
	}

	record Ret() implements AsmIR {
		@Override
		public String toString() {
			return "ret";
		}
	}
}
