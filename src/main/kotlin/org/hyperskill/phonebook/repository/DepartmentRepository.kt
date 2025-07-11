package org.hyperskill.phonebook.repository

import org.hyperskill.phonebook.model.Department
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface DepartmentRepository : JpaRepository<Department, UUID>
