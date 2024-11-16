package br.pucpr.authserver.courts

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/courts")
class CourtController (
    val service: CourtService
) {

    @GetMapping("/all")
    fun all(): List<Court> {
        return service.getAllCourts()
    }

    @GetMapping("/sport/{sportName}")
    fun getBySport(sportName: String): List<Court> {
        return service.getCourtsBySport(sportName)
    }

    @GetMapping("/court/{courtId}")
    fun getCourtById(id: Long): Court {
        return service.getCourtById(id).get()
    }

    @GetMapping("/court/status/{courtStatus}")
    fun getCourtByStatus(status: String): Optional<Court> {
        return service.getCourtByStatus(status)
    }

    @PostMapping("/court")
    @SecurityRequirement(name = "AuthServer")
    fun createCourt(court: Court): Court {
        return service.createCourt(court)
    }

}