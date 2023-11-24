package com.example.elitevetcare.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elitevetcare.Activity.ContentView;
import com.example.elitevetcare.Helper.Libs;
import com.example.elitevetcare.Model.Clinic;
import com.example.elitevetcare.Model.ViewModel.BookAppointmentViewModel;
import com.example.elitevetcare.R;

import java.util.ArrayList;

public class RecyclerViewClinicAdapter extends RecyclerView.Adapter<RecyclerViewClinicAdapter.ClinicViewHolder> {
    ArrayList<Clinic> dataClinic = null;
    FragmentActivity requireActivity;
    BookAppointmentViewModel bookAppointmentViewModel;
    public RecyclerViewClinicAdapter(FragmentActivity Activity,ArrayList<Clinic> list) {
        requireActivity = Activity;
        dataClinic = list;
        bookAppointmentViewModel = new ViewModelProvider(requireActivity).get(BookAppointmentViewModel.class);
    }
    public RecyclerViewClinicAdapter(ArrayList<Clinic> list) {
        dataClinic = list;
    }

    @NonNull
    @Override
    public ClinicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_clinic, parent, false);
        return new ClinicViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewClinicAdapter.ClinicViewHolder holder, int position) {
        Clinic clinic = dataClinic.get(position);
        holder.nameofclinic.setText(clinic.getName());
        holder.address_of_clinic.setText("Địa Chỉ: " + clinic.getStreetAddress() + ", " + clinic.getWard() + ", " + clinic.getDistrict() + ", " + clinic.getCity());
        Libs.SetImageFromURL(clinic.getLogo(), holder.clinic_avatar);
        holder.btn_select_clinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookAppointmentViewModel.setClinicId(clinic.getId());
                ((ContentView)requireActivity).setfragment(R.layout.fragment_select_datetime);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataClinic.size();
    }

    public class ClinicViewHolder extends RecyclerView.ViewHolder {
        TextView nameofclinic, address_of_clinic;
        ImageFilterView clinic_avatar;
        AppCompatButton btn_select_clinic;
        ImageFilterButton btn_like_clinic;
        TextView number_of_like;
        public ClinicViewHolder(@NonNull View itemView) {
            super(itemView);
            nameofclinic = itemView.findViewById(R.id.name_of_clinic);
            address_of_clinic = itemView.findViewById(R.id.address_of_clinic);
            clinic_avatar = itemView.findViewById(R.id.clinic_avatar);
            btn_select_clinic = itemView.findViewById(R.id.btn_select_clinic);
            btn_like_clinic = itemView.findViewById(R.id.btn_like_clinic);
            number_of_like = itemView.findViewById(R.id.number_of_like);
        }
    }
}
