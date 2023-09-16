- error messages with row:col information
- easier way to define (global) variables
- logic and/or (make them work on bool, too)
- more types (i8, i16, i32, i64, u8, u16, u32, u64)
- asm optimization
	- replace
		lt/le/eq/ne/ge/gt
		test
		jump z
	  with
	    cmp
	    jump ge/gt/ne/eq/lt/le
- compile-time asserts
