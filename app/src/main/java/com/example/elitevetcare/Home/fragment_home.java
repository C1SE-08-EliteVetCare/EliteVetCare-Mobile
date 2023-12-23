package com.example.elitevetcare.Home;

import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elitevetcare.Adapter.RecyclerViewAdapter.RecyclerViewPetListAdapter;
import com.example.elitevetcare.Adapter.RecyclerViewAdapter.RecyclerViewPetListHomeAdapter;
import com.example.elitevetcare.Adapter.RecyclerViewAdapter.RecyclerViewPetTreatmentAcceptedAdapter;
import com.example.elitevetcare.Adapter.RecyclerViewAdapter.RecyclerViewClinicHomeAdapter;
import com.example.elitevetcare.Helper.HelperCallingAPI;
import com.example.elitevetcare.Helper.Libs;
import com.example.elitevetcare.Helper.ProgressHelper;
import com.example.elitevetcare.Helper.SocketGate;
import com.example.elitevetcare.Model.CurrentData.CurrentPetList;
import com.example.elitevetcare.Model.CurrentData.CurrentPetTreatment;
import com.example.elitevetcare.Model.CurrentData.CurrentUser;
import com.example.elitevetcare.Model.ObjectModel.Clinic;
import com.example.elitevetcare.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_accept_load.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_home newInstance(String param1, String param2) {
        fragment_home fragment = new fragment_home();
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

    RecyclerView recyclerView;
    RecyclerViewClinicHomeAdapter clinicAdapter;
    RecyclerViewPetListHomeAdapter PetListAdapter;
    RecyclerViewPetTreatmentAcceptedAdapter PetTreatmentAcceptedListAdapter;
    ImageView background_clinic, background_pet, ic_clinic, ic_pet;

    TextView txt_userName_home;

    boolean is_pet_select = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.recycler_clinic_around);
        background_clinic = root.findViewById(R.id.vector_clinic);
        ic_clinic = root.findViewById(R.id.ic_clinic_around);
        background_pet = root.findViewById(R.id.vector_pet);
        ic_pet = root.findViewById(R.id.ic_pet_home);
        txt_userName_home = root.findViewById(R.id.txt_userName_home);

        txt_userName_home.setText(CurrentUser.getCurrentUser().getFullName() + ",");

        ic_clinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_pet_select){
                    is_pet_select = false;
                    SetDataClinic();
                    background_clinic.setImageResource(R.drawable.vector_vet_selected);
                    ic_clinic.setImageResource(R.drawable.vet_icon_selected);
                    background_pet.setImageResource(R.drawable.vector_pet);
                    ic_pet.setImageResource(R.drawable.ic_pet_home);
                }
            }
        });
        ic_pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_pet_select == false){
                    is_pet_select = true;
                    SetDataPet();
                    background_pet.setImageResource(R.drawable.vector_pet_selected);
                    ic_pet.setImageResource(R.drawable.ic_pet_home_selected);
                    background_clinic.setImageResource(R.drawable.vector_vet);
                    ic_clinic.setImageResource(R.drawable.vet_icon);
                }
            }
        });

        SetDataClinic();

        Geocoder geocoder = new Geocoder(getContext());

        // Inflate the layout for this fragment
        return root;
    }

    private void SetDataPet() {
        if(CurrentUser.getCurrentUser().getRole().getId() == 2){
            GetPetList();
            return;
        }
        if(CurrentUser.getCurrentUser().getRole().getId() == 3)
            GetPetTreatment();
    }

    private void GetPetList() {
        if (CurrentPetList.getPetList() == null){
            ProgressHelper.showDialog(getActivity(),"Đang lấy dữ liệu");
            CurrentPetList.CreateInstanceByAPI(new CurrentPetList.PetListCallback() {
                @Override
                public void OnSuccess(CurrentPetList currentPetList) {
                    if(ProgressHelper.isDialogVisible())
                        ProgressHelper.dismissDialog();
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            PetListAdapter = new RecyclerViewPetListHomeAdapter(CurrentPetList.getPetList(), getContext());
                            recyclerView.setAdapter(PetListAdapter);
                        }
                    });
                }
            });
        } else {
            PetListAdapter = new RecyclerViewPetListHomeAdapter(CurrentPetList.getPetList(), getContext());
            recyclerView.setAdapter(PetListAdapter);
        }
    }

    private void GetPetTreatment() {
        if(CurrentPetTreatment.getPetTreatmentAcceptedList() == null){
            Map<String,String> QuerryParams = new HashMap<>();
            QuerryParams.put("status", String.valueOf(2));
            QuerryParams.put("limit", String.valueOf(10));
            CurrentPetTreatment.CreateInstanceByAPI(QuerryParams, new CurrentPetTreatment.PetTreatmentListCallback() {
                        @Override
                        public void OnSuccess(CurrentPetTreatment currentPetTreatmentList) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    PetTreatmentAcceptedListAdapter = new RecyclerViewPetTreatmentAcceptedAdapter(getActivity());
                                    recyclerView.setAdapter(PetTreatmentAcceptedListAdapter);
                                }
                            });

                        }
                    });
        }else {
            PetTreatmentAcceptedListAdapter = new RecyclerViewPetTreatmentAcceptedAdapter(getActivity());
            recyclerView.setAdapter(PetTreatmentAcceptedListAdapter);
        }
    }

    private void SetDataClinic() {
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
                        SocketGate.OpenGate(getActivity());
                    } catch (IOException | JSONException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    Log.d("ClinicRespone", String.valueOf(statusCode));
                    try {
                        Log.d("ClinicRespone", new JSONArray(response.body().string()).toString());
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
    }
    private void SetData(ArrayList<Clinic> List) {
        Future<ArrayList<Clinic>> future = Libs.GetHomeClinic(List, new Geocoder(getActivity().getApplicationContext()));
        try {
            ArrayList<Clinic> finalList = future.get();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    clinicAdapter = new RecyclerViewClinicHomeAdapter(requireActivity(), finalList);
                    recyclerView.setAdapter(clinicAdapter);
                }
            });
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

}