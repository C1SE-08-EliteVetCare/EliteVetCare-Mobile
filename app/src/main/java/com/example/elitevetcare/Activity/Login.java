package com.example.elitevetcare.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.elitevetcare.Authentication.fragment_login;
import com.example.elitevetcare.Authentication.fragment_signup;
import com.example.elitevetcare.Model.CurrentUser;
import com.example.elitevetcare.Helper.DataLocalManager;
import com.example.elitevetcare.Helper.HelperCallingAPI;
import com.example.elitevetcare.Helper.Libs;
import com.example.elitevetcare.R;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends AppCompatActivity {
    private static final int REQ_ONE_TAP = 2;
    private boolean showOneTapUI = true;
    FragmentContainerView frm_container_view;
    FloatingActionButton btn_google_login;
    TextView txt_changing, txt_title_form;
    FragmentManager fragmentManager = getSupportFragmentManager();
    AppCompatActivity btn_next_register;
    private SignInClient oneTapClient;
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInOptions googleSignInOptions;
    private BeginSignInRequest signUpRequest;
    ActivityResultLauncher<IntentSenderRequest> activityResultLauncher;
    float v = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        DataLocalManager.init(getApplicationContext());
        if(DataLocalManager.GetRefreshToken() != null && DataLocalManager.GetRefreshTokenTimeUp() > System.currentTimeMillis()){
            if(AutoLogin())
                return;
        }
        SettingID();
        setContentForm();
        CallingAnimate();
        setEvent();
    }

    private boolean AutoLogin() {
        if(!HelperCallingAPI.RefreshTokenCalling() )
            return false;
        CurrentUser.CreateInstanceByAPI(new CurrentUser.UserCallback() {
            @Override
            public void onUserGeted(CurrentUser currentUser) {
                RedictToMainAction();
            }
        });
        return true;
    }

    public void RedictToMainAction() {
        Intent intent = new Intent(Login.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setEvent() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK){
                    try {
                        SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(result.getData());
                        String idToken = credential.getGoogleIdToken();
                        String username = credential.getId();
                        String FullName = credential.getDisplayName();
                        if (idToken !=  null) {
                            RequestBody body = new FormBody.Builder()
                                    .add("email",username)
                                    .add("password", Libs.generatePassword(10))
                                    .add("fullName",FullName)
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
                                                RedictToMainAction();
                                                Libs.Sendmessage(Login.this,"Tạo Tài Khoản với Google Thành Công");
                                            }
                                        });
                                    } else if (responeStatus == 200) {
                                        SetData(response);
                                        CurrentUser.CreateInstanceByAPI(new CurrentUser.UserCallback() {
                                            @Override
                                            public void onUserGeted(CurrentUser currentUser) {
                                                RedictToMainAction();
                                                Libs.Sendmessage(Login.this,"Đăng Nhập với Google Thành Công");
                                            }
                                        });
                                    }else {
                                        Libs.Sendmessage(Login.this,"Thất Bại");
                                    }
                                }

                                @Override
                                public void onFailure(IOException e) {

                                }
                            });
                        }
                    } catch (ApiException e) {
                    }
                }
            }
        });

        txt_changing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragmentManager.findFragmentById(frm_container_view.getId()) instanceof fragment_login){
                    changeFragment(new fragment_signup());
                    title_animation("Đăng Ký");
                }
                else{
                    changeFragment(new fragment_login());
                    title_animation("Đăng Nhập");
                }
            }
        });

        btn_google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                googleSignInClient = GoogleSignIn.getClient(getApplicationContext(),googleSignInOptions);
//                activityResultLauncher.launch(googleSignInClient.getSignInIntent());
                oneTapClient.beginSignIn(signUpRequest)
                        .addOnSuccessListener(Login.this, new OnSuccessListener<BeginSignInResult>() {
                            @Override
                            public void onSuccess(BeginSignInResult result) {
                                IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build();
                                activityResultLauncher.launch(intentSenderRequest);
                            }
                        })
                        .addOnFailureListener(Login.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("GoogleFailure", e.getLocalizedMessage());
                            }
                        });
            }
        });
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

    private void title_animation(String text) {
        txt_title_form.animate().alpha(0).setDuration(500).setStartDelay(0).withEndAction(() -> {
                    txt_title_form.setText(text); // Thay đổi văn bản
                    txt_title_form.animate()
                            .alpha(1) // Đặt độ trong suốt (opacity) thành 1
                            .setDuration(500) // Đặt thời gian animation
                            .start(); // Bắt đầu animation
                })
                .start();
//        ((ViewGroup)txt_changing.getParent()).removeView(txt_changing);
    }

    private void CallingAnimate() {
        btn_google_login.setAlpha(v);
        txt_changing.setAlpha(v);
        txt_changing.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(0).start();
        btn_google_login.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(200).start();
    }

    private void SettingID() {
        txt_title_form = findViewById(R.id.txt_title_form_login);
        btn_google_login = findViewById(R.id.btn_google_login);
        frm_container_view = findViewById(R.id.frm_view_login);
        txt_changing = findViewById(R.id.txt_click_changing);

        // For Google
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        oneTapClient = Identity.getSignInClient(this);
        signUpRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.google_web_server))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .setAutoSelectEnabled(true)
                .build();
    }

    private void setContentForm() {
        txt_changing.setTranslationY(300);
        btn_google_login.setTranslationY(300);
    }

    public void changeFragment(Fragment newFragment){
        fragmentManager.beginTransaction()
                .replace(frm_container_view.getId(), newFragment, newFragment.getClass().getSimpleName()).addToBackStack(null).commit();
    }
}