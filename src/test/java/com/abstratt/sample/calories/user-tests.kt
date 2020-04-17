package com.abstratt.sample.calories

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import java.net.URI

class UserTests : AbstractAPITest() {

    override fun getBaseUrl(): URI =
            getRootUrl().resolve("users/")

    @Test
    fun createAndRetrieve() {
        val toCreate = UserDTO(username = unique("user1"), password = "pass", role = SecurityRole.Regular)
        val created = exchangeDTO(URI.create("users/"), HttpMethod.POST, toCreate, UserDTO::class, HttpStatus.CREATED)!!
        assertNotNull(created.id)
        assertNotNull(created.url)

        this.username = toCreate.username
        this.password = toCreate.password
        val retrieved = exchangeDTO(URI.create(created.url), HttpMethod.GET, null, UserDTO::class, HttpStatus.OK)
        assertEquals(created, retrieved)

        created.id = null
        toCreate.password = null
        assertEquals(toCreate, created)
    }

    @Test
    fun createAndUpdate() {
        val toCreate = UserDTO(username = unique("user1"), password = "pass", role = SecurityRole.Regular)
        val created = exchangeDTO(URI.create("users/"), HttpMethod.POST, toCreate, UserDTO::class, HttpStatus.CREATED)!!

        this.username = toCreate.username
        val retrieved = exchangeDTO(URI.create(created.url), HttpMethod.GET, null, UserDTO::class, HttpStatus.OK)!!
        retrieved.password = toCreate.password + ".foo"
        exchangeDTO(URI.create(created.url), HttpMethod.PUT, retrieved, UserDTO::class, HttpStatus.OK)
        exchangeDTO(URI.create(created.url), HttpMethod.GET, null, UserDTO::class, HttpStatus.UNAUTHORIZED)
        this.password = retrieved.password
        exchangeDTO(URI.create(created.url), HttpMethod.GET, null, UserDTO::class, HttpStatus.OK)
    }
    @Test
    fun regularUserAccessesOtherUser() {
        val toCreate1 = UserDTO(username = unique("user1"), password = "pass", role = SecurityRole.Regular)
        val toCreate2 = UserDTO(username = unique("user2"), password = "pass", role = SecurityRole.Regular)
        val created1 = exchangeDTO(URI.create("users/"), HttpMethod.POST, toCreate1, UserDTO::class, HttpStatus.CREATED)!!
        val created2 = exchangeDTO(URI.create("users/"), HttpMethod.POST, toCreate2, UserDTO::class, HttpStatus.CREATED)!!

        this.username = toCreate1.username

        exchangeDTO(URI.create(created1.url), HttpMethod.GET, null, UserDTO::class, HttpStatus.OK)
        exchangeDTO(URI.create(created2.url), HttpMethod.GET, null, UserDTO::class, HttpStatus.FORBIDDEN)
    }

    @Test
    fun managerCreatesOtherUser() {
        this.username = "manager01"

        val toCreate1 = UserDTO(username = unique("user1"), password = "pass", role = SecurityRole.Regular)
        val created1 = exchangeDTO(URI.create("users/"), HttpMethod.POST, toCreate1, UserDTO::class, HttpStatus.CREATED)!!
        exchangeDTO(URI.create(created1.url), HttpMethod.GET, null, UserDTO::class, HttpStatus.OK)
        exchangeDTO(URI.create(created1.url), HttpMethod.PUT, created1, UserDTO::class, HttpStatus.OK)
        exchangeDTO(URI.create(created1.url), HttpMethod.DELETE, null, Void::class, HttpStatus.NO_CONTENT)
    }

    @Test
    fun wrongCredentials() {
        val toCreate = UserDTO(username = unique("user1"), password = "pass", role = SecurityRole.Regular)
        val created = exchangeDTO(URI.create("users/"), HttpMethod.POST, toCreate, UserDTO::class, HttpStatus.CREATED)!!

        this.username = toCreate.username
        this.password = toCreate.password + "-foo"

        exchangeDTO(URI.create(created.url), HttpMethod.GET, null, UserDTO::class, HttpStatus.UNAUTHORIZED)

        this.username = toCreate.username + ".foo"
        this.password = toCreate.password
        exchangeDTO(URI.create(created.url), HttpMethod.GET, null, UserDTO::class, HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun createInvalid() {
        val toCreate = UserDTO()
        exchangeDTO(URI.create("users/"), HttpMethod.POST, toCreate, Unit::class, HttpStatus.BAD_REQUEST)
    }
}

