package com.example.amusementpark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainPage extends AppCompatActivity {


    private Button bt_login;
    private EditText et_username;
    private EditText et_password;
    private String username = null;
    private String password = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // elements initialization
        bt_login = (Button) findViewById(R.id.bt_login);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);


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
                }else{
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
                }
                else{
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
                if (checkPassword(et_username.getText().toString(), et_password.getText().toString())) {
                    Intent intent = new Intent(MainPage.this, functions.class);
                    //intent.putExtra("house_id", et_house_id.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }


    public boolean checkPassword(String username, String password) {
        if (!username.equals("a") && !password.equals("1")) {
            return false;
        }
        return true;
    }


}
