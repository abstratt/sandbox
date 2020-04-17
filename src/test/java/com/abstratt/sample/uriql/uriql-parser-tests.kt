package com.abstratt.sample.uriql

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName

abstract class AbstractParsingTests {
    @Rule
    @JvmField
    final val testName = TestName()


    fun parse(toParse: String) : CustomFilter {
        var query = FilterParser().parse(toParse)
        print(testName.methodName + ": " + query)
        return query
    }

}


class ParsingTests : AbstractParsingTests() {


    @Test
    fun comparison() {
        val query = parse("date eq '2016-05-01'")
        checkEqComparisonExpression(query.rootExpression)
    }

    @Test
    fun comparison_parenthesized() {
        val query = parse("((date eq '2016-05-01'))")
        checkEqComparisonExpression(query.rootExpression)
    }


    private fun checkEqComparisonExpression(expression : Expression) {
        assertTrue(expression is ComparisonExpression)
        val asComparison = expression as ComparisonExpression
        assertEquals("date", asComparison.property)
        assertEquals(ComparisonOperator.EQ, asComparison.operator)
        assertEquals("2016-05-01", asComparison.value)
    }


    @Test
    fun composite() {
        val query = parse("date eq '2016-05-01' AND number_of_calories gt 20")
        assertTrue(query.rootExpression is CompositeExpression)
        checkAndCompositeExpression(query.rootExpression as CompositeExpression)
    }

    @Test
    fun composite_parenthesized() {
        val query = parse("(date eq '2016-05-01') AND (number_of_calories gt 20)")
        assertTrue(query.rootExpression is CompositeExpression)
        checkAndCompositeExpression(query.rootExpression as CompositeExpression)
    }


    private fun checkAndCompositeExpression(expression: Expression) {
        assertTrue(expression is CompositeExpression)
        var asCompositeExpression = expression as CompositeExpression
        assertEquals(BooleanOperator.AND, asCompositeExpression.operator)
        assertTrue(asCompositeExpression.left.toString(), asCompositeExpression.left is ComparisonExpression)
        assertTrue(asCompositeExpression.right.toString(), asCompositeExpression.right is ComparisonExpression)
    }

    private fun checkOrCompositeExpression(expression: Expression) {
        assertTrue(expression is CompositeExpression)
        var asCompositeExpression = expression as CompositeExpression
        assertEquals(BooleanOperator.OR, asCompositeExpression.operator)
        assertTrue(asCompositeExpression.left.toString(), asCompositeExpression.left is ComparisonExpression)
        assertTrue(asCompositeExpression.right.toString(), asCompositeExpression.right is ComparisonExpression)
    }

    private fun checkCompositeExpressionWithTwoOperators(expression: Expression) {
        assertTrue(expression.javaClass.name, expression is CompositeExpression)
        val asCompositeExpression = expression as CompositeExpression
        assertEquals(BooleanOperator.OR, asCompositeExpression.operator)
        assertTrue(asCompositeExpression.left.toString(), asCompositeExpression.left is CompositeExpression)
        assertTrue(asCompositeExpression.right.toString(), asCompositeExpression.right is ComparisonExpression)
        checkAndCompositeExpression(asCompositeExpression.left as CompositeExpression)
        checkEqComparisonExpression(asCompositeExpression.right as ComparisonExpression)
    }

    @Test
    fun complex() {
        val expression = parse("(dates eq '2016-05-01') AND ((number_of_calories gt 20) OR (name ne 'john'))")
    }

    @Test
    fun invalid() {
        try {
            parse("foobar")
            fail("Should have failed")
        } catch (e : FilterParser.ParsingException) {
            // success
        }
    }
    @Test
    fun incomplete_input() {
        try {
            parse("date eq '2016-05-01' foo")
            fail("Should have failed")
        } catch (e : FilterParser.ParsingException) {
            // success
        }
    }


}