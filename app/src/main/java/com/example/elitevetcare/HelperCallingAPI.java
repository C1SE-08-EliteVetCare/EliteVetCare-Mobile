package com.example.elitevetcare;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HelperCallingAPI {
    final static String API_ENDPOINT = "https://elitevetcare-be.up.railway.app/";


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
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                callback.onResponse(response);
            }
        });
    }
    public interface MyCallback {
        void onResponse(Response response);
        void onFailure(IOException e);
    }
}
