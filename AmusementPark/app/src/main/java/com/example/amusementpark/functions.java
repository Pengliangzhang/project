package com.example.amusementpark;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class functions extends AppCompatActivity {

    private TextView tv_account;
    private TextView tv_back;
    private Button bt_buy_ticket;
    private Button bt_queuing;
    private Button bt_parking;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.functions);

        // elements initialization
        tv_account = (TextView) findViewById(R.id.tv_account);
        tv_back = (TextView) findViewById(R.id.tv_back);
        bt_buy_ticket = (Button) findViewById(R.id.bt_buy_ticket);
        bt_queuing = (Button) findViewById(R.id.bt_queuing);
        bt_parking = (Button) findViewById(R.id.bt_parking);

        // receive parameter from previous page
        Intent getIntent = getIntent();
        username = getIntent.getStringExtra("username");
        System.out.println("username from previous page = "+username);

        //listener for textview account
        tv_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(functions.this, account.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        //listener for textview back
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(functions.this, MainPage.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        //button listener for buy ticket

        //button listener for queuing
        bt_queuing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(functions.this, queuing.class);
                intent.putExtra("username", username);
                startActivity(intent);

            }
        });

        //button listener for parking
        bt_parking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(functions.this, parking.class);
                intent.putExtra("username", username);
                startActivity(intent);

            }
        });
    }
}