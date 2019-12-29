package com.example.amusementpark;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;


public class buy_tickets extends AppCompatActivity {

    private static final String TAG ="buy_tickets";
    private TextView mydisplayDate;
    private DatePickerDialog.OnDateSetListener mydateSetListener;
    private Spinner ticketTypeSpinner, ticketNumSpinner;
    private String text;
    private int t;
    private Button addtocart_btn;
    private ImageView cartimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_tickets);

        /*Date Picker*/
        //Initialize TextView
        mydisplayDate = (TextView) findViewById(R.id.selectDate_hint);
        mydisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        buy_tickets.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mydateSetListener,
                        year,month,day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mydateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                //Log.d(TAG,"date:"+year+"/"+month+"/"+dayOfMonth);
                String date = month+"/"+dayOfMonth+"/"+year;
                mydisplayDate.setText(date);
            }
        };


        /*Spinners for selecting ticket type and amount*/
        //Initialize Spinners
        ticketTypeSpinner = (Spinner) findViewById(R.id.type_spinner);
        ticketNumSpinner = (Spinner) findViewById(R.id.number_spinner);

        //set ticket types adapter
        ArrayAdapter<String> ticketTpyeAdapter = new ArrayAdapter<String>(
                buy_tickets.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.ticketTypes));
        ticketTypeSpinner.setAdapter(ticketTpyeAdapter);

        //Set ticket numbers adapter
        ArrayAdapter<String> ticketNumAdapter = new ArrayAdapter<String>(
                buy_tickets.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.NumOfTickets));
        ticketNumSpinner.setAdapter(ticketNumAdapter);




        /* Add to Cart Button Function*/
        //Initialization
        addtocart_btn = (Button) findViewById(R.id.addToCart);
        cartimage = (ImageView) findViewById(R.id.cartimage);

        //Make cart_ImageView clickable
        cartimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(buy_tickets.this, myCart.class);
                startActivity(intent);
            }
        });

        //Add_to_Cart Button
        addtocart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Make a toast
                Toast.makeText(getApplicationContext(),"Add to cart",Toast.LENGTH_SHORT).show();
            }
        });




    }



}
