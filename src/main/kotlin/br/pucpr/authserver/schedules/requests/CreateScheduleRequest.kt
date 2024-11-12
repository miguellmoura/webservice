package br.pucpr.authserver.schedules.requests

import br.pucpr.authserver.schedules.Schedule


data class CreateScheduleRequest(
    val scheduleId: Long,
    val courtId: Long,
    val userId: Long,
    val date: String,
    val start: String,
    val end: String
) {
    fun toSchedule(): Schedule {
        return Schedule(scheduleId, courtId, userId, date, start, end)
    }
}