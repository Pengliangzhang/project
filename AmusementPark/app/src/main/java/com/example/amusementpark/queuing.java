package com.example.amusementpark;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blikoon.qrcodescanner.QrCodeActivity;

public class queuing extends AppCompatActivity {
    private TextView tv_account;
    private TextView tv_back;
    private TextView tv_scan_qr;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.queuing);
        setTitle("Queuing");

        // elements initialization
        tv_account = (TextView) findViewById(R.id.tv_account);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_scan_qr=(TextView) findViewById(R.id.tv_scan_qr);


        // receive parameter from previous page
        Intent getIntent = getIntent();
        username = getIntent.getStringExtra("username");
        System.out.println("username from previous page = "+username);

        //listener for textview account
        tv_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(queuing.this, account.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        //listener for textview back
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(queuing.this, functions.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        //listener for textview scan QR code
        tv_scan_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start the qr scan activity
                Intent intent = new Intent(queuing.this, QrCodeActivity.class);
                intent.putExtra("username", username);
                startActivityForResult( intent,REQUEST_CODE_QR_SCAN);
            }
        });

    }
}
