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
			"ParenClose", "Number", "BitCount", "Char", "EscapedChar", "String", 
			"StringChar", "Identifier", "Whitespace", "NL", "LineComment", "BlockComment"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\32\u00fd\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\3\2\3\2\3\2\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3"+
		"\6\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\13\3"+
		"\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3"+
		"\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17"+
		"\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\22\3\22\3\23\6\23\u008f"+
		"\n\23\r\23\16\23\u0090\3\23\3\23\5\23\u0095\n\23\3\23\3\23\6\23\u0099"+
		"\n\23\r\23\16\23\u009a\3\23\3\23\5\23\u009f\n\23\3\23\3\23\3\23\3\23\6"+
		"\23\u00a5\n\23\r\23\16\23\u00a6\3\23\3\23\5\23\u00ab\n\23\3\23\3\23\3"+
		"\23\3\23\5\23\u00b1\n\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\5\24\u00ba"+
		"\n\24\3\25\3\25\5\25\u00be\n\25\3\26\3\26\3\26\3\27\3\27\7\27\u00c5\n"+
		"\27\f\27\16\27\u00c8\13\27\3\27\3\27\3\30\3\30\5\30\u00ce\n\30\3\31\6"+
		"\31\u00d1\n\31\r\31\16\31\u00d2\3\32\6\32\u00d6\n\32\r\32\16\32\u00d7"+
		"\3\32\3\32\3\33\3\33\5\33\u00de\n\33\3\33\5\33\u00e1\n\33\3\33\3\33\3"+
		"\34\3\34\3\34\3\34\7\34\u00e9\n\34\f\34\16\34\u00ec\13\34\3\34\3\34\3"+
		"\35\3\35\3\35\3\35\7\35\u00f4\n\35\f\35\16\35\u00f7\13\35\3\35\3\35\3"+
		"\35\3\35\3\35\3\u00f5\2\36\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25"+
		"\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\2)\2+\2-\25/\2\61\26\63"+
		"\27\65\30\67\319\32\3\2\13\3\2\62;\4\2kkww\5\2\62;CHch\5\2\f\f\17\17)"+
		")\b\2$$\62\62^^ppttvv\5\2\f\f\17\17$$\7\2\13\f\17\17\"\"$$)+\4\2\13\13"+
		"\"\"\4\2\f\f\17\17\2\u010d\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2"+
		"\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2"+
		"\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3"+
		"\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2-\3\2\2\2\2\61\3\2\2\2\2\63\3"+
		"\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\3;\3\2\2\2\5>\3\2\2\2\7F\3"+
		"\2\2\2\tI\3\2\2\2\13O\3\2\2\2\rS\3\2\2\2\17W\3\2\2\2\21Z\3\2\2\2\23_\3"+
		"\2\2\2\25b\3\2\2\2\27h\3\2\2\2\31n\3\2\2\2\33w\3\2\2\2\35|\3\2\2\2\37"+
		"\u0082\3\2\2\2!\u0089\3\2\2\2#\u008b\3\2\2\2%\u00b0\3\2\2\2\'\u00b9\3"+
		"\2\2\2)\u00bd\3\2\2\2+\u00bf\3\2\2\2-\u00c2\3\2\2\2/\u00cd\3\2\2\2\61"+
		"\u00d0\3\2\2\2\63\u00d5\3\2\2\2\65\u00e0\3\2\2\2\67\u00e4\3\2\2\29\u00ef"+
		"\3\2\2\2;<\7/\2\2<=\7/\2\2=\4\3\2\2\2>?\7k\2\2?@\7p\2\2@A\7e\2\2AB\7n"+
		"\2\2BC\7w\2\2CD\7f\2\2DE\7g\2\2E\6\3\2\2\2FG\7h\2\2GH\7p\2\2H\b\3\2\2"+
		"\2IJ\7e\2\2JK\7q\2\2KL\7p\2\2LM\7u\2\2MN\7v\2\2N\n\3\2\2\2OP\7x\2\2PQ"+
		"\7c\2\2QR\7t\2\2R\f\3\2\2\2ST\7g\2\2TU\7p\2\2UV\7f\2\2V\16\3\2\2\2WX\7"+
		"k\2\2XY\7h\2\2Y\20\3\2\2\2Z[\7g\2\2[\\\7n\2\2\\]\7u\2\2]^\7g\2\2^\22\3"+
		"\2\2\2_`\7f\2\2`a\7q\2\2a\24\3\2\2\2bc\7y\2\2cd\7j\2\2de\7k\2\2ef\7n\2"+
		"\2fg\7g\2\2g\26\3\2\2\2hi\7d\2\2ij\7t\2\2jk\7g\2\2kl\7c\2\2lm\7m\2\2m"+
		"\30\3\2\2\2no\7e\2\2op\7q\2\2pq\7p\2\2qr\7v\2\2rs\7k\2\2st\7p\2\2tu\7"+
		"w\2\2uv\7g\2\2v\32\3\2\2\2wx\7v\2\2xy\7t\2\2yz\7w\2\2z{\7g\2\2{\34\3\2"+
		"\2\2|}\7h\2\2}~\7c\2\2~\177\7n\2\2\177\u0080\7u\2\2\u0080\u0081\7g\2\2"+
		"\u0081\36\3\2\2\2\u0082\u0083\7k\2\2\u0083\u0084\7p\2\2\u0084\u0085\7"+
		"n\2\2\u0085\u0086\7k\2\2\u0086\u0087\7p\2\2\u0087\u0088\7g\2\2\u0088 "+
		"\3\2\2\2\u0089\u008a\7*\2\2\u008a\"\3\2\2\2\u008b\u008c\7+\2\2\u008c$"+
		"\3\2\2\2\u008d\u008f\t\2\2\2\u008e\u008d\3\2\2\2\u008f\u0090\3\2\2\2\u0090"+
		"\u008e\3\2\2\2\u0090\u0091\3\2\2\2\u0091\u0094\3\2\2\2\u0092\u0093\t\3"+
		"\2\2\u0093\u0095\5\'\24\2\u0094\u0092\3\2\2\2\u0094\u0095\3\2\2\2\u0095"+
		"\u00b1\3\2\2\2\u0096\u0098\7/\2\2\u0097\u0099\t\2\2\2\u0098\u0097\3\2"+
		"\2\2\u0099\u009a\3\2\2\2\u009a\u0098\3\2\2\2\u009a\u009b\3\2\2\2\u009b"+
		"\u009e\3\2\2\2\u009c\u009d\7k\2\2\u009d\u009f\5\'\24\2\u009e\u009c\3\2"+
		"\2\2\u009e\u009f\3\2\2\2\u009f\u00b1\3\2\2\2\u00a0\u00a1\7\62\2\2\u00a1"+
		"\u00a2\7z\2\2\u00a2\u00a4\3\2\2\2\u00a3\u00a5\t\4\2\2\u00a4\u00a3\3\2"+
		"\2\2\u00a5\u00a6\3\2\2\2\u00a6\u00a4\3\2\2\2\u00a6\u00a7\3\2\2\2\u00a7"+
		"\u00aa\3\2\2\2\u00a8\u00a9\7w\2\2\u00a9\u00ab\5\'\24\2\u00aa\u00a8\3\2"+
		"\2\2\u00aa\u00ab\3\2\2\2\u00ab\u00b1\3\2\2\2\u00ac\u00ad\7)\2\2\u00ad"+
		"\u00ae\5)\25\2\u00ae\u00af\7)\2\2\u00af\u00b1\3\2\2\2\u00b0\u008e\3\2"+
		"\2\2\u00b0\u0096\3\2\2\2\u00b0\u00a0\3\2\2\2\u00b0\u00ac\3\2\2\2\u00b1"+
		"&\3\2\2\2\u00b2\u00ba\7:\2\2\u00b3\u00b4\7\63\2\2\u00b4\u00ba\78\2\2\u00b5"+
		"\u00b6\7\65\2\2\u00b6\u00ba\7\64\2\2\u00b7\u00b8\78\2\2\u00b8\u00ba\7"+
		"\66\2\2\u00b9\u00b2\3\2\2\2\u00b9\u00b3\3\2\2\2\u00b9\u00b5\3\2\2\2\u00b9"+
		"\u00b7\3\2\2\2\u00ba(\3\2\2\2\u00bb\u00be\n\5\2\2\u00bc\u00be\5+\26\2"+
		"\u00bd\u00bb\3\2\2\2\u00bd\u00bc\3\2\2\2\u00be*\3\2\2\2\u00bf\u00c0\7"+
		"^\2\2\u00c0\u00c1\t\6\2\2\u00c1,\3\2\2\2\u00c2\u00c6\7$\2\2\u00c3\u00c5"+
		"\5/\30\2\u00c4\u00c3\3\2\2\2\u00c5\u00c8\3\2\2\2\u00c6\u00c4\3\2\2\2\u00c6"+
		"\u00c7\3\2\2\2\u00c7\u00c9\3\2\2\2\u00c8\u00c6\3\2\2\2\u00c9\u00ca\7$"+
		"\2\2\u00ca.\3\2\2\2\u00cb\u00ce\n\7\2\2\u00cc\u00ce\5+\26\2\u00cd\u00cb"+
		"\3\2\2\2\u00cd\u00cc\3\2\2\2\u00ce\60\3\2\2\2\u00cf\u00d1\n\b\2\2\u00d0"+
		"\u00cf\3\2\2\2\u00d1\u00d2\3\2\2\2\u00d2\u00d0\3\2\2\2\u00d2\u00d3\3\2"+
		"\2\2\u00d3\62\3\2\2\2\u00d4\u00d6\t\t\2\2\u00d5\u00d4\3\2\2\2\u00d6\u00d7"+
		"\3\2\2\2\u00d7\u00d5\3\2\2\2\u00d7\u00d8\3\2\2\2\u00d8\u00d9\3\2\2\2\u00d9"+
		"\u00da\b\32\2\2\u00da\64\3\2\2\2\u00db\u00dd\7\17\2\2\u00dc\u00de\7\f"+
		"\2\2\u00dd\u00dc\3\2\2\2\u00dd\u00de\3\2\2\2\u00de\u00e1\3\2\2\2\u00df"+
		"\u00e1\7\f\2\2\u00e0\u00db\3\2\2\2\u00e0\u00df\3\2\2\2\u00e1\u00e2\3\2"+
		"\2\2\u00e2\u00e3\b\33\2\2\u00e3\66\3\2\2\2\u00e4\u00e5\7\61\2\2\u00e5"+
		"\u00e6\7\61\2\2\u00e6\u00ea\3\2\2\2\u00e7\u00e9\n\n\2\2\u00e8\u00e7\3"+
		"\2\2\2\u00e9\u00ec\3\2\2\2\u00ea\u00e8\3\2\2\2\u00ea\u00eb\3\2\2\2\u00eb"+
		"\u00ed\3\2\2\2\u00ec\u00ea\3\2\2\2\u00ed\u00ee\b\34\2\2\u00ee8\3\2\2\2"+
		"\u00ef\u00f0\7\61\2\2\u00f0\u00f1\7,\2\2\u00f1\u00f5\3\2\2\2\u00f2\u00f4"+
		"\13\2\2\2\u00f3\u00f2\3\2\2\2\u00f4\u00f7\3\2\2\2\u00f5\u00f6\3\2\2\2"+
		"\u00f5\u00f3\3\2\2\2\u00f6\u00f8\3\2\2\2\u00f7\u00f5\3\2\2\2\u00f8\u00f9"+
		"\7,\2\2\u00f9\u00fa\7\61\2\2\u00fa\u00fb\3\2\2\2\u00fb\u00fc\b\35\2\2"+
		"\u00fc:\3\2\2\2\24\2\u0090\u0094\u009a\u009e\u00a6\u00aa\u00b0\u00b9\u00bd"+
		"\u00c6\u00cd\u00d2\u00d7\u00dd\u00e0\u00ea\u00f5\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}