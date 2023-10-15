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
        ; -- push 0 (ptr) --
        sub r15, 8
        mov [r15], rcx
        ; -- mov 1, 0 (ptr) --
        mov rax, rcx
        ; -- literal r0, #49 --
        mov cl, 49
        ; -- store @1, 0 (u8) --
        mov byte [rax], cl
        ; -- pop 0 (ptr) --
        mov rcx, [r15]
        add r15, 8
        ; -- literal r1, #1 --
        mov ax, 1
        ; -- cast 1, (i16 -> ptr) --
        movsx rax, ax
        ; -- add r0, r1 (ptr) --
        add rcx, rax
        ; -- push 0 (ptr) --
        sub r15, 8
        mov [r15], rcx
        ; -- mov 1, 0 (ptr) --
        mov rax, rcx
        ; -- literal r0, #48 --
        mov cl, 48
        ; -- store @1, 0 (u8) --
        mov byte [rax], cl
        ; -- pop 0 (ptr) --
        mov rcx, [r15]
        add r15, 8
        ; -- literal r1, #1 --
        mov ax, 1
        ; -- cast 1, (i16 -> ptr) --
        movsx rax, ax
        ; -- add r0, r1 (ptr) --
        add rcx, rax
        ; -- push 0 (ptr) --
        sub r15, 8
        mov [r15], rcx
        ; -- mov 1, 0 (ptr) --
        mov rax, rcx
        ; -- literal r0, #50 --
        mov cl, 50
        ; -- store @1, 0 (u8) --
        mov byte [rax], cl
        ; -- pop 0 (ptr) --
        mov rcx, [r15]
        add r15, 8
        ; -- literal r1, #1 --
        mov ax, 1
        ; -- cast 1, (i16 -> ptr) --
        movsx rax, ax
        ; -- add r0, r1 (ptr) --
        add rcx, rax
        ; -- push 0 (ptr) --
        sub r15, 8
        mov [r15], rcx
        ; -- mov 1, 0 (ptr) --
        mov rax, rcx
        ; -- literal r0, #52 --
        mov cl, 52
        ; -- store @1, 0 (u8) --
        mov byte [rax], cl
        ; -- pop 0 (ptr) --
        mov rcx, [r15]
        add r15, 8
        ; -- literal r1, #1 --
        mov ax, 1
        ; -- cast 1, (i16 -> ptr) --
        movsx rax, ax
        ; -- add r0, r1 (ptr) --
        add rcx, rax
        ; -- push 0 (ptr) --
        sub r15, 8
        mov [r15], rcx
        ; -- mov 1, 0 (ptr) --
        mov rax, rcx
        ; -- literal r0, #10 --
        mov cl, 10
        ; -- store @1, 0 (u8) --
        mov byte [rax], cl
        ; -- pop 0 (ptr) --
        mov rcx, [r15]
        add r15, 8
        ; -- mem --
        lea rcx, [mem]
        ; -- literal r1, #10 --
        mov ax, 10
        ; -- cast 1, (i16 -> ptr) --
        movsx rax, ax
        ; -- add r0, r1 (ptr) --
        add rcx, rax
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
        ; -- literal r1, #10 --
        mov ax, 10
        ; -- cast 1, (i16 -> ptr) --
        movsx rax, ax
        ; -- add r0, r1 (ptr) --
        add rcx, rax
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
        ; -- literal r1, #1 --
        mov ax, 1
        ; -- cast 1, (i16 -> ptr) --
        movsx rax, ax
        ; -- add r0, r1 (ptr) --
        add rcx, rax
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
