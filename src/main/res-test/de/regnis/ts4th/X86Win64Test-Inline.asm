format pe64 console
include 'win64ax.inc'

STD_IN_HANDLE = -10
STD_OUT_HANDLE = -11
STD_ERR_HANDLE = -12
STACK_SIZE = 1024 * 8

.code
start:
        mov r15, rsp
        sub rsp, STACK_SIZE
        sub rsp, 8
          call init
          call tsf_main
        add rsp, 8
        invoke ExitProcess, 0


        ; -- proc main --
tsf_main:
        ; -- literal r0, #1 --
        mov cx, 1
        ; -- literal r1, #2 --
        mov ax, 2
        ; -- lt r0, r1 (i16) --
        cmp   cx, ax
        mov   cx, 0
        mov   ax, 1
        cmovl cx, ax
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
        ; -- push 0 (i16) --
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
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
.i3:
        ; -- pop 0 (i16) --
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
        ; -- literal r1, #2 --
        mov ax, 2
        ; -- gt r0, r1 (i16) --
        cmp   cx, ax
        mov   cx, 0
        mov   ax, 1
        cmovg cx, ax
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
        ; -- push 0 (i16) --
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
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
.i6:
        ; -- pop 0 (i16) --
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
.end start
; string constants
section '.data' data readable
true_string db 'true'
false_string db 'false'
        string_0 db 'true', 0x0a
        string_1 db 'false', 0x0a

section '.data' data readable writeable
        hStdIn  rb 8
        hStdOut rb 8
        hStdErr rb 8

mem rb 640000
