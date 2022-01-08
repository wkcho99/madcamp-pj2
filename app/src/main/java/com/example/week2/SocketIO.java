package com.example.week2;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketIO {

    public Socket mSocket;
    public String URI = "http://3.35.49.220:54803";
    Gson gson;

    public SocketIO(String kakao_id) {

        try {
            mSocket = IO.socket(URI);
        } catch (URISyntaxException e) {
            Log.e("socketIO", e.toString());
        }

        mSocket.on(Socket.EVENT_CONNECT, fn -> {
            Log.i("socketIO", "complete");
            try {
                mSocket.emit("login", new JSONObject("{\"user_id\":\""+kakao_id+"\"}"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        mSocket.on("login", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                Log.i("socketIO", "login " + data);
            }
        });
    }

    public void skillUP(int pokemon_id, int skill_id, int level){

    }

    public void connect(){
        mSocket.connect();
        Log.i("socketIO", "connect");
    }

    public void disconnect(){
        mSocket.disconnect();
        Log.i("socketIO", "disconnect");
    }

}
