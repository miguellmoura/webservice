package br.pucpr.authserver.users

import br.pucpr.authserver.errors.BadRequestException
import br.pucpr.authserver.roles.RoleRepository
import br.pucpr.authserver.security.Jwt
import br.pucpr.authserver.users.requests.CreateUserRequest
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserServiceTest {

    private val repositoryMock = mockk<UserRepository>(relaxed = true)
    private val roleRepositoryMock = mockk<RoleRepository>()
    private val jwtMock = mockk<Jwt>()
    private val service = UserService(
        repository = repositoryMock,
        roleRepository = roleRepositoryMock,
        jwt = jwtMock
    )

    @AfterEach
    fun cleanUp() {
        clearAllMocks()
    }

    @Test
    fun `insert throws BadRequestException if the email exists`() {
        val userRequest = CreateUserRequest(name = "User", email = "user@email.com", password = "password123")
        val user = userRequest.toUser()

        every { repositoryMock.findByEmail(userRequest.email!!) } returns user

        assertThrows<BadRequestException> {
            service.insert(user)
        }
    }

    @Test
    fun `insert saves the data if the email does not exist`() {
        val userRequest = CreateUserRequest(name = "New User", email = "newuser@email.com", password = "password123")
        val user = userRequest.toUser()
        val savedUser = user.copy(id = 1)

        every { repositoryMock.findByEmail(userRequest.email!!) } returns null
        every { repositoryMock.save(user) } returns savedUser

        service.insert(user) shouldBe savedUser

        verify { repositoryMock.save(user) }
    }

    @Test
    fun `insert throws IllegalArgumentException if the user has an id`() {
        val userRequest = CreateUserRequest(name = "User2", email = "user2@email.com", password = "password123")
        val userWithId = userRequest.toUser().copy(id = 1)

        assertThrows<IllegalArgumentException> {
            service.insert(userWithId)
        }
    }
}