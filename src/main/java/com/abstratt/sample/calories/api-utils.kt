package com.abstratt.sample.calories

import com.abstratt.sample.uriql.CustomFilter
import com.abstratt.sample.uriql.FilterParser
import com.abstratt.sample.uriql.RenderingException
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.client.AbstractClientHttpResponse
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets
import kotlin.reflect.full.memberProperties
import org.springframework.data.domain.Page
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ResponseStatus


abstract class BaseDTO(open var id: Long? = null, open var url: String? = null)
data class Link(var id: Long? = null, var shorthand: String? = null, var url: String? = null)

open class PageDto<T>(
        val items: Collection<T> = emptyList(),
        val pageNumber: Int = 0,
        val pageSize: Int = 0
)

abstract class BaseController<T : BaseEntity, DTO : BaseDTO, S : BaseService<T, *>> {
    @Autowired
    protected lateinit var service: S
    abstract fun toDto(source: T): DTO
    abstract fun fromDto(source: DTO): T

    open fun doGet(@PathVariable("id") id: String): HttpEntity<DTO> {
        val found = service.findById(id.toLong())
        return if (found != null) {
            val asDto = toSingleDto(found)
            ResponseEntity.status(HttpStatus.OK).body(asDto)
        } else
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }

    protected fun toSingleDto(source: T): DTO {
        val asDTO = toDto(source)
        asDTO.url = resolvePath(getBasePath(), asDTO) + "/" + source.id
        return asDTO
    }

    protected fun toListedDto(source: T): DTO {
        val asDTO = toDto(source)
        asDTO.url = resolvePath(getBasePath(), asDTO) + "/" + source.id
        return asDTO
    }

    protected fun resolvePath(basePath: String, source: DTO): String {
        val placeholders = Regex("\\{([^\\}]*)\\}").findAll(basePath).associate { Pair(it.groupValues[0], it.groupValues[1]) }

        val properties = source::class.memberProperties
        val resolvedValues = placeholders.mapValues { pair -> properties.find { it.name == pair.value }?.call(source)?.toString() ?: pair.value }

        var resolvedPath = basePath
        resolvedValues.forEach { resolvedPath = resolvedPath.replace(it.key, it.value) }
        return resolvedPath
    }

    abstract fun getBasePath(): String

    protected fun parseFilter(filter: String?): CustomFilter? =
        if (filter == null) null else FilterParser().parse(filter)

    open fun doDelete(id: String): HttpEntity<Void> {
        val deleted = service.delete(id.toLong())
        return ResponseEntity.status(if (deleted) HttpStatus.NO_CONTENT else HttpStatus.NOT_FOUND).build()
    }

    open fun doList(customFilter : CustomFilter?, pageNumber: Int?, pageSize: Int?, lister : (filter : CustomFilter?, pageNumber: Int?, pageSize: Int?) -> Page<T> = service::list): HttpEntity<PageDto<DTO>> {
        val list = lister(customFilter, pageNumber, pageSize)
        val listOfDtos = list.content.map { toListedDto(it) }
        val pageDto = PageDto(listOfDtos, list.number, list.content.size)
        return ResponseEntity.status(HttpStatus.OK).body(pageDto)
    }

    open fun doCreate(dto: DTO): HttpEntity<DTO> {
        val created = service.create(fromDto(dto))
        return ResponseEntity.status(HttpStatus.CREATED).body(toSingleDto(created))
    }

    open fun doUpdate(id: String, dto: DTO): HttpEntity<DTO> {
        val updated = service.update(fromDto(dto))
        return if (updated != null) {
            val asDto = toSingleDto(updated)
            ResponseEntity.status(HttpStatus.OK).body(asDto)
        } else
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }
}

fun link(id: Long?, shorthand: String?, basePath: String?) =
        Link(id, shorthand, if (basePath != null) basePath + id else null)

class APIException (val httpStatus : HttpStatus, val errorCode : ErrorCode?, message : String?) : Exception(message) {
    companion object {
        fun ensure(condition : Boolean, outcomeProvider : () -> Pair<HttpStatus, ErrorMessage?>) {
            if (!condition) {
                val outcome = outcomeProvider()
                throw APIException(outcome.first, outcome.second?.errorCode, outcome.second?.errorMessage)
            }
        }
    }
}

data class ErrorMessage (val errorMessage: String? = null, val errorCode : ErrorCode? = null)

@ControllerAdvice
class CustomExceptionHandler : ResponseEntityExceptionHandler() {
    override fun handleExceptionInternal(ex: java.lang.Exception?, body: Any?, headers: HttpHeaders?, status: HttpStatus?, request: WebRequest?): ResponseEntity<Any> {
        return super.handleExceptionInternal(ex, body, headers, status, request)
    }
    @ExceptionHandler(FilterParser.ParsingException::class)
    protected fun handleParsingException(ex: FilterParser.ParsingException, request: WebRequest): ResponseEntity<ErrorMessage> {
        return ResponseEntity(ErrorMessage(ex.message, ErrorCode.INVALID_OR_MISSING_DATA), HttpStatus.BAD_REQUEST)
    }
    @ExceptionHandler(RenderingException::class)
    protected fun handleRenderingException(ex: RenderingException, request: WebRequest): ResponseEntity<ErrorMessage> {
        return ResponseEntity(ErrorMessage(ex.message, ErrorCode.INVALID_OR_MISSING_DATA), HttpStatus.BAD_REQUEST)
    }
    @ExceptionHandler(APIException::class)
    protected fun handleAPIException(ex: APIException, request: WebRequest): ResponseEntity<ErrorMessage> {
        return ResponseEntity(ErrorMessage(ex.message, ex.errorCode),  ex.httpStatus)
    }

    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders?, status: HttpStatus, request: WebRequest?): ResponseEntity<Any> {
        val message = if (ex.bindingResult.fieldError != null) ex.bindingResult.fieldError.field + " - " + ex.bindingResult.fieldError.defaultMessage else ex.bindingResult.globalError.defaultMessage
        return ResponseEntity.badRequest().body(ErrorMessage(message, ErrorCode.INVALID_OR_MISSING_DATA))
    }
}


class LoggingInterceptor : ClientHttpRequestInterceptor {

    @Throws(IOException::class)
    override fun intercept(request: HttpRequest, requestEntityBody: ByteArray?,
                  execution: ClientHttpRequestExecution): ClientHttpResponse {
        logger.debug("*** REQUEST: {} - {}", request.getMethod(), request.getURI())
        if (requestEntityBody != null && requestEntityBody.size > 0) {
            logger.debug("Request Entity: {}", String(requestEntityBody, StandardCharsets.UTF_8))
        }
        request.getHeaders().forEach({ key, values -> values.forEach { value -> logger.debug(key + ": " + value) } })
        val response = execution.execute(request, requestEntityBody)
        val wrapped = ClientHttpResponseWrapper(response)
        logger.debug("*** RESPONSE: ")
        logger.debug("Status: {}", wrapped.statusCode)
        logger.debug("Content-Type: {}", wrapped.headers.contentType)
        logger.debug("Response Entity: {}", String(wrapped.responseContents, StandardCharsets.UTF_8))
        return wrapped
    }

    companion object {
        private val logger = LoggerFactory.getLogger(LoggingInterceptor::class.java.name)
    }
}

class ClientHttpResponseWrapper : AbstractClientHttpResponse {

    var responseContents: ByteArray
        get() = field
        private set(value) { field = value}

    private val originalResponse: ClientHttpResponse

    private var wrappedResponse: ByteArrayInputStream

    constructor(originalResponse: ClientHttpResponse) : super() {
        this.originalResponse = originalResponse
        this.responseContents = if (!originalResponse.statusCode.is2xxSuccessful()) ByteArray(0) else IOUtils.toByteArray(originalResponse.body)
        this.wrappedResponse = ByteArrayInputStream(this.responseContents)
    }

    @Throws(IOException::class)
    @Synchronized
    override fun getBody(): InputStream {
        return wrappedResponse
    }

    override fun getHeaders(): HttpHeaders {
        return originalResponse.headers
    }

    @Throws(IOException::class)
    override fun getRawStatusCode(): Int {
        return originalResponse.rawStatusCode
    }

    @Throws(IOException::class)
    override fun getStatusText(): String {
        return originalResponse.statusText
    }

    override fun close() {
        originalResponse.close()
        wrappedResponse.close()
        responseContents = ByteArray(0)
    }

}