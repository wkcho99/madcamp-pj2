package com.example.week2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.TimeZoneFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.net.Socket;
import java.util.ArrayList;

public class TrainActivity extends Fragment {
    private ArrayList<Skill> addrList = new ArrayList<Skill>();
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private Context context;
    User user;
    SocketClient socketClient;

    ImageView my_poke;
    ImageView train_back;
    TextView lvup;
    private TrainAdapter mAdapter;
    private ContentResolver contentResolver;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        socketClient = (SocketClient) getActivity().getApplicationContext();
    }
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.train_activity, container, false);
        user = socketClient.getUser();
        addrList.clear();
        for(int i = 0; i<user.getPoke().getSkills().size();i++)
        {
            addrList.add(user.getPoke().getSkills().get(i));
        }
//        Log.i("before information log","here");
//        Log.i("trainactivity info", user.getPoke().getSkills().get(1).getName());
        return root;
    }
    private void updateData(){
        addrList.clear();
        for(int i = 0; i<user.getPoke().getSkills().size();i++)
        {
            addrList.add(user.getPoke().getSkills().get(i));
        }
        //Log.i("addrList info", addrList.get(1).getName());
        mAdapter.setmList(addrList);
        socketClient.notifyChange();
        TextView coinView = getActivity().findViewById(R.id.coin);
        coinView.setText(""+user.getCoin());

        TextView expView = getActivity().findViewById(R.id.exp);
        double expper = user.getPoke().getExp()*100/(Math.pow(user.getPoke().level,2)*100);
        expView.setText(String.format("%.2f%%", expper));

        TextView levelView = getActivity().findViewById(R.id.level);
        levelView.setText("Lv."+user.getPoke().getLevel());
        ProgressBar prog = getActivity().findViewById(R.id.progressBar);
        prog.setProgress((int)Math.round(expper));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        contentResolver = getActivity().getContentResolver();
        mAdapter = new TrainAdapter(context, addrList);
        final Animation skillupanim = AnimationUtils.loadAnimation(context,R.anim.skill_level_up);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView = view.findViewById(R.id.recyclerview_list);
        my_poke = view.findViewById(R.id.train_poke);
        Log.i("user pokemon number", user.getPoke().getNumber()+"");
        train_back = view.findViewById(R.id.train_back);
        lvup = view.findViewById(R.id.lvup);
        //my_poke.setImageResource(R.drawable.pokemon1);
        //train_back.setImageResource(R.drawable.home);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        setMy_poke(my_poke,user.getPoke().getNumber());
        mAdapter.setOnItemCLickListener(new TrainAdapter.OnItemClickListener() {
            @Override
        public void onUpClick(View v, int position){
            Skill s = mAdapter.getItem(position);
            if(user.getCoin()<s.getSkillcoin()) {
                Toast.makeText(view.getContext(), "코인이 부족합니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            if(Math.pow((position+1),2)>user.poke.getLevel()){
                Toast.makeText(view.getContext(), "레벨이 부족합니다. 필요레벨: "+(int)Math.pow((position+1),2), Toast.LENGTH_SHORT).show();
                return;
            }
            if(s.getLevel() == 99){
                Toast.makeText(view.getContext(), "최대 레벨입니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            user.setCoin(user.getCoin() - s.getSkillcoin());

                user.getPoke().getSkills().get(position).setDamage(s.getDamage()+position+1);
            user.getPoke().getSkills().get(position).setLevel(s.getLevel()+1);
                user.getPoke().getSkills().get(position).setSkillcoin();
                long newExp = user.getPoke().getExp()+(position+1)*user.getPoke().getSkills().get(position).getLevel();
                if(newExp >= Math.pow(user.getPoke().level,2)*100){
                    while(newExp >= Math.pow(user.getPoke().level,2)*100) {
                        newExp -= Math.pow(user.getPoke().level, 2) * 100;
                        user.getPoke().setLevel(user.getPoke().getLevel() + 1);

                        if ((user.getPoke().getLevel() == 3) || (user.getPoke().getLevel() == 5)) {
                            user.getPoke().setNumber(user.getPoke().getNumber() + 1);
                            Log.i("evolution", "" + user.getPoke().getNumber());
                            setMy_poke(my_poke, user.getPoke().getNumber());
                        }
                        socketClient.notifyChange();
                    }
                }
                user.getPoke().setExp(newExp);
            //코인 양 체크
            //코인 감소
                // 요구레벨 체크
            //맥스레벨 체크
                Log.i("getlevel after",""+user.getPoke().getSkills().get(position).getPower());
            //powerup
            //exp++
             updateData();
             mAdapter.setmList(addrList);
             lvup.setVisibility(view.VISIBLE);
             lvup.startAnimation(skillupanim);
             my_poke.startAnimation(skillupanim);
             mAdapter.notifyDataSetChanged();
             lvup.setVisibility(view.INVISIBLE);
        }

        @Override
        public void onItemClick(View v, int position, View itemView){
            modal(v,position);
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

                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(view.getContext(), "확인", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }
    public static void setMy_poke(ImageView img, int num){
        switch (num){
            case 0:
                img.setImageResource(R.drawable.pokemon1);
                break;
            case 1:
                img.setImageResource(R.drawable.pokemon2);
                break;
            case 2:
                img.setImageResource(R.drawable.pokemon3);
                break;
            case 3:
                img.setImageResource(R.drawable.pokemon4);
                break;
            case 4:
                img.setImageResource(R.drawable.pokemon5);
                break;
            case 5:
                img.setImageResource(R.drawable.pokemon6);
                break;
            case 6:
                img.setImageResource(R.drawable.pokemon7);
                break;
            case 7:
                img.setImageResource(R.drawable.pokemon8);
                break;
            case 8:
                img.setImageResource(R.drawable.pokemon9);
                break;
        }
    }
}
