package de.regnis.ts4th;

/**
 * @author Thomas Singer
 */
public class AsmIRFactory {
	public static AsmIR label(String name) {
		return new AsmIR.Label(name);
	}

	public static AsmIR literal(int target, int value) {
		return new AsmIR.IntLiteral(target, value);
	}

	public static AsmIR literal(int target, boolean value) {
		return new AsmIR.BoolLiteral(target, value);
	}

	public static AsmIR ptrLiteral(int target, int index, String varName) {
		return new AsmIR.PtrLiteral(target, index, varName);
	}

	public static AsmIR stringLiteral(int target, int constantIndex) {
		return new AsmIR.StringLiteral(target, constantIndex);
	}

	public static AsmIR push(int reg, int size) {
		return new AsmIR.Push(reg, size);
	}

	public static AsmIR pop(int reg, int size) {
		return new AsmIR.Pop(reg, size);
	}

	public static AsmIR move(int target, int source, int size) {
		return new AsmIR.Move(target, source, size);
	}

	public static AsmIR load(int valueReg, int pointerReg, int valueSize) {
		return new AsmIR.Load(valueReg, valueSize, pointerReg);
	}

	public static AsmIR store(int pointerReg, int valueReg, int valueSize) {
		return new AsmIR.Store(pointerReg, valueReg, valueSize);
	}

	public static AsmIR jump(String target) {
		return new AsmIR.Jump(target);
	}

	public static AsmIR jump(AsmIR.Condition condition, String target) {
		return new AsmIR.Jump(condition, target);
	}

	public static AsmIR call(String name) {
		return new AsmIR.Call(name);
	}

	public static AsmIR binCommand(AsmIR.BinOperation operation, int reg1, int reg2) {
		return new AsmIR.BinCommand(operation, reg1, reg2);
	}

	public static AsmIR printString(int ptrReg, int sizeReg) {
		return new AsmIR.PrintString(ptrReg, sizeReg);
	}

	public static AsmIR printChar() {
		return new AsmIR.PrintChar();
	}

	public static AsmIR print(int size) {
		return new AsmIR.PrintInt(size);
	}

	public static AsmIR mem() {
		return new AsmIR.Mem();
	}

	public static AsmIR ret() {
		return new AsmIR.Ret();
	}
}
