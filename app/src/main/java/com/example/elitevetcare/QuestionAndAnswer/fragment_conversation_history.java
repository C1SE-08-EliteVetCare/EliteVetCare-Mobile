package com.example.elitevetcare.QuestionAndAnswer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.elitevetcare.Adapter.RecyclerViewAdapter.RecyclerViewConversationHistoryAdapter;
import com.example.elitevetcare.Helper.SocketGate;
import com.example.elitevetcare.Interface.SocketOnMessageListener;
import com.example.elitevetcare.Model.CurrentData.CurrentConversationHistory;
import com.example.elitevetcare.Model.CurrentData.CurrentUser;
import com.example.elitevetcare.Model.ObjectModel.Conversation;
import com.example.elitevetcare.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_conversation_history#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_conversation_history extends Fragment implements SocketOnMessageListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_conversation_history() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_chatbot_list.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_conversation_history newInstance(String param1, String param2) {
        fragment_conversation_history fragment = new fragment_conversation_history();
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        SocketGate.addSocketEventListener(this);
    }
    FloatingActionButton new_conversation_btn;
    RecyclerView ConversationHistory_recycler_view;
    RecyclerViewConversationHistoryAdapter ConversationHistory_adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_conversation_history, container, false);
        SetID(root);
        if(CurrentUser.getCurrentUser().getRole().getId() == 3)
            new_conversation_btn.setVisibility(View.INVISIBLE);
        new_conversation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_vet_bottom_sheet bottomSheetFragment = new fragment_vet_bottom_sheet();
                bottomSheetFragment.show(getActivity().getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });
        SetData();

        // Inflate the layout for this fragment
        return root;
    }

    private void SetData() {
        if(CurrentConversationHistory.getConversationHistory() == null)
            CurrentConversationHistory.CreateInstanceByAPI(new CurrentConversationHistory.UserCallback() {
                @Override
                public void onGetSuccess() {
                    UpdateUI();
                }
            });
        else {
            if(CurrentConversationHistory.isIsLoad() == true){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (CurrentConversationHistory.isIsLoad()){
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        UpdateUI();
                    }
                });
            }else
                UpdateUI();

        }
    }

    private void SetID(View root) {
        new_conversation_btn = root.findViewById(R.id.QA_FloatingActionButton);
        ConversationHistory_recycler_view = root.findViewById(R.id.recycler_conversation_history);
    }

    private void UpdateUI() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(ConversationHistory_adapter == null){
                    ConversationHistory_adapter = new RecyclerViewConversationHistoryAdapter(getActivity());
                    ConversationHistory_recycler_view.setAdapter(ConversationHistory_adapter);
                }else {
                    ConversationHistory_adapter.resetData();
                }

            }
        });
    }
    @Override
    public void onMessageListener(String message, int Code) {
        if(Code != SocketGate.MESSAGE_EVENT_CODE)
            return;
        UpdateUI();
    }

    int isExist(int id){
        ArrayList<Conversation> list = CurrentConversationHistory.getConversationHistory();
        for (int i = 0; i < list.size(); i++) {
            Conversation obj = list.get(i);
            if (obj.getId() == id) {
                return i;
            }
        }
        return -1;
    }

}