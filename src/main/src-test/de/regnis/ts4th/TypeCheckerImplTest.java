package de.regnis.ts4th;

import org.junit.*;

import java.util.*;

import static de.regnis.ts4th.InstructionFactory.*;

/**
 * @author Thomas Singer
 */
public class TypeCheckerImplTest {

	@Test
	public void testSimple() {
		final TypeChecker typeChecker = new TypeCheckerImpl();
		new CfgFunction(new Function("inc", TypeList.INT, TypeList.INT, false, List.of(
				literal(1),
				add()
		))).checkTypes(typeChecker);

		try {
			new CfgFunction(new Function("inc", TypeList.INT, TypeList.INT, false, List.of(
					literal(1)
			))).checkTypes(typeChecker);
			Assert.fail();
		}
		catch (InvalidTypeException ignored) {
		}

		try {
			new CfgFunction(new Function("inc", TypeList.INT, TypeList.INT, false, List.of(
					add()
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
				add(),
				command(print)
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
				dup2Int(),
				isLT(),
				branch("if-1", "endif-1"),

				label("if-1"),
				swapInt(),
				jump("endif-1"),

				label("endif-1"),
				dropInt()
		))).checkTypes(typeChecker);

		try {
			new CfgFunction(new Function("fails", TypeList.INT2, TypeList.INT, false, List.of(
					isLT(),
					branch("if-1", "endif-1"),

					label("if-1"),
					dropInt(),
					jump("endif-1"),

					label("endif-1")
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
				label("loop-2"),
				overInt(),
				overInt(),
				isGE(),
				branch("if-3", "endif-3"),

				label("if-3"),
				dropInt(),
				dropInt(),
				jump("endloop-2"),

				label("endif-3"),
				swapInt(),
				dup(),
				command("."),
				literal(1),
				add(),
				swapInt(),
				jump("loop-2"),

				label("endloop-2")
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
				                         label("start"),
				                         literal(1),
				                         add(),
				                         dup(),
				                         literal(10),
				                         isLT(),
				                         branch("start", "end"),
				                         label("end")
		                         ), TypeList.INT, new TypeCheckerImpl()));

		Assert.assertArrayEquals(new TypeList[]{
				                         TypeList.EMPTY, // jump skip
				                         null, // 1
				                         null, // +
				                         TypeList.EMPTY, // skip:
				                         TypeList.EMPTY // 3
		                         },
		                         TypeCheckerImpl.determineTypes(List.of(
				                         jump("skip"),
				                         literal(1),
				                         add(),
				                         label("skip"),
				                         literal(3)
		                         ), TypeList.EMPTY, new TypeCheckerImpl()));
	}
}
