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
		TypeSeparator=1, Include=2, Func=3, Const=4, End=5, If=6, Else=7, Do=8, 
		While=9, Break=10, Continue=11, True=12, False=13, Inline=14, ParenOpen=15, 
		ParenClose=16, Number=17, String=18, Identifier=19, Whitespace=20, NL=21, 
		LineComment=22, BlockComment=23;
	public static final int
		RULE_root = 0, RULE_rootItem = 1, RULE_include = 2, RULE_funcDeclaration = 3, 
		RULE_constDeclaration = 4, RULE_typeList = 5, RULE_instructions = 6, RULE_instruction = 7;
	private static String[] makeRuleNames() {
		return new String[] {
			"root", "rootItem", "include", "funcDeclaration", "constDeclaration", 
			"typeList", "instructions", "instruction"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'--'", "'include'", "'fn'", "'const'", "'end'", "'if'", "'else'", 
			"'do'", "'while'", "'break'", "'continue'", "'true'", "'false'", "'inline'", 
			"'('", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "TypeSeparator", "Include", "Func", "Const", "End", "If", "Else", 
			"Do", "While", "Break", "Continue", "True", "False", "Inline", "ParenOpen", 
			"ParenClose", "Number", "String", "Identifier", "Whitespace", "NL", "LineComment", 
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
			setState(19);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Include) | (1L << Func) | (1L << Const))) != 0)) {
				{
				{
				setState(16);
				rootItem();
				}
				}
				setState(21);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(22);
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
		public IncludeContext include() {
			return getRuleContext(IncludeContext.class,0);
		}
		public FuncDeclarationContext funcDeclaration() {
			return getRuleContext(FuncDeclarationContext.class,0);
		}
		public ConstDeclarationContext constDeclaration() {
			return getRuleContext(ConstDeclarationContext.class,0);
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
			setState(27);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Include:
				enterOuterAlt(_localctx, 1);
				{
				setState(24);
				include();
				}
				break;
			case Func:
				enterOuterAlt(_localctx, 2);
				{
				setState(25);
				funcDeclaration();
				}
				break;
			case Const:
				enterOuterAlt(_localctx, 3);
				{
				setState(26);
				constDeclaration();
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
			setState(29);
			match(Include);
			setState(30);
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

	public static class FuncDeclarationContext extends ParserRuleContext {
		public Token name;
		public TypeListContext beforeTypes;
		public TypeListContext afterTypes;
		public TerminalNode Func() { return getToken(TS4thParser.Func, 0); }
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
		public FuncDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).enterFuncDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).exitFuncDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TS4thVisitor ) return ((TS4thVisitor<? extends T>)visitor).visitFuncDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncDeclarationContext funcDeclaration() throws RecognitionException {
		FuncDeclarationContext _localctx = new FuncDeclarationContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_funcDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(32);
			match(Func);
			setState(34);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Inline) {
				{
				setState(33);
				match(Inline);
				}
			}

			setState(36);
			((FuncDeclarationContext)_localctx).name = match(Identifier);
			setState(37);
			match(ParenOpen);
			setState(38);
			((FuncDeclarationContext)_localctx).beforeTypes = typeList();
			setState(39);
			match(TypeSeparator);
			setState(40);
			((FuncDeclarationContext)_localctx).afterTypes = typeList();
			setState(41);
			match(ParenClose);
			setState(42);
			instructions();
			setState(43);
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

	public static class ConstDeclarationContext extends ParserRuleContext {
		public Token name;
		public TerminalNode Const() { return getToken(TS4thParser.Const, 0); }
		public InstructionsContext instructions() {
			return getRuleContext(InstructionsContext.class,0);
		}
		public TerminalNode End() { return getToken(TS4thParser.End, 0); }
		public TerminalNode Identifier() { return getToken(TS4thParser.Identifier, 0); }
		public ConstDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).enterConstDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TS4thListener ) ((TS4thListener)listener).exitConstDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TS4thVisitor ) return ((TS4thVisitor<? extends T>)visitor).visitConstDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstDeclarationContext constDeclaration() throws RecognitionException {
		ConstDeclarationContext _localctx = new ConstDeclarationContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_constDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(45);
			match(Const);
			setState(46);
			((ConstDeclarationContext)_localctx).name = match(Identifier);
			setState(47);
			instructions();
			setState(48);
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
		enterRule(_localctx, 10, RULE_typeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(53);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Identifier) {
				{
				{
				setState(50);
				match(Identifier);
				}
				}
				setState(55);
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
		enterRule(_localctx, 12, RULE_instructions);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(57); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(56);
				instruction();
				}
				}
				setState(59); 
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
		enterRule(_localctx, 14, RULE_instruction);
		try {
			setState(84);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				_localctx = new NumberContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(61);
				match(Number);
				}
				break;
			case 2:
				_localctx = new StringContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(62);
				match(String);
				}
				break;
			case 3:
				_localctx = new TrueContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(63);
				match(True);
				}
				break;
			case 4:
				_localctx = new FalseContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(64);
				match(False);
				}
				break;
			case 5:
				_localctx = new IdentifierContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(65);
				match(Identifier);
				}
				break;
			case 6:
				_localctx = new IfContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(66);
				match(If);
				setState(67);
				instructions();
				setState(68);
				match(End);
				}
				break;
			case 7:
				_localctx = new IfElseContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(70);
				match(If);
				setState(71);
				((IfElseContext)_localctx).ifInstructions = instructions();
				setState(72);
				match(Else);
				setState(73);
				((IfElseContext)_localctx).elseInstructions = instructions();
				setState(74);
				match(End);
				}
				break;
			case 8:
				_localctx = new WhileContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(76);
				match(While);
				setState(77);
				((WhileContext)_localctx).condition = instructions();
				setState(78);
				match(Do);
				setState(79);
				((WhileContext)_localctx).body = instructions();
				setState(80);
				match(End);
				}
				break;
			case 9:
				_localctx = new BreakContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(82);
				match(Break);
				}
				break;
			case 10:
				_localctx = new ContinueContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(83);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\31Y\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\3\2\7\2\24\n\2\f\2"+
		"\16\2\27\13\2\3\2\3\2\3\3\3\3\3\3\5\3\36\n\3\3\4\3\4\3\4\3\5\3\5\5\5%"+
		"\n\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\7\7\7\66"+
		"\n\7\f\7\16\79\13\7\3\b\6\b<\n\b\r\b\16\b=\3\t\3\t\3\t\3\t\3\t\3\t\3\t"+
		"\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\5\tW"+
		"\n\t\3\t\2\2\n\2\4\6\b\n\f\16\20\2\2\2_\2\25\3\2\2\2\4\35\3\2\2\2\6\37"+
		"\3\2\2\2\b\"\3\2\2\2\n/\3\2\2\2\f\67\3\2\2\2\16;\3\2\2\2\20V\3\2\2\2\22"+
		"\24\5\4\3\2\23\22\3\2\2\2\24\27\3\2\2\2\25\23\3\2\2\2\25\26\3\2\2\2\26"+
		"\30\3\2\2\2\27\25\3\2\2\2\30\31\7\2\2\3\31\3\3\2\2\2\32\36\5\6\4\2\33"+
		"\36\5\b\5\2\34\36\5\n\6\2\35\32\3\2\2\2\35\33\3\2\2\2\35\34\3\2\2\2\36"+
		"\5\3\2\2\2\37 \7\4\2\2 !\7\24\2\2!\7\3\2\2\2\"$\7\5\2\2#%\7\20\2\2$#\3"+
		"\2\2\2$%\3\2\2\2%&\3\2\2\2&\'\7\25\2\2\'(\7\21\2\2()\5\f\7\2)*\7\3\2\2"+
		"*+\5\f\7\2+,\7\22\2\2,-\5\16\b\2-.\7\7\2\2.\t\3\2\2\2/\60\7\6\2\2\60\61"+
		"\7\25\2\2\61\62\5\16\b\2\62\63\7\7\2\2\63\13\3\2\2\2\64\66\7\25\2\2\65"+
		"\64\3\2\2\2\669\3\2\2\2\67\65\3\2\2\2\678\3\2\2\28\r\3\2\2\29\67\3\2\2"+
		"\2:<\5\20\t\2;:\3\2\2\2<=\3\2\2\2=;\3\2\2\2=>\3\2\2\2>\17\3\2\2\2?W\7"+
		"\23\2\2@W\7\24\2\2AW\7\16\2\2BW\7\17\2\2CW\7\25\2\2DE\7\b\2\2EF\5\16\b"+
		"\2FG\7\7\2\2GW\3\2\2\2HI\7\b\2\2IJ\5\16\b\2JK\7\t\2\2KL\5\16\b\2LM\7\7"+
		"\2\2MW\3\2\2\2NO\7\13\2\2OP\5\16\b\2PQ\7\n\2\2QR\5\16\b\2RS\7\7\2\2SW"+
		"\3\2\2\2TW\7\f\2\2UW\7\r\2\2V?\3\2\2\2V@\3\2\2\2VA\3\2\2\2VB\3\2\2\2V"+
		"C\3\2\2\2VD\3\2\2\2VH\3\2\2\2VN\3\2\2\2VT\3\2\2\2VU\3\2\2\2W\21\3\2\2"+
		"\2\b\25\35$\67=V";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}