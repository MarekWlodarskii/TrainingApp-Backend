package com.MarekW.TrainingApp.Repo;

import com.MarekW.TrainingApp.Model.Exercises;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExercisesRepo extends JpaRepository<Exercises, Long> {
    List<Exercises> findByExerciseName(String exerciseName);
    void deleteById(Long id);
    Optional<Exercises> findById(Long id);
}



