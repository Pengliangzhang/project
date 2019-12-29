package com.example.amusementpark;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;

import java.util.ArrayList;
import java.util.List;


public class myCart extends AppCompatActivity {

    private Button next_btn;
    int[] image = {R.drawable.ticket,R.drawable.ticket};
    String[] type = {"Student","Adult"};
    String[] price = {"$15","$20"};
    //String[] remove ={"remove","remove"};





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_cart);


        /* Add Step Progress Bar*/
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


        /* Next Step Button Function*/
        //Initialize Button
        next_btn = (Button) findViewById(R.id.next_bt1);

        //Switch to next page
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(myCart.this,payment.class);
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
            return image.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.cart_adapter_view,null);

            //Initialization
            TextView t_type = (TextView) convertView.findViewById(R.id.listview_type);
            TextView t_price = (TextView) convertView.findViewById(R.id.listview_price);
            ImageView t_remove = (ImageView) convertView.findViewById(R.id.listview_remove);
            Spinner t_numSpinner = (Spinner) convertView.findViewById(R.id.listview_num);
            ImageView t_image = (ImageView) convertView.findViewById(R.id.listview_image);

            //Set ticket numbers adapter
            ArrayAdapter<String> ticketNumAdapter = new ArrayAdapter<String>(
                    myCart.this,
                    android.R.layout.simple_list_item_1,
                    getResources().getStringArray(R.array.NumOfTickets));

            //Set up each info.
            t_type.setText(type[position]);
            t_price.setText(price[position]);
            t_remove.setImageResource(R.drawable.delete);
            t_numSpinner.setAdapter(ticketNumAdapter);
            t_image.setImageResource(image[position]);

            return convertView;
        }
    }

}
