package br.pucpr.authserver.errors

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.FORBIDDEN)
class ForbbidenException (
    message: String = "Forbbiden",
    cause: Throwable? = null
) : Exception(message, cause)