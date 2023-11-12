package com.example.elitevetcare.Authentication;

import static android.widget.Toast.LENGTH_SHORT;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.nfc.Tag;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elitevetcare.Activity.Login;
import com.example.elitevetcare.Helper.HelperCallingAPI;
import com.example.elitevetcare.Helper.SignUpViewModel;
import com.example.elitevetcare.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_verify#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_verify extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextInputEditText verify_code_1,verify_code_2,verify_code_3,verify_code_4,verify_code_5,verify_code_6 ;

    String otp = "";

    CountDownTimer countDownTimer;

    TextView timer;
    int timecount = 60;
    public fragment_verify() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_authen.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_verify newInstance(String param1, String param2) {
        fragment_verify fragment = new fragment_verify();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    SignUpViewModel viewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        new CountDownTimer(60000, 1000){

            public void onTick(long millisUntilFinished) {
                long timeCountDown = millisUntilFinished / 1000;
                timer.setText( "" + timeCountDown);
            }

            public void onFinish() {
                // Đếm ngược kết thúc, thực hiện các hành động như hiển thị một tin nhắn
                // hoặc thực hiện một công việc cụ thể
                // Ví dụ: textView.setText("Đếm ngược Kết thúc!");
            }
        }.start();
        viewModel = new ViewModelProvider(requireActivity()).get( SignUpViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_verify, container, false);
        SetID(root);
        SetEvent();
        return root;
    }

    private void SetEvent(){
        verify_code_1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1) {
                        verify_code_1.clearFocus(); // Loại bỏ sự tập trung của editText1
                        verify_code_2.requestFocus(); // Chuyển sự tập trung đến editText
                        otp += s;

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

            }
        });

        verify_code_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    verify_code_2.clearFocus(); // Loại bỏ sự tập trung của editText1
                    verify_code_3.requestFocus(); // Chuyển sự tập trung đến editText2
                    otp += s;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        verify_code_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    verify_code_3.clearFocus(); // Loại bỏ sự tập trung của editText1
                    verify_code_4.requestFocus(); // Chuyển sự tập trung đến editText2
                    otp += s;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        verify_code_4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    verify_code_4.clearFocus(); // Loại bỏ sự tập trung của editText1
                    verify_code_5.requestFocus(); // Chuyển sự tập trung đến editText2
                    otp += s;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        verify_code_5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    verify_code_5.clearFocus(); // Loại bỏ sự tập trung của editText1
                    verify_code_6.requestFocus(); // Chuyển sự tập trung đến editText2
                    otp += s;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        verify_code_6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 1) {
                    otp += s;

                        RequestBody requestBody = new FormBody.Builder()
                                .add("email", viewModel.getEmail().getValue())
                                .add("otp", otp)
                                .build();
                        HelperCallingAPI.CallingAPI_noHeader(HelperCallingAPI.VERIFY_PATH, requestBody, new HelperCallingAPI.MyCallback() {
                            @Override
                            public void onResponse(Response response) {

//                                Log.d( "onResponse: ", String.valueOf(response.code() ) );
//                                try {
//                                    Log.d("onResponse: ", new JSONObject(response.body().string()).toString()  );
//                                } catch (IOException e) {
//                                    throw new RuntimeException(e);
//                                } catch (JSONException e) {
//                                    throw new RuntimeException(e);
//                                }

                                if (response.code() == 200) {
                                        ((Login) getActivity()).changeFragment(new fragment_success());
                                    }

                                    else {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getActivity(), "Mã OTP không hợp lệ! vui lòng nhập lại.", Toast.LENGTH_LONG).show();
                                                verify_code_6.clearFocus();
                                                verify_code_1.requestFocus();
                                                verify_code_1.setText("");
                                                verify_code_2.setText("");
                                                verify_code_3.setText("");
                                                verify_code_4.setText("");
                                                verify_code_5.setText("");
                                                verify_code_6.setText("");
                                            }
                                        });

                                    }
                            }



                            @Override
                            public void onFailure(IOException e) {
                                if (getActivity() != null) {
                                    Toast.makeText(getActivity(), "Lỗi kết nối mạng. Vui lòng kiểm tra lại kết nối của bạn.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void verifyOTP(String enteredOTP) {

    }


    private void SetID(View root) {

        timer = root.findViewById(R.id.timer);
        verify_code_1 = root.findViewById(R.id.edt_verify_code_1);
        verify_code_2 = root.findViewById(R.id.edt_verify_code_2);
        verify_code_3 = root.findViewById(R.id.edt_verify_code_3);
        verify_code_4 = root.findViewById(R.id.edt_verify_code_4);
        verify_code_5 = root.findViewById(R.id.edt_verify_code_5);
        verify_code_6 = root.findViewById(R.id.edt_verify_code_6);

    }





}