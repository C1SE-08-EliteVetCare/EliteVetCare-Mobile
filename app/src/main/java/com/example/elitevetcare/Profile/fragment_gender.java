package com.example.elitevetcare.Profile;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.elitevetcare.Model.CurrentUser;
import com.example.elitevetcare.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_gender#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_gender extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_gender() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_gender.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_gender newInstance(String param1, String param2) {
        fragment_gender fragment = new fragment_gender();
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
    AppCompatButton btn_male, btn_female, btn_other;
    String GenderString = "";
    int GenderSelected = 3;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gender, container, false);
        SetID(root);
        SetEvent();
        ChangeContent();
        // Inflate the layout for this fragment
        return root;
    }

    private void SetID(View root) {
        btn_male = root.findViewById(R.id.btn_male_gender);
        btn_female = root.findViewById(R.id.btn_female_gender);
        btn_other = root.findViewById(R.id.btn_other_gender);
    }

    private void SetEvent() {
        btn_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GenderSelected = 1;
                ChangeContent();
            }
        });
        btn_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GenderSelected = 2;
                ChangeContent();
            }
        });
        btn_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GenderSelected = 3;
                ChangeContent();
            }
        });
    }

    private void ChangeContent() {
        if(GenderSelected == 1){
            btn_male.setBackgroundResource(R.drawable.custom_button_male_selected);
            btn_female.setBackgroundResource(R.drawable.custom_button_female);
            btn_other.setBackgroundResource(R.drawable.custom_button_othergender);
            GenderString = "Nam";
        }else if(GenderSelected == 2) {
            btn_female.setBackgroundResource(R.drawable.custom_button_female_selected);
            btn_male.setBackgroundResource(R.drawable.custom_button_male);
            btn_other.setBackgroundResource(R.drawable.custom_button_othergender);
            GenderString = "Nữ";
        }else if(GenderSelected == 3) {
            btn_female.setBackgroundResource(R.drawable.custom_button_female);
            btn_male.setBackgroundResource(R.drawable.custom_button_male);
            btn_other.setBackgroundResource(R.drawable.custom_button_othergender_selected);
            GenderString = "Khác";
        }
    }

    public boolean SendData() {
        CurrentUser.getCurrentUser().setGender(GenderString);
        return true;
    }
}