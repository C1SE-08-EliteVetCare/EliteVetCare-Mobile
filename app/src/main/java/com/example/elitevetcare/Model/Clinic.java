package com.example.elitevetcare.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Clinic implements Serializable {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String clinicName;

    @SerializedName("city")
    private String city;

    @SerializedName("district")
    private String district;

    @SerializedName("ward")
    private String ward;

    @SerializedName("streetAddress")
    private String streetAddress;

    @SerializedName("logo")
    private String logo;

    // Constructor, getters, and setters

    public Clinic(int id, String name, String city, String district, String ward, String streetAddress, String logo) {
        this.id = id;
        this.clinicName = name;
        this.city = city;
        this.district = district;
        this.ward = ward;
        this.streetAddress = streetAddress;
        this.logo = logo;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return clinicName;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getWard() {
        return ward;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getLogo() {
        return logo;
    }

    // Additional methods if needed
}