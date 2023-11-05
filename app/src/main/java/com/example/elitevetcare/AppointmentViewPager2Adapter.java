package com.example.elitevetcare;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.elitevetcare.Appointment.fragment_process;
import com.example.elitevetcare.Appointment.fragment_reject;
import com.example.elitevetcare.Appointment.fragment_schedule;
import com.example.elitevetcare.Authentication.fragment_login;
import com.example.elitevetcare.Authentication.fragment_success;
import com.example.elitevetcare.Profile.fragment_address;
import com.example.elitevetcare.Profile.fragment_weight_pet;

public class AppointmentViewPager2Adapter extends FragmentStateAdapter {
    public AppointmentViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new fragment_process();
            case 1:
                return new fragment_schedule();
            case 2:
                return new fragment_reject();
            default:
                return new fragment_process();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
