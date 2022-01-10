package com.example.week2.register;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.week2.R;

import java.util.List;

public class PagerAdapter extends androidx.viewpager.widget.PagerAdapter {

    Context context;
    List<PagerModel> pagerArr;
    LayoutInflater inflater;

    public PagerAdapter(Context context, List<PagerModel> pagerArr){
        this.context = context;
        this.pagerArr = pagerArr;

        inflater = ((Activity) context).getLayoutInflater();
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = inflater.inflate(R.layout.pager_list_item, container, false);

        ImageView iv = view.findViewById(R.id.pager_img);
        view.setTag(position%3);
        ((ViewPager) container).addView(view);
        PagerModel model = pagerArr.get(position%3);

        iv.setImageResource(model.getId());

        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((View) object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}
