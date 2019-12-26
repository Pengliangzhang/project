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

public class payment extends AppCompatActivity {
    private Button confirm_bt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);

        /* Add Step Progress Bar*/
        HorizontalStepView setpview = (HorizontalStepView) findViewById(R.id.step_view2);
        List<StepBean> stepsBeanList = new ArrayList<>();
        StepBean stepBean0 = new StepBean("Summary",1);
        StepBean stepBean1 = new StepBean("Pay",0);
        StepBean stepBean2 = new StepBean("Done",-1);

        stepsBeanList.add(stepBean0);
        stepsBeanList.add(stepBean1);
        stepsBeanList.add(stepBean2);

        setpview
                .setStepViewTexts(stepsBeanList)//总步骤
                .setTextSize(15)//set textSize
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(payment.this, android.R.color.darker_gray))//设置StepsViewIndicator完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(payment.this, android.R.color.darker_gray))//设置StepsViewIndicator未完成线的颜色
                .setStepViewComplectedTextColor(ContextCompat.getColor(payment.this, android.R.color.darker_gray))//设置StepsView text完成线的颜色
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(payment.this, android.R.color.darker_gray))//设置StepsView text未完成线的颜色
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(payment.this, R.drawable.right))//设置StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(payment.this, R.drawable.default_icon))//设置StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(payment.this, R.drawable.attention));//设置StepsViewIndicator AttentionIcon

        /* Confirm Button Function*/
        confirm_bt = (Button) findViewById(R.id.next_bt2);
        //Switch to the next page
        confirm_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(payment.this,paymentDone.class);
                startActivity(intent);
            }
        });
    }
}
