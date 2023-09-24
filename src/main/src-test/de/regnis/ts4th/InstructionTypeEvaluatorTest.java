package de.regnis.ts4th;

import java.util.*;

import org.junit.*;

import static de.regnis.ts4th.InstructionFactory.*;

/**
 * @author Thomas Singer
 */
public class InstructionTypeEvaluatorTest {

	@Test
	public void testEvaluate() {
		Assert.assertEquals(List.of(
				new TypedInstruction(label("start"), TypeList.INT),
				new TypedInstruction(literal(1), TypeList.INT),
				new TypedInstruction(add(), TypeList.INT2),
				new TypedInstruction(dup(), TypeList.INT),
				new TypedInstruction(literal(10), TypeList.INT2),
				new TypedInstruction(isLT(), TypeList.INT3),
				new TypedInstruction(branch("start", "end"), TypeList.INT.append(Type.Bool)),
				new TypedInstruction(label("end"), TypeList.INT)
		), evaluate(List.of(
				label("start"),
				literal(1),
				add(),
				dup(),
				literal(10),
				isLT(),
				branch("start", "end"),
				label("end")
		), TypeList.INT));
	}

	private List<TypedInstruction> evaluate(List<Instruction> instructions, TypeList input) {
		final List<TypedInstruction> typedInstructions = new ArrayList<>();
		final TypeChecker typeChecker = new TypeCheckerImpl();
		InstructionTypeEvaluator.iterate(instructions, input, (instruction, types) -> {
			typedInstructions.add(new TypedInstruction(instruction, types));
			return typeChecker.checkType(instruction, types);
		});
		return typedInstructions;
	}

	private record TypedInstruction(Instruction instruction, TypeList typeList) {
	}
}
