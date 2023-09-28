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
	 * Enter a parse tree produced by {@link TS4thParser#rootItem}.
	 * @param ctx the parse tree
	 */
	void enterRootItem(TS4thParser.RootItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link TS4thParser#rootItem}.
	 * @param ctx the parse tree
	 */
	void exitRootItem(TS4thParser.RootItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link TS4thParser#include}.
	 * @param ctx the parse tree
	 */
	void enterInclude(TS4thParser.IncludeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TS4thParser#include}.
	 * @param ctx the parse tree
	 */
	void exitInclude(TS4thParser.IncludeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TS4thParser#funcDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterFuncDeclaration(TS4thParser.FuncDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TS4thParser#funcDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitFuncDeclaration(TS4thParser.FuncDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TS4thParser#constDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterConstDeclaration(TS4thParser.ConstDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TS4thParser#constDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitConstDeclaration(TS4thParser.ConstDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TS4thParser#varDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVarDeclaration(TS4thParser.VarDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TS4thParser#varDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVarDeclaration(TS4thParser.VarDeclarationContext ctx);
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
	/**
	 * Enter a parse tree produced by the {@code var}
	 * labeled alternative in {@link TS4thParser#instruction}.
	 * @param ctx the parse tree
	 */
	void enterVar(TS4thParser.VarContext ctx);
	/**
	 * Exit a parse tree produced by the {@code var}
	 * labeled alternative in {@link TS4thParser#instruction}.
	 * @param ctx the parse tree
	 */
	void exitVar(TS4thParser.VarContext ctx);
}