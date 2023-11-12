package com.example.elitevetcare.Helper;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SignUpViewModel extends ViewModel {

    final private MutableLiveData<String> name = new MutableLiveData<String>();
    final private MutableLiveData<String> email = new MutableLiveData<String>();
    final private MutableLiveData<String> password = new MutableLiveData<String>();
    final private MutableLiveData<String> phonenumber = new MutableLiveData<String>();


    public void setUserName(String name){this.name.setValue(name);}
    public void setEmail(String email){this.email.setValue(email);}
    public void setPassword(String password){this.password.setValue(password);}
    public void setPhonenumber(String phonenumber){this.phonenumber.setValue(phonenumber);}

    public MutableLiveData<String> getUserName() {
        return this.name;
    }

    public MutableLiveData<String> getEmail() {
        return this.email;
    }

    public MutableLiveData<String> getPassword() {
        return this.password;
    }

    public MutableLiveData<String> getPhonenumber() {
        return this.phonenumber;
    }
}
