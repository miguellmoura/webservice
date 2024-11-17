package br.pucpr.authserver.courts

import br.pucpr.authserver.errors.BadRequestException
import br.pucpr.authserver.schedules.Schedule
import br.pucpr.authserver.schedules.requests.UpdateScheduleRequest
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
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
    @PreAuthorize("hasRole('ADMIN')")
    fun createCourt(court: Court): Court {
        return service.createCourt(court)
    }

    @PatchMapping("/status/{courtId}")
    @SecurityRequirement(name = "AuthServer")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateCourtStatus(
        @PathVariable courtId: Long,
        @RequestParam(required = false, defaultValue = "Disponível") status: String
    ): ResponseEntity<Court> {

        val court = service.getCourtById(courtId).orElseThrow {
            BadRequestException("Court with ID $courtId not found!")
        }

        val updatedCourt = court.copy(
            status = status
        )
        val savedSchedule = service.updateCourt(updatedCourt)
        return ResponseEntity.ok(savedSchedule)
    }

}