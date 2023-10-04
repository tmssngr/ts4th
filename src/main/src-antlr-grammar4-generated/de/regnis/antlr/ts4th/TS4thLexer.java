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
		TypeSeparator=1, Include=2, Func=3, Const=4, Var=5, End=6, If=7, Else=8, 
		Do=9, While=10, Break=11, Continue=12, True=13, False=14, Inline=15, ParenOpen=16, 
		ParenClose=17, Number=18, String=19, Identifier=20, Whitespace=21, NL=22, 
		LineComment=23, BlockComment=24;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"TypeSeparator", "Include", "Func", "Const", "Var", "End", "If", "Else", 
			"Do", "While", "Break", "Continue", "True", "False", "Inline", "ParenOpen", 
			"ParenClose", "Number", "HexDigit", "BitCount", "Char", "EscapedChar", 
			"String", "StringChar", "Identifier", "Whitespace", "NL", "LineComment", 
			"BlockComment"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'--'", "'include'", "'fn'", "'const'", "'var'", "'end'", "'if'", 
			"'else'", "'do'", "'while'", "'break'", "'continue'", "'true'", "'false'", 
			"'inline'", "'('", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "TypeSeparator", "Include", "Func", "Const", "Var", "End", "If", 
			"Else", "Do", "While", "Break", "Continue", "True", "False", "Inline", 
			"ParenOpen", "ParenClose", "Number", "String", "Identifier", "Whitespace", 
			"NL", "LineComment", "BlockComment"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\32\u0106\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\3\2\3\2\3\2\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\6"+
		"\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3"+
		"\n\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3"+
		"\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3"+
		"\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\22\3\22\3\23\6"+
		"\23\u0091\n\23\r\23\16\23\u0092\3\23\3\23\5\23\u0097\n\23\3\23\3\23\6"+
		"\23\u009b\n\23\r\23\16\23\u009c\3\23\3\23\5\23\u00a1\n\23\3\23\3\23\3"+
		"\23\3\23\6\23\u00a7\n\23\r\23\16\23\u00a8\3\23\3\23\5\23\u00ad\n\23\3"+
		"\23\3\23\3\23\3\23\5\23\u00b3\n\23\3\24\3\24\3\25\3\25\3\25\3\25\3\25"+
		"\3\25\3\25\5\25\u00be\n\25\3\26\3\26\5\26\u00c2\n\26\3\27\3\27\3\27\3"+
		"\27\3\27\3\27\5\27\u00ca\n\27\3\30\3\30\7\30\u00ce\n\30\f\30\16\30\u00d1"+
		"\13\30\3\30\3\30\3\31\3\31\5\31\u00d7\n\31\3\32\6\32\u00da\n\32\r\32\16"+
		"\32\u00db\3\33\6\33\u00df\n\33\r\33\16\33\u00e0\3\33\3\33\3\34\3\34\5"+
		"\34\u00e7\n\34\3\34\5\34\u00ea\n\34\3\34\3\34\3\35\3\35\3\35\3\35\7\35"+
		"\u00f2\n\35\f\35\16\35\u00f5\13\35\3\35\3\35\3\36\3\36\3\36\3\36\7\36"+
		"\u00fd\n\36\f\36\16\36\u0100\13\36\3\36\3\36\3\36\3\36\3\36\3\u00fe\2"+
		"\37\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35"+
		"\20\37\21!\22#\23%\24\'\2)\2+\2-\2/\25\61\2\63\26\65\27\67\309\31;\32"+
		"\3\2\13\3\2\62;\4\2kkww\5\2\62;CHch\5\2\f\f\17\17))\b\2$$\62\62^^pptt"+
		"vv\5\2\f\f\17\17$$\7\2\13\f\17\17\"\"$$)+\4\2\13\13\"\"\4\2\f\f\17\17"+
		"\2\u0116\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2"+
		"\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27"+
		"\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2"+
		"\2\2#\3\2\2\2\2%\3\2\2\2\2/\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2"+
		"\2\2\29\3\2\2\2\2;\3\2\2\2\3=\3\2\2\2\5@\3\2\2\2\7H\3\2\2\2\tK\3\2\2\2"+
		"\13Q\3\2\2\2\rU\3\2\2\2\17Y\3\2\2\2\21\\\3\2\2\2\23a\3\2\2\2\25d\3\2\2"+
		"\2\27j\3\2\2\2\31p\3\2\2\2\33y\3\2\2\2\35~\3\2\2\2\37\u0084\3\2\2\2!\u008b"+
		"\3\2\2\2#\u008d\3\2\2\2%\u00b2\3\2\2\2\'\u00b4\3\2\2\2)\u00bd\3\2\2\2"+
		"+\u00c1\3\2\2\2-\u00c3\3\2\2\2/\u00cb\3\2\2\2\61\u00d6\3\2\2\2\63\u00d9"+
		"\3\2\2\2\65\u00de\3\2\2\2\67\u00e9\3\2\2\29\u00ed\3\2\2\2;\u00f8\3\2\2"+
		"\2=>\7/\2\2>?\7/\2\2?\4\3\2\2\2@A\7k\2\2AB\7p\2\2BC\7e\2\2CD\7n\2\2DE"+
		"\7w\2\2EF\7f\2\2FG\7g\2\2G\6\3\2\2\2HI\7h\2\2IJ\7p\2\2J\b\3\2\2\2KL\7"+
		"e\2\2LM\7q\2\2MN\7p\2\2NO\7u\2\2OP\7v\2\2P\n\3\2\2\2QR\7x\2\2RS\7c\2\2"+
		"ST\7t\2\2T\f\3\2\2\2UV\7g\2\2VW\7p\2\2WX\7f\2\2X\16\3\2\2\2YZ\7k\2\2Z"+
		"[\7h\2\2[\20\3\2\2\2\\]\7g\2\2]^\7n\2\2^_\7u\2\2_`\7g\2\2`\22\3\2\2\2"+
		"ab\7f\2\2bc\7q\2\2c\24\3\2\2\2de\7y\2\2ef\7j\2\2fg\7k\2\2gh\7n\2\2hi\7"+
		"g\2\2i\26\3\2\2\2jk\7d\2\2kl\7t\2\2lm\7g\2\2mn\7c\2\2no\7m\2\2o\30\3\2"+
		"\2\2pq\7e\2\2qr\7q\2\2rs\7p\2\2st\7v\2\2tu\7k\2\2uv\7p\2\2vw\7w\2\2wx"+
		"\7g\2\2x\32\3\2\2\2yz\7v\2\2z{\7t\2\2{|\7w\2\2|}\7g\2\2}\34\3\2\2\2~\177"+
		"\7h\2\2\177\u0080\7c\2\2\u0080\u0081\7n\2\2\u0081\u0082\7u\2\2\u0082\u0083"+
		"\7g\2\2\u0083\36\3\2\2\2\u0084\u0085\7k\2\2\u0085\u0086\7p\2\2\u0086\u0087"+
		"\7n\2\2\u0087\u0088\7k\2\2\u0088\u0089\7p\2\2\u0089\u008a\7g\2\2\u008a"+
		" \3\2\2\2\u008b\u008c\7*\2\2\u008c\"\3\2\2\2\u008d\u008e\7+\2\2\u008e"+
		"$\3\2\2\2\u008f\u0091\t\2\2\2\u0090\u008f\3\2\2\2\u0091\u0092\3\2\2\2"+
		"\u0092\u0090\3\2\2\2\u0092\u0093\3\2\2\2\u0093\u0096\3\2\2\2\u0094\u0095"+
		"\t\3\2\2\u0095\u0097\5)\25\2\u0096\u0094\3\2\2\2\u0096\u0097\3\2\2\2\u0097"+
		"\u00b3\3\2\2\2\u0098\u009a\7/\2\2\u0099\u009b\t\2\2\2\u009a\u0099\3\2"+
		"\2\2\u009b\u009c\3\2\2\2\u009c\u009a\3\2\2\2\u009c\u009d\3\2\2\2\u009d"+
		"\u00a0\3\2\2\2\u009e\u009f\7k\2\2\u009f\u00a1\5)\25\2\u00a0\u009e\3\2"+
		"\2\2\u00a0\u00a1\3\2\2\2\u00a1\u00b3\3\2\2\2\u00a2\u00a3\7\62\2\2\u00a3"+
		"\u00a4\7z\2\2\u00a4\u00a6\3\2\2\2\u00a5\u00a7\5\'\24\2\u00a6\u00a5\3\2"+
		"\2\2\u00a7\u00a8\3\2\2\2\u00a8\u00a6\3\2\2\2\u00a8\u00a9\3\2\2\2\u00a9"+
		"\u00ac\3\2\2\2\u00aa\u00ab\7w\2\2\u00ab\u00ad\5)\25\2\u00ac\u00aa\3\2"+
		"\2\2\u00ac\u00ad\3\2\2\2\u00ad\u00b3\3\2\2\2\u00ae\u00af\7)\2\2\u00af"+
		"\u00b0\5+\26\2\u00b0\u00b1\7)\2\2\u00b1\u00b3\3\2\2\2\u00b2\u0090\3\2"+
		"\2\2\u00b2\u0098\3\2\2\2\u00b2\u00a2\3\2\2\2\u00b2\u00ae\3\2\2\2\u00b3"+
		"&\3\2\2\2\u00b4\u00b5\t\4\2\2\u00b5(\3\2\2\2\u00b6\u00be\7:\2\2\u00b7"+
		"\u00b8\7\63\2\2\u00b8\u00be\78\2\2\u00b9\u00ba\7\65\2\2\u00ba\u00be\7"+
		"\64\2\2\u00bb\u00bc\78\2\2\u00bc\u00be\7\66\2\2\u00bd\u00b6\3\2\2\2\u00bd"+
		"\u00b7\3\2\2\2\u00bd\u00b9\3\2\2\2\u00bd\u00bb\3\2\2\2\u00be*\3\2\2\2"+
		"\u00bf\u00c2\n\5\2\2\u00c0\u00c2\5-\27\2\u00c1\u00bf\3\2\2\2\u00c1\u00c0"+
		"\3\2\2\2\u00c2,\3\2\2\2\u00c3\u00c9\7^\2\2\u00c4\u00ca\t\6\2\2\u00c5\u00c6"+
		"\7z\2\2\u00c6\u00c7\5\'\24\2\u00c7\u00c8\5\'\24\2\u00c8\u00ca\3\2\2\2"+
		"\u00c9\u00c4\3\2\2\2\u00c9\u00c5\3\2\2\2\u00ca.\3\2\2\2\u00cb\u00cf\7"+
		"$\2\2\u00cc\u00ce\5\61\31\2\u00cd\u00cc\3\2\2\2\u00ce\u00d1\3\2\2\2\u00cf"+
		"\u00cd\3\2\2\2\u00cf\u00d0\3\2\2\2\u00d0\u00d2\3\2\2\2\u00d1\u00cf\3\2"+
		"\2\2\u00d2\u00d3\7$\2\2\u00d3\60\3\2\2\2\u00d4\u00d7\n\7\2\2\u00d5\u00d7"+
		"\5-\27\2\u00d6\u00d4\3\2\2\2\u00d6\u00d5\3\2\2\2\u00d7\62\3\2\2\2\u00d8"+
		"\u00da\n\b\2\2\u00d9\u00d8\3\2\2\2\u00da\u00db\3\2\2\2\u00db\u00d9\3\2"+
		"\2\2\u00db\u00dc\3\2\2\2\u00dc\64\3\2\2\2\u00dd\u00df\t\t\2\2\u00de\u00dd"+
		"\3\2\2\2\u00df\u00e0\3\2\2\2\u00e0\u00de\3\2\2\2\u00e0\u00e1\3\2\2\2\u00e1"+
		"\u00e2\3\2\2\2\u00e2\u00e3\b\33\2\2\u00e3\66\3\2\2\2\u00e4\u00e6\7\17"+
		"\2\2\u00e5\u00e7\7\f\2\2\u00e6\u00e5\3\2\2\2\u00e6\u00e7\3\2\2\2\u00e7"+
		"\u00ea\3\2\2\2\u00e8\u00ea\7\f\2\2\u00e9\u00e4\3\2\2\2\u00e9\u00e8\3\2"+
		"\2\2\u00ea\u00eb\3\2\2\2\u00eb\u00ec\b\34\2\2\u00ec8\3\2\2\2\u00ed\u00ee"+
		"\7\61\2\2\u00ee\u00ef\7\61\2\2\u00ef\u00f3\3\2\2\2\u00f0\u00f2\n\n\2\2"+
		"\u00f1\u00f0\3\2\2\2\u00f2\u00f5\3\2\2\2\u00f3\u00f1\3\2\2\2\u00f3\u00f4"+
		"\3\2\2\2\u00f4\u00f6\3\2\2\2\u00f5\u00f3\3\2\2\2\u00f6\u00f7\b\35\2\2"+
		"\u00f7:\3\2\2\2\u00f8\u00f9\7\61\2\2\u00f9\u00fa\7,\2\2\u00fa\u00fe\3"+
		"\2\2\2\u00fb\u00fd\13\2\2\2\u00fc\u00fb\3\2\2\2\u00fd\u0100\3\2\2\2\u00fe"+
		"\u00ff\3\2\2\2\u00fe\u00fc\3\2\2\2\u00ff\u0101\3\2\2\2\u0100\u00fe\3\2"+
		"\2\2\u0101\u0102\7,\2\2\u0102\u0103\7\61\2\2\u0103\u0104\3\2\2\2\u0104"+
		"\u0105\b\36\2\2\u0105<\3\2\2\2\25\2\u0092\u0096\u009c\u00a0\u00a8\u00ac"+
		"\u00b2\u00bd\u00c1\u00c9\u00cf\u00d6\u00db\u00e0\u00e6\u00e9\u00f3\u00fe"+
		"\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}