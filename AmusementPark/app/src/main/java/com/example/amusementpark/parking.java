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

public class parking extends AppCompatActivity {


    private TextView tv_account;
    private TextView tv_back;
    private EditText et_plate_number;
    private Button bt_confirm;
    private String username;
    private String plateNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parking);

        // elements initialization
        tv_account = (TextView) findViewById(R.id.tv_account);
        tv_back = (TextView) findViewById(R.id.tv_back);
        et_plate_number = (EditText) findViewById(R.id.et_plate_number);
        bt_confirm = (Button) findViewById(R.id.bt_confirm);

        // receive parameter from previous page
        Intent getIntent = getIntent();
        username = getIntent.getStringExtra("username");
        System.out.println("username from previous page = " + username);

        // hide keyboard
        ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftInput(view.getWindowToken());
            }
        });

        //listener for textview account
        tv_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parking.this, account.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        //listener for textview back
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parking.this, functions.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        //edit text listener for plate number
        et_plate_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(et_plate_number.getText())) {
                    bt_confirm.setEnabled(Boolean.FALSE);
                } else {
                    plateNumber = et_plate_number.getText().toString();
                    bt_confirm.setEnabled(Boolean.TRUE);

                }
            }
        });


        // button for confirm plate number
        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updatePlateNumber(username, plateNumber)) {
                    System.out.println("user name = " + username);
                    System.out.println("plate number = " + plateNumber);
                    toast("confirm plate number");
                } else {
                    et_plate_number.setText("");
                    toast("failure");
                }
            }
        });

    }

    private boolean updatePlateNumber(String username, String plateNumber) {
        if (username != null && plateNumber != null) {
            return true;
        }
        return false;
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
