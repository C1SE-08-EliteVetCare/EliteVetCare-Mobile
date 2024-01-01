package com.example.elitevetcare.Model.CurrentData;

import com.example.elitevetcare.Helper.HelperCallingAPI;
import com.example.elitevetcare.Model.ObjectModel.Conversation;
import com.example.elitevetcare.Model.ObjectModel.User;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Response;

public class CurrentConversationHistory {
    private static CurrentConversationHistory instance = null;
    private static ArrayList<Conversation> ConversationHistory = null;

    public static void setIsLoad(boolean isLoad) {
        CurrentConversationHistory.isLoad = isLoad;
    }

    private static boolean isLoad = false;

    public static boolean isIsLoad() {
        return isLoad;
    }

    public static void init(ArrayList<Conversation> AllOfConversation){
        instance = new CurrentConversationHistory();
        ConversationHistory = AllOfConversation;
    }
    public static CurrentConversationHistory getInstance(){
        if (instance == null)
            instance = new CurrentConversationHistory();
        return instance;
    }
    public static ArrayList<Conversation> getConversationHistory() {
        return CurrentConversationHistory.getInstance().ConversationHistory;
    }
    public void setConversationHistory(ArrayList<Conversation> AllOfConversation) {
        ConversationHistory = AllOfConversation;
    }
    public static CurrentConversationHistory CreateInstanceByAPI(CurrentConversationHistory.UserCallback callback){
        if(instance == null){
            instance = new CurrentConversationHistory();
        }
        if(isLoad == true)
            return instance;
        HelperCallingAPI.CallingAPI_Get(HelperCallingAPI.GET_ALL_CONVERSATION, new HelperCallingAPI.MyCallback() {
            @Override
            public void onResponse(Response response) {
                int statusCode = response.code();
                JSONArray data = null;
                if (statusCode == 200) {
                    try {
                        data = new JSONArray(response.body().string());
                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<Conversation>>(){}.getType();
                        ArrayList<Conversation> ConversationArray = gson.fromJson(data.toString(), listType);
                        instance.setConversationHistory(ConversationArray);

                        callback.onGetSuccess();
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
        void onGetSuccess();
    }
}
