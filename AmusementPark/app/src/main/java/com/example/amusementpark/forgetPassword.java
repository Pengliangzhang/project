package com.example.amusementpark;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class forgetPassword extends AppCompatActivity {
    private TextView tv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpassword);
        setTitle("Forget Password");

        // elements initialization
        tv_back = (TextView) findViewById(R.id.tv_back);

        //listener for textview back
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(forgetPassword.this, MainPage.class);
                startActivity(intent);
            }
        });
    }
}