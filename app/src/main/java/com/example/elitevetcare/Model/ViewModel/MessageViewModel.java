package com.example.elitevetcare.Model.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.elitevetcare.Model.ObjectModel.Message;
import java.util.ArrayList;

public class MessageViewModel extends ViewModel {
    private final MutableLiveData<Integer> ConversationID = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Message>> MessageArray = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public void AddNewMessage(Message Message){
        ArrayList<Message> currentMessages = MessageArray.getValue();
        if (currentMessages != null) {
            currentMessages.add(Message);
            MessageArray.postValue(currentMessages);
        }
    }
    public void AddMessageArray(ArrayList<Message> ListMessage){
        if(MessageArray.getValue() != null){
            MessageArray.getValue().addAll(ListMessage);
        }else {
            SetMessageArray(ListMessage);
        }
    }

    public void SetLoading(Boolean IsBoolean){
        isLoading.setValue(IsBoolean);
    }

    public MutableLiveData<Boolean> isLoading() {
        return isLoading;
    }
    public void SetConversationID(int id){
        ConversationID.setValue(id);
    }

    public MutableLiveData<Integer> getConversationID() {
        return ConversationID;
    }
    public void SetMessageArray(ArrayList<Message> ListMessage){
        MessageArray.setValue(ListMessage);
    }

    public MutableLiveData<ArrayList<Message>> getMessageArray() {
        return MessageArray;
    }
    public void removeMessage(Message message) {
        ArrayList<Message> currentMessages = MessageArray.getValue();
        if (currentMessages != null) {
            currentMessages.remove(message);
            MessageArray.postValue(currentMessages);
        }
    }
}
