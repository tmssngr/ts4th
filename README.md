# TS4th
This experimental project is about a stack-based programming language like Forth witten in Java.
Currently, it can be compiled to Windows 64-bit executables using the [flat assembler](https://flatassembler.net/), but I want to keep the ability to compile it to Z8 assembly for supporting an 8-bit computer from my youth.

The main difference to classic Forth is to have some rudimentary type system.
With classic Forth there is a plain (data) stack of exactly one type and it is the task of the developer to remember which stack entry represents what, e.g. a normal number of a pointer.
Usually, with Forth the developer wrote comments declaring how a *word* (procedure) manipulates the stack:
```
: foo (a b -- c) ... ;
```
With TS4th this comment becomes a requirement like a method definition in other (more or less) modern programming languages:
```
fn foo (int ptr -- ptr)
  ...
end
```
If the function has no return values, the `--` can be omitted.

Use spaces/tabs or line breaks to align the literals and commands according to your needs.

## Types
TS4th supports the types `i8`, `i16` (or `int`), `i32`, `i64` (`i` stands for s**i**gned), `u8`, `u16`, `u32`, `u64` (`u` stands for **u**nsigned), `bool` and `ptr`.
While in classic Forth each integer value could also be used as input for a boolean operation, this would be rejected by TS4th.

Char literals are always of type `u8` (so not yet very unicode friendly).
They can contain usual escape sequences, e.g. `'\n'`.

Integer literals by default are of type `int` unless they are suffixed with `i8`, ..., `u64`, e.g. `1u8` (1 byte unsigned).

To convert between different integer types, use `as_i8`, ..., `as_u64`.
At the moment no conversion happens automatically, and casts from one type to itself is rejected.

String literals like `"hello world"` will put a `ptr` and an its length `int` (=`i16`) onto the stack.
As char literals they can contain usual escape sequences, e.g. `"hello \"world\""`.

## Built-in commands (Intrinsics)
Following commands are supported:
- `+`, `-`, `*`, `/`, `%` (mod), `shl` and `shr` (all require the same integer type 2 times on the stack and will produce the same type)
- `and`, `or` and `xor` (bitwise: require the same integer type 2 times and produce the same type; logical: requires `bool` 2 times and produces a `bool`)
- `<`, `<=`, `==`, `!=`, `>=`, `>` (all require the same integer type 2 times on the stack and will produce a `bool`)
- read from memory: `@8 (ptr--u8)`, ..., `@64 (ptr--u64)`
- write to memory: `@8 (ptr u8--)`, ..., `@64 (ptr u64--)`
- print any integer or `bool`: `print`
- print a character: `emit (u8--)`
- print a string: `printString (ptr u16--)`
- abort the compilation and log the function's data stack types: `???`

## Controls structures
### If
The if statement requires a `bool` value on the stack.
```
fn isSmaller10(int)
	10 < if
		"is smaller than 10" printString
	end
end
```
It also can contain an else branch:
```
fn printBool(bool)
	if
		"true"
	else
		"false"
	printString
end
```
**Note, that each branch needs to produce the same amount of stack types!**

### While loop
The while-loop (`while ... do ...end`) consists of 2 parts, the condition between `while` and `do` and the loop body between `do` and `end`.
You can use `continue` to start a new iteration or `break` to leave the loop.

A for-loop can be simulated:
```
0              // start value for the counter
while dup 10 <
do
	dup print
	1 +        // increment the counter
end
drop           // drop the counter
```
**Note: The condition needs to produce a `bool` value and the method body must not change the stack types!**

## Local Variables
Local variables can be defined between `var` and `do` and used between `do` and `end`.
They take their initial value and type from the stack in the order they occur.
Use a trailing `!` to write to this variable:
```
0 var i do
	while i 10 <
	do
		i print
		i 1 + i! // increment the counter
	end
end
```

They greatly simplify more complex stack operations:
```
10 20 30
rot print  // move the 3rd stack element (10) to the top and print it
swap print // move the 2nd stack element (20) to the top and print it
print      // print the remaing 1st stack element (30)
```
can much easier be understood:
```
10 20 30 var a b c do
	a print
	b print
	c print
end
```

## Inspiring Resources
Back in 2000 Stephan Becher already started a similar project with [StrongForth](https://www.stephan-becher.de/strongforth/).
I'm also inspired by an excellent [YT-series](https://www.youtube.com/watch?v=8QP2fDBIxjM&list=PLpM-Dvs8t0VbMZA7wW9aR3EtBqe2kinu4) about [Porth](https://gitlab.com/tsoding/porth), where Python was used to bootstrap a stack-based programming language compiler.
