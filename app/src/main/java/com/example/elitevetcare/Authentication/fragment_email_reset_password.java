package com.example.elitevetcare.Authentication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.elitevetcare.Activity.Login;

import com.example.elitevetcare.Helper.HelperCallingAPI;

import com.example.elitevetcare.Model.ViewModel.EmailViewModel;
import com.example.elitevetcare.R;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_email_reset_password#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_email_reset_password extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button btn_Continue;
    EditText edt_Emailconfirm;

    EmailViewModel viewModel;
    public fragment_email_reset_password() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_email_reset_password.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_email_reset_password newInstance(String param1, String param2) {
        fragment_email_reset_password fragment = new fragment_email_reset_password();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        viewModel = new ViewModelProvider(requireActivity()).get( EmailViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_email_reset_password, container, false);

        btn_Continue = root.findViewById(R.id.btn_continue);
        edt_Emailconfirm = root.findViewById(R.id.edt_emailconfirm);

        btn_Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email =  edt_Emailconfirm.getText().toString();
                viewModel.setEmail(email);
                if( !edt_Emailconfirm.getText().toString().equals("") ){
                    FormBody body = new FormBody.Builder()
                            .add("email", email)
                            .build();
                    HelperCallingAPI.CallingAPI_QueryParams_Post(HelperCallingAPI.FORGET_PASSWORD, null, body, new HelperCallingAPI.MyCallback() {
                        @Override
                        public void onResponse(Response response) {
                            if(response.isSuccessful()){
                                ((Login) getActivity()).changeFragment(new fragment_confirmEmail());
                            }else {

                            }
                        }

                        @Override
                        public void onFailure(IOException e) {

                        }
                    });

                }else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), " Không được để trống ", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

        });

        return root;


    }
}