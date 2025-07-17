package org.hyperskill.phonebook

import jakarta.servlet.DispatcherType
import org.hyperskill.phonebook.filter.FilterChainExceptionHandler
import org.hyperskill.phonebook.filter.JwtAuthFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.session.DisableEncodeUrlFilter


@Configuration
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter,
    private val filterChainExceptionHandler: FilterChainExceptionHandler,
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .httpBasic(Customizer.withDefaults())
            .csrf { it.disable() }
            .cors { it.disable() }
            .headers { headers -> headers.frameOptions { it.disable() } }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(HttpMethod.OPTIONS).permitAll()
                    .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                    .requestMatchers("/h2-console/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                    .requestMatchers("/api/roles/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER")
                    .requestMatchers("/api/users/**").hasAuthority("ROLE_ADMIN")
                    .requestMatchers("/api/**").authenticated()
                    .anyRequest().denyAll()
            }
            .addFilterBefore(filterChainExceptionHandler, DisableEncodeUrlFilter::class.java)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Throws(Exception::class)
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }
}
