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


        ; -- proc fill --
tsf_fill:
        ; -- pop 0 (u8) --
        mov cl, [r15]
        add r15, 1
        ; -- push var r0 (u8) --
        push cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push var r0 (i16) --
        push cx
        ; -- pop 0 (ptr) --
        mov rcx, [r15]
        add r15, 8
        ; -- push var r0 (ptr) --
        push rcx
tsf_while_1:
        ; -- read var r0, [ptr (i16)] --
        mov cx, [rsp+8]
        ; -- literal r1, #0 --
        mov ax, 0
        ; -- gt r0, r1 (i16) --
        cmp   cx, ax
        mov   cx, 0
        mov   ax, 1
        cmovg cx, ax
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z endwhile_1 --
        jz tsf_endwhile_1
        ; -- read var r0, [<empty> (ptr)] --
        mov rcx, [rsp+0]
        ; -- push 0 (ptr) --
        sub r15, 8
        mov [r15], rcx
        ; -- read var r0, [ptr, i16 (u8)] --
        mov cx, [rsp+10]
        ; -- pop 1 (ptr) --
        mov rax, [r15]
        add r15, 8
        ; -- store @1, 0 (u8) --
        mov byte [rax], cl
        ; -- read var r0, [<empty> (ptr)] --
        mov rcx, [rsp+0]
        ; -- literal r1, #1 --
        mov ax, 1
        ; -- cast 1, (i16 -> ptr) --
        movsx rax, ax
        ; -- add r0, r1 (ptr) --
        add rcx, rax
        ; -- write var [<empty> (ptr)], 0 --
        mov [rsp+0], rcx
        ; -- read var r0, [ptr (i16)] --
        mov cx, [rsp+8]
        ; -- literal r1, #1 --
        mov ax, 1
        ; -- sub r0, r1 (i16) --
        sub cx, ax
        ; -- write var [ptr (i16)], 0 --
        mov [rsp+8], cx
        ; -- jump while_1 --
        jmp tsf_while_1
tsf_endwhile_1:
        ; -- drop vars ptr, i16, u8 --
        add rsp, 12
        ; -- ret --
        ret

        ; -- proc printBoard --
tsf_printBoard:
        ; -- literal r0, #124 --
        mov cl, 124
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- literal r0, #0 --
        mov cx, 0
        ; -- push var r0 (i16) --
        push cx
tsf_while_2:
        ; -- read var r0, [<empty> (i16)] --
        mov cx, [rsp+0]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- literal r0, #30 --
        mov cx, 30
        ; -- literal r1, #2 --
        mov ax, 2
        ; -- sub r0, r1 (i16) --
        sub cx, ax
        ; -- mov 1, 0 (i16) --
        mov ax, cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- lt r0, r1 (i16) --
        cmp   cx, ax
        mov   cx, 0
        mov   ax, 1
        cmovl cx, ax
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z endwhile_2 --
        jz tsf_endwhile_2
        ; -- literal r0, "0 --
        lea rcx, [string_0]
        ; -- push 0 (ptr) --
        sub r15, 8
        mov [r15], rcx
        ; -- literal r0, #2 --
        mov cx, 2
        ; -- var r0, @board --
        lea rcx, [var_0]
        ; -- push 0 (ptr) --
        sub r15, 8
        mov [r15], rcx
        ; -- read var r0, [<empty> (i16)] --
        mov cx, [rsp+0]
        ; -- mov 1, 0 (i16) --
        mov ax, cx
        ; -- pop 0 (ptr) --
        mov rcx, [r15]
        add r15, 8
        ; -- cast 1, (i16 -> ptr) --
        movsx rax, ax
        ; -- add r0, r1 (ptr) --
        add rcx, rax
        ; -- mov 1, 0 (ptr) --
        mov rax, rcx
        ; -- load 0 (u8), @1 --
        mov cl, byte [rax]
        ; -- cast 0, (u8 -> i16) --
        movsx cx, cl
        ; -- mov 1, 0 (i16) --
        mov ax, cx
        ; -- pop 0 (ptr) --
        mov rcx, [r15]
        add r15, 8
        ; -- cast 1, (i16 -> ptr) --
        movsx rax, ax
        ; -- add r0, r1 (ptr) --
        add rcx, rax
        ; -- mov 1, 0 (ptr) --
        mov rax, rcx
        ; -- load 0 (u8), @1 --
        mov cl, byte [rax]
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- read var r0, [<empty> (i16)] --
        mov cx, [rsp+0]
        ; -- literal r1, #1 --
        mov ax, 1
        ; -- add r0, r1 (i16) --
        add cx, ax
        ; -- write var [<empty> (i16)], 0 --
        mov [rsp+0], cx
        ; -- jump while_2 --
        jmp tsf_while_2
tsf_endwhile_2:
        ; -- drop vars i16 --
        add rsp, 2
        ; -- literal r0, #124 --
        mov cl, 124
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- literal r0, #10 --
        mov cl, 10
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- ret --
        ret

        ; -- proc main --
tsf_main:
        ; -- var r0, @board --
        lea rcx, [var_0]
        ; -- push 0 (ptr) --
        sub r15, 8
        mov [r15], rcx
        ; -- literal r0, #30 --
        mov cx, 30
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- literal r0, #0 --
        mov cl, 0
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- fill --
        call tsf_fill
        ; -- var r0, @board --
        lea rcx, [var_0]
        ; -- push 0 (ptr) --
        sub r15, 8
        mov [r15], rcx
        ; -- literal r0, #30 --
        mov cx, 30
        ; -- literal r1, #2 --
        mov ax, 2
        ; -- sub r0, r1 (i16) --
        sub cx, ax
        ; -- mov 1, 0 (i16) --
        mov ax, cx
        ; -- pop 0 (ptr) --
        mov rcx, [r15]
        add r15, 8
        ; -- cast 1, (i16 -> ptr) --
        movsx rax, ax
        ; -- add r0, r1 (ptr) --
        add rcx, rax
        ; -- mov 1, 0 (ptr) --
        mov rax, rcx
        ; -- literal r0, #1 --
        mov cl, 1
        ; -- store @1, 0 (u8) --
        mov byte [rax], cl
        ; -- literal r0, #0 --
        mov cx, 0
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
tsf_while_3:
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- literal r0, #30 --
        mov cx, 30
        ; -- literal r1, #2 --
        mov ax, 2
        ; -- sub r0, r1 (i16) --
        sub cx, ax
        ; -- mov 1, 0 (i16) --
        mov ax, cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- lt r0, r1 (i16) --
        cmp   cx, ax
        mov   cx, 0
        mov   ax, 1
        cmovl cx, ax
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z endwhile_3 --
        jz tsf_endwhile_3
        ; -- printBoard --
        call tsf_printBoard
        ; -- var r1, @board --
        lea rax, [var_0]
        ; -- load 0 (u8), @1 --
        mov cl, byte [rax]
        ; -- cast 0, (u8 -> i16) --
        movsx cx, cl
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- var r0, @board --
        lea rcx, [var_0]
        ; -- literal r1, #1 --
        mov ax, 1
        ; -- cast 1, (i16 -> ptr) --
        movsx rax, ax
        ; -- add r0, r1 (ptr) --
        add rcx, rax
        ; -- mov 1, 0 (ptr) --
        mov rax, rcx
        ; -- load 0 (u8), @1 --
        mov cl, byte [rax]
        ; -- cast 0, (u8 -> i16) --
        movsx cx, cl
        ; -- mov 1, 0 (i16) --
        mov ax, cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- or r0, r1 (i16) --
        or cx, ax
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- literal r0, #1 --
        mov cx, 1
        ; -- push var r0 (i16) --
        push cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push var r0 (i16) --
        push cx
tsf_while_4:
        ; -- read var r0, [i16 (i16)] --
        mov cx, [rsp+2]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- literal r0, #30 --
        mov cx, 30
        ; -- literal r1, #1 --
        mov ax, 1
        ; -- sub r0, r1 (i16) --
        sub cx, ax
        ; -- mov 1, 0 (i16) --
        mov ax, cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- lt r0, r1 (i16) --
        cmp   cx, ax
        mov   cx, 0
        mov   ax, 1
        cmovl cx, ax
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z endwhile_4 --
        jz tsf_endwhile_4
        ; -- read var r0, [<empty> (i16)] --
        mov cx, [rsp+0]
        ; -- mov 1, 0 (i16) --
        mov ax, cx
        ; -- literal r0, #1 --
        mov cx, 1
        ; -- shl r1, r0 (i16) --
        shl ax, cl
        ; -- mov 0, 1 (i16) --
        mov cx, ax
        ; -- literal r1, #7 --
        mov ax, 7
        ; -- and r0, r1 (i16) --
        and cx, ax
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- var r0, @board --
        lea rcx, [var_0]
        ; -- push 0 (ptr) --
        sub r15, 8
        mov [r15], rcx
        ; -- read var r0, [i16 (i16)] --
        mov cx, [rsp+2]
        ; -- literal r1, #1 --
        mov ax, 1
        ; -- add r0, r1 (i16) --
        add cx, ax
        ; -- mov 1, 0 (i16) --
        mov ax, cx
        ; -- pop 0 (ptr) --
        mov rcx, [r15]
        add r15, 8
        ; -- cast 1, (i16 -> ptr) --
        movsx rax, ax
        ; -- add r0, r1 (ptr) --
        add rcx, rax
        ; -- mov 1, 0 (ptr) --
        mov rax, rcx
        ; -- load 0 (u8), @1 --
        mov cl, byte [rax]
        ; -- cast 0, (u8 -> i16) --
        movsx cx, cl
        ; -- mov 1, 0 (i16) --
        mov ax, cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- or r0, r1 (i16) --
        or cx, ax
        ; -- write var [<empty> (i16)], 0 --
        mov [rsp+0], cx
        ; -- literal r0, #110 --
        mov cx, 110
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- read var r0, [<empty> (i16)] --
        mov cx, [rsp+0]
        ; -- pop 1 (i16) --
        mov ax, [r15]
        add r15, 2
        ; -- shr r1, r0 (i16) --
        shr ax, cl
        ; -- mov 0, 1 (i16) --
        mov cx, ax
        ; -- literal r1, #1 --
        mov ax, 1
        ; -- and r0, r1 (i16) --
        and cx, ax
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- var r0, @board --
        lea rcx, [var_0]
        ; -- push 0 (ptr) --
        sub r15, 8
        mov [r15], rcx
        ; -- read var r0, [i16 (i16)] --
        mov cx, [rsp+2]
        ; -- mov 1, 0 (i16) --
        mov ax, cx
        ; -- pop 0 (ptr) --
        mov rcx, [r15]
        add r15, 8
        ; -- cast 1, (i16 -> ptr) --
        movsx rax, ax
        ; -- add r0, r1 (ptr) --
        add rcx, rax
        ; -- mov 1, 0 (ptr) --
        mov rax, rcx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push 1 (ptr) --
        sub r15, 8
        mov [r15], rax
        ; -- pop 1 (ptr) --
        mov rax, [r15]
        add r15, 8
        ; -- store @1, 0 (u8) --
        mov byte [rax], cl
        ; -- read var r0, [i16 (i16)] --
        mov cx, [rsp+2]
        ; -- literal r1, #1 --
        mov ax, 1
        ; -- add r0, r1 (i16) --
        add cx, ax
        ; -- write var [i16 (i16)], 0 --
        mov [rsp+2], cx
        ; -- jump while_4 --
        jmp tsf_while_4
tsf_endwhile_4:
        ; -- drop vars i16, i16 --
        add rsp, 4
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
        ; -- jump while_3 --
        jmp tsf_while_3
tsf_endwhile_3:
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
        string_0 db ' *'

section '.data' data readable writeable
; board
var_0 rb 30

mem rb 640000
