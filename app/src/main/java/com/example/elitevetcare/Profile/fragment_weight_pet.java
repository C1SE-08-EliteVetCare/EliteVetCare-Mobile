package com.example.elitevetcare.Profile;

import android.os.Bundle;

import androidx.compose.ui.text.font.Typeface;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.elitevetcare.R;
import com.kevalpatel2106.rulerpicker.RulerValuePicker;
import com.kevalpatel2106.rulerpicker.RulerValuePickerListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_weight_pet#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_weight_pet extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_weight_pet() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_weight_pet.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_weight_pet newInstance(String param1, String param2) {
        fragment_weight_pet fragment = new fragment_weight_pet();
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
    RulerValuePicker rulerValuePicker;
    TextView selectedValueText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_weight_pet, container, false);
        rulerValuePicker = root.findViewById(R.id.ruler_picker_weight_pets);
        selectedValueText = root.findViewById(R.id.edt_selectedValueText);
        rulerValuePicker.selectValue(45);

        rulerValuePicker.setValuePickerListener(new RulerValuePickerListener() {
            @Override
            public void onValueChange(final int selectedValue) {
                 int true_value = 100 - selectedValue;
                 double true_weight = (true_value * 100.0) /1000;
                selectedValueText.setText(String.valueOf(true_weight).replace('.',','));
            }

            @Override
            public void onIntermediateValueChange(final int selectedValue) {
                //Value changed but the user is still scrolling the ruler.
                //This value is not final value. Application can utilize this value to display the current selected value.
            }
        });
        // Inflate the layout for this fragment
        return root;
    }
}