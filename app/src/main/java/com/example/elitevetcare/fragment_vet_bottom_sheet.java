package com.example.elitevetcare;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elitevetcare.Adapter.RecyclerViewAdapter.RecyclerViewVetAdapter;
import com.example.elitevetcare.Helper.HelperCallingAPI;
import com.example.elitevetcare.Helper.Libs;
import com.example.elitevetcare.Model.CurrentData.CurrentUser;
import com.example.elitevetcare.Model.CurrentData.CurrentVetRecommend;
import com.example.elitevetcare.Model.ObjectModel.User;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;

public class fragment_vet_bottom_sheet extends BottomSheetDialogFragment {

    public fragment_vet_bottom_sheet() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
    RecyclerView recyclerView;
    RecyclerViewVetAdapter vetAdapter;
    ProgressBar progressBar;
    AppCompatEditText edt_name_of_vet;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_vet_bottom_sheet, container, false);
        recyclerView = root.findViewById(R.id.recycler_recommend_Vet);
        progressBar = root.findViewById(R.id.progress_bar_vet_loading);
        edt_name_of_vet = root.findViewById(R.id.edt_name_of_vet);
        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (CurrentVetRecommend.getListVetRecommend().getValue() == null) {
            CurrentVetRecommend.CreateInstanceByAPI(new CurrentVetRecommend.ListCallBack() {
                @Override
                public void onListGeted(CurrentVetRecommend currentUser) {
                    if (getActivity() == null)
                        return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("Runalbe", "Đã Đến bước cuối");
                            vetAdapter = new RecyclerViewVetAdapter(getActivity());
                            recyclerView.setAdapter(vetAdapter);
                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    });
                }
            });
        } else {
            vetAdapter = new RecyclerViewVetAdapter(getActivity());
            recyclerView.setAdapter(vetAdapter);
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        edt_name_of_vet.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edt_name_of_vet.getWindowToken(), 0);
                    edt_name_of_vet.clearFocus();
                    String findName = edt_name_of_vet.getText().toString();
                    if (!findName.isEmpty()) {
                        if(vetAdapter == null){
                            Libs.Sendmessage(getActivity(), "Đang lấy dữ liệu hãy thử lại sau!");
                            return true;
                        }
                        progressBar.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                        Map<String, String> QuerryParam = new HashMap<>();
                        QuerryParam.put("search", findName + "");
                        HelperCallingAPI.CallingAPI_QueryParams_Get(HelperCallingAPI.FIND_VET_API_PATH, QuerryParam, new HelperCallingAPI.MyCallback() {
                            @Override
                            public void onResponse(Response response) {
                                JSONArray data = null;
                                if (response.isSuccessful()) {
                                    try {
                                        data = new JSONArray(response.body().string());
                                        Gson gson = new Gson();
                                        ArrayList<User> user = gson.fromJson(data.toString(), new TypeToken<ArrayList<User>>(){}.getType());
                                        CurrentVetRecommend.AddCurrentList(user);
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                vetAdapter.setFindName(findName);
                                                progressBar.setVisibility(View.GONE);
                                                recyclerView.setVisibility(View.VISIBLE);
                                            }
                                        });
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }

                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Libs.Sendmessage(getActivity(), "Đã Có Lỗi Xảy ra!");
                                }
                            }

                            @Override
                            public void onFailure(IOException e) {
                                progressBar.setVisibility(View.GONE);
                                Libs.Sendmessage(getActivity(), "Đã Có Lỗi Xảy ra!");
                            }
                        });
                    }

                    return true;
                }
                return false;
            }
        });
    }
}