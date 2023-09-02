- error messages with row:col information
- replace
	push a
	pop b
  with
    mov b, a
- replace
	lt/le/eq/ne/ge/gt
	test
	jump z
  with
    cmp
    jump ge/gt/ne/eq/lt/le
