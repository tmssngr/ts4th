package de.regnis.ts4th;

import org.junit.*;

/**
 * @author Thomas Singer
 */
public class TypeListTest {

	@Test
	public void testToString() {
		Assert.assertEquals("<empty>", TypeList.EMPTY.toString());
		Assert.assertEquals(Type.I16.toString(), TypeList.INT.toString());
		Assert.assertEquals(Type.I16 + ", " + Type.I16, TypeList.INT2.toString());
		Assert.assertEquals(Type.I16 + ", " + Type.I16 + ", " + Type.I16, TypeList.INT3.toString());
		Assert.assertEquals(Type.I16 + ", " + Type.I16 + ", " + Type.Bool, TypeList.INT.append(Type.I16).append(Type.Bool).toString());
		Assert.assertEquals(Type.I16 + ", " + Type.Bool + ", " + Type.Bool, TypeList.INT.append(Type.Bool).append(Type.Bool).toString());
	}

	@Test
	public void testCanOperateOn() {
		Assert.assertTrue(TypeList.EMPTY.canOperateOn(TypeList.EMPTY));
		Assert.assertTrue(TypeList.EMPTY.canOperateOn(TypeList.INT));
		Assert.assertTrue(TypeList.EMPTY.canOperateOn(TypeList.INT2));
		Assert.assertTrue(TypeList.EMPTY.canOperateOn(TypeList.BOOL));

		Assert.assertTrue(TypeList.INT.canOperateOn(TypeList.INT));
		Assert.assertTrue(TypeList.INT.canOperateOn(TypeList.INT2));
		Assert.assertTrue(TypeList.INT.canOperateOn(TypeList.BOOL.append(Type.I16)));
		Assert.assertTrue(TypeList.INT2.canOperateOn(TypeList.BOOL.append(Type.I16).append(Type.I16)));

		Assert.assertFalse(TypeList.INT.canOperateOn(TypeList.EMPTY));
		Assert.assertFalse(TypeList.INT.canOperateOn(TypeList.BOOL));
		Assert.assertFalse(TypeList.BOOL.canOperateOn(TypeList.EMPTY));
		Assert.assertFalse(TypeList.BOOL.canOperateOn(TypeList.INT));
		Assert.assertFalse(TypeList.INT2.canOperateOn(TypeList.INT));
		Assert.assertFalse(TypeList.INT2.canOperateOn(TypeList.BOOL.append(Type.I16)));
	}

	@Test
	public void testTransform() {
		Assert.assertEquals(TypeList.EMPTY, TypeList.EMPTY.transform(TypeList.EMPTY, TypeList.EMPTY));
		Assert.assertEquals(TypeList.EMPTY, TypeList.INT.transform(TypeList.INT, TypeList.EMPTY));
		Assert.assertEquals(TypeList.EMPTY, TypeList.BOOL.transform(TypeList.BOOL, TypeList.EMPTY));

		Assert.assertEquals(TypeList.INT, TypeList.EMPTY.transform(TypeList.EMPTY, TypeList.INT));
		Assert.assertEquals(TypeList.INT, TypeList.INT.transform(TypeList.INT, TypeList.INT));
		Assert.assertEquals(TypeList.INT, TypeList.BOOL.transform(TypeList.BOOL, TypeList.INT));

		Assert.assertEquals(TypeList.BOOL, TypeList.EMPTY.transform(TypeList.EMPTY, TypeList.BOOL));
		Assert.assertEquals(TypeList.BOOL, TypeList.INT.transform(TypeList.INT, TypeList.BOOL));
		Assert.assertEquals(TypeList.BOOL, TypeList.BOOL.transform(TypeList.BOOL, TypeList.BOOL));

		Assert.assertEquals(TypeList.INT.append(Type.Bool), TypeList.INT2.transform(TypeList.INT, TypeList.BOOL));

		try {
			TypeList.EMPTY.transform(TypeList.INT, TypeList.EMPTY);
		}
		catch (InvalidTypeException ignored) {
		}
		try {
			TypeList.EMPTY.transform(TypeList.INT, TypeList.INT);
		}
		catch (InvalidTypeException ignored) {
		}
		try {
			TypeList.EMPTY.transform(TypeList.INT, TypeList.BOOL);
		}
		catch (InvalidTypeException ignored) {
		}
		try {
			TypeList.INT.transform(TypeList.INT2, TypeList.EMPTY);
		}
		catch (InvalidTypeException ignored) {
		}
		try {
			TypeList.INT.append(Type.Bool).transform(TypeList.BOOL.append(Type.I16), TypeList.EMPTY);
		}
		catch (InvalidTypeException ignored) {
		}
	}
}
