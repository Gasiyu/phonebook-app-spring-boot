package org.hyperskill.phonebook.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "users")
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    
    @Column(nullable = false, unique = true)
    var username: String,
    
    @Column(nullable = false)
    var password: String,
    
    @Column(nullable = false)
    var role: String,
    
    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true
)
