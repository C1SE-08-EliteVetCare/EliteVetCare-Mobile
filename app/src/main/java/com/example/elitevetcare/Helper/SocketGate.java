package com.example.elitevetcare.Helper;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.example.elitevetcare.Interface.SocketOnMessageListener;
import com.example.elitevetcare.Model.CurrentData.CurrentUser;
import com.example.elitevetcare.Model.ObjectModel.Appointment;
import com.example.elitevetcare.Model.ObjectModel.Conversation;
import com.example.elitevetcare.Model.ObjectModel.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.transports.WebSocket;


public class SocketGate {
    private static Socket mSocket = null;
    public static final int MESSAGE_EVENT_CODE = 1;
    public static final int  APPOINTMENT_CREATE_EVENT_CODE = 2;
    public static final int  APPOINTMENT_STATUS_EVENT_CODE = 3;
    public static Socket getmSocket() {
        return mSocket;
    }

    private static ArrayList<SocketOnMessageListener> listListenerEvent = new ArrayList<>();
    public static void OpenGate(Activity activity){
        if(mSocket != null)
            return;
        try {
            IO.Options options = new IO.Options();
            Map<String, List<String>> headers = new HashMap<>();
            headers.put("Authorization", Collections.singletonList("Bearer " +  DataLocalManager.GetAccessToken()));
            options.extraHeaders = headers;
            // Tạo đối tượng Socket.IO
            mSocket = IO.socket("https://elitevetcare-be.up.railway.app", options);

            // Lắng nghe sự kiện khi kết nối thành công
            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "Ket Noi Thanh Cong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            // Lắng nghe sự kiện từ server
            mSocket.on("connected", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    // Xử lý dữ liệu từ server
                    // Ví dụ: String data = (String) args[0];

                    Log.d("Connectiona", args[0].toString());

                }
            });
            SocketGate.mSocket.on("onMessage", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    SocketGate.notifyListeners(args[0].toString(), MESSAGE_EVENT_CODE);

                    try {
                        Gson gson = new Gson();
                        Conversation conversation = gson.fromJson((new JSONObject(args[0].toString())).get("conversation").toString(), new TypeToken<Conversation>(){}.getType());
                        if(conversation.getLastMessageSent().getAuthor().getId() != CurrentUser.getCurrentUser().getId() && conversation.getId() != CurrentIDConversation){
                            User Recipient = conversation.getCreator().getId() == CurrentUser.getCurrentUser().getId() ? conversation.getRecipient() : conversation.getCreator();
                            NotificationHelper.showNotification(activity.getApplicationContext(), "Tin Nhắn Mới", Recipient.getFullName()+" Đã Gửi cho bạn 1 tin nhắn", Recipient.getId());
                        }


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            AddEvent(activity);
            // Kết nối đến server
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    private static int CurrentIDConversation = -1;

    public static void setCurrentIDConversation(int currentIDConversation) {
        CurrentIDConversation = currentIDConversation;
    }

    private static void AddEvent(Activity activity) {
        SocketGate.mSocket.on("onAppointmentCreate", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                SocketGate.notifyListeners(args[0].toString(), APPOINTMENT_CREATE_EVENT_CODE);
                try {
                    Gson gson = new Gson();
                    if(CurrentUser.getCurrentUser().getRole().getId() == 3 ){
                        User Recipient = gson.fromJson((new JSONObject(args[0].toString())).get("user").toString(), new TypeToken<User>(){}.getType());
                        NotificationHelper.showNotification(activity.getApplicationContext(), "Cuộc hẹn chưa xử lý", Recipient.getFullName()+" đã đặt lịch khám! Bạn nên xem thử!", CurrentUser.getCurrentUser().getId());
                    }


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        });
        SocketGate.mSocket.on("onAppointmentStatus", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                SocketGate.notifyListeners(args[0].toString(), APPOINTMENT_STATUS_EVENT_CODE);

                try {
                    Gson gson = new Gson();
                    if(CurrentUser.getCurrentUser().getRole().getId() == 2){
                        Appointment appointment = gson.fromJson((new JSONObject(args[0].toString())).get("appointmentDetail").toString(), new TypeToken<Appointment>(){}.getType());
                        LocalDate DateAppointment =  LocalDate.parse(appointment.getAppointmentDate());
                        if(appointment.getStatus() == 2)
                            NotificationHelper.showNotification(activity.getApplicationContext(), "Cuộc Hẹn Vào Ngày " + appointment.getAppointmentDate() + " Đã được chấp nhận!", "Bác Sĩ đã đồng ý lịch khám! Kiểm tra ngay!", appointment.getId());
                        if(appointment.getStatus() == 3)
                            NotificationHelper.showNotification(activity.getApplicationContext(), "Cuộc Hẹn Vào Ngày " + appointment.getAppointmentDate() + " Đã bị huỷ!", "Có thể đã trùng lịch với khách khác nên bạn có thể xem xét lại giờ khác!", appointment.getId());
                    }


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public static void CloseGate() {
        if (mSocket != null && mSocket.connected()) {
            mSocket.disconnect();
            mSocket.close();
            mSocket = null;
        }
    }
    public static void addSocketEventListener(SocketOnMessageListener listener) {
        if (!listListenerEvent.contains(listener)) {
            listListenerEvent.add(listener);
        }
    }

    public static void removeSocketEventListener(SocketOnMessageListener listener) {
        listListenerEvent.remove(listener);
    }

    public static void notifyListeners(String message, int Code) {
        for (SocketOnMessageListener listener : listListenerEvent) {
            listener.onMessageListener(message, Code);
        }
    }
}
