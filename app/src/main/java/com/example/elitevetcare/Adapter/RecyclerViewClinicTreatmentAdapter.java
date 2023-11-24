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
import com.example.elitevetcare.Helper.DataLocalManager;
import com.example.elitevetcare.Helper.HelperCallingAPI;
import com.example.elitevetcare.Helper.Libs;
import com.example.elitevetcare.Helper.ProgressHelper;
import com.example.elitevetcare.Model.Clinic;
import com.example.elitevetcare.Model.ViewModel.BookAppointmentViewModel;
import com.example.elitevetcare.Model.ViewModel.PetViewModel;
import com.example.elitevetcare.R;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RecyclerViewClinicTreatmentAdapter extends RecyclerView.Adapter<RecyclerViewClinicTreatmentAdapter.ClinicViewHolder>{
    ArrayList<Clinic> dataClinic = null;
    FragmentActivity requireActivity;
    PetViewModel PetViewModel;
    public RecyclerViewClinicTreatmentAdapter(FragmentActivity Activity,ArrayList<Clinic> list) {
        requireActivity = Activity;
        dataClinic = list;
        PetViewModel = new ViewModelProvider(requireActivity).get(PetViewModel.class);
    }
    public RecyclerViewClinicTreatmentAdapter(ArrayList<Clinic> list) {
        dataClinic = list;
    }

    @NonNull
    @Override
    public RecyclerViewClinicTreatmentAdapter.ClinicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_clinic, parent, false);
        return new RecyclerViewClinicTreatmentAdapter.ClinicViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewClinicTreatmentAdapter.ClinicViewHolder holder, int position) {
        Clinic clinic = dataClinic.get(position);
        holder.nameofclinic.setText(clinic.getName());
        holder.address_of_clinic.setText("Địa Chỉ: " + clinic.getStreetAddress() + ", " + clinic.getWard() + ", " + clinic.getDistrict() + ", " + clinic.getCity());
        Libs.SetImageFromURL(clinic.getLogo(), holder.clinic_avatar);
        holder.btn_select_clinic.setText("Chọn");
        holder.btn_select_clinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ClinicId = clinic.getId();
                int petId = PetViewModel.getCurrentPet().getId();
                if(!ProgressHelper.isDialogVisible())
                    ProgressHelper.showDialog(requireActivity,"Đang Tải Dữ Liệu");

                if(DataLocalManager.GetAccessTokenTimeUp() < System.currentTimeMillis())
                    HelperCallingAPI.RefreshTokenCalling();

                RequestBody body = new FormBody.Builder()
                        .add("petId", String.valueOf(petId))
                        .add("clinicId", String.valueOf(ClinicId))
                        .build();

                HelperCallingAPI.CallingAPI_Post_withHeader(HelperCallingAPI.SEND_TREATMENT_API_PATH,
                        body, DataLocalManager.GetAccessToken(), new HelperCallingAPI.MyCallback() {
                    @Override
                    public void onResponse(Response response) {
                        int statusCode = response.code();

                        if(statusCode == 200){
                            requireActivity.runOnUiThread(() -> ((ContentView)requireActivity).setfragment(R.layout.fragment_tracking_pet_health));
                        }else{
                            if(ProgressHelper.isDialogVisible())
                                ProgressHelper.dismissDialog();
                            Libs.Sendmessage(requireActivity, "Xảy ra lỗi vui lòng thử lại !");
                        }
                    }
                    @Override
                    public void onFailure(IOException e) {

                    }
                });
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
