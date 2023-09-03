// Generated from grammar/TS4th.g4 by ANTLR 4.7.2
package de.regnis.antlr.ts4th;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class TS4thLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		TypeSeparator=1, Include=2, Def=3, End=4, If=5, Else=6, Do=7, While=8, 
		Break=9, Continue=10, True=11, False=12, Inline=13, ParenOpen=14, ParenClose=15, 
		Number=16, String=17, Identifier=18, Whitespace=19, NL=20, LineComment=21, 
		BlockComment=22;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"TypeSeparator", "Include", "Def", "End", "If", "Else", "Do", "While", 
			"Break", "Continue", "True", "False", "Inline", "ParenOpen", "ParenClose", 
			"Number", "Char", "EscapedChar", "String", "StringChar", "Identifier", 
			"Whitespace", "NL", "LineComment", "BlockComment"
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


	public TS4thLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "TS4th.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\30\u00d6\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3"+
		"\4\3\4\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\t"+
		"\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\20\3\20\3\21\5\21\u0080\n\21"+
		"\3\21\6\21\u0083\n\21\r\21\16\21\u0084\3\21\3\21\3\21\3\21\6\21\u008b"+
		"\n\21\r\21\16\21\u008c\3\21\3\21\3\21\3\21\5\21\u0093\n\21\3\22\3\22\5"+
		"\22\u0097\n\22\3\23\3\23\3\23\3\24\3\24\7\24\u009e\n\24\f\24\16\24\u00a1"+
		"\13\24\3\24\3\24\3\25\3\25\5\25\u00a7\n\25\3\26\6\26\u00aa\n\26\r\26\16"+
		"\26\u00ab\3\27\6\27\u00af\n\27\r\27\16\27\u00b0\3\27\3\27\3\30\3\30\5"+
		"\30\u00b7\n\30\3\30\5\30\u00ba\n\30\3\30\3\30\3\31\3\31\3\31\3\31\7\31"+
		"\u00c2\n\31\f\31\16\31\u00c5\13\31\3\31\3\31\3\32\3\32\3\32\3\32\7\32"+
		"\u00cd\n\32\f\32\16\32\u00d0\13\32\3\32\3\32\3\32\3\32\3\32\3\u00ce\2"+
		"\33\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35"+
		"\20\37\21!\22#\2%\2\'\23)\2+\24-\25/\26\61\27\63\30\3\2\13\3\2//\3\2\62"+
		";\5\2\62;CHch\5\2\f\f\17\17))\b\2$$\62\62^^ppttvv\5\2\f\f\17\17$$\7\2"+
		"\13\f\17\17\"\"$$)+\4\2\13\13\"\"\4\2\f\f\17\17\2\u00e0\2\3\3\2\2\2\2"+
		"\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2"+
		"\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2"+
		"\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2\'\3\2\2\2\2+\3\2\2"+
		"\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\3\65\3\2\2\2\58\3\2"+
		"\2\2\7@\3\2\2\2\tD\3\2\2\2\13H\3\2\2\2\rK\3\2\2\2\17P\3\2\2\2\21S\3\2"+
		"\2\2\23Y\3\2\2\2\25_\3\2\2\2\27h\3\2\2\2\31m\3\2\2\2\33s\3\2\2\2\35z\3"+
		"\2\2\2\37|\3\2\2\2!\u0092\3\2\2\2#\u0096\3\2\2\2%\u0098\3\2\2\2\'\u009b"+
		"\3\2\2\2)\u00a6\3\2\2\2+\u00a9\3\2\2\2-\u00ae\3\2\2\2/\u00b9\3\2\2\2\61"+
		"\u00bd\3\2\2\2\63\u00c8\3\2\2\2\65\66\7/\2\2\66\67\7/\2\2\67\4\3\2\2\2"+
		"89\7k\2\29:\7p\2\2:;\7e\2\2;<\7n\2\2<=\7w\2\2=>\7f\2\2>?\7g\2\2?\6\3\2"+
		"\2\2@A\7f\2\2AB\7g\2\2BC\7h\2\2C\b\3\2\2\2DE\7g\2\2EF\7p\2\2FG\7f\2\2"+
		"G\n\3\2\2\2HI\7k\2\2IJ\7h\2\2J\f\3\2\2\2KL\7g\2\2LM\7n\2\2MN\7u\2\2NO"+
		"\7g\2\2O\16\3\2\2\2PQ\7f\2\2QR\7q\2\2R\20\3\2\2\2ST\7y\2\2TU\7j\2\2UV"+
		"\7k\2\2VW\7n\2\2WX\7g\2\2X\22\3\2\2\2YZ\7d\2\2Z[\7t\2\2[\\\7g\2\2\\]\7"+
		"c\2\2]^\7m\2\2^\24\3\2\2\2_`\7e\2\2`a\7q\2\2ab\7p\2\2bc\7v\2\2cd\7k\2"+
		"\2de\7p\2\2ef\7w\2\2fg\7g\2\2g\26\3\2\2\2hi\7v\2\2ij\7t\2\2jk\7w\2\2k"+
		"l\7g\2\2l\30\3\2\2\2mn\7h\2\2no\7c\2\2op\7n\2\2pq\7u\2\2qr\7g\2\2r\32"+
		"\3\2\2\2st\7k\2\2tu\7p\2\2uv\7n\2\2vw\7k\2\2wx\7p\2\2xy\7g\2\2y\34\3\2"+
		"\2\2z{\7*\2\2{\36\3\2\2\2|}\7+\2\2} \3\2\2\2~\u0080\t\2\2\2\177~\3\2\2"+
		"\2\177\u0080\3\2\2\2\u0080\u0082\3\2\2\2\u0081\u0083\t\3\2\2\u0082\u0081"+
		"\3\2\2\2\u0083\u0084\3\2\2\2\u0084\u0082\3\2\2\2\u0084\u0085\3\2\2\2\u0085"+
		"\u0093\3\2\2\2\u0086\u0087\7\62\2\2\u0087\u0088\7z\2\2\u0088\u008a\3\2"+
		"\2\2\u0089\u008b\t\4\2\2\u008a\u0089\3\2\2\2\u008b\u008c\3\2\2\2\u008c"+
		"\u008a\3\2\2\2\u008c\u008d\3\2\2\2\u008d\u0093\3\2\2\2\u008e\u008f\7)"+
		"\2\2\u008f\u0090\5#\22\2\u0090\u0091\7)\2\2\u0091\u0093\3\2\2\2\u0092"+
		"\177\3\2\2\2\u0092\u0086\3\2\2\2\u0092\u008e\3\2\2\2\u0093\"\3\2\2\2\u0094"+
		"\u0097\n\5\2\2\u0095\u0097\5%\23\2\u0096\u0094\3\2\2\2\u0096\u0095\3\2"+
		"\2\2\u0097$\3\2\2\2\u0098\u0099\7^\2\2\u0099\u009a\t\6\2\2\u009a&\3\2"+
		"\2\2\u009b\u009f\7$\2\2\u009c\u009e\5)\25\2\u009d\u009c\3\2\2\2\u009e"+
		"\u00a1\3\2\2\2\u009f\u009d\3\2\2\2\u009f\u00a0\3\2\2\2\u00a0\u00a2\3\2"+
		"\2\2\u00a1\u009f\3\2\2\2\u00a2\u00a3\7$\2\2\u00a3(\3\2\2\2\u00a4\u00a7"+
		"\n\7\2\2\u00a5\u00a7\5%\23\2\u00a6\u00a4\3\2\2\2\u00a6\u00a5\3\2\2\2\u00a7"+
		"*\3\2\2\2\u00a8\u00aa\n\b\2\2\u00a9\u00a8\3\2\2\2\u00aa\u00ab\3\2\2\2"+
		"\u00ab\u00a9\3\2\2\2\u00ab\u00ac\3\2\2\2\u00ac,\3\2\2\2\u00ad\u00af\t"+
		"\t\2\2\u00ae\u00ad\3\2\2\2\u00af\u00b0\3\2\2\2\u00b0\u00ae\3\2\2\2\u00b0"+
		"\u00b1\3\2\2\2\u00b1\u00b2\3\2\2\2\u00b2\u00b3\b\27\2\2\u00b3.\3\2\2\2"+
		"\u00b4\u00b6\7\17\2\2\u00b5\u00b7\7\f\2\2\u00b6\u00b5\3\2\2\2\u00b6\u00b7"+
		"\3\2\2\2\u00b7\u00ba\3\2\2\2\u00b8\u00ba\7\f\2\2\u00b9\u00b4\3\2\2\2\u00b9"+
		"\u00b8\3\2\2\2\u00ba\u00bb\3\2\2\2\u00bb\u00bc\b\30\2\2\u00bc\60\3\2\2"+
		"\2\u00bd\u00be\7\61\2\2\u00be\u00bf\7\61\2\2\u00bf\u00c3\3\2\2\2\u00c0"+
		"\u00c2\n\n\2\2\u00c1\u00c0\3\2\2\2\u00c2\u00c5\3\2\2\2\u00c3\u00c1\3\2"+
		"\2\2\u00c3\u00c4\3\2\2\2\u00c4\u00c6\3\2\2\2\u00c5\u00c3\3\2\2\2\u00c6"+
		"\u00c7\b\31\2\2\u00c7\62\3\2\2\2\u00c8\u00c9\7\61\2\2\u00c9\u00ca\7,\2"+
		"\2\u00ca\u00ce\3\2\2\2\u00cb\u00cd\13\2\2\2\u00cc\u00cb\3\2\2\2\u00cd"+
		"\u00d0\3\2\2\2\u00ce\u00cf\3\2\2\2\u00ce\u00cc\3\2\2\2\u00cf\u00d1\3\2"+
		"\2\2\u00d0\u00ce\3\2\2\2\u00d1\u00d2\7,\2\2\u00d2\u00d3\7\61\2\2\u00d3"+
		"\u00d4\3\2\2\2\u00d4\u00d5\b\32\2\2\u00d5\64\3\2\2\2\20\2\177\u0084\u008c"+
		"\u0092\u0096\u009f\u00a6\u00ab\u00b0\u00b6\u00b9\u00c3\u00ce\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}