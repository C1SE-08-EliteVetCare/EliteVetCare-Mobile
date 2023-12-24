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
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elitevetcare.Activity.ContentView;
import com.example.elitevetcare.Activity.Login;
import com.example.elitevetcare.Helper.DataLocalManager;
import com.example.elitevetcare.Helper.HelperCallingAPI;
import com.example.elitevetcare.Helper.Libs;
import com.example.elitevetcare.Helper.ProgressHelper;
import com.example.elitevetcare.Model.CurrentData.CurrentUser;
import com.example.elitevetcare.R;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_user_profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_user_profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_user_profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_user_profile.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_user_profile newInstance(String param1, String param2) {
        fragment_user_profile fragment = new fragment_user_profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    ActivityResultLauncher<String> ImageGallaryResultLauncher;
    ActivityResultLauncher<Intent> takePictureLauncher;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        SetActivityResult();
    }
    Bitmap bitmap_avatar;
    private void SetActivityResult() {
        ImageGallaryResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Bitmap bitmap = Libs.uriToBitmap(result,getContext());
                bitmap_avatar = bitmap;
                img_avatar.setImageBitmap(bitmap);
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
                            bitmap_avatar = bitmap;
                            img_avatar.setImageBitmap(bitmap);
                        }else{
                            Toast.makeText(getContext(),result.getResultCode()+  " : " + RESULT_OK, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    LinearLayout logout_layout;
    TextView txt_username, txt_phonenumber;
    ImageView img_avatar;
    ImageButton btn_change_avatar,btn_accept_change;
    LinearLayout ll_edit_profile;
    boolean isChanging = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_profile, container, false);
        logout_layout = root.findViewById(R.id.logout_layout);
        txt_username = root.findViewById(R.id.user_name);
        txt_phonenumber = root.findViewById(R.id.txt_phonenumber);
        img_avatar = root.findViewById(R.id.img_avatar_user_profile);
        btn_change_avatar = root.findViewById(R.id.btn_change_avatar);
        btn_accept_change = root.findViewById(R.id.btn_accept_change);
        ll_edit_profile = root.findViewById(R.id.ll_edit_profile);

        txt_username.setText(CurrentUser.getCurrentUser().getFullName());
        txt_phonenumber.setText("(+84)"+CurrentUser.getCurrentUser().getPhone().substring(1));
        Libs.SetImageFromURL(CurrentUser.getCurrentUser().getAvatar(),img_avatar);

        ll_edit_profile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getContext(), ContentView.class);
                intent.putExtra("FragmentCalling", R.layout.fragment_edit_profile_user);
                getActivity().startActivity(intent);
                return false;
            }
        });
        btn_change_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageSourcePopup();

            }
        });
        btn_accept_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressHelper.showMessageDialog(getContext(), "Bạn có chắc muốn thay đổi chứ", new ProgressHelper.CallbackMesageDialog() {
                    @Override
                    public void OnAccecpted() {
                        File File_avatar = Libs.BitmapToFile(getActivity().getCacheDir(),bitmap_avatar);
                        if(File_avatar == null){
                            Libs.Sendmessage(getActivity(),"Đang Có Lỗi Vui Lòng Thử Lại");
                            return;
                        }
                        RequestBody body = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("avatar", "user_avatar.jpg", RequestBody.create(File_avatar, MediaType.parse("image/jpg")))
                                .build();
                        HelperCallingAPI.CallingAPI_QueryParams_Post(HelperCallingAPI.UPLOAD_AVATAR_API_PATH, null, body, new HelperCallingAPI.MyCallback() {
                            @Override
                            public void onResponse(Response response) {
                                if(response.isSuccessful()){
                                    Libs.Sendmessage(getActivity(), "Thay Đổi Avatar Thành Công");
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            btn_accept_change.setVisibility(View.INVISIBLE);
                                            btn_change_avatar.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }else{
                                    Log.d("reportinggg",response.code()+"" );

                                    Libs.Sendmessage(getActivity(), "Đã Xảy Ra Lỗi trong quá trình upload!");
                                }

                            }

                            @Override
                            public void onFailure(IOException e) {
                                Libs.Sendmessage(getActivity(), "Kiểm tra lại kết nối mạng");
                            }
                        });

                    }
                });
            }
        });
        logout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataLocalManager.ResetRefreshToken();
                DataLocalManager.ResetAccessTokens();
                Intent intent = new Intent(getContext(), Login.class);
                getActivity().startActivity(intent);
                getActivity().finish();
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
                btn_accept_change.setVisibility(View.VISIBLE);
                btn_change_avatar.setVisibility(View.INVISIBLE);
            }
        });
        builder.show();
    }

    private void PickImageFromGallary() {
        ImageGallaryResultLauncher.launch("image/*");
    }

    private void PickImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureLauncher.launch(takePictureIntent);
    }
}