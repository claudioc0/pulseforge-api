package com.pulseforge.core.domain;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Aggregate root representing a workout session.
 * All business invariants are enforced at construction time.
 * This class has zero dependencies on any framework or library.
 */
public final class Workout {

    private final UUID id;
    private final String name;
    private final String description;
    private final List<ExerciseSet> sets;
    private final Instant createdAt;

    private Workout(UUID id, String name, String description, List<ExerciseSet> sets, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.sets = List.copyOf(sets);
        this.createdAt = createdAt;
    }

    public static Workout create(String name, String description, List<ExerciseSet> sets) {
        validateName(name);
        validateSets(sets);
        return new Workout(UUID.randomUUID(), name, description, sets, Instant.now());
    }

    public static Workout reconstitute(UUID id, String name, String description, List<ExerciseSet> sets, Instant createdAt) {
        Objects.requireNonNull(id, "id must not be null");
        validateName(name);
        validateSets(sets);
        Objects.requireNonNull(createdAt, "createdAt must not be null");
        return new Workout(id, name, description, sets, createdAt);
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new WorkoutValidationException("Workout name must not be blank");
        }
        if (name.length() > 100) {
            throw new WorkoutValidationException("Workout name must not exceed 100 characters");
        }
    }

    private static void validateSets(List<ExerciseSet> sets) {
        if (sets == null || sets.isEmpty()) {
            throw new WorkoutValidationException("A workout must contain at least one exercise set");
        }
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<ExerciseSet> getSets() { return sets; }
    public Instant getCreatedAt() { return createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Workout other)) return false;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Workout{id=" + id + ", name='" + name + "', sets=" + sets.size() + "}";
    }
}
