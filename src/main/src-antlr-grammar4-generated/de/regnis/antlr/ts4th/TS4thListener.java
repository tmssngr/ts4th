// Generated from grammar/TS4th.g4 by ANTLR 4.7.2
package de.regnis.antlr.ts4th;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link TS4thParser}.
 */
public interface TS4thListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link TS4thParser#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(TS4thParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link TS4thParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(TS4thParser.RootContext ctx);
	/**
	 * Enter a parse tree produced by {@link TS4thParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration(TS4thParser.DeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TS4thParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration(TS4thParser.DeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TS4thParser#typeList}.
	 * @param ctx the parse tree
	 */
	void enterTypeList(TS4thParser.TypeListContext ctx);
	/**
	 * Exit a parse tree produced by {@link TS4thParser#typeList}.
	 * @param ctx the parse tree
	 */
	void exitTypeList(TS4thParser.TypeListContext ctx);
	/**
	 * Enter a parse tree produced by {@link TS4thParser#instructions}.
	 * @param ctx the parse tree
	 */
	void enterInstructions(TS4thParser.InstructionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TS4thParser#instructions}.
	 * @param ctx the parse tree
	 */
	void exitInstructions(TS4thParser.InstructionsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code number}
	 * labeled alternative in {@link TS4thParser#instruction}.
	 * @param ctx the parse tree
	 */
	void enterNumber(TS4thParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by the {@code number}
	 * labeled alternative in {@link TS4thParser#instruction}.
	 * @param ctx the parse tree
	 */
	void exitNumber(TS4thParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by the {@code string}
	 * labeled alternative in {@link TS4thParser#instruction}.
	 * @param ctx the parse tree
	 */
	void enterString(TS4thParser.StringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code string}
	 * labeled alternative in {@link TS4thParser#instruction}.
	 * @param ctx the parse tree
	 */
	void exitString(TS4thParser.StringContext ctx);
	/**
	 * Enter a parse tree produced by the {@code true}
	 * labeled alternative in {@link TS4thParser#instruction}.
	 * @param ctx the parse tree
	 */
	void enterTrue(TS4thParser.TrueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code true}
	 * labeled alternative in {@link TS4thParser#instruction}.
	 * @param ctx the parse tree
	 */
	void exitTrue(TS4thParser.TrueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code false}
	 * labeled alternative in {@link TS4thParser#instruction}.
	 * @param ctx the parse tree
	 */
	void enterFalse(TS4thParser.FalseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code false}
	 * labeled alternative in {@link TS4thParser#instruction}.
	 * @param ctx the parse tree
	 */
	void exitFalse(TS4thParser.FalseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code identifier}
	 * labeled alternative in {@link TS4thParser#instruction}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(TS4thParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code identifier}
	 * labeled alternative in {@link TS4thParser#instruction}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(TS4thParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code if}
	 * labeled alternative in {@link TS4thParser#instruction}.
	 * @param ctx the parse tree
	 */
	void enterIf(TS4thParser.IfContext ctx);
	/**
	 * Exit a parse tree produced by the {@code if}
	 * labeled alternative in {@link TS4thParser#instruction}.
	 * @param ctx the parse tree
	 */
	void exitIf(TS4thParser.IfContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ifElse}
	 * labeled alternative in {@link TS4thParser#instruction}.
	 * @param ctx the parse tree
	 */
	void enterIfElse(TS4thParser.IfElseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ifElse}
	 * labeled alternative in {@link TS4thParser#instruction}.
	 * @param ctx the parse tree
	 */
	void exitIfElse(TS4thParser.IfElseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code while}
	 * labeled alternative in {@link TS4thParser#instruction}.
	 * @param ctx the parse tree
	 */
	void enterWhile(TS4thParser.WhileContext ctx);
	/**
	 * Exit a parse tree produced by the {@code while}
	 * labeled alternative in {@link TS4thParser#instruction}.
	 * @param ctx the parse tree
	 */
	void exitWhile(TS4thParser.WhileContext ctx);
	/**
	 * Enter a parse tree produced by the {@code break}
	 * labeled alternative in {@link TS4thParser#instruction}.
	 * @param ctx the parse tree
	 */
	void enterBreak(TS4thParser.BreakContext ctx);
	/**
	 * Exit a parse tree produced by the {@code break}
	 * labeled alternative in {@link TS4thParser#instruction}.
	 * @param ctx the parse tree
	 */
	void exitBreak(TS4thParser.BreakContext ctx);
	/**
	 * Enter a parse tree produced by the {@code continue}
	 * labeled alternative in {@link TS4thParser#instruction}.
	 * @param ctx the parse tree
	 */
	void enterContinue(TS4thParser.ContinueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code continue}
	 * labeled alternative in {@link TS4thParser#instruction}.
	 * @param ctx the parse tree
	 */
	void exitContinue(TS4thParser.ContinueContext ctx);
}