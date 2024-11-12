package br.pucpr.authserver.courts

import org.springframework.stereotype.Service
import java.util.*

@Service
class CourtService (
    val repository: CourtRepository
){
    fun getAllCourts(): List<Court> {
        return repository.findAll()
    }

    fun getCourtsBySport(sportName: String): List<Court> {
        return repository.findByEsporte(sportName)
    }

    fun getCourtById(id: Long) : Optional<Court> {
        return repository.findById(id)
    }

    fun getCourtByStatus(status: String) : Optional<Court> {
        return repository.findByStatus(status)
    }

    fun createCourt(court: Court): Court {
        return repository.save(court)
    }
}
