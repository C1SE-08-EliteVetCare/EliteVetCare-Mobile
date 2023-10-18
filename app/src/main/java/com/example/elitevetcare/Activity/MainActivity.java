package com.example.elitevetcare.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.elitevetcare.Authentication.fragment_success;
import com.example.elitevetcare.Profile.fragment_user_profile;
import com.example.elitevetcare.R;
import com.example.elitevetcare.Appointment.fragment_appointment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener;

public class MainActivity extends AppCompatActivity {

    FragmentContainerView frm_view;
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sử dụng BottomNavigationView để chuyển đổi giữa các Fragment
        bottomNavigationView = findViewById(R.id.bottom_Nav);

        bottomNavigationView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int item_id = item.getItemId();
                if(item_id == R.id.btn_bottomnav_home)
                    selectedFragment = new fragment_success();
                if(item_id == R.id.btn_bottomnav_QA)
                    selectedFragment = new fragment_success();
                if(item_id == R.id.btn_bottomnav_appointment)
                    selectedFragment = new fragment_appointment();
                if(item_id == R.id.btn_bottomnav_pets)
                    selectedFragment = new fragment_success();
                if(item_id == R.id.btn_bottomnav_profile)
                    selectedFragment = new fragment_user_profile();
                if (selectedFragment != null){
                    transaction = fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left);
                    transaction.replace(R.id.frm_view_main, selectedFragment, selectedFragment.getClass().getSimpleName());
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return true;
                }
                return false;
            }
        });

    }
}