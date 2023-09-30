package de.regnis.ts4th;

import java.io.*;
import java.util.*;
import java.util.function.*;

import org.jetbrains.annotations.*;

import static java.lang.StringTemplate.STR;

/**
 * @author Thomas Singer
 */
public class X86Win64 {

	private static final int PTR_SIZE = 8;

	private static final String INDENTATION = "        ";
	// data stack pointer
	private static final String REG_DSP = "r15";
	private static final String LABEL_PREFIX = "tsf_";
	private static final String LABEL_PREFIX_BI = "tsfbi_";
	private static final String STRING_PREFIX = "string_";
	private static final String VAR_PREFIX = "var_";
	private static final String PRINT_STRING = LABEL_PREFIX_BI + "printString";
	private static final String EMIT = LABEL_PREFIX_BI + "emit";
	private static final String PRINT_UINT = LABEL_PREFIX_BI + "printUint";

	private final Writer writer;

	private int labelIndex;

	public X86Win64(Writer writer) {
		this.writer = writer;
	}

	public void write(AsmIRProgram program) throws IOException {
		write("""
				      format pe64 console
				      include 'win64ax.inc'

				      STD_OUTPUT_HANDLE = -11
				      STACK_SIZE = 1024 * 8

				      .code
				      start:
				      """);
		writeIndented(STR."""
				              mov \{REG_DSP}, rsp
				              sub rsp, STACK_SIZE
				              sub rsp, 8
				                call \{LABEL_PREFIX}main
				              add rsp, 8
				              invoke ExitProcess, 0""");
		writeNL();

		for (AsmIRFunction function : program.functions()) {
			labelIndex = 0;
			write(function);
		}

		writeNL();

		writeCharPrint();
		writeNL();
		writeStringPrint();
		writeNL();
		writeUintPrint();
		write(".end start");

		final List<Supplier<byte[]>> stringLiterals = program.stringLiterals();
		if (stringLiterals.size() > 0) {
			write("""
					      ; string constants
					      section '.data' data readable""");
			for (int i = 0; i < stringLiterals.size(); i++) {
				final String encoded = encode(stringLiterals.get(i).get());
				writeIndented(STRING_PREFIX + i + " db " + encoded);
			}
		}
		writeNL();

		write("""
				      section '.data' data readable writeable""");

		for (Var var : program.vars()) {
			write(STR."""
					      ; \{var.name()}
					      \{VAR_PREFIX + var.index()} rb \{var.size()}
					      """);
		}
		write("""

				      mem rb 640000""");
	}

	private String encode(byte[] bytes) {
		final StringBuilder buffer = new StringBuilder();
		boolean stringIsOpen = false;
		for (byte b : bytes) {
			if (b >= 0x20 && b < 0x7f) {
				if (!stringIsOpen) {
					if (buffer.length() > 0) {
						buffer.append(", ");
					}
					buffer.append("'");
					stringIsOpen = true;
				}
				buffer.append((char)b);
			}
			else {
				if (stringIsOpen) {
					buffer.append("', ");
					stringIsOpen = false;
				}
				buffer.append("0x");
				Utils.toHex(b, 2, buffer);
			}
		}
		if (stringIsOpen) {
			buffer.append("'");
		}
		return buffer.toString();
	}

	private void writeCharPrint() throws IOException {
		// rcx = char
		writeLabel(EMIT);
		writeIndented(STR."""
				              push rcx ; = sub rsp, 8
				                mov rcx, rsp
				                mov rdx, 1
				                call \{PRINT_STRING}
				              pop rcx
				              ret""");
	}

	private void writeStringPrint() throws IOException {
		// rcx = pointer to text
		// rdx = length
		// BOOL WriteFile(
		//  [in]                HANDLE       hFile,                    rcx
		//  [in]                LPCVOID      lpBuffer,                 rdx
		//  [in]                DWORD        nNumberOfBytesToWrite,    r8
		//  [out, optional]     LPDWORD      lpNumberOfBytesWritten,   r9
		//  [in, out, optional] LPOVERLAPPED lpOverlapped              stack
		//);
		writeLabel(PRINT_STRING);
		writeIndented("""
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
				              """);
	}

	private void writeUintPrint() throws IOException {
		// input: rcx
		// rsp+0   = buf (20h long)
		// rsp+20h = pos
		// rsp+24h = x
		writeLabel(PRINT_UINT);
		writeIndented("""
				              push   rbp
				              mov    rbp,rsp
				              sub    rsp, 50h
				              mov    qword [rsp+24h], rcx

				              ; int pos = sizeof(buf);
				              mov    eax, 20h
				              mov    dword [rsp+20h], eax

				              ; do {
				              """);
		writeLabel(".print");
		writeIndented(STR."""
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
				                call   \{PRINT_STRING}
				              ;add    rsp, 8
				              leave ; Set SP to BP, then pop BP
				              ret
				              """);
	}

	private void write(AsmIRFunction function) throws IOException {
		write("");
		writeComment("proc " + function.name());
		writeLabel(LABEL_PREFIX + function.name());

		for (AsmIR instruction : function.instructions()) {
			write(instruction);
		}
	}

	private void write(AsmIR instruction) throws IOException {
		switch (instruction) {
		case AsmIR.Label(String name) -> {
			writeLabel(LABEL_PREFIX + name);
		}
		case AsmIR.IntLiteral(int targetReg, int value) -> {
			writeComment(STR."literal r\{targetReg}, #\{value}");
			writeIndented(STR."mov \{getRegName(targetReg, 2)}, \{value}");
		}
		case  AsmIR.BoolLiteral(int targetReg, boolean value) -> {
			writeComment(STR."literal r\{ targetReg }, #\{ value }");
			writeIndented(STR."mov \{getRegName(targetReg, 2)}, \{ value ? 1 : 0}");
		}
		case AsmIR.PtrLiteral(int targetReg, int index, String name) -> {
			writeComment(STR."var r\{ targetReg }, @\{ name }");
			writeIndented(STR."lea \{getRegName(targetReg, PTR_SIZE)}, [\{ VAR_PREFIX + index }]");
		}
		case  AsmIR.StringLiteral(int targetReg, int index) -> {
			writeComment(STR."literal r\{ targetReg }, \"\{ index }");
			writeIndented(STR."lea \{getRegName(targetReg, PTR_SIZE)}, [\{ STRING_PREFIX + index }]");
		}
		case AsmIR.Jump(AsmIR.Condition condition, String target) -> {
			writeJump(condition, target);
		}
		case AsmIR.Push(int sourceReg, int size) -> {
			writeComment(STR."push \{ sourceReg } (\{size})");

			final String regName = getRegName(sourceReg, size);
			writeIndented(STR."""
					              sub \{REG_DSP}, \{size}
					              mov [\{REG_DSP}], \{regName}
					              """);
		}
		case AsmIR.Pop(int targetReg, int size) -> {
			writeComment(STR."pop \{ targetReg } (\{size})");

			final String regName = getRegName(targetReg, size);
			writeIndented(STR."""
					              mov \{regName}, [\{REG_DSP}]
					              add \{REG_DSP}, \{size}
					              """);
		}
		case AsmIR.Move(int targetReg, int sourceReg, int size) -> {
			writeIndented(STR."mov \{getRegName(targetReg, size)}, \{getRegName(sourceReg, size)}");
		}
		case AsmIR.Load(int valueReg, int pointerReg, int valueSize) -> {
			writeComment(STR."load \{valueReg} (\{valueSize}), @\{pointerReg}");

			writeIndented(STR."mov \{getRegName(valueReg, valueSize)}, \{getSizeWord(valueSize)} [\{getRegName(pointerReg, PTR_SIZE)}]");
		}
		case AsmIR.Store(int pointerReg, int valueReg, int valueSize) -> {
			writeComment(STR."store @\{pointerReg}, \{valueReg} (\{valueSize})");

			writeIndented(STR."mov \{getSizeWord(valueSize)} [\{getRegName(pointerReg, PTR_SIZE)}], \{getRegName(valueReg, valueSize)}");
		}
		case AsmIR.Ret() -> {
			writeRet();
		}
		case AsmIR.BinCommand(AsmIR.BinOperation operation, int reg1, int reg2) -> {
			writeBinCommand(operation, reg1, reg2);
		}
		case AsmIR.PrintInt(int size) -> {
			writePrintInt(size);
		}
		case AsmIR.Emit() -> {
			writeEmit();
		}
		case AsmIR.PrintString(int ptrReg, int sizeReg) -> {
			writePrintString(ptrReg, sizeReg);
		}
		case AsmIR.Mem() -> {
			writeComment("mem");
			writeIndented(STR."lea \{getRegName(AsmIRConverter.REG_0, PTR_SIZE)}, [mem]");
		}
		case AsmIR.Call(String name) -> {
			writeComment(name);
			writeIndented(STR."call \{LABEL_PREFIX}\{name}");
		}
		case AsmIR.PushVar(int sourceReg, Type type) -> {
			writeComment(STR."push var r\{sourceReg} (\{type})");
			final int size = getByteCountForPush(type);
			writeIndented(STR. "push \{ getRegName(sourceReg, size) }");
		}
		case AsmIR.LocalVarRead(int targetReg, Type type, TypeList offset) -> {
			writeComment(STR."read var r\{targetReg}, [\{offset} (\{type})]");
			final int offsetCount = getOffset(offset);
			final int size = getByteCountForPush(type);
			writeIndented(STR. "mov \{ getRegName(targetReg, size) }, [rsp+\{ offsetCount }]");
		}
		case AsmIR.LocalVarWrite(int sourceReg, Type type, TypeList offset) -> {
			writeComment(STR."write var [\{ offset } (\{type})], \{sourceReg}");
			final int offsetCount = getOffset(offset);
			final int size = getByteCountForPush(type);
			writeIndented(STR. "mov [rsp+\{ offsetCount }], \{ getRegName(sourceReg, size) }");
		}
		case AsmIR.DropVars(TypeList types) -> {
			writeComment(STR."drop vars \{types}");
			final int offset = getOffset(types);
			writeIndented(STR. "add rsp, \{offset}");
		}
		}
	}

	private void write(String text) throws IOException {
		writeLines(text, null);
	}

	private void writeIndented(String text) throws IOException {
		writeLines(text, INDENTATION);
	}

	private void writeLines(String text, @Nullable String leading) throws IOException {
		final String[] lines = text.split("\\r?\\n");
		for (String line : lines) {
			if (leading != null
			    && (line.length() > 0 || !leading.isBlank())) {
				writer.write(leading);
			}
			writer.write(line);
			writeNL();
		}
	}

	private void writeNL() throws IOException {
		writer.write(System.lineSeparator());
	}

	private void writeComment(String comment) throws IOException {
		writeIndented(STR."; -- \{comment} --");
	}

	private void writeLabel(String name) throws IOException {
		write(STR."\{name}:");
	}

	private void writeJump(@Nullable AsmIR.Condition condition, String target) throws IOException {
		if (condition == null) {
			writeComment(STR."jump \{target}");
			writeIndented(STR."jmp \{LABEL_PREFIX}\{target}");
			return;
		}

		writeComment(STR."jump \{condition} \{target}");
		writeIndented(switch (condition) {
			case z -> "jz ";
			case nz -> "jnz ";
			case lt -> "jl ";
			case le -> "jle ";
			case ge -> "jge ";
			case gt -> "jg ";
		} + LABEL_PREFIX + target);
	}

	private void writeRet() throws IOException {
		writeComment("ret");
		writeIndented("ret");
	}

	private void writeBinCommand(AsmIR.BinOperation operation, int reg1, int reg2) throws IOException {
		writeComment(STR."\{operation} \{reg1} \{reg2}");

		switch (operation) {
		case add -> writeIndented(STR."add \{getRegName(reg1, 2)}, \{getRegName(reg2, 2)}");
		case add_ptr -> {
			final String offsetReg = getRegName(reg2, 2);
			final String offset64Reg = getRegName(reg2, PTR_SIZE);
			writeIndented(STR."""
			           movsx \{offset64Reg}, \{offsetReg}
			           add   \{getRegName(reg1, PTR_SIZE)}, \{offset64Reg}""");
		}
		case sub -> writeIndented(STR."sub \{getRegName(reg1, 2)}, \{getRegName(reg2, 2)}");
		case imul -> // https://www.felixcloutier.com/x86/imul
				writeIndented(STR."imul \{getRegName(reg1, 2)}, \{getRegName(reg2, 2)}");
		case idiv -> {
			// https://www.felixcloutier.com/x86/idiv
			// (edx eax) / %reg -> eax
			// (edx eax) % %reg -> edx
			final String regName1 = getRegName(reg1, 2);
			final String regName2 = getRegName(reg2, 2);
			writeIndented(STR."""
					              mov dx, \{regName2}
					              xor eax, eax
					              mov ax, \{regName1}
					              xor ecx, ecx
					              mov cx, dx
					              xor edx, edx
					              idiv ecx
					              mov ecx, eax""");
		}
		case imod -> {
			// https://www.felixcloutier.com/x86/idiv
			// (edx eax) / %reg -> eax
			// (edx eax) % %reg -> edx
			final String regName1 = getRegName(reg1, 2);
			final String regName2 = getRegName(reg2, 2);
			writeIndented(STR."""
					              mov dx, \{regName2}
					              xor eax, eax
					              mov ax, \{regName1}
					              xor ecx, ecx
					              mov cx, dx
					              xor edx, edx
					              idiv ecx
					              mov ecx, edx""");
		}
		case and -> writeIndented(STR."and \{getRegName(reg1, 2)}, \{getRegName(reg2, 2)}");
		case or -> writeIndented(STR."or \{getRegName(reg1, 2)}, \{getRegName(reg2, 2)}");
		case xor -> writeIndented(STR."xor \{getRegName(reg1, 2)}, \{getRegName(reg2, 2)}");
		case shl -> {
			// https://www.felixcloutier.com/x86/sal:sar:shl:shr
			Utils.assertTrue(reg2 == 0, "source must be cl");
			writeIndented(STR."shl \{getRegName(reg1, 2)}, \{getRegName(reg2, 1)}");
		}
		case shr -> {
			// https://www.felixcloutier.com/x86/sal:sar:shl:shr
			Utils.assertTrue(reg2 == 0, "source must be cl");
			writeIndented(STR."shr \{getRegName(reg1, 2)}, \{getRegName(reg2, 1)}");
		}
		case boolTest -> writeIndented("test %s, %s".formatted(getRegName(reg1, 1),
		                                                       getRegName(reg2, 1)));
		case lt -> {
			final String regName1 = getRegName(reg1, 2);
			final String regName2 = getRegName(reg2, 2);
			writeIndented(STR."""
					              cmp   \{regName1}, \{regName2}
					              mov   \{regName1}, 0
					              mov   \{regName2}, 1
					              cmovl r\{regName1}, r\{regName2}""");
		}
		case le -> {
			final String regName1 = getRegName(reg1, 2);
			final String regName2 = getRegName(reg2, 2);
			writeIndented(STR."""
					              cmp    \{regName1}, \{regName2}
					              mov    \{regName1}, 0
					              mov    \{regName2}, 1
					              cmovle r\{regName1}, r\{regName2}""");
		}
		case eq -> {
			final String regName1 = getRegName(reg1, 2);
			final String regName2 = getRegName(reg2, 2);
			writeIndented(STR."""
					              cmp   \{regName1}, \{regName2}
					              mov   \{regName1}, 0
					              mov   \{regName2}, 1
					              cmove r\{regName1}, r\{regName2}""");
		}
		case neq -> {
			final String regName1 = getRegName(reg1, 2);
			final String regName2 = getRegName(reg2, 2);
			writeIndented(STR."""
					              cmp    \{regName1}, \{regName2}
					              mov    \{regName1}, 0
					              mov    \{regName2}, 1
					              cmovne r\{regName1}, r\{regName2}""");
		}
		case ge -> {
			final String regName1 = getRegName(reg1, 2);
			final String regName2 = getRegName(reg2, 2);
			writeIndented(STR."""
					              cmp    \{regName1}, \{regName2}
					              mov    \{regName1}, 0
					              mov    \{regName2}, 1
					              cmovge r\{regName1}, r\{regName2}""");
		}
		case gt -> {
			final String regName1 = getRegName(reg1, 2);
			final String regName2 = getRegName(reg2, 2);
			writeIndented(STR."""
					              cmp   \{regName1}, \{regName2}
					              mov   \{regName1}, 0
					              mov   \{regName2}, 1
					              cmovg r\{regName1}, r\{regName2}""");
		}
		default -> throw new IllegalStateException();
		}
	}

	private void writePrintInt(int size) throws IOException {
		writeComment("printInt r0(" + size + ")");

		if (size != 8) {
			writeIndented(STR."movsx rcx, \{getRegName(0, size)}");
		}

		final String labelPos = nextLocalLabel();
		writeIndented(STR."""
				              test   rcx, rcx
				              jns    \{labelPos}
				              neg    rcx
				              push   rcx
				                mov    cl, '-'
				                call   \{ EMIT}
				              pop    rcx
				              """);
		writeLabel(labelPos);
		writeIndented(STR."""
				              sub  rsp, 8
				                call \{PRINT_UINT}
				                mov  cl, ' '
				                call \{ EMIT}
				              add  rsp, 8
				              """);
	}

	private void writeEmit() throws IOException {
		writeComment("emit");
		// expects char in cl
		writeIndented(STR."""
				              sub rsp, 8
				                call \{ EMIT}
				              add rsp, 8
				              """);
	}

	private void writePrintString(int ptrReg, int sizeReg) throws IOException {
		writeComment(STR."printString r\{ ptrReg } (\{ sizeReg })");

		final String ptrRegName = getRegName(ptrReg, PTR_SIZE);
		final String sizeRegName = getRegName(sizeReg, 2);
		// expects ptr in rcx, size in rdx
		writeIndented(STR."""
				              movsx rdx, \{ sizeRegName}
				              mov rcx, \{ ptrRegName}
				              sub rsp, 8
				                call \{PRINT_STRING}
				              add rsp, 8
				              """);
	}

	private String nextLocalLabel() {
		return "." + (++labelIndex);
	}

	private int getByteCountForPush(Type type) {
		// x86 has no push for 8 bit register https://www.felixcloutier.com/x86/push
		return Math.max(2, type.getByteCount(PTR_SIZE));
	}

	private int getOffset(TypeList types) {
		int offset = 0;
		while (true) {
			final Type type = types.type();
			if (type == null) {
				return offset;
			}

			offset += getByteCountForPush(type);
			types = types.prev();
		}
	}

	@NotNull
	private static String getRegName(int reg, int size) {
		return switch (size) {
			case 1 -> switch (reg) {
				case AsmIRConverter.REG_0 -> "cl";
				case AsmIRConverter.REG_1 -> "al";
				case AsmIRConverter.REG_2 -> "bl";
				default -> throw new IllegalStateException("unsupported reg " + reg);
			};
			case 2 -> switch (reg) {
				case AsmIRConverter.REG_0 -> "cx";
				case AsmIRConverter.REG_1 -> "ax";
				case AsmIRConverter.REG_2 -> "bx";
				default -> throw new IllegalStateException("unsupported reg " + reg);
			};
			case 4 -> switch (reg) {
				case AsmIRConverter.REG_0 -> "ecx";
				case AsmIRConverter.REG_1 -> "eax";
				case AsmIRConverter.REG_2 -> "ebx";
				default -> throw new IllegalStateException("unsupported reg " + reg);
			};
			case 8 -> switch (reg) {
				case AsmIRConverter.REG_0 -> "rcx";
				case AsmIRConverter.REG_1 -> "rax";
				case AsmIRConverter.REG_2 -> "rbx";
				default -> throw new IllegalStateException("unsupported reg " + reg);
			};
			default -> throw new IllegalStateException("unsupported size " + size);
		};
	}

	@NotNull
	private static String getSizeWord(int valueSize) {
		return switch (valueSize) {
			case 1 -> "byte";
			case 2 -> "word";
			default -> throw new IllegalStateException("unsupported size " + valueSize);
		};
	}
}
