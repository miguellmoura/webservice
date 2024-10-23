package br.pucpr.authserver.departments.requests

import br.pucpr.authserver.departments.Department
import br.pucpr.authserver.departments.DepartmentRoutine
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateDepartmentRequest(
    @field:NotBlank val name: String?,
    @field:NotNull val description: String?,
    @field:NotNull val officeRoutine: String?) {
    fun toDepartment() = Department(
                                    name = name!!,
                                    description = description!!,
                                    officeRoutine = DepartmentRoutine.valueOf(officeRoutine),
                                    id = null
                                    )
}
