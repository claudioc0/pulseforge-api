package com.pulseforge.usecases;

import com.pulseforge.core.domain.Workout;
import com.pulseforge.core.domain.WorkoutValidationException;
import com.pulseforge.core.usecases.CreateWorkoutUseCase;
import com.pulseforge.core.usecases.ports.CreateWorkoutInputPort.CreateWorkoutCommand;
import com.pulseforge.core.usecases.ports.CreateWorkoutInputPort.CreateWorkoutCommand.ExerciseSetCommand;
import com.pulseforge.core.usecases.ports.WorkoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CreateWorkoutUseCase}.
 * Uses Mockito to isolate the use case from the persistence port.
 * No Spring context is loaded — this is a pure unit test.
 */
@DisplayName("CreateWorkoutUseCase")
class CreateWorkoutUseCaseTest {

    private WorkoutRepository workoutRepository;
    private CreateWorkoutUseCase useCase;

    @BeforeEach
    void setUp() {
        workoutRepository = mock(WorkoutRepository.class);
        useCase = new CreateWorkoutUseCase(workoutRepository);
        when(workoutRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    @DisplayName("should create and persist a valid workout")
    void shouldCreateAndPersistValidWorkout() {
        var command = validCommand();

        Workout result = useCase.execute(command);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("Morning Power");
        assertThat(result.getSets()).hasSize(1);
        assertThat(result.getCreatedAt()).isNotNull();
        verify(workoutRepository, times(1)).save(any(Workout.class));
    }

    @Test
    @DisplayName("should throw when command is null")
    void shouldThrowWhenCommandIsNull() {
        assertThatNullPointerException()
                .isThrownBy(() -> useCase.execute(null))
                .withMessage("command must not be null");
    }

    @Test
    @DisplayName("should propagate domain validation exception for blank name")
    void shouldPropagateExceptionForBlankName() {
        var command = new CreateWorkoutCommand("", null, List.of(validSetCommand()));

        assertThatExceptionOfType(WorkoutValidationException.class)
                .isThrownBy(() -> useCase.execute(command))
                .withMessage("Workout name must not be blank");

        verifyNoInteractions(workoutRepository);
    }

    @Test
    @DisplayName("should propagate domain validation exception for empty sets")
    void shouldPropagateExceptionForEmptySets() {
        var command = new CreateWorkoutCommand("Valid Name", null, List.of());

        assertThatExceptionOfType(WorkoutValidationException.class)
                .isThrownBy(() -> useCase.execute(command))
                .withMessage("A workout must contain at least one exercise set");

        verifyNoInteractions(workoutRepository);
    }

    private CreateWorkoutCommand validCommand() {
        return new CreateWorkoutCommand("Morning Power", "Full-body session", List.of(validSetCommand()));
    }

    private ExerciseSetCommand validSetCommand() {
        return new ExerciseSetCommand("Bench Press", 10, 80.0, 60);
    }
}
