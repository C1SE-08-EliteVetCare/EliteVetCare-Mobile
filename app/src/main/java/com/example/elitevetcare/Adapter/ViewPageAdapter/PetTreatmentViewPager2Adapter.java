package com.example.elitevetcare.Adapter.ViewPageAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.elitevetcare.Appointment.fragment_process;
import com.example.elitevetcare.Appointment.fragment_reject;
import com.example.elitevetcare.Appointment.fragment_schedule;
import com.example.elitevetcare.Pets.fragment_accepted_pet_treatment;
import com.example.elitevetcare.Pets.fragment_process_pet_treatment;

public class PetTreatmentViewPager2Adapter  extends FragmentStateAdapter {
    Fragment FragmentCall = null;
    public PetTreatmentViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public PetTreatmentViewPager2Adapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public PetTreatmentViewPager2Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                FragmentCall = new fragment_process_pet_treatment();
                break;
            default:
                FragmentCall = new fragment_accepted_pet_treatment();
        }
        return FragmentCall;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
