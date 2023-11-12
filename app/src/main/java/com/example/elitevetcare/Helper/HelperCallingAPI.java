package com.example.elitevetcare.Helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;

import com.example.elitevetcare.Activity.Login;
import com.example.elitevetcare.Activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
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
    final public static String GOOGLE_LOGIN= "auth/google/response-token";
    final public static String PET_CONDITION_PATH = "pet/condition/";
    final public static String UPDATE_ADVICE_PATH = "pet/vet-advice/";
    final public static String PROVINCE_HOST = "https://provinces.open-api.vn/api/";
    final public static String PROVINCE_API_PATH = "p/";
    final public static String DISTRICT_API_PATH = "d/";
    final public static String WARD_API_PATH = "w/";
    final public static String QUERRY_PARAM_PROVINCE_API = "?depth=2";
    final public static String UPDATE_PROFILE_API_PATH = "user/update-profile";
    static boolean refresh_status = false;

    public static void CallingAPI_Province(String API_PATH, String params , MyCallback callback){
        HttpUrl Url = HttpUrl.parse(PROVINCE_HOST+API_PATH + params)
                .newBuilder()
                .build();

        Request request = new Request.Builder()
                .url(Url)
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
    public static void CallingAPI_PathParams_Get(String API_PATH, String params , MyCallback callback){
        HttpUrl Url = HttpUrl.parse(API_ENDPOINT+API_PATH)
                .newBuilder()
                .addPathSegment(params)
                .build();
        if(DataLocalManager.GetAccessTokenTimeUp() < System.currentTimeMillis())
            RefreshTokenCalling();

        String HeaderToken = DataLocalManager.GetAccessToken();

        Request request = new Request.Builder()
                .url(Url)
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
    public static void CallingAPI_QueryParams_Get(String API_PATH, Map<String,String> params , MyCallback callback){
        HttpUrl.Builder Url = HttpUrl.parse(API_ENDPOINT+API_PATH).newBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            Url.addQueryParameter(entry.getKey(),entry.getValue());
        }
        if(DataLocalManager.GetAccessTokenTimeUp() < System.currentTimeMillis())
            RefreshTokenCalling();

        String HeaderToken = DataLocalManager.GetAccessToken();

        Request request = new Request.Builder()
                .url(Url.build())
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
    public static void CallingAPI_PathParams_Put(String API_PATH, String params , RequestBody body, MyCallback callback){
        HttpUrl Url = HttpUrl.parse(API_ENDPOINT+API_PATH).newBuilder()
                .build();

        if(DataLocalManager.GetAccessTokenTimeUp() < System.currentTimeMillis())
            RefreshTokenCalling();
        String HeaderToken = DataLocalManager.GetAccessToken();

        Request request = new Request.Builder()
                .url(Url)
                .put(body)
                .addHeader("Authorization", "Bearer " + HeaderToken)
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
    public static void CallingAPI_QueryParams_Post(String API_PATH, Map<String,String> params , RequestBody body, MyCallback callback){


        HttpUrl.Builder Url = HttpUrl.parse(API_ENDPOINT+API_PATH).newBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            Url.addQueryParameter(entry.getKey(),entry.getValue());
        }

        if(DataLocalManager.GetAccessTokenTimeUp() < System.currentTimeMillis())
            RefreshTokenCalling();

        String HeaderToken = DataLocalManager.GetAccessToken();

        Request request = new Request.Builder()
                .url(Url.build())
                .post(body)
                .addHeader("Authorization", "Bearer " + HeaderToken)
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
        if(DataLocalManager.GetAccessTokenTimeUp() < System.currentTimeMillis())
            RefreshTokenCalling();
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
