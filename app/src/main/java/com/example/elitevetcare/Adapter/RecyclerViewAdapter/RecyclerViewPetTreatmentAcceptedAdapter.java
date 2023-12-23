package com.example.elitevetcare.Adapter.RecyclerViewAdapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elitevetcare.Helper.Libs;
import com.example.elitevetcare.Model.CurrentData.CurrentPetTreatment;
import com.example.elitevetcare.Model.ObjectModel.PetTreatment;
import com.example.elitevetcare.R;

public class RecyclerViewPetTreatmentAcceptedAdapter extends RecyclerView.Adapter<RecyclerViewPetTreatmentAcceptedAdapter.PetTreatmentViewHolder>{

    Activity Activity;

    public RecyclerViewPetTreatmentAcceptedAdapter(Activity Activity) {
        this.Activity = Activity;
    }
    @NonNull
    @Override
    public PetTreatmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_processing, parent, false);
        return new RecyclerViewPetTreatmentAcceptedAdapter.PetTreatmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PetTreatmentViewHolder holder, int position) {
        PetTreatment petTreatment = CurrentPetTreatment.getPetTreatmentAcceptedList().get(position);
        String Name = "Chủ Sở Hữu: " + petTreatment.getPet().getOwner().getFullName();
        String Species = "Loài: " + petTreatment.getPet().getSpecies();
        String Breed = "Giống: " + petTreatment.getPet().getBreed();
        String URL_img = petTreatment.getPet().getAvatar();
        holder.txt_Name_of_Pet.setText(Name);

        holder.txt_Species_And_Breed.setText(Species + " | " + Breed);
        Libs.SetImageFromURL(URL_img,holder.image_avatar);


    }

    @Override
    public int getItemCount() {
        return CurrentPetTreatment.getPetTreatmentAcceptedList().size();
    }

    public class PetTreatmentViewHolder extends RecyclerView.ViewHolder{
        public ImageFilterView image_avatar;
        public TextView txt_Name_of_Pet, txt_Species_And_Breed;
        public LinearLayout ll_profile;
        public PetTreatmentViewHolder(@NonNull View itemView) {
            super(itemView);
            image_avatar = itemView.findViewById(R.id.avatar_pet_pet_list);
            txt_Name_of_Pet = itemView.findViewById(R.id.txt_name_of_pet);
            txt_Species_And_Breed = itemView.findViewById(R.id.txt_Species_and_Breed_pet);
            ll_profile = itemView.findViewById(R.id.ll_pet_profile);
        }
    }
}
