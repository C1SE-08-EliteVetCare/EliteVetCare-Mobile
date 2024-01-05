package com.example.elitevetcare.Model.CurrentData;

import com.example.elitevetcare.Helper.HelperCallingAPI;
import com.example.elitevetcare.Model.ObjectModel.Pet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Response;

public class CurrentPetList {
    private static CurrentPetList instance = null;
    private static ArrayList<Pet> PetList = null;
    public static CurrentPetList getInstance(){
        if (instance == null)
            instance = new CurrentPetList();
        return instance;
    }
    public static ArrayList<Pet> getPetList() {
        return CurrentPetList.getInstance().PetList;
    }
    public void setPetList(ArrayList<Pet> list) {
        if (list == null)
            list = new ArrayList<>();
        PetList = list;
    }
    public static CurrentPetList CreateInstanceByAPI(PetListCallback callback){
        if(instance == null)
            instance = new CurrentPetList();
        HelperCallingAPI.CallingAPI_Get(HelperCallingAPI.GET_PET_LIST_PATH, new HelperCallingAPI.MyCallback() {
            @Override
            public void onResponse(Response response) {
                int statusCode = response.code();
                JSONObject data = null;
                if(statusCode == 200) {
                    try {
                        data = new JSONObject(response.body().string());
                        JSONArray ListPetData = data.getJSONArray("data");
                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<Pet>>(){}.getType();
                        ArrayList<Pet> pets = gson.fromJson(ListPetData.toString(), listType);
                        instance.setPetList(pets);
                        callback.OnSuccess(instance);
                    } catch (IOException | JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
            @Override
            public void onFailure(IOException e) {

            }
        });
        return instance;
    }
    public interface PetListCallback {
        void OnSuccess(CurrentPetList currentPetList);
    }
}
