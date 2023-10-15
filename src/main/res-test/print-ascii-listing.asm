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


        ; -- proc printNibble --
tsf_printNibble:
        ; -- pop 0 (u8) --
        mov cl, [r15]
        add r15, 1
        ; -- and r0, 15 (u8) --
        and cl, 15
        ; -- add r0, 48 (u8) --
        add cl, 48
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- gt r0, 57 (u8) --
        cmp   cl, 57
        mov   cx, 0
        mov   bx, 1
        cmova cx, bx
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i2 --
        jz .i2
        ; -- literal r0, #65 --
        mov cl, 65
        ; -- sub r0, 57 (u8) --
        sub cl, 57
        ; -- sub r0, 1 (u8) --
        sub cl, 1
        ; -- mov 1, 0 (u8) --
        mov al, cl
        ; -- pop 0 (u8) --
        mov cl, [r15]
        add r15, 1
        ; -- add r0, r1 (u8) --
        add cl, al
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
.i2:
        ; -- pop 0 (u8) --
        mov cl, [r15]
        add r15, 1
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- ret --
        ret

        ; -- proc printHex2 --
tsf_printHex2:
        ; -- pop 0 (u8) --
        mov cl, [r15]
        add r15, 1
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- mov 1, 0 (u8) --
        mov al, cl
        ; -- shr r1, 4 (u8) --
        shr al, 4
        ; -- push 1 (u8) --
        sub r15, 1
        mov [r15], al
        ; -- call printNibble --
        call tsf_printNibble
        ; -- call printNibble --
        call tsf_printNibble
        ; -- ret --
        ret

        ; -- proc printHex4 --
tsf_printHex4:
        ; -- pop 0 (u16) --
        mov cx, [r15]
        add r15, 2
        ; -- push 0 (u16) --
        sub r15, 2
        mov [r15], cx
        ; -- mov 1, 0 (u16) --
        mov ax, cx
        ; -- shr r1, 8 (u16) --
        shr ax, 8
        ; -- mov 0, 1 (u16) --
        mov cx, ax
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- call printHex2 --
        call tsf_printHex2
        ; -- pop 0 (u16) --
        mov cx, [r15]
        add r15, 2
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- call printHex2 --
        call tsf_printHex2
        ; -- ret --
        ret

        ; -- proc main --
tsf_main:
        ; -- literal r0, #32 --
        mov cl, 32
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- literal r0, #120 --
        mov cl, 120
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- literal r0, #0 --
        mov cl, 0
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- literal r0, #16 --
        mov cl, 16
        ; -- push var r0 (u8) --
        push cx
        ; -- pop 0 (u8) --
        mov cl, [r15]
        add r15, 1
        ; -- push var r0 (u8) --
        push cx
.i1:
        ; -- read var r0, [<empty> (u8)] --
        mov cx, [rsp+0]
        ; -- read var r1, [u8 (u8)] --
        mov ax, [rsp+2]
        ; -- lt r0, r1 (u8) --
        cmp   cl, al
        mov   cx, 0
        mov   ax, 1
        cmovb cx, ax
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i3 --
        jz .i3
        ; -- read var r0, [<empty> (u8)] --
        mov cx, [rsp+0]
        ; -- and r0, 7 (u8) --
        and cl, 7
        ; -- eq r0, 0 (u8) --
        cmp   cl, 0
        mov   cx, 0
        mov   bx, 1
        cmove cx, bx
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i5 --
        jz .i5
        ; -- literal r0, #32 --
        mov cl, 32
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
.i5:
        ; -- read var r0, [<empty> (u8)] --
        mov cx, [rsp+0]
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- call printNibble --
        call tsf_printNibble
        ; -- read var r0, [<empty> (u8)] --
        mov cx, [rsp+0]
        ; -- add r0, 1 (u8) --
        add cl, 1
        ; -- write var [<empty> (u8)], 0 --
        mov [rsp+0], cx
        ; -- jump .i1 --
        jmp .i1
.i3:
        ; -- drop vars u8, u8 --
        add rsp, 4
        ; -- literal r0, #10 --
        mov cl, 10
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- literal r0, #32 --
        mov cl, 32
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- literal r0, #128 --
        mov cl, 128
        ; -- push var r0 (u8) --
        push cx
        ; -- pop 0 (u8) --
        mov cl, [r15]
        add r15, 1
        ; -- push var r0 (u8) --
        push cx
.i7:
        ; -- read var r0, [<empty> (u8)] --
        mov cx, [rsp+0]
        ; -- read var r1, [u8 (u8)] --
        mov ax, [rsp+2]
        ; -- lt r0, r1 (u8) --
        cmp   cl, al
        mov   cx, 0
        mov   ax, 1
        cmovb cx, ax
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i9 --
        jz .i9
        ; -- read var r0, [<empty> (u8)] --
        mov cx, [rsp+0]
        ; -- and r0, 15 (u8) --
        and cl, 15
        ; -- eq r0, 0 (u8) --
        cmp   cl, 0
        mov   cx, 0
        mov   bx, 1
        cmove cx, bx
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i11 --
        jz .i11
        ; -- read var r0, [<empty> (u8)] --
        mov cx, [rsp+0]
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- call printHex2 --
        call tsf_printHex2
.i11:
        ; -- read var r0, [<empty> (u8)] --
        mov cx, [rsp+0]
        ; -- and r0, 7 (u8) --
        and cl, 7
        ; -- eq r0, 0 (u8) --
        cmp   cl, 0
        mov   cx, 0
        mov   bx, 1
        cmove cx, bx
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i13 --
        jz .i13
        ; -- literal r0, #32 --
        mov cl, 32
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
.i13:
        ; -- read var r0, [<empty> (u8)] --
        mov cx, [rsp+0]
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- read var r0, [<empty> (u8)] --
        mov cx, [rsp+0]
        ; -- and r0, 15 (u8) --
        and cl, 15
        ; -- eq r0, 15 (u8) --
        cmp   cl, 15
        mov   cx, 0
        mov   bx, 1
        cmove cx, bx
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i15 --
        jz .i15
        ; -- literal r0, #10 --
        mov cl, 10
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
.i15:
        ; -- read var r0, [<empty> (u8)] --
        mov cx, [rsp+0]
        ; -- add r0, 1 (u8) --
        add cl, 1
        ; -- write var [<empty> (u8)], 0 --
        mov [rsp+0], cx
        ; -- jump .i7 --
        jmp .i7
.i9:
        ; -- drop vars u8, u8 --
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
