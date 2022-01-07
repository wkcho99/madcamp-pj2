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
import com.kakao.auth.Session;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import org.json.JSONObject;
import android.content.Intent;
import android.util.Log;
import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
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
    Button kakaoLogin,kakaoLogout;
    LoginButton loginButton;
    LinearLayout linearLayout;
    private KaKaoCallBack kaKaoCallBack;
    String userId = "1";
    String userName = "조원경";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        viewInit();

        linearLayout.bringToFront();
        linearLayout.setVisibility(View.INVISIBLE);

        kaKaoCallBack = new KaKaoCallBack();
        Session.getCurrentSession().addCallback(kaKaoCallBack);
        Session.getCurrentSession().checkAndImplicitOpen();
        tvData = (TextView)findViewById(R.id.textView);
        kakaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"카카오 로그인 버튼 클릭!",Toast.LENGTH_SHORT).show();
                loginButton.performClick();
                new JSONTask().execute("http://172.10.5.68:80/post");
            }
        });

        kakaoLogout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "정상적으로 로그아웃되었습니다.", Toast.LENGTH_SHORT).show();

                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }

    private void viewInit(){
        linearLayout = findViewById(R.id.linearLayout);
        loginButton = findViewById(R.id.loginButton);
        kakaoLogin = findViewById(R.id.kakaoLogin);
        kakaoLogout = findViewById(R.id.kakaoLogout);
    }

    public void kakaoError(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }
    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("user_id", "1");
                jsonObject.accumulate("name", "조원경");

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    //URL url = new URL("http://172.10.5.68:80/users");
                    URL url = new URL(urls[0]);
                    con = (HttpURLConnection) url.openConnection();
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

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(kaKaoCallBack);
    }
    private class KaKaoCallBack implements ISessionCallback{

        LoginActivity loginActivity = new LoginActivity();

        @Override
        public void onSessionOpened() {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    int result = errorResult.getErrorCode();

                    if (result == ApiErrorCode.CLIENT_ERROR_CODE) loginActivity.kakaoError("네트워크 연결이 불안정합니다. 다시 시도해 주세요.");
                    else loginActivity.kakaoError("로그인 도중 오류가 발생했습니다.");
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    loginActivity.kakaoError("세션이 닫혔습니다. 다시 시도해 주세요.");
                }

                @Override
                public void onSuccess(MeV2Response result) {
                    Log.d("아이디 확인 : ",Long.toString(result.getId())); //닉네임
                    //Log.d("이름 확인 : ",result.getKakaoAccount().getLegalName()); //이메일
                    //Log.d("이미지 확인 : ",result.getThumbnailImagePath()); //프로필 사진
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();

                }
            });
        }

        @Override
        public void onSessionOpenFailed (KakaoException e){
            loginActivity.kakaoError("로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요.");
        }
    }
}