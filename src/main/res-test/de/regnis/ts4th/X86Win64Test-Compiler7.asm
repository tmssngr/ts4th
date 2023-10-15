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


        ; -- proc strlen --
tsf_strlen:
        ; -- pop 0 (ptr) --
        mov rcx, [r15]
        add r15, 8
        ; -- literal r1, #0 --
        mov ax, 0
        ; -- push 1 (i16) --
        sub r15, 2
        mov [r15], ax
        ; -- push 0 (ptr) --
        sub r15, 8
        mov [r15], rcx
.i1:
        ; -- pop 0 (ptr) --
        mov rcx, [r15]
        add r15, 8
        ; -- push 0 (ptr) --
        sub r15, 8
        mov [r15], rcx
        ; -- mov 1, 0 (ptr) --
        mov rax, rcx
        ; -- load 0 (u8), @1 --
        mov cl, byte [rax]
        ; -- cast 0, (u8 -> i16) --
        movsx cx, cl
        ; -- neq r0, 0 (i16) --
        cmp   cx, 0
        mov   cx, 0
        mov   bx, 1
        cmovne cx, bx
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i3 --
        jz .i3
        ; -- pop 1 (ptr) --
        mov rax, [r15]
        add r15, 8
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push 1 (ptr) --
        sub r15, 8
        mov [r15], rax
        ; -- add r0, 1 (i16) --
        add cx, 1
        ; -- mov 1, 0 (i16) --
        mov ax, cx
        ; -- pop 0 (ptr) --
        mov rcx, [r15]
        add r15, 8
        ; -- push 1 (i16) --
        sub r15, 2
        mov [r15], ax
        ; -- literal r1, #1 --
        mov ax, 1
        ; -- cast 1, (i16 -> ptr) --
        movsx rax, ax
        ; -- add r0, r1 (ptr) --
        add rcx, rax
        ; -- push 0 (ptr) --
        sub r15, 8
        mov [r15], rcx
        ; -- jump .i1 --
        jmp .i1
.i3:
        ; -- pop 0 (ptr) --
        mov rcx, [r15]
        add r15, 8
        ; -- ret --
        ret

        ; -- proc main --
tsf_main:
        ; -- literal r0, "0 --
        lea rcx, [string_0]
        ; -- push 0 (ptr) --
        sub r15, 8
        mov [r15], rcx
        ; -- literal r0, #12 --
        mov cx, 12
        ; -- call strlen --
        call tsf_strlen
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- printInt r0(i16) --
        movsx rcx, cx
        test   rcx, rcx
        jns    .x1
        neg    rcx
        push   rcx
          mov    cl, '-'
          call   tsfbi_emit
        pop    rcx
.x1:
        sub  rsp, 8
          call tsfbi_printUint
        add  rsp, 8
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
            js   .x2
            jnz  .x3
            dec  al
.x2:
            mov  rbx, rax
            shl  rbx, 8
            call [_getch]
            or   rax, rbx
.x3:
            mov  rcx, rax
          ; add rsp, 20h
        mov rsp, rdi
        ret

; string constants
section '.data' data readable
true_string db 'true'
false_string db 'false'
        string_0 db 'hello world', 0x00

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
