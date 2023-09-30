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


        ; -- proc strlen --
tsf_strlen:
        ; -- pop 0 (ptr) --
        mov rcx, [r15]
        add r15, 8
        ; -- literal r1, #0 --
        mov ax, 0
        ; -- push 1 (int) --
        sub r15, 2
        mov [r15], ax
        ; -- push 0 (ptr) --
        sub r15, 8
        mov [r15], rcx
tsf_while_1:
        ; -- pop 0 (ptr) --
        mov rcx, [r15]
        add r15, 8
        ; -- push 0 (ptr) --
        sub r15, 8
        mov [r15], rcx
        ; -- mov 1, 0 (ptr) --
        mov rax, rcx
        ; -- literal r0, #0 --
        mov cx, 0
        ; -- load 0 (1), @1 --
        mov cl, byte [rax]
        ; -- literal r1, #0 --
        mov ax, 0
        ; -- neq r0, r1 (int) --
        cmp    cx, ax
        mov    cx, 0
        mov    ax, 1
        cmovne rcx, rax
        ; -- boolTest r0, r0 (int) --
        test cl, cl
        ; -- jump z endwhile_1 --
        jz tsf_endwhile_1
        ; -- pop 1 (ptr) --
        mov rax, [r15]
        add r15, 8
        ; -- pop 0 (int) --
        mov cx, [r15]
        add r15, 2
        ; -- push 1 (ptr) --
        sub r15, 8
        mov [r15], rax
        ; -- literal r1, #1 --
        mov ax, 1
        ; -- add r0, r1 (int) --
        add cx, ax
        ; -- mov 1, 0 (int) --
        mov ax, cx
        ; -- pop 0 (ptr) --
        mov rcx, [r15]
        add r15, 8
        ; -- push 1 (int) --
        sub r15, 2
        mov [r15], ax
        ; -- literal r1, #1 --
        mov ax, 1
        ; -- cast 1, (int -> ptr) --
        movsx rax, ax
        ; -- add r0, r1 (ptr) --
        add rcx, rax
        ; -- push 0 (ptr) --
        sub r15, 8
        mov [r15], rcx
        ; -- jump while_1 --
        jmp tsf_while_1
tsf_endwhile_1:
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
        ; -- strlen --
        call tsf_strlen
        ; -- pop 0 (int) --
        mov cx, [r15]
        add r15, 2
        ; -- printInt r0(int) --
        movsx rcx, cx
        test   rcx, rcx
        jns    .1
        neg    rcx
        push   rcx
          mov    cl, '-'
          call   tsfbi_emit
        pop    rcx
.1:
        sub  rsp, 8
          call tsfbi_printUint
          mov  cl, ' '
          call tsfbi_emit
        add  rsp, 8
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
        mov    eax, 20h
        mov    dword [rsp+20h], eax

        ; do {
.print:
        ; pos--;
        mov    eax, dword [rsp+20h]
        sub    eax, 1
        mov    dword [rsp+20h], eax

        ; int remainder = x mod 10;
        ; x = x / 10;
        mov    eax, dword [rsp+24h]
        mov    ecx, 10
        xor    edx, edx
        div    ecx
        mov    dword [rsp+24h], eax

        ; int digit = remainder + '0';
        add    dl, '0'

        ; buf[pos] = digit;
        mov    eax, dword [rsp+20h]
        movsxd rax, eax
        lea    rcx, qword [rsp]
        add    rcx, rax
        mov    byte [rcx], dl

        ; } while (x > 0);
        mov    eax, dword [rsp+24h]
        cmp    eax, 0
        ja     .print

        ; rcx = &buf[pos]

        ; rdx = sizeof(buf) - pos
        mov    eax, dword [rsp+20h]
        movsxd rax, eax
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
        string_0 db 'hello world', 0x00

section '.data' data readable writeable

mem rb 640000
