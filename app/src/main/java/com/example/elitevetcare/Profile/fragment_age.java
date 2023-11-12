package com.example.elitevetcare.Profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.elitevetcare.Activity.UpdateProfile;
import com.example.elitevetcare.Model.CurrentUser;
import com.example.elitevetcare.Helper.ViewModel.PetInforViewModel;
import com.example.elitevetcare.R;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_age#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_age extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private  NumberPicker npicker_age;

    public fragment_age() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_age.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_age newInstance(String param1, String param2) {
        fragment_age fragment = new fragment_age();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Doan Can Them Code*/
    PetInforViewModel petInforViewModel;
    Bundle args;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        args = getArguments();
        petInforViewModel = new ViewModelProvider(requireActivity()).get(PetInforViewModel .class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_age, container, false);
        npicker_age = ((NumberPicker)root.findViewById(R.id.number_picker_age));
        npicker_age.setMinValue(1);
        npicker_age.setMaxValue(99);
        npicker_age.setValue(15);

        // Inflate the layout for this fragment
        return root ;
    }

    public boolean SendData() {
        if( ((UpdateProfile)getActivity()).GetPreviousFragment() instanceof fragment_gender )
                CurrentUser.getCurrentUser().setBirthYear(String.valueOf( Calendar.getInstance().get(Calendar.YEAR) - npicker_age.getValue() ));
        else if (((UpdateProfile)getActivity()).GetPreviousFragment() instanceof fragment_gender_pets )
            petInforViewModel.setAge(npicker_age.getValue());
        Log.d("userage", String.valueOf(Calendar.getInstance().get(Calendar.YEAR) - npicker_age.getValue()));
        return true;
    }
}