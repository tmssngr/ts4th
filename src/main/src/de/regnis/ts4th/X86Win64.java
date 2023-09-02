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
	private final AsmIR.Visitor<IOException> visitor;

	private int labelIndex;

	public X86Win64(Writer writer) {
		this.writer = writer;
		visitor = new AsmIR.Visitor<>() {
			@Override
			public IOException label(String name) {
				try {
					writeLabel(LABEL_PREFIX + name);
					return null;
				}
				catch (IOException e) {
					return e;
				}
			}

			@Override
			public IOException literal(int value) {
				try {
					writeComment("literal " + value);
					writeIndented("mov cx, " + value);
					return null;
				}
				catch (IOException e) {
					return e;
				}
			}

			@Override
			public IOException literal(boolean value) {
				try {
					writeComment("literal " + value);
					writeIndented("mov cx, " + (value ? 1 : 0));
					return null;
				}
				catch (IOException e) {
					return e;
				}
			}

			@Override
			public IOException stringLiteral(int constantIndex) {
				try {
					writeComment("string literal " + constantIndex);
					writeIndented("lea rcx, [%s]".formatted(STRING_PREFIX + constantIndex));
					return null;
				}
				catch (IOException e) {
					return e;
				}
			}

			@Override
			public IOException jump(AsmIR.Condition condition, String target) {
				try {
					writeJump(condition, target);
					return null;
				}
				catch (IOException e) {
					return e;
				}
			}

			@Override
			public IOException jump(String target) {
				try {
					writeJump(null, target);
					return null;
				}
				catch (IOException e) {
					return e;
				}
			}

			@Override
			public IOException push(int reg, int size) {
				try {
					writeComment("push " + reg + " (" + size + ")");

					final String regName = getRegName(reg, size);
					writeIndented("""
							              sub %s, %d
							              mov [%s], %s
							              """.formatted(REG_DSP, size, REG_DSP, regName));
					return null;
				}
				catch (IOException e) {
					return e;
				}
			}

			@Override
			public IOException pop(int reg, int size) {
				try {
					writeComment("pop " + reg + " (" + size + ")");

					final String regName = getRegName(reg, size);
					writeIndented("""
							              mov %s, [%s]
							              add %s, %d
							              """.formatted(regName, REG_DSP, REG_DSP, size));
					return null;
				}
				catch (IOException e) {
					return e;
				}
			}

			@Override
			public IOException load(int valueReg, int valueSize, int pointerReg) {
				try {
					writeComment("load " + valueReg + " (" + valueSize + "), @" + pointerReg);

					writeIndented("mov %s, %s [%s]".formatted(getRegName(valueReg, valueSize),
					                                          getSizeWord(valueSize),
					                                          getRegName(pointerReg, 8)));
					return null;
				}
				catch (IOException e) {
					return e;
				}
			}

			@Override
			public IOException store(int pointerReg, int valueReg, int valueSize) {
				try {
					writeComment("store @" + pointerReg + ", " + valueReg + " (" + valueSize + ")");

					writeIndented("mov %s [%s], %s".formatted(getSizeWord(valueSize),
					                                          getRegName(pointerReg, 8),
					                                          getRegName(valueReg, valueSize)));
					return null;
				}
				catch (IOException e) {
					return e;
				}
			}

			@Override
			public IOException ret() {
				try {
					writeRet();
					return null;
				}
				catch (IOException e) {
					return e;
				}
			}

			@Override
			public IOException command(String name, int reg1, int reg2) {
				try {
					writeCommand(name, reg1, reg2);
					return null;
				}
				catch (IOException e) {
					return e;
				}
			}
		};
	}

	public void write(List<AsmIRFunction> functions, AsmIRStringLiterals stringLiterals) throws IOException {
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

		for (AsmIRFunction function : functions) {
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

		final List<byte[]> constants = stringLiterals.getConstants();
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
			final IOException exception = instruction.visit(visitor);
			if (exception != null) {
				throw exception;
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

	private void writeCommand(String name, int reg1, int reg2) throws IOException {
		writeComment(name + " " + reg1 + " " + reg2);
		switch (name) {
		case AsmIRConverter.CMD_ADD -> {
			writeIndented("add %s, %s".formatted(getRegName(reg1, 2),
			                                     getRegName(reg2, 2)));
		}
		case AsmIRConverter.CMD_ADD_PTR -> {
			writeIndented("add %s, %s".formatted(getRegName(reg1, 8),
			                                     getRegName(reg2, 2)));
		}
		case AsmIRConverter.CMD_SUB -> {
			writeIndented("sub %s, %s".formatted(getRegName(reg1, 2),
			                                     getRegName(reg2, 2)));
		}
		case AsmIRConverter.CMD_IMUL -> {
			// https://www.felixcloutier.com/x86/imul
			writeIndented("imul %s, %s".formatted(getRegName(reg1, 2),
			                                      getRegName(reg2, 2)));
		}
		case AsmIRConverter.CMD_IDIV -> {
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
		case AsmIRConverter.CMD_IMOD -> {
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
		case AsmIRConverter.CMD_AND -> {
			writeIndented("and %s, %s".formatted(getRegName(reg1, 2),
			                                     getRegName(reg2, 2)));
		}
		case AsmIRConverter.CMD_SHR -> {
			writeIndented("shr %s, %s".formatted(getRegName(reg1, 2),
			                                     getRegName(reg2, 1)));
		}
		case AsmIRConverter.CMD_TEST -> {
			writeIndented("test %s, %s".formatted(getRegName(reg1, 1),
			                                      getRegName(reg2, 1)));
		}
		case AsmIRConverter.CMD_LT -> {
			final String regName1 = getRegName(reg1, 2);
			final String regName2 = getRegName(reg2, 2);
			writeIndented("cmp %s, %s".formatted(regName1, regName2));
			writeIndented("mov %s, 0".formatted(regName1));
			writeIndented("mov %s, 1".formatted(regName2));
			writeIndented("cmovl r%s, r%s".formatted(regName1, regName2));
		}
		case AsmIRConverter.CMD_LE -> {
			final String regName1 = getRegName(reg1, 2);
			final String regName2 = getRegName(reg2, 2);
			writeIndented("cmp %s, %s".formatted(regName1, regName2));
			writeIndented("mov %s, 0".formatted(regName1));
			writeIndented("mov %s, 1".formatted(regName2));
			writeIndented("cmovle r%s, r%s".formatted(regName1, regName2));
		}
		case AsmIRConverter.CMD_EQ -> {
			final String regName1 = getRegName(reg1, 2);
			final String regName2 = getRegName(reg2, 2);
			writeIndented("cmp %s, %s".formatted(regName1, regName2));
			writeIndented("mov %s, 0".formatted(regName1));
			writeIndented("mov %s, 1".formatted(regName2));
			writeIndented("cmove r%s, r%s".formatted(regName1, regName2));
		}
		case AsmIRConverter.CMD_NE -> {
			final String regName1 = getRegName(reg1, 2);
			final String regName2 = getRegName(reg2, 2);
			writeIndented("cmp %s, %s".formatted(regName1, regName2));
			writeIndented("mov %s, 0".formatted(regName1));
			writeIndented("mov %s, 1".formatted(regName2));
			writeIndented("cmovne r%s, r%s".formatted(regName1, regName2));
		}
		case AsmIRConverter.CMD_GE -> {
			final String regName1 = getRegName(reg1, 2);
			final String regName2 = getRegName(reg2, 2);
			writeIndented("cmp %s, %s".formatted(regName1, regName2));
			writeIndented("mov %s, 0".formatted(regName1));
			writeIndented("mov %s, 1".formatted(regName2));
			writeIndented("cmovge r%s, r%s".formatted(regName1, regName2));
		}
		case AsmIRConverter.CMD_GT -> {
			final String regName1 = getRegName(reg1, 2);
			final String regName2 = getRegName(reg2, 2);
			writeIndented("cmp %s, %s".formatted(regName1, regName2));
			writeIndented("mov %s, 0".formatted(regName1));
			writeIndented("mov %s, 1".formatted(regName2));
			writeIndented("cmovg r%s, r%s".formatted(regName1, regName2));
		}
		case AsmIRConverter.CMD_MEM -> {
			writeIndented("lea %s, [mem]".formatted(getRegName(reg1, 8)));
		}
		case AsmIRConverter.CMD_PRINT -> {
			Utils.assertTrue(reg1 == AsmIRConverter.REG_0);
			if (reg2 != 8) {
				writeIndented("""
						              movsx rcx, %s""".formatted(getRegName(reg1, reg2)));
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
		case AsmIRConverter.CMD_PRINT_STRING -> {
			final String ptrReg = getRegName(reg1, 8);
			final String sizeReg = getRegName(reg2, 2);
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
		default -> {
			writeIndented("call " + LABEL_PREFIX + name);
		}
		}
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
