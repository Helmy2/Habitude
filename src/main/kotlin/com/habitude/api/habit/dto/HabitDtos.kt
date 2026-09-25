package com.habitude.api.habit.dto

import com.habitude.api.habit.FrequencyType
import com.habitude.api.habit.Habit
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.Instant

data class CreateHabitRequest(
    @field:NotBlank(message = "Title must not be blank")
    @field:Size(max = 120, message = "Title must be at most 120 characters")
    val title: String,

    val description: String? = null,

    val frequencyType: FrequencyType = FrequencyType.DAILY,

    @field:Min(value = 1, message = "Target count must be at least 1")
    @field:Max(value = 7, message = "Target count cannot exceed 7")
    val targetCount: Int = 1
)

data class UpdateHabitRequest(
    @field:NotBlank(message = "Title must not be blank")
    @field:Size(max = 120, message = "Title must be at most 120 characters")
    val title: String,

    val description: String? = null,

    val frequencyType: FrequencyType,

    @field:Min(value = 1, message = "Target count must be at least 1")
    @field:Max(value = 7, message = "Target count cannot exceed 7")
    val targetCount: Int,

    val archived: Boolean
)

data class HabitResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val frequencyType: FrequencyType,
    val targetCount: Int,
    val archived: Boolean,
    val createdAt: Instant
) {
    companion object {
        fun fromEntity(habit: Habit): HabitResponse = HabitResponse(
            id = checkNotNull(habit.id) { "Habit ID must not be null" },
            title = habit.title,
            description = habit.description,
            frequencyType = habit.frequencyType,
            targetCount = habit.targetCount,
            archived = habit.archived,
            createdAt = habit.createdAt
        )
    }
}
