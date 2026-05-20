package com.pulseforge.core.usecases;

import com.pulseforge.core.domain.ExerciseSet;
import com.pulseforge.core.domain.Workout;
import com.pulseforge.core.usecases.ports.CreateWorkoutInputPort;
import com.pulseforge.core.usecases.ports.WorkoutRepository;

import java.util.List;
import java.util.Objects;

/**
 * Core use case: orchestrates the creation of a new workout.
 *
 * <p>This class depends only on domain objects and port interfaces defined
 * within the core. It has zero knowledge of Spring, JPA, HTTP, or any other
 * infrastructure concern. Dependency inversion is achieved by injecting the
 * {@link WorkoutRepository} output port at construction time.</p>
 */
public final class CreateWorkoutUseCase implements CreateWorkoutInputPort {

    private final WorkoutRepository workoutRepository;

    public CreateWorkoutUseCase(WorkoutRepository workoutRepository) {
        this.workoutRepository = Objects.requireNonNull(workoutRepository, "workoutRepository must not be null");
    }

    @Override
    public Workout execute(CreateWorkoutCommand command) {
        Objects.requireNonNull(command, "command must not be null");

        List<ExerciseSet> sets = mapToExerciseSets(command.sets());
        Workout workout = Workout.create(command.name(), command.description(), sets);

        return workoutRepository.save(workout);
    }

    private List<ExerciseSet> mapToExerciseSets(List<CreateWorkoutCommand.ExerciseSetCommand> setCommands) {
        return setCommands.stream()
                .map(cmd -> new ExerciseSet(
                        cmd.exerciseName(),
                        cmd.repetitions(),
                        cmd.weightKg(),
                        cmd.restSeconds()
                ))
                .toList();
    }
}
