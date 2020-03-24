package com.example.amusementpark;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;



public class forgetPassword extends AppCompatActivity {
    private TextView tv_back;
    private static final int SUCCESS = 1;
    private static final int FAIL = 0;
    private boolean PASS = false;
    private EditText et_usermame;
    private EditText et_email;
    private Button bt_submit;


    private String sessionID = "";

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case SUCCESS:
                    PASS = true;
                    toast("successful");
                    Intent intent = new Intent(forgetPassword.this, MainPage.class);
                    startActivity(intent);
                    break;
                case FAIL:
                    toast("failure");
                    PASS = false;
                    break;

            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpassword);
        setTitle("Forget Password");

        // elements initialization
        tv_back = (TextView) findViewById(R.id.tv_back);
        et_usermame=(EditText) findViewById(R.id.et_username);
        et_email=(EditText) findViewById(R.id.et_email);
        bt_submit=(Button) findViewById(R.id.bt_submit);

        //listener for textview back
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(forgetPassword.this, MainPage.class);
                startActivity(intent);
            }
        });

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("username: "+et_usermame.getText().toString() );
                System.out.println("email: "+et_email.getText().toString());
                submitInfo(et_usermame.getText().toString(),et_email.getText().toString());
            }
        });
    }

    private void submitInfo(final String username, final String email) {
        int result = 0;

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;


                Message message = handler.obtainMessage();
                try {

                    myConnection urlbase = new myConnection();
                    String surl = urlbase.getUrl() + "forgetpassword";
                    System.out.println("surl " + surl);
                    URL url = new URL(surl);
                    connection = (HttpURLConnection) url.openConnection();


                    connection.setDoOutput(true);// 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在; http正文内，因此需要设为true, 默认情况下是false;
                    connection.setDoInput(true);// 设置是否从httpUrlConnection读入，默认情况下是true;
                    connection.setUseCaches(true);// Post 请求不能使用缓存
                    connection.setRequestMethod("POST");

                    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    connection.connect();

                    connection.setConnectTimeout(2 * 1000);//set connection timeout
                    connection.setReadTimeout(2 * 1000);//set reading data timeout



                    String body = "{\"email\":\"" + email + "\",\"username\":\"" + username + "\"}";
                    System.out.println("body " + body);

                    JSONObject jsonObject = new JSONObject(body);


                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                    writer.write(String.valueOf(jsonObject));
                    writer.close();

                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        //定义 BufferedReader输入流来读取URL的响应
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String re = "";
                        String line;
                        while ((line = in.readLine()) != null) {
                            re += line;

                        }
                        int res = Integer.parseInt(re.substring(8, 9));
                        if (res == 0) {
                            message.what = FAIL;

                        } else if (res == 1) {
                            message.what = SUCCESS;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                handler.sendMessage(message);

            }
        }).start();


    }

    private void toast(String s) {
        Toast.makeText(getApplication(), s, Toast.LENGTH_SHORT).show();
    }
}