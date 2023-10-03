format pe64 console
include 'win64ax.inc'

STD_OUTPUT_HANDLE = -11
STACK_SIZE = 1024 * 8

.code
start:
        mov r15, rsp
        sub rsp, STACK_SIZE
        sub rsp, 8
          call tsf_main
        add rsp, 8
        invoke ExitProcess, 0


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
        mov cx, 0
        ; -- literal r1, #false --
        mov ax, 0
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
        mov cx, 0
        ; -- literal r1, #true --
        mov ax, 1
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
        mov cx, 1
        ; -- literal r1, #false --
        mov ax, 0
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
        mov cx, 1
        ; -- literal r1, #true --
        mov ax, 1
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
        mov cx, 0
        ; -- literal r1, #false --
        mov ax, 0
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
        mov cx, 0
        ; -- literal r1, #true --
        mov ax, 1
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
        mov cx, 1
        ; -- literal r1, #false --
        mov ax, 0
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
        mov cx, 1
        ; -- literal r1, #true --
        mov ax, 1
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
        mov cx, 0
        ; -- literal r1, #false --
        mov ax, 0
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
        mov cx, 0
        ; -- literal r1, #true --
        mov ax, 1
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
        mov cx, 1
        ; -- literal r1, #false --
        mov ax, 0
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
        mov cx, 1
        ; -- literal r1, #true --
        mov ax, 1
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
        push    rcx
          push    rdx

            sub  rsp, 20h
              mov  rcx, STD_OUTPUT_HANDLE
              call [GetStdHandle]
              ; handle in rax, 0 if invalid
            add  rsp, 20h

          pop     r8
        pop     rdx

        mov     rcx, rax
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
        string_0 db 'and: '
        string_1 db 'or:  '
        string_2 db 'xor: '

section '.data' data readable writeable

mem rb 640000
