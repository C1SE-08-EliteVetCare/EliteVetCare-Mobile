package com.example.elitevetcare.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.elitevetcare.Helper.HelperCallingAPI;
import com.example.elitevetcare.Helper.ViewModel.PetViewModel;
import com.example.elitevetcare.Model.Pet;
import com.example.elitevetcare.Model.PetCondition;
import com.example.elitevetcare.Pets.fragment_pet_infor_detail;
import com.example.elitevetcare.R;
import com.example.elitevetcare.fragment_tracking_pet_health;
import com.example.elitevetcare.Profile.fragment_update_pet_condition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Response;

public class ContentView extends AppCompatActivity {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    PetViewModel CurrentPetViewModel;
    int selected_fragment;
    Fragment CurrentFragment, PreviouseFragment;
    Pet currentPet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_view);
        Intent intent = getIntent();
        selected_fragment = intent.getIntExtra("FragmentCalling", -1);
        currentPet = (Pet) intent.getSerializableExtra("Pets");
        Init();
        CallingFragment();
    }


    private void Init() {
        CurrentPetViewModel = new ViewModelProvider(ContentView.this).get(PetViewModel.class);
    }
    private void ResetData() {
        selected_fragment = -1;
        CurrentFragment = null;
    }

    private void ChangeFragment(Fragment SelectedFragment) {
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frm_view_content, SelectedFragment, SelectedFragment.getClass().getSimpleName());
        transaction.commit();
    }

    private void CallingFragment() {
        if (selected_fragment == R.layout.fragment_pet_infor_detail){
            PreviouseFragment = CurrentFragment;
            ResetData();
            CurrentPetViewModel.setCurrentPet(currentPet);
            CurrentFragment = new fragment_pet_infor_detail();
            ChangeFragment(CurrentFragment);
            return;
        }
        if (selected_fragment == R.layout.fragment_tracking_pet_health){
            PreviouseFragment = CurrentFragment;
            ResetData();
            CurrentPetViewModel.setCurrentPet(currentPet);
            String PathParams = String.valueOf(currentPet.getId());
            HelperCallingAPI.CallingAPI_PathParams_Get(HelperCallingAPI.PET_CONDITION_PATH,PathParams , new HelperCallingAPI.MyCallback() {
                @Override
                public void onResponse(Response response) {
                    int responseStatus = response.code();
                    JSONObject data = null;
                    Log.d("PetCondition", String.valueOf(response.code()));
                    if(responseStatus == 200){
                        try {
                            data = new JSONObject(response.body().string());
                            JSONArray ListPetData = data.getJSONArray("data");
                            Gson gson = new Gson();
                            Type listType = new TypeToken<ArrayList<PetCondition>>(){}.getType();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    CurrentPetViewModel.setPetCondition(gson.fromJson(ListPetData.toString(), listType));
                                    ArrayList<PetCondition> petConditions = CurrentPetViewModel.getPetCondition().getValue();
                                    Log.d("PetCondition", String.valueOf(petConditions.get(0).getId()));
                                }
                            });

                        } catch (JSONException e) {
                            Log.d("PetCondition",e.getMessage());
                        } catch (IOException e) {
                            Log.d("PetCondition",e.getMessage());
                        }
                        CurrentFragment = new fragment_tracking_pet_health();
                        ChangeFragment(CurrentFragment);
                    } else if (responseStatus == 400) {
                        CurrentFragment = new fragment_update_pet_condition();
                        ChangeFragment(CurrentFragment);
                    }
                }

                @Override
                public void onFailure(IOException e) {

                }
            });


        }
    }
    public void setfragment(int IDFragment){
        selected_fragment = IDFragment;
        CallingFragment();
    }
}