package com.example.elitevetcare.Model.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EmailViewModel extends ViewModel {
    final private MutableLiveData<String> email = new MutableLiveData<>();

    public MutableLiveData<String> getEmail() {
        return email;
    }

    public void setEmail(String email){
        this.email.setValue(email);
    }


}
