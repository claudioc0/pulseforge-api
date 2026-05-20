package com.pulseforge.infrastructure.delivery;

import com.pulseforge.core.domain.Workout;
import com.pulseforge.core.usecases.ports.CreateWorkoutInputPort;
import com.pulseforge.core.usecases.ports.CreateWorkoutInputPort.CreateWorkoutCommand;
import com.pulseforge.core.usecases.ports.CreateWorkoutInputPort.CreateWorkoutCommand.ExerciseSetCommand;
import com.pulseforge.infrastructure.delivery.dto.CreateWorkoutRequest;
import com.pulseforge.infrastructure.delivery.dto.WorkoutResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * REST Controller exposing the workout management API.
 *
 * <p>Responsibilities:
 * <ul>
 *   <li>Receive and validate HTTP requests via Jakarta Validation.</li>
 *   <li>Map the validated DTO into a use case command.</li>
 *   <li>Delegate all business logic to the {@link CreateWorkoutInputPort}.</li>
 *   <li>Map the domain result back to a response DTO and return HTTP 201.</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping("/api/v1/workouts")
public class WorkoutController {

    private final CreateWorkoutInputPort createWorkoutInputPort;

    public WorkoutController(CreateWorkoutInputPort createWorkoutInputPort) {
        this.createWorkoutInputPort = createWorkoutInputPort;
    }

    @PostMapping
    public ResponseEntity<WorkoutResponse> createWorkout(@RequestBody @Valid CreateWorkoutRequest request) {
        CreateWorkoutCommand command = toCommand(request);
        Workout created = createWorkoutInputPort.execute(command);
        WorkoutResponse response = WorkoutResponse.from(created);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    private CreateWorkoutCommand toCommand(CreateWorkoutRequest request) {
        List<ExerciseSetCommand> setCommands = request.sets().stream()
                .map(s -> new ExerciseSetCommand(s.exerciseName(), s.repetitions(), s.weightKg(), s.restSeconds()))
                .toList();
        return new CreateWorkoutCommand(request.name(), request.description(), setCommands);
    }
}
