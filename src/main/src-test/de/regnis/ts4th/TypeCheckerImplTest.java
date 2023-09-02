package de.regnis.ts4th;

import org.junit.*;

import java.util.*;

/**
 * @author Thomas Singer
 */
public class TypeCheckerImplTest {

	@Test
	public void testSimple() {
		final TypeChecker typeChecker = new TypeCheckerImpl();
		new CfgFunction(new Function("inc", TypeList.INT, TypeList.INT, false, List.of(
				Instruction.literal(1),
				BuiltinCommands.add()
		))).checkTypes(typeChecker);

		try {
			new CfgFunction(new Function("inc", TypeList.INT, TypeList.INT, false, List.of(
					Instruction.literal(1)
			))).checkTypes(typeChecker);
			Assert.fail();
		}
		catch (InvalidTypeException ignored) {
		}

		try {
			new CfgFunction(new Function("inc", TypeList.INT, TypeList.INT, false, List.of(
					BuiltinCommands.add()
			))).checkTypes(typeChecker);
			Assert.fail();
		}
		catch (InvalidTypeException ignored) {
		}
	}

	@Test
	public void testOwnMethod() {
		final String print = "ownprint";
		final CfgFunction cfgFunction = new CfgFunction(new Function("printSum", TypeList.INT2, TypeList.EMPTY, false, List.of(
				BuiltinCommands.add(),
				Instruction.command(print)
		)));

		final TypeCheckerImpl typeChecker = new TypeCheckerImpl();
		try {
			cfgFunction.checkTypes(typeChecker);
			Assert.fail();
		}
		catch (InvalidTypeException ignored) {
		}

		typeChecker.add(print, TypeList.INT, TypeList.EMPTY);

		cfgFunction.checkTypes(typeChecker);
	}

	@Test
	public void testIf() {
		final TypeChecker typeChecker = new TypeCheckerImpl();
		new CfgFunction(new Function("max", TypeList.INT2, TypeList.INT, false, List.of(
				BuiltinCommands.dup2Int(),
				BuiltinCommands.isLT(),
				Instruction.branch("if-1", "endif-1"),

				Instruction.label("if-1"),
				BuiltinCommands.swapInt(),
				Instruction.jump("endif-1"),

				Instruction.label("endif-1"),
				BuiltinCommands.dropInt()
		))).checkTypes(typeChecker);

		try {
			new CfgFunction(new Function("fails", TypeList.INT2, TypeList.INT, false, List.of(
					BuiltinCommands.isLT(),
					Instruction.branch("if-1", "endif-1"),

					Instruction.label("if-1"),
					BuiltinCommands.dropInt(),
					Instruction.jump("endif-1"),

					Instruction.label("endif-1")
			))).checkTypes(typeChecker);
		}
		catch (InvalidTypeException ignored) {
		}
	}

	@Test
	public void testLoop() {
		final TypeCheckerImpl typeChecker = new TypeCheckerImpl();
		typeChecker.add(".", TypeList.INT, TypeList.EMPTY);
		new CfgFunction(new Function("loopTest", TypeList.INT2, TypeList.EMPTY, false, List.of(
				Instruction.label("loop-2"),
				BuiltinCommands.overInt(),
				BuiltinCommands.overInt(),
				BuiltinCommands.isGE(),
				Instruction.branch("if-3", "endif-3"),

				Instruction.label("if-3"),
				BuiltinCommands.dropInt(),
				BuiltinCommands.dropInt(),
				Instruction.jump("endloop-2"),

				Instruction.label("endif-3"),
				BuiltinCommands.swapInt(),
				BuiltinCommands.dup(),
				Instruction.command("."),
				Instruction.literal(1),
				BuiltinCommands.add(),
				BuiltinCommands.swapInt(),
				Instruction.jump("loop-2"),

				Instruction.label("endloop-2")
		))).checkTypes(typeChecker);
	}

	@Test
	public void testDetermineTypes() {
		Assert.assertArrayEquals(new TypeList[]{
				                         TypeList.INT, // start:
				                         TypeList.INT, // 1
				                         TypeList.INT2, // +
				                         TypeList.INT, // dup
				                         TypeList.INT2, // 10
				                         TypeList.INT3, // <
				                         TypeList.INT.append(Type.Bool), // branch
				                         TypeList.INT // end:
		                         },
		                         TypeCheckerImpl.determineTypes(List.of(
				                         Instruction.label("start"),
				                         Instruction.literal(1),
				                         BuiltinCommands.add(),
				                         BuiltinCommands.dup(),
				                         Instruction.literal(10),
				                         BuiltinCommands.isLT(),
				                         Instruction.branch("start", "end"),
				                         Instruction.label("end")
		                         ), TypeList.INT, new TypeCheckerImpl()));

		Assert.assertArrayEquals(new TypeList[]{
				                         TypeList.EMPTY, // jump skip
				                         null, // 1
				                         null, // +
				                         TypeList.EMPTY, // skip:
				                         TypeList.EMPTY // 3
		                         },
		                         TypeCheckerImpl.determineTypes(List.of(
				                         Instruction.jump("skip"),
				                         Instruction.literal(1),
				                         BuiltinCommands.add(),
				                         Instruction.label("skip"),
				                         Instruction.literal(3)
		                         ), TypeList.EMPTY, new TypeCheckerImpl()));
	}
}
