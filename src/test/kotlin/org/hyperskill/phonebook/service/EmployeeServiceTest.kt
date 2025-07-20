package org.hyperskill.phonebook.service

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.hyperskill.phonebook.dtos.request.employee.CreateEmployeeRequest
import org.hyperskill.phonebook.model.Department
import org.hyperskill.phonebook.model.Employee
import org.hyperskill.phonebook.repository.DepartmentRepository
import org.hyperskill.phonebook.repository.EmployeeRepository
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*


class EmployeeServiceTest : WordSpec({

    val employeeRepository = mockk<EmployeeRepository>()
    val departmentRepository = mockk<DepartmentRepository>()
    val employeeService = EmployeeService(employeeRepository, departmentRepository)

    "employee service" should {
        
        "get all employees" {
            val employees = listOf(
                Employee(
                    name = "Ann Kramm",
                    position = "testter",
                    phone = "123456789",
                    email = "ann@gmail.com"
                ),
                Employee(
                    name = "Benjamin Kramm",
                    position = "test",
                    phone = "123456789",
                    email = "benjo@gmail.com"
                )
            )


            val pageRequest = PageRequest.of(1, 10)
            val employeePage = PageImpl(employees, pageRequest, employees.size.toLong())
            every { employeeRepository.findAll(pageRequest) } returns employeePage

            val result = employeeService.getAllEmployees(1)
            result.content[0].name shouldBe "Ann Kramm"
            result.content[1].name shouldBe "Benjamin Kramm"

        }

        "save a new employee with department" {
            val departmentId = UUID.randomUUID()
            val department = Department(id = departmentId, name = "IT")
            val request = CreateEmployeeRequest(
                name = "Ann Kramm",
                position = "tester",
                phone = "123456789",
                email = "ann@gmail.com",
                departmentId = departmentId
            )

            every { departmentRepository.findById(departmentId) } returns Optional.of(department)
            every { employeeRepository.save(any()) } answers { firstArg() }

            val result = employeeService.store(request)

            result.name shouldBe "Ann Kramm"
            result.position shouldBe "tester"
            result.phone shouldBe "123456789"
            result.email shouldBe "ann@gmail.com"
            result.department shouldBe department
        }


        "delete an existing employee" {
            val employeeId = UUID.randomUUID()
            val employee = Employee(
                id = employeeId,
                name = "Max Mustermann",
                position = "Entwickler",
                phone = "0123456789",
                email = "max@firma.de"
            )
            every { employeeRepository.findById(employeeId) } returns Optional.of(employee)
            every { employeeRepository.delete(employee) } returns Unit

            employeeService.deleteEmployee(employeeId)
        }
    }


})
