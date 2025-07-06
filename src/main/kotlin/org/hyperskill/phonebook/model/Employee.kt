package org.hyperskill.phonebook.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "employee")

data class Employee(

    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val position: String,

    @Column( nullable = false)
    val phone: String,

    @Column(nullable = true)
    val email: String? = null,

    @ManyToOne
    @JoinColumn(name = "department_id")
    val department: Department? = null,

    @CreationTimestamp
    @Column( nullable = false)
    val createdAt: Instant = Instant.now(),

    @UpdateTimestamp
    @Column(nullable = false)
    val updatedAt: Instant = Instant.now(),

    @Column(name = "is_active", nullable = false)
    val isActive: Boolean = true
)