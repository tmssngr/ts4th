package de.regnis.ts4th;

/**
 * @author Thomas Singer
 */
public class AsmIRFactory {
	public static AsmIR label(String name) {
		return new AsmIR.Label(name);
	}

	public static AsmIR literal(int target, long value, Type type) {
		return new AsmIR.IntLiteral(target, value, type);
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

	public static AsmIR push(int sourceReg, Type type) {
		return new AsmIR.Push(sourceReg, type);
	}

	public static AsmIR pop(int targetReg, Type type) {
		return new AsmIR.Pop(targetReg, type);
	}

	public static AsmIR pushVar(int sourceReg, Type type) {
		return new AsmIR.PushVar(sourceReg, type);
	}

	public static AsmIR localVarRead(int targetReg, Type type, TypeList offset) {
		return new AsmIR.LocalVarRead(targetReg, type, offset);
	}

	public static AsmIR localVarWrite(int sourceReg, Type type, TypeList offset) {
		return new AsmIR.LocalVarWrite(sourceReg, type, offset);
	}

	public static AsmIR dropVar(TypeList types) {
		return new AsmIR.DropVars(types);
	}

	public static AsmIR move(int targetReg, int sourceReg, Type type) {
		return new AsmIR.Move(targetReg, sourceReg, type);
	}

	public static AsmIR cast(int reg, Type sourceType, Type targetType) {
		return new AsmIR.Cast(reg, sourceType, targetType);
	}

	public static AsmIR load(int valueReg, int pointerReg, Type type) {
		return new AsmIR.Load(valueReg, pointerReg, type);
	}

	public static AsmIR store(int pointerReg, int valueReg, Type type) {
		return new AsmIR.Store(pointerReg, valueReg, type);
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

	public static AsmIR binCommand(AsmIR.BinOperation operation, int reg1, int reg2, Type type) {
		return new AsmIR.BinCommand(operation, reg1, reg2, type);
	}

	public static AsmIR binLitCommand(AsmIR.BinOperation operation, int reg, long value, Type type) {
		return new AsmIR.BinLiteralCommand(operation, reg, value, type);
	}

	public static AsmIR printString(int ptrReg, int sizeReg) {
		return new AsmIR.PrintString(ptrReg, sizeReg);
	}

	public static AsmIR emit() {
		return new AsmIR.Emit();
	}

	public static AsmIR print(Type type) {
		return new AsmIR.Print(type);
	}

	public static AsmIR mem() {
		return new AsmIR.Mem();
	}

	public static AsmIR ret() {
		return new AsmIR.Ret();
	}

	public static AsmIR not(int reg, Type type) {
		return new AsmIR.Not(reg, type);
	}

	public static AsmIR setCursor() {
		return new AsmIR.SetCursor();
	}

	public static AsmIR getChar() {
		return new AsmIR.GetChar();
	}
}
