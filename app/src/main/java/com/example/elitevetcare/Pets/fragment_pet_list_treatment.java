package com.example.elitevetcare.Pets;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.elitevetcare.Adapter.ViewPageAdapter.PetTreatmentViewPager2Adapter;
import com.example.elitevetcare.Helper.DataChangeObserver;
import com.example.elitevetcare.Helper.ProgressHelper;
import com.example.elitevetcare.Interface.DataChangeListener;
import com.example.elitevetcare.Model.CurrentData.CurrentPetTreatment;
import com.example.elitevetcare.Model.ViewModel.PetTreatmentViewModel;
import com.example.elitevetcare.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_pet_list_treatment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_pet_list_treatment extends Fragment implements DataChangeListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_pet_list_treatment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_pet_list_treatment.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_pet_list_treatment newInstance(String param1, String param2) {
        fragment_pet_list_treatment fragment = new fragment_pet_list_treatment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    ExecutorService executor = Executors.newSingleThreadExecutor();
    PetTreatmentViewModel petTreatmentViewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        petTreatmentViewModel = new ViewModelProvider(getActivity()).get(PetTreatmentViewModel.class);
        DataChangeObserver.getInstance().setListener(this);
    }
    private TabLayout treatment_tabLayout;
    private ViewPager2 treatment_viewPager2;
    private PetTreatmentViewPager2Adapter PetTreatment_ViewPager2Adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pet_list_treatment, container, false);
        SetId(root);
        SetupViewpager2();
        if (CurrentPetTreatment.getPetTreatmentReadyList() == null)
            GetDataByAPI(1);
        if (CurrentPetTreatment.getPetTreatmentAcceptedList() == null)
            GetDataByAPI(2);
        startApiPolling();
        // Inflate the layout for this fragment
        return root;
    }

    private void GetDataByAPI(int status) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                Map<String,String> QuerryParams = new HashMap<>();
                QuerryParams.put("status", String.valueOf(status));
                QuerryParams.put("page", String.valueOf(1));
                QuerryParams.put("limit", String.valueOf(10));
                CurrentPetTreatment.CreateInstanceByAPI(QuerryParams ,new CurrentPetTreatment.PetTreatmentListCallback() {
                    @Override
                    public void OnSuccess(CurrentPetTreatment currentPetTreatmentList) {
                        if (getActivity() == null){
                            handler.removeCallbacks(x);
                            return;
                        }
                        if(status == 1)
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    petTreatmentViewModel.getProcessingTotalItem().setValue(CurrentPetTreatment.getPetTreatmentReadyList().size());

                                }
                            });

                        if(status == 2)

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    petTreatmentViewModel.getAcceptedTotalItem().setValue(CurrentPetTreatment.getPetTreatmentAcceptedList().size());

                                }
                            });
                            if(ProgressHelper.isDialogVisible())
                                ProgressHelper.dismissDialog();
                    }
                });
            }
        });
    }

    private void SetupViewpager2() {
        PetTreatment_ViewPager2Adapter = new PetTreatmentViewPager2Adapter(getActivity());
        treatment_viewPager2.setAdapter(PetTreatment_ViewPager2Adapter);
        new TabLayoutMediator(treatment_tabLayout, treatment_viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Sẳn Sàng");
                        break;
                    case 1:
                        tab.setText("Đã Nhận");
                        break;
                }
            }
        }).attach();
    }

    private void SetId(View root) {
        treatment_tabLayout = root.findViewById(R.id.pet_list_treatment_tabLayout);
        treatment_viewPager2 = root.findViewById(R.id.pet_treatment_viewpage2);
    }
    Handler handler = null;
    Runnable x  = null;

    private synchronized void startApiPolling() {
        if(handler == null  && petTreatmentViewModel.getisLoad().getValue() != null && petTreatmentViewModel.getisLoad().getValue() != false)
            petTreatmentViewModel.getisLoad().setValue(false);
        if(petTreatmentViewModel.getisLoad().getValue() != null && petTreatmentViewModel.getisLoad().getValue() == true)
            return;
        petTreatmentViewModel.getisLoad().setValue(true);
        handler =  new Handler(Looper.myLooper());
        final int delay = 30000;
        x = new Runnable() {
            public void run() {
                if(getActivity() == null){
                    handler.removeCallbacks(this);
                    petTreatmentViewModel.getisLoad().setValue(false);
                }
                GetDataByAPI(1);
                // Lặp lại sau mỗi khoảng thời gian delay
                handler.postDelayed(this, delay);
            }
        };
        handler.postDelayed(x, delay);
    }

    @Override
    public void onDataChanged(int status) {
        GetDataByAPI(status);
    }
}