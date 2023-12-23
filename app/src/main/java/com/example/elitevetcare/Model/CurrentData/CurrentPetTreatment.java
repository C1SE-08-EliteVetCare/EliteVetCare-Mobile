package com.example.elitevetcare.Model.CurrentData;

import com.example.elitevetcare.Helper.HelperCallingAPI;
import com.example.elitevetcare.Model.ObjectModel.PetTreatment;
import com.github.mikephil.charting.data.Entry;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.Response;

public class CurrentPetTreatment {
    private static CurrentPetTreatment instance = null;
    private static ArrayList<PetTreatment> PetTreatmentReadyList = null;
    private static ArrayList<PetTreatment> PetTreatmentAcceptedList = null;
    public static CurrentPetTreatment getInstance(){
        if (instance == null)
            instance = new CurrentPetTreatment();
        return instance;
    }
    public static ArrayList<PetTreatment> getPetTreatmentReadyList() {
        return CurrentPetTreatment.getInstance().PetTreatmentReadyList;
    }

    public void setPetTreatmentReadyList(ArrayList<PetTreatment> list) {
        if (list == null)
            list = new ArrayList<>();
        CurrentPetTreatment.getInstance().PetTreatmentReadyList = list;
    }
    public static ArrayList<PetTreatment> getPetTreatmentAcceptedList() {
        return CurrentPetTreatment.getInstance().PetTreatmentAcceptedList;
    }

    public void setPetTreatmentAcceptedList(ArrayList<PetTreatment> list) {
        if (list == null)
            list = new ArrayList<>();
        CurrentPetTreatment.getInstance().PetTreatmentAcceptedList = list;
    }


    public static CurrentPetTreatment CreateInstanceByAPI(Map<String, String> querryParams, PetTreatmentListCallback callback){
        if(instance == null)
            instance = new CurrentPetTreatment();
        String API_PATH = HelperCallingAPI.GET_LIST_TREATMENT_API_PATH;
        int status = Integer.parseInt(querryParams.get("status"));
        if(querryParams != null){
            API_PATH += "?";
            for (Map.Entry<String, String> entry : querryParams.entrySet()) {
                API_PATH += entry.getKey() + "=" + entry.getValue() +"&";

            }
        }

        HelperCallingAPI.CallingAPI_Get(API_PATH, new HelperCallingAPI.MyCallback() {
            @Override
            public void onResponse(Response response) {
                JSONObject data = null;
                if(response.isSuccessful()) {
                    try {
                        data = new JSONObject(response.body().string());
                        JSONArray ListPetData = data.getJSONArray("data");
                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<PetTreatment>>(){}.getType();
                        ArrayList<PetTreatment> pets = gson.fromJson(ListPetData.toString(), listType);
                        if (status == 1)
                            instance.setPetTreatmentReadyList(pets);
                        else if (status == 2)
                            instance.setPetTreatmentAcceptedList(pets);
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
    public interface PetTreatmentListCallback {
        void OnSuccess(CurrentPetTreatment currentPetTreatmentList);
    }
}
