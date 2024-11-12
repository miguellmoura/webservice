package br.pucpr.authserver.schedules

import br.pucpr.authserver.errors.ForbbidenException
import br.pucpr.authserver.schedules.requests.CreateScheduleRequest
import br.pucpr.authserver.schedules.requests.UpdateScheduleRequest
import br.pucpr.authserver.users.SortDir
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/schedules")
class ScheduleController (val service: ScheduleService) {

    @GetMapping("/all")
    fun all(): List<Schedule> {
        return service.getAllSchedules()
    }

    @GetMapping("/schedule/{scheduleId}")
    fun getScheduleById(id: Long): Schedule {
        return service.getScheduleById(id)
    }

    @PostMapping("/schedule")
    fun createSchedule(schedule: CreateScheduleRequest): ResponseEntity<Schedule> {

        val new_schedule: CreateScheduleRequest = CreateScheduleRequest(
            scheduleId = schedule.scheduleId,
            courtId = schedule.courtId,
            userId = schedule.userId,
            date = schedule.date,
            start = schedule.start,
            end = schedule.end,
        )

        return ResponseEntity.ok(service.createSchedule(new_schedule))
    }

    @PatchMapping("/updateTime/{scheduleId}")
    fun updateScheduleTime(@PathVariable scheduleId: Long,
                           @RequestBody @Valid updateScheduleRequest: UpdateScheduleRequest): ResponseEntity<Schedule> {
        val schedule = service.getScheduleById(scheduleId) ?: throw ForbbidenException()
        service.deleteSchedule(scheduleId)
        val newSchedule = CreateScheduleRequest(scheduleId = scheduleId, courtId = schedule.idCourt!!, userId = schedule.idUser!!, date = schedule.date!!,
            start = updateScheduleRequest.startTime.toString(),
            end = updateScheduleRequest.endTime.toString())
        service.createSchedule(newSchedule)
        return ResponseEntity.ok(newSchedule.toSchedule())
    }

    @DeleteMapping("/schedule/{scheduleId}")
    fun deleteSchedule(@PathVariable scheduleId: Long): ResponseEntity<Void> {
        service.deleteSchedule(scheduleId)
        return ResponseEntity.noContent().build()
    }





}