package com.pulseforge.infrastructure.delivery.dto;

import com.pulseforge.core.domain.Workout;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record WorkoutResponse(
        UUID id,
        String name,
        String description,
        List<ExerciseSetResponse> sets,
        Instant createdAt
) {
    public static WorkoutResponse from(Workout workout) {
        List<ExerciseSetResponse> setsResponse = workout.getSets().stream()
                .map(s -> new ExerciseSetResponse(s.exerciseName(), s.repetitions(), s.weightKg(), s.restSeconds()))
                .toList();

        return new WorkoutResponse(
                workout.getId(),
                workout.getName(),
                workout.getDescription(),
                setsResponse,
                workout.getCreatedAt()
        );
    }

    public record ExerciseSetResponse(
            String exerciseName,
            int repetitions,
            double weightKg,
            int restSeconds
    ) {}
}