package br.pucpr.authserver.schedules

import br.pucpr.authserver.schedules.requests.CreateScheduleRequest
import br.pucpr.authserver.schedules.requests.UpdateScheduleRequest
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/schedules")
class ScheduleController(val service: ScheduleService) {

    private val logger = LoggerFactory.getLogger(ScheduleController::class.java)

    @GetMapping("/all")
    fun all(): List<Schedule> {
        logger.info("Fetching all schedules")
        return service.getAllSchedules()
    }

    @GetMapping("/schedule/{scheduleId}")
    fun getScheduleById(@PathVariable scheduleId: Long): Schedule {
        logger.info("Fetching schedule with ID: $scheduleId")
        return service.getScheduleById(scheduleId)
    }

    @PostMapping("/schedule")
    fun createSchedule(@RequestBody @Valid schedule: CreateScheduleRequest): ResponseEntity<Schedule> {
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
    fun updateScheduleTime(
        @PathVariable scheduleId: Long,
        @RequestBody @Valid updateScheduleRequest: UpdateScheduleRequest
    ): ResponseEntity<Schedule> {
        logger.info("Updating schedule time for ID: $scheduleId")
        val schedule = service.getScheduleById(scheduleId)

        val updatedSchedule = schedule.copy(
            hourStart = updateScheduleRequest.startTime.toString(),
            hourEnd = updateScheduleRequest.endTime.toString()
        )

        val savedSchedule = service.updateSchedule(updatedSchedule)

        return ResponseEntity.ok(savedSchedule)
    }

    @DeleteMapping("/schedule/{scheduleId}")
    fun deleteSchedule(@PathVariable scheduleId: Long): ResponseEntity<Void> {
        logger.info("Deleting schedule with ID: $scheduleId")
        service.deleteSchedule(scheduleId)
        return ResponseEntity.noContent().build()
    }
}