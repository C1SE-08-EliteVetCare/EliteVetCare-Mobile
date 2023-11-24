package com.example.elitevetcare.Model.ViewModel;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PetConditionViewModel extends ViewModel {
    private final MutableLiveData<File> FileImage = new MutableLiveData<File>();
    private final MutableLiveData<String> portion = new MutableLiveData<String>();
    private final MutableLiveData<String> weight = new MutableLiveData<String>();
    private final MutableLiveData<String> meal = new MutableLiveData<String>();
    private final MutableLiveData<String> manifestation = new MutableLiveData<String>();
    private final MutableLiveData<String> conditionOfDefecation = new MutableLiveData<String>();
    public  void setPortion(String portion){
        this.portion.setValue(portion);
    }
    public  void setWeight(String weight){
        this.weight.setValue(weight);
    }
    public  void setMeal(String meal){
        this.meal.setValue(meal);
    }
    public  void setManifestation(String manifestation){
        this.manifestation.setValue(manifestation);
    }
    public  void setConditionOfDefecation(String conditionOfDefecation){
        this.conditionOfDefecation.setValue(conditionOfDefecation);
    }

    public MutableLiveData<File> getFileImage() {
        return FileImage;
    }

    public MutableLiveData<String> getPortion() {
        return portion;
    }

    public MutableLiveData<String> getWeight() {
        return weight;
    }

    public MutableLiveData<String> getMeal() {
        return meal;
    }

    public MutableLiveData<String> getManifestation() {
        return manifestation;
    }

    public MutableLiveData<String> getConditionOfDefecation() {
        return conditionOfDefecation;
    }
    public void setAvatar(File file){
        FileImage.setValue(file);
    }
    public void setAvatarByBitmap(File CacheDir, Bitmap bitmap){
        File imageFile = new File(CacheDir, "image.png");
        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos); // Lưu dưới dạng PNG
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
