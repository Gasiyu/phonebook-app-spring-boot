package org.hyperskill.phonebook.adapter

import org.hyperskill.phonebook.model.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class AppUserAdapter(private val user: User) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> {
        val userRoles = user.roles.map { SimpleGrantedAuthority("ROLE_${it.name.uppercase()}") }
        LOGGER.debug("User roles: {}", userRoles)
        return userRoles
    }

    override fun getPassword(): String = requireNotNull(user.password)

    override fun getUsername(): String = requireNotNull(user.username)

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(AppUserAdapter::class.java)
    }
}
