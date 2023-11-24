package com.example.elitevetcare.Pets;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.elitevetcare.Adapter.ViewPageAdapter.AppointmentViewPager2Adapter;
import com.example.elitevetcare.Adapter.ViewPageAdapter.PetTreatmentViewPager2Adapter;
import com.example.elitevetcare.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_pet_list_treatment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_pet_list_treatment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_pet_list_treatment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_pet_list_treatment.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_pet_list_treatment newInstance(String param1, String param2) {
        fragment_pet_list_treatment fragment = new fragment_pet_list_treatment();
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
    private TabLayout treatment_tabLayout;
    private ViewPager2 treatment_viewPager2;
    private PetTreatmentViewPager2Adapter PetTreatment_ViewPager2Adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pet_list_treatment, container, false);
        treatment_tabLayout = root.findViewById(R.id.pet_list_treatment_tabLayout);
        treatment_viewPager2 = root.findViewById(R.id.pet_treatment_viewpage2);
        PetTreatment_ViewPager2Adapter = new PetTreatmentViewPager2Adapter(getActivity());
        treatment_viewPager2.setAdapter(PetTreatment_ViewPager2Adapter);
        new TabLayoutMediator(treatment_tabLayout, treatment_viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Sẳn Sàng");
                        break;
                    case 1:
                        tab.setText("Đã Nhận");
                        break;
                }
            }
        }).attach();
        // Inflate the layout for this fragment
        return root;
    }
}