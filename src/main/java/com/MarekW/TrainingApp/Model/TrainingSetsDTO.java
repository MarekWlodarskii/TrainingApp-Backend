package com.MarekW.TrainingApp.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class TrainingSetsDTO {

    private Long id;
    private String exerciseName;
    private LocalDate date;
    private Float weight;
    private Integer reps;
    private String feedback;
    private Float maxWeight;

    public TrainingSetsDTO() {
    }

    public TrainingSetsDTO(Long id, String exerciseName, LocalDate date, Float weight, Integer reps, String feedback, Float maxWeight) {
        this.id = id;
        this.exerciseName = exerciseName;
        this.date = date;
        this.weight = weight;
        this.reps = reps;
        this.feedback = feedback;
        this.maxWeight = maxWeight;
    }

    // Getters and Setters
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Float getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(Float maxWeight) {
        this.maxWeight = maxWeight;
    }

    @Override
    public String toString() {
        return "super.toString();";
    }
}