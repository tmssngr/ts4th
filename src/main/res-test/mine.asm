format pe64 console
include 'win64ax.inc'

STD_IN_HANDLE = -10
STD_OUT_HANDLE = -11
STD_ERR_HANDLE = -12
STACK_SIZE = 1024 * 8

entry start

section '.text' code readable executable

start:
        mov r15, rsp
        sub rsp, STACK_SIZE
        sub rsp, 8
          call init
          call tsf_main
        add rsp, 8
        mov rcx, 0
        sub rsp, 0x20
          call [ExitProcess]


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
        ; -- and r0, 524287 (u32) --
        and ecx, 524287
        ; -- read var r1, [<empty> (u32)] --
        mov rax, [rsp+0]
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
        ; -- shr r1, 15 (u32) --
        shr eax, 15
        ; -- mov 0, 1 (u32) --
        mov ecx, eax
        ; -- read var r1, [<empty> (u32)] --
        mov rax, [rsp+0]
        ; -- imul r0, r1 (u32) --
        imul ecx, eax
        ; -- push 0 (u32) --
        sub r15, 4
        mov [r15], ecx
        ; -- and r0, 65535 (u32) --
        and ecx, 65535
        ; -- mov 1, 0 (u32) --
        mov eax, ecx
        ; -- shl r1, 15 (u32) --
        shl eax, 15
        ; -- pop 0 (u32) --
        mov ecx, [r15]
        add r15, 4
        ; -- push 1 (u32) --
        sub r15, 4
        mov [r15], eax
        ; -- mov 1, 0 (u32) --
        mov eax, ecx
        ; -- shr r1, 16 (u32) --
        shr eax, 16
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
        ; -- and r0, 2147483647 (u32) --
        and ecx, 2147483647
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
        ; -- shr r1, 31 (u32) --
        shr eax, 31
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

        ; -- proc printNibble --
tsf_printNibble:
        ; -- pop 0 (u8) --
        mov cl, [r15]
        add r15, 1
        ; -- and r0, 15 (u8) --
        and cl, 15
        ; -- add r0, 48 (u8) --
        add cl, 48
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- gt r0, 57 (u8) --
        cmp   cl, 57
        mov   cx, 0
        mov   bx, 1
        cmova cx, bx
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i2 --
        jz .i2
        ; -- literal r0, #65 --
        mov cl, 65
        ; -- sub r0, 57 (u8) --
        sub cl, 57
        ; -- sub r0, 1 (u8) --
        sub cl, 1
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
.i2:
        ; -- pop 0 (u8) --
        mov cl, [r15]
        add r15, 1
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- ret --
        ret

        ; -- proc printHex2 --
tsf_printHex2:
        ; -- pop 0 (u8) --
        mov cl, [r15]
        add r15, 1
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- mov 1, 0 (u8) --
        mov al, cl
        ; -- shr r1, 4 (u8) --
        shr al, 4
        ; -- push 1 (u8) --
        sub r15, 1
        mov [r15], al
        ; -- call printNibble --
        call tsf_printNibble
        ; -- call printNibble --
        call tsf_printNibble
        ; -- ret --
        ret

        ; -- proc printHex4 --
tsf_printHex4:
        ; -- pop 0 (u16) --
        mov cx, [r15]
        add r15, 2
        ; -- push 0 (u16) --
        sub r15, 2
        mov [r15], cx
        ; -- mov 1, 0 (u16) --
        mov ax, cx
        ; -- shr r1, 8 (u16) --
        shr ax, 8
        ; -- mov 0, 1 (u16) --
        mov cx, ax
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- call printHex2 --
        call tsf_printHex2
        ; -- pop 0 (u16) --
        mov cx, [r15]
        add r15, 2
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- call printHex2 --
        call tsf_printHex2
        ; -- ret --
        ret

        ; -- proc rowColumnToCell --
tsf_rowColumnToCell:
        ; -- pop 1 (i16) --
        mov ax, [r15]
        add r15, 2
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push 1 (i16) --
        sub r15, 2
        mov [r15], ax
        ; -- imul r0, 40 (i16) --
        imul cx, 40
        ; -- mov 1, 0 (i16) --
        mov ax, cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- add r0, r1 (i16) --
        add cx, ax
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- var r1, @field --
        lea rax, [var_1]
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- cast 0, (i16 -> ptr) --
        movsx rcx, cx
        ; -- add r1, r0 (ptr) --
        add rax, rcx
        ; -- push 1 (ptr) --
        sub r15, 8
        mov [r15], rax
        ; -- ret --
        ret

        ; -- proc getCell --
tsf_getCell:
        ; -- call rowColumnToCell --
        call tsf_rowColumnToCell
        ; -- pop 1 (ptr) --
        mov rax, [r15]
        add r15, 8
        ; -- load 0 (u8), @1 --
        mov cl, byte [rax]
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- ret --
        ret

        ; -- proc bomb? --
tsf_bomb?:
        ; -- pop 0 (u8) --
        mov cl, [r15]
        add r15, 1
        ; -- and r0, 1 (u8) --
        and cl, 1
        ; -- neq r0, 0 (u8) --
        cmp   cl, 0
        mov   cx, 0
        mov   bx, 1
        cmovne cx, bx
        ; -- push 0 (bool) --
        sub r15, 1
        mov [r15], cl
        ; -- ret --
        ret

        ; -- proc open? --
tsf_open?:
        ; -- pop 0 (u8) --
        mov cl, [r15]
        add r15, 1
        ; -- and r0, 2 (u8) --
        and cl, 2
        ; -- neq r0, 0 (u8) --
        cmp   cl, 0
        mov   cx, 0
        mov   bx, 1
        cmovne cx, bx
        ; -- push 0 (bool) --
        sub r15, 1
        mov [r15], cl
        ; -- ret --
        ret

        ; -- proc checkCellBounds --
tsf_checkCellBounds:
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push var r0 (i16) --
        push cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push var r0 (i16) --
        push cx
        ; -- read var r0, [<empty> (i16)] --
        mov cx, [rsp+0]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- read var r0, [i16 (i16)] --
        mov cx, [rsp+2]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- read var r0, [<empty> (i16)] --
        mov cx, [rsp+0]
        ; -- ge r0, 0 (i16) --
        cmp   cx, 0
        mov   cx, 0
        mov   bx, 1
        cmovge cx, bx
        ; -- push 0 (bool) --
        sub r15, 1
        mov [r15], cl
        ; -- read var r0, [<empty> (i16)] --
        mov cx, [rsp+0]
        ; -- lt r0, 20 (i16) --
        cmp   cx, 20
        mov   cx, 0
        mov   bx, 1
        cmovl cx, bx
        ; -- mov 1, 0 (bool) --
        mov al, cl
        ; -- pop 0 (bool) --
        mov cl, [r15]
        add r15, 1
        ; -- and r0, r1 (bool) --
        and cl, al
        ; -- push 0 (bool) --
        sub r15, 1
        mov [r15], cl
        ; -- read var r0, [i16 (i16)] --
        mov cx, [rsp+2]
        ; -- ge r0, 0 (i16) --
        cmp   cx, 0
        mov   cx, 0
        mov   bx, 1
        cmovge cx, bx
        ; -- push 0 (bool) --
        sub r15, 1
        mov [r15], cl
        ; -- read var r0, [i16 (i16)] --
        mov cx, [rsp+2]
        ; -- lt r0, 40 (i16) --
        cmp   cx, 40
        mov   cx, 0
        mov   bx, 1
        cmovl cx, bx
        ; -- mov 1, 0 (bool) --
        mov al, cl
        ; -- pop 0 (bool) --
        mov cl, [r15]
        add r15, 1
        ; -- and r0, r1 (bool) --
        and cl, al
        ; -- mov 1, 0 (bool) --
        mov al, cl
        ; -- pop 0 (bool) --
        mov cl, [r15]
        add r15, 1
        ; -- and r0, r1 (bool) --
        and cl, al
        ; -- push 0 (bool) --
        sub r15, 1
        mov [r15], cl
        ; -- drop vars i16, i16 --
        add rsp, 4
        ; -- ret --
        ret

        ; -- proc setCell --
tsf_setCell:
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
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push var r0 (i16) --
        push cx
        ; -- read var r0, [<empty> (i16)] --
        mov cx, [rsp+0]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- read var r0, [i16 (i16)] --
        mov cx, [rsp+2]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- call rowColumnToCell --
        call tsf_rowColumnToCell
        ; -- pop 1 (ptr) --
        mov rax, [r15]
        add r15, 8
        ; -- read var r0, [i16, i16 (u8)] --
        mov cx, [rsp+4]
        ; -- store @1, 0 (u8) --
        mov byte [rax], cl
        ; -- drop vars i16, i16, u8 --
        add rsp, 6
        ; -- ret --
        ret

        ; -- proc getBombCountAround --
tsf_getBombCountAround:
        ; -- literal r0, #0 --
        mov cx, 0
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- literal r0, #0 --
        mov cx, 0
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- literal r0, #0 --
        mov cx, 0
        ; -- push var r0 (i16) --
        push cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push var r0 (i16) --
        push cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push var r0 (i16) --
        push cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push var r0 (i16) --
        push cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push var r0 (i16) --
        push cx
        ; -- literal r0, #-1 --
        mov cx, -1
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
        ; -- read var r1, [i16 (i16)] --
        mov ax, [rsp+2]
        ; -- lt r0, r1 (i16) --
        cmp   cx, ax
        mov   cx, 0
        mov   ax, 1
        cmovl cx, ax
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i3 --
        jz .i3
        ; -- literal r0, #-1 --
        mov cx, -1
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
.i4:
        ; -- read var r0, [<empty> (i16)] --
        mov cx, [rsp+0]
        ; -- read var r1, [i16 (i16)] --
        mov ax, [rsp+2]
        ; -- lt r0, r1 (i16) --
        cmp   cx, ax
        mov   cx, 0
        mov   ax, 1
        cmovl cx, ax
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i6 --
        jz .i6
        ; -- read var r0, [i16, i16, i16, i16 (i16)] --
        mov cx, [rsp+8]
        ; -- read var r1, [i16, i16 (i16)] --
        mov ax, [rsp+4]
        ; -- add r0, r1 (i16) --
        add cx, ax
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- read var r0, [i16, i16, i16, i16, i16 (i16)] --
        mov cx, [rsp+10]
        ; -- read var r1, [<empty> (i16)] --
        mov ax, [rsp+0]
        ; -- add r0, r1 (i16) --
        add cx, ax
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- call checkCellBounds --
        call tsf_checkCellBounds
        ; -- pop 0 (bool) --
        mov cl, [r15]
        add r15, 1
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i8 --
        jz .i8
        ; -- call getCell --
        call tsf_getCell
        ; -- call bomb? --
        call tsf_bomb?
        ; -- pop 0 (bool) --
        mov cl, [r15]
        add r15, 1
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i11 --
        jz .i11
        ; -- read var r0, [i16, i16, i16, i16, i16, i16, i16, i16 (i16)] --
        mov cx, [rsp+16]
        ; -- add r0, 1 (i16) --
        add cx, 1
        ; -- write var [i16, i16, i16, i16, i16, i16, i16, i16 (i16)], 0 --
        mov [rsp+16], cx
        ; -- jump .i11 --
        jmp .i11
.i8:
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
.i11:
        ; -- read var r0, [<empty> (i16)] --
        mov cx, [rsp+0]
        ; -- add r0, 1 (i16) --
        add cx, 1
        ; -- write var [<empty> (i16)], 0 --
        mov [rsp+0], cx
        ; -- jump .i4 --
        jmp .i4
.i6:
        ; -- drop vars i16, i16 --
        add rsp, 4
        ; -- read var r0, [<empty> (i16)] --
        mov cx, [rsp+0]
        ; -- add r0, 1 (i16) --
        add cx, 1
        ; -- write var [<empty> (i16)], 0 --
        mov [rsp+0], cx
        ; -- jump .i1 --
        jmp .i1
.i3:
        ; -- drop vars i16, i16 --
        add rsp, 4
        ; -- read var r0, [i16, i16, i16, i16 (i16)] --
        mov cx, [rsp+8]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- drop vars i16, i16, i16, i16, i16 --
        add rsp, 10
        ; -- ret --
        ret

        ; -- proc getSpacer --
tsf_getSpacer:
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push var r0 (i16) --
        push cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push var r0 (i16) --
        push cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push var r0 (i16) --
        push cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push var r0 (i16) --
        push cx
        ; -- read var r0, [i16, i16 (i16)] --
        mov cx, [rsp+4]
        ; -- read var r1, [<empty> (i16)] --
        mov ax, [rsp+0]
        ; -- eq r0, r1 (i16) --
        cmp   cx, ax
        mov   cx, 0
        mov   ax, 1
        cmove cx, ax
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i2 --
        jz .i2
        ; -- read var r0, [i16, i16, i16 (i16)] --
        mov cx, [rsp+6]
        ; -- read var r1, [i16 (i16)] --
        mov ax, [rsp+2]
        ; -- eq r0, r1 (i16) --
        cmp   cx, ax
        mov   cx, 0
        mov   ax, 1
        cmove cx, ax
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i4 --
        jz .i4
        ; -- literal r0, #91 --
        mov cl, 91
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- jump .i9 --
        jmp .i9
.i4:
        ; -- read var r0, [i16, i16, i16 (i16)] --
        mov cx, [rsp+6]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- read var r0, [i16 (i16)] --
        mov cx, [rsp+2]
        ; -- sub r0, 1 (i16) --
        sub cx, 1
        ; -- mov 1, 0 (i16) --
        mov ax, cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- eq r0, r1 (i16) --
        cmp   cx, ax
        mov   cx, 0
        mov   ax, 1
        cmove cx, ax
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i7 --
        jz .i7
        ; -- literal r0, #93 --
        mov cl, 93
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- jump .i9 --
        jmp .i9
.i7:
        ; -- literal r0, #32 --
        mov cl, 32
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- jump .i9 --
        jmp .i9
.i2:
        ; -- literal r0, #32 --
        mov cl, 32
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
.i9:
        ; -- drop vars i16, i16, i16, i16 --
        add rsp, 8
        ; -- ret --
        ret

        ; -- proc printCell --
tsf_printCell:
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push var r0 (i16) --
        push cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push var r0 (i16) --
        push cx
        ; -- pop 0 (u8) --
        mov cl, [r15]
        add r15, 1
        ; -- push var r0 (u8) --
        push cx
        ; -- read var r0, [<empty> (u8)] --
        mov cx, [rsp+0]
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- call open? --
        call tsf_open?
        ; -- pop 0 (bool) --
        mov cl, [r15]
        add r15, 1
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i2 --
        jz .i2
        ; -- read var r0, [<empty> (u8)] --
        mov cx, [rsp+0]
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- call bomb? --
        call tsf_bomb?
        ; -- pop 0 (bool) --
        mov cl, [r15]
        add r15, 1
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i4 --
        jz .i4
        ; -- literal r0, #42 --
        mov cl, 42
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- jump .i9 --
        jmp .i9
.i4:
        ; -- read var r0, [u8 (i16)] --
        mov cx, [rsp+2]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- read var r0, [u8, i16 (i16)] --
        mov cx, [rsp+4]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- call getBombCountAround --
        call tsf_getBombCountAround
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- gt r0, 0 (i16) --
        cmp   cx, 0
        mov   cx, 0
        mov   bx, 1
        cmovg cx, bx
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i7 --
        jz .i7
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- add r0, 48 (u8) --
        add cl, 48
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- jump .i9 --
        jmp .i9
.i7:
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- literal r0, #32 --
        mov cl, 32
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- jump .i9 --
        jmp .i9
.i2:
        ; -- literal r0, #46 --
        mov cl, 46
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
.i9:
        ; -- pop 0 (u8) --
        mov cl, [r15]
        add r15, 1
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- drop vars u8, i16, i16 --
        add rsp, 6
        ; -- ret --
        ret

        ; -- proc printField --
tsf_printField:
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push var r0 (i16) --
        push cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push var r0 (i16) --
        push cx
        ; -- literal r0, #0 --
        mov cl, 0
        ; -- literal r1, #0 --
        mov al, 0
        ; -- setCursor --
        mov rdi, rsp
        and spl, 0xf0

        movzx rdx, al
        shl   rdx, 16
        or    dl, cl
        lea   rcx, [hStdOut]
        mov   rcx, qword [rcx]
          sub    rsp, 20h
            call [SetConsoleCursorPosition]
          ; add    rsp, 20h
        mov   rsp, rdi
        ; -- literal r0, #0 --
        mov cx, 0
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- literal r0, #20 --
        mov cx, 20
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
        ; -- read var r1, [i16 (i16)] --
        mov ax, [rsp+2]
        ; -- lt r0, r1 (i16) --
        cmp   cx, ax
        mov   cx, 0
        mov   ax, 1
        cmovl cx, ax
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i3 --
        jz .i3
        ; -- literal r0, #124 --
        mov cl, 124
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- literal r0, #0 --
        mov cx, 0
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- literal r0, #40 --
        mov cx, 40
        ; -- push var r0 (i16) --
        push cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push var r0 (i16) --
        push cx
.i4:
        ; -- read var r0, [<empty> (i16)] --
        mov cx, [rsp+0]
        ; -- read var r1, [i16 (i16)] --
        mov ax, [rsp+2]
        ; -- lt r0, r1 (i16) --
        cmp   cx, ax
        mov   cx, 0
        mov   ax, 1
        cmovl cx, ax
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i6 --
        jz .i6
        ; -- read var r0, [i16, i16 (i16)] --
        mov cx, [rsp+4]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- read var r0, [<empty> (i16)] --
        mov cx, [rsp+0]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- read var r0, [i16, i16, i16, i16 (i16)] --
        mov cx, [rsp+8]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- read var r0, [i16, i16, i16, i16, i16 (i16)] --
        mov cx, [rsp+10]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- call getSpacer --
        call tsf_getSpacer
        ; -- pop 0 (u8) --
        mov cl, [r15]
        add r15, 1
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- read var r0, [i16, i16 (i16)] --
        mov cx, [rsp+4]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- read var r0, [<empty> (i16)] --
        mov cx, [rsp+0]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- call getCell --
        call tsf_getCell
        ; -- read var r0, [i16, i16 (i16)] --
        mov cx, [rsp+4]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- read var r0, [<empty> (i16)] --
        mov cx, [rsp+0]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- call printCell --
        call tsf_printCell
        ; -- read var r0, [<empty> (i16)] --
        mov cx, [rsp+0]
        ; -- add r0, 1 (i16) --
        add cx, 1
        ; -- write var [<empty> (i16)], 0 --
        mov [rsp+0], cx
        ; -- jump .i4 --
        jmp .i4
.i6:
        ; -- drop vars i16, i16 --
        add rsp, 4
        ; -- read var r0, [<empty> (i16)] --
        mov cx, [rsp+0]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- literal r0, #40 --
        mov cx, 40
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- read var r0, [i16, i16 (i16)] --
        mov cx, [rsp+4]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- read var r0, [i16, i16, i16 (i16)] --
        mov cx, [rsp+6]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- call getSpacer --
        call tsf_getSpacer
        ; -- pop 0 (u8) --
        mov cl, [r15]
        add r15, 1
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
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
        ; -- read var r0, [<empty> (i16)] --
        mov cx, [rsp+0]
        ; -- add r0, 1 (i16) --
        add cx, 1
        ; -- write var [<empty> (i16)], 0 --
        mov [rsp+0], cx
        ; -- jump .i1 --
        jmp .i1
.i3:
        ; -- drop vars i16, i16, i16, i16 --
        add rsp, 8
        ; -- ret --
        ret

        ; -- proc abs --
tsf_abs:
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- lt r0, 0 (i16) --
        cmp   cx, 0
        mov   cx, 0
        mov   bx, 1
        cmovl cx, bx
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i2 --
        jz .i2
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- literal r1, #0 --
        mov ax, 0
        ; -- push 1 (i16) --
        sub r15, 2
        mov [r15], ax
        ; -- mov 1, 0 (i16) --
        mov ax, cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- sub r0, r1 (i16) --
        sub cx, ax
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
.i2:
        ; -- ret --
        ret

        ; -- proc initField --
tsf_initField:
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push var r0 (i16) --
        push cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push var r0 (i16) --
        push cx
        ; -- literal r0, #0 --
        mov cx, 0
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- literal r0, #20 --
        mov cx, 20
        ; -- imul r0, 2000 (i16) --
        imul cx, 2000
        ; -- idiv r0, 1000 (i16) --
        mov dx, 1000
        xor eax, eax
        mov ax, cx
        xor ecx, ecx
        mov cx, dx
        xor edx, edx
        idiv ecx
        mov ecx, eax
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
        ; -- read var r1, [i16 (i16)] --
        mov ax, [rsp+2]
        ; -- lt r0, r1 (i16) --
        cmp   cx, ax
        mov   cx, 0
        mov   ax, 1
        cmovl cx, ax
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i3 --
        jz .i3
        ; -- call random --
        call tsf_random
        ; -- literal r0, #20 --
        mov cx, 20
        ; -- cast 0, (i16 -> u32) --
        movsx ecx, cx
        ; -- mov 1, 0 (u32) --
        mov eax, ecx
        ; -- pop 0 (u32) --
        mov ecx, [r15]
        add r15, 4
        ; -- imod r0, r1 (u32) --
        mov edx, eax
        xor eax, eax
        mov eax, ecx
        xor ecx, ecx
        mov ecx, edx
        xor edx, edx
        idiv ecx
        mov ecx, edx
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- call random --
        call tsf_random
        ; -- literal r0, #40 --
        mov cx, 40
        ; -- cast 0, (i16 -> u32) --
        movsx ecx, cx
        ; -- mov 1, 0 (u32) --
        mov eax, ecx
        ; -- pop 0 (u32) --
        mov ecx, [r15]
        add r15, 4
        ; -- imod r0, r1 (u32) --
        mov edx, eax
        xor eax, eax
        mov eax, ecx
        xor ecx, ecx
        mov ecx, edx
        xor edx, edx
        idiv ecx
        mov ecx, edx
        ; -- push var r0 (i16) --
        push cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push var r0 (i16) --
        push cx
        ; -- read var r0, [<empty> (i16)] --
        mov cx, [rsp+0]
        ; -- read var r1, [i16, i16, i16, i16 (i16)] --
        mov ax, [rsp+8]
        ; -- sub r0, r1 (i16) --
        sub cx, ax
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- call abs --
        call tsf_abs
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- gt r0, 1 (i16) --
        cmp   cx, 1
        mov   cx, 0
        mov   bx, 1
        cmovg cx, bx
        ; -- push 0 (bool) --
        sub r15, 1
        mov [r15], cl
        ; -- read var r0, [i16 (i16)] --
        mov cx, [rsp+2]
        ; -- read var r1, [i16, i16, i16, i16, i16 (i16)] --
        mov ax, [rsp+10]
        ; -- sub r0, r1 (i16) --
        sub cx, ax
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- call abs --
        call tsf_abs
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- gt r0, 1 (i16) --
        cmp   cx, 1
        mov   cx, 0
        mov   bx, 1
        cmovg cx, bx
        ; -- mov 1, 0 (bool) --
        mov al, cl
        ; -- pop 0 (bool) --
        mov cl, [r15]
        add r15, 1
        ; -- or r0, r1 (bool) --
        or cl, al
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i5 --
        jz .i5
        ; -- read var r0, [<empty> (i16)] --
        mov cx, [rsp+0]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- read var r0, [i16 (i16)] --
        mov cx, [rsp+2]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- literal r0, #1 --
        mov cl, 1
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- call setCell --
        call tsf_setCell
.i5:
        ; -- drop vars i16, i16 --
        add rsp, 4
        ; -- read var r0, [<empty> (i16)] --
        mov cx, [rsp+0]
        ; -- add r0, 1 (i16) --
        add cx, 1
        ; -- write var [<empty> (i16)], 0 --
        mov [rsp+0], cx
        ; -- jump .i1 --
        jmp .i1
.i3:
        ; -- drop vars i16, i16, i16, i16 --
        add rsp, 8
        ; -- ret --
        ret

        ; -- proc main --
tsf_main:
        ; -- literal r0, #7439742 --
        mov ecx, 7439742
        ; -- push 0 (u32) --
        sub r15, 4
        mov [r15], ecx
        ; -- call initRandom --
        call tsf_initRandom
        ; -- literal r0, #true --
        mov cl, -1
        ; -- push 0 (bool) --
        sub r15, 1
        mov [r15], cl
        ; -- literal r0, #40 --
        mov cx, 40
        ; -- idiv r0, 2 (i16) --
        mov dx, 2
        xor eax, eax
        mov ax, cx
        xor ecx, ecx
        mov cx, dx
        xor edx, edx
        idiv ecx
        mov ecx, eax
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- literal r0, #20 --
        mov cx, 20
        ; -- idiv r0, 2 (i16) --
        mov dx, 2
        xor eax, eax
        mov ax, cx
        xor ecx, ecx
        mov cx, dx
        xor edx, edx
        idiv ecx
        mov ecx, eax
        ; -- push var r0 (i16) --
        push cx
        ; -- pop 0 (i16) --
        mov cx, [r15]
        add r15, 2
        ; -- push var r0 (i16) --
        push cx
        ; -- pop 0 (bool) --
        mov cl, [r15]
        add r15, 1
        ; -- push var r0 (bool) --
        push cx
.i1:
        ; -- read var r0, [bool, i16 (i16)] --
        mov cx, [rsp+4]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- read var r0, [bool (i16)] --
        mov cx, [rsp+2]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- call printField --
        call tsf_printField
        ; -- getChar --
        sub  rsp, 8
          call tsfbi_getChar
        add rsp, 8
        ; -- push 0 (u16) --
        sub r15, 2
        mov [r15], cx
        ; -- push 0 (u16) --
        sub r15, 2
        mov [r15], cx
        ; -- call printHex4 --
        call tsf_printHex4
        ; -- literal r0, #32 --
        mov cl, 32
        ; -- emit --
        sub rsp, 8
          call tsfbi_emit
        add rsp, 8
        ; -- pop 0 (u16) --
        mov cx, [r15]
        add r15, 2
        ; -- push 0 (u16) --
        sub r15, 2
        mov [r15], cx
        ; -- eq r0, 27 (u16) --
        cmp   cx, 27
        mov   cx, 0
        mov   bx, 1
        cmove cx, bx
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i5 --
        jz .i5
        ; -- pop 0 (u16) --
        mov cx, [r15]
        add r15, 2
        ; -- jump .i3 --
        jmp .i3
.i5:
        ; -- pop 0 (u16) --
        mov cx, [r15]
        add r15, 2
        ; -- push 0 (u16) --
        sub r15, 2
        mov [r15], cx
        ; -- eq r0, 57416 (u16) --
        cmp   cx, 57416
        mov   cx, 0
        mov   bx, 1
        cmove cx, bx
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i7 --
        jz .i7
        ; -- read var r0, [bool, i16 (i16)] --
        mov cx, [rsp+4]
        ; -- add r0, 19 (i16) --
        add cx, 19
        ; -- imod r0, 20 (i16) --
        mov dx, 20
        xor eax, eax
        mov ax, cx
        xor ecx, ecx
        mov cx, dx
        xor edx, edx
        idiv ecx
        mov ecx, edx
        ; -- write var [bool, i16 (i16)], 0 --
        mov [rsp+4], cx
.i7:
        ; -- pop 0 (u16) --
        mov cx, [r15]
        add r15, 2
        ; -- push 0 (u16) --
        sub r15, 2
        mov [r15], cx
        ; -- eq r0, 57424 (u16) --
        cmp   cx, 57424
        mov   cx, 0
        mov   bx, 1
        cmove cx, bx
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i9 --
        jz .i9
        ; -- read var r0, [bool, i16 (i16)] --
        mov cx, [rsp+4]
        ; -- add r0, 1 (i16) --
        add cx, 1
        ; -- imod r0, 20 (i16) --
        mov dx, 20
        xor eax, eax
        mov ax, cx
        xor ecx, ecx
        mov cx, dx
        xor edx, edx
        idiv ecx
        mov ecx, edx
        ; -- write var [bool, i16 (i16)], 0 --
        mov [rsp+4], cx
.i9:
        ; -- pop 0 (u16) --
        mov cx, [r15]
        add r15, 2
        ; -- push 0 (u16) --
        sub r15, 2
        mov [r15], cx
        ; -- eq r0, 57419 (u16) --
        cmp   cx, 57419
        mov   cx, 0
        mov   bx, 1
        cmove cx, bx
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i11 --
        jz .i11
        ; -- read var r0, [bool (i16)] --
        mov cx, [rsp+2]
        ; -- add r0, 39 (i16) --
        add cx, 39
        ; -- imod r0, 40 (i16) --
        mov dx, 40
        xor eax, eax
        mov ax, cx
        xor ecx, ecx
        mov cx, dx
        xor edx, edx
        idiv ecx
        mov ecx, edx
        ; -- write var [bool (i16)], 0 --
        mov [rsp+2], cx
.i11:
        ; -- pop 0 (u16) --
        mov cx, [r15]
        add r15, 2
        ; -- push 0 (u16) --
        sub r15, 2
        mov [r15], cx
        ; -- eq r0, 57421 (u16) --
        cmp   cx, 57421
        mov   cx, 0
        mov   bx, 1
        cmove cx, bx
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i13 --
        jz .i13
        ; -- read var r0, [bool (i16)] --
        mov cx, [rsp+2]
        ; -- add r0, 1 (i16) --
        add cx, 1
        ; -- imod r0, 40 (i16) --
        mov dx, 40
        xor eax, eax
        mov ax, cx
        xor ecx, ecx
        mov cx, dx
        xor edx, edx
        idiv ecx
        mov ecx, edx
        ; -- write var [bool (i16)], 0 --
        mov [rsp+2], cx
.i13:
        ; -- pop 0 (u16) --
        mov cx, [r15]
        add r15, 2
        ; -- push 0 (u16) --
        sub r15, 2
        mov [r15], cx
        ; -- eq r0, 13 (u16) --
        cmp   cx, 13
        mov   cx, 0
        mov   bx, 1
        cmove cx, bx
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i15 --
        jz .i15
        ; -- read var r0, [<empty> (bool)] --
        mov cx, [rsp+0]
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i17 --
        jz .i17
        ; -- literal r0, #false --
        mov cl, 0
        ; -- write var [<empty> (bool)], 0 --
        mov [rsp+0], cx
        ; -- read var r0, [bool, i16 (i16)] --
        mov cx, [rsp+4]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- read var r0, [bool (i16)] --
        mov cx, [rsp+2]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- call initField --
        call tsf_initField
.i17:
        ; -- read var r0, [bool, i16 (i16)] --
        mov cx, [rsp+4]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- read var r0, [bool (i16)] --
        mov cx, [rsp+2]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- call getCell --
        call tsf_getCell
        ; -- pop 0 (u8) --
        mov cl, [r15]
        add r15, 1
        ; -- push var r0 (u8) --
        push cx
        ; -- read var r0, [<empty> (u8)] --
        mov cx, [rsp+0]
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- call open? --
        call tsf_open?
        ; -- pop 0 (bool) --
        mov cl, [r15]
        add r15, 1
        ; -- not r0 (bool) --
        not cl
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i19 --
        jz .i19
        ; -- read var r0, [<empty> (u8)] --
        mov cx, [rsp+0]
        ; -- or r0, 2 (u8) --
        or cl, 2
        ; -- write var [<empty> (u8)], 0 --
        mov [rsp+0], cx
        ; -- read var r0, [u8, bool, i16 (i16)] --
        mov cx, [rsp+6]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- read var r0, [u8, bool (i16)] --
        mov cx, [rsp+4]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- read var r0, [<empty> (u8)] --
        mov cx, [rsp+0]
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- call setCell --
        call tsf_setCell
.i19:
        ; -- read var r0, [<empty> (u8)] --
        mov cx, [rsp+0]
        ; -- push 0 (u8) --
        sub r15, 1
        mov [r15], cl
        ; -- call bomb? --
        call tsf_bomb?
        ; -- pop 0 (bool) --
        mov cl, [r15]
        add r15, 1
        ; -- boolTest r0, r0 (i16) --
        test cl, cl
        ; -- jump z .i21 --
        jz .i21
        ; -- read var r0, [u8, bool, i16 (i16)] --
        mov cx, [rsp+6]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- read var r0, [u8, bool (i16)] --
        mov cx, [rsp+4]
        ; -- push 0 (i16) --
        sub r15, 2
        mov [r15], cx
        ; -- call printField --
        call tsf_printField
        ; -- literal r1, "0 --
        lea rax, [string_0]
        ; -- literal r0, #17 --
        mov cx, 17
        ; -- printString r1 (0) --
        movsx rdx, cx
        mov rcx, rax
        sub rsp, 8
          call tsfbi_printString
        add rsp, 8
        ; -- pop 0 (u16) --
        mov cx, [r15]
        add r15, 2
        ; -- drop vars u8 --
        add rsp, 2
        ; -- jump .i3 --
        jmp .i3
.i21:
        ; -- drop vars u8 --
        add rsp, 2
.i15:
        ; -- pop 0 (u16) --
        mov cx, [r15]
        add r15, 2
        ; -- jump .i1 --
        jmp .i1
.i3:
        ; -- drop vars bool, i16, i16 --
        add rsp, 6
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

tsfbi_getChar:
        mov rdi, rsp
        and spl, 0xf0
          sub rsp, 20h
            call [_getch]
            test al, al
            js   .x1
            jnz  .x2
            dec  al
.x1:
            mov  rbx, rax
            shl  rbx, 8
            call [_getch]
            or   rax, rbx
.x2:
            mov  rcx, rax
          ; add rsp, 20h
        mov rsp, rdi
        ret

; string constants
section '.data' data readable
        string_0 db 'boom! you', 0x27, 've lost'

section '.data' data readable writeable
        hStdIn  rb 8
        hStdOut rb 8
        hStdErr rb 8
        ; __random__
        var_0 rb 4
        ; field
        var_1 rb 800

mem rb 640000

section '.idata' import data readable writeable

library kernel32,'KERNEL32.DLL',\
        msvcrt,'MSVCRT.DLL'

import kernel32,\
       ExitProcess,'ExitProcess',\
       GetStdHandle,'GetStdHandle',\
       SetConsoleCursorPosition,'SetConsoleCursorPosition',\
       WriteFile,'WriteFile'

import msvcrt,\
       _getch,'_getch'
