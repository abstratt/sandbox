package com.abstratt.sample.uriql

import com.abstratt.sample.uriql.antlr.URIQLLexer
import com.abstratt.sample.uriql.antlr.URIQLParser
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.Parser
import org.antlr.v4.runtime.atn.ATNConfigSet
import org.antlr.v4.runtime.dfa.DFA
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.pattern.RuleTagToken
import java.util.*


class FilterParser {
    class ParsingException (errorMessage : String, position : Int) : Exception(errorMessage + " at " + position)
    fun parse(s: String) : CustomFilter {
        val lexer = URIQLLexer(CharStreams.fromString(s))
        val parser = URIQLParser(UnbufferedTokenStream<RuleTagToken>(lexer))
        parser.addErrorListener(ErrorListener({ message, position -> throw ParsingException(message, position) }))
        val query = QueryBuilder().buildQuery(parser.query())
        return query
    }

}

class ErrorListener(val collector : (String, Int) -> Unit) : ANTLRErrorListener {
    override fun reportAttemptingFullContext(recognizer: Parser?, dfa: DFA?, startIndex: Int, stopIndex: Int, conflictingAlts: BitSet?, configs: ATNConfigSet?) {
    }

    override fun syntaxError(recognizer: Recognizer<*, *>?, offendingSymbol: Any?, line: Int, charPositionInLine: Int, msg: String?, e: RecognitionException?) {
        collector(msg!!, charPositionInLine)
    }

    override fun reportAmbiguity(recognizer: Parser?, dfa: DFA?, startIndex: Int, stopIndex: Int, exact: Boolean, ambigAlts: BitSet?, configs: ATNConfigSet?) {
    }

    override fun reportContextSensitivity(recognizer: Parser?, dfa: DFA?, startIndex: Int, stopIndex: Int, prediction: Int, configs: ATNConfigSet?) {
    }
}

class QueryBuilder {
    fun buildQuery(query: URIQLParser.QueryContext): CustomFilter {
        val root = buildExpression(query.expression())
        return CustomFilter(root)
    }
    private fun buildExpression(expression: ParseTree): Expression {
        val unwrapped = expression
        return when (unwrapped) {
            is URIQLParser.ComparisonExpressionContext -> buildComparisonExpression(unwrapped)
            is URIQLParser.CompositeExpressionContext -> buildCompositeExpression(unwrapped)
            is URIQLParser.NestedExpressionContext -> buildExpression(unwrapped.expression())
            is URIQLParser.ExpressionContext -> buildExpression(unwrapped.getChild(0))
            is URIQLParser.ResolvedExpressionContext -> buildResolvedExpression(unwrapped)
            else -> throw IllegalArgumentException(unwrapped.javaClass.name)
        }
    }

    private fun buildResolvedExpression(expression: URIQLParser.ResolvedExpressionContext): Expression =
            when (expression.getChild(0)) {
                is URIQLParser.ComparisonExpressionContext -> buildComparisonExpression(expression.comparisonExpression())
                is URIQLParser.NestedExpressionContext -> buildExpression(expression.nestedExpression())
                else -> throw IllegalArgumentException(expression.javaClass.name)
            }


    private fun buildCompositeExpression(expression: URIQLParser.CompositeExpressionContext): Expression =
            buildCompositeExpression(expression, expression.childCount / 2 - 1)

    private fun buildCompositeExpression(expression: URIQLParser.CompositeExpressionContext, index : Int): Expression =
        if (index == 0)
            CompositeExpression(
                    buildExpression(expression.resolvedExpression(0)),
                    parseLogicalOperator(expression.booleanOperator(0)),
                    buildExpression(expression.resolvedExpression(1) as URIQLParser.ResolvedExpressionContext)
            )
        else
            CompositeExpression(
                    buildCompositeExpression(expression, index - 1),
                    parseLogicalOperator(expression.booleanOperator(index)),
                    buildResolvedExpression(expression.resolvedExpression(index) as URIQLParser.ResolvedExpressionContext)
            )

    private fun parseLogicalOperator(booleanOperator: URIQLParser.BooleanOperatorContext): BooleanOperator =
        BooleanOperator.valueOf(booleanOperator.text.toUpperCase())


    private fun buildComparisonExpression(expression: URIQLParser.ComparisonExpressionContext) =
            ComparisonExpression(
                    parsePropertyName(expression.property()),
                    parseComparisonOperator(expression.comparisonOperator()),
                    parseLiteral(expression.literal()))

    private fun parsePropertyName(property: URIQLParser.PropertyContext): String = property.text
    private fun parseComparisonOperator(operator: URIQLParser.ComparisonOperatorContext): ComparisonOperator =
        ComparisonOperator.valueOf(operator.text.toUpperCase())
    private fun parseLiteral(literal: URIQLParser.LiteralContext): String =
            literal.NUM_LITERAL()?.text ?: literal.CHAR_LITERAL().text.substring(1).dropLast(1)

}
