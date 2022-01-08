package com.example.week2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class LoadingActivity extends Activity{

    SocketClient socketClient;
    User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        socketClient = (SocketClient) getApplicationContext();
        socketClient.requestUserInfo(user.getUser_id());

    }

    @Override
    protected void onResume() {
        super.onResume();

        while(socketClient.getUser().getName() == null){

        }

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();

    }
}
