package com.pulseforge.infrastructure.persistence.repository;

import com.pulseforge.infrastructure.persistence.entity.WorkoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SpringDataWorkoutRepository extends JpaRepository<WorkoutEntity, UUID> {
}