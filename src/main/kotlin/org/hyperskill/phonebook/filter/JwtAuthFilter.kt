package org.hyperskill.phonebook.filter


import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.hyperskill.phonebook.service.JwtService
import org.hyperskill.phonebook.service.UserAuthService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component
class JwtAuthFilter(
    private val jwtService: JwtService,
    private val userAuthService: UserAuthService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requestUri = request.requestURI
        val method = request.method
        LOGGER.debug("Processing JWT authentication for {} {}", method, requestUri)

        val authHeader: String? = request.getHeader("Authorization")

        if (authHeader !== null && authHeader.startsWith("Bearer ")) {
            LOGGER.debug("Authorization header found with Bearer token")
            val token = authHeader.substring(7)

            try {
                val username = jwtService.extractUsername(token)
                LOGGER.debug("Extracted username from token: {}", username)

                if (SecurityContextHolder.getContext().authentication == null) {
                    LOGGER.debug("No existing authentication found, attempting to authenticate user: {}", username)

                    val userDetails: UserDetails = userAuthService.loadUserByUsername(username)

                    if (jwtService.isValid(token, userDetails)) {
                        LOGGER.info("JWT token validated successfully for user: {}", username)
                        val authToken = UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.authorities
                        )
                        authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                        SecurityContextHolder.getContext().authentication = authToken
                        LOGGER.debug(
                            "Authentication set in security context for user: {} with authorities: {}",
                            username, userDetails.authorities
                        )
                    } else {
                        LOGGER.warn("JWT token validation failed for user: {}", username)
                    }
                } else {
                    LOGGER.debug("Authentication already exists in security context for user: {}", username)
                }
            } catch (e: Exception) {
                LOGGER.error("Error processing JWT token: {}", e.message, e)
            }
        } else {
            LOGGER.debug("No Authorization header or Bearer token found for {} {}", method, requestUri)
        }

        filterChain.doFilter(request, response)
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(JwtAuthFilter::class.java)
    }
}
