package org.hyperskill.phonebook.repository

import org.hyperskill.phonebook.model.Employee
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface EmployeeRepository : JpaRepository<Employee, UUID> {
    fun save(employee: Employee): Employee
    fun findByDepartmentId(departmentId: UUID, pageable: Pageable): Page<Employee>
    fun findByPositionContainingIgnoreCase(position: String, pageable: Pageable): Page<Employee>
    fun findByDepartmentIdAndPositionContainingIgnoreCase(departmentId: UUID, position: String, pageable: Pageable): Page<Employee>
}