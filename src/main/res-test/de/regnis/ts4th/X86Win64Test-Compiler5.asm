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
        ; -- mem --
        lea rcx, [mem]
        ; -- mov 1, 0 (ptr) --
        mov rax, rcx
        ; -- literal r0, #49 --
        mov cl, 49
        ; -- store @1, 0 (u8) --
        mov byte [rax], cl
        ; -- mem --
        lea rcx, [mem]
        ; -- literal r1, #1 --
        mov ax, 1
        ; -- cast 1, (i16 -> ptr) --
        movsx rax, ax
        ; -- add r0, r1 (ptr) --
        add rcx, rax
        ; -- mov 1, 0 (ptr) --
        mov rax, rcx
        ; -- literal r0, #48 --
        mov cl, 48
        ; -- store @1, 0 (u8) --
        mov byte [rax], cl
        ; -- mem --
        lea rcx, [mem]
        ; -- literal r1, #2 --
        mov ax, 2
        ; -- cast 1, (i16 -> ptr) --
        movsx rax, ax
        ; -- add r0, r1 (ptr) --
        add rcx, rax
        ; -- mov 1, 0 (ptr) --
        mov rax, rcx
        ; -- literal r0, #50 --
        mov cl, 50
        ; -- store @1, 0 (u8) --
        mov byte [rax], cl
        ; -- mem --
        lea rcx, [mem]
        ; -- literal r1, #3 --
        mov ax, 3
        ; -- cast 1, (i16 -> ptr) --
        movsx rax, ax
        ; -- add r0, r1 (ptr) --
        add rcx, rax
        ; -- mov 1, 0 (ptr) --
        mov rax, rcx
        ; -- literal r0, #52 --
        mov cl, 52
        ; -- store @1, 0 (u8) --
        mov byte [rax], cl
        ; -- mem --
        lea rcx, [mem]
        ; -- mov 1, 0 (ptr) --
        mov rax, rcx
        ; -- literal r0, #4 --
        mov cx, 4
        ; -- printString r1 (0) --
        movsx rdx, cx
        mov rcx, rax
        sub rsp, 8
          call tsfbi_printString
        add rsp, 8
        ; -- mem --
        lea rcx, [mem]
        ; -- push 0 (ptr) --
        sub r15, 8
        mov [r15], rcx
        ; -- literal r0, #104 --
        mov cl, 104
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- call appendChar --
        call tsf_appendChar
        ; -- literal r0, #101 --
        mov cl, 101
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- call appendChar --
        call tsf_appendChar
        ; -- literal r0, #108 --
        mov cl, 108
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- call appendChar --
        call tsf_appendChar
        ; -- literal r0, #108 --
        mov cl, 108
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- call appendChar --
        call tsf_appendChar
        ; -- literal r0, #111 --
        mov cl, 111
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- call appendChar --
        call tsf_appendChar
        ; -- pop 0 (ptr) --
        mov rcx, [r15]
        add r15, 8
        ; -- mem --
        lea rcx, [mem]
        ; -- mov 1, 0 (ptr) --
        mov rax, rcx
        ; -- literal r0, #5 --
        mov cx, 5
        ; -- printString r1 (0) --
        movsx rdx, cx
        mov rcx, rax
        sub rsp, 8
          call tsfbi_printString
        add rsp, 8
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
        ; -- ret --
        ret

        ; -- proc appendChar --
tsf_appendChar:
        ; -- pop 1 (u8) --
        mov al, [r15]
        add r15, 1
        ; -- pop 0 (ptr) --
        mov rcx, [r15]
        add r15, 8
        ; -- push 0 (ptr) --
        sub r15, 8
        mov [r15], rcx
        ; -- push 1 (u8) --
        sub r15, 1
        mov [r15], al
        ; -- mov 1, 0 (ptr) --
        mov rax, rcx
        ; -- pop 0 (u8) --
        mov cl, [r15]
        add r15, 1
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- store @1, 0 (u8) --
        mov byte [rax], cl
        ; -- pop 0 (u8) --
        mov cl, [r15]
        add r15, 1
        ; -- pop 0 (ptr) --
        mov rcx, [r15]
        add r15, 8
        ; -- add r0, 1 (ptr) --
        add rcx, 1
        ; -- push 0 (ptr) --
        sub r15, 8
        mov [r15], rcx
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
        string_0 db 0x0a, 'hello "world"', 0x0a

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
