package com.example.elitevetcare.Adapter.RecyclerViewAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elitevetcare.Helper.Libs;
import com.example.elitevetcare.Model.CurrentData.CurrentUser;
import com.example.elitevetcare.Model.ObjectModel.Message;
import com.example.elitevetcare.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Calendar;

public class RecyclerViewMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    ArrayList<Message> listMessage;
    final private int MESSAGE_SENT_TYPE = 1;
    final private int MESSAGE_RECEIVED_TYPE = 2;
    public RecyclerViewMessageAdapter(ArrayList<Message> listMessage) {
        this.listMessage = listMessage;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MESSAGE_SENT_TYPE){
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent,parent,false);
            return new MessageSentViewHolder(itemView);
        }
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received,parent,false);
        return new MessageReceivedViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        if(listMessage.get(position).getAuthor().getId() == CurrentUser.getCurrentUser().getId())
            return MESSAGE_SENT_TYPE;
        return MESSAGE_RECEIVED_TYPE;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = listMessage.get(position);
        if(getItemCount()-1 == position){
            if(message.getAuthor().getId() != CurrentUser.getCurrentUser().getId()){
                MessageReceivedViewHolder newHolder = ((MessageReceivedViewHolder)holder);
                newHolder.txt_message_content.setBackgroundResource(R.drawable.background_last_received_message);
                newHolder.txt_message_content.setText(message.getContent());
                Libs.SetImageFromURL(message.getAuthor().getAvatar(),newHolder.avatar_recipient);
                newHolder.avatar_recipient.setVisibility(View.VISIBLE);
                newHolder.txt_time_received.setText(Libs.calculateDateDifference(message.getCreatedAt(), Calendar.getInstance().getTime()) + " Trước");
                newHolder.txt_time_received.setVisibility(View.VISIBLE);
            }else if (message.getAuthor().getId() == CurrentUser.getCurrentUser().getId()) {
                MessageSentViewHolder newHolder = ((MessageSentViewHolder)holder);
                newHolder.txt_message_content.setBackgroundResource(R.drawable.background_last_sent_message);
                newHolder.txt_message_content.setText(message.getContent());
                newHolder.txt_time_sent.setText(Libs.calculateDateDifference(message.getCreatedAt(), Calendar.getInstance().getTime()) + " Trước");
                newHolder.txt_time_sent.setVisibility(View.VISIBLE);
            }
        }else {
            Message nextMessage = listMessage.get(position + 1);
            if(nextMessage.getAuthor().getId() != message.getAuthor().getId()){
                if(message.getAuthor().getId() != CurrentUser.getCurrentUser().getId()){
                    MessageReceivedViewHolder newHolder = ((MessageReceivedViewHolder)holder);
                    newHolder.txt_message_content.setBackgroundResource(R.drawable.background_last_received_message);
                    newHolder.txt_message_content.setText(message.getContent());
                    Libs.SetImageFromURL(message.getAuthor().getAvatar(),newHolder.avatar_recipient);
                    newHolder.avatar_recipient.setVisibility(View.VISIBLE);
                    newHolder.txt_time_received.setText(Libs.calculateDateDifference(message.getCreatedAt(), Calendar.getInstance().getTime()));
                    newHolder.txt_time_received.setVisibility(View.GONE);
                }else if (message.getAuthor().getId() == CurrentUser.getCurrentUser().getId()) {
                    MessageSentViewHolder newHolder = ((MessageSentViewHolder)holder);
                    newHolder.txt_message_content.setBackgroundResource(R.drawable.background_last_sent_message);
                    newHolder.txt_message_content.setText(message.getContent());
                    newHolder.txt_time_sent.setText(Libs.calculateDateDifference(message.getCreatedAt(), Calendar.getInstance().getTime()));
                    newHolder.txt_time_sent.setVisibility(View.GONE);
                }

            }else {
                if(message.getAuthor().getId() != CurrentUser.getCurrentUser().getId()){
                    MessageReceivedViewHolder newHolder = ((MessageReceivedViewHolder)holder);
                    newHolder.txt_message_content.setText(message.getContent());
                    newHolder.txt_message_content.setBackgroundResource(R.drawable.background_received_message);
                    newHolder.avatar_recipient.setVisibility(View.GONE);
                    newHolder.txt_time_received.setText(Libs.calculateDateDifference(message.getCreatedAt(), Calendar.getInstance().getTime()));
                    newHolder.txt_time_received.setVisibility(View.GONE);
                }else if (message.getAuthor().getId() == CurrentUser.getCurrentUser().getId()) {
                    MessageSentViewHolder newHolder = ((MessageSentViewHolder)holder);
                    newHolder.txt_message_content.setText(message.getContent());
                    newHolder.txt_message_content.setBackgroundResource(R.drawable.background_sent_message);
                    newHolder.txt_time_sent.setText(Libs.calculateDateDifference(message.getCreatedAt(), Calendar.getInstance().getTime()));
                    newHolder.txt_time_sent.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return listMessage.size();
    }

    public class MessageSentViewHolder extends RecyclerView.ViewHolder {
        TextView txt_time_sent, txt_message_content;
        public MessageSentViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_time_sent = itemView.findViewById(R.id.txt_time_sent);
            txt_message_content = itemView.findViewById(R.id.txt_message_content);
        }
    }
    public class MessageReceivedViewHolder extends RecyclerView.ViewHolder {
        TextView txt_time_received, txt_message_content;
        RoundedImageView avatar_recipient;
        public MessageReceivedViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_message_content = itemView.findViewById(R.id.txt_message_content);
            txt_time_received = itemView.findViewById(R.id.txt_time_received);
            avatar_recipient = itemView.findViewById(R.id.recipient_Avatar);
        }
    }
}
