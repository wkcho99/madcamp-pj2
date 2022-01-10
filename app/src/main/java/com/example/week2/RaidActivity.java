package com.example.week2;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.RadialGradient;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RaidActivity extends Fragment {
    Map<String,Integer> guild_member = new HashMap<>();
    Map<String,Integer> every_guild = new HashMap<>();
    private RecyclerView mRecyclerView1;
    private RecyclerView mRecyclerView2;
    private LinearLayoutManager mLayoutManager1;
    private LinearLayoutManager mLayoutManager2;
    private RaidGuildAdapter mAdapter1;
    private RaidMyAdapter mAdapter2;
    private SocketClient socketClient;

    private MutableLiveData<Integer> raidCnt;

    private MutableLiveData<JSONArray> liveData;
    User user;
    private Context context;
    private ContentResolver contentResolver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        socketClient = (SocketClient) getActivity().getApplicationContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.raid_activity, container, false);
      liveData = new MutableLiveData<>();
        raidCnt = new MutableLiveData<>();
        socketClient.requestRaidInfo(liveData, raidCnt);

        raidCnt.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.i("raidActivity raid cnt", " " + raidCnt.getValue());
            }
        });

        user = socketClient.getUser();
        liveData.observe(getViewLifecycleOwner(), new Observer<JSONArray>() {
            @Override
            public void onChanged(JSONArray jsonArray) {
                try {
                    Log.i("raidActivity", ((JSONObject) liveData.getValue().get(1)).getString("guild"));
                    for(int i = 0; i <4; i++)
                    {
                        Integer guild_name = ((JSONObject)liveData.getValue().get(i)).getInt("guild");
                        Integer j = 0;
                        Integer each_damage = 0;
                        while(((JSONObject)liveData.getValue().get(i)).getJSONArray("users").get(j)!=null)
                        {
                            each_damage+=((JSONObject)((JSONObject)liveData.getValue().get(i)).getJSONArray("users").get(j)).getInt("damage");
                        }
                        every_guild.put(guild_name+"분반", each_damage);
                    }
                    Integer each_damage = 0;
                    String each_name;
                    for(int i = 0; i < ((JSONObject)liveData.getValue().get(user.getGuild())).getJSONArray("users").length();i++){
                        each_name = ((JSONObject)((JSONObject)liveData.getValue().get(user.getGuild())).getJSONArray("users").get(i)).getString("name");
                        each_damage=((JSONObject)((JSONObject)liveData.getValue().get(user.getGuild())).getJSONArray("users").get(i)).getInt("damage");
                        guild_member.put(each_name,each_damage);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

//        raid_first.startAnimation(bigger);
//        raid_first.setScaleX(2.0f);
//        raid_first.setScaleX(3.0f);
        return root;
        //return new GameView(getActivity());

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
        ImageView raid_first = (ImageView) root.findViewById(R.id.raid_first);
        final Animation bigger = AnimationUtils.loadAnimation(getActivity(), R.anim.get_big);
        Glide.with(this).load(R.raw.raid_first).into(raid_first);
        Button bt = view.findViewById(R.id.start_bt);
        bt.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RaidEntered.class);
                startActivity(intent);
            }
        });
        TextView cando = view.findViewById(R.id.cando);
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

}
