package com.abstratt.sample.calories

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import java.net.URI
import java.net.URLEncoder
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import kotlin.reflect.KClass

class MealTests : AbstractAPITest() {

    @Before
    fun setUp() {
        this.username = "regular01"
        loadUser()
    }


    override fun getBaseUrl(): URI {
        return super.getRootUrl().resolve("users/{userId}/meals/".replace("{userId}", user!!.id.toString()))
    }

    @Test
    fun createAndRetrieve() {
        val toCreate = MealDTO(description = "1 cup of milk", date = LocalDate.of(2018, Month.FEBRUARY, 15), time = LocalTime.of(14, 41), calories = 200)
        val created = exchangeDTO(getBaseUrl(), HttpMethod.POST, toCreate, MealDTO::class, HttpStatus.CREATED)!!
        assertNotNull(created.id)
        assertNotNull(created.url)

        val retrieved = exchangeDTO(URI.create(created.url), HttpMethod.GET, null, MealDTO::class, HttpStatus.OK)
        assertEquals(created, retrieved)

        created.id = null
        assertEquals(toCreate, created)
    }

    @Test
    fun regularUser_OwnMeals() {

        val toCreate = MealDTO(description = "1 cup of milk", date = LocalDate.of(2018, Month.FEBRUARY, 15), time = LocalTime.of(14, 41), calories = 200)
        val user1Meal1 = exchangeDTO(getBaseUrl(), HttpMethod.POST, toCreate, MealDTO::class, HttpStatus.CREATED)!!

        this.username = "regular02"
        loadUser()

        val mealsBefore = exchangeDTO(getBaseUrl(), HttpMethod.GET, null, PageOfMealsDTO::class, HttpStatus.OK)

        val user2Meal1 = exchangeDTO(getBaseUrl(), HttpMethod.POST, toCreate, MealDTO::class, HttpStatus.CREATED)!!
        val user2Meal2 = exchangeDTO(getBaseUrl(), HttpMethod.POST, toCreate, MealDTO::class, HttpStatus.CREATED)!!

        val mealsAfter = exchangeDTO(getBaseUrl(), HttpMethod.GET, null, PageOfMealsDTO::class, HttpStatus.OK)
        assertEquals(2, mealsAfter!!.items.size - mealsBefore!!.items.size)
        assertEquals(1, mealsAfter.items.filter { it.url == user2Meal1.url }.size)
        assertEquals(1, mealsAfter.items.filter { it.url == user2Meal2.url }.size)
        assertTrue(mealsAfter.items.filter { it.url == user1Meal1.url }.none())
    }

    @Test
    fun regularUser_OwnMeals_CustomFilters() {
        val getDescription : (Int) -> String = { "${getUniqueTag()} - $it cup(s) of milk" }

        val meals = 0.rangeTo(4).map { index ->
            exchangeDTO(getBaseUrl(), HttpMethod.POST, MealDTO(
                    description = getDescription(index),
                    date = LocalDate.of(2018, Month.FEBRUARY, 1 + index),
                    calories = (1 + index) * 100
            ), MealDTO::class, HttpStatus.CREATED)!!.url
        }

        assertEquals(meals.subList(3, 5), filterMealUrls("description ge '${getDescription(3)}'"))
        assertEquals(meals.subList(1, 2), filterMealUrls("calories eq 200"))
        assertEquals(listOf(meals[1], meals[3], meals[4]), filterMealUrls("calories eq 200 OR calories ge 400"))
        assertEquals(listOf(meals[0], meals[4]), filterMealUrls("mealDate lt '2018-02-02' OR mealDate gt '2018-02-04'"))
        assertEquals(emptyList<String>(), filterMealUrls("mealDate lt '2018-02-02' AND mealDate gt '2018-02-04'"))
    }


    private inline fun <reified DTO : Any?> filterMeals(filter: String, expectedStatus: HttpStatus = HttpStatus.OK, dtoClass: KClass<Any> = PageOfMealsDTO::class as KClass<Any>) : DTO =
        exchangeDTO(getBaseUrl().resolve("?filter=" + URLEncoder.encode(filter, "UTF8")), HttpMethod.GET, null, dtoClass, expectedStatus) as DTO

    private fun filterMealUrls(filter: String) =
        (filterMeals(filter, HttpStatus.OK) as PageOfMealsDTO).items.map { it.url }

    class PageOfMealsDTO : PageDto<MealDTO>()

    @Test
    fun regularUser_OtherUsersMeals() {
        this.username = "regular01"
        val user1MealsURI = getBaseUrl()
        val toCreate = MealDTO(description = "1 cup of milk", date = LocalDate.of(2018, Month.FEBRUARY, 15), time = LocalTime.of(14, 41), calories = 200)
        val user1Meal = exchangeDTO(getBaseUrl(), HttpMethod.POST, toCreate, MealDTO::class, HttpStatus.CREATED)!!

        this.username = "regular02"
        loadUser()

        val user2Meal = exchangeDTO(getBaseUrl(), HttpMethod.POST, toCreate, MealDTO::class, HttpStatus.CREATED)!!

        // can get own meal/meals
        exchangeDTO(URI.create(user2Meal.url), HttpMethod.GET, null, MealDTO::class, HttpStatus.OK)
        exchangeDTO(getBaseUrl(), HttpMethod.GET, null, Void::class, HttpStatus.OK)

        // cannot get other users' meals
        exchangeDTO(URI.create(user1Meal.url), HttpMethod.GET, null, MealDTO::class, HttpStatus.FORBIDDEN)
        exchangeDTO(user1MealsURI, HttpMethod.GET, null, MealDTO::class, HttpStatus.FORBIDDEN)
    }

    @Test
    fun userManager_OtherUsersMeals() {
        val user1MealsURI = getBaseUrl()
        val toCreate = MealDTO(description = "1 cup of milk", date = LocalDate.of(2018, Month.FEBRUARY, 15), time = LocalTime.of(14, 41), calories = 200)
        val user1Meal = exchangeDTO(getBaseUrl(), HttpMethod.POST, toCreate, MealDTO::class, HttpStatus.CREATED)!!

        this.username = "manager01"
        loadUser()

        // cannot get other users' meals
        exchangeDTO(URI.create(user1Meal.url), HttpMethod.GET, null, MealDTO::class, HttpStatus.FORBIDDEN)
        exchangeDTO(user1MealsURI, HttpMethod.GET, null, MealDTO::class, HttpStatus.FORBIDDEN)
    }

    @Test
    fun administrator_OtherUsersMeals() {
        val user1MealsURI = getBaseUrl()
        val toCreate = MealDTO(description = "1 cup of milk", date = LocalDate.of(2018, Month.FEBRUARY, 15), time = LocalTime.of(14, 41), calories = 200)
        val user1Meal = exchangeDTO(getBaseUrl(), HttpMethod.POST, toCreate, MealDTO::class, HttpStatus.CREATED)!!

        this.username = "admin01"

        // can get other users' meals
        exchangeDTO(URI.create(user1Meal.url), HttpMethod.GET, null, MealDTO::class, HttpStatus.OK)
        exchangeDTO(user1MealsURI, HttpMethod.GET, null, MealDTO::class, HttpStatus.OK)
    }

    @Test
    fun createInvalid() {
        val toCreate = MealDTO()
        exchangeDTO(getBaseUrl(), HttpMethod.POST, toCreate, Unit::class, HttpStatus.BAD_REQUEST)
    }
}

