- [local variables](https://www.youtube.com/watch?v=NWmJdtT6Fww&list=PLpM-Dvs8t0VbMZA7wW9aR3EtBqe2kinu4&index=43)
- error messages with row:col information
- easier way to define (global) variables
- logic and/or (make them work on `bool`, too)
- more types (i8, i16, i32, i64, u8, u16, u32, u64)
- `elif` (to avoid multiple `end` at the end)
- asm optimization
	- replace
		lt/le/eq/ne/ge/gt
		test
		jump z
	  with
	    cmp
	    jump ge/gt/ne/eq/lt/le
- compile-time asserts
