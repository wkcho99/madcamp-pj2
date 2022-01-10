package com.example.week2;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

import java.util.ArrayList;

public class AdventureActivity extends Fragment {
    private Context context;
    private ContentResolver contentResolver;
    private AnimationDrawable animationDrawable;
    SocketClient socketClient;
    MainActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentResolver = getActivity().getContentResolver();
        socketClient = (SocketClient) getActivity().getApplicationContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.adventure_activity, container, false);
        return root;
        //return new GameView(getActivity());

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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        User user = socketClient.getUser();
        context = getActivity();
        contentResolver = getActivity().getContentResolver();
        ImageView adventure_back = view.findViewById(R.id.adventure_back);
        ImageView adventure_poke = view.findViewById(R.id.adventure_poke);
        setdot_poke(adventure_poke,user.getPoke().getNumber());
            final Animation adventuring = AnimationUtils.loadAnimation(getActivity(), R.anim.adventuring);
            animationDrawable = (AnimationDrawable) adventure_back.getBackground();
            animationDrawable.start();
        adventuring.setAnimationListener(new Animation.AnimationListener(){
                @Override
                public void onAnimationEnd(Animation animation){ // 애니메이션이 끝났을 때
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
            @Override
            public void onAnimationStart(Animation animation){
            }

            @Override
            public void onAnimationRepeat(Animation animation){
            }
        });
        adventure_poke.startAnimation(adventuring);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(activity != null) activity.onFragmentChange(1);
            }
        }, 3000);

    }
    public static void setdot_poke(ImageView img, int num){
        switch (num){
            case 0:
                img.setImageResource(R.drawable.dot1);
                break;
            case 1:
                img.setImageResource(R.drawable.dot2);
                break;
            case 2:
                img.setImageResource(R.drawable.dot3);
                break;
            case 3:
                img.setImageResource(R.drawable.dot4);
                break;
            case 4:
                img.setImageResource(R.drawable.dot5);
                break;
            case 5:
                img.setImageResource(R.drawable.dot6);
                break;
            case 6:
                img.setImageResource(R.drawable.dot7);
                break;
            case 7:
                img.setImageResource(R.drawable.dot8);
                break;
            case 8:
                img.setImageResource(R.drawable.dot9);
                break;
        }
    }
}