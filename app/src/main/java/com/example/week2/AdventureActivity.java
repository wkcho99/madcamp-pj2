package com.example.week2;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdventureActivity extends Fragment {
    private ArrayList<Skill> addrList = new ArrayList<Skill>();
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridManager;
    private Context context;
    User user;
    private AdventureAdapter mAdapter;
    private ContentResolver contentResolver;
    private AnimationDrawable animationDrawable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentResolver = getActivity().getContentResolver();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.adventure_activity, container, false);

        //Glide.with(this).load(R.raw.adventure).into(adventure_back);
        //root.setContentView(new MyGameView(this));

        user = (User) getArguments().getSerializable("user");
        for (int i = 0; i < user.getPoke().getSkills().size(); i++) {
            addrList.add(user.getPoke().getSkills().get(i));
        }
        mAdapter = new AdventureAdapter(context, addrList);
        return root;
        //return new GameView(getActivity());

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
        //mAdapter.setmList2(addrList);
        mAdapter.notifyDataSetChanged();
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
        Log.i("adventureactivity info1",addrList.get(1).getName());
        mGridManager = new GridLayoutManager(context, 2);
        mRecyclerView = view.findViewById(R.id.recyclerview_list2);
        ImageView adventure_back = view.findViewById(R.id.adventure_back);
        ImageView adventure_poke = view.findViewById(R.id.adventure_poke);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mGridManager);
        updateData();
        mRecyclerView.setAdapter(mAdapter);
        Log.i("activity addrlist",addrList.get(1).getName());
        //mAdapter.setmList2(addrList);
        final Animation adventuring = AnimationUtils.loadAnimation(getActivity(), R.anim.adventuring);
        animationDrawable = (AnimationDrawable) adventure_back.getBackground();
        animationDrawable.start();
        adventure_poke.startAnimation(adventuring);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mGridManager.HORIZONTAL);
        DividerItemDecoration dividerItemDecoration2 = new DividerItemDecoration(mRecyclerView.getContext(),
                mGridManager.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.addItemDecoration(dividerItemDecoration2);
        //mAdapter.setmList2(addrList);
        mAdapter.setOnItemCLickListener2(new AdventureAdapter.OnItemClickListener2() {
            @Override
            public void onItemClick2(View v, int position, View itemView) {
                modal(v, position);
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
                        Toast.makeText(view.getContext(), "확인", Toast.LENGTH_SHORT).show();
                        //((Activity) view.getContext()).finish();
                    }
                }).show();
    }
}