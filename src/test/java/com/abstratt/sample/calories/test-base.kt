package com.abstratt.sample.calories

import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import java.net.URI
import java.nio.charset.StandardCharsets
import java.util.*
import javax.annotation.PostConstruct
import kotlin.reflect.KClass
import org.junit.rules.TestName
import org.springframework.http.client.ClientHttpResponse
import java.util.UUID




@RunWith(SpringJUnit4ClassRunner::class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AbstractAPITest {
    protected var username: String? = null
    protected var password: String? = "pass"
    protected var user: ApplicationUser? = null

    protected fun loadUser() : ApplicationUser {
        this.user = userService.findUserByUsername(username!!)
        Assert.assertNotNull(this.user)
        return this.user!!
    }

    inner class CredentialsInterceptor : ClientHttpRequestInterceptor {
        override fun intercept(request: HttpRequest, body: ByteArray?, execution: ClientHttpRequestExecution): ClientHttpResponse {
            if (username != null) {
                val credentials = username + ":" + password
                val encodedCredentials = Base64.getEncoder()
                        .encodeToString(credentials.toByteArray(StandardCharsets.ISO_8859_1))
                request.headers.set("Authorization", "Basic " + encodedCredentials)
            }
            return execution.execute(request, body)
        }

    }


    @LocalServerPort
    var serverPort: Int? = null

    @Autowired
    private lateinit var template: TestRestTemplate

    private val uniqueTag = UUID.randomUUID().toString()

    @Rule @JvmField
    final val testName = TestName()

    private fun getUniqueSlug(): String {
        return javaClass.simpleName + "." + testName.methodName + "-" + uniqueTag + "-"
    }

    protected fun unique(value: String): String {
        return (getUniqueSlug() + value).toLowerCase()
    }

    protected fun getUniqueTag(): String {
        return uniqueTag
    }

    @PostConstruct
    private fun init() {
        addSingleton(template.restTemplate.interceptors, LoggingInterceptor())
        addSingleton(template.restTemplate.interceptors, CredentialsInterceptor())
    }

    private fun <E : Any> addSingleton(collection: MutableCollection<E>, toAdd: E) {
        collection.removeIf { it -> toAdd::class.isInstance(it) }
        collection.add(toAdd)
    }

    @Autowired
    lateinit var userService : ApplicationUserService

    abstract fun getBaseUrl(): URI

    public fun getRootUrl() = URI.create("http://localhost:${serverPort}/api/")

    fun <P, R : Any> exchangeDTO(uri: URI, method: HttpMethod, request: P?, responseClass: KClass<R>?, expectedStatus: HttpStatus): R? {
        return exchange(uri, method, if (request != null) HttpEntity(request) else null, responseClass, expectedStatus).body
    }

    fun <P, R : Any> exchange(uri: URI, method: HttpMethod, httpEntity: HttpEntity<P>?, responseClass: KClass<R>?, expectedStatus: HttpStatus): ResponseEntity<R> {
        val actualUri = if (!uri.isAbsolute) getRootUrl().resolve(uri) else uri
        val responseEntity = getTemplate().exchange(actualUri, method, httpEntity, responseClass?.java)
        Assert.assertEquals(responseEntity.toString(), expectedStatus, responseEntity.statusCode)
        return responseEntity
    }

    protected fun uri(uriString: String): URI {
        return URI.create(uriString)
    }

    protected final fun getTemplate() : TestRestTemplate = this.template
}