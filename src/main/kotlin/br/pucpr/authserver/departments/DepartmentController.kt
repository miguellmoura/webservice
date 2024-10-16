package br.pucpr.authserver.departments

import br.pucpr.authserver.departments.requests.CreateDepartmentRequest
import br.pucpr.authserver.departments.responses.DepartmentResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/departments")

class DepartmentController(
    val departmentService: DepartmentService
)   {

    @PostMapping
    fun insert(@RequestBody @Valid department: CreateDepartmentRequest) {
        departmentService.insert(department.toDepartment())
            .let { DepartmentResponse(it) }
            .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }
    }

    @GetMapping
    fun findAll() = departmentService.findAll()
        .map { DepartmentResponse(it) }
        .let { ResponseEntity.ok(it) }

}