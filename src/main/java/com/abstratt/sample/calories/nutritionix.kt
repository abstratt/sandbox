package com.abstratt.sample.calories

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.net.URI


@Service
class NutritionixCalorieCalculationService(
        val restTemplateBuilder: RestTemplateBuilder
) : CalorieCalculationService {
    private lateinit var restTemplate: RestTemplate
    @Value("\${caloriesapp.calculation.nutritionix.uri}")
    private lateinit var nutritionixURI: URI
    @Value("\${caloriesapp.calculation.nutritionix.applicationId}")
    private lateinit var nutritionixApplicationId: String
    @Value("\${caloriesapp.calculation.nutritionix.apiKey}")
    private lateinit var nutritionixApiKey: String

    init {
        restTemplate = restTemplateBuilder.build()
        restTemplate.interceptors.add(LoggingInterceptor())
    }

    companion object {
        private val logger = LoggerFactory.getLogger(NutritionixCalorieCalculationService::class.java.name)
    }

    class Request(val query: String? = null) {
        val line_delimited = true
        val use_raw_foods = true
        val use_branded_foods = true
    }

    class Response(val foods: List<Food>? = null) {
        class Food(val food_name: String? = null, val nf_calories: Double? = null, val metadata: FoodMetadata? = null) {
            class FoodMetadata(val original_input: String? = null)
        }
    }

    override fun calculate(mealDescriptions: List<String>): Map<String, Int> {
        val headers = HttpHeaders()
        headers.accept = listOf(MediaType.APPLICATION_JSON)
        headers.contentType = MediaType.APPLICATION_JSON
        headers.set("x-app-id", nutritionixApplicationId)
        headers.set("x-app-key", nutritionixApiKey)

        val request = Request(mealDescriptions.joinToString(separator = "\n"))
        val entity = HttpEntity(request, headers)
        try {
            val response = restTemplate.exchange(nutritionixURI, HttpMethod.POST, entity, Response::class.java)
            return response.body.foods?.filter {
                it.nf_calories != null && it.metadata?.original_input != null
            }?.associate {
                        Pair(it.metadata!!.original_input!!, it.nf_calories!!.toInt())
                    } ?: emptyMap<String, Int>()
        } catch (e : HttpClientErrorException) {
            logger.error("Error accessing Nutritionix API: ${e.statusCode}")
            return emptyMap()
        }
    }
}