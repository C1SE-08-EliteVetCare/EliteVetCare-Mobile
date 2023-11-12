package com.example.elitevetcare.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.example.elitevetcare.Home.fragment_home;
import com.example.elitevetcare.Model.CurrentUser;
import com.example.elitevetcare.Model.User;
import com.example.elitevetcare.Pets.fragment_list_pet;
import com.example.elitevetcare.Profile.fragment_user_profile;
import com.example.elitevetcare.QuestionAndAnswer.fragment_chatbot_list;
import com.example.elitevetcare.R;
import com.example.elitevetcare.Appointment.fragment_appointment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener;

public class MainActivity extends AppCompatActivity {

    FragmentContainerView frm_view;
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    BottomNavigationView bottomNavigationView;
    LinearLayout top_bar_select;
    Fragment selectedFragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sử dụng BottomNavigationView để chuyển đổi giữa các Fragment
        bottomNavigationView = findViewById(R.id.bottom_Nav);
        top_bar_select = findViewById(R.id.top_tab_bar);


        ChangeFragment(new fragment_home());

        User currentUser = CurrentUser.getCurrentUser();
        if(currentUser.getBirthYear() == null)
        {
            Intent intent = new Intent(getApplicationContext(),UpdateProfile.class);
            intent.putExtra("FragmentCalling",R.layout.fragment_gender);
            startActivity(intent);
        }


        bottomNavigationView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectedFragment = null;
                int item_id = item.getItemId();
                if(item_id == R.id.btn_bottomnav_home)
                    selectedFragment = new fragment_home();
                if(item_id == R.id.btn_bottomnav_QA)
                    selectedFragment = new fragment_chatbot_list();
                if(item_id == R.id.btn_bottomnav_appointment)
                    selectedFragment = new fragment_appointment();
                if(item_id == R.id.btn_bottomnav_pets){
                    selectedFragment = new fragment_list_pet();
                    if(CurrentUser.getCurrentUser().getRole().getId() == 2)
                        selectedFragment = new fragment_list_pet();
                }

                if(item_id == R.id.btn_bottomnav_profile){
                    selectedFragment = new fragment_user_profile();
//                    top_bar_select.setVisibility(View.INVISIBLE);
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
}