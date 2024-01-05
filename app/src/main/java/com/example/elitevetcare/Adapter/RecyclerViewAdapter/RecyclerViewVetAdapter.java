package com.example.elitevetcare.Adapter.RecyclerViewAdapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elitevetcare.Activity.ContentView;
import com.example.elitevetcare.Helper.Libs;
import com.example.elitevetcare.Model.CurrentData.CurrentConversationHistory;
import com.example.elitevetcare.Model.CurrentData.CurrentUser;
import com.example.elitevetcare.Model.CurrentData.CurrentVetRecommend;
import com.example.elitevetcare.Model.ObjectModel.Conversation;
import com.example.elitevetcare.Model.ObjectModel.User;
import com.example.elitevetcare.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class RecyclerViewVetAdapter extends RecyclerView.Adapter<RecyclerViewVetAdapter.VetViewHolder>{

    ArrayList<User> ListVet = null;
    Activity CurrentActivity = null;

    public RecyclerViewVetAdapter(Activity activity) {
        CurrentActivity = activity;
        if(CurrentVetRecommend.getListVetRecommend().getValue() != null)
            ListVet = CurrentVetRecommend.getListVetRecommend().getValue();
    }

    public void setFindName(String findName) {
        if(!findName.isEmpty()){
            ArrayList<User> newList = new ArrayList<>();
            for (User vet : CurrentVetRecommend.getListVetRecommend().getValue()){
                if(vet.getFullName().toLowerCase().contains(findName.toLowerCase()))
                    newList.add(vet);
            }
            ListVet = newList;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public VetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_vet,parent, false);
        return new VetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VetViewHolder holder, int position) {
        User Vet = ListVet.get(position);
        Libs.SetImageFromURL(Vet.getAvatar(),holder.img_vet_avatar);
        holder.VetName.setText(Vet.getFullName());
        holder.VetComapany.setText("Bác sĩ tại: " + Vet.getClinic().getName());
        holder.ll_vet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User VetConversation = ListVet.get(holder.getAdapterPosition());
                boolean flag = false;
                Intent intent = new Intent(CurrentActivity.getApplicationContext(), ContentView.class);
                intent.putExtra("FragmentCalling",R.layout.fragment_conversation);
                for(Conversation conversation : CurrentConversationHistory.getConversationHistory()){
                    if(conversation.getCreator().getId() == VetConversation.getId() || conversation.getRecipient().getId() == VetConversation.getId()){
                        intent.putExtra("conversation", conversation);
                        flag = true;
                        break;
                    }
                }
                if (!flag) intent.putExtra("User", VetConversation);
                CurrentActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ListVet.size();
    }

    public class VetViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView img_vet_avatar;
        TextView VetName, VetComapany;
        LinearLayout ll_vet;
        public VetViewHolder(@NonNull View itemView) {
            super(itemView);
            img_vet_avatar = itemView.findViewById(R.id.img_vet_avatar);
            VetName = itemView.findViewById(R.id.txt_vet_Name);
            VetComapany = itemView.findViewById(R.id.txt_vet_Company);
            ll_vet = itemView.findViewById(R.id.ll_vet_conversation);
        }
    }
}
