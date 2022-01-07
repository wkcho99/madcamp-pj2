package com.example.week2;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.kakao.sdk.common.util.Utility;
import com.kakao.sdk.user.UserApi;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;
import com.kakao.sdk.user.model.Profile;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;

import java.io.BufferedWriter;

import java.io.IOException;

import java.io.InputStream;

import java.io.InputStreamReader;

import java.io.OutputStream;

import java.io.OutputStreamWriter;

import java.net.HttpURLConnection;

import java.net.MalformedURLException;

import java.net.URL;

public class LoginActivity extends Activity {
    private TextView tvData;
    Button kakaoTalkLogin, kakaoAccountLogin, kakaoLogout;
    Button loginButton;
    LinearLayout linearLayout;
    String userId = "1";
    String nickname = "조원경";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        viewInit();

        linearLayout.bringToFront();
        linearLayout.setVisibility(View.INVISIBLE);
        tvData = (TextView)findViewById(R.id.textView);
        // Log.d("keyhash", Utility.INSTANCE.getKeyHash(this));

        kakaoTalkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "카카오톡 버튼 클릭!", Toast.LENGTH_SHORT).show();

                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(getBaseContext())) {
                    UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this, (oAuthToken, error) -> {
                        if (error != null) {
                            Log.e("TAG", "로그인 실패", error);
                        } else if (oAuthToken != null) {
                            Log.i("TAG", "로그인 성공(토큰) : " + oAuthToken.getAccessToken());

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            Log.i("before sending",userId);
                            intent.putExtra("nickname", nickname);
                            startActivity(intent);
                            finish();
                        }
                        return null;
                    });
                    //new JSONTask().execute("http://172.10.5.68:443/post");
                }
                else {
                    UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this, (token, loginError) -> {
                        if(loginError != null){
                            Log.e("TAG", "로그인 실패", loginError);
                        } else{
                            Log.i("TAG", "로그인 성공(토큰) : " + token.getAccessToken());
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            Log.i("before sending",nickname);
                            intent.putExtra("nickname", nickname);
                            startActivity(intent);
                            finish();

                        }
                        return null;
                    });
                }
                setId();
                new JSONTask().execute("http://172.10.5.68/post");
            }
        });


        kakaoLogout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "정상적으로 로그아웃되었습니다.", Toast.LENGTH_SHORT).show();

                UserApiClient.getInstance().logout((error) -> {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return null;
                });
            }
        });
    }

    private void viewInit(){
        linearLayout = findViewById(R.id.linearLayout);
        loginButton = findViewById(R.id.loginButton);
        kakaoTalkLogin = findViewById(R.id.kakaoTalkLogin);
        kakaoLogout = findViewById(R.id.kakaoLogout);
    }

    public void kakaoError(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("user_id", userId);
                jsonObject.accumulate("name", nickname);

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    //URL url = new URL("http://172.10.5.68:443/users");
                    URL url = new URL(urls[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout(30000);
                    con.setConnectTimeout(30000);
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Cache-Control", "no-cache");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Accept", "text/html");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.connect();

                    OutputStream outStream = con.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();
                    InputStream stream = con.getInputStream();
                    JsonObject BODY = new JsonObject();
                            reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuilder buffer = new StringBuilder();

                    String line = "";
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }

                    return buffer.toString();

                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            tvData.setText(result);
        }

//        private void sendObject(){
//            JSONObject jsonObject = new JSONObject();
//            try{
//                jsonObject.put("nation", mJsonNationEt.getText().toString());
//                jsonObject.put("name", mJsonNameEt.getText().toString());
//                jsonObject.put("address", mJsonAddressEt.getText().toString());
//                jsonObject.put("age", mJsonAgeEt.getText().toString());
//            }catch (JSONException e){
//                e.printStackTrace();
//            }
//            receiveObject(jsonObject);
//        }
//        private void receiveObject(JSONObject data){
//            try{
//                    data.getString("nation"));
//                data.getString("nation"));
//                data.getString("address"));
//                data.getString("age"));
//            } catch (JSONException e){
//                e.printStackTrace();
//            }
//        }

    }
//    private void select_doProcess() {
//
//        HttpClient client = HttpClientBuilder.create().build();
//        HttpPost post = new HttpPost("http://192.168.219.101:8090/readus/insertuserinfo.do");
//        ArrayList<NameValuePair> nameValues =
//                new ArrayList<NameValuePair>();
//
//        try {
//            //Post방식으로 넘길 값들을 각각 지정을 해주어야 한다.
//            nameValues.add(new BasicNameValuePair(
//                    "userId", URLDecoder.decode(userId, "UTF-8")));
//            nameValues.add(new BasicNameValuePair(
//                    "userName", URLDecoder.decode(userName, "UTF-8")));
//
//            //HttpPost에 넘길 값을들 Set해주기
//            post.setEntity(
//                    new UrlEncodedFormEntity(
//                            nameValues, "UTF-8"));
//        } catch (UnsupportedEncodingException ex) {
//            Log.e("Insert Log", ex.toString());
//        }
//
//        try {
//            //설정한 URL을 실행시키기
//            HttpResponse response = client.execute(post);
//            //통신 값을 받은 Log 생성. (200이 나오는지 확인할 것~) 200이 나오면 통신이 잘 되었다는 뜻!
//            Log.i("Insert Log", "response.getStatusCode:" + response.getStatusLine().getStatusCode());
//
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
protected void setId(){
    UserApiClient.getInstance().me((user, throwable) -> {
        if(throwable != null){
            Log.e("tag", "사용자 정보 요청 실패" + throwable);
        } else{
            Account kakaoAccount = user.getKakaoAccount();
            Log.i("getuserid",Long.toString(user.getId()));
            userId = Long.toString(user.getId());
            if(kakaoAccount != null){
                Profile profile = kakaoAccount.getProfile();
                Log.i("tag", profile.getNickname());
                nickname = profile.getNickname();
            }
        }

        return null;
    });
}
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}