package com.example.elitevetcare.Adapter.RecyclerViewAdapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elitevetcare.Activity.ContentView;
import com.example.elitevetcare.Helper.Libs;
import com.example.elitevetcare.Model.ObjectModel.Appointment;
import com.example.elitevetcare.Model.CurrentData.CurrentAppointment;
import com.example.elitevetcare.Model.CurrentData.CurrentUser;
import com.example.elitevetcare.R;

public class RecyclerViewRejectAppointmentAdapter extends RecyclerView.Adapter<RecyclerViewRejectAppointmentAdapter.RejectAppointmentViewHolder>{

    Activity activity;

    public RecyclerViewRejectAppointmentAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public RejectAppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reject, parent, false);
        return new RecyclerViewRejectAppointmentAdapter.RejectAppointmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RejectAppointmentViewHolder holder, int position) {
        int roleID = CurrentUser.getCurrentUser().getRole().getId();
        Appointment appointment = CurrentAppointment.getRejectAppointmentList().get(position);
        String Name = "";
        if (roleID == 2) {
            Name = "Phòng khám: ";
            Name += appointment.getClinic().getName();
            Libs.SetImageFromURL(appointment.getClinic().getLogo(),holder.item_reject_Avatar);
        }
        else{
            Name = "Khách Hàng: ";
            Name += appointment.getPetOwner().getFullName();
            Libs.SetImageFromURL(appointment.getPetOwner().getAvatar(),holder.item_reject_Avatar);
        }

        String Date = "Ngày: ";
        String Time = "Giờ: ";

        Date += appointment.getAppointmentDate();
        Time += appointment.getAppointmentTime();
        holder.txt_name_pet_owner.setText(Name);
        holder.txt_date.setText(Date);
        holder.txt_time.setText(Time);
        holder.ll_item_process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getApplicationContext(), ContentView.class);
                intent.putExtra("FragmentCalling", R.layout.fragment_detail_appointment);
                intent.putExtra("Appointment", appointment);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return CurrentAppointment.getRejectAppointmentList().size();
    }

    public class RejectAppointmentViewHolder extends RecyclerView.ViewHolder{
        public TextView txt_name_pet_owner, txt_date, txt_time;
        ImageFilterButton item_reject_Avatar;
        LinearLayout ll_item_process;
        public RejectAppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name_pet_owner = itemView.findViewById(R.id.txt_item_reject_name);
            txt_date = itemView.findViewById(R.id.txt_Date);
            txt_time = itemView.findViewById(R.id.txt_time);
            item_reject_Avatar = itemView.findViewById(R.id.Avatar_item_reject);
            ll_item_process = itemView.findViewById(R.id.ll_item_reject);
        }
    }
}
