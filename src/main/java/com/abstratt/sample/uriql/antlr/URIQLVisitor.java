// Generated from /home/rafael/source/abstratt-calories/src/main/resources/URIQL.g4 by ANTLR 4.7
package com.abstratt.sample.uriql.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link URIQLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface URIQLVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link URIQLParser#query}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuery(URIQLParser.QueryContext ctx);
	/**
	 * Visit a parse tree produced by {@link URIQLParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(URIQLParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link URIQLParser#compositeExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompositeExpression(URIQLParser.CompositeExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link URIQLParser#resolvedExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResolvedExpression(URIQLParser.ResolvedExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link URIQLParser#nestedExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNestedExpression(URIQLParser.NestedExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link URIQLParser#comparisonExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisonExpression(URIQLParser.ComparisonExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link URIQLParser#comparisonOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisonOperator(URIQLParser.ComparisonOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link URIQLParser#booleanOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanOperator(URIQLParser.BooleanOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link URIQLParser#property}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProperty(URIQLParser.PropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link URIQLParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(URIQLParser.LiteralContext ctx);
}