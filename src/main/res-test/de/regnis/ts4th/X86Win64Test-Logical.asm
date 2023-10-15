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
        ; -- literal r1, "0 --
        lea rax, [string_0]
        ; -- literal r0, #5 --
        mov cx, 5
        ; -- printString r1 (0) --
        movsx rdx, cx
        mov rcx, rax
        sub rsp, 8
          call tsfbi_printString
        add rsp, 8
        ; -- literal r0, #false --
        mov cl, 0
        ; -- literal r1, #false --
        mov al, 0
        ; -- and r0, r1 (bool) --
        and cl, al
        ; -- printBool --
        or cl, cl
        lea rcx, [false_string]
        mov rdx, 5
        jz .x1
        lea rcx, [true_string]
        mov rdx, 4
.x1:
        sub  rsp, 8
          call tsfbi_printString
        add rsp, 8
        ; -- literal r0, #32 --
        mov cl, 32
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- literal r0, #false --
        mov cl, 0
        ; -- literal r1, #true --
        mov al, -1
        ; -- and r0, r1 (bool) --
        and cl, al
        ; -- printBool --
        or cl, cl
        lea rcx, [false_string]
        mov rdx, 5
        jz .x2
        lea rcx, [true_string]
        mov rdx, 4
.x2:
        sub  rsp, 8
          call tsfbi_printString
        add rsp, 8
        ; -- literal r0, #32 --
        mov cl, 32
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- literal r0, #true --
        mov cl, -1
        ; -- literal r1, #false --
        mov al, 0
        ; -- and r0, r1 (bool) --
        and cl, al
        ; -- printBool --
        or cl, cl
        lea rcx, [false_string]
        mov rdx, 5
        jz .x3
        lea rcx, [true_string]
        mov rdx, 4
.x3:
        sub  rsp, 8
          call tsfbi_printString
        add rsp, 8
        ; -- literal r0, #32 --
        mov cl, 32
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- literal r0, #true --
        mov cl, -1
        ; -- literal r1, #true --
        mov al, -1
        ; -- and r0, r1 (bool) --
        and cl, al
        ; -- printBool --
        or cl, cl
        lea rcx, [false_string]
        mov rdx, 5
        jz .x4
        lea rcx, [true_string]
        mov rdx, 4
.x4:
        sub  rsp, 8
          call tsfbi_printString
        add rsp, 8
        ; -- literal r0, #10 --
        mov cl, 10
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- literal r1, "1 --
        lea rax, [string_1]
        ; -- literal r0, #5 --
        mov cx, 5
        ; -- printString r1 (0) --
        movsx rdx, cx
        mov rcx, rax
        sub rsp, 8
          call tsfbi_printString
        add rsp, 8
        ; -- literal r0, #false --
        mov cl, 0
        ; -- literal r1, #false --
        mov al, 0
        ; -- or r0, r1 (bool) --
        or cl, al
        ; -- printBool --
        or cl, cl
        lea rcx, [false_string]
        mov rdx, 5
        jz .x5
        lea rcx, [true_string]
        mov rdx, 4
.x5:
        sub  rsp, 8
          call tsfbi_printString
        add rsp, 8
        ; -- literal r0, #32 --
        mov cl, 32
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- literal r0, #false --
        mov cl, 0
        ; -- literal r1, #true --
        mov al, -1
        ; -- or r0, r1 (bool) --
        or cl, al
        ; -- printBool --
        or cl, cl
        lea rcx, [false_string]
        mov rdx, 5
        jz .x6
        lea rcx, [true_string]
        mov rdx, 4
.x6:
        sub  rsp, 8
          call tsfbi_printString
        add rsp, 8
        ; -- literal r0, #32 --
        mov cl, 32
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- literal r0, #true --
        mov cl, -1
        ; -- literal r1, #false --
        mov al, 0
        ; -- or r0, r1 (bool) --
        or cl, al
        ; -- printBool --
        or cl, cl
        lea rcx, [false_string]
        mov rdx, 5
        jz .x7
        lea rcx, [true_string]
        mov rdx, 4
.x7:
        sub  rsp, 8
          call tsfbi_printString
        add rsp, 8
        ; -- literal r0, #32 --
        mov cl, 32
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- literal r0, #true --
        mov cl, -1
        ; -- literal r1, #true --
        mov al, -1
        ; -- or r0, r1 (bool) --
        or cl, al
        ; -- printBool --
        or cl, cl
        lea rcx, [false_string]
        mov rdx, 5
        jz .x8
        lea rcx, [true_string]
        mov rdx, 4
.x8:
        sub  rsp, 8
          call tsfbi_printString
        add rsp, 8
        ; -- literal r0, #10 --
        mov cl, 10
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- literal r1, "2 --
        lea rax, [string_2]
        ; -- literal r0, #5 --
        mov cx, 5
        ; -- printString r1 (0) --
        movsx rdx, cx
        mov rcx, rax
        sub rsp, 8
          call tsfbi_printString
        add rsp, 8
        ; -- literal r0, #false --
        mov cl, 0
        ; -- literal r1, #false --
        mov al, 0
        ; -- xor r0, r1 (bool) --
        xor cl, al
        ; -- printBool --
        or cl, cl
        lea rcx, [false_string]
        mov rdx, 5
        jz .x9
        lea rcx, [true_string]
        mov rdx, 4
.x9:
        sub  rsp, 8
          call tsfbi_printString
        add rsp, 8
        ; -- literal r0, #32 --
        mov cl, 32
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- literal r0, #false --
        mov cl, 0
        ; -- literal r1, #true --
        mov al, -1
        ; -- xor r0, r1 (bool) --
        xor cl, al
        ; -- printBool --
        or cl, cl
        lea rcx, [false_string]
        mov rdx, 5
        jz .x10
        lea rcx, [true_string]
        mov rdx, 4
.x10:
        sub  rsp, 8
          call tsfbi_printString
        add rsp, 8
        ; -- literal r0, #32 --
        mov cl, 32
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- literal r0, #true --
        mov cl, -1
        ; -- literal r1, #false --
        mov al, 0
        ; -- xor r0, r1 (bool) --
        xor cl, al
        ; -- printBool --
        or cl, cl
        lea rcx, [false_string]
        mov rdx, 5
        jz .x11
        lea rcx, [true_string]
        mov rdx, 4
.x11:
        sub  rsp, 8
          call tsfbi_printString
        add rsp, 8
        ; -- literal r0, #32 --
        mov cl, 32
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- literal r0, #true --
        mov cl, -1
        ; -- literal r1, #true --
        mov al, -1
        ; -- xor r0, r1 (bool) --
        xor cl, al
        ; -- printBool --
        or cl, cl
        lea rcx, [false_string]
        mov rdx, 5
        jz .x12
        lea rcx, [true_string]
        mov rdx, 4
.x12:
        sub  rsp, 8
          call tsfbi_printString
        add rsp, 8
        ; -- literal r0, #10 --
        mov cl, 10
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
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

tsfbi_emit:
        push rcx ; = sub rsp, 8
          mov rcx, rsp
          mov rdx, 1
          call tsfbi_printString
        pop rcx
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
        true_string  db 'true'
        false_string db 'false'

        string_0 db 'and: '
        string_1 db 'or:  '
        string_2 db 'xor: '

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
