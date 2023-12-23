package com.example.elitevetcare.Model.CurrentData;

import android.util.Log;

import com.example.elitevetcare.Helper.HelperCallingAPI;
import com.example.elitevetcare.Model.ObjectModel.Appointment;
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

public class CurrentAppointment {
    private static CurrentAppointment instance = null;
    private static ArrayList<Appointment> ProcessingListAppointment = null;
    private static int ProcessingPage = -1;
    private static ArrayList<Appointment> AcceptedListAppointment = null;
    private static int AcceptedPage = -1;
    private static ArrayList<Appointment> RejectedListAppointment = null;
    private static int RejectedPage = -1;
    public static CurrentAppointment getInstance(){
        if (instance == null)
            instance = new CurrentAppointment();
        return instance;
    }
    public static int getProcessingPage() {
        return CurrentAppointment.getInstance().ProcessingPage;
    }

    public static void setProcessingPage(int processingPage) {
        CurrentAppointment.getInstance().ProcessingPage = processingPage;
    }

    public static int getAcceptedPage() {
        return CurrentAppointment.getInstance().AcceptedPage;
    }

    public static void setAcceptedPage(int acceptedPage) {
        CurrentAppointment.getInstance().AcceptedPage = acceptedPage;
    }

    public static int getRejectedPage() {
        return CurrentAppointment.getInstance().RejectedPage;
    }

    public static void setRejectedPage(int rejectedPage) {
        CurrentAppointment.getInstance().RejectedPage = rejectedPage;
    }

    public static ArrayList<Appointment> getAcceptedAppointmentList() {
        return CurrentAppointment.getInstance().AcceptedListAppointment;
    }
    public static ArrayList<Appointment> getRejectAppointmentList() {
        return CurrentAppointment.getInstance().RejectedListAppointment;
    }
    public static ArrayList<Appointment> getProcessingAppointmentList() {
        return CurrentAppointment.getInstance().ProcessingListAppointment;
    }

    public void setProcessingAppointmentList(ArrayList<Appointment> list) {
        ProcessingListAppointment = list;
    }
    public void setAcceptedAppointmentList(ArrayList<Appointment> list) {
        AcceptedListAppointment = list;
    }
    public void setRejectAppointmentList(ArrayList<Appointment> list) {
        RejectedListAppointment = list;
    }
    public static CurrentAppointment CreateInstanceByAPI(Map<String,String> QuerryParams, AppointmentCallback callback){
        if(instance == null)
            instance = new CurrentAppointment();
        String API_PATH = "";
        int status = Integer.parseInt(QuerryParams.get("status"));
        if(CurrentUser.getCurrentUser().getRole().getId() == 2){
            API_PATH = HelperCallingAPI.APPOINTMENT_PET_OWNER_API_PATH;
        } else if (CurrentUser.getCurrentUser().getRole().getId() == 3) {
            API_PATH = HelperCallingAPI.APPOINTMENT_VET_API_PATH;
        }
        HelperCallingAPI.CallingAPI_QueryParams_Get(API_PATH, QuerryParams, new HelperCallingAPI.MyCallback() {
            @Override
            public void onResponse(Response response) {
                int statusCode = response.code();
                JSONObject data = null;
                if(statusCode == 200) {
                    try {
                        data = new JSONObject(response.body().string());
                        JSONArray ListPetData = data.getJSONArray("data");
                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<Appointment>>(){}.getType();
                        ArrayList<Appointment> List = gson.fromJson(ListPetData.toString(), listType);
                        if(status == 1)
                            instance.setProcessingAppointmentList(List);
                        else if (status == 2) {
                            instance.setAcceptedAppointmentList(List);
                        }else if (status == 3) {
                            instance.setRejectAppointmentList(List);
                        }
                        callback.OnSuccess(data);
                    } catch (IOException | JSONException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    Log.d("AppointmentRespone", String.valueOf(statusCode));
                    try {
                        Log.d("AppointmentRespone", new JSONObject(response.body().string()).toString());
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
    public interface AppointmentCallback {
        void OnSuccess(JSONObject JsonData) throws JSONException;
    }
}
