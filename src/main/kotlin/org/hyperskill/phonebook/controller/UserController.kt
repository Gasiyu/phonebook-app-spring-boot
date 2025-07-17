package org.hyperskill.phonebook.controller

import org.hyperskill.phonebook.dtos.AddUserRoleRequest
import org.hyperskill.phonebook.dtos.UserDto
import org.hyperskill.phonebook.model.Role
import org.hyperskill.phonebook.service.UserService
import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController()
@RequestMapping("api")
@Validated
class UserController(
    private val userService: UserService,
) {
    @GetMapping("/roles")
    fun getRoles(): List<Role> {
        return userService.getRoles()
    }

    @PostMapping("/users/{userId}/roles")
    fun addRole(@PathVariable userId: UUID, @RequestBody @Valid body: AddUserRoleRequest): UserDto {
        return userService.addRole(userId, body.roleId)
    }

    @DeleteMapping("/users/{userId}/roles/{roleId}")
    fun removeRole(@PathVariable userId: UUID, @PathVariable roleId: UUID): UserDto {
        return userService.removeRole(userId, roleId)
    }
}