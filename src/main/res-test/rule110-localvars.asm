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


        ; -- proc fill --
tsf_fill:
        ; -- pop 0 (u8) --
        mov cl, [r15]
        add r15, 1
        ; -- push var r0 (u8) --
        push cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push var r0 (i16) --
        push cx
        ; -- pop 0 (ptr) --
        mov rcx, [r15]
        add r15, 8
        ; -- push var r0 (ptr) --
        push rcx
.i1:
        ; -- read var r0, [ptr (i16)] --
        mov cx, [rsp+8]
        ; -- gt r0, 0 (i16) --
        cmp   cx, 0
        mov   cx, 0
        mov   bx, 1
        cmovg cx, bx
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i3 --
        jz .i3
        ; -- read var r1, [<empty> (ptr)] --
        mov rax, [rsp+0]
        ; -- read var r0, [ptr, i16 (u8)] --
        mov cx, [rsp+10]
        ; -- store @1, 0 (u8) --
        mov byte [rax], cl
        ; -- read var r0, [<empty> (ptr)] --
        mov rcx, [rsp+0]
        ; -- literal r1, #1 --
        mov ax, 1
        ; -- cast 1, (i16 -> ptr) --
        movsx rax, ax
        ; -- add r0, r1 (ptr) --
        add rcx, rax
        ; -- write var [<empty> (ptr)], 0 --
        mov [rsp+0], rcx
        ; -- read var r0, [ptr (i16)] --
        mov cx, [rsp+8]
        ; -- sub r0, 1 (i16) --
        sub cx, 1
        ; -- write var [ptr (i16)], 0 --
        mov [rsp+8], cx
        ; -- jump .i1 --
        jmp .i1
.i3:
        ; -- drop vars ptr, i16, u8 --
        add rsp, 12
        ; -- ret --
        ret

        ; -- proc printBoard --
tsf_printBoard:
        ; -- literal r0, #124 --
        mov cl, 124
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- literal r0, #0 --
        mov cx, 0
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- literal r0, #30 --
        mov cx, 30
        ; -- sub r0, 2 (i16) --
        sub cx, 2
        ; -- push var r0 (i16) --
        push cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push var r0 (i16) --
        push cx
.i1:
        ; -- read var r0, [<empty> (i16)] --
        mov cx, [rsp+0]
        ; -- read var r1, [i16 (i16)] --
        mov ax, [rsp+2]
        ; -- lt r0, r1 (i16) --
        cmp   cx, ax
        mov   cx, 0
        mov   ax, 1
        cmovl cx, ax
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i3 --
        jz .i3
        ; -- literal r0, "0 --
        lea rcx, [string_0]
        ; -- push 0 (ptr) --
        sub r15, 8
        mov [r15], rcx
        ; -- literal r0, #2 --
        mov cx, 2
        ; -- var r0, @board --
        lea rcx, [var_0]
        ; -- read var r1, [<empty> (i16)] --
        mov ax, [rsp+0]
        ; -- cast 1, (i16 -> ptr) --
        movsx rax, ax
        ; -- add r0, r1 (ptr) --
        add rcx, rax
        ; -- mov 1, 0 (ptr) --
        mov rax, rcx
        ; -- load 0 (u8), @1 --
        mov cl, byte [rax]
        ; -- cast 0, (u8 -> i16) --
        movsx cx, cl
        ; -- mov 1, 0 (i16) --
        mov ax, cx
        ; -- pop 0 (ptr) --
        mov rcx, [r15]
        add r15, 8
        ; -- cast 1, (i16 -> ptr) --
        movsx rax, ax
        ; -- add r0, r1 (ptr) --
        add rcx, rax
        ; -- mov 1, 0 (ptr) --
        mov rax, rcx
        ; -- load 0 (u8), @1 --
        mov cl, byte [rax]
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- read var r0, [<empty> (i16)] --
        mov cx, [rsp+0]
        ; -- add r0, 1 (i16) --
        add cx, 1
        ; -- write var [<empty> (i16)], 0 --
        mov [rsp+0], cx
        ; -- jump .i1 --
        jmp .i1
.i3:
        ; -- drop vars i16, i16 --
        add rsp, 4
        ; -- literal r0, #124 --
        mov cl, 124
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- literal r0, #10 --
        mov cl, 10
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- ret --
        ret

        ; -- proc main --
tsf_main:
        ; -- var r0, @board --
        lea rcx, [var_0]
        ; -- push 0 (ptr) --
        sub r15, 8
        mov [r15], rcx
        ; -- literal r0, #30 --
        mov cx, 30
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- literal r0, #0 --
        mov cl, 0
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- call fill --
        call tsf_fill
        ; -- var r0, @board --
        lea rcx, [var_0]
        ; -- push 0 (ptr) --
        sub r15, 8
        mov [r15], rcx
        ; -- literal r0, #30 --
        mov cx, 30
        ; -- sub r0, 2 (i16) --
        sub cx, 2
        ; -- mov 1, 0 (i16) --
        mov ax, cx
        ; -- pop 0 (ptr) --
        mov rcx, [r15]
        add r15, 8
        ; -- cast 1, (i16 -> ptr) --
        movsx rax, ax
        ; -- add r0, r1 (ptr) --
        add rcx, rax
        ; -- mov 1, 0 (ptr) --
        mov rax, rcx
        ; -- literal r0, #1 --
        mov cl, 1
        ; -- store @1, 0 (u8) --
        mov byte [rax], cl
        ; -- literal r0, #0 --
        mov cx, 0
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- literal r0, #30 --
        mov cx, 30
        ; -- sub r0, 2 (i16) --
        sub cx, 2
        ; -- push var r0 (i16) --
        push cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push var r0 (i16) --
        push cx
.i1:
        ; -- read var r0, [<empty> (i16)] --
        mov cx, [rsp+0]
        ; -- read var r1, [i16 (i16)] --
        mov ax, [rsp+2]
        ; -- lt r0, r1 (i16) --
        cmp   cx, ax
        mov   cx, 0
        mov   ax, 1
        cmovl cx, ax
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i3 --
        jz .i3
        ; -- call printBoard --
        call tsf_printBoard
        ; -- var r1, @board --
        lea rax, [var_0]
        ; -- load 0 (u8), @1 --
        mov cl, byte [rax]
        ; -- cast 0, (u8 -> i16) --
        movsx cx, cl
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- var r0, @board --
        lea rcx, [var_0]
        ; -- literal r1, #1 --
        mov ax, 1
        ; -- cast 1, (i16 -> ptr) --
        movsx rax, ax
        ; -- add r0, r1 (ptr) --
        add rcx, rax
        ; -- mov 1, 0 (ptr) --
        mov rax, rcx
        ; -- load 0 (u8), @1 --
        mov cl, byte [rax]
        ; -- cast 0, (u8 -> i16) --
        movsx cx, cl
        ; -- mov 1, 0 (i16) --
        mov ax, cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- or r0, r1 (i16) --
        or cx, ax
        ; -- push var r0 (i16) --
        push cx
        ; -- literal r0, #0 --
        mov cx, 0
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- literal r0, #30 --
        mov cx, 30
        ; -- sub r0, 1 (i16) --
        sub cx, 1
        ; -- push var r0 (i16) --
        push cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push var r0 (i16) --
        push cx
.i4:
        ; -- read var r0, [<empty> (i16)] --
        mov cx, [rsp+0]
        ; -- read var r1, [i16 (i16)] --
        mov ax, [rsp+2]
        ; -- lt r0, r1 (i16) --
        cmp   cx, ax
        mov   cx, 0
        mov   ax, 1
        cmovl cx, ax
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i6 --
        jz .i6
        ; -- read var r1, [i16, i16 (i16)] --
        mov ax, [rsp+4]
        ; -- shl r1, 1 (i16) --
        shl ax, 1
        ; -- mov 0, 1 (i16) --
        mov cx, ax
        ; -- and r0, 7 (i16) --
        and cx, 7
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- var r0, @board --
        lea rcx, [var_0]
        ; -- push 0 (ptr) --
        sub r15, 8
        mov [r15], rcx
        ; -- read var r0, [<empty> (i16)] --
        mov cx, [rsp+0]
        ; -- add r0, 1 (i16) --
        add cx, 1
        ; -- mov 1, 0 (i16) --
        mov ax, cx
        ; -- pop 0 (ptr) --
        mov rcx, [r15]
        add r15, 8
        ; -- cast 1, (i16 -> ptr) --
        movsx rax, ax
        ; -- add r0, r1 (ptr) --
        add rcx, rax
        ; -- mov 1, 0 (ptr) --
        mov rax, rcx
        ; -- load 0 (u8), @1 --
        mov cl, byte [rax]
        ; -- cast 0, (u8 -> i16) --
        movsx cx, cl
        ; -- mov 1, 0 (i16) --
        mov ax, cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- or r0, r1 (i16) --
        or cx, ax
        ; -- write var [i16, i16 (i16)], 0 --
        mov [rsp+4], cx
        ; -- literal r1, #110 --
        mov ax, 110
        ; -- read var r0, [i16, i16 (i16)] --
        mov cx, [rsp+4]
        ; -- shr r1, r0 (i16) --
        shr ax, cl
        ; -- mov 0, 1 (i16) --
        mov cx, ax
        ; -- and r0, 1 (i16) --
        and cx, 1
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- var r0, @board --
        lea rcx, [var_0]
        ; -- read var r1, [<empty> (i16)] --
        mov ax, [rsp+0]
        ; -- cast 1, (i16 -> ptr) --
        movsx rax, ax
        ; -- add r0, r1 (ptr) --
        add rcx, rax
        ; -- mov 1, 0 (ptr) --
        mov rax, rcx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push 1 (ptr) --
        sub r15, 8
        mov [r15], rax
        ; -- pop 1 (ptr) --
        mov rax, [r15]
        add r15, 8
        ; -- store @1, 0 (u8) --
        mov byte [rax], cl
        ; -- read var r0, [<empty> (i16)] --
        mov cx, [rsp+0]
        ; -- add r0, 1 (i16) --
        add cx, 1
        ; -- write var [<empty> (i16)], 0 --
        mov [rsp+0], cx
        ; -- jump .i4 --
        jmp .i4
.i6:
        ; -- drop vars i16, i16, i16 --
        add rsp, 6
        ; -- read var r0, [<empty> (i16)] --
        mov cx, [rsp+0]
        ; -- add r0, 1 (i16) --
        add cx, 1
        ; -- write var [<empty> (i16)], 0 --
        mov [rsp+0], cx
        ; -- jump .i1 --
        jmp .i1
.i3:
        ; -- drop vars i16, i16 --
        add rsp, 4
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
        string_0 db ' *'

section '.data' data readable writeable
        hStdIn  rb 8
        hStdOut rb 8
        hStdErr rb 8
        ; board
        var_0 rb 30

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
