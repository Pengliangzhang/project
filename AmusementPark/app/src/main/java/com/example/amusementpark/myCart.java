package com.example.amusementpark;

import android.content.Intent;
import android.graphics.drawable.AnimatedStateListDrawable;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

@SuppressWarnings("unchecked")
public class myCart extends AppCompatActivity {

    private ArrayList<Ticket> mylist = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_cart);


        /* Add Step Progress Bar */
        HorizontalStepView setpview = (HorizontalStepView) findViewById(R.id.step_view);
        List<StepBean> stepsBeanList = new ArrayList<>();
        StepBean stepBean0 = new StepBean("Summary",0);
        StepBean stepBean1 = new StepBean("Pay",-1);
        StepBean stepBean2 = new StepBean("Done",-1);

        stepsBeanList.add(stepBean0);
        stepsBeanList.add(stepBean1);
        stepsBeanList.add(stepBean2);

         setpview
                .setStepViewTexts(stepsBeanList)//总步骤
                .setTextSize(15)//set textSize
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(myCart.this, android.R.color.darker_gray))//设置StepsViewIndicator完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(myCart.this, android.R.color.darker_gray))//设置StepsViewIndicator未完成线的颜色
                .setStepViewComplectedTextColor(ContextCompat.getColor(myCart.this, android.R.color.darker_gray))//设置StepsView text完成线的颜色
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(myCart.this, android.R.color.darker_gray))//设置StepsView text未完成线的颜色
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(myCart.this, R.drawable.right))//设置StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(myCart.this, R.drawable.default_icon))//设置StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(myCart.this, R.drawable.attention));//设置StepsViewIndicator AttentionIcon



        /* Add List view in myCart*/
        ListView myListView = (ListView) findViewById(R.id.mycart_listview);
        cartAdapter adapter = new cartAdapter();
        myListView.setAdapter(adapter);

        //Receive parameter from previous page
        //Intent getIntent = new Intent();
        mylist = (ArrayList<Ticket>) getIntent().getSerializableExtra("mylist");

        /* Next Step Button Function*/
        //Initialize Button
        Button next_btn = (Button) findViewById(R.id.next_bt1);

        //Switch to next page
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(myCart.this,payment.class);
                intent.putExtra("mylist", mylist);
                startActivity(intent);
            }
        });

    }

    /*
     * list view adapter for cart_adapter_view.xml
     */
     class cartAdapter extends BaseAdapter{

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
            ImageView t_delete = (ImageView) convertView.findViewById(R.id.listview_remove); //final
            Spinner t_numSpinner = (Spinner) convertView.findViewById(R.id.listview_num);
            ImageView t_image = (ImageView) convertView.findViewById(R.id.listview_image);


            t_type.setText(mylist.get(position).getType());
            System.out.println(t_type.toString());
            t_date.setText(mylist.get(position).getDate());
            t_price.setText(mylist.get(position).getPrice());

            t_delete.setImageResource(R.drawable.delete);
            t_image.setImageResource(R.drawable.ticket4);

            /*
            *  Display the same ticket number in shopping cart as selected in the previous activity.
            */
            //Number of tickets from the previous activity
            String ticketNumber = mylist.get(position).getNumber();
            //Set ticket numbers adapter
            ArrayAdapter<CharSequence> Num_adapter = ArrayAdapter.createFromResource(myCart.this, R.array.NumOfTickets, android.R.layout.simple_list_item_1);
            Num_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            t_numSpinner.setAdapter(Num_adapter);
            if (ticketNumber != null) {
                int spinnerPosition = Num_adapter.getPosition(ticketNumber);
                t_numSpinner.setSelection(spinnerPosition);
            }




            /* Delete ticket function */
            //individual delete
            t_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mylist.remove(position);
                    refresh();

                }
            });
            return convertView;

        }



    }

    /*
       refresh page
      */
    private void refresh() {
        finish();
        Intent intent1 = new Intent(this, myCart.class);
        intent1.putExtra("mylist", mylist);
        System.out.println("my list size now: "+mylist.size());
        startActivity(intent1);


    }


}
