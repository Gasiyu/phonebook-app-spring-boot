package org.hyperskill.phonebook.service

import org.hyperskill.phonebook.adapter.AppUserAdapter
import org.hyperskill.phonebook.model.User
import org.hyperskill.phonebook.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserAuthService(
    private val userRepository: UserRepository,
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    fun getUser(username: String): User {
        return userRepository.findByUsername(username).orElseThrow { UsernameNotFoundException("User not found") }
    }

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        return AppUserAdapter(getUser(username))
    }
}
