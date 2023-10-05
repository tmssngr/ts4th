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


        ; -- proc initRandom --
tsf_initRandom:
        ; -- var r1, @__random__ --
        lea rax, [var_0]
        ; -- pop 0 (u32) --
        mov ecx, [r15]
        add r15, 4
        ; -- store @1, 0 (u32) --
        mov dword [rax], ecx
        ; -- ret --
        ret

        ; -- proc random --
tsf_random:
        ; -- literal r0, #48271 --
        mov ecx, 48271
        ; -- push var r0 (u32) --
        push rcx
        ; -- var r1, @__random__ --
        lea rax, [var_0]
        ; -- load 0 (u32), @1 --
        mov ecx, dword [rax]
        ; -- push 0 (u32) --
        sub r15, 4
        mov [r15], ecx
        ; -- literal r1, #524287 --
        mov eax, 524287
        ; -- and r0, r1 (u32) --
        and ecx, eax
        ; -- push 0 (u32) --
        sub r15, 4
        mov [r15], ecx
        ; -- read var r0, [<empty> (u32)] --
        mov rcx, [rsp+0]
        ; -- mov 1, 0 (u32) --
        mov eax, ecx
        ; -- pop 0 (u32) --
        mov ecx, [r15]
        add r15, 4
        ; -- imul r0, r1 (u32) --
        imul ecx, eax
        ; -- mov 1, 0 (u32) --
        mov eax, ecx
        ; -- pop 0 (u32) --
        mov ecx, [r15]
        add r15, 4
        ; -- push 1 (u32) --
        sub r15, 4
        mov [r15], eax
        ; -- mov 1, 0 (u32) --
        mov eax, ecx
        ; -- literal r0, #15 --
        mov ecx, 15
        ; -- shr r1, r0 (u32) --
        shr eax, cl
        ; -- push 1 (u32) --
        sub r15, 4
        mov [r15], eax
        ; -- read var r0, [<empty> (u32)] --
        mov rcx, [rsp+0]
        ; -- mov 1, 0 (u32) --
        mov eax, ecx
        ; -- pop 0 (u32) --
        mov ecx, [r15]
        add r15, 4
        ; -- imul r0, r1 (u32) --
        imul ecx, eax
        ; -- push 0 (u32) --
        sub r15, 4
        mov [r15], ecx
        ; -- literal r1, #65535 --
        mov eax, 65535
        ; -- and r0, r1 (u32) --
        and ecx, eax
        ; -- mov 1, 0 (u32) --
        mov eax, ecx
        ; -- literal r0, #15 --
        mov ecx, 15
        ; -- shl r1, r0 (u32) --
        shl eax, cl
        ; -- pop 0 (u32) --
        mov ecx, [r15]
        add r15, 4
        ; -- push 1 (u32) --
        sub r15, 4
        mov [r15], eax
        ; -- mov 1, 0 (u32) --
        mov eax, ecx
        ; -- literal r0, #16 --
        mov ecx, 16
        ; -- shr r1, r0 (u32) --
        shr eax, cl
        ; -- pop 0 (u32) --
        mov ecx, [r15]
        add r15, 4
        ; -- add r0, r1 (u32) --
        add ecx, eax
        ; -- mov 1, 0 (u32) --
        mov eax, ecx
        ; -- pop 0 (u32) --
        mov ecx, [r15]
        add r15, 4
        ; -- add r0, r1 (u32) --
        add ecx, eax
        ; -- push 0 (u32) --
        sub r15, 4
        mov [r15], ecx
        ; -- literal r1, #2147483647 --
        mov eax, 2147483647
        ; -- and r0, r1 (u32) --
        and ecx, eax
        ; -- mov 1, 0 (u32) --
        mov eax, ecx
        ; -- pop 0 (u32) --
        mov ecx, [r15]
        add r15, 4
        ; -- push 1 (u32) --
        sub r15, 4
        mov [r15], eax
        ; -- mov 1, 0 (u32) --
        mov eax, ecx
        ; -- literal r0, #31 --
        mov ecx, 31
        ; -- shr r1, r0 (u32) --
        shr eax, cl
        ; -- pop 0 (u32) --
        mov ecx, [r15]
        add r15, 4
        ; -- add r0, r1 (u32) --
        add ecx, eax
        ; -- push 0 (u32) --
        sub r15, 4
        mov [r15], ecx
        ; -- push 0 (u32) --
        sub r15, 4
        mov [r15], ecx
        ; -- var r1, @__random__ --
        lea rax, [var_0]
        ; -- pop 0 (u32) --
        mov ecx, [r15]
        add r15, 4
        ; -- store @1, 0 (u32) --
        mov dword [rax], ecx
        ; -- drop vars u32 --
        add rsp, 8
        ; -- ret --
        ret

        ; -- proc randomU8 --
tsf_randomU8:
        ; -- random --
        call tsf_random
        ; -- pop 0 (u32) --
        mov ecx, [r15]
        add r15, 4
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- ret --
        ret

        ; -- proc main --
tsf_main:
        ; -- literal r0, #7439742 --
        mov ecx, 7439742
        ; -- push 0 (u32) --
        sub r15, 4
        mov [r15], ecx
        ; -- initRandom --
        call tsf_initRandom
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
        ; -- literal r1, #50 --
        mov ax, 50
        ; -- lt r0, r1 (i16) --
        cmp   cx, ax
        mov   cx, 0
        mov   ax, 1
        cmovl cx, ax
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i3 --
        jz .i3
        ; -- randomU8 --
        call tsf_randomU8
        ; -- pop 0 (u8) --
        mov cl, [r15]
        add r15, 1
        ; -- printInt r0(u8) --
        movzx rcx, cl
        sub  rsp, 8
          call tsfbi_printUint
          mov  cl, ' '
          call tsfbi_emit
        add  rsp, 8
        ; -- literal r0, #10 --
        mov cl, 10
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
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

section '.data' data readable writeable
; __random__
var_0 rb 4

mem rb 640000
