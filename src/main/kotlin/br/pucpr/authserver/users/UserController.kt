package br.pucpr.authserver.users

import org.springframework.security.core.Authentication

import br.pucpr.authserver.errors.BadRequestException
import br.pucpr.authserver.errors.ForbbidenException
import br.pucpr.authserver.security.UserToken
import br.pucpr.authserver.users.requests.CreateUserRequest
import br.pucpr.authserver.users.requests.LoginRequest
import br.pucpr.authserver.users.requests.UpdateUserRequest
import br.pucpr.authserver.users.responses.LoginResponse
import br.pucpr.authserver.users.responses.UserResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpClientErrorException.Forbidden

@RestController
@RequestMapping("/users")
class UserController(
    val service: UserService,
) {
    @PostMapping
    fun insert(@RequestBody @Valid user: CreateUserRequest) =
        service.insert(user.toUser())
            .let { UserResponse(it) }
            .let { ResponseEntity.status(CREATED).body(it) }


    @GetMapping
    fun list(
        @RequestParam(required = false) sortDir: String?,
        @RequestParam(required = false) role: String?
    ) =
        service.list(
            sortDir = SortDir.getByName(sortDir) ?:
                throw BadRequestException("Invalid sort dir!"),
            role=role
        )
        .map { UserResponse(it) }
        .let { ResponseEntity.ok(it) }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) =
        service.findByIdOrNull(id)
            ?.let { UserResponse(it) }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "AuthServer")
    @PreAuthorize("hasRole('ADMIN')")

    fun delete(@PathVariable id: Long): ResponseEntity<Void> =
        service.delete(id)
            ?.let { ResponseEntity.ok().build() }
            ?: ResponseEntity.notFound().build()

    @PatchMapping("/{id}")
    @SecurityRequirement(name = "AuthServer")
    @PreAuthorize("permitAll()")
    fun update(
        @PathVariable id: Long,
        @RequestBody @Valid updateUserRequest: UpdateUserRequest,
        auth: Authentication
    ): ResponseEntity<UserResponse> {
        val token = auth.principal as? UserToken ?: throw ForbbidenException()
        if (token.id != id && !token.isAdmin) throw ForbbidenException()

        return service.update(id, updateUserRequest.name!!)
            ?.let { UserResponse(it) }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.noContent().build()
    }

    @PutMapping("/{id}/roles/{role}")
    fun grant(
        @PathVariable id: Long,
        @PathVariable role: String
    ): ResponseEntity<Void> =
    if (service.addRole(id, role.uppercase()))
        ResponseEntity.ok().build()
    else
        ResponseEntity.status(NO_CONTENT).build()

    @PostMapping("/login")
    fun login(@Valid @RequestBody login: LoginRequest) =
        service.login(login.email!!, login.password!!)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
}
