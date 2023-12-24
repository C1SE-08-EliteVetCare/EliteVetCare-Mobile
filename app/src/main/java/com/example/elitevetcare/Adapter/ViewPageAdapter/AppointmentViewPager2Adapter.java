package com.example.elitevetcare.Adapter.ViewPageAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.elitevetcare.Appointment.fragment_process;
import com.example.elitevetcare.Appointment.fragment_reject;

public class AppointmentViewPager2Adapter extends FragmentStateAdapter {
    Fragment FragmentCall = null;
    public AppointmentViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
//                FragmentCall = new fragment_schedule();
                break;
            case 2:
                FragmentCall = new fragment_reject();
                break;
            default:
                FragmentCall = new fragment_process();
        }
        return FragmentCall;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
