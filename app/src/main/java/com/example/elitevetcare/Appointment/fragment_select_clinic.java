package com.example.elitevetcare.Appointment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.elitevetcare.Helper.HelperCallingAPI;
import com.example.elitevetcare.Model.Clinic;
import com.example.elitevetcare.R;
import com.example.elitevetcare.Adapter.RecyclerViewClinicAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_select_clinic#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_select_clinic extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_select_clinic() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_select_clinic.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_select_clinic newInstance(String param1, String param2) {
        fragment_select_clinic fragment = new fragment_select_clinic();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    RecyclerView recycler_clinicView;
    RecyclerViewClinicAdapter clinicAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_select_clinic, container, false);
        recycler_clinicView = root.findViewById(R.id.Recycler_list_item_clinic);
        HelperCallingAPI.CallingAPI_Get(HelperCallingAPI.GET_CLINIC_API_PATH, new HelperCallingAPI.MyCallback() {
            @Override
            public void onResponse(Response response) {
                int statusCode = response.code();
                JSONArray data = null;
                if(statusCode == 200) {
                    try {
                        data = new JSONArray(response.body().string());
                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<Clinic>>(){}.getType();
                        ArrayList<Clinic> List = gson.fromJson(data.toString(), listType);
                        //Log.d("ClinicRespone", List.toString());
                        SetData(List);
                    } catch (IOException | JSONException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    Log.d("ClinicRespone", String.valueOf(statusCode));
                    try {
                        Log.d("ClinicRespone", new JSONObject(response.body().string()).toString());
                    } catch (IOException | JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(IOException e) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Có lỗi hãy kiểm tra kết nối mạng và thử lại sau !", Toast.LENGTH_SHORT);
                        requireActivity().finish();
                    }
                });

            }
        });
        // Inflate the layout for this fragment
        return root;
    }

    private void SetData(ArrayList<Clinic> List) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                clinicAdapter = new RecyclerViewClinicAdapter(requireActivity(),List);
                recycler_clinicView.setAdapter(clinicAdapter);
            }
        });

    }
}