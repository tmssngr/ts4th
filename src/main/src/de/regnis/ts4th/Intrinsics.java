package de.regnis.ts4th;

import java.util.*;
import java.util.function.*;

import org.jetbrains.annotations.*;

import static de.regnis.ts4th.AsmIR.BinOperation.*;
import static de.regnis.ts4th.AsmIRConverter.*;

/**
 * @author Thomas Singer
 */
public class Intrinsics {

	public static final String DROP = "drop";
	public static final String DUP = "dup";
	public static final String DUP2 = "dup2";
	public static final String SWAP = "swap";
	public static final String OVER = "over";
	public static final String ROT = "rot";

	public static final String ADD = "+";
	public static final String SUB = "-";
	public static final String MUL = "*";
	public static final String DIV = "/";
	public static final String MOD = "%";
	public static final String AND = "and";
	public static final String OR = "or";
	public static final String XOR = "xor";
	public static final String SHL = "shl";
	public static final String SHR = "shr";

	public static final String MEM = "mem";
	public static final String LOAD8 = "@8";
	public static final String STORE8 = "!8";

	public static final String PRINT = "print";

	public static final String IS_LT = "<";
	public static final String IS_LE = "<=";
	public static final String IS_EQ = "==";
	public static final String IS_NE = "!=";
	public static final String IS_GE = ">=";
	public static final String IS_GT = ">";

	private static final Map<String, Command> nameToCommand = new HashMap<>();

	static {
		nameToCommand.put(DROP, new Command() {
			@Override
			public TypeList process(String name, Location location, TypeList input) {
				if (input.isEmpty()) {
					throw new InvalidTypeException(location, name + " (a --) can't operate on empty stack");
				}
				return input.prev();
			}

			@Override
			public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
				output.accept(AsmIRFactory.pop(REG_0, types.type()));
			}
		});
		nameToCommand.put(DUP, new Command() {
			@Override
			public TypeList process(String name, Location location, TypeList input) {
				if (input.isEmpty()) {
					throw new InvalidTypeException(location, name + " (a -- a a) can't operate on empty stack");
				}
				return input.append(input.type());
			}

			@Override
			public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
				final Type type = types.type();
				output.accept(AsmIRFactory.pop(REG_0, type));
				output.accept(AsmIRFactory.push(REG_0, type));
				output.accept(AsmIRFactory.push(REG_0, type));
			}
		});
		nameToCommand.put(DUP2, new Command() {
			@Override
			public TypeList process(String name, Location location, TypeList input) {
				if (input.size() < 2) {
					throw new InvalidTypeException(location, name + " (a b -- a b a b) requires 2 elements on the stack");
				}

				final TypeList parent = input.prev();
				return input
						.append(parent.type())
						.append(input.type());
			}

			@Override
			public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
				final Type type1 = types.type();
				final Type type2 = types.prev().type();
				output.accept(AsmIRFactory.pop(REG_1, type1));
				output.accept(AsmIRFactory.pop(REG_0, type2));
				output.accept(AsmIRFactory.push(REG_0, type2));
				output.accept(AsmIRFactory.push(REG_1, type1));
				output.accept(AsmIRFactory.push(REG_0, type2));
				output.accept(AsmIRFactory.push(REG_1, type1));
			}
		});
		nameToCommand.put(SWAP, new Command() {
			@Override
			public TypeList process(String name, Location location, TypeList input) {
				if (input.size() < 2) {
					throw new InvalidTypeException(location, name + " (a b -- b a) requires 2 elements on the stack");
				}

				final TypeList parent = input.prev();
				return parent.prev()
						.append(input.type())
						.append(parent.type());
			}

			@Override
			public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
				final Type type1 = types.type();
				final Type type2 = types.prev().type();
				output.accept(AsmIRFactory.pop(REG_1, type1));
				output.accept(AsmIRFactory.pop(REG_0, type2));
				output.accept(AsmIRFactory.push(REG_1, type1));
				output.accept(AsmIRFactory.push(REG_0, type2));
			}
		});
		nameToCommand.put(OVER, new Command() {
			@Override
			public TypeList process(String name, Location location, TypeList input) {
				if (input.size() < 2) {
					throw new InvalidTypeException(location, name + " (a, b -- a, b, a) requires 2 elements on the stack");
				}

				final TypeList parent = input.prev();
				return input
						.append(parent.type());
			}

			@Override
			public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
				final Type type1 = types.type();
				final Type type2 = types.prev().type();
				output.accept(AsmIRFactory.pop(REG_1, type1));
				output.accept(AsmIRFactory.pop(REG_0, type2));
				output.accept(AsmIRFactory.push(REG_0, type2));
				output.accept(AsmIRFactory.push(REG_1, type1));
				output.accept(AsmIRFactory.push(REG_0, type2));
			}
		});
		nameToCommand.put(ROT, new Command() {
			@Override
			public TypeList process(String name, Location location, TypeList input) {
				if (input.size() < 3) {
					throw new InvalidTypeException(location, name + " (a b c -- b c a) requires 3 elements on the stack");
				}

				final TypeList parent = input.prev();
				final TypeList parent2 = parent.prev();
				return parent2.prev()
						.append(parent.type())
						.append(input.type())
						.append(parent2.type());
			}

			@Override
			public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
				final Type type1 = types.type();
				final Type type2 = types.prev().type();
				final Type type3 = types.prev().prev().type();
				output.accept(AsmIRFactory.pop(REG_2, type1)); // c
				output.accept(AsmIRFactory.pop(REG_1, type2)); // b
				output.accept(AsmIRFactory.pop(REG_0, type3)); // a
				output.accept(AsmIRFactory.push(REG_1, type2));
				output.accept(AsmIRFactory.push(REG_2, type1));
				output.accept(AsmIRFactory.push(REG_0, type3));
			}
		});

		nameToCommand.put(ADD, new Command() {
			@Override
			public TypeList process(String name, Location location, TypeList input) {
				TypeList output = input.transform(TypeList.INT2, TypeList.INT);
				if (output != null) {
					return output;
				}

				final TypeList ptrInt = TypeList.PTR.append(Type.I16);
				output = input.transform(ptrInt, TypeList.PTR);
				if (output != null) {
					return output;
				}

				final TypeList intPtr = TypeList.INT.append(Type.Ptr);
				output = input.transform(intPtr, TypeList.PTR);
				if (output != null) {
					return output;
				}

				throw new InvalidTypeException(location, "Invalid types for command " + name + "! Expected " + TypeList.INT2 + ", " + ptrInt + " or " + intPtr + " but got " + input);
			}

			@Override
			public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
				final Type type1 = types.type();
				final Type type2 = types.prev().type();
				if (type1 == Type.I16 && type2 == Type.I16) {
					output.accept(AsmIRFactory.pop(REG_1, type1));
					output.accept(AsmIRFactory.pop(REG_0, type2));
					output.accept(AsmIRFactory.binCommand(add, REG_0, REG_1, Type.I16));
					output.accept(AsmIRFactory.push(REG_0, type1));
				}
				else if (type1 == Type.Ptr) {
					output.accept(AsmIRFactory.pop(REG_1, type1));
					output.accept(AsmIRFactory.pop(REG_0, type2));
					output.accept(AsmIRFactory.cast(REG_0, type2, Type.Ptr));
					output.accept(AsmIRFactory.binCommand(add, REG_1, REG_0, Type.Ptr));
					output.accept(AsmIRFactory.push(REG_1, type1));
				}
				else if (type2 == Type.Ptr) {
					output.accept(AsmIRFactory.pop(REG_1, type1));
					output.accept(AsmIRFactory.pop(REG_0, type2));
					output.accept(AsmIRFactory.cast(REG_1, type1, Type.Ptr));
					output.accept(AsmIRFactory.binCommand(add, REG_0, REG_1, Type.Ptr));
					output.accept(AsmIRFactory.push(REG_0, type2));
				}
				else {
					throw new IllegalStateException();
				}
			}
		});
		nameToCommand.put(SUB, new BinaryCommand(sub));
		nameToCommand.put(MUL, new BinaryCommand(imul));
		nameToCommand.put(DIV, new BinaryCommand(idiv));
		nameToCommand.put(MOD, new BinaryCommand(imod));
		nameToCommand.put(AND, new BinaryCommand(and));
		nameToCommand.put(OR, new BinaryCommand(or));
		nameToCommand.put(XOR, new BinaryCommand(xor));
		nameToCommand.put(SHL, new Command() {
			@Override
			public TypeList process(String name, Location location, TypeList input) {
				final TypeList output = input.transform(TypeList.INT2, TypeList.INT);
				if (output == null) {
					throw new InvalidTypeException(location, "Invalid types for command " + name + "! Expected " + TypeList.INT2 + " but got " + input);
				}

				return output;
			}

			@Override
			public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
				output.accept(AsmIRFactory.pop(REG_0, Type.I16));
				output.accept(AsmIRFactory.pop(REG_1, Type.I16));
				output.accept(AsmIRFactory.binCommand(shl, REG_1, REG_0, Type.I16));
				output.accept(AsmIRFactory.push(REG_1, Type.I16));
			}
		});
		nameToCommand.put(SHR, new Command() {
			@Override
			public TypeList process(String name, Location location, TypeList input) {
				final TypeList output = input.transform(TypeList.INT2, TypeList.INT);
				if (output == null) {
					throw new InvalidTypeException(location, "Invalid types for command " + name + "! Expected " + TypeList.INT2 + " but got " + input);
				}

				return output;
			}

			@Override
			public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
				output.accept(AsmIRFactory.pop(REG_0, Type.I16));
				output.accept(AsmIRFactory.pop(REG_1, Type.I16));
				output.accept(AsmIRFactory.binCommand(shr, REG_1, REG_0, Type.I16));
				output.accept(AsmIRFactory.push(REG_1, Type.I16));
			}
		});

		nameToCommand.put(MEM, new Command() {
			@Override
			public TypeList process(String name, Location location, TypeList input) {
				return input.append(Type.Ptr);
			}

			@Override
			public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
				output.accept(AsmIRFactory.mem());
				output.accept(AsmIRFactory.push(REG_0, Type.Ptr));
			}
		});

		nameToCommand.put(LOAD8, new Command() {
			@Override
			public TypeList process(String name, Location location, TypeList input) {
				final TypeList output = input.transform(TypeList.PTR, TypeList.INT);
				if (output == null) {
					throw new InvalidTypeException(location, "Invalid types for command " + name + "! Expected " + TypeList.PTR + " but got " + input);
				}

				return output;
			}

			@Override
			public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
				output.accept(AsmIRFactory.pop(REG_1, Type.Ptr));
				output.accept(AsmIRFactory.literal(REG_0, 0, Type.I16));
				output.accept(AsmIRFactory.load(REG_0, REG_1, 1));
				output.accept(AsmIRFactory.push(REG_0, Type.I16));
			}
		});
		nameToCommand.put(STORE8, new Command() {
			@Override
			public TypeList process(String name, Location location, TypeList input) {
				TypeList expected = TypeList.INT.append(Type.Ptr);
				TypeList output = input.transform(expected, TypeList.EMPTY);
				if (output != null) {
					return output;
				}

				expected = TypeList.PTR.append(Type.I16);
				output = input.transform(expected, TypeList.EMPTY);
				if (output == null) {
					throw new InvalidTypeException(location, "Invalid types for command " + name + "! Expected " + expected + " but got " + input);
				}

				return output;
			}

			@Override
			public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
				if (types.type() == Type.Ptr) {
					output.accept(AsmIRFactory.pop(REG_1, Type.Ptr));
					output.accept(AsmIRFactory.pop(REG_0, Type.I16));
				}
				else {
					output.accept(AsmIRFactory.pop(REG_0, Type.I16));
					output.accept(AsmIRFactory.pop(REG_1, Type.Ptr));
				}
				output.accept(AsmIRFactory.store(REG_1, REG_0, 1));
			}
		});

		nameToCommand.put("emit", new Command() {
			@Override
			public TypeList process(String name, Location location, TypeList input) {
				if (input.isEmpty()) {
					throw new InvalidTypeException(location, name + " (ascii --) can't operate on empty stack");
				}

				if (input.type() != Type.I16) {
					throw new InvalidTypeException(location, name + " (ascii --) only can work on `int`");
				}

				return input.prev();
			}

			@Override
			public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
				output.accept(AsmIRFactory.pop(REG_0, Type.I16));
				output.accept(AsmIRFactory.emit());
			}
		});
		nameToCommand.put(PRINT, new Command() {
			@Override
			public TypeList process(String name, Location location, TypeList input) {
				if (input.isEmpty()) {
					throw new InvalidTypeException(location, name + " (a --) can't operate on empty stack");
				}

				final Type type = input.type();
				final List<Type> allowed = List.of(Type.I8, Type.I16, Type.I32, Type.I64, Type.U8, Type.U16, Type.U32, Type.U64);
				if (!allowed.contains(type)) {
					throw new InvalidTypeException(location, name + " (a --) only can work on " + typesToString(allowed) + ", but got " + input);
				}

				return input.prev();
			}

			@Override
			public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
				final Type type = types.type();
				output.accept(AsmIRFactory.pop(REG_0, type));
				output.accept(AsmIRFactory.print(type));
			}
		});
		nameToCommand.put("printString", new Command() {
			@Override
			public TypeList process(String name, Location location, TypeList input) {
				TypeList expected = TypeList.INT.append(Type.Ptr);
				TypeList output = input.transform(expected, TypeList.EMPTY);
				if (output != null) {
					return output;
				}

				expected = TypeList.PTR.append(Type.I16);
				output = input.transform(expected, TypeList.EMPTY);
				if (output == null) {
					throw new InvalidTypeException(location, "Invalid types for command " + name + "! Expected " + expected + " but got " + input);
				}

				return output;
			}

			@Override
			public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
				if (types.type() == Type.Ptr) {
					output.accept(AsmIRFactory.pop(REG_1, Type.Ptr));
					output.accept(AsmIRFactory.pop(REG_0, Type.I16));
				}
				else {
					output.accept(AsmIRFactory.pop(REG_0, Type.I16));
					output.accept(AsmIRFactory.pop(REG_1, Type.Ptr));
				}
				output.accept(AsmIRFactory.printString(REG_1, REG_0));
			}
		});
		nameToCommand.put("???", new Command() {
			@Override
			public TypeList process(String name, Location location, TypeList input) {
				throw new InvalidTypeException(location, "Types " + input);
			}

			@Override
			public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
				throw new IllegalStateException("not reachable");
			}
		});

		nameToCommand.put(IS_LT, new RelationalCommand(lt));
		nameToCommand.put(IS_LE, new RelationalCommand(le));
		nameToCommand.put(IS_EQ, new RelationalCommand(eq));
		nameToCommand.put(IS_NE, new RelationalCommand(neq));
		nameToCommand.put(IS_GE, new RelationalCommand(ge));
		nameToCommand.put(IS_GT, new RelationalCommand(gt));
	}

	@Nullable
	public static Command get(String name) {
		return nameToCommand.get(name);
	}

	private static String typesToString(List<Type> types) {
		return Utils.join(types, Type::toString, ", ");
	}

	public interface Command {
		TypeList process(String name, Location location, TypeList input);

		void toIR(String name, TypeList types, Consumer<AsmIR> output);
	}

	private record BinaryCommand(AsmIR.BinOperation operation) implements Command {
		public TypeList process(String name, Location location, TypeList input) {
			final TypeList output = input.transform(TypeList.INT2, TypeList.INT);
			if (output == null) {
				throw new InvalidTypeException(location, "Invalid types for command " + name + "! Expected " + TypeList.INT2 + " but got " + input);
			}

			return output;
		}

		@Override
		public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
			output.accept(AsmIRFactory.pop(REG_1, Type.I16));
			output.accept(AsmIRFactory.pop(REG_0, Type.I16));
			output.accept(AsmIRFactory.binCommand(operation, REG_0, REG_1, Type.I16));
			output.accept(AsmIRFactory.push(REG_0, Type.I16));
		}
	}

	private record RelationalCommand(AsmIR.BinOperation operation) implements Command {
		public TypeList process(String name, Location location, TypeList input) {
			final TypeList output = input.transform(TypeList.INT2, TypeList.BOOL);
			if (output == null) {
				throw new InvalidTypeException(location, "Invalid types for command " + name + "! Expected " + TypeList.INT2 + " but got " + input);
			}

			return output;
		}

		@Override
		public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
			output.accept(AsmIRFactory.pop(REG_1, Type.I16));
			output.accept(AsmIRFactory.pop(REG_0, Type.I16));
			output.accept(AsmIRFactory.binCommand(operation, REG_0, REG_1, Type.I16));
			output.accept(AsmIRFactory.push(REG_0, Type.Bool));
		}
	}
}



