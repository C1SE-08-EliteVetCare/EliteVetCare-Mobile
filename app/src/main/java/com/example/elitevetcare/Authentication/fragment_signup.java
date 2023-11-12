package com.example.elitevetcare.Authentication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.elitevetcare.Activity.Login;
import com.example.elitevetcare.Activity.MainActivity;
import com.example.elitevetcare.Helper.DataLocalManager;
import com.example.elitevetcare.Helper.HelperCallingAPI;
import com.example.elitevetcare.Helper.SignUpViewModel;
import com.example.elitevetcare.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_signup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_signup extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public fragment_signup() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_signup.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_signup newInstance(String param1, String param2) {
        fragment_signup fragment = new fragment_signup();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    AppCompatEditText edt_username, edt_email, edt_password, edt_phonenumber;

    AppCompatButton btn_next_register;
    LinearLayout group_btn_next;

    SignUpViewModel viewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        viewModel = new ViewModelProvider(requireActivity()).get( SignUpViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_signup, container, false);

        SetID(root);

        animation();
        SetEvent();


        // Inflate the layout for this fragment
        return root;
    }

    private void animation() {
        edt_username.setTranslationX(800);
        edt_email.setTranslationX(800);
        edt_password.setTranslationX(800);
        edt_phonenumber.setTranslationX(800);
        group_btn_next.setTranslationX(800);

        edt_username.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        edt_email.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        edt_password.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        edt_phonenumber.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(1000).start();
        group_btn_next.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(1200).start();
    }

    private void SetEvent() {

        btn_next_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username =  edt_username.getText().toString();
                String email =  edt_email.getText().toString();
                String password =  edt_password.getText().toString();
                String phone =  edt_phonenumber.getText().toString();


                viewModel.setUserName(username);
                viewModel.setEmail(email);
                viewModel.setPassword(password);
                viewModel.setPhonenumber(phone);

                RequestBody requestBody = new FormBody.Builder()
                        .add("fullName", username)
                        .add("email", email)
                        .add("password", password)
                        .add("phone", phone)
                        .build();

                HelperCallingAPI.CallingAPI_noHeader(HelperCallingAPI.REGISTER_PATH, requestBody, new HelperCallingAPI.MyCallback() {
                    @Override
                    public void onResponse(Response response) {
                        if (response.code() == 201) {
                            ((Login)getActivity()).changeFragment(new fragment_verify());

                        } else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "Đăng ký không thành công. Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(IOException e) {
                        Toast.makeText(getActivity(), "Lỗi kết nối mạng. Vui lòng kiểm tra lại kết nối của bạn.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void SetID(View root) {
        edt_username = root.findViewById(R.id.edt_username_register);
        edt_email = root.findViewById(R.id.edt_email_register);
        edt_password = root.findViewById(R.id.edt_password_register);
        edt_phonenumber = root.findViewById(R.id.edt_phone_register);
        group_btn_next = root.findViewById(R.id.group_btn_next_register);
        btn_next_register = root.findViewById(R.id.btn_next_register);
    }
}