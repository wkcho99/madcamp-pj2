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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

public class TrainActivity extends Fragment {
    private ArrayList<Skill> addrList = new ArrayList<Skill>();
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private Context context;
    User user;

    ImageView my_poke;
    ImageView train_back;
    TextView lvup;
    TextView skillcost;
    private TrainAdapter mAdapter;
    private ContentResolver contentResolver;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.train_activity, container, false);
        user = (User) getArguments().getSerializable("user");
        for(int i = 0; i<user.getPoke().getSkills().size();i++)
        {
            addrList.add(user.getPoke().getSkills().get(i));
        }
        Log.i("before information log","here");
        Log.i("trainactivity info", user.getPoke().getSkills().get(1).getName());
        return root;
    }
    private void updateData(){
        addrList.clear();
//        skill1.setCool(5.0f);
//        skill1.setLevel(1);
//        skill1.setPower(10);
//        skill1.setName("몸통박치기");
        for(int i = 0; i<user.getPoke().getSkills().size();i++)
        {
            addrList.add(user.getPoke().getSkills().get(i));
        }
        Log.i("addrList info", addrList.get(1).getName());
        mAdapter.setmList(addrList);
        //mAdapter.notifyDataSetChanged();
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
        train_back = view.findViewById(R.id.train_back);
        lvup = view.findViewById(R.id.lvup);
        my_poke.setImageResource(R.drawable.pokemon1);
        train_back.setImageResource(R.drawable.home);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);


        updateData();
        mAdapter.setOnItemCLickListener(new TrainAdapter.OnItemClickListener() {
            @Override
        public void onUpClick(View v, int position){
            Skill s = mAdapter.getItem(position);
            if(user.getCoin()<s.getSkillcoin()) {
                Toast.makeText(view.getContext(), "코인이 부족합니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            if(Math.pow(position,2)>user.poke.getLevel()){
                Toast.makeText(view.getContext(), "레벨이 부족합니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            if(s.getLevel() == 99){
                Toast.makeText(view.getContext(), "최대 레벨입니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            user.setCoin(user.getCoin() - s.getSkillcoin());

                Log.i("getlevel before",""+user.getPoke().getSkills().get(position).getPower());
                user.getPoke().getSkills().get(position).setPower(s.getPower()+position+1);
            user.getPoke().getSkills().get(position).setLevel(s.getLevel()+1);
                user.getPoke().getSkills().get(position).setSkillcoin();
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
//        if (pay) {
//            msg = "주문을 접수 하시겠습니까?";
//        } else {
//            msg = "본 주문은 무통장 입금 주문입니다.\n입금 확인이 되었다면 확인 버튼을 눌러주세요.";
//        }

        new AlertDialog.Builder(view.getContext())
                .setTitle("스킬 정보")
                .setMessage(msg)

                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(view.getContext(), "확인", Toast.LENGTH_SHORT).show();
                        //((Activity) view.getContext()).finish();
                    }
                }).show();
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(view.getContext(), "코인을 결제하시겠습니까?", Toast.LENGTH_SHORT).show();
//                        //((Activity) view.getContext()).finish();
//                    }
//                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//                Toast.makeText(view.getContext(), "코인이 부족합니다.", Toast.LENGTH_SHORT).show();
//
//            }
    }
}
