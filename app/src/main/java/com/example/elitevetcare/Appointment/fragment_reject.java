package com.example.elitevetcare.Appointment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.elitevetcare.Helper.HelperCallingAPI;
import com.example.elitevetcare.Helper.Libs;
import com.example.elitevetcare.Model.ObjectModel.Appointment;
import com.example.elitevetcare.Model.CurrentData.CurrentAppointment;
import com.example.elitevetcare.Model.CurrentData.CurrentUser;
import com.example.elitevetcare.Model.ViewModel.AppointmentViewModel;
import com.example.elitevetcare.R;
import com.example.elitevetcare.Adapter.RecyclerViewAdapter.RecyclerViewRejectAppointmentAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_reject#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_reject extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_reject() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_reject.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_reject newInstance(String param1, String param2) {
        fragment_reject fragment = new fragment_reject();
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
        appointmentViewModel = new ViewModelProvider(requireActivity()).get(AppointmentViewModel.class);
    }

    RecyclerView recycler_AppointmentView;
    RecyclerView.Adapter RejectAppointmentAdapter;
    FragmentContainerView FragmentContainer;
    private boolean isLoading = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reject, container, false);
        recycler_AppointmentView = root.findViewById(R.id.Recycler_list_item_reject_appointment);
        FragmentContainer = root.findViewById(R.id.fragment_container_no_appointment);
        appointmentViewModel.getRejectedTotalItem().observe(requireActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer > 0){
                    recycler_AppointmentView.setVisibility(View.VISIBLE);
                    FragmentContainer.setVisibility(View.GONE);
                    if(RejectAppointmentAdapter != null) {
                        RejectAppointmentAdapter.notifyDataSetChanged();
                        return;
                    }
                    RejectAppointmentAdapter = new RecyclerViewRejectAppointmentAdapter(getActivity());
                    recycler_AppointmentView.setAdapter(RejectAppointmentAdapter);
                    recycler_AppointmentView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                            int visibleItemCount = layoutManager.getChildCount();
                            int totalItemCount = layoutManager.getItemCount();
                            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                            if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
                                    && firstVisibleItemPosition >= 0) {
                                // Nếu cuộn đến cuối cùng, tải thêm dữ liệu
                                loadMoreData();
                            }
                        }
                    });
                }else {
                    recycler_AppointmentView.setVisibility(View.GONE);
                    FragmentContainer.setVisibility(View.VISIBLE);
                }
            }
        });
        // Inflate the layout for this fragment
        return root;
    }
    private void loadMoreData() {
        isLoading = true;
        String API_PATH = "";
        if(CurrentUser.getCurrentUser().getRole().getId() == 2){
            API_PATH = HelperCallingAPI.APPOINTMENT_PET_OWNER_API_PATH;
        } else if (CurrentUser.getCurrentUser().getRole().getId() == 3) {
            API_PATH = HelperCallingAPI.APPOINTMENT_VET_API_PATH;
        }
        if(appointmentViewModel.getRejectedTotalItem().getValue() <= CurrentAppointment.getRejectAppointmentList().size()){
            isLoading = false;
            return;
        }
        Libs.Sendmessage(getActivity(), "Đang load thêm dữ liệu !");
        int page = 0;
        if(appointmentViewModel.getRejectedTotalItem().getValue() - CurrentAppointment.getRejectAppointmentList().size() >= 1)
            page = (CurrentAppointment.getRejectAppointmentList().size()/10) + 1;

        Map<String,String> QuerryParams =  new HashMap<>();
        QuerryParams.put("status", String.valueOf(3));
        QuerryParams.put("limit", String.valueOf(10));
        QuerryParams.put("page", String.valueOf(page));

        HelperCallingAPI.CallingAPI_QueryParams_Get(API_PATH, QuerryParams, new HelperCallingAPI.MyCallback() {
            @Override
            public void onResponse(Response response) {
                int statusCode = response.code();
                JSONObject data = null;

                if(statusCode == 200) {
                    try {
                        data = new JSONObject(response.body().string());
                        JSONArray ListPetData = data.getJSONArray("data");
                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<Appointment>>(){}.getType();
                        ArrayList<Appointment> List = gson.fromJson(ListPetData.toString(), listType);
                        CurrentAppointment.getRejectAppointmentList().addAll(List);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                RejectAppointmentAdapter.notifyDataSetChanged();

                            }
                        });
                    } catch (IOException | JSONException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    Log.d("RejectAppointmentRespone", String.valueOf(statusCode));
                    try {
                        Log.d("RejectAppointmentRespone", new JSONObject(response.body().string()).toString());
                    } catch (IOException | JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                isLoading = false;
            }
            @Override
            public void onFailure(IOException e) {

            }
        });
    }
}