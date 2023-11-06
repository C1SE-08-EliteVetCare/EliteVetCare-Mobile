package com.example.elitevetcare.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.elitevetcare.Helper.DataLocalManager;
import com.example.elitevetcare.Helper.HelperCallingAPI;
import com.example.elitevetcare.Helper.PetInforViewModel;
import com.example.elitevetcare.Profile.fragment_age;
import com.example.elitevetcare.Profile.fragment_gender_pets;
import com.example.elitevetcare.Profile.fragment_update_pet_infor;
import com.example.elitevetcare.Profile.fragment_weight_pet;
import com.example.elitevetcare.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdateProfile extends AppCompatActivity {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    PetInforViewModel petInforViewModel;
    int selected_fragment;
    Fragment CurrentFragment, PreviouseFragment;
    AppCompatButton btn_nextDoing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        Intent intent = getIntent();
        selected_fragment = intent.getIntExtra("FragmentCalling", -1);
        Init();
        CallingFragment();


    }
    private void Init() {
        btn_nextDoing = findViewById(R.id.btn_nextDoing_UpdateProfile);
        petInforViewModel = new ViewModelProvider(UpdateProfile.this).get(PetInforViewModel.class);
    }
    private void ResetData() {
        selected_fragment = -1;
        CurrentFragment = null;
        btn_nextDoing.setOnClickListener(null);
    }

    private void ChangeFragment(Fragment SelectedFragment) {
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frm_view_update_profile, SelectedFragment, SelectedFragment.getClass().getSimpleName());
        transaction.commit();
    }

    private void CallingFragment() {
        if (selected_fragment == R.layout.fragment_update_pet_infor){
            PreviouseFragment = CurrentFragment;
            ResetData();
            CurrentFragment = new fragment_update_pet_infor();
            ChangeFragment(CurrentFragment);
            btn_nextDoing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean result_status = ((fragment_update_pet_infor)CurrentFragment).SendData();
                    if (result_status){
                        selected_fragment = R.layout.fragment_gender_pets;
                        CallingFragment();
                    }
                }
            });
            return;
        }

        if(selected_fragment == R.layout.fragment_gender_pets){
            PreviouseFragment = CurrentFragment;
            ResetData();
            CurrentFragment = new fragment_gender_pets();
            Bundle bundle = new Bundle();
            bundle.putString("Function","Pet");
            CurrentFragment.setArguments(bundle);
            ChangeFragment(CurrentFragment);
            btn_nextDoing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean result_status = ((fragment_gender_pets)CurrentFragment).SendData();
                    if (result_status){
                        selected_fragment = R.layout.fragment_age;
                        CallingFragment();
                    }
                }
            });
            return;
        }

        if(selected_fragment == R.layout.fragment_age){
            PreviouseFragment = CurrentFragment;
            ResetData();
            CurrentFragment = new fragment_age();
            Bundle bundle = new Bundle();
            bundle.putString("Function","Pet");
            CurrentFragment.setArguments(bundle);
            ChangeFragment(CurrentFragment);
            btn_nextDoing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean result_status = ((fragment_age)CurrentFragment).SendData();
                    if (result_status){
                        selected_fragment = R.layout.fragment_weight_pet;
                        CallingFragment();
                    }
                }
            });
            return;
        }

        if(selected_fragment == R.layout.fragment_weight_pet){
            PreviouseFragment = CurrentFragment;
            ResetData();
            CurrentFragment = new fragment_weight_pet();
            ChangeFragment(CurrentFragment);
            btn_nextDoing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean result_status = ((fragment_weight_pet)CurrentFragment).SendData();
                    if (result_status){
                        RequestBody body = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("name", petInforViewModel.getNickName().getValue())
                                .addFormDataPart("species", petInforViewModel.getSpecies().getValue())
                                .addFormDataPart("breed", petInforViewModel.getBreed().getValue())
                                .addFormDataPart("gender",petInforViewModel.getGender().getValue())
                                .addFormDataPart("age",String.valueOf(petInforViewModel.getAge().getValue()))
                                .addFormDataPart("weight",String.valueOf(petInforViewModel.getWeight().getValue()))
                                .addFormDataPart("furColor",petInforViewModel.getColor().getValue())
                                .addFormDataPart("avatar", "pet_avatar.jpg", RequestBody.create(petInforViewModel.getFileImage().getValue(),MediaType.parse("image/jpg")))
                                .build();
                        Toast.makeText(UpdateProfile.this, petInforViewModel.getFileImage().getValue().getPath(), Toast.LENGTH_SHORT).show();
//                        if (DataLocalManager.getInstance() != null && DataLocalManager.GetAccessTokenTimeUp() < System.currentTimeMillis()) {
//                            if(HelperCallingAPI.RefreshTokenCalling()!= true)
//                                return;
//                        }
                        HelperCallingAPI.CallingAPI_withHeader(HelperCallingAPI.CREATE_PET_PROFILE_PATH, body, DataLocalManager.GetAccessToken(), new HelperCallingAPI.MyCallback() {
                            @Override
                            public void onResponse(Response response) {
                                int statusCode = response.code();
                                JSONObject data = null;
                                if(statusCode == 201) {
                                    try {
                                            data = new JSONObject(response.body().string());
                                        JSONObject finalData = data;
                                        runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    finish();
                                                    try {
                                                        Toast.makeText(UpdateProfile.this, finalData.getString("message"), Toast.LENGTH_SHORT).show();
                                                    } catch (JSONException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }
                                            });
                                    } catch (JSONException | IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                            @Override
                            public void onFailure(IOException e) {

                            }
                        });
                    }
                }
            });
        }
    }


}