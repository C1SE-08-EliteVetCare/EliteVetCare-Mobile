package com.example.elitevetcare.Helper;
import com.example.elitevetcare.Interface.DataChangeListener;

public class DataChangeObserver {
    private static DataChangeObserver instance;

    private DataChangeListener listener;

    private DataChangeObserver() {
        // Không để nó tạo instance
    }

    public static synchronized DataChangeObserver getInstance() {
        if (instance == null) {
            instance = new DataChangeObserver();
        }
        return instance;
    }

    public DataChangeListener getListener() {
        return listener;
    }

    public void setListener(DataChangeListener listener) {
        this.listener = listener;
    }
}
