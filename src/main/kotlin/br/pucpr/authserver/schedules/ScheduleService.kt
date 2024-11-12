package br.pucpr.authserver.schedules

import br.pucpr.authserver.schedules.requests.CreateScheduleRequest
import br.pucpr.authserver.users.SortDir
import org.springframework.stereotype.Service

@Service
class ScheduleService (val repository: ScheduleRepository) {

    fun getAllSchedules(): List<Schedule> {
        return repository.findAll()
    }

    fun getScheduleById(id: Long) : Schedule {
        return repository.findById(id).get()
    }

    fun createSchedule(schedule: CreateScheduleRequest): Schedule {
        return repository.save(Schedule(
            id = schedule.scheduleId,
            idCourt = schedule.courtId,
            idUser = schedule.userId,
            date = schedule.date,
            hourStart = schedule.start,
            hourEnd = schedule.end
        ))
    }

    fun updateSchedule(schedule: CreateScheduleRequest): Schedule {
        return repository.save(Schedule(
            id = schedule.scheduleId,
            idCourt = schedule.courtId,
            idUser = schedule.userId,
            date = schedule.date,
            hourStart = schedule.start,
            hourEnd = schedule.end
        ))
    }

    fun deleteSchedule(id: Long) {
        repository.deleteById(id)
    }

    fun findScheduleByCourtId(courtId: Long, sortDir: SortDir): Schedule {
        return repository.findByIdCourt(courtId)
    }
}