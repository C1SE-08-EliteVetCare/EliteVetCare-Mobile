package com.example.elitevetcare.Model.ObjectModel;

public class ServicePackage {
    String name;
    String description;
    String image_service;
    public ServicePackage() {
        this.name = "";
        this.description = "";
        String image_service = "";
    }
    public ServicePackage(String name, String description) {
        this.name = name;
        this.description = description;
        this.image_service = "";
    }
    public ServicePackage(String name, String description, String image_service) {
        this.name = name;
        this.description = description;
        this.image_service = image_service;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_service() {
        return image_service;
    }

    public void setImage_service(String image_service) {
        this.image_service = image_service;
    }


}
