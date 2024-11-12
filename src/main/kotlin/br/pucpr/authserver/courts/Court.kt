package br.pucpr.authserver.courts

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name="tbCourt")
class Court {
    @Id @GeneratedValue
    var id: Long? = null
    var esporte: String? = null
    var numBloco: Int? = null
    var status: String? = null

}