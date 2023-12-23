package com.example.elitevetcare.Profile;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.elitevetcare.Model.CurrentUser;
import com.example.elitevetcare.Helper.HelperCallingAPI;
import com.example.elitevetcare.Helper.Libs;
import com.example.elitevetcare.Model.Province;
import com.example.elitevetcare.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_address#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_address extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<String> list_city,list_distrist,list_ward;
    private String address;
    ArrayAdapter<String> adapterItems;
    AppCompatAutoCompleteTextView autoCompleteText_city,autoCompleteText_distrist,autoCompleteText_ward;

    public fragment_address() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_address.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_address newInstance(String param1, String param2) {
        fragment_address fragment = new fragment_address();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    ArrayList<String> ProvinceArray = new ArrayList<>();
    ArrayList<String> DistrictArray = new ArrayList<>();
    ArrayList<String> WardArray = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        GetDataProvince();
    }

    AppCompatSpinner spinner_city, spinner_district, spinner_ward;
    AppCompatEditText edt_address;

    List<Province> ListProvince;
    Province SelectedProvince;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_address, container, false);

        spinner_city = root.findViewById(R.id.spinner_city_Useraddress);
        spinner_district = root.findViewById(R.id.spinner_district_Useraddress);
        spinner_ward = root.findViewById(R.id.spinner_ward_Useraddress);
        edt_address = root.findViewById(R.id.edt_street_address);
        // Inflate the layout for this fragment
        return root;
    }



    private void GetDataProvince() {
        HelperCallingAPI.CallingAPI_Province(HelperCallingAPI.PROVINCE_API_PATH, "", new HelperCallingAPI.MyCallback() {
            @Override
            public void onResponse(Response response) {
                int statusCode = response.code();
                Log.d("ProvinceTest", String.valueOf(statusCode));
                JSONArray data = null;
                if(statusCode == 200){
                    try {
                        data = new JSONArray(response.body().string());
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Province>>(){}.getType();
                        ListProvince = gson.fromJson(data.toString(), listType);
                        for (Province city: ListProvince) {
                            ProvinceArray.add(city.getName());
                        }
                        SetUpSpinner(spinner_city,ProvinceArray);
                        SetDataDistrict();
                        Log.d("ProvinceTest", ListProvince.get(0).getCodename());

                    } catch (JSONException | IOException e) {
                        Log.d("ProvinceError", e.getMessage() );
                    }
                }
            }

            @Override
            public void onFailure(IOException e) {

            }
        });
    }
    private void SetDataDistrict() {
        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int cityCode = ListProvince.get(position).getCode();
                HelperCallingAPI.CallingAPI_Province(HelperCallingAPI.PROVINCE_API_PATH + cityCode, HelperCallingAPI.QUERRY_PARAM_PROVINCE_API, new HelperCallingAPI.MyCallback() {
                    @Override
                    public void onResponse(Response response) {
                        int statusCode = response.code();
                        JSONObject data = null;
                        if(statusCode == 200){
                            try {
                                data = new JSONObject(response.body().string());
                                Gson gson = new Gson();
                                Type listType = new TypeToken<Province>(){}.getType();
                                SelectedProvince = gson.fromJson(data.toString(), listType);
                                DistrictArray.clear();
                                for (Province.District district: SelectedProvince.getDistricts()) {
                                    DistrictArray.add(district.getName());
                                }
                                SetUpSpinner(spinner_district,DistrictArray);
                                SetDataWard();
                            } catch (JSONException | IOException e) {
                                Log.d("ProvinceError", e.getMessage() );
                            }
                        }
                    }

                    @Override
                    public void onFailure(IOException e) {

                    }
                });
                Log.d("ProvinceTest", String.valueOf(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void SetDataWard() {
        spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int cityCode = SelectedProvince.getDistricts().get(position).getCode();
                HelperCallingAPI.CallingAPI_Province(HelperCallingAPI.DISTRICT_API_PATH + cityCode, HelperCallingAPI.QUERRY_PARAM_PROVINCE_API, new HelperCallingAPI.MyCallback() {
                    @Override
                    public void onResponse(Response response) {
                        int statusCode = response.code();
                        JSONObject data = null;
                        if(statusCode == 200){
                            try {
                                data = new JSONObject(response.body().string());
                                Gson gson = new Gson();
                                Type listType = new TypeToken<Province.District>(){}.getType();
                                Province.District currentDistrict = gson.fromJson(data.toString(), listType);
                                SelectedProvince.getDistricts().set(position,currentDistrict);
                                WardArray.clear();
                                for (Province.District.Ward Ward: SelectedProvince.getDistricts().get(position).getWards()) {
                                    WardArray.add(Ward.getName());
                                }
                                SetUpSpinner(spinner_ward,WardArray);
                            } catch (JSONException | IOException e) {
                                Log.d("ProvinceError", e.getMessage() );
                            }
                        }
                    }

                    @Override
                    public void onFailure(IOException e) {

                    }
                });
                Log.d("ProvinceTest", String.valueOf(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void SetUpSpinner(AppCompatSpinner spinner, ArrayList<String> arrayString) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, arrayString);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }
        });
    }

    public boolean SendData(){

        String city = Libs.capitalizeFirstLetter(ListProvince.get(spinner_city.getSelectedItemPosition()).getName());
        String district = Libs.capitalizeFirstLetter(SelectedProvince.getDistricts().get(spinner_district.getSelectedItemPosition()).getName());
        String ward = Libs.capitalizeFirstLetter(SelectedProvince.getDistricts().get(spinner_district.getSelectedItemPosition())
                .getWards().get(spinner_ward.getSelectedItemPosition()).getName());
        String streetAddress = Libs.capitalizeFirstLetter(edt_address.getText().toString());
        if(streetAddress.isEmpty()){
            Toast.makeText(getContext(), "Hãy Nhập Địa Chỉ Vào", Toast.LENGTH_SHORT).show();
            return false;
        }

        CurrentUser.getCurrentUser().setCity(city);
        CurrentUser.getCurrentUser().setDistrict(district);
        CurrentUser.getCurrentUser().setWard(ward);
        CurrentUser.getCurrentUser().setStreetAddress(streetAddress);

        return true;
    }
}