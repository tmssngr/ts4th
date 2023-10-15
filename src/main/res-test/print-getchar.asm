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
.i1:
        ; -- getChar --
        sub  rsp, 8
          call tsfbi_getChar
        add rsp, 8
        ; -- push 0 (u16) --
        sub r15, 2
        mov [r15], cx
        ; -- call printHex4 --
        call tsf_printHex4
        ; -- literal r0, #10 --
        mov cl, 10
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- jump .i1 --
        jmp .i1

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

tsfbi_printUint:
        push   rbp
        mov    rbp,rsp
        sub    rsp, 50h
        mov    qword [rsp+24h], rcx

        ; int pos = sizeof(buf);
        mov    ax, 20h
        mov    word [rsp+20h], ax

        ; do {
.print:
        ; pos--;
        mov    ax, word [rsp+20h]
        dec    ax
        mov    word [rsp+20h], ax

        ; int remainder = x mod 10;
        ; x = x / 10;
        mov    rax, qword [rsp+24h]
        mov    ecx, 10
        xor    edx, edx
        div    ecx
        mov    qword [rsp+24h], rax

        ; int digit = remainder + '0';
        add    dl, '0'

        ; buf[pos] = digit;
        mov    ax, word [rsp+20h]
        movzx  rax, ax
        lea    rcx, qword [rsp]
        add    rcx, rax
        mov    byte [rcx], dl

        ; } while (x > 0);
        mov    rax, qword [rsp+24h]
        cmp    rax, 0
        ja     .print

        ; rcx = &buf[pos]

        ; rdx = sizeof(buf) - pos
        mov    ax, word [rsp+20h]
        movzx  rax, ax
        mov    rdx, 20h
        sub    rdx, rax

        ;sub    rsp, 8  not necessary because initial push rbp
          call   tsfbi_printString
        ;add    rsp, 8
        leave ; Set SP to BP, then pop BP
        ret

tsfbi_getChar:
        mov rdi, rsp
        and spl, 0xf0
          sub rsp, 20h
            call [_getch]
            test al, al
            js   .x1
            jnz  .x2
            dec  al
.x1:
            mov  rbx, rax
            shl  rbx, 8
            call [_getch]
            or   rax, rbx
.x2:
            mov  rcx, rax
          ; add rsp, 20h
        mov rsp, rdi
        ret

; string constants
section '.data' data readable
true_string db 'true'
false_string db 'false'

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
