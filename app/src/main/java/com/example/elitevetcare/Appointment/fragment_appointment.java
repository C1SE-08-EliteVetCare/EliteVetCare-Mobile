package com.example.elitevetcare.Appointment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.elitevetcare.Activity.ContentView;
import com.example.elitevetcare.Adapter.ViewPageAdapter.AppointmentViewPager2Adapter;
import com.example.elitevetcare.Helper.DataChangeObserver;
import com.example.elitevetcare.Helper.ProgressHelper;
import com.example.elitevetcare.Helper.SocketGate;
import com.example.elitevetcare.Interface.DataChangeListener;
import com.example.elitevetcare.Interface.SocketOnMessageListener;
import com.example.elitevetcare.Model.CurrentData.CurrentAppointment;
import com.example.elitevetcare.Model.CurrentData.CurrentUser;
import com.example.elitevetcare.Model.ViewModel.AppointmentViewModel;
import com.example.elitevetcare.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_appointment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_appointment extends Fragment implements DataChangeListener, SocketOnMessageListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_appointment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_appointment.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_appointment newInstance(String param1, String param2) {
        fragment_appointment fragment = new fragment_appointment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    AppointmentViewModel appointmentViewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Log.d("ProcessData",requireActivity().toString());
        appointmentViewModel = new ViewModelProvider(requireActivity()).get(AppointmentViewModel.class);
        DataChangeObserver.getInstance().setListener(this);
        SocketGate.addSocketEventListener(this::onMessageListener);
    }

    private TabLayout appointment_tabLayout;
    private ViewPager2 appointment_viewPager2;
    private AppointmentViewPager2Adapter appointment_ViewPager2Adapter;
    FloatingActionButton btn_new_appointment;
    ExecutorService  executor = Executors.newSingleThreadExecutor();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_appointment, container, false);
        SetID(root);
        SetUpViewPager2();
        SetUI();
        if(CurrentAppointment.getProcessingAppointmentList() == null )
            GetDataByAPI(1);
        if( CurrentAppointment.getAcceptedAppointmentList() == null)
            GetDataByAPI(2);
        if(CurrentAppointment.getRejectAppointmentList() == null)
            GetDataByAPI(3);
        startApiPolling();
        // Inflate the layout for this fragment
        return root;
    }

    private void SetUI() {
        if(CurrentUser.getCurrentUser().getRole().getId() == 3){
            btn_new_appointment.setVisibility(View.GONE);
            return;
        }
        btn_new_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), ContentView.class);
                intent.putExtra("FragmentCalling", R.layout.fragment_select_clinic);
                getActivity().startActivity(intent);
            }
        });
    }

    private void SetID(View root) {
        appointment_tabLayout = root.findViewById(R.id.appointment_tabLayout);
        appointment_viewPager2 = root.findViewById(R.id.appointment_viewpager2);
        btn_new_appointment = root.findViewById(R.id.appointment_FloatingActionButton);
    }

    public void GetDataByAPI(int status) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                Map <String,String> QuerryParams = new HashMap<>();
                QuerryParams.put("status", String.valueOf(status));
                QuerryParams.put("page", String.valueOf(1));
                QuerryParams.put("limit", String.valueOf(10));

                CurrentAppointment.CreateInstanceByAPI(QuerryParams, new CurrentAppointment.AppointmentCallback() {
                    @Override
                    public void OnSuccess(JSONObject JsonData) throws JSONException {
                        int total = Integer.parseInt(JsonData.getString("total"));
                        if (status == 1)
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    appointmentViewModel.setProcessingTotalItem(total);
                                    Log.d("ProcessData", String.valueOf(appointmentViewModel.getProcessingTotalItem().getValue()));
                                }
                            });
                        if (status == 2)
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    appointmentViewModel.setAcceptedTotalItem(total);
                                }
                            });
                        if (status == 3)
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    appointmentViewModel.setRejectedTotalItem(total);
                                }
                            });
                        if(ProgressHelper.isDialogVisible())
                            ProgressHelper.dismissDialog();
                    }
                });
            }
        });
    }

    private void SetUpViewPager2() {
        appointment_ViewPager2Adapter = new AppointmentViewPager2Adapter(requireActivity());
        appointment_viewPager2.setAdapter(appointment_ViewPager2Adapter);
        new TabLayoutMediator(appointment_tabLayout, appointment_viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Đang Xử Lý");
                        break;
                    case 1:
                        tab.setText("Đã Nhận");
                        break;
                    case 2:
                        tab.setText("Từ Chối");
                        break;
                }
            }
        }).attach();
    }
    private synchronized void startApiPolling() {
        if(appointmentViewModel.getisLoad().getValue() != null && appointmentViewModel.getisLoad().getValue() == true)
            return;
        appointmentViewModel.getisLoad().setValue(true);
        Handler handler =  new Handler(Looper.myLooper());
        final int delay = 60000;
        handler.postDelayed(new Runnable() {
            public void run() {
                GetDataByAPI(1);
                GetDataByAPI(2);
                GetDataByAPI(3);
                // Lặp lại sau mỗi khoảng thời gian delay
                handler.postDelayed(this, delay);
            }
        }, delay);
    }
    @Override
    public void onDataChanged(int status) {
        GetDataByAPI(status);
    }

    @Override
    public void onMessageListener(String message, int Code) {
        if(Code != SocketGate.APPOINTMENT_CREATE_EVENT_CODE && Code != SocketGate.APPOINTMENT_STATUS_EVENT_CODE)
            return;
        if(Code == SocketGate.APPOINTMENT_CREATE_EVENT_CODE)
            GetDataByAPI(1);
        else if (Code == SocketGate.APPOINTMENT_STATUS_EVENT_CODE) {
            GetDataByAPI(1);
            GetDataByAPI(2);
            GetDataByAPI(3);
        }
    }
}