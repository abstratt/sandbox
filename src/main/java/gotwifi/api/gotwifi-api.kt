package gotwifi.api

import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors
import javax.persistence.*
import javax.transaction.Transactional

@Entity
abstract class BaseEntity(@Id @GeneratedValue open var id: Long? = null)

@Entity
data class Contributor(override var id: Long? = null, @Column(unique = true) var name: String? = null) : BaseEntity(id)

@Entity
data class Place(
        override var id: Long? = null,
        @Column(unique = true) var name: String? = null,
        var address: Address? = null,
        @ManyToOne var placeType: PlaceType? = null
) : BaseEntity(id)

@Embeddable
data class Address(
        var streetAddress: String?,
        var city: String?,
        var state: String?,
        var country: String?
)

@Entity
@Table(uniqueConstraints = arrayOf(UniqueConstraint(name = "one_review_per_contributor", columnNames = arrayOf("contributor_id", "place_id"))))
data class Review(
        override var id: Long? = null,
        @ManyToOne var contributor: Contributor? = null,
        @ManyToOne var place: Place? = null,
        var overallRating: Rating? = null,
        var foodRating: Rating? = null,
        var serviceRating: Rating? = null,
        var priceRating: Rating? = null,
        var comfortRating: Rating? = null,
        var noiseRating: Rating? = null,
        var internetRating: Rating? = null,
        var wifiPassword: String? = null,
        var wifiPasswordRequired: Boolean? = null,
        @ManyToMany var foods: Collection<Food> = emptySet(),
        @ManyToMany var drinks: Collection<Drink> = emptySet(),
        @ManyToMany var seats: Collection<Seat> = emptySet()
) : BaseEntity(id) {
    var date: LocalDateTime? = null
}

@Entity
data class Food(override var id: Long? = null,
                var name: String) : BaseEntity(id)

@Entity
data class Drink(
        override var id: Long? = null,
        var name: String
) : BaseEntity(id)

@Entity
data class Seat(override var id: Long? = null,
                var name: String) : BaseEntity(id)

@Entity
data class PlaceType(override var id: Long? = null,
                     var name: String) : BaseEntity(id)

enum class Rating(val level: Int) {
    NotApplicable(0),
    Terrible(1),
    Bad(2),
    Regular(3),
    Good(4),
    Excellent(5)
}


open class BaseService<T : BaseEntity>(val repository: BaseRepository<T>) {
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

    open fun list(page: Int?, limit: Int?): Page<T> {
        return repository.findAll(defaultPageRequest(page, limit))
    }

}

@Service
class ContributorService(repository: ContributorRepository) : BaseService<Contributor>(repository)

@Service
class PlaceService(repository: PlaceRepository) : BaseService<Place>(repository) {
}

@Service
class ReviewService(
        repository: ReviewRepository,
        val placeRepository: PlaceRepository,
        val contributorRepository: ContributorRepository
) : BaseService<Review>(repository) {
    fun reviewsFor(place: Place): Page<Review> {
        return repository.findAll(Example.of(Review(place = place)), defaultPageRequest())
    }

    fun reviewsBy(contributor: Contributor): Page<Review> {
        return repository.findAll(Example.of(Review(contributor = contributor)), defaultPageRequest())
    }


    override fun create(toCreate: Review): Review {
        toCreate.place = placeRepository.findOne(Example.of(toCreate.place))
        toCreate.contributor = contributorRepository.findOne(Example.of(toCreate.contributor))
        toCreate.date = LocalDateTime.now()
        return super.create(toCreate)
    }

    override fun update(toUpdate: Review): Review? {
        toUpdate.date = LocalDateTime.now()
        return super.update(toUpdate)
    }


}

interface BaseRepository<T : BaseEntity> : JpaRepository<T, Long>

@Transactional(Transactional.TxType.MANDATORY)
interface ContributorRepository : BaseRepository<Contributor>

@Transactional(Transactional.TxType.MANDATORY)
interface ReviewRepository : BaseRepository<Review>

@Transactional(Transactional.TxType.MANDATORY)
interface PlaceRepository : BaseRepository<Place>

abstract class BaseDto(open var id: Long? = null, open var url: String? = null)

data class Link(var id : Long? = null, var shorthand : String? = null, var url : String? = null)
data class ContributorDto(override var id: Long? = null, override var url: String? = null, var name: String? = null) : BaseDto(id, url)
data class PlaceDto(override var id: Long? = null, override var url: String? = null, var name: String? = null) : BaseDto(id, url)
data class ReviewDto(
        override var id: Long? = null,
        override var url: String? = null,
        var contributor: Link? = null,
        var place: Link? = null,
        var overallRating: Rating? = null,
        var foodRating: Rating? = null,
        var serviceRating: Rating? = null,
        var priceRating: Rating? = null,
        var comfortRating: Rating? = null,
        var noiseRating: Rating? = null,
        var internetRating: Rating? = null,
        var wifiPassword: String? = null,
        var wifiPasswordRequired: Boolean? = null,
        var foods: Collection<String> = ArrayList(),
        var drinks: Collection<String> = ArrayList(),
        var seats: Collection<String> = ArrayList()
) : BaseDto(id, url) {
    var date: String? = null
}

data class PageDto<T>(val items: Collection<T> = emptyList(), val pageNumber: Int = 0, val pageSize: Int = 0)

abstract class BaseController<T : BaseEntity, DTO : BaseDto>(val service: BaseService<T>) {
    abstract fun toDto(instance: T): DTO
    abstract fun fromDto(dto: DTO): T

    @RequestMapping(path = arrayOf("/{id}"), method = arrayOf(RequestMethod.GET))
    open fun one(@PathVariable("id") id: String): HttpEntity<DTO> {
        val found = service.findById(id.toLong())
        return if (found != null) {
            val asDto = toDto(found)
            ResponseEntity.status(HttpStatus.OK).body(asDto)
        } else
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }

    @RequestMapping(path = arrayOf("/{id}"), method = arrayOf(RequestMethod.DELETE))
    open fun delete(@PathVariable("id") id: String): HttpEntity<Void> {
        val deleted = service.delete(id.toLong())
        return ResponseEntity.status(if (deleted) HttpStatus.NO_CONTENT else HttpStatus.NOT_FOUND).build()
    }


    @RequestMapping(method = arrayOf(RequestMethod.GET))
    open fun list(@RequestParam(required = false) pageNumber: Int?, @RequestParam(required = false) pageSize: Int?): HttpEntity<PageDto<DTO>> {
        val list = service.list(pageNumber, pageSize)
        val listOfDtos = list.content.stream().map { toDto(it) }.collect(Collectors.toList())
        val pageDto = PageDto(listOfDtos, list.number, list.content.size)
        return ResponseEntity.status(HttpStatus.OK).body(pageDto)
    }

    @RequestMapping(method = arrayOf(RequestMethod.POST), consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    open fun create(@RequestBody dto: DTO): HttpEntity<DTO> {
        val created = service.create(fromDto(dto))
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(created))
    }

    @RequestMapping(path = arrayOf("/{id}"), method = arrayOf(RequestMethod.PUT), consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    open fun update(@RequestBody dto: DTO): HttpEntity<DTO> {
        val updated = service.update(fromDto(dto))
        return if (updated != null) {
            val asDto = toDto(updated)
            ResponseEntity.status(HttpStatus.OK).body(asDto)
        } else
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }

}

@RestController
@RequestMapping("/contributors/")
class ContributorController(service: ContributorService) : BaseController<Contributor, ContributorDto>(service) {
    override fun toDto(instance: Contributor): ContributorDto {
        val dto = ContributorDto(id = instance.id, name = instance.name, url = "/contributors/" + instance.id)
        return dto
    }

    override fun fromDto(dto: ContributorDto): Contributor {
        return Contributor(dto.id, dto.name)
    }
}

@RestController
@RequestMapping("/places/")
class PlaceController(service: PlaceService) : BaseController<Place, PlaceDto>(service) {
    override fun toDto(instance: Place): PlaceDto {
        val dto = PlaceDto(id = instance.id, name = instance.name, url = "/places/" + instance.id)
        return dto
    }

    override fun fromDto(dto: PlaceDto): Place {
        return Place(dto.id, dto.name)
    }
}

@RestController
@RequestMapping("/reviews/")
class ReviewController(service: ReviewService) : BaseController<Review, ReviewDto>(service) {
    override fun toDto(instance: Review): ReviewDto {
        val dto = ReviewDto(
                id = instance.id,
                place = link(id = instance.place?.id, shorthand = instance.place?.name, basePath = "/places/"),
                contributor = link(id = instance.contributor?.id, shorthand = instance.contributor?.name, basePath = "/contributors/"),
                overallRating = instance.overallRating,
                comfortRating = instance.comfortRating,
                foodRating = instance.foodRating,
                internetRating = instance.internetRating,
                noiseRating =  instance.noiseRating,
                priceRating = instance.priceRating,
                serviceRating = instance.serviceRating,
                wifiPassword =  instance.wifiPassword,
                wifiPasswordRequired =  instance.wifiPasswordRequired,
                drinks = instance.drinks?.map { it.name },
                foods = instance.foods?.map { it.name },
                seats = instance.seats?.map { it.name },
                url = "/reviews/" + instance.id
        )
        dto.date = toExternalDateTime(instance.date)
        return dto
    }

    override fun fromDto(dto: ReviewDto): Review {
        return Review(
                id = dto.id,
                contributor = Contributor(id = dto.contributor?.id),
                place = Place(id = dto.place?.id),
                overallRating = dto.overallRating,
                comfortRating = dto.comfortRating,
                foodRating = dto.foodRating,
                internetRating = dto.internetRating,
                noiseRating = dto.noiseRating,
                priceRating = dto.priceRating,
                serviceRating = dto.serviceRating,
                wifiPassword = dto.wifiPassword,
                wifiPasswordRequired = dto.wifiPasswordRequired
        )
    }
}

fun toExternalDateTime(dateTime : LocalDateTime?) = if (dateTime == null) null else DateTimeFormatter.ISO_DATE_TIME.format(dateTime)

fun link(id : Long?, shorthand : String?, basePath : String?) = Link(id, shorthand, if (basePath != null) basePath + id else null)

fun defaultPageRequest(page: Int? = 0, limit: Int? = 10) = PageRequest(page?:0, limit?:9999, Sort(Sort.Order("id")))
