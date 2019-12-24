package com.example.amusementpark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
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

public class MainPage extends AppCompatActivity {


    private Button bt_login;
    private EditText et_username;
    private EditText et_password;
    private TextView tv_register;
    private TextView tv_forget_password;
    private String username = null;
    private String password = null;

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
                if (checkPassword(username, password)) {
                    System.out.println("user name = " + username);
                    System.out.println("password = " + password);
                    Intent intent = new Intent(MainPage.this, functions.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                } else {

                    et_password.setText("");
                    toast("password is incorrect.");
                }
            }
        });
    }


    private boolean checkPassword(String username, String password) {
        if (username.equals("a") && password.equals("1")) {
            return true;
        } else {
            return false;
        }
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
