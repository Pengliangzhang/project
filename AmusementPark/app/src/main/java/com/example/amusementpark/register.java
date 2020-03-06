package com.example.amusementpark;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import java.util.Timer;
import java.util.TimerTask;

public class register extends AppCompatActivity {
    private TextView tv_back;

    private EditText et_username;
    private EditText et_first_password;
    private EditText et_second_password;
    private EditText et_email;
    private Button bt_register;
    private TextView tv_notice_info;
    private TextView tv_mCountNumber;


    private String username;

    private static final int SUCCESS = 1;
    private static final int FAIL = 0;
    private static final int FAILUSERNAME = 2;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case SUCCESS:

                    toast("Please check your email for invitation!");




                    Intent intent = new Intent(register.this, MainPage.class);
                    startActivity(intent);
                    break;
                case FAIL:
                    toast("register failure");

                    break;
                case FAILUSERNAME:
                    tv_notice_info.setText("Username is occupied, Please reset");
                    break;


            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        setTitle("Register");

        // elements initialization
        tv_back = (TextView) findViewById(R.id.tv_back);

        et_username = (EditText) findViewById(R.id.et_username);
        et_first_password = (EditText) findViewById(R.id.et_first_password);
        et_second_password = (EditText) findViewById(R.id.et_second_password);
        et_email = (EditText) findViewById(R.id.et_email);
        bt_register = (Button) findViewById(R.id.bt_register);
        tv_notice_info = (TextView) findViewById(R.id.tv_notice_info);
        tv_mCountNumber = (TextView) findViewById(R.id.tv_mCountNumber);

        // hide keyboard
        ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftInput(view.getWindowToken());
            }
        });

        //listener for textview back
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(register.this, MainPage.class);
                startActivity(intent);
            }
        });

        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if not null
                if (!et_username.getText().toString().equals("") && !et_first_password.getText().toString().equals("") &&
                        !et_second_password.getText().toString().equals("") && !et_email.getText().toString().equals("")) {
                    //confirm password not equals to password
                    if (!et_first_password.getText().toString().equals(et_second_password.getText().toString())) {
                        tv_notice_info.setText("Two Password are NOT Same.");
                        //toast("two password are not same.");
                        et_first_password.setText("");
                        et_second_password.setText("");
                    } else {
                        register(et_username.getText().toString(), et_first_password.getText().toString(), et_email.getText().toString());

                    }
                } else {
                    tv_notice_info.setText("Cannot be Null");
                    //toast("cannot be null");
                }
            }

        });


    }


    private void register(final String username, final String password, final String email) {
        int result = 0;

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                Message message = handler.obtainMessage();
                try {

                    myConnection urlbase = new myConnection();
                    String surl = urlbase.getUrl() + "usersignup";
                    System.out.println("surl " + surl);
                    URL url = new URL(surl);

                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);// 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在; http正文内，因此需要设为true, 默认情况下是false;
                    connection.setDoInput(true);// 设置是否从httpUrlConnection读入，默认情况下是true;
                    connection.setUseCaches(false);// Post 请求不能使用缓存
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    connection.connect();
                    connection.setConnectTimeout(2 * 1000);//set connection timeout
                    connection.setReadTimeout(2 * 1000);//set reading data timeout

                    String body = "{\"ps\":\"" + password + "\",\"username\":\"" + username + "\",\"email\":" + email + "}";

                    System.out.println("body " + body);

                    JSONObject jsonObject = new JSONObject(body);

                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                    writer.write(String.valueOf(jsonObject));
                    writer.close();

                    int responseCode = connection.getResponseCode();
                    System.out.println("response code " + responseCode);

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        //定义 BufferedReader输入流来读取URL的响应
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String re = "";
                        String line;
                        while ((line = in.readLine()) != null) {
                            re += line;

                        }
                        System.out.println("re " + re);
                        int res = Integer.parseInt(re.substring(8, 9));
                        System.out.println("result " + res);
                        if (res == 0) {
                            message.what = FAIL;

                        } else if (res == 1) {
                            message.what = SUCCESS;

                        }
                    } else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                        message.what = FAILUSERNAME;
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

    //hide keyboard
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void toast(String s) {
        Toast.makeText(getApplication(), s, Toast.LENGTH_SHORT).show();
    }

}
