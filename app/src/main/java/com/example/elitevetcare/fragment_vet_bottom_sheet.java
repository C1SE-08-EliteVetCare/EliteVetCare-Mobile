package com.example.elitevetcare;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elitevetcare.Adapter.RecyclerViewAdapter.RecyclerViewVetAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_vet_bottom_sheet, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_recommend_Vet);
        progressBar = view.findViewById(R.id.progress_bar_vet_loading);
        vetAdapter = new RecyclerViewVetAdapter();
        recyclerView.setAdapter(vetAdapter);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

            }
        }, 5000); // 5 giây

    }
}