package com.pulseforge.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "workouts")
public class WorkoutEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    private String description;

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseSetEntity> sets = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    public WorkoutEntity() {}

    public WorkoutEntity(UUID id, String name, String description, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
    }

    public void addSet(ExerciseSetEntity setEntity) {
        sets.add(setEntity);
        setEntity.setWorkout(this);
    }

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<ExerciseSetEntity> getSets() { return sets; }
    public void setSets(List<ExerciseSetEntity> sets) { this.sets = sets; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}