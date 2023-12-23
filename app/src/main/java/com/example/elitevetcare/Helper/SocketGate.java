//package com.example.elitevetcare.Helper;
//
//import android.app.Activity;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.example.elitevetcare.Interface.SocketOnMessageListener;
//
//import java.net.URISyntaxException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//
//import io.socket.client.IO;
//import io.socket.client.Socket;
//import io.socket.emitter.Emitter;
//import io.socket.engineio.client.transports.WebSocket;
//
//
//public class SocketGate {
//    private static Socket mSocket = null;
//    private static ArrayList<SocketOnMessageListener> listListenerEvent = new ArrayList<>();
//    public static void OpenGate(Activity activity){
//        if(mSocket != null)
//            return;
//        try {
//            IO.Options options = new IO.Options();
//            Map<String, List<String>> headers = new HashMap<>();
//            headers.put("Authorization", Collections.singletonList("Bearer " +  DataLocalManager.GetAccessToken()));
//            options.extraHeaders = headers;
//            // Tạo đối tượng Socket.IO
//            mSocket = IO.socket("https://elitevetcare-be.up.railway.app", options);
//
//            // Lắng nghe sự kiện khi kết nối thành công
//            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
//                @Override
//                public void call(Object... args) {
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(activity, "Ket Noi Thanh Cong", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            });
//
//            // Lắng nghe sự kiện từ server
//            mSocket.on("connected", new Emitter.Listener() {
//                @Override
//                public void call(Object... args) {
//                    // Xử lý dữ liệu từ server
//                    // Ví dụ: String data = (String) args[0];
//
//                    Log.d("Connectiona", args[0].toString());
//                }
//            });
//            mSocket.on("onMessage", new Emitter.Listener() {
//                @Override
//                public void call(Object... args) {
//                    notifyListeners(args[0].toString());
//                    Log.d("ConnectionaRespone", args[0].toString());
//                }
//            });
//            // Kết nối đến server
//            mSocket.connect();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void addSocketEventListener(SocketOnMessageListener listener) {
//        if (!listListenerEvent.contains(listener)) {
//            listListenerEvent.add(listener);
//        }
//    }
//
//    public static void removeSocketEventListener(SocketOnMessageListener listener) {
//        listListenerEvent.remove(listener);
//    }
//
//    private static void notifyListeners(String message) {
//        for (SocketOnMessageListener listener : listListenerEvent) {
//            listener.onMessageListener(message);
//        }
//    }
//}
