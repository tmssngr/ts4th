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
        ; -- literal r0, #1 --
        mov cx, 1
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- literal r0, #2 --
        mov cx, 2
        ; -- push var r0 (i16) --
        push cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push var r0 (i16) --
        push cx
.i1:
        ; -- read var r0, [<empty> (i16)] --
        mov cx, [rsp+0]
        ; -- literal r1, #100 --
        mov ax, 100
        ; -- lt r0, r1 (i16) --
        cmp   cx, ax
        mov   cx, 0
        mov   ax, 1
        cmovl cx, ax
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i3 --
        jz .i3
        ; -- read var r0, [<empty> (i16)] --
        mov cx, [rsp+0]
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
        ; -- read var r0, [i16 (i16)] --
        mov cx, [rsp+2]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- read var r0, [<empty> (i16)] --
        mov cx, [rsp+0]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- read var r0, [i16 (i16)] --
        mov cx, [rsp+2]
        ; -- mov 1, 0 (i16) --
        mov ax, cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- add r0, r1 (i16) --
        add cx, ax
        ; -- write var [i16 (i16)], 0 --
        mov [rsp+2], cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- write var [<empty> (i16)], 0 --
        mov [rsp+0], cx
        ; -- jump .i1 --
        jmp .i1
.i3:
        ; -- drop vars i16, i16 --
        add rsp, 4
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

section '.data' data readable writeable

mem rb 640000