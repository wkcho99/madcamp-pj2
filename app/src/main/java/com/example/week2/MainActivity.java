package com.example.week2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.kakao.sdk.common.util.Utility;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;
import com.kakao.sdk.user.model.Profile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends FragmentActivity {
    Long coin;
    Integer level;
    double exp;
    double expper;
    int frag = 0;
    User user;
    SocketClient socketClient;

    TextView nick;
    TextView levelview;
    TextView coinview;
    TextView expview;

    Button train, adventure, raid;
    //Button logout;
    ProgressBar prog;
    TrainActivity fragment1;
    AdventureActivity fragment2;
    RaidActivity fragment3;
    AdventureActivity2 fragment4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        socketClient = (SocketClient) getApplicationContext();

        setContentView(R.layout.activity_main);

        user = socketClient.getUser();


        //logout = findViewById(R.id.logout);
        train = findViewById(R.id.train);
        adventure = findViewById(R.id.adventure);
        raid = findViewById(R.id.raid);
        levelview = findViewById(R.id.level);
        coinview = findViewById(R.id.coin);
        expview = findViewById(R.id.exp);
        prog = findViewById(R.id.progressBar);

        fragment1 = new TrainActivity();
        fragment2 = new AdventureActivity();
        fragment3 = new RaidActivity();
        fragment4 = new AdventureActivity2();


        Log.i("mainactivity user info",user.toString());

        //logout = findViewById(R.id.logout);
        train = findViewById(R.id.train);
        adventure = findViewById(R.id.adventure);
        raid = findViewById(R.id.raid);

        TrainActivity fragment1 = new TrainActivity();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user",user);

        AdventureActivity fragment2 = new AdventureActivity();
        RaidActivity fragment3 = new RaidActivity();
        fragment3.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_main,fragment1).commit();


    }

    @Override
    protected void onStart() {
        super.onStart();

        /*
        logout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "정상적으로 로그아웃되었습니다.", Toast.LENGTH_SHORT).show();

                UserApiClient.getInstance().logout(error -> {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return null;
                });
            }
        });
         */
        train.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Toast.makeText(getApplicationContext(), "트레인 버튼 클릭됨.", Toast.LENGTH_SHORT).show();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                transaction.replace(R.id.fragment_main,fragment1);
                transaction.commit();
            }
        });
        raid.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Toast.makeText(getApplicationContext(), "레이드 버튼 클릭됨.", Toast.LENGTH_SHORT).show();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                transaction.replace(R.id.fragment_main,fragment3);
                transaction.commit();
            }
        });
        adventure.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Toast.makeText(getApplicationContext(), "어드벤처 버튼 클릭됨.", Toast.LENGTH_SHORT).show();
                onFragmentChange(frag);
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_main,fragment2);
//                transaction.commit();
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

        coin = user.getCoin();
        level = user.poke.getLevel();
        exp = user.poke.getExp();

        levelview.setText("Lv."+level);
        coinview.setText(""+coin);
        expper = exp*100/(Math.pow(user.poke.level,2)*100);
        expview.setText(String.format("%.2f%%", user.getPoke().getExp()*100/(Math.pow(user.getPoke().level,2)*100)));
        prog.setProgress((int)Math.round(expper));



        if(socketClient.addCoin > 0) {
            showDialog();
            socketClient.addCoin = 0;
        }

    }

    @Override
    protected void onStop() {
        setResult(RESULT_OK);
        super.onStop();
    }
    @Override
    public void onBackPressed() {
        //setResult(RESULT_OK);
        exitGame();
    }
    @Override
    protected void onDestroy() {
        setResult(RESULT_OK);
        super.onDestroy();
        Log.i("socketIO", "destroy");
        socketClient.disconnect();
    }
    public void onFragmentChange(int index){
        if(index == 0){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            transaction.replace(R.id.fragment_main,fragment2);
            frag = 0;
            transaction.commit();
        }
        if(index == 1){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            transaction.replace(R.id.fragment_main,fragment4);
            frag = 1;
            transaction.commit();
        }
        if(index == 3){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            transaction.replace(R.id.fragment_main,fragment3);
            frag = 3;
            transaction.commit();
        }

    }

    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("쉬는 동안 " + socketClient.addCoin + "코인 획득");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void exitGame(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("게임을 그만하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //socketClient
                MainActivity.super.onBackPressed();
                finishAffinity();
            }
        });
        builder.setNegativeButton("취소",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
