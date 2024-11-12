package br.pucpr.authserver.courts

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CourtRepository: JpaRepository<Court, Long> {
    fun findByEsporte(esporte: String): List<Court>
    fun findByStatus(status: String): Optional<Court>
}