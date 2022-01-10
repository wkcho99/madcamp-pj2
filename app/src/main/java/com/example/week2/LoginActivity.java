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

import com.example.week2.register.RegisterActivity;
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
    Profile profile;
    String kakaoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        viewInit();

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
                    profile = kakaoAccount.getProfile();
                    kakaoId = Long.toString(user.getId());
                    User loginUser = new User(null, null, null, 0, null,0,0,3);
                    socketClient.setUser(loginUser);
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


    public void startActivity(int code){

        if(code == 0) {
            // game start
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else if(code == 1){
            // register
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            intent.putExtra("userName", profile.getNickname());
            intent.putExtra("kakaoId", kakaoId);
            startActivity(intent);
            finish();
        }

    }

}