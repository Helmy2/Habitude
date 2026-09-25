package com.habitude.api.habit

import com.habitude.api.common.exception.ResourceNotFoundException
import com.habitude.api.habit.dto.CreateHabitRequest
import com.habitude.api.habit.dto.HabitResponse
import com.habitude.api.habit.dto.UpdateHabitRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class HabitService(
    private val habitRepository: HabitRepository
) {
    fun getAllHabits(includeArchived: Boolean): List<HabitResponse> {
        val habits = if (includeArchived) {
            habitRepository.findAllByOrderByCreatedAtDesc()
        } else {
            habitRepository.findAllByArchivedFalseOrderByCreatedAtDesc()
        }
        return habits.map { HabitResponse.fromEntity(it) }
    }

    fun getHabitById(id: Long): HabitResponse {
        val habit = findHabitOrThrow(id)
        return HabitResponse.fromEntity(habit)
    }

    @Transactional
    fun createHabit(request: CreateHabitRequest): HabitResponse {
        val habit = Habit(
            title = request.title.trim(),
            description = request.description?.trim()?.ifBlank { null },
            frequencyType = request.frequencyType,
            targetCount = request.targetCount
        )
        val saved = habitRepository.save(habit)
        return HabitResponse.fromEntity(saved)
    }

    @Transactional
    fun updateHabit(id: Long, request: UpdateHabitRequest): HabitResponse {
        val habit = findHabitOrThrow(id)
        habit.updateDetails(
            title = request.title.trim(),
            description = request.description?.trim()?.ifBlank { null },
            frequencyType = request.frequencyType,
            targetCount = request.targetCount,
            archived = request.archived
        )
        return HabitResponse.fromEntity(habit)
    }

    @Transactional
    fun setArchivedStatus(id: Long, archived: Boolean): HabitResponse {
        val habit = findHabitOrThrow(id)
        if (archived) {
            habit.archive()
        } else {
            habit.unarchive()
        }
        return HabitResponse.fromEntity(habit)
    }

    @Transactional
    fun deleteHabit(id: Long) {
        val habit = findHabitOrThrow(id)
        habitRepository.delete(habit)
    }

    private fun findHabitOrThrow(id: Long): Habit =
        habitRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Habit with id $id not found") }
}