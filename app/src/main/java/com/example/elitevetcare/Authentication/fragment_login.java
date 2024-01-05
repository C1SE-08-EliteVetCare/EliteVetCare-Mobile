package com.example.elitevetcare.Authentication;


import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.app.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.biometric.FingerprintDialogFragment;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.elitevetcare.Activity.Login;
import com.example.elitevetcare.Helper.Libs;
import com.example.elitevetcare.Helper.ProgressHelper;
import com.example.elitevetcare.Model.CurrentData.CurrentUser;
import com.example.elitevetcare.Helper.DataLocalManager;
import com.example.elitevetcare.Helper.HelperCallingAPI;
import com.example.elitevetcare.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.Executor;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_login extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private LinearLayout group_login;

    public fragment_login() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_login.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_login newInstance(String param1, String param2) {
        fragment_login fragment = new fragment_login();
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
        }
    }

    AppCompatButton btn_login;
    ImageFilterButton btn_fingerPrint_login;
    AppCompatEditText edt_email, edt_password;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        SetID(root);
        InitData();
        SetAnimator();
        SetEvent();
        // Inflate the layout for this fragment
        return root;
    }

    private void InitData() {
        if(DataLocalManager.GetEmailUser() != null && DataLocalManager.GetEmailUser() != "")
            edt_email.setText(DataLocalManager.GetEmailUser());
    }
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private void SetEvent() {
        btn_fingerPrint_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BiometricManager biometricManager = BiometricManager.from(getContext());
                switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
                    case BiometricManager.BIOMETRIC_SUCCESS:
                        biometricPrompt.authenticate(promptInfo);
                        break;
                    case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                        Libs.Sendmessage(getActivity(),"Thiết bị không có tính nâng vân tay");
                        break;
                    case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                        Libs.Sendmessage(getActivity(),"Thiết bị tắt tính nâng vân tay");
                        break;
                    case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                        Libs.Sendmessage(getActivity(),"Thiết bị chưa đăng ký vân tay");
                        break;
                }
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = String.valueOf(edt_email.getText());
                String password = String.valueOf(edt_password.getText());
                if(email.isEmpty()){
                    Libs.Sendmessage(getActivity(),"Vui lòng nhập email !");
                    return;
                }
                if(password.isEmpty()){
                    Libs.Sendmessage(getActivity(),"Vui lòng nhập mật khẩu !");
                    return;
                }
                ProgressHelper.showDialog(requireActivity(),"Đang đăng nhập");
                RequestBody LoginBody = new FormBody.Builder()
                        .add("email", email)
                        .add("password",password)
                        .build();
                HelperCallingAPI.CallingAPI_noHeader(HelperCallingAPI.LOGIN_PATH, LoginBody, new HelperCallingAPI.MyCallback() {
                    @Override
                    public void onResponse(Response response){
                        int statusCode = response.code();
                        JSONObject data = null;
                        if(statusCode == 200) {
                            try {
                                data = new JSONObject(response.body().string());
                                String AccessToken = data.getString("accessToken");
                                String RefreshToken = data.getString("refreshToken");
                                DataLocalManager.setAccessTokens(AccessToken);
                                DataLocalManager.setRefreshToken(RefreshToken);
                                DataLocalManager.SetEmailUser(email);
                                CurrentUser.CreateInstanceByAPI(new CurrentUser.UserCallback() {
                                    @Override
                                    public void onUserGeted(CurrentUser currentUser) {
                                        if(ProgressHelper.isDialogVisible())
                                            ProgressHelper.dismissDialog();
                                        ((Login)getActivity()).RedictToMainAction();
                                    }
                                });
                            } catch (JSONException | IOException e) {
                                throw new RuntimeException(e);
                            }
                        }else if(statusCode == 401){
                            try {
                                data = new JSONObject(response.body().string());
                                if(ProgressHelper.isDialogVisible())
                                    ProgressHelper.dismissDialog();
                                Libs.Sendmessage(getActivity(), data.getString("message"));

                            }catch (JSONException | IOException e){

                            }
                        }else if (statusCode == 400){
                            try {
                                data = new JSONObject(response.body().string());
                                if(ProgressHelper.isDialogVisible())
                                    ProgressHelper.dismissDialog();
                                JSONArray jsonArray = data.getJSONArray("message");
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                if(jsonObject.has("password"))
                                    Libs.Sendmessage(getActivity(), jsonObject.getString("password"));
                                else if (jsonObject.has("email"))
                                    Libs.Sendmessage(getActivity(), jsonObject.getString("email"));

                            }catch (JSONException | IOException e){

                            }
                        }
                    }

                    @Override
                    public void onFailure(IOException e) {
                        Activity activity = getActivity();
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),"false",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });
    }

    private void SetAnimator() {
        group_login.setTranslationX(800);
        //animation
        group_login.animate().translationX(0).alpha(1).setDuration(1200).setStartDelay(1000).start();
    }

    private void SetID(View root) {

        group_login = root.findViewById(R.id.group_login);
        btn_login = root.findViewById(R.id.btn_login);
        btn_fingerPrint_login = root.findViewById(R.id.btn_fingerPrint_login);
        edt_email = root.findViewById(R.id.edt_email_login);
        edt_password = root.findViewById(R.id.edt_password_login);

        executor = getContext().getMainExecutor();
        biometricPrompt = new BiometricPrompt(getActivity(),
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getContext(),
                                "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                if(DataLocalManager.GetEmailUser() == null ||DataLocalManager.GetEmailUser() == ""){
                    Libs.Sendmessage(getActivity(),"chưa từng đăng nhập account không thể đăng nhập bằng vân tay!");
                    return;
                }
                Log.d("NAMEEMAIL", edt_email.getText().toString() +"\n" + DataLocalManager.GetEmailUser());
                if(!DataLocalManager.GetEmailUser().equals(edt_email.getText().toString())){
                    Libs.Sendmessage(getActivity(),"tài khoản này chưa từng đăng nhập trên thiết bị! ");
                    return;
                }
                if(!ProgressHelper.isDialogVisible())
                    ProgressHelper.showDialog(getActivity(), "Đang Đăng Nhập Vui Lòng Đợi!");
                RequestBody body = new FormBody.Builder()
                        .add("email",DataLocalManager.GetEmailUser())
                        .add("password", Libs.generatePassword(10))
                        .add("fullName","")
                        .build();
                HelperCallingAPI.CallingAPI_noHeader(HelperCallingAPI.GOOGLE_LOGIN, body, new HelperCallingAPI.MyCallback() {
                    @Override
                    public void onResponse(Response response) {
                        int responeStatus = response.code();
                        if (responeStatus == 201){
                            SetData(response);
                            CurrentUser.CreateInstanceByAPI(new CurrentUser.UserCallback() {
                                @Override
                                public void onUserGeted(CurrentUser currentUser) {
                                    if(ProgressHelper.isDialogVisible())
                                        ProgressHelper.dismissDialog();
                                    ((Login)getActivity()).RedictToMainAction();
                                    Libs.Sendmessage(getActivity(),"Đăng Nhập Thành Công");
                                }
                            });
                        } else if (responeStatus == 200) {
                            SetData(response);
                            CurrentUser.CreateInstanceByAPI(new CurrentUser.UserCallback() {
                                @Override
                                public void onUserGeted(CurrentUser currentUser) {
                                    if(ProgressHelper.isDialogVisible())
                                        ProgressHelper.dismissDialog();
                                    ((Login)getActivity()).RedictToMainAction();
                                    Libs.Sendmessage(getActivity(),"Đăng Nhập Thành Công");
                                }
                            });
                        }else {
                            Libs.Sendmessage(getActivity(),"Thất Bại");
                        }
                    }

                    @Override
                    public void onFailure(IOException e) {

                    }
                });
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getContext(), "Authentication failed",
                                Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Sử dụng vân tay để đăng nhập !")
                .setSubtitle("Vui lòng chạm tay vào thiết bị cảm biến !!")
                .setNegativeButtonText("Use account password")
                .build();
    }
    private static void SetData(Response response) {
        JSONObject data = null;
        try {
            data = new JSONObject(response.body().string());
            String AccessToken = data.getString("accessToken");
            String RefreshToken = data.getString("refreshToken");
            DataLocalManager.setAccessTokens(AccessToken);
            DataLocalManager.setRefreshToken(RefreshToken);
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}