package com.habitude.api.habit

import com.habitude.api.habit.dto.CreateHabitRequest
import com.habitude.api.habit.dto.HabitResponse
import com.habitude.api.habit.dto.UpdateHabitRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/api/v1/habits")
class HabitController(
    private val habitService: HabitService
) {
    @GetMapping
    fun getAllHabits(
        @RequestParam(defaultValue = "false") includeArchived: Boolean
    ): ResponseEntity<List<HabitResponse>> {
        val habits = habitService.getAllHabits(includeArchived)
        return ResponseEntity.ok(habits)
    }

    @GetMapping("/{id}")
    fun getHabitById(@PathVariable id: Long): ResponseEntity<HabitResponse> {
        val habit = habitService.getHabitById(id)
        return ResponseEntity.ok(habit)
    }

    @PostMapping
    fun createHabit(
        @Valid @RequestBody request: CreateHabitRequest
    ): ResponseEntity<HabitResponse> {
        val created = habitService.createHabit(request)
        val location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(created.id)
            .toUri()
        return ResponseEntity.created(location).body(created)
    }

    @PutMapping("/{id}")
    fun updateHabit(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateHabitRequest
    ): ResponseEntity<HabitResponse> {
        val updated = habitService.updateHabit(id, request)
        return ResponseEntity.ok(updated)
    }

    @PatchMapping("/{id}/archive")
    fun archiveHabit(@PathVariable id: Long): ResponseEntity<HabitResponse> {
        val updated = habitService.setArchivedStatus(id, true)
        return ResponseEntity.ok(updated)
    }

    @PatchMapping("/{id}/unarchive")
    fun unarchiveHabit(@PathVariable id: Long): ResponseEntity<HabitResponse> {
        val updated = habitService.setArchivedStatus(id, false)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{id}")
    fun deleteHabit(@PathVariable id: Long): ResponseEntity<Unit> {
        habitService.deleteHabit(id)
        return ResponseEntity.noContent().build()
    }
}