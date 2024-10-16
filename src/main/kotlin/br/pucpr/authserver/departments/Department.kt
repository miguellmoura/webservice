package br.pucpr.authserver.departments

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

import jakarta.persistence.Column
import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull
import javax.annotation.processing.Generated

@Entity
class Department (
    @Id @GeneratedValue
    var id: Long?,

    @Column(nullable = false, unique = true)
    var name: String,

    @NotNull
    var description: String,

    @NotNull
    var officeRoutine: DepartmentRoutine
)