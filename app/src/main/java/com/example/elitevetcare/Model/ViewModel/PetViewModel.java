package com.example.elitevetcare.Model.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.elitevetcare.Model.Pet;
import com.example.elitevetcare.Model.PetCondition;

import java.util.ArrayList;
import java.util.List;

public class PetViewModel extends ViewModel {
    private final MutableLiveData<Pet> CurrentPet = new MutableLiveData<Pet>();
    private final MutableLiveData<ArrayList<PetCondition>> PetCondition = new MutableLiveData<ArrayList<PetCondition>>();
    public void setCurrentPet(Pet currentPet){
        this.CurrentPet.setValue(currentPet);
    }
    public Pet getCurrentPet(){
        return CurrentPet.getValue();
    }
    public MutableLiveData<ArrayList<PetCondition>> getPetCondition() {
        return PetCondition;
    }
    public void setPetCondition(ArrayList<PetCondition> conditions) {
        PetCondition.setValue(conditions);
    }
}
