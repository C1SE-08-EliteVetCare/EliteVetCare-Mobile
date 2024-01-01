package com.example.elitevetcare.Adapter.RecyclerViewAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elitevetcare.R;

import java.util.zip.Inflater;

public class RecyclerViewVetAdapter extends RecyclerView.Adapter<RecyclerViewVetAdapter.VetViewHolder>{
    @NonNull
    @Override
    public VetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_vet,parent, false);
        return new VetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VetViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class VetViewHolder extends RecyclerView.ViewHolder {
        public VetViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
