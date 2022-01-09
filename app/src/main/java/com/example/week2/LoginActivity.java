package com.example.week2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;
import com.kakao.sdk.user.model.Profile;

import android.util.Log;

import java.net.Socket;
import java.util.ArrayList;

public class LoginActivity extends Activity {
    SocketClient socketClient;
    Button kakaoTalkLogin;
    // Button kakaoLogout;
    // Button kakaoAccountLogin;
    LinearLayout linearLayout;
    ServerThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        viewInit();
        linearLayout.bringToFront();
        linearLayout.setVisibility(View.INVISIBLE);
        socketClient = (SocketClient) getApplicationContext();
        thread = new ServerThread(this, socketClient);

        kakaoTalkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(getBaseContext())) {
                    UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this, (oAuthToken, error) -> {
                        if (error != null) {
                            Log.e("TAG", "로그인 실패", error);
                        } else if (oAuthToken != null) {
                            startGame();
                        }
                        return null;
                    });
                }
                else {
                    UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this, (token, loginError) -> {
                        if(loginError != null){
                            Log.e("TAG", "로그인 실패", loginError);
                        } else{
                            startGame();
                        }
                        return null;
                    });
                }
            }
        });

//        kakaoAccountLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                UserApiClient.getInstance().loginWithKakaoAccount(getBaseContext(), (token, loginError) -> {
//                    if(loginError != null){
//                        Toast.makeText(getApplicationContext(), "로그인 실패!", Toast.LENGTH_SHORT).show();
//                        Log.e("TAG", "로그인 실패", loginError);
//                    } else{
//                        startGame();
//                    }
//                    return null;
//                });
//            }
//        });

//        kakaoLogout.setOnClickListener(new Button.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "정상적으로 로그아웃되었습니다.", Toast.LENGTH_SHORT).show();
//
//                UserApiClient.getInstance().logout((error) -> {
//                    return null;
//                });
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void viewInit(){
        linearLayout = findViewById(R.id.linearLayout);
        kakaoTalkLogin = findViewById(R.id.kakaoTalkLogin);
        //kakaoAccountLogin = findViewById(R.id.kakaoAccountLogin);
        //kakaoLogout = findViewById(R.id.kakaoLogout);
    }

    public void startGame(){

        UserApiClient.getInstance().me((user, throwable) -> {
            if(throwable != null){
                Log.e("tag", "사용자 정보 요청 실패" + throwable);
            } else{
                Account kakaoAccount = user.getKakaoAccount();
                if(kakaoAccount != null){
                    Profile profile = kakaoAccount.getProfile();

                    User loginuser = new User(null, null, null, 0);
                    socketClient.setUser(loginuser);
                    Toast.makeText(getApplicationContext(), "서버에 접속 중입니다.", Toast.LENGTH_SHORT).show();
                    socketClient.requestUserInfo(Long.toString(user.getId()));
                    thread.start();
                }
            }
            return null;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void startActivity(){

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();

    }

}