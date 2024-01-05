package com.example.elitevetcare.Model.ObjectModel;

import com.google.gson.annotations.SerializedName;

public class PetCondition {
    @SerializedName("id")
    private int id;

    @SerializedName("portion")
    private int portion;

    @SerializedName("weight")
    private double weight;

    @SerializedName("meal")
    private String meal;

    @SerializedName("manifestation")
    private String manifestation;

    @SerializedName("conditionOfDefecation")
    private String conditionOfDefecation;

    @SerializedName("actualImg")
    private String actualImg;

    @SerializedName("vetAdvice")
    private String vetAdvice;

    @SerializedName("recommendedMedicines")
    private String recommendedMedicines;

    @SerializedName("recommendedMeal")
    private String recommendedMeal;

    @SerializedName("dateUpdate")
    private String dateUpdate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPortion() {
        return portion;
    }

    public void setPortion(int portion) {
        this.portion = portion;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public String getManifestation() {
        return manifestation;
    }

    public void setManifestation(String manifestation) {
        this.manifestation = manifestation;
    }

    public String getConditionOfDefecation() {
        return conditionOfDefecation;
    }

    public void setConditionOfDefecation(String conditionOfDefecation) {
        this.conditionOfDefecation = conditionOfDefecation;
    }

    public String getActualImg() {
        return actualImg;
    }

    public void setActualImg(String actualImg) {
        this.actualImg = actualImg;
    }

    public String getVetAdvice() {
        return vetAdvice;
    }

    public void setVetAdvice(String vetAdvice) {
        this.vetAdvice = vetAdvice;
    }

    public String getRecommendedMedicines() {
        return recommendedMedicines;
    }

    public void setRecommendedMedicines(String recommendedMedicines) {
        this.recommendedMedicines = recommendedMedicines;
    }

    public String getRecommendedMeal() {
        return recommendedMeal;
    }

    public void setRecommendedMeal(String recommendedMeal) {
        this.recommendedMeal = recommendedMeal;
    }

    public String getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(String dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

}