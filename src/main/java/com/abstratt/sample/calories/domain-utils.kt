package com.abstratt.sample.calories

import com.abstratt.sample.uriql.CustomFilter
import com.abstratt.sample.uriql.CustomFilterSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.NestedRuntimeException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.sql.Date
import java.sql.Time
import java.time.LocalDate
import javax.persistence.*
import java.time.LocalTime
import javax.persistence.AttributeConverter
import kotlin.reflect.KClass


@Converter(autoApply = true)
class LocalDateConverter : AttributeConverter<LocalDate, Date> {
    override fun convertToDatabaseColumn(entityValue: LocalDate?): Date? =
        if (entityValue == null) null else Date.valueOf(entityValue)

    override fun convertToEntityAttribute(databaseValue: Date?): LocalDate? =
        databaseValue?.toLocalDate()
}

@Converter(autoApply = true)
class LocalTimeConverter : AttributeConverter<LocalTime, Time> {
    override fun convertToDatabaseColumn(entityValue: LocalTime?) = entityValue?.let { Time.valueOf(it) }
    override fun convertToEntityAttribute(databaseValue: java.sql.Time?) = databaseValue?.toLocalTime()
}

@MappedSuperclass
abstract class BaseEntity (
    @Id
    @GeneratedValue
    open var id: Long? = null
)

fun defaultPageRequest(page: Int? = 0, limit: Int? = 100) =
        PageRequest(page ?: 0, limit ?: 100, Sort(Sort.Order("id")))

interface BaseRepository<T : BaseEntity> : JpaRepository<T, Long>, JpaSpecificationExecutor<T>

abstract class BaseService<T : BaseEntity, R : BaseRepository<T>>(val entityClass: KClass<T>) {
    @Autowired
    lateinit var repository : R

    open fun findById(id: Long): T? {
        return repository.findOne(id)
    }

    open fun create(toCreate: T): T {
        toCreate.id = null
        val savedInstance = repository.save(toCreate)
        return repository.getOne(savedInstance.id)
    }

    open fun update(toUpdate: T): T? {
        checkNotNull(toUpdate.id, { "id was missing" })
        val existingInstance = repository.findOne(toUpdate.id)
        if (existingInstance == null)
            return null
        return repository.save(toUpdate)
    }

    open fun delete(id: Long): Boolean {
        val existingInstance = repository.findOne(id)
        if (existingInstance != null) {
            repository.delete(existingInstance)
            return true
        }
        return false
    }

    open fun list(filter : CustomFilter? = null, page: Int? = null, limit: Int? = null): Page<T> {
        return repository.findAll(filterAsSpecification(filter), defaultPageRequest(page, limit))
    }

    fun filterAsSpecification(filter: CustomFilter?): Specification<T>? {
        return CustomFilterSpecification<T>(entityClass, filter)
    }
}

interface ErrorCodeException {
    val errorCode: ErrorCode
}

class BusinessException : NestedRuntimeException, ErrorCodeException {
    override var errorCode: ErrorCode

    constructor(message: String?, e: Exception) : super(message, e) {
        this.errorCode = ErrorCode.UNEXPECTED
    }

    constructor(errorCode: ErrorCode, message: String?) : super(message ?: errorCode.defaultMessage) {
        this.errorCode = errorCode
    }

    companion object {
        private val serialVersionUID = 1L

        fun ensure(condition: Boolean, errorCode: ErrorCode, messageSupplier: () -> String) {
            if (!condition) {
                throw BusinessException(errorCode, messageSupplier())
            }
        }

        fun ensure(condition: Boolean, errorCode: ErrorCode, message: String? = null) {
            if (!condition) {
                throw BusinessException(errorCode, message)
            }
        }

        fun wrapException(e: Exception): BusinessException {
            return if (e is BusinessException) {
                e
            } else BusinessException("Unexpected error", e)
        }
    }
}

enum class ErrorCode private constructor(val errorCode: Int?, val errorKind: ErrorKind, val defaultMessage: String) {
    UNEXPECTED(-1, ErrorKind.INTERNAL, "Unexpected error"),
    UNSPECIFIED(null, "Unspecified error"),
    UNKNOWN_OBJECT(1001, ErrorKind.UNKNOWN_OBJECT, "Object not found"),
    TOO_MANY_ITEMS(1002, "Too may items"),
    INVALID_OR_MISSING_DATA(1003, "Invalid or missing data");

    enum class ErrorKind {
        CLIENT,
        UNKNOWN_OBJECT,
        INTERNAL
    }

    private constructor(errorCode: Int?, defaultMessage: String) : this(errorCode, ErrorKind.CLIENT, defaultMessage) {}
}

