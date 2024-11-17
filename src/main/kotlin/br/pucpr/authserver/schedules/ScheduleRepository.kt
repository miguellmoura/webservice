package br.pucpr.authserver.schedules

import br.pucpr.authserver.users.SortDir
import br.pucpr.authserver.users.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ScheduleRepository: JpaRepository<Schedule, Long> {
    fun findByIdCourt(courtId: Long): List<Schedule>?

    @Query(
        "select s from Schedule s " +
                "where s.date = :date " +
                "and s.hourStart >= :startTime " +
                "and s.hourEnd <= :endTime " +
                "and s.idCourt = :courtId"
    )
    fun findByTime(startTime: String, endTime: String, date: String, courtId: Long?): Schedule?

}