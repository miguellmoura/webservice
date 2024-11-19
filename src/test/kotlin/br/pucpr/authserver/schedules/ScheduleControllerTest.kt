package br.pucpr.authserver.schedules

import br.pucpr.authserver.courts.Court
import br.pucpr.authserver.courts.CourtController
import br.pucpr.authserver.courts.CourtService
import br.pucpr.authserver.schedules.requests.CreateScheduleRequest
import br.pucpr.authserver.schedules.requests.UpdateScheduleRequest
import br.pucpr.authserver.schedules.responses.ScheduleResponse
import br.pucpr.authserver.users.SortDir
import br.pucpr.authserver.users.User
import io.mockk.InternalPlatformDsl.toStr
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalTime
import java.util.*
import kotlin.math.exp

@ExtendWith(MockitoExtension::class)
class ScheduleControllerTest {

    @Mock
    lateinit var service: ScheduleService

    @Mock
    lateinit var courtService: CourtService

    @InjectMocks
    lateinit var controller: ScheduleController

    @Test
    fun `should return all schedules`() {
        val expectedSchedules = listOf(
            ScheduleResponse(id = 1, userId = 1, courtId = 1, date = "2021-10-10", hourStart = "10:00", hourEnd = "11:00"),
            ScheduleResponse(id = 2, userId = 2, courtId = 2, date = "2021-10-10", hourStart = "11:00", hourEnd = "12:00")
        )

        Mockito.`when`(service.list(SortDir.ASC, null)).thenReturn(expectedSchedules.map { it.toSchedule() })

        val result = controller.list(null, null)

        assertEquals(expectedSchedules, result.body)
    }

    @Test
    fun `should return schedule by ID`() {
        val scheduleId = 1L
        val expectedSchedule = Schedule(id = scheduleId, idCourt = 1, idUser = 1, date = "2021-10-10", hourStart = "10:00", hourEnd = "11:00")
        Mockito.`when`(service.getScheduleById(scheduleId)).thenReturn(expectedSchedule)

        val result = controller.getScheduleById(scheduleId)

        assertEquals(expectedSchedule, result)
    }

    @Test
    fun `should create a new schedule`() {
        val newSchedule = CreateScheduleRequest(
            scheduleId = 1,
            courtId = 1,
            userId = 1,
            date = "2021-10-10",
            start = "10:00",
            end = "11:00"
        )
        val court = Court(id = 1, esporte = "Futebol", numBloco = 1, status = "Disponível")
        val expectedSchedule = Schedule(id = 1, idCourt = 1, idUser = 1, date = "2021-10-10", hourStart = "10:00", hourEnd = "11:00")

        Mockito.`when`(courtService.getCourtById(1)).thenReturn(Optional.of(court))
        Mockito.`when`(service.createSchedule(newSchedule)).thenReturn(expectedSchedule)

        val result = controller.createSchedule(newSchedule)

        assertEquals(expectedSchedule, result.body)
    }

    @Test
    fun `should update schedule time`() {
        val scheduleId = 1L

        val court = Court(id = 1L, esporte = "Futebol", numBloco = 1, status = "Disponível")
        val user = User(id = 1, name = "User", email = "user@gmail.com", password = "123456")
        val schedule = Schedule(
            id = scheduleId,
            idCourt = court.id,
            idUser = user.id,
            date = "2021-10-10",
            hourStart = "19:00",
            hourEnd = "20:00"
        )

        val updateScheduleRequest = UpdateScheduleRequest(
            startTime = LocalTime.parse("10:00:00"),
            endTime = LocalTime.parse("11:00:00"),
            date = "2021-10-10"
        )

        val expectedSchedule = schedule.copy(
            date = updateScheduleRequest.date,
            hourStart = updateScheduleRequest.startTime.toString(),
            hourEnd = updateScheduleRequest.endTime.toString()
        )

        Mockito.`when`(service.getScheduleById(scheduleId)).thenReturn(schedule)
        Mockito.`when`(service.updateSchedule(Mockito.any(Schedule::class.java))).thenReturn(expectedSchedule)

        val captor = org.mockito.ArgumentCaptor.forClass(Schedule::class.java)

        val result = controller.updateScheduleTime(scheduleId, updateScheduleRequest)

        Mockito.verify(service).updateSchedule(captor.capture())
        val capturedArgument = captor.value

        assertEquals(expectedSchedule, capturedArgument)

        assertEquals(expectedSchedule, result.body)
    }




    @Test
    fun `should delete schedule`() {
        val scheduleId = 1L

        controller.deleteSchedule(scheduleId)

        Mockito.verify(service).deleteSchedule(scheduleId)
    }

    @Test
    fun `should return schedules by court`() {
        val courtId = 1L
        val expectedSchedules = listOf(
            Schedule(id = 1, idCourt = courtId, idUser = 1, date = "2021-10-10", hourStart = "10:00", hourEnd = "11:00"),
            Schedule(id = 2, idCourt = courtId, idUser = 2, date = "2021-10-10", hourStart = "11:00", hourEnd = "12:00")
        )
        Mockito.`when`(service.list(SortDir.ASC, courtId)).thenReturn(expectedSchedules)

        val result = controller.list(SortDir.ASC.toString(), courtId)

        assertEquals(expectedSchedules, result.body)
    }


}