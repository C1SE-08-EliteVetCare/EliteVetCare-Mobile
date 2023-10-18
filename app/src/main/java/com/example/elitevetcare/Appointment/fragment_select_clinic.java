package com.example.elitevetcare.Appointment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.elitevetcare.R;
import com.example.elitevetcare.RecyclerViewClinicAdapter;

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
        clinicAdapter = new RecyclerViewClinicAdapter();
        recycler_clinicView.setAdapter(clinicAdapter);
        // Inflate the layout for this fragment
        return root;
    }
}