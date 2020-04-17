package com.abstratt.sample.calories

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.text.SimpleDateFormat

@Component
class CaloryCalculationTask {

    @Autowired
    lateinit var calorieCalculationAndUpdatingService: CalorieCalculationAndUpdatingService

    @Scheduled(fixedRateString = "\${caloriesapp.calculation.intervalInMillis}")
    fun calculateMissingCalories() {
        log.info("Calculating missing calories")
        calorieCalculationAndUpdatingService.checkAndUpdateMissingCalories()
    }

    companion object {

        private val log = LoggerFactory.getLogger(CaloryCalculationTask::class.java)

        private val dateFormat = SimpleDateFormat("HH:mm:ss")
        @Service
        class CalorieCalculationAndUpdatingService {
            @Autowired
            lateinit var mealRepository: MealRepository

            @Autowired
            lateinit var calculationService: CalorieCalculationService

            @Autowired
            lateinit var updatingService: CalorieUpdatingService

            // we do not want to hold a database transaction during the remote request
            @Transactional(readOnly = true)
            fun checkAndUpdateMissingCalories() {
                val descriptionsMissingCalories = findDescriptionsNeedingCalories()
                if (descriptionsMissingCalories.isEmpty()) {
                    return
                }
                val caloriesCalculated = calculateMissingCalories(descriptionsMissingCalories)
                updatingService.updateMealsMissingCalories(caloriesCalculated)
            }
            fun calculateMissingCalories(mealDescriptions: List<String>) : Map<String, Int> {
                val calculated = LinkedHashMap(calculationService.calculate(mealDescriptions))
                if (calculated.size < mealDescriptions.size) {
                    // assign a default value for those we could not calculate
                    mealDescriptions.forEach {
                        calculated.computeIfAbsent(it, { 0 })
                    }
                }
                return calculated
            }

            fun findDescriptionsNeedingCalories(): List<String> =
                    mealRepository.findDescriptionsMissingCalories().take(100)
        }
    }
}


@Service
class CalorieUpdatingService {
    @Autowired
    lateinit var mealRepository: MealRepository

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public fun updateMealsMissingCalories(caloriesCalculated: Map<String, Int>) {
        caloriesCalculated.forEach { mealRepository.assignMissingCalories(it.value, it.key) }
    }
}


interface CalorieCalculationService {
    /**
     * Calculates calories for each of the meal descriptions.
     *
     * The resulting map may have less entries than the input list,
     * in case a hit is not found.
     *
     * @param mealDescriptions a list of meal descriptions to calculate calories for
     * @return a map from meal description to meal calories
     */
    fun calculate(mealDescriptions : List<String>) : Map<String, Int>
}



