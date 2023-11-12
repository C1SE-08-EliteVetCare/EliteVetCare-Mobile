package com.example.elitevetcare.Profile;

import static android.app.Activity.RESULT_OK;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.elitevetcare.Activity.ContentView;
import com.example.elitevetcare.Activity.UpdateProfile;
import com.example.elitevetcare.Helper.DataLocalManager;
import com.example.elitevetcare.Helper.HelperCallingAPI;
import com.example.elitevetcare.Helper.Libs;
import com.example.elitevetcare.Helper.ViewModel.PetConditionViewModel;
import com.example.elitevetcare.Helper.ViewModel.PetInforViewModel;
import com.example.elitevetcare.Helper.ViewModel.PetViewModel;
import com.example.elitevetcare.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_update_pet_condition#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_update_pet_condition extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_update_pet_condition() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_update_pet_condition.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_update_pet_condition newInstance(String param1, String param2) {
        fragment_update_pet_condition fragment = new fragment_update_pet_condition();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    ActivityResultLauncher<String> ImageGallaryResultLauncher;
    ActivityResultLauncher<Intent> takePictureLauncher;
    PetConditionViewModel petConditionViewModel;
    PetViewModel petViewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        petConditionViewModel = new ViewModelProvider(requireActivity()).get(PetConditionViewModel.class);
        petViewModel = new ViewModelProvider(requireActivity()).get(PetViewModel.class);
        SetActivityResult();
    }
    private void SetActivityResult() {
        ImageGallaryResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                img_pet_condition.setImageURI(result);
                File file = Libs.uriToFile(getActivity(),result);
                petConditionViewModel.setAvatar(file);
            }
        });

        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Toast.makeText(getContext(), "true", Toast.LENGTH_SHORT).show();
                            // Xử lý ảnh đã chụp
                            Bundle extras = result.getData().getExtras();
                            Bitmap bitmap = (Bitmap) extras.get("data");
                            img_pet_condition.setImageBitmap(bitmap);
                            petConditionViewModel.setAvatarByBitmap(getActivity().getCacheDir(),bitmap);
                        }else{
                            Toast.makeText(getContext(),result.getResultCode()+  " : " + RESULT_OK, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }
    AppCompatEditText edt_portion, edt_weight, edt_meal, edt_manifestation, edt_conditionOfDefecation;
    ImageFilterView img_pet_condition;
    ImageButton img_add_img;
    AppCompatButton btn_Update;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_update_pet_condition, container, false);
        edt_portion = root.findViewById(R.id.edt_khauphanan);
        edt_weight = root.findViewById(R.id.edt_cannang);
        edt_meal = root.findViewById(R.id.edt_thucan);
        edt_manifestation = root.findViewById(R.id.edt_trangthai);
        edt_conditionOfDefecation = root.findViewById(R.id.edt_tinhtrang);
        img_pet_condition = root.findViewById(R.id.img_pet_condition);
        img_add_img = root.findViewById(R.id.btn_img_condition);
        btn_Update = root.findViewById(R.id.btn_done_Update_condition);
        img_add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageSourcePopup();
            }
        });
        btn_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestBody body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("portion", edt_portion.getText().toString())
                        .addFormDataPart("weight", edt_weight.getText().toString())
                        .addFormDataPart("meal", edt_meal.getText().toString())
                        .addFormDataPart("manifestation", edt_manifestation.getText().toString())
                        .addFormDataPart("conditionOfDefecation",edt_conditionOfDefecation.getText().toString())
                        .addFormDataPart("actualImg", "pet_avatar.jpg", RequestBody.create(petConditionViewModel.getFileImage().getValue(), MediaType.parse("image/png")))
                        .build();
                Log.d("UpdateCondition", "portion " + edt_portion.getText().toString());
                Log.d("UpdateCondition", "edt_weight " + edt_weight.getText().toString());
                Log.d("UpdateCondition", "edt_meal " + edt_meal.getText().toString());
                Log.d("UpdateCondition", "edt_manifestation " + edt_manifestation.getText().toString());
                Log.d("UpdateCondition", "edt_conditionOfDefecation " + edt_conditionOfDefecation.getText().toString());
                Log.d("UpdateCondition", "fileimgae " + petConditionViewModel.getFileImage().getValue().toString());
//                        if (DataLocalManager.getInstance() != null && DataLocalManager.GetAccessTokenTimeUp() < System.currentTimeMillis()) {
//                            if(HelperCallingAPI.RefreshTokenCalling()!= true)
//                                return;
//                        }
                HelperCallingAPI.CallingAPI_PathParams_Put(HelperCallingAPI.PET_CONDITION_PATH, String.valueOf(petViewModel.getCurrentPet().getId()), body, new HelperCallingAPI.MyCallback() {
                    @Override
                    public void onResponse(Response response) {
                        int statusCode = response.code();
                        JSONObject data = null;
                        if(statusCode == 200) {
                            try {
                                data = new JSONObject(response.body().string());
                                JSONObject finalData = data;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((ContentView)getActivity()).setfragment(R.layout.fragment_tracking_pet_health);
                                        try {
                                            Toast.makeText(getContext(), finalData.getString("message"), Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                });
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
    private void showImageSourcePopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Chọn nguồn ảnh");
        builder.setItems(new CharSequence[]{"Máy ảnh", "Thư viện ảnh"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) { // Chọn máy ảnh
                    PickImageFromCamera();
                } else { // Chọn từ thư viện
                    PickImageFromGallary();
                }
            }
        });
        builder.show();
    }
    public void PickImageFromGallary() {
        ImageGallaryResultLauncher.launch("image/*");
    }
    private void PickImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureLauncher.launch(takePictureIntent);
    }
}