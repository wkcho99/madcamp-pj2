package com.example.week2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends View {

    private static final int BLANK = 0; // 상태값 (대기상태)
    private static final int PLAY = 1; //게임이 진행중
    private static final int DELAY = 1500; // 도형 생성 시간 (1500 = 1.5초)

    private  int status;
    private Random rnd = new Random(); //난수값을 구하는 클래스
    private Activity mParent;

    public GameView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        mParent = (Activity)context;
        status = BLANK;

        //핸들러 실행
        handler.sendEmptyMessageDelayed(0,DELAY);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //캔버스를 검정색으로 두자
        canvas.drawColor(Color.BLACK);

    }

    //일정 간격으로 도형을 생성하기 위한 핸들러
    Handler handler = new Handler(){

        @Override
        public void handleMessage (@NonNull Message msg){
            status = PLAY;
            invalidate();

        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return false;
    }


}
