package com.example.week2;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
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

import java.util.ArrayList;

public class AdventureActivity2 extends Fragment {
    private ArrayList<Skill> addrList = new ArrayList<Skill>();
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridManager;
    private Context context;
    MediaPlayer mediaPlayer;
    User user;
    ProgressBar prog;
    private int mob_hp;
    private AdventureAdapter mAdapter;
    MainActivity activity;
    private ContentResolver contentResolver;
    private AnimationDrawable animationDrawable;
    SocketClient socketClient;
    int[] images = new int[] {R.drawable.mob1, R.drawable.mob2, R.drawable.mob3, R.drawable.mob4, R.drawable.mob5, R.drawable.mob6};
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentResolver = getActivity().getContentResolver();
        socketClient = (SocketClient) getActivity().getApplicationContext();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.adventure_activity2, container, false);
        //Glide.with(this).load(R.raw.adventure).into(adventure_back);
        //root.setContentView(new MyGameView(this));
        user = socketClient.getUser();
        addrList.clear();
        for (int i = 0; i < user.getPoke().getSkills().size(); i++) {
            addrList.add(user.getPoke().getSkills().get(i));
        }
        mAdapter = new AdventureAdapter(context, addrList);
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.battle);
        if(mediaPlayer!=null){
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
        Log.i("adventure", ""+mob_hp + " " + user.getPoke().getSkills().get(0).getDamage());
        return root;
        //return new GameView(getActivity());

    }

    @Override
    public void onResume() {
        super.onResume();

        mob_hp = user.getPoke().getLevel()*10;
        prog = getActivity().findViewById(R.id.progressBar2);
        prog.setMax(mob_hp);
        prog.setProgress(mob_hp);
        updateData();
    }

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
        mob_hp = user.getPoke().getLevel()*10;
        //mAdapter.setmList2(addrList);
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        context = getActivity();
        contentResolver = getActivity().getContentResolver();
        mGridManager = new GridLayoutManager(context, 2);
        mRecyclerView = view.findViewById(R.id.recyclerview_list2);
        ImageView adventure_back = view.findViewById(R.id.adventure_back2);
        ImageView mob = view.findViewById(R.id.mob);
        int imageId = (int)(Math.random() * images.length);
        mob.setImageResource(images[imageId]);
        mob.setVisibility(View.VISIBLE);
        mob_hp = user.getPoke().getLevel()*10;
        prog = view.findViewById(R.id.progressBar2);
        prog.setMax(mob_hp);
        prog.setProgress(mob_hp);
        ImageView adventure_poke2 = view.findViewById(R.id.adventure_poke2);
        TextView narr = view.findViewById(R.id.narr);
//        ProgressBar prog = view.findViewById(R.id.progressBar2);
//        prog.setMax(mob_hp);
//        prog.setProgress(mob_hp);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mGridManager);
        updateData();
        TrainActivity.setMy_poke(adventure_poke2,user.getPoke().getNumber());
        mRecyclerView.setAdapter(mAdapter);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                    mGridManager.HORIZONTAL);
            DividerItemDecoration dividerItemDecoration2 = new DividerItemDecoration(mRecyclerView.getContext(),
                    mGridManager.VERTICAL);
            mRecyclerView.addItemDecoration(dividerItemDecoration);
            mRecyclerView.addItemDecoration(dividerItemDecoration2);
        final Animation die = AnimationUtils.loadAnimation(getActivity(), R.anim.mob_die);
        final Animation attacked = AnimationUtils.loadAnimation(getActivity(), R.anim.mob_attacked);
            //mAdapter.setmList2(addrList);

            mAdapter.setOnItemCLickListener2(new AdventureAdapter.OnItemClickListener2() {
                @Override
                public void onUpClick2(View v, int position) {
                    modal(v, position);
                }

            @Override
                    public void onItemClick2(View v, int position, View itemView) {
            int attack = mAdapter.getItem(position).getDamage();
            Long passT = ((System.currentTimeMillis() - mAdapter.getItem(position).getStart()));
                Log.i("skill cool check",mAdapter.getItem(position).getName()+passT);
            if(passT<mAdapter.getItem(position).getCool()*1000){
                //쿨탐 덜참
                Toast.makeText(view.getContext(), "쿨타임이 "+ (passT/1000)+"초 남았습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            else if((mob_hp-attack)<=0)
            {
                //몬스터 죽음
                mob_hp = 0;
                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }
                if(mediaPlayer != null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                prog.setProgress(mob_hp);
                mob.startAnimation(die);
                Long newcoin = user.getCoin() + user.getPoke().getLevel()*10;
                user.setCoin(newcoin);
                long newExp = user.getPoke().getExp()+user.poke.level*19;
                narr.setText("사냥감을 처치했다!" + "\n"+user.getPoke().getLevel()*10+" 코인 획득"+ ","+user.poke.level*19+" 경험치 획득");
                if(newExp >= Math.pow(user.getPoke().level,2)*100){
                    while(newExp >= Math.pow(user.getPoke().level,2)*100) {

                        newExp -= Math.pow(user.getPoke().level, 2) * 100;
                        user.getPoke().setLevel(user.getPoke().getLevel() + 1);
                        narr.setText("포켓몬의 레벨이 상승했다!");
                        if ((user.getPoke().getLevel() == 3) || (user.getPoke().getLevel() == 5)) {
                            user.getPoke().setNumber(user.getPoke().getNumber() + 1);
                            narr.setText("포켓몬의 레벨이 상승했다!"+"포켓몬이 진화했다!");
                            Log.i("evolution", "" + user.getPoke().getNumber());
                            TrainActivity.setMy_poke(adventure_poke2, user.getPoke().getNumber());
                        }

                    }
                }
                user.getPoke().setExp(newExp);
                user.getPoke().setExp(newExp);
                TextView coinview = getActivity().findViewById(R.id.coin);
                coinview.setText(""+newcoin);
                TextView expView = getActivity().findViewById(R.id.exp);
                double expper = user.getPoke().getExp()*100/(Math.pow(user.getPoke().level,2)*100);
                expView.setText(String.format("%.2f%%", expper));
                ProgressBar prog = getActivity().findViewById(R.id.progressBar);
                prog.setProgress((int)Math.round(expper));
                TextView levelView = getActivity().findViewById(R.id.level);
                levelView.setText("Lv."+user.getPoke().getLevel());
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                socketClient.notifyChange();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(activity != null){
                            mob.setVisibility(View.INVISIBLE);
                            activity.onFragmentChange(0);
                        }
                    }
                }, 900);
                return;
            }
            else {
                narr.setText(mAdapter.getItem(position).getName()+" 스킬 사용!");
                mob.startAnimation(attacked);
                mob_hp -= attack;
                prog.setProgress(mob_hp);
                mAdapter.getItem(position).setStart(System.currentTimeMillis());
                Log.i("skill cool start",mAdapter.getItem(position).getName()+mAdapter.getItem(position).getStart());
            }

        }
        });
    }

    public void modal(final View view, int position) {
        String msg;
        Skill s = mAdapter.getItem(position);
        msg = s.getName();
        new AlertDialog.Builder(view.getContext())
                .setTitle("스킬 정보")
                .setMessage(msg)

                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(view.getContext(), "확인", Toast.LENGTH_SHORT).show();
                        //((Activity) view.getContext()).finish();
                    }
                }).show();
    }

}