package com.example.amusementpark;

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

import androidx.appcompat.app.AppCompatActivity;

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

        // receive parameter from previous page
        Intent getIntent = getIntent();
        username = getIntent.getStringExtra("username");
        sessionID = getIntent.getStringExtra("sessionID");
        System.out.println("username from previous page = "+username);

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
                if (updateInformation(username, currPassword, confirmPassword)) {
                    toast("update successful.");
                    Intent intent = new Intent(account.this, account.class);
                    intent.putExtra("username", username);
                    intent.putExtra("sessionID", sessionID);
                    startActivity(intent);
                } else {
                    et_username.setText(username);
                    et_curr_password.setText("");
                    et_confirm_password.setText("");
                    toast("update failure.");
                }
            }
        });
    }

    private boolean updateInformation(String username, String currPassword, String confirmPassword) {
        if (username != null && currPassword.equals(confirmPassword)) {
            System.out.println("username = "+username);
            System.out.println("current password = "+currPassword);
            System.out.println("confirm password = "+confirmPassword);
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
