package com.pulseforge.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "exercise_sets")
public class ExerciseSetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String exerciseName;

    @Column(nullable = false)
    private int repetitions;

    @Column(nullable = false)
    private double weightKg;

    @Column(nullable = false)
    private int restSeconds;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id", nullable = false)
    private WorkoutEntity workout;

    // Construtor padrão exigido pelo Hibernate
    public ExerciseSetEntity() {}

    public ExerciseSetEntity(String exerciseName, int repetitions, double weightKg, int restSeconds) {
        this.exerciseName = exerciseName;
        this.repetitions = repetitions;
        this.weightKg = weightKg;
        this.restSeconds = restSeconds;
    }

    // Getters e Setters corrigidos e limpos
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(double weightKg) {
        this.weightKg = weightKg;
    }

    public int getRestSeconds() {
        return restSeconds;
    }

    public void setRestSeconds(int restSeconds) {
        this.restSeconds = restSeconds;
    }

    public WorkoutEntity getWorkout() {
        return workout;
    }

    public void setWorkout(WorkoutEntity workout) {
        this.workout = workout;
    }
}