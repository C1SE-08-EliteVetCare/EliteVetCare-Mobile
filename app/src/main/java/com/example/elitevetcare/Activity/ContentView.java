package com.example.elitevetcare.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.elitevetcare.Appointment.fragment_pet_owner_infor;
import com.example.elitevetcare.Appointment.fragment_select_clinic;
import com.example.elitevetcare.Appointment.fragment_select_datetime;
import com.example.elitevetcare.Appointment.fragment_select_service;
import com.example.elitevetcare.Helper.HelperCallingAPI;
import com.example.elitevetcare.Helper.Libs;
import com.example.elitevetcare.Helper.ProgressHelper;
import com.example.elitevetcare.Model.CurrentData.CurrentUser;
import com.example.elitevetcare.Model.ObjectModel.Conversation;
import com.example.elitevetcare.Model.ObjectModel.Message;
import com.example.elitevetcare.Model.ObjectModel.Pet;
import com.example.elitevetcare.Model.ObjectModel.PetCondition;
import com.example.elitevetcare.Model.ObjectModel.User;
import com.example.elitevetcare.Model.ViewModel.MessageViewModel;
import com.example.elitevetcare.Model.ViewModel.PetViewModel;
import com.example.elitevetcare.Pets.fragment_pet_infor_detail;
import com.example.elitevetcare.Pets.fragment_select_clinic_treatment;
import com.example.elitevetcare.Pets.fragment_tracking_pet_health;
import com.example.elitevetcare.Pets.fragment_update_pet_condition;
import com.example.elitevetcare.Profile.fragment_edit_profile_user;
import com.example.elitevetcare.QuestionAndAnswer.fragment_conversation;
import com.example.elitevetcare.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.makeramen.roundedimageview.RoundedImageView;

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
    MessageViewModel MessageViewModel;
    TextView txt_content;
    int selected_fragment;
    Fragment CurrentFragment, PreviouseFragment;
    Pet currentPet;
    RoundedImageView img_avatar_conversation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_view);
        Intent intent = getIntent();
        selected_fragment = intent.getIntExtra("FragmentCalling", -1);
        if (intent.hasExtra("Pets"))
            currentPet = (Pet) intent.getSerializableExtra("Pets");
        SetID();
        Init();
        CallingFragment();
    }

    private void SetID() {
        txt_content = findViewById(R.id.txt_content_title);
        img_avatar_conversation = findViewById(R.id.img_avatar_conversation);
    }


    private void Init() {
        CurrentPetViewModel = new ViewModelProvider(ContentView.this).get(PetViewModel.class);
        MessageViewModel = new ViewModelProvider(ContentView.this).get(MessageViewModel.class);
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
        if (selected_fragment == R.layout.fragment_edit_profile_user){
            PreviouseFragment = CurrentFragment;
            ResetData();
            txt_content.setText("Chỉnh Sửa Hồ Sơ");
            CurrentFragment = new fragment_edit_profile_user();
            ChangeFragment(CurrentFragment);
            return;
        }
        if (selected_fragment == R.layout.fragment_pet_infor_detail){
            CallingPetInforDetail();
            return;
        }
        if (selected_fragment == R.layout.fragment_tracking_pet_health){
            CallingTrackingPetHealth();
            return;
        }
        if(selected_fragment == R.layout.fragment_select_clinic){
            CallingSelectClinic();
            return;
        }
        if(selected_fragment == R.layout.fragment_select_datetime){
            CallingSelectTime();
            return;
        }
        if(selected_fragment == R.layout.fragment_pet_owner_infor){
            CallingPetOwnerInfor();
            return;
        }
        if(selected_fragment == R.layout.fragment_select_service){
            PreviouseFragment = CurrentFragment;
            ResetData();
            txt_content.setText("Chọn Gói Khám");
            CurrentFragment = new fragment_select_service();
            ChangeFragment(CurrentFragment);
            return;
        }
        if(selected_fragment == R.layout.fragment_select_clinic_treatment){
            CallingSelectClinicTreatment();
            return;
        }
        if(selected_fragment == R.layout.fragment_conversation){
            PreviouseFragment = CurrentFragment;
            ResetData();
            Conversation conversation = (Conversation) getIntent().getSerializableExtra("conversation");
            User Recipient = null;
            if(conversation.getRecipient().getId() != CurrentUser.getCurrentUser().getId())
                Recipient = conversation.getRecipient();
            else
                Recipient = conversation.getCreator();
            Libs.SetImageFromURL(Recipient.getAvatar(), img_avatar_conversation);
            img_avatar_conversation.setVisibility(View.VISIBLE);
            txt_content.setText(Recipient.getFullName());
            CurrentFragment = new fragment_conversation();
            MessageViewModel.SetLoading(true);
            MessageViewModel.SetConversationID(conversation.getId());
            HelperCallingAPI.CallingAPI_PathParams_Get(HelperCallingAPI.GET_ALL_MESSAGE_OF_CONVERSATION, conversation.getId()+"", new HelperCallingAPI.MyCallback() {
                @Override
                public void onResponse(Response response) {
                    if(response.isSuccessful()) {
                        try {
                            JSONObject data = new JSONObject(response.body().string());
                            Gson gson = new Gson();
                            Type listType = new TypeToken<ArrayList<Message>>(){}.getType();
                            ArrayList<Message> listMessage = gson.fromJson(data.get("data").toString(), listType);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    MessageViewModel.SetLoading(false);
                                    MessageViewModel.AddMessageArray(listMessage);
                                }
                            });
                        } catch (JSONException | IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else {
                        Toast.makeText(ContentView.this, "Đang có lỗi khi lấy tin nhắn cũ", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(IOException e) {

                }
            });
            ChangeFragment(CurrentFragment);
            return;
        }
    }

    private void CallingSelectClinicTreatment() {
        PreviouseFragment = CurrentFragment;
        ResetData();
        txt_content.setText("Các Phòng Khám");
        CurrentFragment = new fragment_select_clinic_treatment();
        ChangeFragment(CurrentFragment);
    }

    private void CallingPetOwnerInfor() {
        PreviouseFragment = CurrentFragment;
        ResetData();
        txt_content.setText("Thông Tin Khách Hàng");
        CurrentFragment = new fragment_pet_owner_infor();
        ChangeFragment(CurrentFragment);
    }

    private void CallingSelectTime() {
        PreviouseFragment = CurrentFragment;
        ResetData();
        txt_content.setText("Chọn Lịch");
        CurrentFragment = new fragment_select_datetime();
        ChangeFragment(CurrentFragment);
    }

    private void CallingSelectClinic() {
        PreviouseFragment = CurrentFragment;
        ResetData();
        txt_content.setText("Các Phòng Khám");
        CurrentFragment = new fragment_select_clinic();
        ChangeFragment(CurrentFragment);
    }

    private void CallingTrackingPetHealth() {
        PreviouseFragment = CurrentFragment;
        ResetData();
        CurrentPetViewModel.setCurrentPet(currentPet);
        String PathParams = String.valueOf(currentPet.getId());

        if(!ProgressHelper.isDialogVisible())
            ProgressHelper.showDialog(ContentView.this,"Đang Lấy Dữ Liệu");
        HelperCallingAPI.CallingAPI_PathParams_Get(HelperCallingAPI.PET_CONDITION_PATH,PathParams , new HelperCallingAPI.MyCallback() {
            @Override
            public void onResponse(Response response) {
                int responseStatus = response.code();
                JSONObject data = null;

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
                                if(petConditions == null || petConditions.size() <= 0){
                                    txt_content.setText("Cập nhập tình trạng thú cưng");
                                    CurrentFragment = new fragment_update_pet_condition();
                                    Toast.makeText(ContentView.this, "Hãy thử cập nhập lần đầu tiên nhé!", Toast.LENGTH_SHORT).show();
                                    ChangeFragment(CurrentFragment);
                                    return;
                                }
                                txt_content.setText("Tình Trạng Thú Cưng");
                                CurrentFragment = new fragment_tracking_pet_health();
                                ChangeFragment(CurrentFragment);
//                                Log.d("PetCondition", String.valueOf(petConditions.get(0).getId()));
                            }
                        });

                    } catch (JSONException e) {
                        Log.d("PetCondition",e.getMessage());
                    } catch (IOException e) {
                        Log.d("PetCondition",e.getMessage());
                    }
                } else if (responseStatus == 400) {
                    CurrentFragment = new fragment_update_pet_condition();
                    ChangeFragment(CurrentFragment);
                }

                if(ProgressHelper.isDialogVisible())
                    ProgressHelper.dismissDialog();
            }

            @Override
            public void onFailure(IOException e) {

            }
        });
    }

    private void CallingPetInforDetail() {
        PreviouseFragment = CurrentFragment;
        ResetData();
        CurrentPetViewModel.setCurrentPet(currentPet);
        CurrentFragment = new fragment_pet_infor_detail();
        txt_content.setText("Thông Tin Chi Tiết");
        ChangeFragment(CurrentFragment);
    }

    public void setfragment(int IDFragment){
        selected_fragment = IDFragment;
        CallingFragment();
    }
}