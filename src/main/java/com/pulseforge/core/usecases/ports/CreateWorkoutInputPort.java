package com.pulseforge.core.usecases.ports;

import com.pulseforge.core.domain.Workout;
import java.util.List;

public interface CreateWorkoutInputPort {
    Workout execute(CreateWorkoutCommand command);

    record CreateWorkoutCommand(
            String name,
            String description,
            List<ExerciseSetCommand> sets
    ) {
        public record ExerciseSetCommand(
                String exerciseName,
                int repetitions,
                double weightKg,
                int restSeconds
        ) {}
    }
}