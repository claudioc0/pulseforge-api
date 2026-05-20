package com.pulseforge.core.usecases.ports;

import com.pulseforge.core.domain.Workout;

public interface WorkoutRepository {
    Workout save(Workout workout);
}