package com.habitude.api.habit

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HabitRepository : JpaRepository<Habit, Long> {
    fun findAllByArchivedFalseOrderByCreatedAtDesc(): List<Habit>
    fun findAllByOrderByCreatedAtDesc(): List<Habit>
}