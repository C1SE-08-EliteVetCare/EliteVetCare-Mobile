package com.example.elitevetcare.Model.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BookAppointmentViewModel extends ViewModel {


    private final MutableLiveData<String> Date = new MutableLiveData<String>();
    private final MutableLiveData<String> Time = new MutableLiveData<String>();
    private final MutableLiveData<String> servicePackage = new MutableLiveData<String>();
    private final MutableLiveData<Integer> clinicId = new MutableLiveData<Integer>();
    public void setDate(String date) {
        Date.setValue(date);
    }

    public void setTime(String time) {
        Time.setValue(time);
    }

    public void setServicePackage(String ServiePackage) {
        servicePackage.setValue(ServiePackage);
    }

    public void setClinicId(int id) {
        clinicId.setValue(id);
    }
    public MutableLiveData<String> getDate() {
        return Date;
    }

    public MutableLiveData<String> getTime() {
        return Time;
    }

    public MutableLiveData<String> getServicePackage() {
        return servicePackage;
    }

    public MutableLiveData<Integer> getClinicId() {
        return clinicId;
    }
}
