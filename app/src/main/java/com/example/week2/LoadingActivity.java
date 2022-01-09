package com.example.week2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class LoadingActivity extends Activity{

    SocketClient socketClient;
    User user;
    ServerThread thread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        socketClient = (SocketClient) getApplicationContext();
        user = socketClient.getUser();
        socketClient.requestUserInfo(user.getUser_id());


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void startActivity(String result){
        Intent intent;
        if(result == null){
            intent = new Intent(getApplicationContext(), LoginActivity.class);
            Toast.makeText(getApplicationContext(), "서버에 연결할 수 없습니다.", Toast.LENGTH_SHORT).show();
        } else {
            intent = new Intent(getApplicationContext(), MainActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
