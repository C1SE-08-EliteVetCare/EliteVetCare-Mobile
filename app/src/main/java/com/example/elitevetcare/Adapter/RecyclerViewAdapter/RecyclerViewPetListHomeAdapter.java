package com.example.elitevetcare.Adapter.RecyclerViewAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elitevetcare.Activity.ContentView;
import com.example.elitevetcare.Activity.UpdateProfile;
import com.example.elitevetcare.Helper.Libs;
import com.example.elitevetcare.Model.ObjectModel.Pet;
import com.example.elitevetcare.R;

import java.util.ArrayList;

public class RecyclerViewPetListHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<Pet> petList;
    Context mContext;
    Activity activity;
    public RecyclerViewPetListHomeAdapter() {
        this.petList = null;
    }
    public RecyclerViewPetListHomeAdapter(ArrayList<Pet> petList) {
        this.petList = petList;
    }
    public RecyclerViewPetListHomeAdapter(ArrayList<Pet> petList, Activity activity) {
        this.petList = petList;
        this.activity = activity;
        this.mContext = activity.getApplicationContext();
    }
    public RecyclerViewPetListHomeAdapter(ArrayList<Pet> petList, Context applicationContext) {
        this.petList = petList;
        this.mContext = applicationContext;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_processing, parent, false);
        return new RecyclerViewPetListHomeAdapter.PetListViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Pet currentPetPosition = petList.get(position);
        RecyclerViewPetListHomeAdapter.PetListViewHolder currentHolder = (RecyclerViewPetListHomeAdapter.PetListViewHolder) holder;
        currentHolder.txt_Species_And_Breed.setText("Loài: " + currentPetPosition.getSpecies() + " | Giống: " + currentPetPosition.getBreed());
        currentHolder.txt_Name_of_Pet.setText(currentPetPosition.getName());
        Libs.SetImageFromURL(currentPetPosition,currentHolder.image_avatar);
        currentHolder.ll_pet_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ContentView.class);
                intent.putExtra("FragmentCalling",R.layout.fragment_pet_infor_detail);
                intent.putExtra("Pets", currentPetPosition);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return 5;
    }

    public class PetListViewHolder extends RecyclerView.ViewHolder{
        public ImageFilterView image_avatar;
        public TextView txt_Name_of_Pet, txt_Species_And_Breed;
        LinearLayout ll_pet_profile;
        public PetListViewHolder(@NonNull View itemView) {
            super(itemView);
            image_avatar = itemView.findViewById(R.id.avatar_pet_pet_list);
            txt_Name_of_Pet = itemView.findViewById(R.id.txt_name_of_pet);
            txt_Species_And_Breed = itemView.findViewById(R.id.txt_Species_and_Breed_pet);
            ll_pet_profile = itemView.findViewById(R.id.ll_pet_profile);
        }
    }
}
