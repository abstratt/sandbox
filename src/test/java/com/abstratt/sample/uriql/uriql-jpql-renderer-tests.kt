package com.abstratt.sample.uriql

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class SimpleEntity(
    var description: String? = null,
    var date: LocalDate? = null,
    var calories: Int? = null
)

class JPQLRenderingTests : AbstractParsingTests() {
    fun render(uriql : String) : Pair<String, Map<String, Any>> =
        JPQLRenderer(SimpleEntity::class).render(FilterParser().parse(uriql))

    @Test
    fun comparison_date() {
        val asJPQL = render("date eq '2016-05-01'")
        assertEquals("date = :_arg_date", asJPQL.first)
        assertEquals(mapOf(Pair("_arg_date", LocalDate.of(2016, 5, 1))), asJPQL.second)
    }

    @Test
    fun comparison_int() {
        val asJPQL = render("calories lt 200")
        assertEquals("calories < :_arg_calories", asJPQL.first)
        assertEquals(mapOf(Pair("_arg_calories", 200)), asJPQL.second)
    }


    @Test
    fun composite() {
        val asJPQL = render("date eq '2016-05-01' AND calories gt 20")
        assertEquals("date = :_arg_date AND calories > :_arg_calories", asJPQL.first)
        assertEquals(mapOf(Pair("_arg_date", LocalDate.of(2016, 5, 1)), Pair("_arg_calories", 20)), asJPQL.second)
    }
}