package br.pucpr.authserver.courts

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name="tbCourt")
class Court {
    @Id @GeneratedValue
    @Schema (description = "Identificador da quadra", example = "1")
    var id: Long? = null
    @Schema (description = "Nome do esporte", example = "Futebol")
    var esporte: String? = null
    @Schema (description = "Número do bloco", example = "1")
    var numBloco: Int? = null
    @Schema (description = "Status da quadra", example = "Disponível")
    var status: String? = null

}