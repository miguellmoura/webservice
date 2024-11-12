package br.pucpr.authserver.court

import br.pucpr.authserver.courts.Court
import br.pucpr.authserver.courts.CourtController
import br.pucpr.authserver.courts.CourtService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class CourtControllerTest {

    @Mock
    lateinit var service: CourtService

    @InjectMocks
    lateinit var controller: CourtController

    @Test
    fun `should return all courts`() {
        val expectedCourts = listOf(
            Court(id = 1, esporte = "Futebol", status = "Disponível"),
            Court(id = 2, esporte = "Basquete", status = "Indisponível")
        )
        Mockito.`when`(service.getAllCourts()).thenReturn(expectedCourts)

        val result = controller.all()

        assertEquals(expectedCourts, result)
    }

    @Test
    fun `should return courts by sport`() {
        val sportName = "Futebol"
        val expectedCourts = listOf(Court(id = 1, esporte = sportName, status = "Disponível"))
        Mockito.`when`(service.getCourtsBySport(sportName)).thenReturn(expectedCourts)

        val result = controller.getBySport(sportName)

        assertEquals(expectedCourts, result)
    }

    @Test
    fun `should return court by ID`() {
        val courtId = 1L
        val expectedCourt = Court(id = courtId, esporte = "Futebol", status = "Disponível")
        Mockito.`when`(service.getCourtById(courtId)).thenReturn(Optional.of(expectedCourt))

        val result = controller.getCourtById(courtId)

        assertEquals(expectedCourt, result)
    }

    @Test
    fun `should return court by status`() {
        val status = "Disponível"
        val expectedCourt = Optional.of(Court(id = 1, esporte = "Futebol", status = status))
        Mockito.`when`(service.getCourtByStatus(status)).thenReturn(expectedCourt)

        val result = controller.getCourtByStatus(status)

        assertEquals(expectedCourt, result)
    }

    @Test
    fun `should create a new court`() {
        val newCourt = Court(id = 3, esporte = "Tênis", status = "Disponível")
        Mockito.`when`(service.createCourt(newCourt)).thenReturn(newCourt)

        val result = controller.createCourt(newCourt)

        assertEquals(newCourt, result)
    }
}