package com.example.amusementpark;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ticket_info extends AppCompatActivity {
    private Button buyticket_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticket_info);

        /* Buy Ticket Button Function*/
        buyticket_bt = (Button) findViewById(R.id.buyTicket);
        //Switch to the next page
        buyticket_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ticket_info.this,buy_tickets.class);
                startActivity(intent);
            }
        });
    }
}
