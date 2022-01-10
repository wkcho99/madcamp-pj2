package com.example.week2;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RaidMyAdapter extends RecyclerView.Adapter<RaidMyAdapter.CustomViewHolder> {
    private ArrayList <String> mList;
    private Context context;
    private User user;
    public RaidMyAdapter(Context context, ArrayList<Skill> list) {
        this.context = context;
        this.mList = list;
    }

    /* CustomViewHolder constructed with textViews */
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected TextView rank;
        protected TextView damage_tot;
        public CustomViewHolder(View view) {
            super(view);
            this.name = view.findViewById(R.id.name);
            this.rank = view.findViewById(R.id.rank);
            this.damage_tot = view.findViewById(R.id.skillcost);

        }
    }
    public void setmList(ArrayList<Skill> list){
        this.mList = list;
        notifyDataSetChanged();
    }
    /* Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type
     * to represent an item. */
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rank_list, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }
    /* Called when notifyItemChanged */
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {
        Skill data;
        data = mList.get(position);
        viewholder.name.setText(data.getName());
        viewholder.level.setText("LV."+Integer.toString(data.getLevel()));
        viewholder.bt.setText("cost: "+data.getSkillcoin()+"\n"+"power: "+data.getDamage());
        Log.i("viewholder:",data.getName()+data.getLevel());
    }
    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
    public Skill getItem(int position){ return mList.get(position);}
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}