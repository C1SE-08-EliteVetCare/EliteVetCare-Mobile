package com.example.elitevetcare.Profile;

import android.os.Bundle;

import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.elitevetcare.Activity.Login;
import com.example.elitevetcare.Authentication.fragment_login;
import com.example.elitevetcare.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_user_profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_user_profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageFilterButton  imgbtn_updateprofile;
    public fragment_user_profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_user_profile.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_user_profile newInstance(String param1, String param2) {
        fragment_user_profile fragment = new fragment_user_profile();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_user_profile, container, false);

        imgbtn_updateprofile = root.findViewById(R.id.btn_edit_profile);

        imgbtn_updateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Login) getActivity()).changeFragment(new fragment_edit_profile_user());
            }
        });


        return root;
    }
}