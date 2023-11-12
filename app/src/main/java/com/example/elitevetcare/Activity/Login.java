package com.example.elitevetcare.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elitevetcare.Authentication.fragment_login;
import com.example.elitevetcare.Authentication.fragment_signup;
import com.example.elitevetcare.Helper.DataLocalManager;
import com.example.elitevetcare.Helper.HelperCallingAPI;
import com.example.elitevetcare.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends AppCompatActivity {

    FragmentContainerView frm_container_view;
    FloatingActionButton btn_google_login;
    TextView txt_changing, txt_title_form;
    FragmentManager fragmentManager = getSupportFragmentManager();
    AppCompatActivity btn_next_register;
    float v = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        DataLocalManager.init(getApplicationContext());
        if(DataLocalManager.GetRefreshToken() != null && DataLocalManager.GetRefreshTokenTimeUp() > System.currentTimeMillis()){
            AutoLogin();
            return;
        }
        SettingID();
        setContentForm();
        CallingAnimate();
        setEvent();
    }

    private void AutoLogin() {
        if(!HelperCallingAPI.RefreshTokenCalling())
            return;
        Intent intent = new Intent(Login.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setEvent() {
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