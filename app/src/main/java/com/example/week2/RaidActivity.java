package com.example.week2;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.RadialGradient;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RaidActivity extends Fragment {
    Map<String,Integer> guild_member = new HashMap<>();
    Map<String,Integer> every_guild = new HashMap<>();
    List<Map.Entry<String, Integer>> list_entries;
    List<Map.Entry<String, Integer>> list_entries2;
    TextView inclass;
    private RecyclerView mRecyclerView1;
    private RecyclerView mRecyclerView2;
    private LinearLayoutManager mLayoutManager1;
    private LinearLayoutManager mLayoutManager2;
    private RaidGuildAdapter mAdapter1;
    private RaidMyAdapter mAdapter2;
    private SocketClient socketClient;
    Button bt;
    private MutableLiveData<Integer> raidCnt;
    private MutableLiveData<JSONArray> liveData;
    User user;
    private Context context;
    private ContentResolver contentResolver;
    MutableLiveData<Integer> raid_hp;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        socketClient = (SocketClient) getActivity().getApplicationContext();
        liveData = socketClient.raidInfo;
        raidCnt = socketClient.raidCnt;
        raid_hp = socketClient.bossHp;
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView cando = getActivity().findViewById(R.id.cando);
        cando.setText(user.getRaid_times()+"/3");
        socketClient.requestRaidInfo(liveData, raidCnt);
        socketClient.requestBossInfo(raid_hp);
        Log.i("i'm in resume","haha");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.raid_activity, container, false);

        socketClient.requestBossInfo(raid_hp);
        socketClient.requestRaidInfo(liveData, raidCnt);

        bt = root.findViewById(R.id.start_bt);
        bt.setEnabled(false);
        user = socketClient.getUser();
        liveData.observe(getViewLifecycleOwner(), new Observer<JSONArray>() {
            @Override
            public void onChanged(JSONArray jsonArray) {
                Log.i("raidInfo", "change?");
                try {
                    Log.i("raidActivity", ((JSONObject) liveData.getValue().get(1)).getString("guild"));
                    for(int i = 0; i <4; i++)
                    {
                        Integer guild_name = ((JSONObject)liveData.getValue().get(i)).getInt("guild");
                        Integer each_damage = 0;
                        int len = ((JSONObject)liveData.getValue().get(i)).getJSONArray("users").length();
                        while(len >0)
                        {
                            Log.i("raidactivity rank1", ""+ guild_name +"and"+ each_damage);
                            len--;
                            each_damage+=((JSONObject)((JSONObject)liveData.getValue().get(i)).getJSONArray("users").get(len)).getInt("damage");
                        }
                        every_guild.put(guild_name+"분반", each_damage);
                        Log.i("raid every guild", ""+ guild_name + each_damage);
                    }
                    list_entries = new ArrayList<Map.Entry<String, Integer>>(every_guild.entrySet());
                    Collections.sort(list_entries, new Comparator<Map.Entry<String, Integer>>() {
                        // compare로 값을 비교
                        public int compare(Map.Entry<String, Integer> obj1, Map.Entry<String, Integer> obj2) {
                            return obj2.getValue().compareTo(obj1.getValue());
                        }
                    });
                    Integer each_damage = 0;
                    String each_name;
                    int user_guild = user.getGuild()-1;
                    Log.i("user guild name", ((JSONObject)liveData.getValue().get(user_guild)).getJSONArray("users").length()+"");
                    inclass.setText(user.getGuild() + "분반 내 순위");
                    guild_member.clear();
                    for(int i = 0; i < ((JSONObject)liveData.getValue().get(user_guild)).getJSONArray("users").length();i++){
                        each_name = ((JSONObject)((JSONObject)liveData.getValue().get(user_guild)).getJSONArray("users").get(i)).getString("name");
                        each_damage=((JSONObject)((JSONObject)liveData.getValue().get(user_guild)).getJSONArray("users").get(i)).getInt("damage");
                        if(guild_member.containsKey(each_name))
                            guild_member.put(each_name+i,each_damage);
                        else
                            guild_member.put(each_name,each_damage);

                        Log.i("raid guild_member", ""+ each_name + each_damage + guild_member.entrySet().toString());

                    }
                    list_entries2 = new ArrayList<Map.Entry<String, Integer>>(guild_member.entrySet());
                    Collections.sort(list_entries2, new Comparator<Map.Entry<String, Integer>>() {
                        // compare로 값을 비교
                        public int compare(Map.Entry<String, Integer> obj1, Map.Entry<String, Integer> obj2) {
                            return obj2.getValue().compareTo(obj1.getValue());
                        }
                    });
                    TextView cando = root.findViewById(R.id.cando);
                    cando.setText(user.getRaid_times()+"/3");
                    mAdapter1.setmList(list_entries);
                    mAdapter2.setmList(list_entries2);
                    mAdapter1.notifyDataSetChanged();
                    mAdapter2.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        raid_hp.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (raid_hp == null || raid_hp.getValue() == null || raid_hp.getValue() == 0) {
                    bt.setEnabled(false);
                } else {
                    bt.setEnabled(true);
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
        mAdapter1 = new RaidGuildAdapter(context, list_entries);
        mLayoutManager1 = new LinearLayoutManager(getActivity());
        mRecyclerView1 = view.findViewById(R.id.guilds_rank);
        mRecyclerView1.setAdapter(mAdapter1);
        mRecyclerView1.setHasFixedSize(true);
        mRecyclerView1.setLayoutManager(mLayoutManager1);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView1.getContext(),
                mLayoutManager1.getOrientation());
        mRecyclerView1.addItemDecoration(dividerItemDecoration);
        mAdapter2 = new RaidMyAdapter(context, list_entries2);
        mLayoutManager2 = new LinearLayoutManager(getActivity());
        mRecyclerView2 = view.findViewById(R.id.my_rank);
        mRecyclerView2.setAdapter(mAdapter2);
        mRecyclerView2.setHasFixedSize(true);
        mRecyclerView2.setLayoutManager(mLayoutManager2);
        inclass = view.findViewById(R.id.inclassRanking);
        DividerItemDecoration dividerItemDecoration2 = new DividerItemDecoration(mRecyclerView2.getContext(),
                mLayoutManager2.getOrientation());
        mRecyclerView2.addItemDecoration(dividerItemDecoration);
        ImageView raid_first = (ImageView) view.findViewById(R.id.raid_first);
        final Animation bigger = AnimationUtils.loadAnimation(getActivity(), R.anim.get_big);
        Glide.with(this).load(R.raw.raid_first).into(raid_first);
        bt.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getRaid_times() == 0){
                    Toast.makeText(view.getContext(), "도전 횟수가 모두 소진되었습니다", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(raid_hp.getValue() == 0){
                    hp_modal(view);
                    return;
                }
                Intent intent = new Intent(getActivity(), RaidEntered.class);
                Log.i("intent hp", raid_hp.getValue()+"");
                intent.putExtra("raid_hp",raid_hp.getValue());
                startActivity(intent);
            }
        });
        //my_poke.setImageResource(R.drawable.pokemon1);
        //train_back.setImageResource(R.drawable.home);

    }
    public void hp_modal(final View view) {
        String msg;
        new AlertDialog.Builder(view.getContext())
                .setTitle("도전 불가")
                .setMessage("디아루가가 이미 처치되었습니다.")

                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(view.getContext(), "확인", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

}
