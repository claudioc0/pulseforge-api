package com.pulseforge.infrastructure.configuration;

import com.pulseforge.core.usecases.CreateWorkoutUseCase;
import com.pulseforge.core.usecases.ports.CreateWorkoutInputPort;
import com.pulseforge.core.usecases.ports.WorkoutRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorkoutConfiguration {

    @Bean
    public CreateWorkoutInputPort createWorkoutInputPort(WorkoutRepository workoutRepository) {
        return new CreateWorkoutUseCase(workoutRepository);
    }
}