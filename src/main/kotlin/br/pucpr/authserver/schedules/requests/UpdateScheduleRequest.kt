package br.pucpr.authserver.schedules.requests
import jakarta.validation.constraints.NotBlank
import java.util.Date

data class UpdateScheduleRequest (
    @field:NotBlank
    val startTime: Date,
    val endTime: Date
)