package br.pucpr.authserver.departments.responses

import br.pucpr.authserver.departments.Department

data class DepartmentResponse(val id: Long, val name: String, val description: String, val officeRoutine: String) {
    constructor(department: Department): this(
        id = department.id!!,
        name = department.name,
        description = department.description,
        officeRoutine = department.officeRoutine.name
    )
}
