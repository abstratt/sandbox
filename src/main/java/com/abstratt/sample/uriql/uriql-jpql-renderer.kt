package com.abstratt.sample.uriql

import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.domain.Specifications
import java.time.LocalDate
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.withNullability

class RenderingException (message : String) : Exception(message)

fun <T : Any> buildSpecification(entity: KClass<T>, customFilter: CustomFilter): Specification<T> =
        CustomFilterSpecification(entity, customFilter)

fun <T : Any> convertValue(entity : KClass<T>, value: String, property: String): Comparable<Any>? {
    val entityProperty = entity.memberProperties.find { it.name == property }
    if (entityProperty == null) {
        throw RenderingException("No property named ${property}")
    }
    if (entityProperty.visibility != KVisibility.PUBLIC) {
        throw RenderingException("No public property named ${property}")
    }

    val converter : (String) -> Comparable<Any>? = findConverter(entityProperty.returnType.withNullability(false).classifier as KClass<*>)
    return converter(value)
}

fun <R : Any> findConverter(returnType: KClass<*>): (String) -> Comparable<R>? =
    when {
        returnType.java.isEnum() -> { value: String ->
            val asEnumValue : java.lang.Enum<*>? =  returnType.java.enumConstants.map { it as java.lang.Enum<*> }.firstOrNull { it.name().equals(value) }
            val result = (if (asEnumValue == null) returnType.java.enumConstants[0]  else asEnumValue)
            result as Comparable<R>
        }
        returnType == String::class -> { value: String -> value as Comparable<R>? }
        returnType == Int::class -> { value: String -> value.toInt() as Comparable<R>? }
        returnType == LocalDate::class -> { value: String -> LocalDate.parse(value) as Comparable<R>? }
        else -> { value: String -> value as Comparable<R>? }
    }

class CustomFilterSpecification<T : Any>(
        private val entity : KClass<T>,
        private val customFilter: CustomFilter?
    ) : Specification<T> {
    override fun toPredicate(root: Root<T>, query: CriteriaQuery<*>, builder: CriteriaBuilder): Predicate {
        return Builder(entity, root, query, builder).buildSpecification(customFilter?.rootExpression)
    }

    class Builder<T : Any>(val entity : KClass<T>, val root: Root<*>, val query: CriteriaQuery<*>, val builder: CriteriaBuilder) {
        fun buildQuery(expression: Expression?) : Predicate {
            return when(expression) {
                is ComparisonExpression -> buildQuery(expression)
                is CompositeExpression -> buildQuery(expression)
                else -> builder.isTrue(builder.literal(true))
            }
        }
        private fun buildQuery(expression: ComparisonExpression): Predicate {
            val propertyRef : javax.persistence.criteria.Expression<Comparable<Any>> = root.get(expression.property)
            val value : Comparable<Any>? = convertValue(entity, expression.value, expression.property)
            if (value == null) {
                throw RenderingException("Could not obtain value for ${expression.property}")
            }
            return when(expression.operator) {
                ComparisonOperator.GE -> builder.greaterThanOrEqualTo(propertyRef, value)
                ComparisonOperator.GT -> builder.greaterThan(propertyRef, value)
                ComparisonOperator.LT -> builder.lessThan(propertyRef, value)
                ComparisonOperator.LE -> builder.lessThanOrEqualTo(propertyRef, value)
                ComparisonOperator.EQ -> builder.equal(propertyRef, value)
                ComparisonOperator.NE -> builder.notEqual(propertyRef, value)
            }
        }

        private fun buildQuery(expression: CompositeExpression): Predicate {
            val leftPredicate = buildQuery(expression.left)
            val rightPredicate = buildQuery(expression.right)
            return when(expression.operator) {
                BooleanOperator.AND -> builder.and(leftPredicate, rightPredicate)
                BooleanOperator.OR -> builder.or(leftPredicate, rightPredicate)
            }
        }

        fun buildSpecification(rootExpression: Expression?): Predicate {
            return buildQuery(rootExpression)
        }

    }
}

class JPQLRenderer(val entity : KClass<*>) {

    fun render(uriql : CustomFilter) : Pair<String, Map<String, Any>> {
        val parameters = LinkedHashMap<String, Any>()
        val jpql = renderExpression(uriql.rootExpression, parameters)
        return Pair(jpql, parameters)
    }

    private fun renderExpression(expression: Expression, parameters : MutableMap<String, Any>): String =
        when(expression) {
            is ComparisonExpression -> renderComparisonExpression(expression, parameters)
            is CompositeExpression -> renderCompositeExpression(expression, parameters)
            else -> throw IllegalArgumentException(expression.toString())
        }

    private fun renderCompositeExpression(expression: CompositeExpression, parameters: MutableMap<String, Any>): String {
        return "${renderExpression(expression.left, parameters)} ${renderOperator(expression.operator)} ${renderExpression(expression.right, parameters)}"
    }

    private fun renderComparisonExpression(expression: ComparisonExpression, parameters: MutableMap<String, Any>): String {
        val key = "_arg_" + expression.property
        val convertedValue = convertValue(this.entity, expression.value, expression.property)
        if (convertedValue != null) {
            parameters.put(key, convertedValue)
        }
        return "${expression.property} ${renderOperator(expression.operator)} :${key}"
    }

    private fun renderOperator(operator: ComparisonOperator): String =
        when(operator) {
            ComparisonOperator.EQ -> "="
            ComparisonOperator.NE -> "<>"
            ComparisonOperator.GE -> ">="
            ComparisonOperator.GT -> ">"
            ComparisonOperator.LE -> "<="
            ComparisonOperator.LT -> "<"
        }

    private fun renderOperator(operator: BooleanOperator): String =
        when(operator) {
            BooleanOperator.AND -> "AND"
            BooleanOperator.OR -> "OR"
        }

}