// Generated from grammar/TS4th.g4 by ANTLR 4.7.2
package de.regnis.antlr.ts4th;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class TS4thParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		TypeSeparator=1, Include=2, Def=3, End=4, If=5, Else=6, Do=7, While=8, 
		Break=9, Continue=10, True=11, False=12, Inline=13, ParenOpen=14, ParenClose=15, 
		Number=16, String=17, Identifier=18, Whitespace=19, NL=20, LineComment=21, 
		BlockComment=22;
	public static final int
		RULE_root = 0, RULE_rootItem = 1, RULE_include = 2, RULE_declaration = 3, 
		RULE_typeList = 4, RULE_instructions = 5, RULE_instruction = 6;
	private static String[] makeRuleNames() {
		return new String[] {
			"root", "rootItem", "include", "declaration", "typeList", "instructions", 
			"instruction"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'--'", "'include'", "'def'", "'end'", "'if'", "'else'", "'do'", 
			"'while'", "'break'", "'continue'", "'true'", "'false'", "'inline'", 
			"'('", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "TypeSeparator", "Include", "Def", "End", "If", "Else", "Do", "While", 
			"Break", "Continue", "True", "False", "Inline", "ParenOpen", "ParenClose", 
			"Number", "String", "Identifier", "Whitespace", "NL", "LineComment", 
			"BlockComment"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "TS4th.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public TS4thParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class RootContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(TS4thParser.EOF, 0); }
		public List<RootItemContext> rootItem() {
			return getRuleContexts(RootItemContext.class);
		}
		public RootItemContext rootItem(int i) {
			return getRuleContext(RootItemContext.class,i);
		}
		public RootContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_root; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).enterRoot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).exitRoot(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TS4thVisitor ) return ((TS4thVisitor<? extends T>)visitor).visitRoot(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RootContext root() throws RecognitionException {
		RootContext _localctx = new RootContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_root);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(17);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Include || _la==Def) {
				{
				{
				setState(14);
				rootItem();
				}
				}
				setState(19);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(20);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RootItemContext extends ParserRuleContext {
		public DeclarationContext declaration() {
			return getRuleContext(DeclarationContext.class,0);
		}
		public IncludeContext include() {
			return getRuleContext(IncludeContext.class,0);
		}
		public RootItemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rootItem; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).enterRootItem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).exitRootItem(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TS4thVisitor ) return ((TS4thVisitor<? extends T>)visitor).visitRootItem(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RootItemContext rootItem() throws RecognitionException {
		RootItemContext _localctx = new RootItemContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_rootItem);
		try {
			setState(24);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Def:
				enterOuterAlt(_localctx, 1);
				{
				setState(22);
				declaration();
				}
				break;
			case Include:
				enterOuterAlt(_localctx, 2);
				{
				setState(23);
				include();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IncludeContext extends ParserRuleContext {
		public TerminalNode Include() { return getToken(TS4thParser.Include, 0); }
		public TerminalNode String() { return getToken(TS4thParser.String, 0); }
		public IncludeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_include; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).enterInclude(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).exitInclude(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TS4thVisitor ) return ((TS4thVisitor<? extends T>)visitor).visitInclude(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IncludeContext include() throws RecognitionException {
		IncludeContext _localctx = new IncludeContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_include);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(26);
			match(Include);
			setState(27);
			match(String);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclarationContext extends ParserRuleContext {
		public Token name;
		public TypeListContext beforeTypes;
		public TypeListContext afterTypes;
		public TerminalNode Def() { return getToken(TS4thParser.Def, 0); }
		public TerminalNode ParenOpen() { return getToken(TS4thParser.ParenOpen, 0); }
		public TerminalNode TypeSeparator() { return getToken(TS4thParser.TypeSeparator, 0); }
		public TerminalNode ParenClose() { return getToken(TS4thParser.ParenClose, 0); }
		public InstructionsContext instructions() {
			return getRuleContext(InstructionsContext.class,0);
		}
		public TerminalNode End() { return getToken(TS4thParser.End, 0); }
		public TerminalNode Identifier() { return getToken(TS4thParser.Identifier, 0); }
		public List<TypeListContext> typeList() {
			return getRuleContexts(TypeListContext.class);
		}
		public TypeListContext typeList(int i) {
			return getRuleContext(TypeListContext.class,i);
		}
		public TerminalNode Inline() { return getToken(TS4thParser.Inline, 0); }
		public DeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).enterDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).exitDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TS4thVisitor ) return ((TS4thVisitor<? extends T>)visitor).visitDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclarationContext declaration() throws RecognitionException {
		DeclarationContext _localctx = new DeclarationContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_declaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(29);
			match(Def);
			setState(31);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Inline) {
				{
				setState(30);
				match(Inline);
				}
			}

			setState(33);
			((DeclarationContext)_localctx).name = match(Identifier);
			setState(34);
			match(ParenOpen);
			setState(35);
			((DeclarationContext)_localctx).beforeTypes = typeList();
			setState(36);
			match(TypeSeparator);
			setState(37);
			((DeclarationContext)_localctx).afterTypes = typeList();
			setState(38);
			match(ParenClose);
			setState(39);
			instructions();
			setState(40);
			match(End);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeListContext extends ParserRuleContext {
		public List<TerminalNode> Identifier() { return getTokens(TS4thParser.Identifier); }
		public TerminalNode Identifier(int i) {
			return getToken(TS4thParser.Identifier, i);
		}
		public TypeListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).enterTypeList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).exitTypeList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TS4thVisitor ) return ((TS4thVisitor<? extends T>)visitor).visitTypeList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeListContext typeList() throws RecognitionException {
		TypeListContext _localctx = new TypeListContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_typeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(45);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Identifier) {
				{
				{
				setState(42);
				match(Identifier);
				}
				}
				setState(47);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InstructionsContext extends ParserRuleContext {
		public List<InstructionContext> instruction() {
			return getRuleContexts(InstructionContext.class);
		}
		public InstructionContext instruction(int i) {
			return getRuleContext(InstructionContext.class,i);
		}
		public InstructionsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_instructions; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).enterInstructions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).exitInstructions(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TS4thVisitor ) return ((TS4thVisitor<? extends T>)visitor).visitInstructions(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InstructionsContext instructions() throws RecognitionException {
		InstructionsContext _localctx = new InstructionsContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_instructions);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(49); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(48);
				instruction();
				}
				}
				setState(51); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << If) | (1L << While) | (1L << Break) | (1L << Continue) | (1L << True) | (1L << False) | (1L << Number) | (1L << String) | (1L << Identifier))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InstructionContext extends ParserRuleContext {
		public InstructionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_instruction; }
	 
		public InstructionContext() { }
		public void copyFrom(InstructionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class NumberContext extends InstructionContext {
		public TerminalNode Number() { return getToken(TS4thParser.Number, 0); }
		public NumberContext(InstructionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).enterNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).exitNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TS4thVisitor ) return ((TS4thVisitor<? extends T>)visitor).visitNumber(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IdentifierContext extends InstructionContext {
		public TerminalNode Identifier() { return getToken(TS4thParser.Identifier, 0); }
		public IdentifierContext(InstructionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).enterIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).exitIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TS4thVisitor ) return ((TS4thVisitor<? extends T>)visitor).visitIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StringContext extends InstructionContext {
		public TerminalNode String() { return getToken(TS4thParser.String, 0); }
		public StringContext(InstructionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).enterString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).exitString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TS4thVisitor ) return ((TS4thVisitor<? extends T>)visitor).visitString(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BreakContext extends InstructionContext {
		public TerminalNode Break() { return getToken(TS4thParser.Break, 0); }
		public BreakContext(InstructionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).enterBreak(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).exitBreak(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TS4thVisitor ) return ((TS4thVisitor<? extends T>)visitor).visitBreak(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ContinueContext extends InstructionContext {
		public TerminalNode Continue() { return getToken(TS4thParser.Continue, 0); }
		public ContinueContext(InstructionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).enterContinue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).exitContinue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TS4thVisitor ) return ((TS4thVisitor<? extends T>)visitor).visitContinue(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TrueContext extends InstructionContext {
		public TerminalNode True() { return getToken(TS4thParser.True, 0); }
		public TrueContext(InstructionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).enterTrue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).exitTrue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TS4thVisitor ) return ((TS4thVisitor<? extends T>)visitor).visitTrue(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FalseContext extends InstructionContext {
		public TerminalNode False() { return getToken(TS4thParser.False, 0); }
		public FalseContext(InstructionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).enterFalse(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).exitFalse(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TS4thVisitor ) return ((TS4thVisitor<? extends T>)visitor).visitFalse(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class WhileContext extends InstructionContext {
		public InstructionsContext condition;
		public InstructionsContext body;
		public TerminalNode While() { return getToken(TS4thParser.While, 0); }
		public TerminalNode Do() { return getToken(TS4thParser.Do, 0); }
		public TerminalNode End() { return getToken(TS4thParser.End, 0); }
		public List<InstructionsContext> instructions() {
			return getRuleContexts(InstructionsContext.class);
		}
		public InstructionsContext instructions(int i) {
			return getRuleContext(InstructionsContext.class,i);
		}
		public WhileContext(InstructionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).enterWhile(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).exitWhile(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TS4thVisitor ) return ((TS4thVisitor<? extends T>)visitor).visitWhile(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IfContext extends InstructionContext {
		public TerminalNode If() { return getToken(TS4thParser.If, 0); }
		public InstructionsContext instructions() {
			return getRuleContext(InstructionsContext.class,0);
		}
		public TerminalNode End() { return getToken(TS4thParser.End, 0); }
		public IfContext(InstructionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).enterIf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).exitIf(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TS4thVisitor ) return ((TS4thVisitor<? extends T>)visitor).visitIf(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IfElseContext extends InstructionContext {
		public InstructionsContext ifInstructions;
		public InstructionsContext elseInstructions;
		public TerminalNode If() { return getToken(TS4thParser.If, 0); }
		public TerminalNode Else() { return getToken(TS4thParser.Else, 0); }
		public TerminalNode End() { return getToken(TS4thParser.End, 0); }
		public List<InstructionsContext> instructions() {
			return getRuleContexts(InstructionsContext.class);
		}
		public InstructionsContext instructions(int i) {
			return getRuleContext(InstructionsContext.class,i);
		}
		public IfElseContext(InstructionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).enterIfElse(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).exitIfElse(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TS4thVisitor ) return ((TS4thVisitor<? extends T>)visitor).visitIfElse(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InstructionContext instruction() throws RecognitionException {
		InstructionContext _localctx = new InstructionContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_instruction);
		try {
			setState(76);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				_localctx = new NumberContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(53);
				match(Number);
				}
				break;
			case 2:
				_localctx = new StringContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(54);
				match(String);
				}
				break;
			case 3:
				_localctx = new TrueContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(55);
				match(True);
				}
				break;
			case 4:
				_localctx = new FalseContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(56);
				match(False);
				}
				break;
			case 5:
				_localctx = new IdentifierContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(57);
				match(Identifier);
				}
				break;
			case 6:
				_localctx = new IfContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(58);
				match(If);
				setState(59);
				instructions();
				setState(60);
				match(End);
				}
				break;
			case 7:
				_localctx = new IfElseContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(62);
				match(If);
				setState(63);
				((IfElseContext)_localctx).ifInstructions = instructions();
				setState(64);
				match(Else);
				setState(65);
				((IfElseContext)_localctx).elseInstructions = instructions();
				setState(66);
				match(End);
				}
				break;
			case 8:
				_localctx = new WhileContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(68);
				match(While);
				setState(69);
				((WhileContext)_localctx).condition = instructions();
				setState(70);
				match(Do);
				setState(71);
				((WhileContext)_localctx).body = instructions();
				setState(72);
				match(End);
				}
				break;
			case 9:
				_localctx = new BreakContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(74);
				match(Break);
				}
				break;
			case 10:
				_localctx = new ContinueContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(75);
				match(Continue);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\30Q\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\3\2\7\2\22\n\2\f\2\16\2\25"+
		"\13\2\3\2\3\2\3\3\3\3\5\3\33\n\3\3\4\3\4\3\4\3\5\3\5\5\5\"\n\5\3\5\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\7\6.\n\6\f\6\16\6\61\13\6\3\7\6\7\64"+
		"\n\7\r\7\16\7\65\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3"+
		"\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\5\bO\n\b\3\b\2\2\t\2\4\6\b\n\f"+
		"\16\2\2\2W\2\23\3\2\2\2\4\32\3\2\2\2\6\34\3\2\2\2\b\37\3\2\2\2\n/\3\2"+
		"\2\2\f\63\3\2\2\2\16N\3\2\2\2\20\22\5\4\3\2\21\20\3\2\2\2\22\25\3\2\2"+
		"\2\23\21\3\2\2\2\23\24\3\2\2\2\24\26\3\2\2\2\25\23\3\2\2\2\26\27\7\2\2"+
		"\3\27\3\3\2\2\2\30\33\5\b\5\2\31\33\5\6\4\2\32\30\3\2\2\2\32\31\3\2\2"+
		"\2\33\5\3\2\2\2\34\35\7\4\2\2\35\36\7\23\2\2\36\7\3\2\2\2\37!\7\5\2\2"+
		" \"\7\17\2\2! \3\2\2\2!\"\3\2\2\2\"#\3\2\2\2#$\7\24\2\2$%\7\20\2\2%&\5"+
		"\n\6\2&\'\7\3\2\2\'(\5\n\6\2()\7\21\2\2)*\5\f\7\2*+\7\6\2\2+\t\3\2\2\2"+
		",.\7\24\2\2-,\3\2\2\2.\61\3\2\2\2/-\3\2\2\2/\60\3\2\2\2\60\13\3\2\2\2"+
		"\61/\3\2\2\2\62\64\5\16\b\2\63\62\3\2\2\2\64\65\3\2\2\2\65\63\3\2\2\2"+
		"\65\66\3\2\2\2\66\r\3\2\2\2\67O\7\22\2\28O\7\23\2\29O\7\r\2\2:O\7\16\2"+
		"\2;O\7\24\2\2<=\7\7\2\2=>\5\f\7\2>?\7\6\2\2?O\3\2\2\2@A\7\7\2\2AB\5\f"+
		"\7\2BC\7\b\2\2CD\5\f\7\2DE\7\6\2\2EO\3\2\2\2FG\7\n\2\2GH\5\f\7\2HI\7\t"+
		"\2\2IJ\5\f\7\2JK\7\6\2\2KO\3\2\2\2LO\7\13\2\2MO\7\f\2\2N\67\3\2\2\2N8"+
		"\3\2\2\2N9\3\2\2\2N:\3\2\2\2N;\3\2\2\2N<\3\2\2\2N@\3\2\2\2NF\3\2\2\2N"+
		"L\3\2\2\2NM\3\2\2\2O\17\3\2\2\2\b\23\32!/\65N";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}