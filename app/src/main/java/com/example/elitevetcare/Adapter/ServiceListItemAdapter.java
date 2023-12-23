package com.example.elitevetcare.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elitevetcare.Model.ObjectModel.ServicePackage;
import com.example.elitevetcare.R;

import java.util.ArrayList;

public class ServiceListItemAdapter extends  RecyclerView.Adapter<ServiceListItemAdapter.ServiceItemViewHolder> {
    private Context mContext;
    private ArrayList<ServicePackage> mData;
    private int selectedItem = -1;

    public ServiceListItemAdapter(Context context, ArrayList<ServicePackage> list ){
        this.mData = list;
        mContext = context;
        Log.d("ServicePackage", String.valueOf(mData.size()));
    }
    @NonNull
    @Override
    public ServiceListItemAdapter.ServiceItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new ServiceListItemAdapter.ServiceItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceListItemAdapter.ServiceItemViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    public Integer getSelectedItemPosition() {
        return selectedItem != -1 ? selectedItem : null;
    }

    public ServicePackage getSelectedItem() {
        return selectedItem != -1 ? mData.get(selectedItem) : null;
    }
    class ServiceItemViewHolder extends RecyclerView.ViewHolder {
        ImageView flagView;
        TextView txt_nameService;
        TextView txt_Description;
        RadioButton radioButton;

        public ServiceItemViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radio_button_service);
            txt_nameService = itemView.findViewById(R.id.txt_name_service);
            txt_Description = itemView.findViewById(R.id.txt_service_description);
            radioButton.setOnClickListener(v -> {
                selectedItem = getAdapterPosition();
                notifyDataSetChanged();
            });
        }

        public void bind(int position) {
            ServicePackage servicePackage = mData.get(position);
            radioButton.setChecked(position == selectedItem);
            txt_Description.setText(servicePackage.getDescription());
            txt_nameService.setText(servicePackage.getName());
        }
    }
}
