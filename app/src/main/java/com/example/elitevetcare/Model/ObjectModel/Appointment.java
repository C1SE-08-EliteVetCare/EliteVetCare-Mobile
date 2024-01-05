package com.example.elitevetcare.Model.ObjectModel;

import com.google.gson.annotations.SerializedName;

public class Appointment {

    @SerializedName("id")
    private int id;

    @SerializedName("appointmentDate")
    private String appointmentDate;

    @SerializedName("appointmentTime")
    private String appointmentTime;

    @SerializedName("servicePackage")
    private String servicePackage;

    @SerializedName("status")
    private int status;

    @SerializedName("acceptedId")
    private String acceptedId;  // Integer để cho phép giá trị null

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("vetAppointment")
    private VetAppointment vetAppointment;

    @SerializedName("clinic")
    private Clinic clinic;
    @SerializedName("petOwner")
    private PetOwner petOwner;

    // Constructor, getters, and setters

//    public Appointment(int id, String appointmentDate, String appointmentTime, String servicePackage, int status, String acceptedId, String createdAt, String updatedAt, VetAppointment vetAppointment, Clinic clinic) {
//        this.id = id;
//        this.appointmentDate = appointmentDate;
//        this.appointmentTime = appointmentTime;
//        this.servicePackage = servicePackage;
//        this.status = status;
//        this.acceptedId = acceptedId;
//        this.createdAt = createdAt;
//        this.updatedAt = updatedAt;
//        this.vetAppointment = vetAppointment;
//        this.clinic = clinic;
//    }
public int getId() {
    return id;
}

    public void setId(int id) {
        this.id = id;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getServicePackage() {
        return servicePackage;
    }

    public void setServicePackage(String servicePackage) {
        this.servicePackage = servicePackage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAcceptedId() {
        return acceptedId;
    }

    public void setAcceptedId(String acceptedId) {
        this.acceptedId = acceptedId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public VetAppointment getVetAppointment() {
        return vetAppointment;
    }

    public void setVetAppointment(VetAppointment vetAppointment) {
        this.vetAppointment = vetAppointment;
    }

    public Clinic getClinic() {
        return clinic;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    public PetOwner getPetOwner() {
        return petOwner;
    }

    public void setPetOwner(PetOwner petOwner) {
        this.petOwner = petOwner;
    }

    public class VetAppointment {
        @SerializedName("vetId")
        private int vetId;

        @SerializedName("fullName")
        private String fullName;

        @SerializedName("email")
        private String email;

        @SerializedName("phone")
        private String phone;

        @SerializedName("dateAccepted")
        private String dateAccepted;


        public int getVetId() {
            return vetId;
        }

        public String getFullName() {
            return fullName;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }

        public String getDateAccepted() {
            return dateAccepted;
        }
    }
    public class PetOwner {
        @SerializedName("id")
        private int id;

        @SerializedName("fullName")
        private String fullName;

        @SerializedName("email")
        private String email;

        @SerializedName("phone")
        private String phone;

        @SerializedName("avatar")
        private String avatar;
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }



    }
    // Additional methods if needed
}