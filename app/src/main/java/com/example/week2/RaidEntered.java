package com.example.week2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class RaidEntered extends Activity {
    private ArrayList<Skill> addrList = new ArrayList<Skill>();
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridManager;
    private Context context;
    User user;
    private int raid_hp= 1000;
    private RaidEnteredAdapter mAdapter;
    MainActivity activity;
    private ContentResolver contentResolver;
    private AnimationDrawable animationDrawable;
    SocketClient socketClient;
    private Long startTime;
    private Integer damage = 0;
    public Integer is_raid;
    ProgressBar prog;
    private int raid_cnt;

    MutableLiveData<Integer> bossHp;

    private void updateData(){
        addrList.clear();
//        skill1.setCool(5.0f);
//        skill1.setLevel(1);
//        skill1.setPower(10);
//        skill1.setName("몸통박치기");
        for(int i = 0; i<user.getPoke().getSkills().size();i++)
        {
            if(Math.pow((i+1),2)<=user.poke.getLevel()){
                addrList.add(user.getPoke().getSkills().get(i));
            }
        }
        //mAdapter.setmList2(addrList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        prog = findViewById(R.id.progressBar3);
        prog.setProgress(raid_hp);
        updateData();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentResolver = getContentResolver();
        socketClient = (SocketClient) getApplicationContext();
        user = socketClient.getUser();

        socketClient.requestBossInfo(bossHp);


        for (int i = 0; i < user.getPoke().getSkills().size(); i++) {
            addrList.add(user.getPoke().getSkills().get(i));
        }
        mAdapter = new RaidEnteredAdapter(context, addrList);
        //raid_hp set
        Log.i("raid", ""+raid_hp + " " + user.getPoke().getSkills().get(0).getDamage());
        setContentView(R.layout.raid_entered);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mGridManager = new GridLayoutManager(context, 2);
        mRecyclerView = findViewById(R.id.recyclerview_list3);
        ImageView raid_back = findViewById(R.id.raid_back);
        ImageView boss = findViewById(R.id.boss);
        boss.setVisibility(View.VISIBLE);
        TextView narr = findViewById(R.id.narr2);
        prog = findViewById(R.id.progressBar3);
        prog.setMax(raid_hp);
        prog.setProgress(raid_hp);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mGridManager);
        //updateData();
        mRecyclerView.setAdapter(mAdapter);
        raid_cnt = user.getRaid_times();
        updateData();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mGridManager.HORIZONTAL);
        DividerItemDecoration dividerItemDecoration2 = new DividerItemDecoration(mRecyclerView.getContext(),
                mGridManager.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.addItemDecoration(dividerItemDecoration2);
        startTime = System.currentTimeMillis();
        final Animation die = AnimationUtils.loadAnimation(this, R.anim.mob_die);
        final Animation attacked = AnimationUtils.loadAnimation(this, R.anim.mob_attacked);
        //mAdapter.setmList2(addrList);

        mAdapter.setOnItemCLickListener3(new RaidEnteredAdapter.OnItemClickListener3() {

            @Override
            public void onUpClick3(int position) {
                int attack = mAdapter.getItem(position).getDamage();
                Long passT = ((System.currentTimeMillis() - mAdapter.getItem(position).getStart()));
                Log.i("skill cool check",mAdapter.getItem(position).getName()+passT);
                if(passT<mAdapter.getItem(position).getCool()*1000){
                    //쿨탐 덜참
                    Toast.makeText(getApplicationContext(), "쿨타임이 "+ (passT/1000)+"초 남았습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if((raid_hp-attack)<=0)
                {
                    //몬스터 죽음
                    boss.startAnimation(die);
                    prog.setProgress(0);
                    Long newcoin = user.getCoin() + user.getPoke().getLevel()*10;
                    damage += raid_hp-attack;
                    user.setRaid_times(raid_cnt-1);
                    user.setCoin(newcoin);
                    user.getPoke().getSkills().get(position).setSkillcoin();
                    long newExp = user.getPoke().getExp()+user.poke.level*19;
                    ////다음으로
                    narr.setText("디아루가를 처치했다!" + "\n"+user.getPoke().getLevel()*10+" 코인 획득"+ ","+user.poke.level*19+" 경험치 획득");
                    if(newExp >= Math.pow(user.getPoke().level,2)*100){
                        while(newExp >= Math.pow(user.getPoke().level,2)*100) {

                            newExp -= Math.pow(user.getPoke().level, 2) * 100;
                            user.getPoke().setLevel(user.getPoke().getLevel() + 1);
                            narr.setText("포켓몬의 레벨이 상승했다!");
                            if ((user.getPoke().getLevel() == 3) || (user.getPoke().getLevel() == 5)) {
                                user.getPoke().setNumber(user.getPoke().getNumber() + 1);
                                narr.setText("포켓몬의 레벨이 상승했다!"+"포켓몬이 진화했다!");
                                Log.i("evolution", "" + user.getPoke().getNumber());
                            }

                        }
                    }
                    user.getPoke().setExp(newExp);
                    user.getPoke().setExp(newExp);
//                    TextView coinview = findViewById(R.id.coin);
//                    coinview.setText(""+newcoin);
//                    TextView expView = findViewById(R.id.exp);
//                    double expper = user.getPoke().getExp()*100/(Math.pow(user.getPoke().level,2)*100);
//                    expView.setText(String.format("%.2f%%", expper));
//                    ProgressBar prog = findViewById(R.id.progressBar);
//                    prog.setProgress((int)Math.round(expper));
//                    TextView levelView = findViewById(R.id.level);
//                    levelView.setText("Lv."+user.getPoke().getLevel());
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    socketClient.getUser().setRaid_damage(socketClient.getUser().getRaid_damage() + damage);
                    socketClient.notifyChange();
                    socketClient.sendRaidDamage(damage);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(getApplicationContext() != null){
                                //boss.setVisibility(View.INVISIBLE);
                                finish();
                            }
                        }
                    }, 900);
                    return;
                }
                else {
                    narr.setText(mAdapter.getItem(position).getName()+" 스킬 사용!");
                    boss.startAnimation(attacked);
                    narr.setText(mAdapter.getItem(position).getName()+" 스킬 사용!"+"\n"+mAdapter.getItem(position).getDamage()+"의 데미지를 입혔다!");
                    damage += attack;
                    raid_hp -= attack;
                    prog.setProgress(raid_hp);
                    mAdapter.getItem(position).setStart(System.currentTimeMillis());
                    Log.i("skill cool start",mAdapter.getItem(position).getName()+mAdapter.getItem(position).getStart());
                }

            }
        });
        ///// 다음으로
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(getApplicationContext() != null){
                    //boss.setVisibility(View.INVISIBLE);
                    user.setRaid_times(raid_cnt-1);
                    narr.setText("총 "+damage+"의 데미지를 입혔다!");
                    socketClient.sendRaidDamage(damage);

                    socketClient.getUser().setRaid_damage(socketClient.getUser().getRaid_damage() + damage);
                    socketClient.notifyChange();
                    finish();
                }
            }
        }, 10000);

    }
    @Override
    public void onBackPressed() {
        exitRaid();
    }
    public void exitRaid(){
        AlertDialog.Builder builder = new AlertDialog.Builder(RaidEntered.this);
        builder.setMessage("레이드를 나가시겠습니까?(횟수가 차감되지 않습니다.)");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RaidEntered.super.onBackPressed();
                finish();
                //socketClient.
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
