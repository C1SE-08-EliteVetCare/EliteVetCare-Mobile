package com.example.elitevetcare.Appointment;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.elitevetcare.Activity.ContentView;
import com.example.elitevetcare.Helper.HelperCallingAPI;
import com.example.elitevetcare.Helper.Libs;
import com.example.elitevetcare.Helper.ProgressHelper;
import com.example.elitevetcare.Model.CurrentData.CurrentUser;
import com.example.elitevetcare.Model.ObjectModel.User;
import com.example.elitevetcare.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_pet_owner_infor#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_pet_owner_infor extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_pet_owner_infor() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_pet_owner_infor.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_pet_owner_infor newInstance(String param1, String param2) {
        fragment_pet_owner_infor fragment = new fragment_pet_owner_infor();
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
    }
    AppCompatEditText edt_name, edt_phone;
    AppCompatSpinner spinner_gender, spinner_age;

    AppCompatButton btn_next_Doing;
    User currentUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pet_owner_infor, container, false);
        SetID(root);
        InitData();
        SetEvent();
        // Inflate the layout for this fragment
        return root;
    }

    private void SetEvent() {
        btn_next_Doing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Libs.capitalizeFirstLetter(edt_name.getText().toString());
                String gender = Libs.HandleString(spinner_gender.getSelectedItem().toString());
                String age = Libs.AgeToBirthYear(spinner_age.getSelectedItem().toString());
                String phone = Libs.HandleString(edt_phone.getText().toString());

                if(name.isEmpty()){
                    Toast.makeText(getContext(),"Hãy Nhập Tên !",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(gender.isEmpty()){
                    Toast.makeText(getContext(),"Chưa có giới tính !",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Integer.parseInt(age) < 10 ){
                    Toast.makeText(getContext(),"Tuổi Không hợp lệ !",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(phone.isEmpty()){
                    Toast.makeText(getContext(),"Hãy Nhập Số Điện Thoại !",Toast.LENGTH_SHORT).show();
                    return;
                }

//                Log.d("Infor", currentUser.getFullName() + "\n" + name +currentUser.getFullName().compareTo(name) + "\n" +gender + "\n"+age + "\n"+phone + "\n" + "\n");
                if(currentUser.getFullName().compareTo(name)  != 0 || currentUser.getGender().compareTo(gender) != 0 || currentUser.getBirthYear().compareTo(age) != 0 || currentUser.getPhone().compareTo(phone) != 0)
                {
                    ProgressHelper.showMessageDialog(getActivity(), "Điều này sẽ thay đổi thông tin cá nhân của bạn, bạn đồng ý chứ ?", new ProgressHelper.CallbackMesageDialog() {
                        @Override
                        public void OnAccecpted() {
                            if (!ProgressHelper.isDialogVisible())
                                ProgressHelper.showDialog(getActivity(),"Đang cập nhập dữ liệu !");
                            RequestBody body = new FormBody.Builder()
                                    .add("name", name)
                                    .add("gender", gender)
                                    .add("birthYear", age)
                                    .add("phone", phone)
                                    .build();
                            HelperCallingAPI.CallingAPI_PathParams_Put(HelperCallingAPI.UPDATE_PROFILE_API_PATH, "", body, new HelperCallingAPI.MyCallback() {
                                @Override
                                public void onResponse(Response response) {
                                    if (ProgressHelper.isDialogVisible())
                                        ProgressHelper.dismissDialog();
                                    int statusCode = response.code();
                                    if (statusCode == 200 ){
                                        CurrentUser.CreateInstanceByAPI(currentUser -> {});
                                        ((ContentView)getActivity()).setfragment(R.layout.fragment_select_service);
                                    }else{
                                        Libs.Sendmessage(getActivity(), "Đang Xảy Ra Lỗi Vui Lòng Thử Lại");
                                    }

                                }

                                @Override
                                public void onFailure(IOException e) {
                                    if (ProgressHelper.isDialogVisible())
                                        ProgressHelper.dismissDialog();
                                    Libs.Sendmessage(getActivity(), "Đang Xảy Ra Lỗi Vui Lòng Thử Lại");
                                }
                            });
                        }
                    });
                    return;
                }
                ((ContentView)getActivity()).setfragment(R.layout.fragment_select_service);
            }
        });
    }

    private void InitData() {
        List<String> numbers = IntStream.rangeClosed(1, 99)
                .mapToObj(Integer::toString)
                .collect(Collectors.toList());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, numbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_age.setAdapter(adapter);
        spinner_age.setSelection(Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(currentUser.getBirthYear()) - 1);

        List<String> genderList = new ArrayList<>();
        genderList.add("nam");
        genderList.add("nữ");
        genderList.add("Khác");
        int position = genderList.indexOf(currentUser.getGender());
        if (position != -1) {
            spinner_gender.setSelection(position);
        }

        edt_name.setText(currentUser.getFullName());
        edt_phone.setText(CurrentUser.getCurrentUser().getPhone());
    }

    private void SetID(View root) {
        edt_name = root.findViewById(R.id.edt_user_name);
        edt_phone = root.findViewById(R.id.edt_user_phonenumber);
        spinner_age = root.findViewById(R.id.spinner_user_age);
        spinner_gender = root.findViewById(R.id.spinner_user_gender);
        btn_next_Doing = root.findViewById(R.id.btn_next_Doing);
        currentUser= CurrentUser.getCurrentUser();
    }
}