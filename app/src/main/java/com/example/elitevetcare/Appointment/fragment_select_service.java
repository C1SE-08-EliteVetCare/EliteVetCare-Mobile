package com.example.elitevetcare.Appointment;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.elitevetcare.Adapter.ServiceListItemAdapter;
import com.example.elitevetcare.Helper.DataChangeObserver;
import com.example.elitevetcare.Helper.HelperCallingAPI;
import com.example.elitevetcare.Model.ObjectModel.ServicePackage;
import com.example.elitevetcare.Model.ViewModel.AppointmentViewModel;
import com.example.elitevetcare.Model.ViewModel.BookAppointmentViewModel;
import com.example.elitevetcare.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_select_service#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_select_service extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_select_service() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_select_service.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_select_service newInstance(String param1, String param2) {
        fragment_select_service fragment = new fragment_select_service();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    BookAppointmentViewModel bookAppointmentViewModel;
    AppointmentViewModel appointmentViewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        bookAppointmentViewModel = new ViewModelProvider(requireActivity()).get(BookAppointmentViewModel.class);
        appointmentViewModel = new ViewModelProvider(getActivity()).get(AppointmentViewModel.class);
    }
    RecyclerView list_item_service_package;
    ArrayList<ServicePackage> listService;
    ServiceListItemAdapter ServiceAdapter;
    AppCompatButton btn_done_service;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_select_service, container, false);
        list_item_service_package = root.findViewById(R.id.list_item_service_package);
        btn_done_service = root.findViewById(R.id.btn_done_service);
        listService = new ArrayList<>();
        listService.add(new ServicePackage("Khám Định Kỳ", "Nên khám mỗi tháng một lần"));
        listService.add(new ServicePackage("Khám bệnh", "Khi thú cưng bị bệnh"));
        listService.add(new ServicePackage("Tiêm Vacxin", "Tuỳ theo Vacxin"));
        Log.d("ServicePackage", listService.toString());
        ServiceAdapter = new ServiceListItemAdapter(getActivity(),listService);
        list_item_service_package.setAdapter(ServiceAdapter);

        btn_done_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ServiceAdapter.getSelectedItem() == null){
                    Toast.makeText(getContext(), "Chưa Chọn Gói Dịch Vụ !", Toast.LENGTH_SHORT).show();
                    return;
                }
//                Log.d("ServicePackage", String.valueOf(appointmentViewModel.getProcessingTotalItem().getValue()));
                RequestBody body = new FormBody.Builder()
                        .add("appointmentDate", bookAppointmentViewModel.getDate().getValue())
                        .add("appointmentTime", bookAppointmentViewModel.getTime().getValue())
                        .add("servicePackage", ServiceAdapter.getSelectedItem().getName())
                        .add("clinicId", bookAppointmentViewModel.getClinicId().getValue().toString())
                        .build();
                HelperCallingAPI.CallingAPI_QueryParams_Post(HelperCallingAPI.CREATE_APPOINTMENT_API_PATH, null, body, new HelperCallingAPI.MyCallback() {
                    @Override
                    public void onResponse(Response response) {
                        int statusCode = response.code();
                        JSONObject dataJson;
                        if(statusCode == 201){
                            DataChangeObserver.getInstance().getListener().onDataChanged(1);
                            getActivity().finish();
                        }else{
                            try {
                                dataJson = new JSONObject(response.body().string());
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Toast.makeText(getContext(),dataJson.getString("message"),Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                        getActivity().finish();
                                    }
                                });
                            } catch (JSONException | IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(IOException e) {

                    }
                });
            }
        });
        // Inflate the layout for this fragment
        return root;
    }
}