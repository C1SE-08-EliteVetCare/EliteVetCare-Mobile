package com.example.elitevetcare.Model.ViewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;

public class AppointmentViewModel extends ViewModel {
    private final MutableLiveData<Integer> ProcessingTotalItem = new MutableLiveData<Integer>();
    private final MutableLiveData<Integer> AcceptedTotalItem = new MutableLiveData<Integer>();
    private final MutableLiveData<Integer> RejectedTotalItem = new MutableLiveData<Integer>();
    private final MutableLiveData<Boolean> isLoad = new MutableLiveData<Boolean>();
    public void setisLoad(boolean isLoadorNot) {
        isLoad.setValue(isLoadorNot);
    }
    public MutableLiveData<Boolean> getisLoad() {
        return isLoad;
    }

    public void setProcessingTotalItem(int totalItem) {
        Log.d("ProcessData", " total item: "+totalItem);
        ProcessingTotalItem.setValue(totalItem);
    }

    public void setAcceptedTotalItem(int totalItem) {
        AcceptedTotalItem.setValue(totalItem);
    }

    public void setRejectedTotalItem(int totalItem) {
        RejectedTotalItem.setValue(totalItem);
    }
    public MutableLiveData<Integer> getProcessingTotalItem() {
        return ProcessingTotalItem;
    }

    public MutableLiveData<Integer> getAcceptedTotalItem() {
        return AcceptedTotalItem;
    }

    public MutableLiveData<Integer> getRejectedTotalItem() {
        return RejectedTotalItem;
    }
}
