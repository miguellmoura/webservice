package br.pucpr.authserver.departments

import org.springframework.stereotype.Service

@Service
class DepartmentService (val departmentRepository: DepartmentRepository) {

    fun insert(department: Department): Department {
        return departmentRepository.save(department)
    }

    fun findAll(): List<Department> {
        return departmentRepository.findAll()
    }

}