package com.example.elitevetcare.QuestionAndAnswer;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
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
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.elitevetcare.Activity.ContentView;
import com.example.elitevetcare.Adapter.RecyclerViewAdapter.RecyclerViewMessageAdapter;
import com.example.elitevetcare.Helper.HelperCallingAPI;
import com.example.elitevetcare.Helper.Libs;
import com.example.elitevetcare.Helper.SocketGate;
import com.example.elitevetcare.Interface.SocketOnMessageListener;
import com.example.elitevetcare.Model.CurrentData.CurrentConversationHistory;
import com.example.elitevetcare.Model.CurrentData.CurrentUser;
import com.example.elitevetcare.Model.ObjectModel.Conversation;
import com.example.elitevetcare.Model.ObjectModel.Message;
import com.example.elitevetcare.Model.ViewModel.MessageViewModel;
import com.example.elitevetcare.R;
import com.github.mikephil.charting.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_conversation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_conversation extends Fragment implements SocketOnMessageListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_conversation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_conversation.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_conversation newInstance(String param1, String param2) {
        fragment_conversation fragment = new fragment_conversation();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SocketGate.removeSocketEventListener(this);
    }

    MessageViewModel messageViewModel;
    ActivityResultLauncher<String> ImageGallaryResultLauncher;
    ActivityResultLauncher<Intent> takePictureLauncher;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        ImageGallaryResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if(result == null)
                    return;
                Bitmap bitmap = Libs.uriToBitmap(result,getContext());
                SendImageMessage(bitmap);
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
                            SendImageMessage(bitmap);
                        }else{
                            Toast.makeText(getContext(),result.getResultCode()+  " : " + RESULT_OK, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        messageViewModel = new ViewModelProvider(getActivity()).get(MessageViewModel.class);
        SocketGate.addSocketEventListener(this);
    }

    private void SendImageMessage(Bitmap bitmap) {
        int conversationID = messageViewModel.getConversationID().getValue();
        Message message = new Message(-1, "", bitmap,"",CurrentUser.getCurrentUser(), conversationID, Calendar.getInstance().getTime());
        messageViewModel.getMessageArray().getValue().add(message);
        UpdateUI();
        File File_avatar = Libs.BitmapToFile(getActivity().getCacheDir(), bitmap);
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("conversationId", conversationID +"")
                .addFormDataPart("img", "message_content.jpg", RequestBody.create(File_avatar, MediaType.parse("image/jpg")))
                .build();
        HelperCallingAPI.CallingAPI_QueryParams_Post(HelperCallingAPI.SEND_IMAGE_MESSAGE_CONVERSATION, null, body, new HelperCallingAPI.MyCallback() {
            @Override
            public void onResponse(Response response) {
                if(response.isSuccessful()){
                    Libs.Sendmessage(getActivity(), "Gửi Ảnh Thành Công");
                }else {
                    Libs.Sendmessage(getActivity(), "Gửi Ảnh Thất Bại");
                    messageViewModel.removeMessage(message);
                }

            }

            @Override
            public void onFailure(IOException e) {
                Libs.Sendmessage(getActivity(), "Gửi Ảnh Thất Bại");
                listMessage.remove(message);
                UpdateUI();
            }
        });
    }

    RecyclerView Message_recycler_view;
    RecyclerViewMessageAdapter messageAdapter;
    ProgressBar progressBar;
    ArrayList<Message> listMessage;
    ImageFilterButton btn_send_message, btn_send_image;
    AppCompatEditText edt_input_message;

    LinearLayout ll_inputsend;
   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_conversation, container, false);
        progressBar = root.findViewById(R.id.progressBar);
        Message_recycler_view = root.findViewById(R.id.recycler_message);
        btn_send_message = root.findViewById(R.id.btn_send_message);
        edt_input_message = root.findViewById(R.id.edt_input_message);
        btn_send_image = root.findViewById(R.id.btn_send_image);


       btn_send_image.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               showImageSourcePopup();
           }
       });
        btn_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageContent = edt_input_message.getText().toString();
                int conversationID = messageViewModel.getConversationID().getValue();
                if(messageContent.isEmpty() == true){
                    {
                        Toast.makeText(getContext(),"Nhập Tin Nhắn",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if(conversationID < 0 )
                    return;

                edt_input_message.setText("");
                Message message = new Message(-1, messageContent, CurrentUser.getCurrentUser(), conversationID, Calendar.getInstance().getTime());
                messageViewModel.AddNewMessage(message);
                UpdateUI();
                RequestBody body = new FormBody.Builder()
                        .add("content", messageContent)
                        .add("conversationId",conversationID+"")
                        .build();
                HelperCallingAPI.CallingAPI_QueryParams_Post(HelperCallingAPI.SEND_MESSAGE_CONVERSATION, null, body, new HelperCallingAPI.MyCallback() {
                    @Override
                    public void onResponse(Response response) {
                        if(response.isSuccessful()){
                            Libs.Sendmessage(getActivity(), "Gửi Tin Nhắn Thành Công");
                        }else {
                            Libs.Sendmessage(getActivity(), "Gửi Tin Nhắn Thất Bại");
                            messageViewModel.getMessageArray().getValue().remove(message);
                        }

                    }

                    @Override
                    public void onFailure(IOException e) {
                        Libs.Sendmessage(getActivity(), "Gửi Tin Nhắn Thất Bại");
                        messageViewModel.removeMessage(message);
                    }
                });
            }
        });

        messageViewModel.isLoading().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(messageViewModel.isLoading().getValue() == false){
                    progressBar.setVisibility(View.GONE);
                    Message_recycler_view.setVisibility(View.VISIBLE);
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    Message_recycler_view.setVisibility(View.INVISIBLE);
                }
            }
        });

        messageViewModel.getMessageArray().observe(getActivity(), new Observer<ArrayList<Message>>() {
            @Override
            public void onChanged(ArrayList<Message> messages) {
                UpdateUI();
            }
        });
        // Inflate the layout for this fragment
        return root;
    }

    private void UpdateUI() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(listMessage == null){
                    listMessage = messageViewModel.getMessageArray().getValue();
                    Collections.reverse(listMessage);
                    if(listMessage != null){
                        messageAdapter = new RecyclerViewMessageAdapter(listMessage);
                        Message_recycler_view.setAdapter(messageAdapter);
                        Message_recycler_view.scrollToPosition(listMessage.size()-1);
                    }
                }else{
                    listMessage = messageViewModel.getMessageArray().getValue();
                    messageAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    @Override
    public void onMessageListener(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            Gson gson = new Gson();
            Message NewMessage = gson.fromJson(jsonObject.get("message").toString(), new TypeToken<Message>(){}.getType());
            if(NewMessage.getAuthor().getId() != CurrentUser.getCurrentUser().getId()){
                messageViewModel.AddNewMessage(NewMessage);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
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

    private void PickImageFromGallary() {
        ImageGallaryResultLauncher.launch("image/*");
    }

    private void PickImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureLauncher.launch(takePictureIntent);
    }
}