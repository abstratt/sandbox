// Generated from /home/rafael/source/abstratt-calories/src/main/resources/URIQL.g4 by ANTLR 4.7
package com.abstratt.sample.uriql.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link URIQLParser}.
 */
public interface URIQLListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link URIQLParser#query}.
	 * @param ctx the parse tree
	 */
	void enterQuery(URIQLParser.QueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link URIQLParser#query}.
	 * @param ctx the parse tree
	 */
	void exitQuery(URIQLParser.QueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link URIQLParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(URIQLParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link URIQLParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(URIQLParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link URIQLParser#compositeExpression}.
	 * @param ctx the parse tree
	 */
	void enterCompositeExpression(URIQLParser.CompositeExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link URIQLParser#compositeExpression}.
	 * @param ctx the parse tree
	 */
	void exitCompositeExpression(URIQLParser.CompositeExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link URIQLParser#resolvedExpression}.
	 * @param ctx the parse tree
	 */
	void enterResolvedExpression(URIQLParser.ResolvedExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link URIQLParser#resolvedExpression}.
	 * @param ctx the parse tree
	 */
	void exitResolvedExpression(URIQLParser.ResolvedExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link URIQLParser#nestedExpression}.
	 * @param ctx the parse tree
	 */
	void enterNestedExpression(URIQLParser.NestedExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link URIQLParser#nestedExpression}.
	 * @param ctx the parse tree
	 */
	void exitNestedExpression(URIQLParser.NestedExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link URIQLParser#comparisonExpression}.
	 * @param ctx the parse tree
	 */
	void enterComparisonExpression(URIQLParser.ComparisonExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link URIQLParser#comparisonExpression}.
	 * @param ctx the parse tree
	 */
	void exitComparisonExpression(URIQLParser.ComparisonExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link URIQLParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void enterComparisonOperator(URIQLParser.ComparisonOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link URIQLParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void exitComparisonOperator(URIQLParser.ComparisonOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link URIQLParser#booleanOperator}.
	 * @param ctx the parse tree
	 */
	void enterBooleanOperator(URIQLParser.BooleanOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link URIQLParser#booleanOperator}.
	 * @param ctx the parse tree
	 */
	void exitBooleanOperator(URIQLParser.BooleanOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link URIQLParser#property}.
	 * @param ctx the parse tree
	 */
	void enterProperty(URIQLParser.PropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link URIQLParser#property}.
	 * @param ctx the parse tree
	 */
	void exitProperty(URIQLParser.PropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link URIQLParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(URIQLParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link URIQLParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(URIQLParser.LiteralContext ctx);
}