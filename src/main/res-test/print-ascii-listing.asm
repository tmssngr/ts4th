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


        ; -- proc printNibble --
tsf_printNibble:
        ; -- pop 0 (u8) --
        mov cl, [r15]
        add r15, 1
        ; -- literal r1, #15 --
        mov al, 15
        ; -- and r0, r1 (u8) --
        and cl, al
        ; -- literal r1, #48 --
        mov al, 48
        ; -- add r0, r1 (u8) --
        add cl, al
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- literal r1, #57 --
        mov al, 57
        ; -- gt r0, r1 (u8) --
        cmp   cl, al
        mov   cx, 0
        mov   ax, 1
        cmova cx, ax
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z endif_1 --
        jz tsf_endif_1
        ; -- literal r0, #65 --
        mov cl, 65
        ; -- literal r1, #57 --
        mov al, 57
        ; -- sub r0, r1 (u8) --
        sub cl, al
        ; -- literal r1, #1 --
        mov al, 1
        ; -- sub r0, r1 (u8) --
        sub cl, al
        ; -- mov 1, 0 (u8) --
        mov al, cl
        ; -- pop 0 (u8) --
        mov cl, [r15]
        add r15, 1
        ; -- add r0, r1 (u8) --
        add cl, al
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
tsf_endif_1:
        ; -- pop 0 (u8) --
        mov cl, [r15]
        add r15, 1
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- ret --
        ret

        ; -- proc printHex8 --
tsf_printHex8:
        ; -- pop 0 (u8) --
        mov cl, [r15]
        add r15, 1
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- mov 1, 0 (u8) --
        mov al, cl
        ; -- literal r0, #4 --
        mov cl, 4
        ; -- shr r1, r0 (u8) --
        shr al, cl
        ; -- push 1 (u8) --
        sub r15, 1
        mov [r15], al
        ; -- printNibble --
        call tsf_printNibble
        ; -- printNibble --
        call tsf_printNibble
        ; -- ret --
        ret

        ; -- proc main --
tsf_main:
        ; -- literal r0, #0 --
        mov cl, 0
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- literal r0, #16 --
        mov cl, 16
        ; -- push var r0 (u8) --
        push cx
        ; -- pop 0 (u8) --
        mov cl, [r15]
        add r15, 1
        ; -- push var r0 (u8) --
        push cx
        ; -- literal r0, #32 --
        mov cl, 32
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- literal r0, #120 --
        mov cl, 120
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
tsf_while_2:
        ; -- read var r0, [<empty> (u8)] --
        mov cx, [rsp+0]
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- read var r0, [u8 (u8)] --
        mov cx, [rsp+2]
        ; -- mov 1, 0 (u8) --
        mov al, cl
        ; -- pop 0 (u8) --
        mov cl, [r15]
        add r15, 1
        ; -- lt r0, r1 (u8) --
        cmp   cl, al
        mov   cx, 0
        mov   ax, 1
        cmovb cx, ax
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z endwhile_2 --
        jz tsf_endwhile_2
        ; -- read var r0, [<empty> (u8)] --
        mov cx, [rsp+0]
        ; -- literal r1, #7 --
        mov al, 7
        ; -- and r0, r1 (u8) --
        and cl, al
        ; -- literal r1, #0 --
        mov al, 0
        ; -- eq r0, r1 (u8) --
        cmp   cl, al
        mov   cx, 0
        mov   ax, 1
        cmove cx, ax
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z endif_3 --
        jz tsf_endif_3
        ; -- literal r0, #32 --
        mov cl, 32
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
tsf_endif_3:
        ; -- read var r0, [<empty> (u8)] --
        mov cx, [rsp+0]
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- printNibble --
        call tsf_printNibble
        ; -- read var r0, [<empty> (u8)] --
        mov cx, [rsp+0]
        ; -- literal r1, #1 --
        mov al, 1
        ; -- add r0, r1 (u8) --
        add cl, al
        ; -- write var [<empty> (u8)], 0 --
        mov [rsp+0], cx
        ; -- jump while_2 --
        jmp tsf_while_2
tsf_endwhile_2:
        ; -- literal r0, #10 --
        mov cl, 10
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- drop vars u8, u8 --
        add rsp, 4
        ; -- literal r0, #32 --
        mov cl, 32
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- literal r0, #128 --
        mov cl, 128
        ; -- push var r0 (u8) --
        push cx
        ; -- pop 0 (u8) --
        mov cl, [r15]
        add r15, 1
        ; -- push var r0 (u8) --
        push cx
tsf_while_4:
        ; -- read var r0, [<empty> (u8)] --
        mov cx, [rsp+0]
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- read var r0, [u8 (u8)] --
        mov cx, [rsp+2]
        ; -- mov 1, 0 (u8) --
        mov al, cl
        ; -- pop 0 (u8) --
        mov cl, [r15]
        add r15, 1
        ; -- lt r0, r1 (u8) --
        cmp   cl, al
        mov   cx, 0
        mov   ax, 1
        cmovb cx, ax
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z endwhile_4 --
        jz tsf_endwhile_4
        ; -- read var r0, [<empty> (u8)] --
        mov cx, [rsp+0]
        ; -- literal r1, #15 --
        mov al, 15
        ; -- and r0, r1 (u8) --
        and cl, al
        ; -- literal r1, #0 --
        mov al, 0
        ; -- eq r0, r1 (u8) --
        cmp   cl, al
        mov   cx, 0
        mov   ax, 1
        cmove cx, ax
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z endif_5 --
        jz tsf_endif_5
        ; -- read var r0, [<empty> (u8)] --
        mov cx, [rsp+0]
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- printHex8 --
        call tsf_printHex8
tsf_endif_5:
        ; -- read var r0, [<empty> (u8)] --
        mov cx, [rsp+0]
        ; -- literal r1, #7 --
        mov al, 7
        ; -- and r0, r1 (u8) --
        and cl, al
        ; -- literal r1, #0 --
        mov al, 0
        ; -- eq r0, r1 (u8) --
        cmp   cl, al
        mov   cx, 0
        mov   ax, 1
        cmove cx, ax
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z endif_6 --
        jz tsf_endif_6
        ; -- literal r0, #32 --
        mov cl, 32
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
tsf_endif_6:
        ; -- read var r0, [<empty> (u8)] --
        mov cx, [rsp+0]
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- read var r0, [<empty> (u8)] --
        mov cx, [rsp+0]
        ; -- literal r1, #15 --
        mov al, 15
        ; -- and r0, r1 (u8) --
        and cl, al
        ; -- literal r1, #15 --
        mov al, 15
        ; -- eq r0, r1 (u8) --
        cmp   cl, al
        mov   cx, 0
        mov   ax, 1
        cmove cx, ax
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z endif_7 --
        jz tsf_endif_7
        ; -- literal r0, #10 --
        mov cl, 10
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
tsf_endif_7:
        ; -- read var r0, [<empty> (u8)] --
        mov cx, [rsp+0]
        ; -- literal r1, #1 --
        mov al, 1
        ; -- add r0, r1 (u8) --
        add cl, al
        ; -- write var [<empty> (u8)], 0 --
        mov [rsp+0], cx
        ; -- jump while_4 --
        jmp tsf_while_4
tsf_endwhile_4:
        ; -- drop vars u8, u8 --
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
