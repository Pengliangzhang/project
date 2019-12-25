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

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;


public class buy_tickets extends AppCompatActivity {

    private static final String TAG ="buy_tickets";
    private TextView mydisplayDate;
    private DatePickerDialog.OnDateSetListener mydateSetListener;
    private Spinner ticketTypeSpinner, ticketNumSpinner,daysSpinner;
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
        daysSpinner = (Spinner) findViewById(R.id.days_spinner);

        //Set ticket types(with prices) adapter which depend on the days_spinner
        daysSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            ArrayAdapter<String> ticketTpyeAdapter = null;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text = daysSpinner.getSelectedItem().toString();
                t = Integer.valueOf(text);

                //Set ticket type adapter
                if(t==1){//1 day pass
                    ticketTpyeAdapter = new ArrayAdapter<String>(
                            buy_tickets.this,
                            android.R.layout.simple_list_item_1,
                            getResources().getStringArray(R.array.ticketTypes));
                }else if (t==2){ //2 day pass
                    ticketTpyeAdapter = new ArrayAdapter<String>(
                            buy_tickets.this,
                            android.R.layout.simple_list_item_1,
                            getResources().getStringArray(R.array.ticketTypes2));
                }else if(t==3){ //3 day pass
                    ticketTpyeAdapter = new ArrayAdapter<String>(
                            buy_tickets.this,
                            android.R.layout.simple_list_item_1,
                            getResources().getStringArray(R.array.ticketTypes3));
                }else{
                    //nothing
                }

                //ticketTpyeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ticketTypeSpinner.setAdapter(ticketTpyeAdapter);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        //Set ticket numbers adapter
        ArrayAdapter<String> ticketNumAdapter = new ArrayAdapter<String>(
                buy_tickets.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.NumOfTickets));
        ticketNumSpinner.setAdapter(ticketNumAdapter);

        //Set days adapter
        ArrayAdapter<String> daysAdapter = new ArrayAdapter<String>(
                buy_tickets.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.NumOfDays));
        daysSpinner.setAdapter(daysAdapter);


        /* Add to Cart Button Function*/
        //Initialization
        addtocart_btn = (Button) findViewById(R.id.addToCart);
        cartimage = (ImageView) findViewById(R.id.cartimage);
        cartimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(buy_tickets.this, myCart.class);
                startActivity(intent);
            }
        });

    }



}
