package br.pucpr.authserver.schedules

import br.pucpr.authserver.courts.CourtService
import br.pucpr.authserver.errors.BadRequestException
import br.pucpr.authserver.schedules.requests.CreateScheduleRequest
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.ResponseEntity
import java.util.*

class ScheduleControllerTest {

    private val serviceMock = mockk<ScheduleService>(relaxed = true)
    private val courtServiceMock = mockk<CourtService>()
    private val controller = ScheduleController(serviceMock, courtServiceMock)

    @AfterEach
    fun cleanUp() {
        clearAllMocks()
    }

    @Test
    fun `createSchedule throws BadRequestException if the court is not found`() {
        val scheduleRequest = CreateScheduleRequest(
            scheduleId = 1,
            courtId = 1,
            userId = 1,
            date = "2023-10-10",
            start = "10:00:00",
            end = "11:00:00"
        )

        every { courtServiceMock.getCourtById(scheduleRequest.courtId) } returns Optional.empty()

        assertThrows<BadRequestException> {
            controller.createSchedule(scheduleRequest)
        }
    }

    @Test
    fun `createSchedule throws BadRequestException if the court is inactive`() {
        val scheduleRequest = CreateScheduleRequest(
            scheduleId = 1,
            courtId = 1,
            userId = 1,
            date = "2023-10-10",
            start = "10:00:00",
            end = "11:00:00"
        )

        every { courtServiceMock.getCourtById(scheduleRequest.courtId) } returns Optional.of(mockk {
            every { status } returns "Indisponível"
        })

        assertThrows<BadRequestException> {
            controller.createSchedule(scheduleRequest)
        }
    }

    @Test
    fun `createSchedule throws BadRequestException if the schedule already exists`() {
        val scheduleRequest = CreateScheduleRequest(
            scheduleId = 1,
            courtId = 1,
            userId = 1,
            date = "2023-10-10",
            start = "10:00:00",
            end = "11:00:00"
        )

        every { courtServiceMock.getCourtById(scheduleRequest.courtId) } returns Optional.of(mockk {
            every { status } returns "Disponível"
        })
        every { serviceMock.getScheduleByTime(scheduleRequest.start, scheduleRequest.end, scheduleRequest.date, scheduleRequest.courtId) } returns mockk()

        assertThrows<BadRequestException> {
            controller.createSchedule(scheduleRequest)
        }
    }

    @Test
    fun `createSchedule saves the schedule if valid`() {
        val scheduleRequest = CreateScheduleRequest(
            scheduleId = 1,
            courtId = 1,
            userId = 1,
            date = "2023-10-10",
            start = "10:00:00",
            end = "11:00:00"
        )
        val savedSchedule = Schedule(
            id = 1,
            idCourt = 1,
            idUser = 1,
            date = "2023-10-10",
            hourStart = "10:00:00",
            hourEnd = "11:00:00"
        )

        every { courtServiceMock.getCourtById(scheduleRequest.courtId) } returns Optional.of(mockk {
            every { status } returns "Disponível"
        })
        every { serviceMock.getScheduleByTime(scheduleRequest.start, scheduleRequest.end, scheduleRequest.date, scheduleRequest.courtId) } returns null
        every { serviceMock.createSchedule(scheduleRequest) } returns savedSchedule

        val response = controller.createSchedule(scheduleRequest)

        response shouldBe ResponseEntity.ok(savedSchedule)
        verify { serviceMock.createSchedule(scheduleRequest) }
    }
}