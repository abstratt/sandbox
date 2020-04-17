package com.abstratt.sample.uriql


data class CustomFilter(val rootExpression : Expression)

abstract class Expression()

enum class BooleanOperator {
    AND, OR
}

enum class ComparisonOperator {
    EQ, NE, LT, LE, GT, GE
}

data class CompositeExpression(val left : Expression, val operator : BooleanOperator, val right : Expression) : Expression()

data class ComparisonExpression(val property : String, val operator : ComparisonOperator, val value : String) : Expression()
