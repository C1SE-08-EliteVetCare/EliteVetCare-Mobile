package com.example.elitevetcare.Model;

import com.google.gson.annotations.SerializedName;

public class User {


    private int id;

    private String email;

    private String fullName;

    private String gender;

    private String city;

    private String district;

    private String ward;

    private String streetAddress;

    private String birthYear; // Integer để xử lý giá trị null

    private String avatar;

    private String phone;

    private String clinic;

    private Role role;

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getGender() {
        return gender;
    }

    public String getCity() {
        return city;
    }
    public String getWard() {
        return ward;
    }
    public String getDistrict() {
        return district;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getPhone() {
        return phone;
    }

    public String getClinic() {
        return clinic;
    }

    public Role getRole() {
        return role;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
    public void setWard(String ward) {
        this.ward = ward;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setClinic(String clinic) {
        this.clinic = clinic;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    public class Role {

        private int id;

        private String name;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

}

