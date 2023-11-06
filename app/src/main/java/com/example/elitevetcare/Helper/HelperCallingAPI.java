package com.example.elitevetcare.Helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.elitevetcare.Activity.Login;
import com.example.elitevetcare.Activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HelperCallingAPI {
    final static String API_ENDPOINT = "https://elitevetcare-be.up.railway.app/";
    final public static String CREATE_PET_PROFILE_PATH = "pet/create";
    final public static String LOGIN_PATH = "auth/login";
    final public static String REFRESH_TOKEN_PATH = "auth/refresh-token";
    final public static String GET_PET_LIST_PATH = "pet/pets";
    final public static String CURRENT_USER_PATH = "user/me";
    static boolean refresh_status = false;
    public static void CallingAPI_noHeader(String API_PATH, RequestBody body, MyCallback callback){
        String URL = API_ENDPOINT + API_PATH;

        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                callback.onResponse(response);
            }
        });
    }
    public static void CallingAPI_withHeader(String API_PATH, RequestBody body, String HeaderToken, MyCallback callback){
        String URL = API_ENDPOINT + API_PATH;

        Request request = new Request.Builder()
                .url(URL)
                .addHeader("Authorization", "Bearer " + HeaderToken)
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("API_RESPONSE", e.toString());
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d("API_RESPONSE1", response.toString());
                callback.onResponse(response);
            }
        });
    }
    public static void CallingAPI_Get(String API_PATH, MyCallback callback){
        String URL = API_ENDPOINT + API_PATH;
        if( DataLocalManager.getInstance() != null && DataLocalManager.GetAccessTokenTimeUp() < System.currentTimeMillis())
            RefreshToken();
        String HeaderToken = DataLocalManager.GetAccessToken();
        Request request = new Request.Builder()
                .url(URL)
                .addHeader("Authorization", "Bearer " + HeaderToken)
                .get()
                .build();

        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                callback.onResponse(response);
            }
        });
    }
    private static boolean RefreshToken()  {
        String URL = API_ENDPOINT + REFRESH_TOKEN_PATH;
        RequestBody body = new FormBody.Builder()
                .build();
        if(DataLocalManager.getInstance() == null)
            return false;
        Request request = new Request.Builder()
                .url(URL)
                .addHeader("Authorization", "Bearer " + DataLocalManager.GetRefreshToken())
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);

        Response response = null;
        try {
            response = call.execute();
            if(response.code() != 201)
                return false;
            JSONObject data = null;
            data = new JSONObject(response.body().string());
            DataLocalManager.setAccessTokens(data.getString("accessToken"));
        } catch (IOException e) {
            return false;
        } catch (JSONException e) {
            return false;
        }
        return true;
    }
    public static boolean RefreshTokenCalling()  {
        if (refresh_status)
            refresh_status = false;
        Thread refreshThread =  new Thread(() -> {
            if (HelperCallingAPI.RefreshToken()) {
                refresh_status = true;
            }
        });
        refreshThread.start();
        try {
            refreshThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return refresh_status;
    }
    public interface MyCallback {
        void onResponse(Response response);
        void onFailure(IOException e);
    }
}
