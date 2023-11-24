package com.example.elitevetcare.Pets;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.elitevetcare.Activity.ContentView;
import com.example.elitevetcare.Helper.Libs;
import com.example.elitevetcare.Model.ViewModel.PetViewModel;
import com.example.elitevetcare.Model.Pet;
import com.example.elitevetcare.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_pet_infor_detail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_pet_infor_detail extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_pet_infor_detail() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_pet_infor_detail.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_pet_infor_detail newInstance(String param1, String param2) {
        fragment_pet_infor_detail fragment = new fragment_pet_infor_detail();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    PetViewModel CurrentPetViewModel;
    Bundle args;
    Pet CurrentPet;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        args = getArguments();
        CurrentPetViewModel = new ViewModelProvider(requireActivity()).get(PetViewModel .class);
        CurrentPet = CurrentPetViewModel.getCurrentPet();
    }
    TextView txt_PetName;
    ImageFilterView img_avatar;
    AppCompatButton btn_next;
    AppCompatEditText edt_Age,edt_Gender, edt_species, edt_breed, edt_weight, edt_furColor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pet_infor_detail, container, false);
        SetID(root);

        txt_PetName.setText(CurrentPet.getName());
        edt_Age.setText(CurrentPet.getAge()+"");
        edt_Gender.setText(CurrentPet.getGender());
        edt_species.setText(CurrentPet.getSpecies());
        edt_breed.setText(CurrentPet.getBreed());
        edt_weight.setText(CurrentPet.getWeight()+ " KG");
        edt_furColor.setText(CurrentPet.getFurColor());

        Libs.SetImageFromURL(CurrentPet,img_avatar);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CurrentPet.getClinic() == null || CurrentPet.getClinic().getName() == null){
                    ((ContentView)getActivity()).setfragment(R.layout.fragment_select_clinic_treatment);
                    return;
                }
                ((ContentView)getActivity()).setfragment(R.layout.fragment_tracking_pet_health);
            }
        });
        // Inflate the layout for this fragment
        return root;
    }

    private void SetID(View root) {
        txt_PetName = root.findViewById(R.id.txt_pet_name_pet_detail);
        edt_Age = root.findViewById(R.id.edt_age_pet_detail);
        edt_Gender = root.findViewById(R.id.edt_gender_pet_detail);
        edt_species = root.findViewById(R.id.edt_species_pet_detail);
        edt_breed = root.findViewById(R.id.edt_breed_pet_detail);
        edt_weight = root.findViewById(R.id.edt_weight_pet_detail);
        edt_furColor = root.findViewById(R.id.edt_furColor_pet_detail);
        img_avatar = root.findViewById(R.id.img_detail_avatar_pet_profile);
        btn_next = root.findViewById(R.id.btn_nextAction);
    }
}