package com.example.week2.register;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.week2.SocketClient;
import com.example.week2.User;

public class RegisterThread extends Thread{

    SocketClient socketClient;
    RegisterActivity registerActivity;

    public RegisterThread(RegisterActivity registerActivity, SocketClient socketClient) {
        this.socketClient = socketClient;
        this.registerActivity = registerActivity;
    }

    public void run(){

        User user;
        int time = 0;
        do {
            user = socketClient.getUser();
            Log.i("thread", user.toString());
            time += 1;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } while (time <= 10 && user.getName() == null);

        if(user.getName() != null) {
            registerActivity.startGame();
        }
        else{

        }


    }
}
