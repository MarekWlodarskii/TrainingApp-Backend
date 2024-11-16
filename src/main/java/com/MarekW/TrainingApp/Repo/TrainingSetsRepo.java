package com.MarekW.TrainingApp.Repo;

import com.MarekW.TrainingApp.Model.TrainingSets;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TrainingSetsRepo extends JpaRepository<TrainingSets, Long> {
    List<TrainingSets> findByDate(LocalDate date);
    Boolean existsByExerciseNameAndDateBefore(String exerciseName, LocalDate date);
    TrainingSets findTopByDateBeforeAndExerciseNameOrderByWeightDesc(LocalDate date, String exerciseName);
}



