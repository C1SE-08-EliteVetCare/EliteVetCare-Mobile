package com.example.elitevetcare.Appointment;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.elitevetcare.Activity.ContentView;
import com.example.elitevetcare.Helper.Libs;
import com.example.elitevetcare.Model.ViewModel.BookAppointmentViewModel;
import com.example.elitevetcare.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_select_datetime#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_select_datetime extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_select_datetime() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_select_datetime.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_select_datetime newInstance(String param1, String param2) {
        fragment_select_datetime fragment = new fragment_select_datetime();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    BookAppointmentViewModel bookAppointmentViewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        bookAppointmentViewModel =  new ViewModelProvider(requireActivity()).get(BookAppointmentViewModel.class);
    }
    View Inflater__;
    AppCompatEditText edt_hours, edt_minute;
    AppCompatButton btn_done_select;
    int Lastbtn = -1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Inflater__ = inflater.inflate(R.layout.fragment_select_datetime, container, false);
        SetId();
        SetEvent();
        return Inflater__;
    }

    private void SetId() {
        edt_hours = Inflater__.findViewById(R.id.edt_hours_appointment);
        edt_minute = Inflater__.findViewById(R.id.edt_minute_appointment);
        btn_done_select = Inflater__.findViewById(R.id.btn_done_select_datetime);
    }

    private void SetEvent() {
        SetOnClick();
        edt_hours.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if( !s.toString().isEmpty()){
                    int enteredHour = Integer.parseInt(s.toString());
                    if(enteredHour >= 24){
                        edt_hours.setText("23");
                        Toast.makeText(getContext(), "Không thể nhập quá 23h !!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt_hours.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    // Khi EditText không còn focus
                    String enteredText = edt_hours.getText().toString();
                    if (!enteredText.isEmpty()) {
                        int enteredHour = Integer.parseInt(enteredText);
                        if (enteredHour < 10 ) {
                            edt_hours.setText("0" + enteredHour);
                        }
                    }
                }
            }
        });
        edt_minute.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if( !s.toString().isEmpty()){
                    int enteredHour = Integer.parseInt(s.toString());
                    if(enteredHour >= 60){
                        edt_minute.setText("59");
                        Toast.makeText(getContext(), "Không thể nhập quá 60 phút !!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt_minute.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    // Khi EditText không còn focus
                    String enteredText = edt_minute.getText().toString();
                    if (!enteredText.isEmpty()) {
                        int enteredMinutes = Integer.parseInt(enteredText);
                        if (enteredMinutes < 10 ) {
                            edt_minute.setText("0" + enteredMinutes);
                        }
                    }
                }
            }
        });
        btn_done_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookAppointmentViewModel.getDate().getValue() == null || bookAppointmentViewModel.getDate().getValue() == ""){
                    Toast.makeText(getContext(), "Chưa chọn ngày !!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edt_hours.getText().toString().isEmpty() || edt_minute.getText().toString().isEmpty()){
                    edt_minute.setText("00");
                    Toast.makeText(getContext(), "Hãy chọn thời gian !!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String time = Libs.HandleString(edt_hours.getText().toString() + ":" + edt_minute.getText().toString());
                bookAppointmentViewModel.setTime(time);

                ((ContentView)requireActivity()).setfragment(R.layout.fragment_pet_owner_infor);
            }
        });
    }

    private void SetOnClick() {
        Inflater__.findViewById(R.id.btn_9h).setOnClickListener(this::onSelectTimeButtonClick);
        Inflater__.findViewById(R.id.btn_9h30).setOnClickListener(this::onSelectTimeButtonClick);
        Inflater__.findViewById(R.id.btn_10h).setOnClickListener(this::onSelectTimeButtonClick);
        Inflater__.findViewById(R.id.btn_10h30).setOnClickListener(this::onSelectTimeButtonClick);
        Inflater__.findViewById(R.id.btn_11h).setOnClickListener(this::onSelectTimeButtonClick);
        Inflater__.findViewById(R.id.btn_15h).setOnClickListener(this::onSelectTimeButtonClick);
        Inflater__.findViewById(R.id.btn_15h30).setOnClickListener(this::onSelectTimeButtonClick);
        Inflater__.findViewById(R.id.btn_16h).setOnClickListener(this::onSelectTimeButtonClick);
    }


    public void onSelectTimeButtonClick(View view) {
        // Xử lý sự kiện tại đây
        int buttonId = view.getId();
        if(buttonId == R.id.btn_9h){
            view.setBackgroundResource(R.drawable.custom_button_time_selected);
            ((AppCompatButton)view).setTextColor(getResources().getColor(R.color.white));
        }else
        if(buttonId == R.id.btn_9h30){
            view.setBackgroundResource(R.drawable.custom_button_time_selected);
            ((AppCompatButton)view).setTextColor(getResources().getColor(R.color.white));
        }else
        if(buttonId == R.id.btn_10h){
            view.setBackgroundResource(R.drawable.custom_button_time_selected);
            ((AppCompatButton)view).setTextColor(getResources().getColor(R.color.white));
        }else
        if(buttonId == R.id.btn_10h30){
            view.setBackgroundResource(R.drawable.custom_button_time_selected);
            ((AppCompatButton)view).setTextColor(getResources().getColor(R.color.white));
        }else
        if(buttonId == R.id.btn_11h){
            view.setBackgroundResource(R.drawable.custom_button_time_selected);
            ((AppCompatButton)view).setTextColor(getResources().getColor(R.color.white));
        }else
        if(buttonId == R.id.btn_15h){
            view.setBackgroundResource(R.drawable.custom_button_time_selected);
            ((AppCompatButton)view).setTextColor(getResources().getColor(R.color.white));
        }else
        if(buttonId == R.id.btn_15h30){
            view.setBackgroundResource(R.drawable.custom_button_time_selected);
            ((AppCompatButton)view).setTextColor(getResources().getColor(R.color.white));
        }else
        if(buttonId == R.id.btn_16h){
            view.setBackgroundResource(R.drawable.custom_button_time_selected);
            ((AppCompatButton)view).setTextColor(getResources().getColor(R.color.white));
        }
        ResetButton(buttonId);
        SetTime();
    }

    private void SetTime() {
        String hours = ((AppCompatButton)Inflater__.findViewById(Lastbtn)).getText().toString().split(":")[0];
        String minus = ((AppCompatButton)Inflater__.findViewById(Lastbtn)).getText().toString().split(":")[1].split(" ")[0];
        edt_hours.setText(hours);
        edt_minute.setText(minus);
    }

    private void ResetButton(int buttonId) {
        if(Lastbtn <0){
            Lastbtn = buttonId;
            return;
        }
        AppCompatButton btn = Inflater__.findViewById(Lastbtn);
        btn.setBackgroundResource(R.drawable.custom_button_time);
        btn.setTextColor(getResources().getColor(R.color.blue));
        Lastbtn = buttonId;
    }
}