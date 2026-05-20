package com.pulseforge.infrastructure.delivery.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CreateWorkoutRequest(
        @NotBlank(message = "Workout name is required")
        @Size(max = 100, message = "Workout name must not exceed 100 characters")
        String name,

        @Size(max = 255, message = "Description must not exceed 255 characters")
        String description,

        @NotEmpty(message = "Workout must contain at least one exercise set")
        @Valid
        List<ExerciseSetRequest> sets
) {
    public record ExerciseSetRequest(
            @NotBlank(message = "Exercise name is required")
            String exerciseName,

            int repetitions,
            double weightKg,
            int restSeconds
    ) {}
}