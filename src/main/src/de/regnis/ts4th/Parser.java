package de.regnis.ts4th;

import de.regnis.antlr.ts4th.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;

/**
 * @author Thomas Singer
 */
public final class Parser extends TS4thBaseVisitor<Object> {

	public static List<Function> parseFile(Path file) throws ParseCancellationException {
		final FileIncludeHandler rootIncludeHandler = new FileIncludeHandler(file.toAbsolutePath());
		return rootIncludeHandler.parse();
	}

	public static List<Function> parseString(String s) {
		return parseString(s, unused -> {
			throw new ParseCancellationException("can't include");
		});
	}

	public static List<Function> parseString(String s, IncludeHandler includeHandler) {
		if (false) {
			showLexerTokens(CharStreams.fromString(s));
		}
		return parse(CharStreams.fromString(s), includeHandler);
	}

	private final IncludeHandler includeHandler;
	private String continueLabel;
	private String breakLabel;
	private int index;
	private Parser(IncludeHandler includeHandler) {
		this.includeHandler = includeHandler;
	}

	@Override
	public List<Function> visitRoot(TS4thParser.RootContext ctx) {
		index = 0;
		breakLabel = null;
		continueLabel = null;

		final List<Function> functions = new ArrayList<>();
		for (TS4thParser.RootItemContext rootItemContext : ctx.rootItem()) {
			final List<Function> rootItemFunctions = visitRootItem(rootItemContext);
			functions.addAll(rootItemFunctions);
		}
		return functions;
	}

	@Override
	public List<Function> visitRootItem(TS4thParser.RootItemContext ctx) {
		final TS4thParser.DeclarationContext declaration = ctx.declaration();
		if (declaration != null) {
			return List.of(visitDeclaration(declaration));
		}

		return visitInclude(ctx.include());
	}

	@Override
	public List<Function> visitInclude(TS4thParser.IncludeContext ctx) {
		final String path = parseStringLiteral(ctx.String());
		return includeHandler.include(path);
	}

	@Override
	public Function visitDeclaration(TS4thParser.DeclarationContext ctx) {
		final String name = ctx.name.getText();
		final boolean inline = ctx.Inline() != null;
		final TypeList beforeTypes = visitTypeList(ctx.beforeTypes);
		final TypeList afterTypes = visitTypeList(ctx.afterTypes);
		final List<Instruction> commands = visitInstructions(ctx.instructions());
		commands.add(Instruction.ret());
		return new Function(name, beforeTypes, afterTypes, inline, commands);
	}

	@Override
	public TypeList visitTypeList(TS4thParser.TypeListContext ctx) {
		TypeList typeList = TypeList.EMPTY;
		for (TerminalNode node : ctx.Identifier()) {
			final String text = node.getText();
			if (text.equals("int")) {
				typeList = typeList.append(Type.Int);
				continue;
			}

			if (text.equals("ptr")) {
				typeList = typeList.append(Type.Ptr);
				continue;
			}

			if (text.equals("bool")) {
				typeList = typeList.append(Type.Bool);
				continue;
			}

			throw new ParseCancellationException("%1$d:%2$d invalid type %3$s".formatted(node.getSymbol().getLine(), node.getSymbol().getCharPositionInLine(), text));
		}
		return typeList;
	}

	@Override
	public List<Instruction> visitInstructions(TS4thParser.InstructionsContext ctx) {
		final List<Instruction> instructions = new ArrayList<>();
		for (var instruction : ctx.instruction()) {
			//noinspection unchecked
			instructions.addAll((List<Instruction>)visit(instruction));
		}
		return instructions;
	}

	@Override
	public List<Instruction> visitNumber(TS4thParser.NumberContext ctx) {
		final TerminalNode node = ctx.Number();
		final String text = node.getText();
		final int value;
		if (text.startsWith("0x")) {
			value = Integer.parseInt(text.substring(2), 16);
		}
		else if (text.length() >= 3 && text.startsWith("'") && text.endsWith("'")) {
			final String unescape = unescape(text.substring(1, text.length() - 1));
			if (unescape.length() != 1) {
				throw new ParseCancellationException("%1$d:%2$d invalid char %3$s".formatted(node.getSymbol().getLine(), node.getSymbol().getCharPositionInLine(), text));
			}
			value = unescape.charAt(0);
		}
		else {
			value = Integer.parseInt(text);
		}
		return List.of(Instruction.literal(value));
	}

	@Override
	public Object visitString(TS4thParser.StringContext ctx) {
		final String value = parseStringLiteral(ctx.String());
		return List.of(Instruction.literal(value));
	}

	private String parseStringLiteral(TerminalNode node) {
		final String text = node.getText();
		final String raw = text.substring(1, text.length() - 1);
		return unescape(raw);
	}

	@Override
	public List<Instruction> visitTrue(TS4thParser.TrueContext ctx) {
		return List.of(Instruction.literal(true));
	}

	@Override
	public List<Instruction> visitFalse(TS4thParser.FalseContext ctx) {
		return List.of(Instruction.literal(false));
	}

	@Override
	public List<Instruction> visitIdentifier(TS4thParser.IdentifierContext ctx) {
		final Token token = ctx.Identifier().getSymbol();
		return List.of(Instruction.command(ctx.getText(), new Location(token.getLine(), token.getCharPositionInLine())));
	}

	@Override
	public List<Instruction> visitIf(TS4thParser.IfContext ctx) {
		index++;
		final String labelIf = "if_" + index;
		final String labelEnd = "endif_" + index;

		final List<Instruction> instructions = new ArrayList<>();
		instructions.add(Instruction.branch(labelIf, labelEnd));

		instructions.add(Instruction.label(labelIf));
		instructions.addAll(visitInstructions(ctx.instructions()));
		addJumpIfPrevIsNoJumpOrBranch(labelEnd, instructions);

		instructions.add(Instruction.label(labelEnd));
		return instructions;
	}

	@Override
	public List<Instruction> visitIfElse(TS4thParser.IfElseContext ctx) {
		index++;
		final String labelIf = "if_" + index;
		final String labelElse = "else_" + index;
		final String labelEnd = "endif_" + index;

		final List<Instruction> instructions = new ArrayList<>();
		instructions.add(Instruction.branch(labelIf, labelElse));

		instructions.add(Instruction.label(labelIf));
		instructions.addAll(visitInstructions(ctx.ifInstructions));
		addJumpIfPrevIsNoJumpOrBranch(labelEnd, instructions);

		instructions.add(Instruction.label(labelElse));
		instructions.addAll(visitInstructions(ctx.elseInstructions));
		addJumpIfPrevIsNoJumpOrBranch(labelEnd, instructions);

		instructions.add(Instruction.label(labelEnd));
		return instructions;
	}

	@Override
	public List<Instruction> visitWhile(TS4thParser.WhileContext ctx) {
		index++;
		final String prevBreakLabel = breakLabel;
		final String prevContinueLabel = continueLabel;
		final String labelWhile = "while_" + index;
		final String labelWhileBody = "whilebody_" + index;
		final String labelWhileEnd = "endwhile_" + index;

		try {
			final List<Instruction> instructions = new ArrayList<>();
			instructions.add(Instruction.label(labelWhile));

			breakLabel = null;
			continueLabel = null;

			instructions.addAll(visitInstructions(ctx.condition));
			instructions.add(Instruction.branch(labelWhileBody, labelWhileEnd));

			instructions.add(Instruction.label(labelWhileBody));

			breakLabel = labelWhileEnd;
			continueLabel = labelWhile;

			instructions.addAll(visitInstructions(ctx.body));
			instructions.add(Instruction.jump(labelWhile));

			instructions.add(Instruction.label(labelWhileEnd));
			return instructions;
		}
		finally {
			breakLabel = prevBreakLabel;
			continueLabel = prevContinueLabel;
		}
	}

	@Override
	public List<Instruction> visitBreak(TS4thParser.BreakContext ctx) {
		if (breakLabel == null) {
			final Token token = ctx.Break().getSymbol();
			throw new ParseCancellationException("%1$d:%2$d break only can be used inside while-do-end between do and end".formatted(token.getLine(), token.getCharPositionInLine()));
		}
		return List.of(Instruction.jump(breakLabel));
	}

	@Override
	public List<Instruction> visitContinue(TS4thParser.ContinueContext ctx) {
		if (continueLabel == null) {
			final Token token = ctx.Continue().getSymbol();
			throw new ParseCancellationException("%1$d:%2$d continue only can be used inside while-do-end between do and end".formatted(token.getLine(), token.getCharPositionInLine()));
		}
		return List.of(Instruction.jump(continueLabel));
	}

	private void addJumpIfPrevIsNoJumpOrBranch(String target, List<Instruction> instructions) {
		if (instructions.size() > 0) {
			final Instruction lastInstruction = instructions.get(instructions.size() - 1);
			if (lastInstruction.isJump() || lastInstruction.isLabel()) {
				return;
			}
		}
		instructions.add(Instruction.jump(target));
	}

	private static String unescape(String s) {
		final StringBuilder buffer = new StringBuilder();
		boolean escaped = false;
		for (int i = 0; i < s.length(); i++) {
			final char chr = s.charAt(i);
			if (escaped) {
				buffer.append(switch (chr) {
					case '0' -> '\0';
					case 't' -> '\t';
					case 'n' -> '\n';
					case 'r' -> '\r';
					default -> chr;
				});
				escaped = false;
			}
			else if (chr == '\\') {
				escaped = true;
			}
			else {
				buffer.append(chr);
			}
		}
		Utils.assertTrue(!escaped);
		return buffer.toString();
	}

	private static List<Function> parse(CharStream charStream, IncludeHandler includeHandler) {
		final TS4thLexer lexer = new TS4thLexer(charStream);
		final TokenStream tokenStream = new CommonTokenStream(lexer);

		final TS4thParser parser = new TS4thParser(tokenStream);
		parser.addErrorListener(new BaseErrorListener() {
			@Override
			public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
				throw new ParseCancellationException("line " + line + ":" + charPositionInLine + " " + msg);
			}
		});

		final TS4thParser.RootContext rootContext = parser.root();
		final Parser astFactory = new Parser(includeHandler);
		return astFactory.visitRoot(rootContext);
	}

	private static void showLexerTokens(CharStream charStream) {
		final TS4thLexer lexer = new TS4thLexer(charStream);
		while (true) {
			final Token token = lexer.nextToken();
			if (token.getType() < 0) {
				break;
			}

			final String name = TS4thLexer.VOCABULARY.getDisplayName(token.getType());
			System.out.println(token.getLine() + ":" + token.getCharPositionInLine() + " " + name + ": " + token.getText());
		}
	}

	public interface IncludeHandler {
		List<Function> include(String fileString);
	}

	private static class FileIncludeHandler implements IncludeHandler {
		private final Path file;
		private final Set<Path> files;

		public FileIncludeHandler(Path file) {
			this.file = file;
			files = new HashSet<>();
			files.add(file);
		}

		private FileIncludeHandler(Path file, Set<Path> files) {
			this.file = file;
			this.files = files;
		}

		@Override
		public List<Function> include(String fileString) {
			final Path includeFile = file.resolveSibling(fileString);
			if (!files.add(includeFile)) {
				return List.of();
			}

			return new FileIncludeHandler(includeFile, files)
					.parse();
		}

		public List<Function> parse() throws ParseCancellationException {
			try {
				if (false) {
					try (InputStream stream = Files.newInputStream(file)) {
						showLexerTokens(CharStreams.fromStream(stream));
					}
				}

				try (InputStream stream = Files.newInputStream(file)) {
					return Parser.parse(CharStreams.fromStream(stream), this);
				}
			}
			catch (IOException ex) {
				throw new ParseCancellationException("Failed to parse file " + file, ex);
			}
		}
	}
}
