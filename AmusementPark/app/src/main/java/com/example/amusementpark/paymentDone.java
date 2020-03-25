package com.example.amusementpark;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;

@SuppressWarnings("unchecked")
public class paymentDone extends AppCompatActivity {
    private String date,email;
    private  int value, type;
    private  Date expireD;
    private ArrayList<Ticket> mylist = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_done);


        /* Receive parameter from previous page */
        Intent getIntent = new Intent();
        mylist = (ArrayList<Ticket>) getIntent().getSerializableExtra("mylist");


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

        /*See My Tickets TextView Function*/
        TextView myticket = (TextView) findViewById(R.id.textView);

        //Switch to ticket_info page
        myticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(paymentDone.this,ticket_info.class);
                intent.putExtra("mylist", mylist);
                startActivity(intent);
            }
        });


        /*
            Send Ticket info. to database when the payment is done
         */
        for(int i=0; i < mylist.size();i++){
            String str_value = mylist.get(i).getPrice();
            String str_type = mylist.get(i).getType();
            value = Integer.parseInt(str_value.substring(1));

            switch (str_type.substring(0,2)){
                case "Ch":
                    type = 1;
                    break;

                case "Ad":
                    type = 2;
                    break;

                case "Se":
                    type = 3;
                    break;

                case "St":
                    type = 4;
                    break;

                default:
                    System.out.println("no match");
            }


            date = mylist.get(i).getDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                expireD = dateFormat.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            String strD =dateFormat.format(expireD);
            email = "12345@icloud.com"; //temporary email for testing

            //System.out.println("++++++++++++++++++++++++:   "+value+" "+type+" "+expire+"  "+email);

            //Send the info.
            SendTicketInfo(value,type,strD,email);
        }

    }



    /* Send ticket info. to database */
    private void SendTicketInfo(final int value, final int type, final String expire, final String email){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader in = null;
                BufferedWriter out = null;
                Message message = handler.obtainMessage();

                try{

                    //String str_url = "http://0d3d2083.ngrok.io/" + "buytickets";
                    String str_url = "http://35.230.92.53/buytickets" ;
                    System.out.println("str_url " + str_url);
                    URL url = new URL(str_url);
                    connection = (HttpURLConnection) url.openConnection(); // 打开和URL之间的连接

                    // 发送POST请求必须设置如下两行
                    connection.setDoOutput(true);// 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在; http正文内，因此需要设为true, 默认情况下是false;
                    connection.setDoInput(true);// 设置是否从httpUrlConnection读入，默认情况下是true;
                    connection.setUseCaches(false);// Post 请求不能使用缓存
                    connection.setRequestMethod("POST");

                    // 设置通用的请求属性
                    connection.setRequestProperty("accept", "*/*");
                    connection.setRequestProperty("connection", "Keep-Alive");
                    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    //connection.addRequestProperty("Cookie", sessionID);
                    connection.connect();
                    connection.setConnectTimeout(2 * 1000);//set connection timeout
                    connection.setReadTimeout(2 * 1000);//set reading data timeout


                    String body = "{\"value\":\"" + value + "\",\"type\":\"" + type + "\",\"expire\":\"" + expire + "\",\"email\":\"" + email + "\"}";
                    System.out.println("body " + body);

                    JSONObject jsonObject = new JSONObject(body);

                    out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                    out.write(String.valueOf(jsonObject));
                    //out.flush(); //++
                    out.close();

                    int responseCode = connection.getResponseCode();
                    System.out.println("response code " + responseCode);

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        //定义 BufferedReader输入流来读取URL的响应
                         in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
                        String result = "";
                        String line;
                        while ((line = in.readLine()) != null) {
                            result += line;

                        }
                        //in.close();//++

                        System.out.println(" result: -------------------: " + result);
                        //int res = Integer.parseInt(result.substring(8, 9));
                        //int res = Integer.parseInt(result);

                        //System.out.println("result code from server: " + res);
                        /*
                        if (res == 0) {
                            //message.what = FAIL;
                        } else if (res == 1) {

                            //message.what = SUCCESS;
                        }

                         */
                    }else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST){
                        System.out.println("FFFFFFFFFail!!!!!!!!!!");
                    }





                }catch (IOException e){
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally { // 使用finally块来关闭输入流
                    if(connection!=null){
                        connection.disconnect();
                    }
                }
                handler.sendMessage(message);
            }
        }).start();
    }

    //Handler
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                //case:


            }
        }

    };









}
