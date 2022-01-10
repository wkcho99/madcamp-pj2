package com.example.week2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RaidEnteredAdapter extends RecyclerView.Adapter<RaidEnteredAdapter.CustomViewHolder> {
    private ArrayList<Skill> mList;
    private Context context;
    private User user;
    static private OnItemClickListener3 mListener = null;

    public RaidEnteredAdapter(Context context, ArrayList<Skill> list) {
        this.context = context;
        this.mList = list;
    }

    public interface OnItemClickListener3 {
        void onUpClick3(int position);
    }

    public void setOnItemCLickListener3(RaidEnteredAdapter.OnItemClickListener3 listener) {
        mListener = listener;
    }

    /* CustomViewHolder constructed with textViews */
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected TextView level;
        protected Button bt;

        public CustomViewHolder(View view) {
            super(view);
            this.name = view.findViewById(R.id.skill_name);
            this.level = view.findViewById(R.id.skill_level);
            this.bt = view.findViewById(R.id.skillcost);
            int buttonWidth = 200;
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) bt.getLayoutParams();
            params.width = buttonWidth;
            params.leftMargin = buttonWidth;
            bt.setLayoutParams(params);
            bt.setText("사용");
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //mListener.onItemClick3(view, getAdapterPosition(), view);
                }
            });
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //스킬 레벨업
                    mListener.onUpClick3(getAdapterPosition());
                }
            });
        }
    }

    public void setmList3(ArrayList<Skill> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    /* Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type
     * to represent an item. */
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.skill_list, viewGroup, false);

        RaidEnteredAdapter.CustomViewHolder viewHolder = new RaidEnteredAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    /* Called when notifyItemChanged */
    @Override
    public void onBindViewHolder(@NonNull RaidEnteredAdapter.CustomViewHolder viewholder, int position) {
        Skill data;
        data = mList.get(position);
        Log.i("viewholder3:", data.getName() + data.getLevel());
        viewholder.name.setText(data.getName());
        viewholder.level.setText("LV." + Integer.toString(data.getLevel()));
        Log.i("viewholder3:", data.getName() + data.getLevel());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

    public Skill getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}