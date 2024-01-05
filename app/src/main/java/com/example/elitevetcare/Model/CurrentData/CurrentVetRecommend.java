package com.example.elitevetcare.Model.CurrentData;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.elitevetcare.Helper.HelperCallingAPI;
import com.example.elitevetcare.Helper.Libs;
import com.example.elitevetcare.Helper.ProgressHelper;
import com.example.elitevetcare.Model.ObjectModel.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;

public class CurrentVetRecommend{
    private static CurrentVetRecommend instance = null;
    private static MutableLiveData<ArrayList<User>> ListVetRecommend = new MutableLiveData<>();
    public static void init(ArrayList<User>  list){
        instance = new CurrentVetRecommend();
        ListVetRecommend.setValue(list);
    }
    public static CurrentVetRecommend getInstance(){
        if (instance == null){
            instance = new CurrentVetRecommend();
        }

        return instance;
    }
    public static MutableLiveData<ArrayList<User>>  getListVetRecommend() {
        return CurrentVetRecommend.getInstance().ListVetRecommend;
    }
    public void setCurrentList(ArrayList<User> list) {
        ListVetRecommend.postValue(list);
    }
    public static void AddCurrentList(ArrayList<User> list) {
        ArrayList<User> CurrentList = ListVetRecommend.getValue();
        if(CurrentList != null){
            for(User Vet : list){
                boolean Duplicate = false;
                for(User DuplicateVet : CurrentList){
                    if(DuplicateVet.getId() == Vet.getId()){
                        Duplicate = true;
                        break;
                    }

                }
                if(Duplicate == false)
                    CurrentList.add(Vet);
            }
            ListVetRecommend.postValue(CurrentList);
        }else
            ListVetRecommend.postValue(list);
    }
    public static CurrentVetRecommend CreateInstanceByAPI(ListCallBack callback){
        if(instance == null){
            instance = new CurrentVetRecommend();
        }
        Map<String,String> QuerryParam = new HashMap<>();
        QuerryParam.put("userId",CurrentUser.getCurrentUser().getId()+"");
        Log.d("Runalbe", "Đã Đến bước 1");
        HelperCallingAPI.CallingAPI_QueryParams_Get(HelperCallingAPI.VET_RECOMMEND_API_PATH, QuerryParam,new HelperCallingAPI.MyCallback() {
            @Override
            public void onResponse(Response response) {
                int statusCode = response.code();
                JSONArray data = null;
                if (statusCode == 200) {
                    try {
                        data = new JSONArray(response.body().string());
                        Gson gson = new Gson();
                        //Type listType = new TypeToken<ArrayList<Pet>>(){}.getType();
                        ArrayList<User> user = gson.fromJson(data.toString(), new TypeToken<ArrayList<User>>(){}.getType());
                        instance.setCurrentList(user);
                        callback.onListGeted(instance);
                    } catch (IOException | JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            @Override
            public void onFailure(IOException e) {
                if(ProgressHelper.isDialogVisible())
                    ProgressHelper.dismissDialog();
            }
        });

        return instance;
    }
    public interface ListCallBack {
        void onListGeted(CurrentVetRecommend currentUser);
    }
}
