package com.pulseforge.infrastructure.persistence.adapter;

import com.pulseforge.core.domain.ExerciseSet;
import com.pulseforge.core.domain.Workout;
import com.pulseforge.core.usecases.ports.WorkoutRepository;
import com.pulseforge.infrastructure.persistence.entity.ExerciseSetEntity;
import com.pulseforge.infrastructure.persistence.entity.WorkoutEntity;
import com.pulseforge.infrastructure.persistence.repository.SpringDataWorkoutRepository;
import org.springframework.stereotype.Component;

/**
 * Persistence Adapter: implements the core {@link WorkoutRepository} output port
 * using Spring Data JPA. Responsible for mapping between domain objects and JPA entities.
 *
 * <p>This class is the only place in the persistence layer that knows about
 * both the domain model and the JPA model.</p>
 */
@Component
public class WorkoutPersistenceAdapter implements WorkoutRepository {

    private final SpringDataWorkoutRepository springDataWorkoutRepository;

    public WorkoutPersistenceAdapter(SpringDataWorkoutRepository springDataWorkoutRepository) {
        this.springDataWorkoutRepository = springDataWorkoutRepository;
    }

    @Override
    public Workout save(Workout workout) {
        WorkoutEntity entity = toEntity(workout);
        WorkoutEntity saved = springDataWorkoutRepository.save(entity);
        return toDomain(saved);
    }

    private WorkoutEntity toEntity(Workout workout) {
        WorkoutEntity entity = new WorkoutEntity(
                workout.getId(),
                workout.getName(),
                workout.getDescription(),
                workout.getCreatedAt()
        );
        workout.getSets().stream()
                .map(this::toSetEntity)
                .forEach(entity::addSet);
        return entity;
    }

    private ExerciseSetEntity toSetEntity(ExerciseSet set) {
        return new ExerciseSetEntity(
                set.exerciseName(),
                set.repetitions(),
                set.weightKg(),
                set.restSeconds()
        );
    }

    private Workout toDomain(WorkoutEntity entity) {
        var sets = entity.getSets().stream()
                .map(s -> new ExerciseSet(s.getExerciseName(), s.getRepetitions(), s.getWeightKg(), s.getRestSeconds()))
                .toList();
        return Workout.reconstitute(entity.getId(), entity.getName(), entity.getDescription(), sets, entity.getCreatedAt());
    }
}
