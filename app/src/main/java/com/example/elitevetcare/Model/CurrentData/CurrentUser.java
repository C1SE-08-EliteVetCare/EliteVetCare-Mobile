package com.example.elitevetcare.Model.CurrentData;


import com.example.elitevetcare.Helper.HelperCallingAPI;
import com.example.elitevetcare.Model.ObjectModel.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

public class CurrentUser {
    private static CurrentUser instance = null;
    private static User currentUser = null;
    public static void init(User user){
        instance = new CurrentUser();
        currentUser = user;
    }
    public static CurrentUser getInstance(){
        if (instance == null)
            instance = new CurrentUser();
        return instance;
    }
    public static User getCurrentUser() {
        return CurrentUser.getInstance().currentUser;
    }
    public void setCurrentUser(User user) {
        currentUser = user;
    }

    public static CurrentUser CreateInstanceByAPI(UserCallback callback){
        if(instance == null){
            instance = new CurrentUser();
        }
        HelperCallingAPI.CallingAPI_Get(HelperCallingAPI.CURRENT_USER_PATH, new HelperCallingAPI.MyCallback() {
            @Override
            public void onResponse(Response response) {
                int statusCode = response.code();
                JSONObject data = null;
                if (statusCode == 200) {
                    try {
                        data = new JSONObject(response.body().string());
                        Gson gson = new Gson();
                        //Type listType = new TypeToken<ArrayList<Pet>>(){}.getType();
                        User user = gson.fromJson(data.toString(), new TypeToken<User>(){}.getType());
                        instance.setCurrentUser(user);
                        callback.onUserGeted(instance);
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
    public interface UserCallback {
        void onUserGeted(CurrentUser currentUser);
    }
}
