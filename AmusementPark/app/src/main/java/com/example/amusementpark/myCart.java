package com.example.amusementpark;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;

import java.util.ArrayList;
import java.util.List;


public class myCart extends AppCompatActivity {

    private Button next_btn;

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
}
