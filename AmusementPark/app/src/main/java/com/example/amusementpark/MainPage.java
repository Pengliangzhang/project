package com.example.amusementpark;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.http.cookie.Cookie;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


import java.net.CookieHandler;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainPage extends AppCompatActivity {


    private Button bt_login;
    private EditText et_username;
    private EditText et_password;
    private TextView tv_register;
    private TextView tv_forget_password;
    private String username = null;
    private String password = null;

    private static final int SUCCESS = 1;
    private static final int FAIL = 0;
    private boolean PASS = false;

    public CookieManager cookieManager = null;
    public static String cookies;

    private String sessionID = "";

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case SUCCESS:
                    PASS = true;
                    Intent intent = new Intent(MainPage.this, functions.class);
                    intent.putExtra("username", username);
                    intent.putExtra("sessionID", sessionID);
                    startActivity(intent);
                    break;
                case FAIL:
                    toast("password incorrect");
                    et_password.setText("");
                    PASS = false;
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        // hide keyboard
        ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftInput(view.getWindowToken());
            }
        });

        // elements initialization
        bt_login = (Button) findViewById(R.id.bt_login);
        et_username = (EditText) findViewById(R.id.et_curr_password);
        et_password = (EditText) findViewById(R.id.et_password);
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_forget_password = (TextView) findViewById(R.id.tv_forget_password);

        // click listener for register
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, register.class);
                startActivity(intent);
            }
        });

        // click listener for forget password
        tv_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, forgetPassword.class);
                startActivity(intent);
            }
        });


        //edit text listener for username
        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(et_username.getText())) {
                    bt_login.setEnabled(Boolean.FALSE);
                } else {
                    username = et_username.getText().toString();
                    if (password != null) {
                        bt_login.setEnabled(Boolean.TRUE);
                    }
                }
            }
        });


        //edit text listener for password
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(et_password.getText())) {
                    bt_login.setEnabled(Boolean.FALSE);
                } else {
                    password = et_password.getText().toString();
                    if (username != null) {
                        bt_login.setEnabled(Boolean.TRUE);
                    }
                }
            }
        });

        // button for login
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check username and password
                checkPassword(username, password);
            }
        });
    }


    private void checkPassword(final String username, final String password) {
        int result = 0;

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;


                Message message = handler.obtainMessage();
                try {

                    myConnection urlbase = new myConnection();
                    String surl = urlbase.getUrl() + "login";
                    System.out.println("surl " + surl);
                    URL url = new URL(surl);
                    connection = (HttpURLConnection) url.openConnection();

                    /*connection.setRequestMethod("GET");
                    //设置连接超时时间（毫秒）
                    connection.setConnectTimeout(5000);
                    //设置读取超时时间（毫秒）
                    connection.setReadTimeout(5000);
                    //返回输入流
                    InputStream in = connection.getInputStream();

                    //读取输入流
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    show(result.toString());
                    */

                    //add cookie header


                    connection.setDoOutput(true);// 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在; http正文内，因此需要设为true, 默认情况下是false;
                    connection.setDoInput(true);// 设置是否从httpUrlConnection读入，默认情况下是true;
                    connection.setUseCaches(true);// Post 请求不能使用缓存
                    connection.setRequestMethod("POST");

                    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    connection.connect();

                    connection.setConnectTimeout(2 * 1000);//set connection timeout
                    connection.setReadTimeout(2 * 1000);//set reading data timeout



                    String body = "{\"ps\":\"" + password + "\",\"username\":\"" + username + "\"}";
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

                    String[] aaa = connection.getHeaderField("Set-Cookie").split(";");
                    sessionID = aaa[0];
                    System.out.println("=======session ID: "+sessionID);

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
