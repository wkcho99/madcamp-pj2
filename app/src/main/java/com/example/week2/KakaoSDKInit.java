package com.example.week2;

import android.app.Application;
import com.kakao.sdk.common.KakaoSdk;

public class KakaoSDKInit extends Application {

    private static volatile KakaoSDKInit instance = null;

    public static KakaoSDKInit getGlobalApplicationContext() {
        if(instance == null) {
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        KakaoSdk.init(this, getString(R.string.kakaoApi));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }
}
