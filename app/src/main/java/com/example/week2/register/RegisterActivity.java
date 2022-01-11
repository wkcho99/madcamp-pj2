package com.example.week2.register;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.week2.MainActivity;
import com.example.week2.R;
import com.example.week2.SocketClient;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends Activity{

    EditText registerName;
    TextView registerPokemon;
    Spinner registerClass;
    Button  cancelBtn, registerBtn;
    Integer[] classes = {1, 2, 3, 4};
    String userName;
    String kakaoId;

    List<PagerModel> pagerArr;
    PagerAdapter PagerAdapter;

    SocketClient socketClient;
    RegisterThread thread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        socketClient = (SocketClient) getApplicationContext();
        thread = new RegisterThread(this, socketClient);


        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        kakaoId = intent.getStringExtra("kakaoId");

        registerName = findViewById(R.id.registerName);
        registerClass = findViewById(R.id.registerClass);
        registerPokemon = findViewById(R.id.registerPokemon);
        registerBtn = findViewById(R.id.registerOK);

        registerName.setText(userName);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(
                this, R.layout.support_simple_spinner_dropdown_item, classes);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        registerClass.setAdapter(adapter);

        ViewPager vp = findViewById(R.id.registerPager);

        pagerArr = new ArrayList<>();
        pagerArr.add(new PagerModel(R.drawable.pokemon1, "모부기", 0));
        pagerArr.add(new PagerModel(R.drawable.pokemon4, "불꽃숭이", 3));
        pagerArr.add(new PagerModel(R.drawable.pokemon7, "팽도리", 6));
        PagerAdapter = new PagerAdapter(this, pagerArr);

        vp.setAdapter(PagerAdapter);
        vp.setClipToPadding(false);
        vp.setPadding(200, 0, 200, 0);
        vp.setPageMargin(100);
        vp.setCurrentItem(Integer.MAX_VALUE/2);
        registerPokemon.setText(pagerArr.get((Integer.MAX_VALUE/2)%3).getName());

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                registerPokemon.setText(pagerArr.get(position%3).getName());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int pokeNum = (vp.getCurrentItem()%3) * 3;
                String userName = registerName.getText().toString();
                String classValue = registerClass.getSelectedItem().toString();

                String query = "{" +
                        "user_id='" + kakaoId + '\'' +
                        ", name='" + userName + '\'' +
                        ", pokeNum=" + pokeNum  +
                        ", classValue=" + classValue +
                        '}';

                Log.i("register", query);

                socketClient.register(query);
                thread.start();

            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void startGame(){
        Log.i("startGame", "done?");
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

}
