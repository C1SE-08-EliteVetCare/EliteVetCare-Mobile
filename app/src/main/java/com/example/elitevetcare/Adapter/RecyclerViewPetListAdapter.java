package com.example.elitevetcare.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elitevetcare.Activity.ContentView;
import com.example.elitevetcare.Activity.UpdateProfile;
import com.example.elitevetcare.Helper.Libs;
import com.example.elitevetcare.Model.Pet;
import com.example.elitevetcare.R;
import java.util.ArrayList;

public class RecyclerViewPetListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private static final int VIEW_TYPE_FOOTER = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private ArrayList<Pet> petList;
    Context mContext;
    Activity activity;
    public RecyclerViewPetListAdapter() {
        this.petList = null;
    }
    public RecyclerViewPetListAdapter(ArrayList<Pet> petList) {
        this.petList = petList;
    }
    public RecyclerViewPetListAdapter(ArrayList<Pet> petList, Activity activity) {
        this.petList = petList;
        this.activity = activity;
    }
    public RecyclerViewPetListAdapter(ArrayList<Pet> petList, Context applicationContext) {
        this.petList = petList;
        this.mContext = applicationContext;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_NORMAL) {
            View itemView = inflater.inflate(R.layout.item_processing, parent, false);
            return new PetListViewHolder(itemView);
        } else {
            View itemView = inflater.inflate(R.layout.item_addnew, parent, false);
            return new LastItemViewHolder(itemView);
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (position == petList.size()) {
            return VIEW_TYPE_FOOTER; // Đây là mục cuối cùng
        } else {
            return VIEW_TYPE_NORMAL; // Các mục khác ở đầu
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == VIEW_TYPE_FOOTER){
            ((LastItemViewHolder)holder).btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, UpdateProfile.class);
                    intent.putExtra("FragmentCalling",R.layout.fragment_update_pet_infor);
                    context.startActivity(intent);
                }
            });
        }
        if(getItemViewType(position) == VIEW_TYPE_NORMAL) {
            Pet currentPetPosition = petList.get(position);
            PetListViewHolder currentHolder = (PetListViewHolder) holder;
            currentHolder.txt_Species_And_Breed.setText("Loài: " + currentPetPosition.getSpecies() + " | Giống: " + currentPetPosition.getBreed());
            currentHolder.txt_Name_of_Pet.setText(currentPetPosition.getName());
            Libs.SetImageFromURL(currentPetPosition,currentHolder);

            currentHolder.image_avatar.setOnClickListener(new View.OnClickListener() {
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
    }


    @Override
    public int getItemCount() {
        return petList.size()+1;
    }

    public class PetListViewHolder extends RecyclerView.ViewHolder{
        public ImageFilterView image_avatar;
        public TextView txt_Name_of_Pet, txt_Species_And_Breed;
        public PetListViewHolder(@NonNull View itemView) {
            super(itemView);
            image_avatar = itemView.findViewById(R.id.avatar_pet_pet_list);
            txt_Name_of_Pet = itemView.findViewById(R.id.txt_name_of_pet);
            txt_Species_And_Breed = itemView.findViewById(R.id.txt_Species_and_Breed_pet);
        }
    }
    public class LastItemViewHolder extends RecyclerView.ViewHolder{
        public ImageFilterButton btn_add;
        public LastItemViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_add = itemView.findViewById(R.id.btn_click_new_profile);
        }
    }

}
