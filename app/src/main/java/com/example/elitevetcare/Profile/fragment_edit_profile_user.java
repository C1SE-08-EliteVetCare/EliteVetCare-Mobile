package com.example.elitevetcare.Profile;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import com.example.elitevetcare.Helper.HelperCallingAPI;
import com.example.elitevetcare.Helper.Libs;
import com.example.elitevetcare.Model.CurrentData.CurrentUser;
import com.example.elitevetcare.Model.ObjectModel.Province;
import com.example.elitevetcare.Model.ObjectModel.User;
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

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_edit_profile_user#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_edit_profile_user extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private ArrayList<String> list_city,list_distrist,list_ward;
//    private String address;
//    ArrayAdapter<String> adapterItems;
//    AppCompatAutoCompleteTextView autoCompleteText_city,autoCompleteText_distrist,autoCompleteText_ward;

    AppCompatButton btn_update;

    public fragment_edit_profile_user() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_edit_profile_user.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_edit_profile_user newInstance(String param1, String param2) {
        fragment_edit_profile_user fragment = new fragment_edit_profile_user();
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

    }


    public void setData(){
        User user = CurrentUser.getCurrentUser();
        edt_username.setText(user.getFullName());
        edt_birthyear.setText(user.getBirthYear());
        edt_emal.setText(user.getEmail());
        ArrayAdapter<String> spinnerAdapter = (ArrayAdapter<String>) spinner_gender.getAdapter();
        spinner_gender.setSelection(spinnerAdapter.getPosition(user.getGender()));
        GetDataProvince(2);
        edt_address.setText(user.getStreetAddress());
    }

    private void showYearPickerDialog() {
        // Create a custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_date_picker);
        dialog.setTitle("Select Year");

        // Find views in the custom dialog
        DatePicker datePicker = dialog.findViewById(R.id.dPk_datePicker);
        Button okButton = dialog.findViewById(R.id.btn_ok);

        // Set a listener for the OK button
        okButton.setOnClickListener(view -> {
            // Handle the selected year
            int selectedYear = datePicker.getYear();
            Toast.makeText(getActivity(), "Selected Year: " + selectedYear, Toast.LENGTH_SHORT).show();


            edt_birthyear.setText(String.valueOf(selectedYear));

            // Dismiss the dialog
            dialog.dismiss();
        });

        // Show the custom dialog
        dialog.show();
    }

    AppCompatEditText edt_username, edt_birthyear, edt_emal, edt_address ;
    AppCompatSpinner  spinner_gender, spinner_city, spinner_district, spinner_ward;

    List<Province> ListProvince;
    Province SelectedProvince;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_profile_user, container, false);

        edt_username = root.findViewById(R.id.edt_edits_username);
        edt_birthyear = root.findViewById(R.id.date_edit_birthday_user);
        edt_emal = root.findViewById(R.id.edt_edit_user_email);
        spinner_gender = root.findViewById(R.id.spinner_sex_editprofile);
        spinner_city = root.findViewById(R.id.spinner_city_editprofile);
        spinner_district = root.findViewById(R.id.spinner_district_editprofile);
        spinner_ward = root.findViewById(R.id.spinner_ward_editprofile);
        edt_address = root.findViewById(R.id.edt_edit_user_address);
        btn_update = root.findViewById(R.id.btn_update_user_profile);
        edt_username.setText(CurrentUser.getCurrentUser().getFullName());
        edt_birthyear.setText(CurrentUser.getCurrentUser().getBirthYear());

        setData();
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateUserProfile();
            }

        });

        edt_birthyear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_birthyear.clearFocus();
                showYearPickerDialog();
            }
        });

        // Inflate the layout for this fragment
        return root;
    }

    private void updateUserProfile() {
        // Retrieve user input from UI

        String updatedName = edt_username.getText().toString();
        String updatedBirthYear = edt_birthyear.getText().toString();
        String updatedSex = spinner_gender.getSelectedItem().toString();
        String updatedCity = spinner_city.getSelectedItem().toString();
        String updatedDistrict = spinner_district.getSelectedItem().toString();
        String updatedWard = spinner_ward.getSelectedItem().toString();
        String updatedAddress = edt_address.getText().toString();



        // Bắt lỗi
        // Libs.capitalizeFirstLetter()
        // String name = Libs.capitalizeFirstLetter(ListProvince.get(spinner_city.getSelectedItemPosition()).getName());
        // String sex = Libs.capitalizeFirstLetter(ListProvince.get(spinner_city.getSelectedItemPosition()).getName());
        // String birthyear = Libs.capitalizeFirstLetter(ListProvince.get(spinner_city.getSelectedItemPosition()).getName());


        String city = Libs.capitalizeFirstLetter(ListProvince.get(spinner_city.getSelectedItemPosition()).getName());
        String district = Libs.capitalizeFirstLetter(SelectedProvince.getDistricts().get(spinner_district.getSelectedItemPosition()).getName());
        String ward = Libs.capitalizeFirstLetter(SelectedProvince.getDistricts().get(spinner_district.getSelectedItemPosition())
                .getWards().get(spinner_ward.getSelectedItemPosition()).getName());
        String streetAddress = Libs.capitalizeFirstLetter(edt_address.getText().toString());
        if(streetAddress.isEmpty()){
            Toast.makeText(getContext(), "Hãy Nhập Địa Chỉ Vào", Toast.LENGTH_SHORT).show();
        }

        // CurrentUser.getCurrentUser().setGender(sex);
        // CurrentUser.getCurrentUser().setFullName(name);
        // CurrentUser.getCurrentUser().setBirthYear(birthyear);
        CurrentUser.getCurrentUser().setCity(city);
        CurrentUser.getCurrentUser().setDistrict(district);
        CurrentUser.getCurrentUser().setWard(ward);
        CurrentUser.getCurrentUser().setStreetAddress(streetAddress);

        // Libs.HandleString()

        // Create a RequestBody with updated information
        RequestBody body = new FormBody.Builder()
                .add("fullName", updatedName)
                .add("birthYear", updatedBirthYear)
                .add("gender", updatedSex)
                .add("city", updatedCity)
                .add("district", updatedDistrict)
                .add("ward", updatedWard)
                .add("streetAddress", updatedAddress)
                .build();

        // Call your Retrofit API to update user information
        HelperCallingAPI.CallingAPI_noHeader(HelperCallingAPI.UPDATE_PROFILE_API_PATH, body, new HelperCallingAPI.MyCallback() {
            @Override
            public void onResponse(Response response) {
                int statusCode = response.code();
                if(statusCode == 200) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Update Success", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"Update False", Toast.LENGTH_SHORT).show();
                        }
                    });
            }

            @Override
            public void onFailure(IOException e) {

            }
        });
    }

    private void GetDataProvince(int status ) {
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
                        if(status == 1)
                            SetDataDistrict(1);
                        else{
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ArrayAdapter<String> spinnerAdapter = (ArrayAdapter<String>) spinner_city.getAdapter();
                                    spinner_city.setSelection(spinnerAdapter.getPosition(CurrentUser.getCurrentUser().getCity()));
                                    SetDataDistrict(2);
                                }
                            });
                        }
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
    private void SetDataDistrict(int status) {
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
                                if(status == 1)
                                    SetDataWard(1);
                                else {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            ArrayAdapter<String> spinnerAdapter = (ArrayAdapter<String>) spinner_district.getAdapter();
                                            spinner_district.setSelection(spinnerAdapter.getPosition(CurrentUser.getCurrentUser().getDistrict()));
                                            SetDataWard(2);
                                        }
                                    });
                                }
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

    private void SetDataWard(int status) {
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
                                if(status == 2)
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ArrayAdapter<String> spinnerAdapter = (ArrayAdapter<String>) spinner_ward.getAdapter();
                                            spinner_ward.setSelection(spinnerAdapter.getPosition(CurrentUser.getCurrentUser().getWard()));
                                        }
                                    });

//                                setData();
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


}