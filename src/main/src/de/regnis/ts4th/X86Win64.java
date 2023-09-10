package de.regnis.ts4th;

import java.io.*;
import java.util.*;

import org.jetbrains.annotations.*;

/**
 * @author Thomas Singer
 */
public class X86Win64 {

	private static final String INDENTATION = "        ";
	// data stack pointer
	private static final String REG_DSP = "r15";
	private static final String LABEL_PREFIX = "tsf_";
	private static final String LABEL_PREFIX_BI = "tsfbi_";
	private static final String STRING_PREFIX = "string_";
	private static final String PRINT_STRING = LABEL_PREFIX_BI + "printString";
	private static final String PRINT_CHAR = LABEL_PREFIX_BI + "printChar";
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
		writeIndented("""
				              mov %s, rsp
				              sub rsp, STACK_SIZE
				              """.formatted(REG_DSP));
		writeIndented("""
				              sub rsp, 8
				                call %s
				              add rsp, 8""".formatted(LABEL_PREFIX + "main"));
		writeIndented("""
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

		final List<byte[]> constants = program.stringLiterals().getConstants();
		if (constants.size() > 0) {
			write("""
					      ; string constants
					      section '.data' data readable""");
			for (int i = 0; i < constants.size(); i++) {
				final String encoded = encode(constants.get(i));
				writeIndented(STRING_PREFIX + i + " db " + encoded);
			}
		}
		writeNL();

		write("""
				      section '.data' data readable writeable

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
		writeLabel(PRINT_CHAR);
		writeIndented("""
				              push rcx ; = sub rsp, 8
				                mov rcx, rsp
				                mov rdx, 1
				                call %s
				              pop rcx
				              ret""".formatted(PRINT_STRING));
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
		writeIndented("""
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
				                call   %s
				              ;add    rsp, 8
				              leave ; Set SP to BP, then pop BP
				              ret
				              """.formatted(PRINT_STRING));
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
		if (instruction instanceof AsmIR.Label l) {
			writeLabel(LABEL_PREFIX + l.name());
			return;
		}

		if (instruction instanceof AsmIR.IntLiteral l) {
			writeComment("literal " + l.value());
			writeIndented("mov cx, " + l.value());
			return;
		}

		if (instruction instanceof AsmIR.BoolLiteral l) {
			writeComment("literal " + l.value());
			writeIndented("mov cx, " + (l.value() ? 1 : 0));
			return;
		}

		if (instruction instanceof AsmIR.StringLiteral l) {
			writeComment("string literal " + l.constantIndex());
			writeIndented("lea rcx, [%s]".formatted(STRING_PREFIX + l.constantIndex()));
			return;
		}

		if (instruction instanceof AsmIR.Jump j) {
			writeJump(j.condition(), j.target());
			return;
		}

		if (instruction instanceof AsmIR.Push p) {
			final int reg = p.reg();
			final int size = p.size();
			writeComment("push " + reg + " (" + size + ")");

			final String regName = getRegName(reg, size);
			writeIndented("""
					              sub %s, %d
					              mov [%s], %s
					              """.formatted(REG_DSP, size, REG_DSP, regName));
			return;
		}

		if (instruction instanceof AsmIR.Pop p) {
			final int reg = p.reg();
			final int size = p.size();
			writeComment("pop " + reg + " (" + size + ")");

			final String regName = getRegName(reg, size);
			writeIndented("""
					              mov %s, [%s]
					              add %s, %d
					              """.formatted(regName, REG_DSP, REG_DSP, size));
			return;
		}

		if (instruction instanceof AsmIR.Load l) {
			final int valueReg = l.valueReg();
			final int valueSize = l.valueSize();
			final int pointerReg = l.pointerReg();
			writeComment("load " + valueReg + " (" + valueSize + "), @" + pointerReg);

			writeIndented("mov %s, %s [%s]".formatted(getRegName(valueReg, valueSize),
			                                          getSizeWord(valueSize),
			                                          getRegName(pointerReg, 8)));
			return;
		}

		if (instruction instanceof AsmIR.Store s) {
			final int pointerReg = s.pointerReg();
			final int valueReg = s.valueReg();
			final int valueSize = s.valueSize();
			writeComment("store @" + pointerReg + ", " + valueReg + " (" + valueSize + ")");

			writeIndented("mov %s [%s], %s".formatted(getSizeWord(valueSize),
			                                          getRegName(pointerReg, 8),
			                                          getRegName(valueReg, valueSize)));
			return;
		}

		if (instruction instanceof AsmIR.Ret) {
			writeRet();
			return;
		}

		if (instruction instanceof AsmIR.BinCommand c) {
			writeBinCommand(c.operation(), c.reg1(), c.reg2());
			return;
		}

		if (instruction instanceof AsmIR.PrintInt p) {
			writePrintInt(p);
			return;
		}

		if (instruction instanceof AsmIR.PrintString p) {
			writePrintString(p);
			return;
		}

		if (instruction instanceof AsmIR.Mem) {
			writeComment("mem");
			writeIndented("lea %s, [mem]".formatted(getRegName(AsmIRConverter.REG_0, 8)));
			return;
		}

		if (instruction instanceof AsmIR.Call c) {
			writeComment(c.name());
			writeIndented("call " + LABEL_PREFIX + c.name());
			return;
		}

		throw new IllegalStateException();
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
		writeIndented("; -- " + comment + " --");
	}

	private void writeLabel(String name) throws IOException {
		write(name + ":");
	}

	private void writeJump(@Nullable AsmIR.Condition condition, String target) throws IOException {
		if (condition == null) {
			writeComment("jump " + target);
			writeIndented("jmp " + LABEL_PREFIX + target);
			return;
		}

		writeComment("jump " + condition + " " + target);
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
		writeComment(operation + " " + reg1 + " " + reg2);

		switch (operation) {
		case add -> writeIndented("add %s, %s".formatted(getRegName(reg1, 2),
		                                                 getRegName(reg2, 2)));
		case add_ptr -> {
			final String offsetReg = getRegName(reg2, 2);
			final String offset64Reg = getRegName(reg2, 8);
			writeIndented("movsx %s, %s".formatted(offset64Reg, offsetReg));
			writeIndented("add   %s, %s".formatted(getRegName(reg1, 8), offset64Reg));
		}
		case sub -> writeIndented("sub %s, %s".formatted(getRegName(reg1, 2),
		                                                 getRegName(reg2, 2)));
		case imul -> // https://www.felixcloutier.com/x86/imul
				writeIndented("imul %s, %s".formatted(getRegName(reg1, 2),
				                                      getRegName(reg2, 2)));
		case idiv -> {
			// https://www.felixcloutier.com/x86/idiv
			// (edx eax) / %reg -> eax
			// (edx eax) % %reg -> edx
			final String regName1 = getRegName(reg1, 2);
			final String regName2 = getRegName(reg2, 2);
			writeIndented("mov dx, %s".formatted(regName2));
			writeIndented("""
					              xor eax, eax
					              mov ax, %s
					              xor ecx, ecx
					              mov cx, dx
					              xor edx, edx
					              idiv ecx
					              mov ecx, eax""".formatted(regName1));
		}
		case imod -> {
			// https://www.felixcloutier.com/x86/idiv
			// (edx eax) / %reg -> eax
			// (edx eax) % %reg -> edx
			final String regName1 = getRegName(reg1, 2);
			final String regName2 = getRegName(reg2, 2);
			writeIndented("mov dx, %s".formatted(regName2));
			writeIndented("""
					              xor eax, eax
					              mov ax, %s
					              xor ecx, ecx
					              mov cx, dx
					              xor edx, edx
					              idiv ecx
					              mov ecx, edx""".formatted(regName1));
		}
		case and -> writeIndented("and %s, %s".formatted(getRegName(reg1, 2),
		                                                 getRegName(reg2, 2)));
		case or -> writeIndented("or %s, %s".formatted(getRegName(reg1, 2),
		                                               getRegName(reg2, 2)));
		case xor -> writeIndented("xor %s, %s".formatted(getRegName(reg1, 2),
		                                                 getRegName(reg2, 2)));
		case shl -> writeIndented("shl %s, %s".formatted(getRegName(reg1, 2),
		                                                 getRegName(reg2, 1)));
		case shr -> writeIndented("shr %s, %s".formatted(getRegName(reg1, 2),
		                                                 getRegName(reg2, 1)));
		case boolTest -> writeIndented("test %s, %s".formatted(getRegName(reg1, 1),
		                                                       getRegName(reg2, 1)));
		case lt -> {
			final String regName1 = getRegName(reg1, 2);
			final String regName2 = getRegName(reg2, 2);
			writeIndented("cmp %s, %s".formatted(regName1, regName2));
			writeIndented("mov %s, 0".formatted(regName1));
			writeIndented("mov %s, 1".formatted(regName2));
			writeIndented("cmovl r%s, r%s".formatted(regName1, regName2));
		}
		case le -> {
			final String regName1 = getRegName(reg1, 2);
			final String regName2 = getRegName(reg2, 2);
			writeIndented("cmp %s, %s".formatted(regName1, regName2));
			writeIndented("mov %s, 0".formatted(regName1));
			writeIndented("mov %s, 1".formatted(regName2));
			writeIndented("cmovle r%s, r%s".formatted(regName1, regName2));
		}
		case eq -> {
			final String regName1 = getRegName(reg1, 2);
			final String regName2 = getRegName(reg2, 2);
			writeIndented("cmp %s, %s".formatted(regName1, regName2));
			writeIndented("mov %s, 0".formatted(regName1));
			writeIndented("mov %s, 1".formatted(regName2));
			writeIndented("cmove r%s, r%s".formatted(regName1, regName2));
		}
		case neq -> {
			final String regName1 = getRegName(reg1, 2);
			final String regName2 = getRegName(reg2, 2);
			writeIndented("cmp %s, %s".formatted(regName1, regName2));
			writeIndented("mov %s, 0".formatted(regName1));
			writeIndented("mov %s, 1".formatted(regName2));
			writeIndented("cmovne r%s, r%s".formatted(regName1, regName2));
		}
		case ge -> {
			final String regName1 = getRegName(reg1, 2);
			final String regName2 = getRegName(reg2, 2);
			writeIndented("cmp %s, %s".formatted(regName1, regName2));
			writeIndented("mov %s, 0".formatted(regName1));
			writeIndented("mov %s, 1".formatted(regName2));
			writeIndented("cmovge r%s, r%s".formatted(regName1, regName2));
		}
		case gt -> {
			final String regName1 = getRegName(reg1, 2);
			final String regName2 = getRegName(reg2, 2);
			writeIndented("cmp %s, %s".formatted(regName1, regName2));
			writeIndented("mov %s, 0".formatted(regName1));
			writeIndented("mov %s, 1".formatted(regName2));
			writeIndented("cmovg r%s, r%s".formatted(regName1, regName2));
		}
		default -> throw new IllegalStateException();
		}
	}

	private void writePrintInt(AsmIR.PrintInt c) throws IOException {
		writeComment(c.toString());

		final int size = c.size();
		if (size != 8) {
			writeIndented("""
					              movsx rcx, %s""".formatted(getRegName(0, size)));
		}

		final String labelPos = nextLocalLabel();
		writeIndented("""
				              test   rcx, rcx
				              jns    %s
				              neg    rcx
				              push   rcx
				                mov    cl, '-'
				                call   %s
				              pop    rcx
				              """.formatted(labelPos, PRINT_CHAR));
		writeLabel(labelPos);
		writeIndented("""
				              sub  rsp, 8
				                call %s
				              """.formatted(PRINT_UINT));
		writeIndented("""
				                mov  cl, ' '
				                call %s
				              add  rsp, 8
				              """.formatted(PRINT_CHAR));
	}

	private void writePrintString(AsmIR.PrintString p) throws IOException {
		writeComment(p.toString());
		final String ptrReg = getRegName(p.ptrReg(), 8);
		final String sizeReg = getRegName(p.sizeReg(), 2);
		writeIndented("""
				              movsx rdx, %s""".formatted(sizeReg));
		writeIndented("""
				              mov rcx, %s""".formatted(ptrReg));
		// expects ptr in rcx, size in rdx
		writeIndented("""
				              sub rsp, 8
				                call %s
				              add rsp, 8""".formatted(PRINT_STRING));
	}

	private String nextLocalLabel() {
		return "." + (++labelIndex);
	}

	@NotNull
	private static String getRegName(int reg, int size) {
		return switch (size) {
			case 1 -> {
				if (reg != AsmIRConverter.REG_0) {
					throw new IllegalStateException("unsupported reg " + reg);
				}
				yield "cl";
			}
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
