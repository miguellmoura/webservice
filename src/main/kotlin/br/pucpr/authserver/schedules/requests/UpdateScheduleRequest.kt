package br.pucpr.authserver.schedules.requests

import java.time.LocalTime
import io.swagger.v3.oas.annotations.media.Schema

data class UpdateScheduleRequest(
    @Schema(example = "10:20:00")
    val startTime: LocalTime,

    @Schema(example = "17:25:00")
    val endTime: LocalTime,
)