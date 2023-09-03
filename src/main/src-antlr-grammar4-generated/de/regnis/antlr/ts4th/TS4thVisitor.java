// Generated from grammar/TS4th.g4 by ANTLR 4.7.2
package de.regnis.antlr.ts4th;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link TS4thParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface TS4thVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link TS4thParser#root}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoot(TS4thParser.RootContext ctx);
	/**
	 * Visit a parse tree produced by {@link TS4thParser#rootItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRootItem(TS4thParser.RootItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link TS4thParser#include}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInclude(TS4thParser.IncludeContext ctx);
	/**
	 * Visit a parse tree produced by {@link TS4thParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaration(TS4thParser.DeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link TS4thParser#typeList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeList(TS4thParser.TypeListContext ctx);
	/**
	 * Visit a parse tree produced by {@link TS4thParser#instructions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstructions(TS4thParser.InstructionsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code number}
	 * labeled alternative in {@link TS4thParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(TS4thParser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by the {@code string}
	 * labeled alternative in {@link TS4thParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(TS4thParser.StringContext ctx);
	/**
	 * Visit a parse tree produced by the {@code true}
	 * labeled alternative in {@link TS4thParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrue(TS4thParser.TrueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code false}
	 * labeled alternative in {@link TS4thParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFalse(TS4thParser.FalseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code identifier}
	 * labeled alternative in {@link TS4thParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(TS4thParser.IdentifierContext ctx);
	/**
	 * Visit a parse tree produced by the {@code if}
	 * labeled alternative in {@link TS4thParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf(TS4thParser.IfContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ifElse}
	 * labeled alternative in {@link TS4thParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfElse(TS4thParser.IfElseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code while}
	 * labeled alternative in {@link TS4thParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile(TS4thParser.WhileContext ctx);
	/**
	 * Visit a parse tree produced by the {@code break}
	 * labeled alternative in {@link TS4thParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreak(TS4thParser.BreakContext ctx);
	/**
	 * Visit a parse tree produced by the {@code continue}
	 * labeled alternative in {@link TS4thParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinue(TS4thParser.ContinueContext ctx);
}