- structs
- malloc/free
	- get rid of `mem`
- pointer-to type (pointer-to primitive or pointer-to pointer-to)
- `return` statement
- `elif` or `switch` (to avoid multiple `end` at the end)
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
		```
		lt/le/eq/ne/ge/gt
		test
		jump z
		```
	  with
		```
		cmp
		jump ge/gt/ne/eq/lt/le
		```
- compile-time asserts
- local variables:
	- maybe, instead of pushing each variable using a single AsmIR.PushVar, it might be better to allocate a larger block at once (rounding up to 0xFFFFFFF0) and use AsmIR.LocalVarWrite operations
	- maybe, instead of pushing variables immediately to the stack, the lastly created could reside in some registers
- maybe allow blocks of (platform specific) assembly code which literally is passed to the resulting asm file
