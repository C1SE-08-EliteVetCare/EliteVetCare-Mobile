package com.example.elitevetcare.Pets;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.elitevetcare.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_process_pet_treatment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_process_pet_treatment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_process_pet_treatment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_process_pet_treatment.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_process_pet_treatment newInstance(String param1, String param2) {
        fragment_process_pet_treatment fragment = new fragment_process_pet_treatment();
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
    RecyclerView Recycler_list_item_pet_treatment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_process_pet_treatment, container, false);
        Recycler_list_item_pet_treatment = root.findViewById(R.id.Recycler_list_item_pet_treatment);

        // Inflate the layout for this fragment
        return root;
    }
}