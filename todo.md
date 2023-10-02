- [local variables](https://www.youtube.com/watch?v=NWmJdtT6Fww&list=PLpM-Dvs8t0VbMZA7wW9aR3EtBqe2kinu4&index=43)
- logic and/or (make them work on `bool`, too)
- `elif` (to avoid multiple `end` at the end)
- asm optimization
	- replace
		```
		cast X, (sT -> tT)
		mov Y, X (tT)
		```
		with
		```
		mov Y, X (sT)
		cast Y, (sT -> tT)
		```
	- replace
		lt/le/eq/ne/ge/gt
		test
		jump z
	  with
	    cmp
	    jump ge/gt/ne/eq/lt/le
- compile-time asserts
- local variables: maybe, instead of pushing each variable using a single AsmIR.PushVar, it might be better to allocate a larger block at once (rounding up to 0xFFFFFFF0) and use AsmIR.LocalVarWrite operations
