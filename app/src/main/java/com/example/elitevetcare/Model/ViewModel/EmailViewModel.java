package com.example.elitevetcare.Model.ViewModel;

<<<<<<< HEAD
import androidx.lifecycle.LiveData;
=======
>>>>>>> a25147a4e45b1e51c5b870d3a2dec78c4d188046
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
<<<<<<< HEAD


=======
>>>>>>> a25147a4e45b1e51c5b870d3a2dec78c4d188046
}
