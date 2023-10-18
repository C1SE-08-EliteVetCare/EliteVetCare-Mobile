package com.example.elitevetcare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewClinicAdapter extends RecyclerView.Adapter<RecyclerViewClinicAdapter.ClinicViewHolder> {
    @NonNull
    @Override
    public ClinicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_clinic, parent, false);
        return new ClinicViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewClinicAdapter.ClinicViewHolder holder, int position) {
        holder.nameofclinic.setText("asdasdas");
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ClinicViewHolder extends RecyclerView.ViewHolder {
        TextView nameofclinic;
        public ClinicViewHolder(@NonNull View itemView) {
            super(itemView);
            nameofclinic =itemView.findViewById(R.id.name_of_clinic);
        }
    }
}
