package gotwifi.api.test

import gotwifi.api.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.runners.Suite
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@RunWith(Suite::class)
@Suite.SuiteClasses(ContributorTest::class, PlaceTest::class, ReviewTest::class)
class TestSuite

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class EndpointTest<DTO : BaseDto>(val basePath: String, val dtoClass: ParameterizedTypeReference<DTO>, val pageClass: ParameterizedTypeReference<PageDto<DTO>>) {
    @Autowired
    protected lateinit var testRestTemplate: TestRestTemplate

    abstract fun getSample(): DTO

    abstract fun applyChange(dto: DTO)

    @Before
    fun setUp() {
        doSetUp()
    }

    open fun doSetUp() {}

    @Test
    fun testCreate() {
        val template = getSample()
        val result = testRestTemplate.exchange(basePath, HttpMethod.POST, HttpEntity(template), dtoClass)
        assertNotNull(result)
        assertEquals(HttpStatus.CREATED, result.statusCode)
        val created = result.body
        assertNotNull(created.id)
        template.id = created.id
        template.url = basePath + created.id
        assertEquals(template, created)
    }

    @Test
    fun testGet() {
        val template = getSample()
        val created = testRestTemplate.exchange(basePath, HttpMethod.POST, HttpEntity(template), dtoClass).body
        assertNotNull(created.url)
        val result = testRestTemplate.exchange(created.url, HttpMethod.GET, null, dtoClass)
        assertNotNull(result)
        assertEquals(HttpStatus.OK, result.statusCode)
        val retrieved = result.body
        assertEquals(created, retrieved)
    }

    @Test
    fun testPut() {
        val template = getSample()
        val created = testRestTemplate.exchange(basePath, HttpMethod.POST, HttpEntity(template), dtoClass).body
        applyChange(created)
        val result = testRestTemplate.exchange(created.url, HttpMethod.PUT, HttpEntity(created), dtoClass)
        assertNotNull(result)
        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(created, result.body)
    }

    @Test
    fun testDelete() {
        val template = getSample()
        val created = testRestTemplate.exchange(basePath, HttpMethod.POST, HttpEntity(template), dtoClass).body
        val result = testRestTemplate.exchange(created.url, HttpMethod.DELETE, null, Void::class.javaPrimitiveType)
        assertEquals(HttpStatus.NO_CONTENT, result.statusCode)
        val afterDeleted = testRestTemplate.exchange(created.url, HttpMethod.GET, null, dtoClass)
        assertNotNull(result)
        assertEquals(HttpStatus.NOT_FOUND, afterDeleted.statusCode)
    }

    @Test
    fun testEmptyList() {
        val result = testRestTemplate.exchange(basePath + "/?pageNumber=9999", HttpMethod.GET, null, pageClass)
        assertNotNull(result)
        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(PageDto<ContributorDto>(pageNumber = 9999), result.body)
    }
}

class ContributorTest : EndpointTest<ContributorDto>(basePath = "/contributors/", dtoClass = typeRef<ContributorDto>(), pageClass = typeRef<PageDto<ContributorDto>>()) {
    override fun getSample(): ContributorDto = ContributorDto(name = unique("susan"))
    override fun applyChange(dto: ContributorDto) {
        dto.name = dto.name + " updated"
    }
}

class PlaceTest : EndpointTest<PlaceDto>(basePath = "/places/", dtoClass = typeRef<PlaceDto>(), pageClass = typeRef<PageDto<PlaceDto>>()) {
    override fun getSample(): PlaceDto = PlaceDto(name = unique("Coffee Shop"))
    override fun applyChange(dto: PlaceDto) {
        dto.name = dto.name + " updated"
    }
}

class ReviewTest : EndpointTest<ReviewDto>(basePath = "/reviews/", dtoClass = typeRef<ReviewDto>(), pageClass = typeRef<PageDto<ReviewDto>>()) {
    lateinit var contributor: ContributorDto
    lateinit var place: PlaceDto

    override fun getSample(): ReviewDto =
        ReviewDto(
            contributor = link(contributor.id, contributor.name, "/contributors/"),
            place = link(place.id, place.name, "/places/"),
            overallRating = Rating.Regular)

    override fun applyChange(dto: ReviewDto) {
        dto.overallRating = if (dto.overallRating == null)
            Rating.Regular
        else
            // anything else
            Rating.values().find { it != dto.overallRating }
    }

    override fun doSetUp() {
        val newContributor = ContributorDto(name = unique("susan"))
        val newPlace = PlaceDto(name = unique("Coffe Shop"))
        contributor = testRestTemplate.exchange("/contributors/", HttpMethod.POST, HttpEntity(newContributor), typeRef<ContributorDto>()).body
        place = testRestTemplate.exchange("/places/", HttpMethod.POST, HttpEntity(newPlace), typeRef<PlaceDto>()).body
    }
}


inline fun <reified T : Any> typeRef(): ParameterizedTypeReference<T> = object : ParameterizedTypeReference<T>() {}

fun unique(value: String): String = value + "-" + UUID.randomUUID().toString()