package br.pucpr.authserver.schedules

import br.pucpr.authserver.courts.CourtService
import br.pucpr.authserver.errors.BadRequestException
import br.pucpr.authserver.schedules.requests.CreateScheduleRequest
import br.pucpr.authserver.schedules.requests.UpdateScheduleRequest
import br.pucpr.authserver.schedules.responses.ScheduleResponse
import br.pucpr.authserver.users.SortDir
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/schedules")
class ScheduleController(val service: ScheduleService, val courtService: CourtService) {

    private val logger = LoggerFactory.getLogger(ScheduleController::class.java)

    @GetMapping
    fun list(
        @RequestParam(required = false) sortDir: String?,
        @RequestParam(required = false) idCourt: Long?
    ) =
        service.list(
            sortDir = SortDir.getByName(sortDir) ?:
            throw BadRequestException("Invalid sort dir!"),
            idCourt = idCourt
        )
            .map { ScheduleResponse(it) }
            .let { ResponseEntity.ok(it) }


    @GetMapping("/schedule/{scheduleId}")
    fun getScheduleById(id: Long, @PathVariable scheduleId: Long): Schedule {
        logger.info("Fetching schedule with ID: $scheduleId")
        return service.getScheduleById(id)
    }

    @PostMapping("/schedule")
    @SecurityRequirement(name = "AuthServer")
    fun createSchedule(@RequestBody @Valid schedule: CreateScheduleRequest): ResponseEntity<Schedule> {

        if (courtService.getCourtById(schedule.courtId).isEmpty) {
            throw BadRequestException("Court with ID ${schedule.courtId} not found!")
        }

        if (service.getScheduleByTime(schedule.start, schedule.end, schedule.date, schedule.courtId) != null) {
            throw BadRequestException("Schedule already exists for this time and date!")
        }

        logger.info("Creating new schedule: $schedule")
        val newSchedule = CreateScheduleRequest(
            scheduleId = schedule.scheduleId,
            courtId = schedule.courtId,
            userId = schedule.userId,
            date = schedule.date,
            start = schedule.start,
            end = schedule.end,
        )

        return ResponseEntity.ok(service.createSchedule(newSchedule))
    }

    @PatchMapping("/updateTime/{scheduleId}")
    @SecurityRequirement(name = "AuthServer")
    fun updateScheduleTime(
        @PathVariable scheduleId: Long,
        @RequestBody @Valid updateScheduleRequest: UpdateScheduleRequest
    ): ResponseEntity<Schedule> {
        logger.info("Updating schedule time for ID: $scheduleId")
        val schedule = service.getScheduleById(scheduleId)

        if (service.getScheduleByTime(
                updateScheduleRequest.startTime.toString(),
                updateScheduleRequest.endTime.toString(),
                updateScheduleRequest.date,
                schedule.idCourt
            ) != null
        ) {
            throw BadRequestException("Schedule already exists for this time and date!")
        }

        val updatedSchedule = schedule.copy(
            hourStart = updateScheduleRequest.startTime.toString(),
            hourEnd = updateScheduleRequest.endTime.toString(),
            date = updateScheduleRequest.date
        )

        val savedSchedule = service.updateSchedule(updatedSchedule)

        return ResponseEntity.ok(savedSchedule)
    }

    @DeleteMapping("/schedule/{scheduleId}")
    @SecurityRequirement(name = "AuthServer")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteSchedule(@PathVariable scheduleId: Long): ResponseEntity<Void> {
        logger.info("Deleting schedule with ID: $scheduleId")
        service.deleteSchedule(scheduleId)
        return ResponseEntity.noContent().build()
    }
}