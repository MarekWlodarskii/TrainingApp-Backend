package com.MarekW.TrainingApp;

import com.MarekW.TrainingApp.Model.Exercises;
import com.MarekW.TrainingApp.Model.TrainingSets;
import com.MarekW.TrainingApp.Model.TrainingSetsDTO;
import com.MarekW.TrainingApp.Repo.ExercisesRepo;
import com.MarekW.TrainingApp.Repo.TrainingSetsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
public class Controller {

    @Autowired
    TrainingSetsRepo trainingSetsRepo;

    @Autowired
    ExercisesRepo exercisesRepo;

    @GetMapping(path = "/")
    public String temp(){
        //trainingSetsRepo.save(new TrainingSets("Snatch", LocalDate.of(2024, 7, 20), 80f, 1, "RPE 7"));
        //trainingSetsRepo.save(new TrainingSets("Snatch", LocalDate.of(2024, 7, 20), 80f, 2, "RPE 9"));
        //trainingSetsRepo.save(new TrainingSets("Snatch", LocalDate.of(2024, 7, 19), 90f, 2, "RPE 9"));
        exercisesRepo.save(new Exercises("Snatch", "Weightlifting"));
        exercisesRepo.save(new Exercises("Clean and Jerk", "Weightlifting"));
        exercisesRepo.save(new Exercises("Back Squat", "Legs"));
        return "Wit am";
    }

    @GetMapping(path = "/example")
    public List<TrainingSets> getExamples() {
        return trainingSetsRepo.findAll();
    }

    @GetMapping(path = "/getTrainingDay")
    @ResponseBody
    public List<TrainingSetsDTO> GetTrainingSets(@RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return trainingSetsRepo.findByDate(date).stream().map(trainingSet -> ConvertToDTO(trainingSet, trainingSetsRepo)).toList();
    }

    private static TrainingSetsDTO ConvertToDTO(TrainingSets trainingSets, TrainingSetsRepo trainingSetsRepo){
        TrainingSets maxWeightSet = trainingSetsRepo.findTopByDateBeforeAndExerciseNameOrderByWeightDesc(trainingSets.getDate(), trainingSets.getExerciseName());
        Float maxWeight = maxWeightSet == null ? trainingSets.getWeight() : maxWeightSet.getWeight();

        return new TrainingSetsDTO(
                trainingSets.getId(),
                trainingSets.getExerciseName(),
                trainingSets.getDate(),
                trainingSets.getWeight(),
                trainingSets.getReps(),
                trainingSets.getFeedback(),
                maxWeight
        );
    }

    @GetMapping(path = "/getExerciseList")
    public List<Exercises> GetTrainingSets() {
        return exercisesRepo.findAll();
    }

    @GetMapping(path = "/getExerciseMax")
    @ResponseBody
    public ResponseEntity<Float> GetExerciseMax(
            @RequestParam(name = "exerciseName") String exerciseName,
            @RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if(!trainingSetsRepo.existsByExerciseNameAndDateBefore(exerciseName, date)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(ConvertToDTO(trainingSetsRepo.findTopByDateBeforeAndExerciseNameOrderByWeightDesc(date, exerciseName), trainingSetsRepo).getWeight());
    }

    @PostMapping(path = "/saveSet")
    public Long SaveSet(@RequestBody TrainingSets trainingSets){
        TrainingSets savedSet = trainingSetsRepo.save(trainingSets);
        return savedSet.getId();
    }

    @PostMapping(path ="/saveTrainingDay")
    public List<TrainingSetsDTO> SaveTrainingDay(
            @RequestBody List<TrainingSets> trainingSets,
            @RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){

        List<TrainingSets> existingTrainingSets = trainingSetsRepo.findByDate(date);

        // Gdy przychodzaca lista jest pusta
        if(trainingSets == null || trainingSets.isEmpty()){
            if(!existingTrainingSets.isEmpty()){
                for(TrainingSets ts: existingTrainingSets){
                    trainingSetsRepo.deleteById(ts.getId());
                }
            }
            return null;
        }

        // Gdy istniejaca lista jest pusta
        if(existingTrainingSets == null || trainingSets.isEmpty()){
            for(TrainingSets ts: trainingSets){
                trainingSetsRepo.save(ts);
            }
            return trainingSetsRepo.findByDate(date).stream().map(trainingSet -> ConvertToDTO(trainingSet, trainingSetsRepo)).toList();
        }

        // Gdy obie listy sa niepuste
        for(TrainingSets ts: trainingSetsRepo.findByDate(date)){
            if(!trainingSets.stream().anyMatch(trainingSet -> trainingSet.getId().equals(ts.getId()))){
                trainingSetsRepo.deleteById(ts.getId());
            }
        }

        for (TrainingSets ts: trainingSets) {
            Optional<TrainingSets> existingSet = trainingSetsRepo.findById(ts.getId());

            if (!existingSet.isPresent()) {
                trainingSetsRepo.save(ts);
            } else {
                TrainingSets existing = existingSet.get();
                if (!existing.equals(ts)) {
                    trainingSetsRepo.deleteById(ts.getId());
                    trainingSetsRepo.save(ts);
                }
            }

        }

        return trainingSetsRepo.findByDate(trainingSets.get(0).getDate()).stream().map(trainingSet -> ConvertToDTO(trainingSet, trainingSetsRepo)).toList();
    }

    @DeleteMapping(path = "/RemoveRecord")
    public boolean RemoveRecord(
            @RequestParam(name = "id") Long id) {
       trainingSetsRepo.deleteById(id);
       return trainingSetsRepo.findById(id).isEmpty();
    }
}
