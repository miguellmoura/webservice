package br.pucpr.authserver.schedules

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name="tbSchedule")
class Schedule {
    @Id @GeneratedValue
    var id: Long? = null
    var idCourt: Long? = null
    var idUser: Long? = null
    var date: String? = null
    var hourStart: String? = null
    var hourEnd: String? = null

    constructor(id: Long?, idCourt: Long?, idUser: Long?, date: String?, hourStart: String?, hourEnd: String?) {
        this.id = id
        this.idCourt = idCourt
        this.idUser = idUser
        this.date = date
        this.hourStart = hourStart
        this.hourEnd = hourEnd
    }
    fun copy(
        id: Long? = this.id,
        idCourt: Long? = this.idCourt,
        idUser: Long? = this.idUser,
        date: String? = this.date,
        hourStart: String? = this.hourStart,
        hourEnd: String? = this.hourEnd
    ): Schedule {
        return Schedule(id, idCourt, idUser, date, hourStart, hourEnd)
    }
}