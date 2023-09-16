package de.regnis.ts4th;

import java.util.*;
import java.util.function.*;

import org.jetbrains.annotations.*;

import static de.regnis.ts4th.AsmIR.BinOperation.*;
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

	public static final String ABORT = "abort";

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
					throw new InvalidTypeException(location, "drop (a --) can't operate on empty stack");
				}
				return input.prev();
			}

			@Override
			public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
				final int size = size(types.type());
				output.accept(AsmIRFactory.pop(REG_0, size));
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
			public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
				final int size = size(types.type());
				output.accept(AsmIRFactory.pop(REG_0, size));
				output.accept(AsmIRFactory.push(REG_0, size));
				output.accept(AsmIRFactory.push(REG_0, size));
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
			public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
				final int size1st = size(types.type());
				final int size2nd = size(types.prev().type());
				output.accept(AsmIRFactory.pop(REG_1, size1st));
				output.accept(AsmIRFactory.pop(REG_0, size2nd));
				output.accept(AsmIRFactory.push(REG_0, size2nd));
				output.accept(AsmIRFactory.push(REG_1, size1st));
				output.accept(AsmIRFactory.push(REG_0, size2nd));
				output.accept(AsmIRFactory.push(REG_1, size1st));
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
			public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
				final int size1st = size(types.type());
				final int size2nd = size(types.prev().type());
				output.accept(AsmIRFactory.pop(REG_1, size1st));
				output.accept(AsmIRFactory.pop(REG_0, size2nd));
				output.accept(AsmIRFactory.push(REG_1, size1st));
				output.accept(AsmIRFactory.push(REG_0, size2nd));
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
			public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
				final int size1st = size(types.type());
				final int size2nd = size(types.prev().type());
				output.accept(AsmIRFactory.pop(REG_1, size1st));
				output.accept(AsmIRFactory.pop(REG_0, size2nd));
				output.accept(AsmIRFactory.push(REG_0, size2nd));
				output.accept(AsmIRFactory.push(REG_1, size1st));
				output.accept(AsmIRFactory.push(REG_0, size2nd));
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
			public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
				final int size1st = size(types.type());
				final int size2nd = size(types.prev().type());
				final int size3rd = size(types.prev().prev().type());
				output.accept(AsmIRFactory.pop(REG_2, size1st)); // c
				output.accept(AsmIRFactory.pop(REG_1, size2nd)); // b
				output.accept(AsmIRFactory.pop(REG_0, size3rd)); // a
				output.accept(AsmIRFactory.push(REG_1, size2nd));
				output.accept(AsmIRFactory.push(REG_2, size1st));
				output.accept(AsmIRFactory.push(REG_0, size3rd));
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
			public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
				final Type type1 = types.type();
				final Type type2 = types.prev().type();
				if (type1 == Type.Int && type2 == Type.Int) {
					output.accept(AsmIRFactory.pop(REG_1, 2));
					output.accept(AsmIRFactory.pop(REG_0, 2));
					output.accept(AsmIRFactory.binCommand(add, REG_0, REG_1));
					output.accept(AsmIRFactory.push(REG_0, 2));
				}
				else if (type1 == Type.Ptr) {
					output.accept(AsmIRFactory.pop(REG_1, PTR_SIZE));
					output.accept(AsmIRFactory.pop(REG_0, 2));
					output.accept(AsmIRFactory.binCommand(add_ptr, REG_1, REG_0));
					output.accept(AsmIRFactory.push(REG_1, PTR_SIZE));
				}
				else if (type2 == Type.Ptr) {
					output.accept(AsmIRFactory.pop(REG_1, 2));
					output.accept(AsmIRFactory.pop(REG_0, PTR_SIZE));
					output.accept(AsmIRFactory.binCommand(add_ptr, REG_0, REG_1));
					output.accept(AsmIRFactory.push(REG_0, PTR_SIZE));
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
		nameToCommand.put(SHL, new BinaryCommand(shl));
		nameToCommand.put(SHR, new BinaryCommand(shr));

		nameToCommand.put(MEM, new Command() {
			@Override
			public TypeList process(String name, Location location, TypeList input) {
				return input.append(Type.Ptr);
			}

			@Override
			public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
				output.accept(AsmIRFactory.mem());
				output.accept(AsmIRFactory.push(REG_0, PTR_SIZE));
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
				output.accept(AsmIRFactory.pop(REG_1, PTR_SIZE));
				output.accept(AsmIRFactory.literal(0));
				output.accept(AsmIRFactory.load(REG_0, REG_1, 1));
				output.accept(AsmIRFactory.push(REG_0, 2));
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
			public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
				if (types.type() == Type.Ptr) {
					output.accept(AsmIRFactory.pop(REG_1, PTR_SIZE));
					output.accept(AsmIRFactory.pop(REG_0, 2));
				}
				else {
					output.accept(AsmIRFactory.pop(REG_0, 2));
					output.accept(AsmIRFactory.pop(REG_1, PTR_SIZE));
				}
				output.accept(AsmIRFactory.store(REG_1, REG_0, 1));
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
			public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
				final int size = size(types.type());
				output.accept(AsmIRFactory.pop(REG_0, size));
				output.accept(AsmIRFactory.print(size));
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
			public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
				if (types.type() == Type.Ptr) {
					output.accept(AsmIRFactory.pop(REG_1, PTR_SIZE));
					output.accept(AsmIRFactory.pop(REG_0, 2));
				}
				else {
					output.accept(AsmIRFactory.pop(REG_0, 2));
					output.accept(AsmIRFactory.pop(REG_1, PTR_SIZE));
				}
				output.accept(AsmIRFactory.printString(REG_1, REG_0));
			}
		});
		nameToCommand.put(ABORT, new Command() {
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

	private static int size(Type type) {
		return switch (type) {
			case Int -> 2;
			case Bool -> 1;
			case Ptr -> PTR_SIZE;
		};
	}

	public static class Command {
		protected Command() {
		}

		public TypeList process(String name, Location location, TypeList input) {
			throw new IllegalStateException("not implemented yet");
		}

		public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
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

		public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
			throw new IllegalStateException("not implemented yet " + name);
		}
	}

	private final static class BinaryCommand extends Command {
		private final AsmIR.BinOperation operation;

		public BinaryCommand(AsmIR.BinOperation operation) {
			this.operation = operation;
		}

		@Override
		public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
			output.accept(AsmIRFactory.pop(REG_1, 2));
			output.accept(AsmIRFactory.pop(REG_0, 2));
			output.accept(AsmIRFactory.binCommand(operation, REG_0, REG_1));
			output.accept(AsmIRFactory.push(REG_0, 2));
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
		private final AsmIR.BinOperation operation;

		public RelationalCommand(AsmIR.BinOperation operation) {
			this.operation = operation;
		}

		@Override
		public void toIR(String name, TypeList types, Consumer<AsmIR> output) {
			output.accept(AsmIRFactory.pop(REG_1, 2));
			output.accept(AsmIRFactory.pop(REG_0, 2));
			output.accept(AsmIRFactory.binCommand(operation, REG_0, REG_1));
			output.accept(AsmIRFactory.push(REG_0, 1));
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



