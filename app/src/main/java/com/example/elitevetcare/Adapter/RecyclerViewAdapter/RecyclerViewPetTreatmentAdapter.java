package com.example.elitevetcare.Adapter.RecyclerViewAdapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elitevetcare.Activity.ContentView;
import com.example.elitevetcare.Helper.DataChangeObserver;
import com.example.elitevetcare.Helper.HelperCallingAPI;
import com.example.elitevetcare.Helper.Libs;
import com.example.elitevetcare.Helper.ProgressHelper;
import com.example.elitevetcare.Model.CurrentData.CurrentPetTreatment;
import com.example.elitevetcare.Model.ObjectModel.Pet;
import com.example.elitevetcare.Model.ObjectModel.PetTreatment;
import com.example.elitevetcare.R;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RecyclerViewPetTreatmentAdapter extends RecyclerView.Adapter<RecyclerViewPetTreatmentAdapter.PetTreatmentViewHolder> {
    Activity Activity;

    public RecyclerViewPetTreatmentAdapter(Activity Activity) {
        this.Activity = Activity;
    }
    @NonNull
    @Override
    public PetTreatmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_process, parent, false);
        return new RecyclerViewPetTreatmentAdapter.PetTreatmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PetTreatmentViewHolder holder, int position) {
        PetTreatment petTreatment = CurrentPetTreatment.getPetTreatmentReadyList().get(position);
        String Name = "Chủ Sở Hữu: " + petTreatment.getPet().getOwner().getFullName();
        String Species = "Loài: " + petTreatment.getPet().getSpecies();
        String Breed = "Giống: " + petTreatment.getPet().getBreed();
        String URL_img = petTreatment.getPet().getAvatar();
        holder.txt_name_pet_owner.setText(Name);
        holder.txt_Species.setText(Species);
        holder.txt_Breed.setText(Breed);
        Libs.SetImageFromURL(URL_img,holder.petAvatar);

        holder.ll_item_process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity.getApplicationContext(), ContentView.class);
                intent.putExtra("PetTreatment", petTreatment);
                intent.putExtra("FragmentCalling", R.layout.fragment_pet_treatment_detail);
                Activity.startActivity(intent);
            }
        });
        holder.btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int TreatmentID = CurrentPetTreatment.getPetTreatmentReadyList().get(holder.getAdapterPosition()).getId();
                if(!ProgressHelper.isDialogVisible())
                    ProgressHelper.showDialog(Activity,"Đang Cập Nhập Dữ Liệu");
                RequestBody body = new FormBody.Builder()
                        .add("treatmentId", TreatmentID+"")
                        .build();
                HelperCallingAPI.CallingAPI_QueryParams_Post(HelperCallingAPI.ACCEPT_TREATMENT_API_PATH,
                        null, body, new HelperCallingAPI.MyCallback() {
                    @Override
                    public void onResponse(Response response) {
                        JSONObject data = null;
                        if(response.isSuccessful()){
                            DataChangeObserver.getInstance().getListener().onDataChanged(1);
                            DataChangeObserver.getInstance().getListener().onDataChanged(2);
                        }else {
                            try {
                                Log.d("ResponeAPI", response.body().string());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(IOException e) {
                        Log.d("ResponeAPI", e.toString());
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return CurrentPetTreatment.getPetTreatmentReadyList().size();
    }

    public class PetTreatmentViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_name_pet_owner, txt_Species, txt_Breed;
        ImageFilterButton petAvatar;
        AppCompatButton btn_accept, btn_reject;
        LinearLayout ll_item_process;
        public PetTreatmentViewHolder(@NonNull View itemView)
        {
            super(itemView);
            ll_item_process = itemView.findViewById(R.id.ll_item_process);
            txt_name_pet_owner = itemView.findViewById(R.id.txt_name_process_appointment);
            txt_Species = itemView.findViewById(R.id.txt_Date);
            txt_Breed = itemView.findViewById(R.id.txt_time);
            petAvatar = itemView.findViewById(R.id.process_appointment_avatar);
            btn_accept = itemView.findViewById(R.id.btn_accept_appointment);
            btn_reject = itemView.findViewById(R.id.btn_Reject_appointment);
        }
    }
}
