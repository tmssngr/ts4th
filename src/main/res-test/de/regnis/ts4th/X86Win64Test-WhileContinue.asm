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
        ; -- literal r0, #0 --
        mov cx, 0
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
.i1:
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- literal r1, #10 --
        mov ax, 10
        ; -- lt r0, r1 (i16) --
        cmp   cx, ax
        mov   cx, 0
        mov   ax, 1
        cmovl cx, ax
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i3 --
        jz .i3
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- literal r1, #3 --
        mov ax, 3
        ; -- eq r0, r1 (i16) --
        cmp   cx, ax
        mov   cx, 0
        mov   ax, 1
        cmove cx, ax
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i5 --
        jz .i5
        ; -- literal r1, "0 --
        lea rax, [string_0]
        ; -- literal r0, #15 --
        mov cx, 15
        ; -- printString r1 (0) --
        movsx rdx, cx
        mov rcx, rax
        sub rsp, 8
          call tsfbi_printString
        add rsp, 8
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- literal r1, #2 --
        mov ax, 2
        ; -- add r0, r1 (i16) --
        add cx, ax
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- jump .i1 --
        jmp .i1
.i5:
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
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
          mov  cl, ' '
          call tsfbi_emit
        add  rsp, 8
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- literal r1, #1 --
        mov ax, 1
        ; -- add r0, r1 (i16) --
        add cx, ax
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- jump .i1 --
        jmp .i1
.i3:
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
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

; string constants
section '.data' data readable
true_string db 'true'
false_string db 'false'
        string_0 db 'three and four '

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
