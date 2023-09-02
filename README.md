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
def foo (int ptr -- ptr)
  ...
end
```

## Inspiring Resources
Back in 2000 Stephan Becher already started a similar project with [StrongForth](https://www.stephan-becher.de/strongforth/).
I'm also inspired by an excellent [YT-series](https://www.youtube.com/watch?v=8QP2fDBIxjM&list=PLpM-Dvs8t0VbMZA7wW9aR3EtBqe2kinu4) about [Porth](https://gitlab.com/tsoding/porth), where Python was used to bootstrap a stack-based programming language compiler.
