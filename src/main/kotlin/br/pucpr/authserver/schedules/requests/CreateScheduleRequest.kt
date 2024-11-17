package br.pucpr.authserver.schedules.requests

import io.swagger.v3.oas.annotations.media.Schema


data class CreateScheduleRequest(
    @Schema(description = "Campo opcional", example = "")
    val scheduleId: Long?,

    @Schema (description = "Identificador da quadra agendada", example = "1")
    val courtId: Long,

    @Schema(description = "Identificador do usuário", example = "1")
    val userId: Long,

    @Schema(description = "Data do agendamento", example = "2021-10-10")
    val date: String,

    @Schema(description = "Hora de início do agendamento", example = "10:00:00")
    val start: String,

    @Schema(description = "Hora de término do agendamento", example = "11:00:00")
    val end: String
) {
}