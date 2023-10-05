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
        ; -- literal r0, #-8 --
        mov cl, -8
        ; -- printInt r0(i8) --
        movsx rcx, cl
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
        ; -- literal r0, #-16 --
        mov cx, -16
        ; -- printInt r0(i16) --
        movsx rcx, cx
        test   rcx, rcx
        jns    .x2
        neg    rcx
        push   rcx
          mov    cl, '-'
          call   tsfbi_emit
        pop    rcx
.x2:
        sub  rsp, 8
          call tsfbi_printUint
          mov  cl, ' '
          call tsfbi_emit
        add  rsp, 8
        ; -- literal r0, #-16 --
        mov cx, -16
        ; -- printInt r0(i16) --
        movsx rcx, cx
        test   rcx, rcx
        jns    .x3
        neg    rcx
        push   rcx
          mov    cl, '-'
          call   tsfbi_emit
        pop    rcx
.x3:
        sub  rsp, 8
          call tsfbi_printUint
          mov  cl, ' '
          call tsfbi_emit
        add  rsp, 8
        ; -- literal r0, #-32 --
        mov ecx, -32
        ; -- printInt r0(i32) --
        movsxd rcx, ecx
        test   rcx, rcx
        jns    .x4
        neg    rcx
        push   rcx
          mov    cl, '-'
          call   tsfbi_emit
        pop    rcx
.x4:
        sub  rsp, 8
          call tsfbi_printUint
          mov  cl, ' '
          call tsfbi_emit
        add  rsp, 8
        ; -- literal r0, #-64 --
        mov rcx, -64
        ; -- printInt r0(i64) --
        test   rcx, rcx
        jns    .x5
        neg    rcx
        push   rcx
          mov    cl, '-'
          call   tsfbi_emit
        pop    rcx
.x5:
        sub  rsp, 8
          call tsfbi_printUint
          mov  cl, ' '
          call tsfbi_emit
        add  rsp, 8
        ; -- literal r0, #8 --
        mov cl, 8
        ; -- printInt r0(u8) --
        movzx rcx, cl
        sub  rsp, 8
          call tsfbi_printUint
          mov  cl, ' '
          call tsfbi_emit
        add  rsp, 8
        ; -- literal r0, #16 --
        mov cx, 16
        ; -- printInt r0(i16) --
        movsx rcx, cx
        test   rcx, rcx
        jns    .x6
        neg    rcx
        push   rcx
          mov    cl, '-'
          call   tsfbi_emit
        pop    rcx
.x6:
        sub  rsp, 8
          call tsfbi_printUint
          mov  cl, ' '
          call tsfbi_emit
        add  rsp, 8
        ; -- literal r0, #16 --
        mov cx, 16
        ; -- printInt r0(u16) --
        movzx rcx, cx
        sub  rsp, 8
          call tsfbi_printUint
          mov  cl, ' '
          call tsfbi_emit
        add  rsp, 8
        ; -- literal r0, #32 --
        mov ecx, 32
        ; -- printInt r0(u32) --
        sub  rsp, 8
          call tsfbi_printUint
          mov  cl, ' '
          call tsfbi_emit
        add  rsp, 8
        ; -- literal r0, #64 --
        mov rcx, 64
        ; -- printInt r0(u64) --
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

mem rb 640000
