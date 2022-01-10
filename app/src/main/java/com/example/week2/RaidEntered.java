package com.example.week2;

import android.app.Activity;
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
    private int raid_hp= 10000;
    private RaidEnteredAdapter mAdapter;
    MainActivity activity;
    private ContentResolver contentResolver;
    private AnimationDrawable animationDrawable;
    SocketClient socketClient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentResolver = getContentResolver();
        socketClient = (SocketClient) getApplicationContext();
        user = socketClient.getUser();
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
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mGridManager);
        //updateData();
        mRecyclerView.setAdapter(mAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mGridManager.HORIZONTAL);
        DividerItemDecoration dividerItemDecoration2 = new DividerItemDecoration(mRecyclerView.getContext(),
                mGridManager.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.addItemDecoration(dividerItemDecoration2);
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
                    Long newcoin = user.getCoin() + user.getPoke().getLevel()*10;
                    user.setCoin(newcoin);
                    user.getPoke().getSkills().get(position).setSkillcoin();
                    long newExp = user.getPoke().getExp()+user.poke.level*19;
                    narr.setText("디아루가를 처치했다!" + "\n"+user.getPoke().getLevel()*10+" 코인 획득"+ "\n"+user.poke.level*19+" 경험치 획득");
                    if(newExp >= Math.pow(user.getPoke().level,2)*100){
                        while(newExp >= Math.pow(user.getPoke().level,2)*100) {

                            newExp -= Math.pow(user.getPoke().level, 2) * 100;
                            user.getPoke().setLevel(user.getPoke().getLevel() + 1);
                            narr.setText("포켓몬의 레벨이 상승했다!");
                            if ((user.getPoke().getLevel() == 3) || (user.getPoke().getLevel() == 5)) {
                                user.getPoke().setNumber(user.getPoke().getNumber() + 1);
                                narr.setText("포켓몬이 진화했다!");
                                Log.i("evolution", "" + user.getPoke().getNumber());
                            }

                        }
                    }
                    user.getPoke().setExp(newExp);
                    user.getPoke().setExp(newExp);
                    TextView coinview = findViewById(R.id.coin);
                    coinview.setText(""+newcoin);
                    TextView expView = findViewById(R.id.exp);
                    double expper = user.getPoke().getExp()*100/(Math.pow(user.getPoke().level,2)*100);
                    expView.setText(String.format("%.2f%%", expper));
                    ProgressBar prog = findViewById(R.id.progressBar);
                    prog.setProgress((int)Math.round(expper));
                    TextView levelView = findViewById(R.id.level);
                    levelView.setText("Lv."+user.getPoke().getLevel());
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    socketClient.notifyChange();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(activity != null){
                                //boss.setVisibility(View.INVISIBLE);
                                activity.onFragmentChange(0);
                            }
                        }
                    }, 900);
                    return;
                }
                else {
                    narr.setText(mAdapter.getItem(position).getName()+" 스킬 사용!");
                    boss.startAnimation(attacked);
                    raid_hp -= attack;
                    mAdapter.getItem(position).setStart(System.currentTimeMillis());
                    Log.i("skill cool start",mAdapter.getItem(position).getName()+mAdapter.getItem(position).getStart());
                }

            }
        });
    }

}
