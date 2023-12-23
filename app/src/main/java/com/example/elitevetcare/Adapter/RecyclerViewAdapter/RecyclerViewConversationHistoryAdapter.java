package com.example.elitevetcare.Adapter.RecyclerViewAdapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
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
import com.example.elitevetcare.Model.ObjectModel.Conversation;
import com.example.elitevetcare.Model.ObjectModel.User;
import com.example.elitevetcare.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class RecyclerViewConversationHistoryAdapter extends RecyclerView.Adapter<RecyclerViewConversationHistoryAdapter.ConversationHistoryViewHolder>{

    Activity activity;
    public RecyclerViewConversationHistoryAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ConversationHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation_history, parent, false);
        return new ConversationHistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationHistoryViewHolder holder, int position) {
        Conversation conversation = CurrentConversationHistory.getConversationHistory().get(position);
        User Recipient = null;
        String messageContent = "";
        if(conversation.getRecipient().getId() != CurrentUser.getCurrentUser().getId()){
            Recipient = conversation.getRecipient();
            messageContent += conversation.getLastMessageSent().getAuthor().getFullName() + ": " + conversation.getLastMessageSent().getContent();
        }

        else{
            Recipient = conversation.getCreator();
            messageContent += "Bạn: " + conversation.getLastMessageSent().getContent();
        }

        if(messageContent.length() > 30)
            messageContent = messageContent.substring(0,27) +"...";
        Libs.SetImageFromURL(Recipient.getAvatar(),holder.img_avatar_recipient);
        holder.name_of_recipient.setText(Recipient.getFullName());
        holder.txt_time_of_last_message.setText(Libs.calculateDateDifference(conversation.getUpdatedAt(),Calendar.getInstance().getTime()));
        holder.txt_last_message_content.setText(messageContent);
        holder.ll_conversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getApplicationContext(),ContentView.class);
                intent.putExtra("FragmentCalling",R.layout.fragment_conversation);
                intent.putExtra("conversation", CurrentConversationHistory.getConversationHistory().get(holder.getAdapterPosition()));
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return CurrentConversationHistory.getConversationHistory().size();
    }

    public class ConversationHistoryViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_conversation;
        RoundedImageView img_avatar_recipient;
        TextView name_of_recipient, txt_last_message_content, txt_time_of_last_message;
        public ConversationHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            img_avatar_recipient = itemView.findViewById(R.id.img_avatar_recipient);
            name_of_recipient = itemView.findViewById(R.id.name_of_recipient);
            txt_last_message_content = itemView.findViewById(R.id.txt_last_message_content);
            txt_time_of_last_message = itemView.findViewById(R.id.txt_time_of_last_message);
            ll_conversation = itemView.findViewById(R.id.ll_conversation);
        }
    }
}
