package br.pucpr.authserver.schedules

import br.pucpr.authserver.schedules.requests.CreateScheduleRequest
import br.pucpr.authserver.users.SortDir
import br.pucpr.authserver.users.User
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ScheduleService(val repository: ScheduleRepository) {

    fun findByIdOrNull(id: Long): List<Schedule>? {
        return repository.findByIdCourt(id)
    }

    fun getScheduleById(id: Long): Schedule {
        return repository.findById(id).orElseThrow { NoSuchElementException("Schedule not found with id: $id") }
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

    fun updateSchedule(schedule: Schedule): Schedule {
        return repository.save(schedule)
    }

    fun deleteSchedule(id: Long) {
        repository.deleteById(id)
    }

    fun list(sortDir: SortDir, idCourt: Long?): List<Schedule>
    {
        return if (idCourt != null) {
            repository.findByIdCourt(idCourt) ?: emptyList()
        } else {
            when (sortDir) {
                SortDir.ASC -> repository.findAll()
                SortDir.DESC -> repository.findAll(Sort.by("id").reverse())
            }
        }
    }
}
