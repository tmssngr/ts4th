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
        ; -- literal r0, #1024 --
        mov cx, 1024
        ; -- push 0 (2) --
        sub r15, 2
        mov [r15], cx
        ; -- literal r0, #768 --
        mov cx, 768
        mov ax, cx
        ; -- pop 0 (2) --
        mov cx, [r15]
        add r15, 2
        ; -- push 0 (2) --
        sub r15, 2
        mov [r15], cx
        ; -- push 1 (2) --
        sub r15, 2
        mov [r15], ax
        ; -- push 0 (2) --
        sub r15, 2
        mov [r15], cx
        ; -- push 1 (2) --
        sub r15, 2
        mov [r15], ax
        ; -- gcd --
        call tsf_gcd
        ; -- pop 2 (2) --
        mov bx, [r15]
        add r15, 2
        ; -- pop 1 (2) --
        mov ax, [r15]
        add r15, 2
        ; -- pop 0 (2) --
        mov cx, [r15]
        add r15, 2
        ; -- push 1 (2) --
        sub r15, 2
        mov [r15], ax
        ; -- push 2 (2) --
        sub r15, 2
        mov [r15], bx
        mov ax, cx
        ; -- pop 0 (2) --
        mov cx, [r15]
        add r15, 2
        ; -- push 0 (2) --
        sub r15, 2
        mov [r15], cx
        ; -- push 1 (2) --
        sub r15, 2
        mov [r15], ax
        mov ax, cx
        ; -- pop 0 (2) --
        mov cx, [r15]
        add r15, 2
        ; -- idiv 0 1 --
        mov dx, ax
        xor eax, eax
        mov ax, cx
        xor ecx, ecx
        mov cx, dx
        xor edx, edx
        idiv ecx
        mov ecx, eax
        ; -- printInt r0(2) --
        movsx rcx, cx
        test   rcx, rcx
        jns    .1
        neg    rcx
        push   rcx
          mov    cl, '-'
          call   tsfbi_printChar
        pop    rcx
.1:
        sub  rsp, 8
          call tsfbi_printUint
          mov  cl, ' '
          call tsfbi_printChar
        add  rsp, 8
        ; -- pop 1 (2) --
        mov ax, [r15]
        add r15, 2
        ; -- pop 0 (2) --
        mov cx, [r15]
        add r15, 2
        ; -- idiv 0 1 --
        mov dx, ax
        xor eax, eax
        mov ax, cx
        xor ecx, ecx
        mov cx, dx
        xor edx, edx
        idiv ecx
        mov ecx, eax
        ; -- printInt r0(2) --
        movsx rcx, cx
        test   rcx, rcx
        jns    .2
        neg    rcx
        push   rcx
          mov    cl, '-'
          call   tsfbi_printChar
        pop    rcx
.2:
        sub  rsp, 8
          call tsfbi_printUint
          mov  cl, ' '
          call tsfbi_printChar
        add  rsp, 8
        ; -- ret --
        ret

        ; -- proc gcd --
tsf_gcd:
tsf_while_1:
        ; -- pop 1 (2) --
        mov ax, [r15]
        add r15, 2
        ; -- pop 0 (2) --
        mov cx, [r15]
        add r15, 2
        ; -- push 0 (2) --
        sub r15, 2
        mov [r15], cx
        ; -- push 1 (2) --
        sub r15, 2
        mov [r15], ax
        ; -- lt 0 1 --
        cmp cx, ax
        mov cx, 0
        mov ax, 1
        cmovl rcx, rax
        ; -- boolTest 0 0 --
        test cl, cl
        ; -- jump z else_2 --
        jz tsf_else_2
        ; -- pop 1 (2) --
        mov ax, [r15]
        add r15, 2
        ; -- pop 0 (2) --
        mov cx, [r15]
        add r15, 2
        ; -- push 0 (2) --
        sub r15, 2
        mov [r15], cx
        ; -- push 1 (2) --
        sub r15, 2
        mov [r15], ax
        mov ax, cx
        ; -- pop 0 (2) --
        mov cx, [r15]
        add r15, 2
        ; -- sub 0 1 --
        sub cx, ax
        ; -- push 0 (2) --
        sub r15, 2
        mov [r15], cx
        ; -- jump while_1 --
        jmp tsf_while_1
tsf_else_2:
        ; -- pop 1 (2) --
        mov ax, [r15]
        add r15, 2
        ; -- pop 0 (2) --
        mov cx, [r15]
        add r15, 2
        ; -- push 0 (2) --
        sub r15, 2
        mov [r15], cx
        ; -- push 1 (2) --
        sub r15, 2
        mov [r15], ax
        ; -- gt 0 1 --
        cmp cx, ax
        mov cx, 0
        mov ax, 1
        cmovg rcx, rax
        ; -- boolTest 0 0 --
        test cl, cl
        ; -- jump z endwhile_1 --
        jz tsf_endwhile_1
        ; -- pop 1 (2) --
        mov ax, [r15]
        add r15, 2
        ; -- pop 0 (2) --
        mov cx, [r15]
        add r15, 2
        ; -- push 1 (2) --
        sub r15, 2
        mov [r15], ax
        mov ax, cx
        ; -- pop 0 (2) --
        mov cx, [r15]
        add r15, 2
        ; -- push 0 (2) --
        sub r15, 2
        mov [r15], cx
        ; -- push 1 (2) --
        sub r15, 2
        mov [r15], ax
        mov ax, cx
        ; -- pop 0 (2) --
        mov cx, [r15]
        add r15, 2
        ; -- sub 0 1 --
        sub cx, ax
        ; -- push 0 (2) --
        sub r15, 2
        mov [r15], cx
        ; -- jump while_1 --
        jmp tsf_while_1
tsf_endwhile_1:
        ; -- pop 0 (2) --
        mov cx, [r15]
        add r15, 2
        ; -- ret --
        ret

tsfbi_printChar:
        push rcx ; = sub rsp, 8
          mov rcx, rsp
          mov rdx, 1
          call tsfbi_printString
        pop rcx
        ret

tsfbi_printString:
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
        add     rsp, 8
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

section '.data' data readable writeable

mem rb 640000
