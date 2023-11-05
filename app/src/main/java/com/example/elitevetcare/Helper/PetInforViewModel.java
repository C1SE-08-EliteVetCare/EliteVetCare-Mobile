package com.example.elitevetcare.Helper;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PetInforViewModel extends ViewModel {
    private final MutableLiveData<String> species = new MutableLiveData<String>();
    private final MutableLiveData<String> breed = new MutableLiveData<String>();

    private final MutableLiveData<String> nickName = new MutableLiveData<String>();

    private final MutableLiveData<String> Color = new MutableLiveData<String>();

    private final MutableLiveData<File> FileImage = new MutableLiveData<File>();

    private final MutableLiveData<String> Gender = new MutableLiveData<String>();
    private final MutableLiveData<Integer> Age = new MutableLiveData<Integer>();
    private final MutableLiveData<Double> Weight = new MutableLiveData<Double>();
    public void setWeight(Double Weight){
        this.Weight.setValue(Weight);
    }
    public void setAge(int Age){
        this.Age.setValue(Age);
    }
    public void setGender(int GenderSelected){
        if(GenderSelected == 1)
            this.Gender.setValue("Đực");
        if(GenderSelected == 2)
            this.Gender.setValue("Cái");
    }

    public void setAvatar(File file){
        FileImage.setValue(file);
    }
    public void setAvatarByBitmap(File CacheDir,Bitmap bitmap){
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

    public void setAvatarByUri(Uri uri){
        File imageFile = new File(uri.getPath());
        this.FileImage.setValue(imageFile);
    }
    public void setSpecies(String species) {
        this.species.setValue(species);
    }

    public void setBreed(String breed) {
        this.breed.setValue(breed);
    }

    public void setNickName(String nickName) {
        this.nickName.setValue(nickName);
    }

    public void setColor(String Color) {
        this.Color.setValue(Color);
    }


    public MutableLiveData<String> getSpecies() {
        return this.species;
    }
    public MutableLiveData<String> getBreed() {
        return this.breed;
    }
    public MutableLiveData<String> getNickName() {
        return this.nickName;
    }
    public MutableLiveData<String> getColor() {
        return this.Color;
    }
    public MutableLiveData<File> getFileImage() {
        return this.FileImage;
    }
    public MutableLiveData<String> getGender() {
        return this.Gender;
    }
    public MutableLiveData<Integer> getAge() {
        return this.Age;
    }
    public MutableLiveData<Double> getWeight() {
        return this.Weight;
    }
}
