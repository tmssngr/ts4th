package de.regnis.ts4th;

import de.regnis.antlr.ts4th.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import org.jetbrains.annotations.*;

/**
 * @author Thomas Singer
 */
public final class Parser extends TS4thBaseVisitor<Object> {

	public static List<Declaration> parseFile(Path file) throws ParseCancellationException {
		final FileIncludeHandler rootIncludeHandler = new FileIncludeHandler(file.toAbsolutePath());
		return rootIncludeHandler.parse();
	}

	public static List<Declaration> parseString(String s) {
		return parseString(s, unused -> {
			throw new ParseCancellationException("can't include");
		});
	}

	public static List<Declaration> parseString(String s, IncludeHandler includeHandler) {
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
	public List<Declaration> visitRoot(TS4thParser.RootContext ctx) {
		index = 0;
		breakLabel = null;
		continueLabel = null;

		final List<Declaration> declarations = new ArrayList<>();
		for (TS4thParser.RootItemContext rootItemContext : ctx.rootItem()) {
			final List<Declaration> rootItemFunctions = visitRootItem(rootItemContext);
			declarations.addAll(rootItemFunctions);
		}
		return declarations;
	}

	@Override
	public List<Declaration> visitRootItem(TS4thParser.RootItemContext ctx) {
		final TS4thParser.IncludeContext include = ctx.include();
		if (include != null) {
			return visitInclude(include);
		}

		final TS4thParser.ConstDeclarationContext constDeclaration = ctx.constDeclaration();
		if (constDeclaration != null) {
			return List.of(visitConstDeclaration(constDeclaration));
		}

		final TS4thParser.VarDeclarationContext varDeclaration = ctx.varDeclaration();
		if (varDeclaration != null) {
			return List.of(visitVarDeclaration(varDeclaration));
		}

		final TS4thParser.FuncDeclarationContext funcDeclaration = ctx.funcDeclaration();
		if (funcDeclaration != null) {
			return List.of(visitFuncDeclaration(funcDeclaration));
		}

		throw new ParseCancellationException("not implemented");
	}

	@Override
	public List<Declaration> visitInclude(TS4thParser.IncludeContext ctx) {
		final String path = parseStringLiteral(ctx.String());
		return includeHandler.include(path);
	}

	@Override
	public ConstDeclaration visitConstDeclaration(TS4thParser.ConstDeclarationContext ctx) {
		final String name = ctx.name.getText();
		final List<Instruction> instructions = visitInstructions(ctx.instructions());
		final Location location = createLocation(ctx.name);
		return new ConstDeclaration(location, name, instructions);
	}

	@Override
	public VarDeclaration visitVarDeclaration(TS4thParser.VarDeclarationContext ctx) {
		final String name = ctx.name.getText();
		final List<Instruction> instructions = visitInstructions(ctx.instructions());
		final Location location = createLocation(ctx.name);
		return new VarDeclaration(location, name, instructions);
	}

	@Override
	public Function visitFuncDeclaration(TS4thParser.FuncDeclarationContext ctx) {
		final String name = ctx.name.getText();
		final boolean inline = ctx.Inline() != null;
		final TypeList beforeTypes = visitTypeList(ctx.beforeTypes);
		final TypeList afterTypes = visitTypeList(ctx.afterTypes);
		final List<Instruction> instructions = visitInstructions(ctx.instructions());
		instructions.add(new Instruction.Ret());
		final Location locationStart = createLocation(ctx.name);
		final Location locationEnd = createLocation(ctx.End());
		return new Function(locationStart, locationEnd, name, new TypesInOut(beforeTypes, afterTypes), inline, instructions);
	}

	@Override
	public TypeList visitTypeList(@Nullable TS4thParser.TypeListContext ctx) {
		TypeList typeList = TypeList.EMPTY;
		if (ctx != null) {
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
		return List.of(new Instruction.IntLiteral(value));
	}

	@Override
	public Object visitString(TS4thParser.StringContext ctx) {
		final String value = parseStringLiteral(ctx.String());
		return List.of(new Instruction.StringLiteral(value));
	}

	@Override
	public List<Instruction> visitTrue(TS4thParser.TrueContext ctx) {
		return List.of(new Instruction.BoolLiteral(true));
	}

	@Override
	public List<Instruction> visitFalse(TS4thParser.FalseContext ctx) {
		return List.of(new Instruction.BoolLiteral(false));
	}

	@Override
	public List<Instruction> visitIdentifier(TS4thParser.IdentifierContext ctx) {
		final TerminalNode identifier = ctx.Identifier();
		final String name = identifier.getText();
		final Location location = createLocation(identifier);
		return List.of(new Instruction.Command(name, location));
	}

	@Override
	public List<Instruction> visitIf(TS4thParser.IfContext ctx) {
		index++;
		final String labelIf = "if_" + index;
		final String labelEnd = "endif_" + index;

		final Location ifLocation = createLocation(ctx.If());

		final List<Instruction> instructions = new ArrayList<>();
		instructions.add(new Instruction.Branch(labelIf, labelEnd, ifLocation));

		instructions.add(new Instruction.Label(labelIf, ifLocation));
		instructions.addAll(visitInstructions(ctx.instructions()));

		final Location endLocation = createLocation(ctx.End());
		addJumpIfPrevIsNoJumpOrBranch(labelEnd, instructions, endLocation);

		instructions.add(new Instruction.Label(labelEnd, endLocation));
		return instructions;
	}

	@Override
	public List<Instruction> visitIfElse(TS4thParser.IfElseContext ctx) {
		index++;
		final String labelIf = "if_" + index;
		final String labelElse = "else_" + index;
		final String labelEnd = "endif_" + index;

		final Location ifLocation = createLocation(ctx.If());

		final List<Instruction> instructions = new ArrayList<>();
		instructions.add(new Instruction.Branch(labelIf, labelElse, ifLocation));

		instructions.add(new Instruction.Label(labelIf, ifLocation));
		instructions.addAll(visitInstructions(ctx.ifInstructions));

		final Location elseLocation = createLocation(ctx.Else());
		addJumpIfPrevIsNoJumpOrBranch(labelEnd, instructions, elseLocation);

		instructions.add(new Instruction.Label(labelElse, elseLocation));
		instructions.addAll(visitInstructions(ctx.elseInstructions));

		final Location endLocation = createLocation(ctx.End());
		addJumpIfPrevIsNoJumpOrBranch(labelEnd, instructions, endLocation);

		instructions.add(new Instruction.Label(labelEnd, endLocation));
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
			instructions.add(new Instruction.Label(labelWhile, createLocation(ctx.While())));

			breakLabel = null;
			continueLabel = null;

			instructions.addAll(visitInstructions(ctx.condition));

			final Location doLocation = createLocation(ctx.Do());
			instructions.add(new Instruction.Branch(labelWhileBody, labelWhileEnd, doLocation));

			instructions.add(new Instruction.Label(labelWhileBody, doLocation));

			breakLabel = labelWhileEnd;
			continueLabel = labelWhile;

			instructions.addAll(visitInstructions(ctx.body));
			instructions.add(new Instruction.Jump(labelWhile));

			instructions.add(new Instruction.Label(labelWhileEnd, createLocation(ctx.End())));
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
		return List.of(new Instruction.Jump(breakLabel));
	}

	@Override
	public List<Instruction> visitContinue(TS4thParser.ContinueContext ctx) {
		if (continueLabel == null) {
			final Token token = ctx.Continue().getSymbol();
			throw new ParseCancellationException("%1$d:%2$d continue only can be used inside while-do-end between do and end".formatted(token.getLine(), token.getCharPositionInLine()));
		}
		return List.of(new Instruction.Jump(continueLabel));
	}

	private String parseStringLiteral(TerminalNode node) {
		final String text = node.getText();
		final String raw = text.substring(1, text.length() - 1);
		return unescape(raw);
	}

	private void addJumpIfPrevIsNoJumpOrBranch(String target, List<Instruction> instructions, Location location) {
		if (instructions.size() > 0) {
			final Instruction lastInstruction = instructions.get(instructions.size() - 1);
			if (lastInstruction instanceof Instruction.Jump || lastInstruction instanceof Instruction.Label) {
				return;
			}
		}
		instructions.add(new Instruction.Jump(target, location));
	}

	private static Location createLocation(TerminalNode token) {
		return createLocation(token.getSymbol());
	}

	private static Location createLocation(Token token) {
		return new Location(token.getLine(), token.getCharPositionInLine());
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

	private static List<Declaration> parse(CharStream charStream, IncludeHandler includeHandler) {
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
		List<Declaration> include(String fileString);
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
		public List<Declaration> include(String fileString) {
			final Path includeFile = file.resolveSibling(fileString);
			if (!files.add(includeFile)) {
				return List.of();
			}

			return new FileIncludeHandler(includeFile, files)
					.parse();
		}

		public List<Declaration> parse() throws ParseCancellationException {
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
