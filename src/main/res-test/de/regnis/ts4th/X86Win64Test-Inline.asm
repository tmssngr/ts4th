format pe64 console
include 'win64ax.inc'

STD_IN_HANDLE = -10
STD_OUT_HANDLE = -11
STD_ERR_HANDLE = -12
STACK_SIZE = 1024 * 8

entry start

section '.text' code readable executable

start:
        mov r15, rsp
        sub rsp, STACK_SIZE
        sub rsp, 8
          call init
          call tsf_main
        add rsp, 8
        mov rcx, 0
        sub rsp, 0x20
          call [ExitProcess]


        ; -- proc main --
tsf_main:
        ; -- literal r0, #1 --
        mov cx, 1
        ; -- lt r0, 2 (i16) --
        cmp   cx, 2
        mov   cx, 0
        mov   bx, 1
        cmovl cx, bx
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i2 --
        jz .i2
        ; -- literal r0, "0 --
        lea rcx, [string_0]
        ; -- push 0 (ptr) --
        sub r15, 8
        mov [r15], rcx
        ; -- literal r0, #5 --
        mov cx, 5
        ; -- push 0 (u16) --
        sub r15, 2
        mov [r15], cx
        ; -- jump .i3 --
        jmp .i3
.i2:
        ; -- literal r0, "1 --
        lea rcx, [string_1]
        ; -- push 0 (ptr) --
        sub r15, 8
        mov [r15], rcx
        ; -- literal r0, #6 --
        mov cx, 6
        ; -- push 0 (u16) --
        sub r15, 2
        mov [r15], cx
.i3:
        ; -- pop 0 (u16) --
        mov cx, [r15]
        add r15, 2
        ; -- pop 1 (ptr) --
        mov rax, [r15]
        add r15, 8
        ; -- printString r1 (0) --
        movsx rdx, cx
        mov rcx, rax
        sub rsp, 8
          call tsfbi_printString
        add rsp, 8
        ; -- literal r0, #1 --
        mov cx, 1
        ; -- gt r0, 2 (i16) --
        cmp   cx, 2
        mov   cx, 0
        mov   bx, 1
        cmovg cx, bx
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i5 --
        jz .i5
        ; -- literal r0, "0 --
        lea rcx, [string_0]
        ; -- push 0 (ptr) --
        sub r15, 8
        mov [r15], rcx
        ; -- literal r0, #5 --
        mov cx, 5
        ; -- push 0 (u16) --
        sub r15, 2
        mov [r15], cx
        ; -- jump .i6 --
        jmp .i6
.i5:
        ; -- literal r0, "1 --
        lea rcx, [string_1]
        ; -- push 0 (ptr) --
        sub r15, 8
        mov [r15], rcx
        ; -- literal r0, #6 --
        mov cx, 6
        ; -- push 0 (u16) --
        sub r15, 2
        mov [r15], cx
.i6:
        ; -- pop 0 (u16) --
        mov cx, [r15]
        add r15, 2
        ; -- pop 1 (ptr) --
        mov rax, [r15]
        add r15, 8
        ; -- printString r1 (0) --
        movsx rdx, cx
        mov rcx, rax
        sub rsp, 8
          call tsfbi_printString
        add rsp, 8
        ; -- ret --
        ret

init:
           sub  rsp, 20h
             mov  rcx, STD_IN_HANDLE
             call [GetStdHandle]
             ; handle in rax, 0 if invalid
             lea rcx, [hStdIn]
             mov qword [rcx], rax

             mov  rcx, STD_OUT_HANDLE
             call [GetStdHandle]
             ; handle in rax, 0 if invalid
             lea rcx, [hStdOut]
             mov qword [rcx], rax

             mov  rcx, STD_ERR_HANDLE
             call [GetStdHandle]
             ; handle in rax, 0 if invalid
             lea rcx, [hStdErr]
             mov qword [rcx], rax
           add  rsp, 20h
        ret

tsfbi_printString:
        mov     rdi, rsp
        and     spl, 0xf0

        mov     r8, rdx
        mov     rdx, rcx
        lea     rcx, [hStdOut]
        mov     rcx, qword [rcx]
        xor     r9, r9
        push    0
          sub     rsp, 20h
            call    [WriteFile]
          add     rsp, 20h
        ; add     rsp, 8
        mov     rsp, rdi
        ret

; string constants
section '.data' data readable
        string_0 db 'true', 0x0a
        string_1 db 'false', 0x0a

section '.data' data readable writeable
        hStdIn  rb 8
        hStdOut rb 8
        hStdErr rb 8

mem rb 640000

section '.idata' import data readable writeable

library kernel32,'KERNEL32.DLL',\
        msvcrt,'MSVCRT.DLL'

import kernel32,\
       ExitProcess,'ExitProcess',\
       GetStdHandle,'GetStdHandle',\
       SetConsoleCursorPosition,'SetConsoleCursorPosition',\
       WriteFile,'WriteFile'

import msvcrt,\
       _getch,'_getch'
