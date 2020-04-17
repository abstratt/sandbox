package com.abstratt.sample.calories

import com.abstratt.sample.uriql.CustomFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.domain.Specifications
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime
import java.util.Optional.ofNullable
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.ManyToOne
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import kotlin.reflect.KClass

interface SecurityRoleConstants {
    companion object {
        const val REGULAR_NAME: String = "Regular"
        const val USER_MANAGER_NAME: String = "Manager"
        const val ADMINISTRATOR_NAME: String = "Administrator"
        const val ANONYMOUS_NAME: String = "Anonymous"

        const val ANONYMOUS_ROLE: String = "ROLE_${ANONYMOUS_NAME}"
        const val REGULAR_ROLE: String = "ROLE_${REGULAR_NAME}"
        const val USER_MANAGER_ROLE: String = "ROLE_${USER_MANAGER_NAME}"
        const val ADMINISTRATOR_ROLE: String = "ROLE_${ADMINISTRATOR_NAME}"
    }
}

enum class SecurityRole(val roleName: String) {
    Regular(Constants.REGULAR_NAME),
    Manager(Constants.USER_MANAGER_NAME),
    Administrator(Constants.ADMINISTRATOR_NAME),
    Anonymous(Constants.ANONYMOUS_NAME);

    val authority = "ROLE_${roleName}"

    object Constants {
        const val REGULAR_NAME: String = "Regular"
        const val USER_MANAGER_NAME: String = "Manager"
        const val ADMINISTRATOR_NAME: String = "Administrator"
        const val ANONYMOUS_NAME: String = "Anonymous"

        const val ANONYMOUS_ROLE: String = "ROLE_${ANONYMOUS_NAME}"
        const val REGULAR_ROLE: String = "ROLE_${REGULAR_NAME}"
        const val USER_MANAGER_ROLE: String = "ROLE_${USER_MANAGER_NAME}"
        const val ADMINISTRATOR_ROLE: String = "ROLE_${ADMINISTRATOR_NAME}"
    }
    companion object {
        fun forAuthority(authority: String): SecurityRole? =
                values().find { it.authority == authority }

        fun allAuthorities() = values().map { it.authority }.toTypedArray()
    }
}

@Entity
data class ApplicationUser(
        override var id: Long? = null,
        var username: String? = null,
        private var password: String? = null,
        @Enumerated(EnumType.STRING)
        var role: SecurityRole? = null
) : BaseEntity(id) {
    fun updatePassword(newPassword : String) {
        password = newPassword
    }
    fun readPassword() : String? =
        password
}

@Component
class CustomUserDetailsService : UserDetailsService {
    @Autowired
    lateinit var applicationUserService: ApplicationUserService

    override fun loadUserByUsername(username: String): CustomUserDetails =
            ofNullable(applicationUserService.findUserByUsername(username))
                    .map { it.toUserDetails() }
                    .orElseThrow { UsernameNotFoundException(username) }
}

fun ApplicationUser.toUserDetails(): CustomUserDetails =
        CustomUserDetails(this.id!!, this.username!!, this.readPassword()!!, this.authorities())

class CustomUserDetails(var userId : Long, username: String, password: String, authorities: List<GrantedAuthority>) : User(username, password, authorities)

fun ApplicationUser.authorities(): List<GrantedAuthority> =
        listOf(SimpleGrantedAuthority(this.role!!.authority))

abstract class UserService<U : ApplicationUser, R : UserRepository<U>>(entityClass : KClass<U>) : BaseService<U, R>(entityClass) {
    fun findUserByUsername(username: String) = repository.findByUsername(username)

    fun findUserByUsernameAndPassword(username: String, password: String) = repository.findByUsernameAndPassword(username, password)

    override fun update(toUpdate: U): U? {
        val existingInstance = repository.findOne(toUpdate.id)
        if (existingInstance != null) {
            if (toUpdate.readPassword() != null) {
                existingInstance.updatePassword(toUpdate.readPassword()!!)
                return repository.save(existingInstance)
            }
        }
        return existingInstance
    }

    override fun create(toCreate: U): U {
        BusinessException.ensure(toCreate.readPassword() != null, ErrorCode.INVALID_OR_MISSING_DATA, "password")
        return super.create(toCreate)
    }
}

interface UserRepository<U : ApplicationUser> : BaseRepository<U> {
    fun findByUsername(username: String): U?
    fun findByUsernameAndPassword(username: String, password : String): U?
}

@Service
class ApplicationUserService : UserService<ApplicationUser, ApplicationUserRepository>(ApplicationUser::class) {
}

@Repository
interface ApplicationUserRepository : UserRepository<ApplicationUser> {
}

fun getCurrentUserId(): Long? =
        (SecurityContextHolder.getContext().authentication.principal as CustomUserDetails).userId

fun getCurrentUsername(): String =
        (SecurityContextHolder.getContext().authentication.principal as User).username

fun getCurrentRoles(): Set<SecurityRole> =
        getRoles(SecurityContextHolder.getContext().authentication)

fun isCurrentRole(vararg toCheck : SecurityRole): Boolean =
        getRoles(SecurityContextHolder.getContext().authentication).intersect(toCheck.toSet()).isNotEmpty()

fun getRoles(authentication: Authentication): Set<SecurityRole> =
        authentication.authorities.map { SecurityRole.forAuthority(it.authority) }.filter { it != null }.map { it!! }.toSet() as Set<SecurityRole>

@Entity
data class Meal(
    override var id: Long? = null,
    var description: String? = null,
    var calories: Int? = null,
    var mealDate: LocalDate? = null,
    var mealTime: LocalTime? = null,
    var aboveRequired : Boolean? = null,
    @ManyToOne
    var user : ApplicationUser? = null
) : BaseEntity(id)

@Service
class MealService : BaseService<Meal, MealRepository>(Meal::class) {
    @Autowired
    lateinit var applicationUserService: ApplicationUserService

    fun findUserMeals(userId: Long, customFilter : CustomFilter?, page: Int?, limit: Int?) : Page<Meal> =
        repository.findAll(
            Specifications.where(
                filterAsSpecification(customFilter)
            ).and(
                    ExampleSpecification(
                            Example.of(Meal(user = ApplicationUser(id = userId)))
                    )
            ),
                defaultPageRequest(page, limit)
        )

    fun createForCurrentUser(toCreate: Meal): Meal {
        toCreate.user = applicationUserService.findUserByUsername(getCurrentUsername())
        return create(toCreate)
    }
}

interface MealRepository : BaseRepository<Meal> {
    @Query("SELECT DISTINCT(meal.description) FROM Meal meal WHERE calories IS NULL")
    fun findDescriptionsMissingCalories () : List<String>

    @Modifying
	@Query("UPDATE Meal meal SET calories = :calories WHERE description = :description AND calories IS NULL")
	fun assignMissingCalories(@Param("calories") calories : Int, @Param("description") description: String)
}

class ExampleSpecification<T>(
    private val example: Example<T>
) : Specification<T> {
    override fun toPredicate(root: Root<T>, query: CriteriaQuery<*>, cb: CriteriaBuilder): Predicate {
        return QueryByExamplePredicateBuilder.getPredicate(root, cb, example)
    }
}

