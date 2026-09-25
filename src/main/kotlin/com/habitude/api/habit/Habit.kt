package com.habitude.api.habit

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(
    name = "habits",
    indexes = [
        Index(name = "idx_habits_archived_created", columnList = "archived, createdAt DESC")
    ]
)
class Habit(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, length = 120)
    var title: String,

    @Column(columnDefinition = "TEXT")
    var description: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var frequencyType: FrequencyType = FrequencyType.DAILY,

    @Column(nullable = false)
    var targetCount: Int = 1,

    @Column(nullable = false)
    var archived: Boolean = false,

    @Column(nullable = false, updatable = false)
    val createdAt: Instant = Instant.now()
) {
    fun updateDetails(
        title: String,
        description: String?,
        frequencyType: FrequencyType,
        targetCount: Int,
        archived: Boolean
    ) {
        this.title = title
        this.description = description
        this.frequencyType = frequencyType
        this.targetCount = targetCount
        this.archived = archived
    }

    fun archive() {
        this.archived = true
    }

    fun unarchive() {
        this.archived = false
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Habit) return false
        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String =
        "Habit(id=$id, title='$title', frequencyType=$frequencyType, archived=$archived)"
}