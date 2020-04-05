package com.example.amusementpark;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class order_history extends AppCompatActivity {
    private ArrayList<Ticket> mylist = new ArrayList<>();
    private ArrayList<Ticket> pre_list = new ArrayList<>(); //previous list



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_history);


        /* Receive parameter from previous page */
        pre_list = (ArrayList<Ticket>) getIntent().getSerializableExtra("mylist");



        if(pre_list != null){
            for(int i = 0; i< pre_list.size(); i++){
                mylist.add(pre_list.get(i));
            }
        }




        /* Add List view in myCart*/
        ListView myListView = (ListView) findViewById(R.id.orders);
        ticketAdapter adapter = new ticketAdapter();
        myListView.setAdapter(adapter);




    }

    /*
     * list view adapter for cart_adapter_view.xml
     */
    class ticketAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if(mylist==null){
                return 0;
            }else {
                return mylist.size();
            }

        }

        @Override
        public Object getItem(int position) {

            return mylist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if( convertView == null ){
                //We must create a View:
                convertView = getLayoutInflater().inflate(R.layout.cart_adapter_view,parent,false);
            }
            //Here we can do changes to the convertView, such as set a text on a TextView
            //or an image on an ImageView.
            //Initialization
            TextView t_type = (TextView) convertView.findViewById(R.id.listview_type);
            TextView t_price = (TextView) convertView.findViewById(R.id.listview_price);
            TextView t_date = (TextView) convertView.findViewById(R.id.listview_date);
            Spinner t_numSpinner = (Spinner) convertView.findViewById(R.id.listview_num);
            ImageView t_image = (ImageView) convertView.findViewById(R.id.listview_image);


            t_type.setText(mylist.get(position).getType());
            System.out.println(t_type.toString());
            t_date.setText(mylist.get(position).getDate());
            t_price.setText(mylist.get(position).getPrice());

            //t_delete.setImageResource(R.drawable.delete);
            t_image.setImageResource(R.drawable.ticket4);

            /*
             *  Display the same ticket number in shopping cart as selected in the previous activity.
             */
            //Number of tickets from the previous activity
            String ticketNumber = mylist.get(position).getNumber();
            //Set ticket numbers adapter
            ArrayAdapter<CharSequence> Num_adapter = ArrayAdapter.createFromResource(order_history.this, R.array.NumOfTickets, android.R.layout.simple_list_item_1);
            Num_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            t_numSpinner.setAdapter(Num_adapter);
            if (ticketNumber != null) {
                int spinnerPosition = Num_adapter.getPosition(ticketNumber);
                t_numSpinner.setSelection(spinnerPosition);
            }

            //disable the numSpinner
            t_numSpinner.setEnabled(false);

            return convertView;

        }


    }
}
