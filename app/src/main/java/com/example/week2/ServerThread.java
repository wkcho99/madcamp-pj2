package com.example.week2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class ServerThread extends Thread{

    SocketClient socketClient;
    LoginActivity loginActivity;

    public ServerThread(LoginActivity loginActivity, SocketClient socketClient) {
        this.socketClient = socketClient;
        this.loginActivity = loginActivity;
    }

    public void run(){

        User user;
        int time = 0;
        do {
            user = socketClient.getUser();
            time += 1;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } while (time <= 10 && user.getUser_id() == null);

        if(user.getName() != null) {
            loginActivity.startActivity(0);
        } else if(time <= 10){
            loginActivity.startActivity(1);
        }
        else{
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run()
                {
                    Toast.makeText(loginActivity, "서버에 접속할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    socketClient.disconnect();
                }
            }, 0);
        }


    }
}
