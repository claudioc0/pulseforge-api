package com.pulseforge.core.domain;

public class WorkoutValidationException extends RuntimeException {
    public WorkoutValidationException(String message) {
        super(message);
    }
}