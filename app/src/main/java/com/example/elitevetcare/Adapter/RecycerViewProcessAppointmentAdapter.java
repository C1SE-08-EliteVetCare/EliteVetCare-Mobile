package com.example.elitevetcare.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elitevetcare.Model.CurrentAppointment;
import com.example.elitevetcare.R;

public class RecycerViewProcessAppointmentAdapter extends RecyclerView.Adapter<RecycerViewProcessAppointmentAdapter.ProcessAppointmentViewHolder>{
    public ProcessAppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_process, parent, false);
        return new ProcessAppointmentViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(@NonNull ProcessAppointmentViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return CurrentAppointment.getProcessingAppointmentList().size();
    }

    public class ProcessAppointmentViewHolder extends RecyclerView.ViewHolder {

        public ProcessAppointmentViewHolder(@NonNull View itemView)
        {
            super(itemView);
        }


    }
}
