package com.example.elitevetcare.Model.CurrentData;

import com.example.elitevetcare.Helper.HelperCallingAPI;
import com.example.elitevetcare.Helper.ProgressHelper;
import com.example.elitevetcare.Model.ObjectModel.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Response;

public class CurrentVetRecommend{
//    private static CurrentVetRecommend instance = null;
//    private static ArrayList<User> ListVetRecommend;
//    public static void init(ArrayList<User>  list){
//        instance = new CurrentVetRecommend();
//        ListVetRecommend = list;
//    }
//    public static CurrentVetRecommend getInstance(){
//        if (instance == null)
//            instance = new CurrentVetRecommend();
//        return instance;
//    }
//    public static ArrayList<User> getListVetRecommend() {
//        return CurrentVetRecommend.getInstance().ListVetRecommend;
//    }
//    public void setCurrentList(ArrayList<User> list) {
//        ListVetRecommend = list;
//    }
//
//    public static CurrentUser CreateInstanceByAPI(ListCallBack callback){
//        if(instance == null){
//            instance = new CurrentVetRecommend();
//        }
//        HelperCallingAPI.CallingAPI_Get(HelperCallingAPI.CURRENT_USER_PATH, new HelperCallingAPI.MyCallback() {
//            @Override
//            public void onResponse(Response response) {
//                int statusCode = response.code();
//                JSONObject data = null;
//                if (statusCode == 200) {
//                    try {
//                        data = new JSONObject(response.body().string());
//                        Gson gson = new Gson();
//                        //Type listType = new TypeToken<ArrayList<Pet>>(){}.getType();
//                        User user = gson.fromJson(data.toString(), new TypeToken<User>(){}.getType());
//                        instance.setCurrentUser(user);
//                        callback.onListGeted(instance);
//                    } catch (IOException | JSONException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
//            @Override
//            public void onFailure(IOException e) {
//                if(ProgressHelper.isDialogVisible())
//                    ProgressHelper.dismissDialog();
//            }
//        });
//
//        return instance;
//    }
//    public interface ListCallBack {
//        void onListGeted(CurrentVetRecommend currentUser);
//    }
}
