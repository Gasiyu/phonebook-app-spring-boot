package org.hyperskill.phonebook.repository

import org.hyperskill.phonebook.model.Employee
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface EmployeeRepository : JpaRepository<Employee, UUID> {
    fun save(employee: Employee): Employee
}