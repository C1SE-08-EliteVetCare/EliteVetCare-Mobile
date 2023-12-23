package com.example.elitevetcare.Pets;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.elitevetcare.Helper.ProgressHelper;
import com.example.elitevetcare.Model.CurrentData.CurrentPetList;
import com.example.elitevetcare.Model.ObjectModel.Pet;
import com.example.elitevetcare.R;
import com.example.elitevetcare.Adapter.RecyclerViewAdapter.RecyclerViewPetListAdapter;
import java.util.ArrayList;


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
    Activity MainActivity;
    public fragment_list_pet() {
        this.MainActivity = null;
    }
    public fragment_list_pet(Activity MainActivity) {
        this.MainActivity = MainActivity;
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
                            PetList = CurrentPetList.getPetList();
                            PetListAdapter = new RecyclerViewPetListAdapter(PetList, getContext());
                            recycler_clinicView.setAdapter(PetListAdapter);
                        }
                    });
                }
            });
        } else {
            PetList = CurrentPetList.getPetList();
            PetListAdapter = new RecyclerViewPetListAdapter(PetList, getContext());
            recycler_clinicView.setAdapter(PetListAdapter);
        }
    }
    public void RefreshData(){

    }
}