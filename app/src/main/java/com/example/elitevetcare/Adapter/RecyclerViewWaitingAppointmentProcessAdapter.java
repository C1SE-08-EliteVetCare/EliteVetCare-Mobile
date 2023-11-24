package com.example.elitevetcare.Adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elitevetcare.Helper.DataChangeObserver;
import com.example.elitevetcare.Helper.HelperCallingAPI;
import com.example.elitevetcare.Helper.Libs;
import com.example.elitevetcare.Helper.ProgressHelper;
import com.example.elitevetcare.Model.Appointment;
import com.example.elitevetcare.Model.CurrentAppointment;
import com.example.elitevetcare.Model.CurrentUser;
import com.example.elitevetcare.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RecyclerViewWaitingAppointmentProcessAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_FOOTER = 0;
    private static final int VIEW_TYPE_PET_OWNER = 2;
    private static final int VIEW_TYPE_VET = 3;
    Activity Activity;

    public RecyclerViewWaitingAppointmentProcessAdapter(Activity Activity) {
        this.Activity = Activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_PET_OWNER) {
            View itemView = inflater.inflate(R.layout.item_appointment_waitting_process, parent, false);
            return new PetOwnerAppointmentViewHolder(itemView);
        } else if (viewType == VIEW_TYPE_VET) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_process, parent, false);
            return new VetAppointmentViewHolder(itemView);
        }
        return null;
    }


    @Override
    public int getItemViewType(int position) {
        int roleID = CurrentUser.getCurrentUser().getRole().getId();
        if (position == CurrentAppointment.getProcessingAppointmentList().size()) {
            return VIEW_TYPE_FOOTER; // Đây là mục cuối cùng
        }
        if (roleID == VIEW_TYPE_PET_OWNER)
            return VIEW_TYPE_PET_OWNER;
        return VIEW_TYPE_VET;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Appointment appointment = CurrentAppointment.getProcessingAppointmentList().get(position);
        String Name = "";
        String Date = "Ngày: ";
        String Time = "Giờ: ";
        if (getItemViewType(position) == VIEW_TYPE_PET_OWNER){
            PetOwnerAppointmentViewHolder newHolder = (PetOwnerAppointmentViewHolder)holder;
            Name = "Phòng Khám: ";
            Name += appointment.getClinic().getName();
            Date += appointment.getAppointmentDate();
            Time += appointment.getAppointmentTime();
            newHolder.txt_name_pet_owner.setText(Name);
            newHolder.txt_date.setText(Date);
            newHolder.txt_time.setText(Time);
            Libs.SetImageFromURL(appointment.getClinic().getLogo(),newHolder.clinicAvatar);
        }
        if (getItemViewType(position) == VIEW_TYPE_VET){
            VetAppointmentViewHolder newHolder = (VetAppointmentViewHolder)holder;
            Name = "Khách Hàng: ";
            Name += appointment.getPetOwner().getFullName();
            Date += appointment.getAppointmentDate();
            Time += appointment.getAppointmentTime();
            newHolder.txt_name_pet_owner.setText(Name);
            newHolder.txt_date.setText(Date);
            newHolder.txt_time.setText(Time);
            Libs.SetImageFromURL(appointment.getPetOwner().getAvatar(),newHolder.clinicAvatar);
            newHolder.btn_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int idAppointment = CurrentAppointment.getProcessingAppointmentList().get(newHolder.getAdapterPosition()).getId();
                    if (idAppointment == RecyclerView.NO_POSITION)
                        return;
                    if(!ProgressHelper.isDialogVisible())
                        ProgressHelper.showDialog(Activity,"Đang Cập Nhập Dữ Liệu");
                    RequestBody body = new FormBody.Builder()
                            .add("action", String.valueOf(1))
                            .build();
                    HelperCallingAPI.CallingAPI_PathParams_Patch(HelperCallingAPI.ACCEPT_REJECT_APPOINTMENT_API_PATH, String.valueOf(idAppointment), body, new HelperCallingAPI.MyCallback() {
                        @Override
                        public void onResponse(Response response) {
                            int statusCode = response.code();
                            JSONObject JsonData = null;
                            if(statusCode == 200){
                                DataChangeObserver.getInstance().getListener().onDataChanged(1);
                                DataChangeObserver.getInstance().getListener().onDataChanged(2);
                            }else {
                                try {
                                    JsonData = new JSONObject(response.body().string());
                                    Log.d("error",JsonData.toString());
                                } catch (JSONException |IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                        }

                        @Override
                        public void onFailure(IOException e) {

                        }
                    });
                }
            });
            newHolder.btn_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int idAppointment = CurrentAppointment.getProcessingAppointmentList().get(newHolder.getAdapterPosition()).getId();
                    if (idAppointment == RecyclerView.NO_POSITION)
                        return;
                    if(!ProgressHelper.isDialogVisible())
                        ProgressHelper.showDialog(Activity,"Đang Cập Nhập Dữ Liệu");
                    RequestBody body = new FormBody.Builder()
                            .add("action", String.valueOf(2))
                            .build();
                    HelperCallingAPI.CallingAPI_PathParams_Patch(HelperCallingAPI.ACCEPT_REJECT_APPOINTMENT_API_PATH, String.valueOf(idAppointment), body, new HelperCallingAPI.MyCallback() {
                        @Override
                        public void onResponse(Response response) {
                            int statusCode = response.code();
                            JSONObject JsonData = null;
                            if(statusCode == 200){
                                DataChangeObserver.getInstance().getListener().onDataChanged(1);
                                DataChangeObserver.getInstance().getListener().onDataChanged(3);
                            }else {
                                try {
                                    JsonData = new JSONObject(response.body().string());
                                    Log.d("error",JsonData.toString());
                                } catch (JSONException |IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                        }

                        @Override
                        public void onFailure(IOException e) {

                        }
                    });
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return CurrentAppointment.getProcessingAppointmentList().size();
    }

    public class PetOwnerAppointmentViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_name_pet_owner, txt_date, txt_time;
        ImageFilterButton clinicAvatar;
        AppCompatButton btn_cancel;
        public PetOwnerAppointmentViewHolder(@NonNull View itemView)
        {
            super(itemView);
            txt_name_pet_owner = itemView.findViewById(R.id.txt_name_process_appointment);
            txt_date = itemView.findViewById(R.id.txt_Date);
            txt_time = itemView.findViewById(R.id.txt_time);
            clinicAvatar = itemView.findViewById(R.id.process_appointment_avatar);
            btn_cancel = itemView.findViewById(R.id.btn_cancel);
        }


    }
    public class VetAppointmentViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_name_pet_owner, txt_date, txt_time;
        ImageFilterButton clinicAvatar;
        AppCompatButton btn_accept, btn_reject;
        public VetAppointmentViewHolder(@NonNull View itemView)
        {
            super(itemView);
            txt_name_pet_owner = itemView.findViewById(R.id.txt_name_process_appointment);
            txt_date = itemView.findViewById(R.id.txt_Date);
            txt_time = itemView.findViewById(R.id.txt_time);
            clinicAvatar = itemView.findViewById(R.id.process_appointment_avatar);
            btn_accept = itemView.findViewById(R.id.btn_accept_appointment);
            btn_reject = itemView.findViewById(R.id.btn_Reject_appointment);
        }


    }

}
