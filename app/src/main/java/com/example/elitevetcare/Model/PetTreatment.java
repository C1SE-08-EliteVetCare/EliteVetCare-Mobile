package com.example.elitevetcare.Model;

import com.google.gson.annotations.SerializedName;

public class PetTreatment {
    private int id;
    private int clinicId;

    @SerializedName("dateAccepted")
    private String dateAccepted;

    @SerializedName("createdAt")
    private String createdAt;

    private Pet pet;
    private Vet vet;

    public static class Pet {
        private int id;
        private String name;
        private String species;
        private String breed;
        private String gender;
        private int age;
        private int weight;

        @SerializedName("furColor")
        private String furColor;

        @SerializedName("avatar")
        private String avatar;

        private Owner owner;

        // Constructors, getters, and setters...
    }

    public static class Owner {
        private int id;
        private String email;

        @SerializedName("fullName")
        private String fullName;

        private String phone;

    }

    public static class Vet {
        private int id;
        private String email;

        @SerializedName("fullName")
        private String fullName;

        private String phone;

        // Constructors, getters, and setters...
    }
}