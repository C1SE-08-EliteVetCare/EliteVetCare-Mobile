package com.example.elitevetcare.Adapter.RecyclerViewAdapter;

import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elitevetcare.Helper.Libs;
import com.example.elitevetcare.Model.CurrentData.CurrentLocation;
import com.example.elitevetcare.Model.ObjectModel.Clinic;
import com.example.elitevetcare.Model.ViewModel.BookAppointmentViewModel;
import com.example.elitevetcare.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

public class RecyclerViewClinicHomeAdapter extends RecyclerView.Adapter<RecyclerViewClinicHomeAdapter.ClinicViewHolder> {
    ArrayList<Clinic> dataClinic = null;
    FragmentActivity requireActivity;
    BookAppointmentViewModel bookAppointmentViewModel;
    Geocoder geocoder;
    public RecyclerViewClinicHomeAdapter(FragmentActivity Activity,ArrayList<Clinic> list) {
        requireActivity = Activity;
        dataClinic = list;
        geocoder = new Geocoder(Activity.getApplicationContext());
    }
    @NonNull
    @Override
    public ClinicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_clinic_around, parent, false);
        return new RecyclerViewClinicHomeAdapter.ClinicViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ClinicViewHolder holder, int position) {
        Clinic clinic = dataClinic.get(position);
        holder.txt_name.setText(clinic.getName());
        holder.txt_address.setText("Địa Chỉ: " + clinic.getStreetAddress() );
        holder.txt_distance.setText(String.format("%.1f", clinic.getDistance()/1000));
        Libs.SetImageFromURL(clinic.getLogo(),holder.img_clinic);
        Log.d("Location", position +": " + clinic.getDistance());
    }

    @Override
    public int getItemCount() {
        return dataClinic.size();
    }

    public class ClinicViewHolder extends RecyclerView.ViewHolder {
        ImageFilterView img_clinic;
        TextView txt_name, txt_address, txt_like, txt_distance;
        public ClinicViewHolder(@NonNull View itemView) {
            super(itemView);
            img_clinic = itemView.findViewById(R.id.img_avatar_clinic);
            txt_name = itemView.findViewById(R.id.name_of_clinic);
            txt_address = itemView.findViewById(R.id.address_of_clinic);
            txt_distance = itemView.findViewById(R.id.txt_distance);

        }
    }
}
