package com.example.elitevetcare.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

//import com.example.elitevetcare.Helper.SocketGate;
import com.example.elitevetcare.Home.fragment_home;
import com.example.elitevetcare.Interface.SocketOnMessageListener;
import com.example.elitevetcare.Model.CurrentData.CurrentConversationHistory;
import com.example.elitevetcare.Model.CurrentData.CurrentLocation;
import com.example.elitevetcare.Model.CurrentData.CurrentUser;
import com.example.elitevetcare.Model.ObjectModel.Conversation;
import com.example.elitevetcare.Model.ObjectModel.User;
import com.example.elitevetcare.Model.ViewModel.AppointmentViewModel;
import com.example.elitevetcare.Pets.fragment_list_pet;
import com.example.elitevetcare.Pets.fragment_pet_list_treatment;
import com.example.elitevetcare.Profile.fragment_user_profile;
import com.example.elitevetcare.QuestionAndAnswer.fragment_conversation_history;
import com.example.elitevetcare.R;
import com.example.elitevetcare.Appointment.fragment_appointment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SocketOnMessageListener {

    FragmentContainerView frm_view;
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    AppointmentViewModel appointmentViewModel;
    BottomNavigationView bottomNavigationView;
    LinearLayout top_bar_select;
    Fragment selectedFragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        SocketGate.OpenGate(this);
//        SocketGate.addSocketEventListener(this);
        // Sử dụng BottomNavigationView để chuyển đổi giữa các Fragment
        bottomNavigationView = findViewById(R.id.bottom_Nav);
        top_bar_select = findViewById(R.id.top_tab_bar);
        appointmentViewModel = new ViewModelProvider(this).get(AppointmentViewModel.class);
        User currentUser = CurrentUser.getCurrentUser();

        if(currentUser.getBirthYear() == null)
        {
            Intent intent = new Intent(getApplicationContext(),UpdateProfile.class);
            intent.putExtra("FragmentCalling",R.layout.fragment_gender);
            startActivity(intent);
        }

        if (CurrentLocation.getCurrentLocation() == null)
            CurrentLocation.getInstance().GetCurrentLocationByAPI(this, new CurrentLocation.OnSuccesLoad() {
                @Override
                public void OnSuccess() {
                    ChangeFragment(new fragment_home());
                    Log.d("Location", CurrentLocation.getCurrentLocation().getLatitude() + "  " + CurrentLocation.getCurrentLocation().getLongitude() );
                }
            });

        bottomNavigationView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectedFragment = null;
                int item_id = item.getItemId();
                if(item_id == R.id.btn_bottomnav_home)
                    selectedFragment = new fragment_home();
                if(item_id == R.id.btn_bottomnav_QA)
                    selectedFragment = new fragment_conversation_history();
                if(item_id == R.id.btn_bottomnav_appointment){
                    selectedFragment = new fragment_appointment();
                }
                if(item_id == R.id.btn_bottomnav_pets){
                    if(CurrentUser.getCurrentUser().getRole().getId() == 2)
                        selectedFragment = new fragment_list_pet();
                    else
                        selectedFragment = new fragment_pet_list_treatment();
                }

                if(item_id == R.id.btn_bottomnav_profile){
                    selectedFragment = new fragment_user_profile();
                    top_bar_select.setVisibility(View.INVISIBLE);
                }else if (top_bar_select.getVisibility() == View.INVISIBLE){
                    top_bar_select.setVisibility(View.VISIBLE);
                }

                if (selectedFragment != null){
                    ChangeFragment(selectedFragment);
                    return true;
                }
                return false;
            }
        });

    }

    private void ChangeFragment(Fragment selectedFragment) {
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left);
        transaction.replace(R.id.frm_view_main, selectedFragment, selectedFragment.getClass().getSimpleName());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public Fragment getSelectedFragment() {
        return selectedFragment;
    }

    @Override
    public void onMessageListener(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            Gson gson = new Gson();
            Conversation conversation = gson.fromJson(jsonObject.get("conversation").toString(), new TypeToken<Conversation>(){}.getType());
            int index = isExist(conversation.getId());
            if(index != -1)
                CurrentConversationHistory.getConversationHistory().remove(index);
            CurrentConversationHistory.getConversationHistory().add(0,conversation);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    int isExist(int id){
        ArrayList<Conversation> list = CurrentConversationHistory.getConversationHistory();
        for (int i = 0; i < list.size(); i++) {
            Conversation obj = list.get(i);
            if (obj.getId() == id) {
                return i;
            }
        }
        return -1;
    }
}