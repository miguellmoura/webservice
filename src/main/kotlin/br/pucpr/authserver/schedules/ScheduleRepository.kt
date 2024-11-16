package br.pucpr.authserver.schedules

import br.pucpr.authserver.users.SortDir
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ScheduleRepository: JpaRepository<Schedule, Long> {
    fun findByIdCourt(courtId: Long): List<Schedule>?
}