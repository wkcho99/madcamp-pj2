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
import java.util.List;
import java.util.Map;

public class RaidGuildAdapter extends RecyclerView.Adapter<RaidGuildAdapter.CustomViewHolder> {
    private List<Map.Entry<String, Integer>> mList;
    private ArrayList<Integer> ranks = new ArrayList<Integer>();
    private Context context;
    private User user;
    public RaidGuildAdapter(Context context, List<Map.Entry<String, Integer>> list) {
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
            this.damage_tot = view.findViewById(R.id.damage_tot);

        }
    }
    public void setmList( List<Map.Entry<String, Integer>> list){
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
        viewholder.name.setText(mList.get(position).getKey());
        if((position != 0) && (mList.get(position).getValue() == mList.get(position-1).getValue()))
        {
            //Log.i("mlist pos & value",mList.get(position).getValue()+","+.getValue());
            int temp = ranks.get(position-1);
            viewholder.rank.setText(temp+"");
            ranks.add(temp);
        }
        else {
            viewholder.rank.setText(Integer.toString(position+1));
            ranks.add(position+1);
        }
        viewholder.damage_tot.setText(Integer.toString(mList.get(position).getValue()));
    }
    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
    public Map.Entry<String, Integer> getItem(int position){ return mList.get(position);}
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}