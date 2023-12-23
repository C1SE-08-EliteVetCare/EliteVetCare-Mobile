package com.example.elitevetcare.Helper.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EmailViewModel extends ViewModel {
    final private MutableLiveData<String> email = new MutableLiveData<String>();
    public void setEmail(String email){this.email.setValue(email);}
    public MutableLiveData<String> getEmail() {
        return this.email;
    }
}
