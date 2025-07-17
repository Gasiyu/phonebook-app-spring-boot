package org.hyperskill.phonebook.adapter

import org.hyperskill.phonebook.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class AppUserAdapter(private val user: User) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> {
        println("User roles: ${user.roles}")
        return user.roles.map { SimpleGrantedAuthority("ROLE_${it.name.uppercase()}") }
    }

    override fun getPassword(): String = requireNotNull(user.password)

    override fun getUsername(): String = requireNotNull(user.username)

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}