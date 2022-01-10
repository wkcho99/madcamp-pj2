package com.example.week2;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;
import com.kakao.sdk.user.UserApiClient;

import org.w3c.dom.Text;

public class RaidActivity extends Fragment {
    private AnimationDrawable animationDrawable;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.raid_activity, container, false);
        ImageView raid_first = (ImageView) root.findViewById(R.id.raid_first);
        final Animation bigger = AnimationUtils.loadAnimation(getActivity(), R.anim.get_big);
        Glide.with(this).load(R.raw.raid_first).into(raid_first);
        Button bt = root.findViewById(R.id.start_bt);
        bt.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RaidEntered.class);
                startActivity(intent);
            }
        });
        TextView cando = root.findViewById(R.id.cando);
//        raid_first.startAnimation(bigger);
//        raid_first.setScaleX(2.0f);
//        raid_first.setScaleX(3.0f);
        return root;
        //return new GameView(getActivity());

    }

}
