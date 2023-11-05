package com.example.elitevetcare.Pets;

import android.os.Bundle;

import androidx.core.app.CoreComponentFactory;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.elitevetcare.Helper.HelperCallingAPI;
import com.example.elitevetcare.Model.Pet;
import com.example.elitevetcare.R;
import com.example.elitevetcare.RecyclerView.RecyclerViewPetListAdapter;
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
 * Use the {@link fragment_list_pet#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_list_pet extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_list_pet() {
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
    public static fragment_list_pet newInstance(String param1, String param2) {
        fragment_list_pet fragment = new fragment_list_pet();
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
    RecyclerViewPetListAdapter PetListAdapter;
    ArrayList<Pet> PetList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_pet, container, false);
        recycler_clinicView = root.findViewById(R.id.Recycler_list_item_pet_profile);
        GetDataByAPI();
        // Inflate the layout for this fragment
        return root;
    }

    private void GetDataByAPI() {
        Thread API_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HelperCallingAPI.CallingAPI_Get(HelperCallingAPI.GET_PET_LIST_PATH, new HelperCallingAPI.MyCallback() {
                    @Override
                    public void onResponse(Response response) {
                        int statusCode = response.code();
                        JSONObject data = null;
                        if(statusCode == 200) {
                            try {
                                data = new JSONObject(response.body().string());
                                JSONArray ListPetData = data.getJSONArray("data");
                                Gson gson = new Gson();
                                Type listType = new TypeToken<ArrayList<Pet>>(){}.getType();
                                PetList = gson.fromJson(ListPetData.toString(), listType);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        PetListAdapter = new RecyclerViewPetListAdapter(PetList, getActivity());
//                                        PetListAdapter = new RecyclerViewPetListAdapter(PetList );
                                        recycler_clinicView.setAdapter(PetListAdapter);
                                    }
                                });
                            } catch (IOException | JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }

                    }
                    @Override
                    public void onFailure(IOException e) {

                    }
                });
            }
        });
        API_thread.start();
        try {
            API_thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}