package com.example.elitevetcare.Model;

public class Pet {
    private int id;
    private String name;
    private String species;
    private String breed;
    private String gender;
    private int age;
    private float weight;
    private String furColor;
    private String avatar;
    private int ownerId;
    private String createdAt;
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setFurColor(String furColor) {
        this.furColor = furColor;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public String getBreed() {
        return breed;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public float getWeight() {
        return weight;
    }

    public String getFurColor() {
        return furColor;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
