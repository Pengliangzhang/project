package com.example.amusementpark;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

public class account extends AppCompatActivity {

    private TextView tv_account;
    private TextView tv_back;
    private EditText et_username;
    private EditText et_curr_password;
    private EditText et_confirm_password;
    private Button bt_update_info;
    private String username;
    private String sessionID;
    private String currPassword;
    private String confirmPassword;
    private Button bt_logout;

    private static final int SUCCESS = 1;
    private static final int FAIL = 0;
    private boolean PASS = false;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case SUCCESS:
                    PASS = true;
                    Intent intent = new Intent(account.this, MainPage.class);
                    toast("update successful");
                    sessionID="";
                    startActivity(intent);
                    break;
                case FAIL:
                    toast("failure");
                    PASS = false;
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);
        setTitle("Account");

        // hide keyboard
        ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftInput(view.getWindowToken());
            }
        });

        // elements initialization
        tv_account = (TextView) findViewById(R.id.tv_account);
        tv_back = (TextView) findViewById(R.id.tv_back);
        et_username = (EditText) findViewById(R.id.et_username);
        et_curr_password = (EditText) findViewById(R.id.et_curr_password);
        et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
        bt_update_info = (Button) findViewById(R.id.bt_update_info);
        bt_logout=(Button) findViewById(R.id.bt_logout);

        // receive parameter from previous page
        Intent getIntent = getIntent();
        username = getIntent.getStringExtra("username");
        sessionID = getIntent.getStringExtra("sessionID");
        System.out.println("session ID from previous page = " + sessionID);
        System.out.println("username from previous page = " + username);

        et_username.setText(username);

        //listener for textview account
        tv_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(account.this, account.class);
                intent.putExtra("username", username);
                intent.putExtra("sessionID", sessionID);
                startActivity(intent);
            }
        });

        //listener for textview back
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(account.this, functions.class);
                intent.putExtra("username", username);
                intent.putExtra("sessionID", sessionID);
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
                    bt_update_info.setEnabled(Boolean.FALSE);
                } else {
                    username = et_username.getText().toString();
                    if (currPassword != null && currPassword != null && confirmPassword != null) {
                        bt_update_info.setEnabled(Boolean.TRUE);
                    }
                }
            }
        });


        //edit text listener for current password
        et_curr_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(et_curr_password.getText())) {
                    bt_update_info.setEnabled(Boolean.FALSE);
                } else {
                    currPassword = et_curr_password.getText().toString();
                    if (username != null && currPassword != null && confirmPassword != null) {
                        bt_update_info.setEnabled(Boolean.TRUE);
                    }
                }
            }
        });

        //edit text listener for current password
        et_confirm_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(et_confirm_password.getText())) {
                    bt_update_info.setEnabled(Boolean.FALSE);
                } else {
                    confirmPassword = et_confirm_password.getText().toString();
                    if (username != null && currPassword != null && confirmPassword != null) {
                        bt_update_info.setEnabled(Boolean.TRUE);
                    }
                }
            }
        });

        // button for update information
        bt_update_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInformation(username, currPassword, confirmPassword)) {

                    updatePassword(username, confirmPassword);

                } else {
                    et_username.setText(username);
                    et_curr_password.setText("");
                    et_confirm_password.setText("");
                    toast("Password different.");
                }
            }
        });

        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(account.this, MainPage.class);
                startActivity(intent);
            }
        });
    }


    // check passwords are same
    private boolean checkInformation(String username, String currPassword, String confirmPassword) {
        if (username != null && currPassword.equals(confirmPassword)) {
            System.out.println("username = " + username);
            System.out.println("current password = " + currPassword);
            System.out.println("confirm password = " + confirmPassword);

            return true;
        } else {
            return false;
        }

    }

    private void updatePassword(final String username, final String password) {

        int result = 0;

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                Message message = handler.obtainMessage();
                try {

                    myConnection urlbase = new myConnection();
                    String surl = urlbase.getUrl() + "userinfo";//!!!!!!!!!!
                    System.out.println("surl " + surl);
                    URL url = new URL(surl);
                    connection = (HttpURLConnection) url.openConnection();


                    connection.setDoOutput(true);// 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在; http正文内，因此需要设为true, 默认情况下是false;
                    connection.setDoInput(true);// 设置是否从httpUrlConnection读入，默认情况下是true;
                    connection.setUseCaches(false);// Post 请求不能使用缓存
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    connection.addRequestProperty("Cookie", sessionID);
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
                        System.out.println("whole results: " + re);
                        int res = Integer.parseInt(re.substring(8, 9));

                        System.out.println("result code from server: " + res);
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
