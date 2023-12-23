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
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.elitevetcare.Helper.Libs;
import com.example.elitevetcare.Model.ViewModel.PetInforViewModel;
import com.example.elitevetcare.R;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_update_pet_infor#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_update_pet_infor extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_update_pet_infor() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_update_pet_infor.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_update_pet_infor newInstance(String param1, String param2) {
        fragment_update_pet_infor fragment = new fragment_update_pet_infor();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Doan Can Them Code*/
    ActivityResultLauncher<String> ImageGallaryResultLauncher;
    ActivityResultLauncher<Intent> takePictureLauncher;
    PetInforViewModel petInforViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        petInforViewModel = new ViewModelProvider(requireActivity()).get(PetInforViewModel.class);

        SetActivityResult();
    }

    private void SetActivityResult() {
        ImageGallaryResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                imageAvatar.setImageURI(result);
                File file = Libs.uriToFile(getActivity(),result);
                petInforViewModel.setAvatar(file);
                Log.d("petavatarGallary", petInforViewModel.getFileImage().getValue().toString());
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
                            imageAvatar.setImageBitmap(bitmap);
                            petInforViewModel.setAvatarByBitmap(getActivity().getFilesDir(),bitmap);
                            Log.d("petavatar", petInforViewModel.getFileImage().getValue().toString());
                        }else{
                            Toast.makeText(getContext(),result.getResultCode()+  " : " + RESULT_OK, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }


    ImageFilterButton btn_add_avatar;
    AppCompatSpinner Sprinner_Species;
    AppCompatEditText edt_breed, edt_nickName, edt_color;
    ImageView imageAvatar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_update_pet_infor, container, false);
        SetID(root);
        SetEvent();
        // Inflate the layout for this fragment
        return root;
    }

    private void SetEvent() {
        btn_add_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageSourcePopup();
            }
        });
    }

    private void SetID(View root) {


        btn_add_avatar = root.findViewById(R.id.btn_add_avatar_pet);
        Sprinner_Species = root.findViewById(R.id.spinner_species_pet_profile);
        edt_breed = root.findViewById(R.id.edt_breed_pet_profile);
        edt_nickName = root.findViewById(R.id.edt_nickName_pet_profile);
        edt_color = root.findViewById(R.id.edt_color_pet_profile);
        imageAvatar = root.findViewById(R.id.img_avatar_pet_profile);
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
    public boolean SendData() {
        petInforViewModel.setSpecies(Sprinner_Species.getSelectedItem().toString());
        petInforViewModel.setBreed(edt_breed.getText().toString());
        petInforViewModel.setNickName(edt_nickName.getText().toString());
        petInforViewModel.setColor(edt_color.getText().toString());
        return true;
    }
    public void PickImageFromGallary() {
        ImageGallaryResultLauncher.launch("image/*");
    }
    private void PickImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureLauncher.launch(takePictureIntent);
    }

}