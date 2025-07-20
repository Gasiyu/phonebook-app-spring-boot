package org.hyperskill.phonebook.model

import jakarta.persistence.*
import java.util.*


@Entity
@Table(name = "roles")
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,
    var name: String,
)
