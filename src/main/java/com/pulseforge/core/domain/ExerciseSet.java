package com.pulseforge.core.domain;

import java.util.Objects;

public record ExerciseSet(
        String exerciseName,
        int repetitions,
        double weightKg,
        int restSeconds
) {
    public ExerciseSet {
        if (exerciseName == null || exerciseName.isBlank()) {
            throw new WorkoutValidationException("Exercise name must not be blank");
        }
        if (repetitions <= 0) {
            throw new WorkoutValidationException("Repetitions must be greater than zero");
        }
        if (weightKg < 0) {
            throw new WorkoutValidationException("Weight cannot be negative");
        }
        if (restSeconds < 0) {
            throw new WorkoutValidationException("Rest time cannot be negative");
        }
    }
}