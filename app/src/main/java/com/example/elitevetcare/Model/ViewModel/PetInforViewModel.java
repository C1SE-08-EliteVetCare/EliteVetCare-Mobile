package com.example.elitevetcare.Model.ViewModel;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.elitevetcare.Helper.Libs;
import com.example.elitevetcare.Model.ObjectModel.Pet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class PetInforViewModel extends ViewModel {
    private final MutableLiveData<String> species = new MutableLiveData<String>();
    private final MutableLiveData<String> breed = new MutableLiveData<String>();

    private final MutableLiveData<String> nickName = new MutableLiveData<String>();

    private final MutableLiveData<String> Color = new MutableLiveData<String>();

    private final MutableLiveData<File> FileImage = new MutableLiveData<File>();

    private final MutableLiveData<String> Gender = new MutableLiveData<String>();
    private final MutableLiveData<Integer> Age = new MutableLiveData<Integer>();
    private final MutableLiveData<Double> Weight = new MutableLiveData<Double>();
    public void setData(File CacheDir,Pet CurrentPet){
        this.species.setValue(CurrentPet.getSpecies());
        this.breed.setValue(CurrentPet.getBreed());
        this.Gender.setValue(CurrentPet.getGender());
        this.nickName.setValue(CurrentPet.getName());
        this.Color.setValue(CurrentPet.getFurColor());
        this.Age.setValue(CurrentPet.getAge());
        this.Weight.setValue(CurrentPet.getWeight());
        Future<Bitmap> futureBitmap = Libs.URLtoBitMap(CurrentPet);
        try {
            this.setAvatarByBitmap(CacheDir,futureBitmap.get());
        } catch (ExecutionException e) {
            Log.d("ExceptionIMAGE",e.getMessage());
        } catch (InterruptedException e) {
            Log.d("ExceptionIMAGE",e.getMessage());
        }
    }
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
        File imageFile = new File(CacheDir, "image.jpg");
        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            setAvatar(imageFile);
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
