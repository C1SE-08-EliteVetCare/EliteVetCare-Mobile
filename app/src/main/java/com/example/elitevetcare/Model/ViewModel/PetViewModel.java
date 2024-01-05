package com.example.elitevetcare.Model.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.elitevetcare.Model.ObjectModel.Pet;
import com.example.elitevetcare.Model.ObjectModel.PetCondition;
import com.example.elitevetcare.Model.ObjectModel.PetTreatment;

import java.util.ArrayList;

public class PetViewModel extends ViewModel {

    private final MutableLiveData<PetTreatment> CurrentPetTreatment = new MutableLiveData<>();
    private final MutableLiveData<Pet> CurrentPet = new MutableLiveData<Pet>();
    private final MutableLiveData<ArrayList<PetCondition>> PetCondition = new MutableLiveData<ArrayList<PetCondition>>();
    public void setCurrentPet(Pet currentPet){
        this.CurrentPet.setValue(currentPet);
    }
    public Pet getCurrentPet(){
        return CurrentPet.getValue();
    }
    public void setCurrentPetTreatment(PetTreatment currentPet){
        this.CurrentPetTreatment.setValue(currentPet);
    }
    public PetTreatment getCurrentPetTreatment() {
        return CurrentPetTreatment.getValue();
    }
    public MutableLiveData<ArrayList<PetCondition>> getPetCondition() {
        return PetCondition;
    }
    public void setPetCondition(ArrayList<PetCondition> conditions) {
        PetCondition.setValue(conditions);
    }
}
