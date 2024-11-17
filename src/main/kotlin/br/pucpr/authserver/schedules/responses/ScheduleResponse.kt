package br.pucpr.authserver.schedules.responses
import br.pucpr.authserver.schedules.Schedule

data class ScheduleResponse(
    val id: Long,
    val userId: Long,
    val courtId: Long,
    val date: String,
    val hourStart: String,
    val hourEnd: String
) {
    constructor(schedule: Schedule): this(
        id = schedule.id!!,
        userId = schedule.idUser!!,
        courtId = schedule.idCourt!!,
        date = schedule.date!!,
        hourStart = schedule.hourStart!!,
        hourEnd = schedule.hourEnd!!
    )

    fun toSchedule(): Schedule {
        return Schedule(
            id = id,
            idCourt = courtId,
            idUser = userId,
            date = date,
            hourStart = hourStart,
            hourEnd = hourEnd
        )
    }
}

