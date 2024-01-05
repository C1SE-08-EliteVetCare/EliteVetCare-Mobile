package com.example.elitevetcare.Appointment;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.elitevetcare.Helper.DataChangeObserver;
import com.example.elitevetcare.Helper.HelperCallingAPI;
import com.example.elitevetcare.Helper.Libs;
import com.example.elitevetcare.Model.CurrentData.CurrentUser;
import com.example.elitevetcare.Model.ObjectModel.Appointment;
import com.example.elitevetcare.R;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_detail_appointment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_detail_appointment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_detail_appointment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_detail_appointment.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_detail_appointment newInstance(String param1, String param2) {
        fragment_detail_appointment fragment = new fragment_detail_appointment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    Appointment CurrAppointment = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            CurrAppointment = (Appointment) getArguments().getSerializable("Appointment");
        }
    }
    AppCompatEditText txt_name, txt_phone, txt_date, txt_time, txt_address;
    AppCompatButton btn_accept;
    RoundedImageView img_avatar_detail_appointment;
    LinearLayout ll_phone_appoint, ll_address_clinic;
    TextView txt_title, txt_title_appointment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail_appointment, container, false);
        if(CurrAppointment == null)
            return root;
        SetID(root);
        InitData();
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestBody body = new FormBody.Builder()
                        .add("action", String.valueOf(1))
                        .build();
                btn_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                HelperCallingAPI.CallingAPI_PathParams_Patch(HelperCallingAPI.ACCEPT_REJECT_APPOINTMENT_API_PATH, String.valueOf(CurrAppointment.getId()), body, new HelperCallingAPI.MyCallback() {
                    @Override
                    public void onResponse(Response response) {
                        int statusCode = response.code();
                        JSONObject JsonData = null;
                        if(statusCode == 200){
                            DataChangeObserver.getInstance().getListener().onDataChanged(1);
                            DataChangeObserver.getInstance().getListener().onDataChanged(2);
                        }else {
                            try {
                                JsonData = new JSONObject(response.body().string());
                                Log.d("error",JsonData.toString());
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

    private void InitData() {
        txt_title_appointment.setText(CurrAppointment.getServicePackage());
        if(CurrentUser.getCurrentUser().getRole().getId() == 3){
            txt_name.setText(CurrAppointment.getPetOwner().getFullName());
            txt_phone.setText(CurrAppointment.getPetOwner().getPhone());
            CurrAppointment.setClinic(CurrentUser.getCurrentUser().getClinic());
            Libs.SetImageFromURL(CurrAppointment.getPetOwner().getAvatar(),img_avatar_detail_appointment);
            if(CurrAppointment.getStatus() == 3)
                btn_accept.setVisibility(View.INVISIBLE);
        }else {
            txt_title.setText("Phòng Khám ");
            txt_name.setText(CurrAppointment.getClinic().getName());
            ll_phone_appoint.setVisibility(View.GONE);
            btn_accept.setVisibility(View.GONE);
            Libs.SetImageFromURL(CurrAppointment.getClinic().getLogo(),img_avatar_detail_appointment);
        }
        txt_date.setText(CurrAppointment.getAppointmentDate());
        txt_time.setText(CurrAppointment.getAppointmentTime());
        txt_address.setText(CurrAppointment.getClinic().getStreetAddress()+", " + CurrAppointment.getClinic().getWard() + ", " + CurrAppointment.getClinic().getDistrict()+ ", " + CurrAppointment.getClinic().getCity());
    }

    private void SetID(View root) {
        txt_name = root.findViewById(R.id.txt_name_user);
        txt_phone = root.findViewById(R.id.txt_phone_user);
        txt_date = root.findViewById(R.id.txt_date_appointment);
        txt_time = root.findViewById(R.id.txt_time_appointment);
        txt_address = root.findViewById(R.id.txt_address_clinic);
        btn_accept = root.findViewById(R.id.btn_accept);
        img_avatar_detail_appointment = root.findViewById(R.id.img_avatar_detail_appointment);
        ll_phone_appoint = root.findViewById(R.id.ll_phone_appoint);
        txt_title = root.findViewById(R.id.txt_title_name);
        ll_address_clinic = root.findViewById(R.id.ll_address_clinic);
        txt_title_appointment = root.findViewById(R.id.txt_title_appointment);
    }
}