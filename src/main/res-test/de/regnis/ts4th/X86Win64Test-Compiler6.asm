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
        ; -- mem --
        lea rcx, [mem]
        ; -- push 0 (ptr) --
        sub r15, 8
        mov [r15], rcx
        ; -- mov 1, 0 (ptr) --
        mov rax, rcx
        ; -- literal r0, #49 --
        mov cx, 49
        ; -- store @1, 0 (1) --
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
        mov cx, 48
        ; -- store @1, 0 (1) --
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
        mov cx, 50
        ; -- store @1, 0 (1) --
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
        mov cx, 52
        ; -- store @1, 0 (1) --
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
        mov cx, 10
        ; -- store @1, 0 (1) --
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
        mov cx, 104
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- appendChar --
        call tsf_appendChar
        ; -- literal r0, #101 --
        mov cx, 101
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- appendChar --
        call tsf_appendChar
        ; -- literal r0, #108 --
        mov cx, 108
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- appendChar --
        call tsf_appendChar
        ; -- literal r0, #108 --
        mov cx, 108
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- appendChar --
        call tsf_appendChar
        ; -- literal r0, #111 --
        mov cx, 111
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- appendChar --
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
        ; -- pop 1 (i16) --
        mov ax, [r15]
        add r15, 2
        ; -- pop 0 (ptr) --
        mov rcx, [r15]
        add r15, 8
        ; -- push 0 (ptr) --
        sub r15, 8
        mov [r15], rcx
        ; -- push 1 (i16) --
        sub r15, 2
        mov [r15], ax
        ; -- mov 1, 0 (ptr) --
        mov rax, rcx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- store @1, 0 (1) --
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

section '.data' data readable writeable

mem rb 640000
