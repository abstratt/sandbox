package com.abstratt.sample.calories

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.abstratt.sample.uriql.CustomFilter
import org.apache.commons.lang3.StringUtils
import org.aspectj.weaver.tools.cache.SimpleCacheFactory.path
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalTime
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class UserDTO(
        override var id: Long? = null,
        @field:NotNull
        var username: String? = null,
        var password: String? = null,
        @field:NotNull
        var role: SecurityRole? = null
) : BaseDTO(id = id)

data class MealDTO(
        override var id: Long? = null,
        @field:NotNull
        var description: String? = null,

        @JsonFormat(pattern = "yyyy/MM/dd")
        @field:NotNull
        var date: LocalDate? = null,

        @JsonFormat(pattern = "hh:mm")
        var time: LocalTime? = null,

        var calories: Int? = null,
        var lessThanExpected: Boolean? = null,
        @JsonIgnore
        var userId: Long? = null
) : BaseDTO(id = id)

@RestController
open class MealController : BaseController<Meal, MealDTO, MealService>() {
    override fun toDto(source: Meal): MealDTO = MealDTO(id = source.id, description = source.description, calories = source.calories, date = source.mealDate, time = source.mealTime, userId = source.user?.id)
    override fun fromDto(source: MealDTO) = Meal(id = source.id, description = source.description, calories = source.calories, mealDate = source.date, mealTime = source.time)

    override fun getBasePath(): String = "users/{userId}/meals"

    @RequestMapping(path = ["api/users/{userId}/meals/{mealId}"], method = arrayOf(RequestMethod.GET))
    @Secured(SecurityRole.Constants.REGULAR_ROLE, SecurityRole.Constants.ADMINISTRATOR_ROLE)
    fun get(@PathVariable("userId") userId: String, @PathVariable("mealId") mealId: String): HttpEntity<MealDTO> {
        if (isCurrentRole(SecurityRole.Regular)) {
            APIException.ensure(userId.equals(getCurrentUserId()?.toString()), { Pair(HttpStatus.FORBIDDEN, ErrorMessage(null, null)) })
        }
        return doGet(mealId)
    }

    @RequestMapping(path = ["api/users/{userId}/meals/{mealId}"], method = arrayOf(RequestMethod.DELETE))
    @Secured(SecurityRole.Constants.REGULAR_ROLE, SecurityRole.Constants.ADMINISTRATOR_ROLE)
    open fun delete(@PathVariable("userId") userId: String, @PathVariable("mealId") mealId: String): HttpEntity<Void> {
        if (isCurrentRole(SecurityRole.Regular)) {
            APIException.ensure(userId.equals(getCurrentUserId()?.toString()), { Pair(HttpStatus.FORBIDDEN, ErrorMessage(null, null)) })
        }
        return doDelete(mealId)
    }

    @RequestMapping(path = ["api/users/{userId}/meals/"], method = [RequestMethod.GET])
    @Secured(SecurityRole.Constants.REGULAR_ROLE, SecurityRole.Constants.ADMINISTRATOR_ROLE)
    open fun userMeals(@PathVariable("userId") userIdString: String, @RequestParam(required = false) filter: String?, @RequestParam(required = false) pageNumber: Int?, @RequestParam(required = false) pageSize: Int?): HttpEntity<PageDto<MealDTO>> {
        if (isCurrentRole(SecurityRole.Regular)) {
            APIException.ensure(userIdString.equals(getCurrentUserId()?.toString()), { Pair(HttpStatus.FORBIDDEN, ErrorMessage(null, null)) })
        }
        val userId = userIdString.toLong()
        return doList(parseFilter(filter), pageNumber, pageSize, { filter : CustomFilter?, page: Int?, limit: Int? -> service.findUserMeals(userId, filter, page, limit) })
    }

    @RequestMapping(path = ["api/users/{userId}/meals/"], method = [RequestMethod.POST], consumes = [MediaType.APPLICATION_JSON_VALUE])
    @Secured(SecurityRole.Constants.REGULAR_ROLE, SecurityRole.Constants.ADMINISTRATOR_ROLE)
    open fun create(@PathVariable("userId") userId: String,  @Valid @RequestBody dto: MealDTO): HttpEntity<MealDTO> {
        if (isCurrentRole(SecurityRole.Regular)) {
            APIException.ensure(userId.equals(getCurrentUserId()?.toString()), { Pair(HttpStatus.FORBIDDEN, ErrorMessage(null, null)) })
        }
        return doCreate(dto)
    }

    @RequestMapping(path = ["api/users/{userId}/meals/{mealId}"], method = arrayOf(RequestMethod.PUT), consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @Secured(SecurityRole.Constants.REGULAR_ROLE, SecurityRole.Constants.ADMINISTRATOR_ROLE)
    open fun update(@PathVariable("userId") userId: String, @PathVariable("mealId") mealId: String, @Valid @RequestBody dto: MealDTO): HttpEntity<MealDTO> {
        if (isCurrentRole(SecurityRole.Regular)) {
            APIException.ensure(userId.equals(getCurrentUserId()?.toString()), { Pair(HttpStatus.FORBIDDEN, ErrorMessage(null, null)) })
        }
        return doUpdate(mealId, dto)
    }

    override fun doCreate(dto: MealDTO): HttpEntity<MealDTO> {
        val created = service.createForCurrentUser(fromDto(dto))
        return ResponseEntity.status(HttpStatus.CREATED).body(toSingleDto(created))
    }

}

@RestController
class ApplicationUserController : BaseController<ApplicationUser, UserDTO, ApplicationUserService>() {
    override fun toDto(source: ApplicationUser) = UserDTO(id = source.id, username = source.username, role = source.role)

    override fun fromDto(source: UserDTO) = ApplicationUser(id = source.id, username = source.username, password = source.password, role = source.role)

    override fun getBasePath(): String = "users"

    @RequestMapping(path = ["api/users/{userId}"], method = [(RequestMethod.GET)])
    @Secured(SecurityRole.Constants.REGULAR_ROLE, SecurityRole.Constants.USER_MANAGER_ROLE, SecurityRole.Constants.ADMINISTRATOR_ROLE)
    fun get(@PathVariable("userId") userId : String): HttpEntity<UserDTO> {
        if (isCurrentRole(SecurityRole.Regular)) {
            APIException.ensure(userId.equals(getCurrentUserId()?.toString()), { Pair(HttpStatus.FORBIDDEN, ErrorMessage(null, null)) })
        }
        return doGet(userId)
    }

    @RequestMapping(path = ["api/users/{userId}"], method = [RequestMethod.DELETE])
    @Secured(SecurityRole.Constants.REGULAR_ROLE, SecurityRole.Constants.USER_MANAGER_ROLE, SecurityRole.Constants.ADMINISTRATOR_ROLE)
    open fun delete(@PathVariable("userId") userId: String): HttpEntity<Void> {
        if (isCurrentRole(SecurityRole.Regular)) {
            APIException.ensure(userId.equals(getCurrentUserId()?.toString()), { Pair(HttpStatus.FORBIDDEN, ErrorMessage(null, null)) })
        }
        return doDelete(userId)
    }

    @RequestMapping(path = ["api/users/"], method = [(RequestMethod.GET)])
    @Secured(SecurityRole.Constants.REGULAR_ROLE, SecurityRole.Constants.USER_MANAGER_ROLE, SecurityRole.Constants.ADMINISTRATOR_ROLE)
    open fun users(@RequestParam(required = false) filter: String?, @RequestParam(required = false) pageNumber: Int?, @RequestParam(required = false) pageSize: Int?): HttpEntity<PageDto<UserDTO>> {
        if (isCurrentRole(SecurityRole.Regular)) {
            val userId = getCurrentUserId()!!
            val fragment = "id eq ${userId}"
            return doList(parseFilter(if (StringUtils.isEmpty(filter)) fragment else "(${filter}) AND (${fragment})"), pageNumber, pageSize)
        }
        return doList(parseFilter(filter), pageNumber, pageSize)
    }

    @RequestMapping(path = ["api/users/"], method = [(RequestMethod.POST)], consumes = [(MediaType.APPLICATION_JSON_VALUE)])
    @Secured(SecurityRole.Constants.ANONYMOUS_ROLE, SecurityRole.Constants.USER_MANAGER_ROLE, SecurityRole.Constants.ADMINISTRATOR_ROLE)
    open fun create(@Valid @RequestBody dto: UserDTO): HttpEntity<UserDTO> {
        return doCreate(dto)
    }

    @RequestMapping(path = ["api/users/{userId}"], method = [(RequestMethod.PUT)], consumes = [(MediaType.APPLICATION_JSON_VALUE)])
    @Secured(SecurityRole.Constants.REGULAR_ROLE, SecurityRole.Constants.USER_MANAGER_ROLE, SecurityRole.Constants.ADMINISTRATOR_ROLE)
    open fun update(@PathVariable("userId") userId: String, @Valid @RequestBody dto: UserDTO): HttpEntity<UserDTO> {
        if (isCurrentRole(SecurityRole.Regular)) {
            APIException.ensure(userId.equals(getCurrentUserId()?.toString()), { Pair(HttpStatus.FORBIDDEN, ErrorMessage(null, null)) })
        }
        return doUpdate(userId, dto)
    }
}