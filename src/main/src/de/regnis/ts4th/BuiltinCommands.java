package de.regnis.ts4th;

import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

import static de.regnis.ts4th.AsmIRConverter.*;

/**
 * @author Thomas Singer
 */
public class BuiltinCommands {

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
	public static final String PRINT_ASCII = "asciiPrint";
	public static final String PRINT_STRING = "printString";

	public static final String IS_LT = "<";
	public static final String IS_LE = "<=";
	public static final String IS_EQ = "==";
	public static final String IS_NE = "!=";
	public static final String IS_GE = ">=";
	public static final String IS_GT = ">";

	private static final Map<String, Command> nameToCommand = new HashMap<>();

	private static int size(Type type) {
		return switch (type) {
			case Int -> 2;
			case Bool -> 1;
			case Ptr -> PTR_SIZE;
		};
	}

	static {
		nameToCommand.put(DROP, new Command() {
			@Override
			public TypeList process(String name, Location location, TypeList input) {
				if (input.isEmpty()) {
					throw new InvalidTypeException(location, "drop (a --) can't operate on empty stack");
				}
				return input.prev();
			}

			@Override
			public void toIR(TypeList types, Consumer<AsmIR> output) {
				final int size = size(types.type());
				output.accept(AsmIR.pop(REG_0, size));
			}
		});
		nameToCommand.put(DUP, new Command() {
			@Override
			public TypeList process(String name, Location location, TypeList input) {
				if (input.isEmpty()) {
					throw new InvalidTypeException(location, "dup (a -- a a) can't operate on empty stack");
				}
				return input.append(input.type());
			}

			@Override
			public void toIR(TypeList types, Consumer<AsmIR> output) {
				final int size = size(types.type());
				output.accept(AsmIR.pop(REG_0, size));
				output.accept(AsmIR.push(REG_0, size));
				output.accept(AsmIR.push(REG_0, size));
			}
		});
		nameToCommand.put(DUP2, new Command() {
			@Override
			public TypeList process(String name, Location location, TypeList input) {
				if (input.size() < 2) {
					throw new InvalidTypeException(location, "dup2 (a b -- a b a b) requires 2 elements on the stack");
				}

				final TypeList parent = input.prev();
				return input
						.append(parent.type())
						.append(input.type());
			}

			@Override
			public void toIR(TypeList types, Consumer<AsmIR> output) {
				final int size1st = size(types.type());
				final int size2nd = size(types.prev().type());
				output.accept(AsmIR.pop(REG_1, size1st));
				output.accept(AsmIR.pop(REG_0, size2nd));
				output.accept(AsmIR.push(REG_0, size2nd));
				output.accept(AsmIR.push(REG_1, size1st));
				output.accept(AsmIR.push(REG_0, size2nd));
				output.accept(AsmIR.push(REG_1, size1st));
			}
		});
		nameToCommand.put(SWAP, new Command() {
			@Override
			public TypeList process(String name, Location location, TypeList input) {
				if (input.size() < 2) {
					throw new InvalidTypeException(location, "swap (a b -- b a) requires 2 elements on the stack");
				}

				final TypeList parent = input.prev();
				return parent.prev()
						.append(input.type())
						.append(parent.type());
			}

			@Override
			public void toIR(TypeList types, Consumer<AsmIR> output) {
				final int size1st = size(types.type());
				final int size2nd = size(types.prev().type());
				output.accept(AsmIR.pop(REG_1, size1st));
				output.accept(AsmIR.pop(REG_0, size2nd));
				output.accept(AsmIR.push(REG_1, size1st));
				output.accept(AsmIR.push(REG_0, size2nd));
			}
		});
		nameToCommand.put(OVER, new Command() {
			@Override
			public TypeList process(String name, Location location, TypeList input) {
				if (input.size() < 2) {
					throw new InvalidTypeException(location, "over (a, b -- a, b, a) requires 2 elements on the stack");
				}

				final TypeList parent = input.prev();
				return input
						.append(parent.type());
			}

			@Override
			public void toIR(TypeList types, Consumer<AsmIR> output) {
				final int size1st = size(types.type());
				final int size2nd = size(types.prev().type());
				output.accept(AsmIR.pop(REG_1, size1st));
				output.accept(AsmIR.pop(REG_0, size2nd));
				output.accept(AsmIR.push(REG_0, size2nd));
				output.accept(AsmIR.push(REG_1, size1st));
				output.accept(AsmIR.push(REG_0, size2nd));
			}
		});
		nameToCommand.put(ROT, new Command() {
			@Override
			public TypeList process(String name, Location location, TypeList input) {
				if (input.size() < 3) {
					throw new InvalidTypeException(location, "rot (a b c -- b c a) requires 3 elements on the stack");
				}

				final TypeList parent = input.prev();
				final TypeList parent2 = parent.prev();
				return parent2.prev()
						.append(parent.type())
						.append(input.type())
						.append(parent2.type());
			}

			@Override
			public void toIR(TypeList types, Consumer<AsmIR> output) {
				final int size1st = size(types.type());
				final int size2nd = size(types.prev().type());
				final int size3rd = size(types.prev().prev().type());
				output.accept(AsmIR.pop(REG_2, size1st)); // c
				output.accept(AsmIR.pop(REG_1, size2nd)); // b
				output.accept(AsmIR.pop(REG_0, size3rd)); // a
				output.accept(AsmIR.push(REG_1, size2nd));
				output.accept(AsmIR.push(REG_2, size1st));
				output.accept(AsmIR.push(REG_0, size3rd));
			}
		});

		nameToCommand.put(ADD, new Command() {
			@Override
			public TypeList process(String name, Location location, TypeList input) {
				TypeList output = input.transform(TypeList.INT2, TypeList.INT);
				if (output != null) {
					return output;
				}

				final TypeList ptrInt = TypeList.PTR.append(Type.Int);
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
			public void toIR(TypeList types, Consumer<AsmIR> output) {
				final Type type1 = types.type();
				final Type type2 = types.prev().type();
				if (type1 == Type.Int && type2 == Type.Int) {
					output.accept(AsmIR.pop(REG_1, 2));
					output.accept(AsmIR.pop(REG_0, 2));
					output.accept(AsmIR.command(CMD_ADD, REG_0, REG_1));
					output.accept(AsmIR.push(REG_0, 2));
				}
				else if (type1 == Type.Ptr) {
					output.accept(AsmIR.pop(REG_1, PTR_SIZE));
					output.accept(AsmIR.pop(REG_0, 2));
					output.accept(AsmIR.command(CMD_ADD_PTR, REG_1, REG_0));
					output.accept(AsmIR.push(REG_1, PTR_SIZE));
				}
				else if (type2 == Type.Ptr) {
					output.accept(AsmIR.pop(REG_1, 2));
					output.accept(AsmIR.pop(REG_0, PTR_SIZE));
					output.accept(AsmIR.command(CMD_ADD, REG_0, REG_1));
					output.accept(AsmIR.push(REG_0, PTR_SIZE));
				}
				else {
					throw new IllegalStateException();
				}
			}
		});
		nameToCommand.put(SUB, new ArithmeticCommand(CMD_SUB));
		nameToCommand.put(MUL, new ArithmeticCommand(CMD_IMUL));
		nameToCommand.put(DIV, new ArithmeticCommand(CMD_IDIV));
		nameToCommand.put(MOD, new ArithmeticCommand(CMD_IMOD));
		nameToCommand.put(AND, new ArithmeticCommand(CMD_AND));
		nameToCommand.put(OR, new ArithmeticCommand(CMD_OR));
		nameToCommand.put(XOR, new ArithmeticCommand(CMD_XOR));
		nameToCommand.put(SHL, new ArithmeticCommand(CMD_SHL));
		nameToCommand.put(SHR, new ArithmeticCommand(CMD_SHR));

		nameToCommand.put(MEM, new Command() {
			@Override
			public TypeList process(String name, Location location, TypeList input) {
				return input.append(Type.Ptr);
			}

			@Override
			public void toIR(TypeList types, Consumer<AsmIR> output) {
				output.accept(AsmIR.command(CMD_MEM, REG_0, 0));
				output.accept(AsmIR.push(REG_0, PTR_SIZE));
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
			public void toIR(TypeList types, Consumer<AsmIR> output) {
				output.accept(AsmIR.pop(REG_1, PTR_SIZE));
				output.accept(AsmIR.literal(0));
				output.accept(AsmIR.load(REG_0, REG_1, 1));
				output.accept(AsmIR.push(REG_0, 2));
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

				expected = TypeList.PTR.append(Type.Int);
				output = input.transform(expected, TypeList.EMPTY);
				if (output == null) {
					throw new InvalidTypeException(location, "Invalid types for command " + name + "! Expected " + expected + " but got " + input);
				}

				return output;
			}

			@Override
			public void toIR(TypeList types, Consumer<AsmIR> output) {
				if (types.type() == Type.Ptr) {
					output.accept(AsmIR.pop(REG_1, PTR_SIZE));
					output.accept(AsmIR.pop(REG_0, 2));
				}
				else {
					output.accept(AsmIR.pop(REG_0, 2));
					output.accept(AsmIR.pop(REG_1, PTR_SIZE));
				}
				output.accept(AsmIR.store(REG_1, REG_0, 1));
			}
		});

		nameToCommand.put(PRINT_ASCII, new DefaultCommand(TypeList.INT, TypeList.EMPTY));
		nameToCommand.put(PRINT, new Command() {
			@Override
			public TypeList process(String name, Location location, TypeList input) {
				if (input.isEmpty()) {
					throw new InvalidTypeException(location, "print (a --) can't operate on empty stack");
				}

				final Type type = input.type();
				if (type != Type.Int && type != Type.Ptr) {
					throw new InvalidTypeException(location, "print (a --) only can work on `int` and `ptr`");
				}

				return input.prev();
			}

			@Override
			public void toIR(TypeList types, Consumer<AsmIR> output) {
				final int size = size(types.type());
				output.accept(AsmIR.pop(REG_0, size));
				output.accept(AsmIR.command(CMD_PRINT, REG_0, size));
			}
		});
		nameToCommand.put(PRINT_STRING, new Command() {
			@Override
			public TypeList process(String name, Location location, TypeList input) {
				TypeList expected = TypeList.INT.append(Type.Ptr);
				TypeList output = input.transform(expected, TypeList.EMPTY);
				if (output != null) {
					return output;
				}

				expected = TypeList.PTR.append(Type.Int);
				output = input.transform(expected, TypeList.EMPTY);
				if (output == null) {
					throw new InvalidTypeException(location, "Invalid types for command " + name + "! Expected " + expected + " but got " + input);
				}

				return output;
			}

			@Override
			public void toIR(TypeList types, Consumer<AsmIR> output) {
				if (types.type() == Type.Ptr) {
					output.accept(AsmIR.pop(REG_1, PTR_SIZE));
					output.accept(AsmIR.pop(REG_0, 2));
				}
				else {
					output.accept(AsmIR.pop(REG_0, 2));
					output.accept(AsmIR.pop(REG_1, PTR_SIZE));
				}
				output.accept(AsmIR.command(CMD_PRINT_STRING, REG_1, REG_0));
			}
		});

		nameToCommand.put(IS_LT, new RelationalCommand(CMD_LT));
		nameToCommand.put(IS_LE, new RelationalCommand(CMD_LE));
		nameToCommand.put(IS_EQ, new RelationalCommand(CMD_EQ));
		nameToCommand.put(IS_NE, new RelationalCommand(CMD_NE));
		nameToCommand.put(IS_GE, new RelationalCommand(CMD_GE));
		nameToCommand.put(IS_GT, new RelationalCommand(CMD_GT));
	}

	@Nullable
	public static Command get(String name) {
		return nameToCommand.get(name);
	}

	public static class Command {
		protected Command() {
		}

		public TypeList process(String name, Location location, TypeList input) {
			throw new IllegalStateException("not implemented yet");
		}

		public void toIR(TypeList types, Consumer<AsmIR> output) {
			throw new IllegalStateException("not implemented yet");
		}
	}

	private static class DefaultCommand extends Command {
		public final TypesInOut typesInOut;

		protected DefaultCommand(TypeList in, TypeList out) {
			this.typesInOut = new TypesInOut(in, out);
		}

		public TypeList process(String name, Location location, TypeList input) {
			final TypeList output = input.transform(typesInOut.in(), typesInOut.out());
			if (output == null) {
				throw new InvalidTypeException(location, "Invalid types for command " + name + "! Expected " + typesInOut.in() + " but got " + input);
			}

			return output;
		}

		public void toIR(TypeList types, Consumer<AsmIR> output) {
			throw new IllegalStateException("not implemented yet");
		}
	}

	private final static class ArithmeticCommand extends Command {
		private final String command;

		public ArithmeticCommand(String command) {
			this.command = command;
		}

		@Override
		public void toIR(TypeList types, Consumer<AsmIR> output) {
			output.accept(AsmIR.pop(REG_1, 2));
			output.accept(AsmIR.pop(REG_0, 2));
			output.accept(AsmIR.command(command, REG_0, REG_1));
			output.accept(AsmIR.push(REG_0, 2));
		}

		public TypeList process(String name, Location location, TypeList input) {
			final TypeList output = input.transform(TypeList.INT2, TypeList.INT);
			if (output == null) {
				throw new InvalidTypeException(location, "Invalid types for command " + name + "! Expected " + TypeList.INT2 + " but got " + input);
			}

			return output;
		}
	}

	private final static class RelationalCommand extends Command {
		private final String command;

		public RelationalCommand(String command) {
			this.command = command;
		}

		@Override
		public void toIR(TypeList types, Consumer<AsmIR> output) {
			output.accept(AsmIR.pop(REG_1, 2));
			output.accept(AsmIR.pop(REG_0, 2));
			output.accept(AsmIR.command(command, REG_0, REG_1));
			output.accept(AsmIR.push(REG_0, 1));
		}

		public TypeList process(String name, Location location, TypeList input) {
			final TypeList output = input.transform(TypeList.INT2, TypeList.BOOL);
			if (output == null) {
				throw new InvalidTypeException(location, "Invalid types for command " + name + "! Expected " + TypeList.INT2 + " but got " + input);
			}

			return output;
		}
	}
}



