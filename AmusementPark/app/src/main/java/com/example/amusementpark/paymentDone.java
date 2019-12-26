package com.example.amusementpark;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;

import java.util.ArrayList;
import java.util.List;

public class paymentDone extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_done);

        /* Add Step Progress Bar*/
        HorizontalStepView setpview = (HorizontalStepView) findViewById(R.id.step_view2);
        List<StepBean> stepsBeanList = new ArrayList<>();
        StepBean stepBean0 = new StepBean("Summary",1);
        StepBean stepBean1 = new StepBean("Pay",1);
        StepBean stepBean2 = new StepBean("Done",1);

        stepsBeanList.add(stepBean0);
        stepsBeanList.add(stepBean1);
        stepsBeanList.add(stepBean2);

        setpview
                .setStepViewTexts(stepsBeanList)//总步骤
                .setTextSize(15)//set textSize
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(paymentDone.this, android.R.color.darker_gray))//设置StepsViewIndicator完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(paymentDone.this, android.R.color.darker_gray))//设置StepsViewIndicator未完成线的颜色
                .setStepViewComplectedTextColor(ContextCompat.getColor(paymentDone.this, android.R.color.darker_gray))//设置StepsView text完成线的颜色
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(paymentDone.this, android.R.color.darker_gray))//设置StepsView text未完成线的颜色
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(paymentDone.this, R.drawable.right))//设置StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(paymentDone.this, R.drawable.default_icon))//设置StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(paymentDone.this, R.drawable.attention));//设置StepsViewIndicator AttentionIcon

    }
}
