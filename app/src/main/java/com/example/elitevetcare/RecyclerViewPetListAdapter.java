package com.example.elitevetcare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewPetListAdapter extends RecyclerView.Adapter<RecyclerViewPetListAdapter.PetViewHolder>
{
    @NonNull
    @Override
    public RecyclerViewPetListAdapter.PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_processing, parent, false);
        return new RecyclerViewPetListAdapter.PetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewPetListAdapter.PetViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class PetViewHolder extends RecyclerView.ViewHolder{

        public PetViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
