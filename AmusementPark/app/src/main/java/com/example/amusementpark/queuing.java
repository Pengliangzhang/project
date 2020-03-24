package com.example.amusementpark;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.blikoon.qrcodescanner.QrCodeActivity;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class queuing extends AppCompatActivity {
    private TextView tv_account;
    private TextView tv_back;
    private TextView tv_StationName;
    private TextView tv_EstimateWaitingTime;
    private Button bt_CancelQueueOne;
    private Button bt_CancelQueueTwo;
    private Button bt_CancelQueueThree;
    private Button bt_AddQueue;
    private TextView tv_StationOne;
    private TextView tv_StationTwo;
    private TextView tv_StationThree;
    private TextView tv_TimeOne;
    private TextView tv_TimeTwo;
    private TextView tv_TimeThree;
    private TextView tv_scan_qr;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private String username;
    private String station = null;
    private String message;
    private String id= null;
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.queuing);
        setTitle("Queuing");

        // elements initialization
        tv_account = (TextView) findViewById(R.id.tv_account);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_scan_qr=(TextView) findViewById(R.id.tv_scan_qr);
        tv_StationName = (TextView) findViewById(R.id.tv_StationName);
        tv_StationOne = (TextView) findViewById(R.id.tv_StationOne);
        tv_StationTwo = (TextView) findViewById(R.id.tv_StationTwo);
        tv_StationThree = (TextView) findViewById(R.id.tv_StationThree);
        tv_TimeOne = (TextView) findViewById(R.id.tv_TimeOne);
        tv_TimeTwo = (TextView) findViewById(R.id.tv_TimeTwo);
        tv_TimeThree = (TextView) findViewById(R.id.tv_TimeThree);
        tv_EstimateWaitingTime = (TextView) findViewById(R.id.tv_EstimateWaitingTime);
        bt_CancelQueueOne = (Button) findViewById(R.id.bt_CancelQueueOne);
        bt_CancelQueueTwo = (Button) findViewById(R.id.bt_CancelQueueTwo);
        bt_CancelQueueThree = (Button) findViewById(R.id.bt_CancelQueueThree);
        bt_AddQueue = (Button) findViewById(R.id.bt_AddQueue);


        // receive parameter from previous page
        Intent getIntent = getIntent();
        username = getIntent.getStringExtra("username");
        System.out.println("username from previous page = "+username);

        //listener for textview account
        tv_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(queuing.this, account.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        //listener for textview back
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(queuing.this, functions.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        //listener for textview scan QR code
        tv_scan_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start the qr scan activity
                Intent intent = new Intent(queuing.this, QrCodeActivity.class);
                intent.putExtra("username", username);
                startActivityForResult( intent,REQUEST_CODE_QR_SCAN);
            }
        });

        // button for cancel the first queue
        bt_CancelQueueOne.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     if (!tv_StationTwo.getText().toString().equals("")) {
                                                         tv_StationOne.setText(tv_StationTwo.getText().toString());
                                                         tv_TimeOne.setText(tv_TimeTwo.getText().toString());
                                                         tv_StationTwo.setText(tv_StationThree.getText().toString());
                                                         tv_TimeTwo.setText(tv_TimeThree.getText().toString());
                                                     }
                                                     SynCancelQueue("59v7e54ook3cnxprabcdefgh",tv_StationOne.getText().toString());
                                                 }
                                             }
        );

        // button for cancel the second queue
        bt_CancelQueueTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (!tv_StationThree.getText().toString().equals("")) {
                    tv_StationTwo.setText(tv_StationThree.getText().toString());
                    tv_TimeTwo.setText(tv_TimeThree.getText().toString());
                }
                SynCancelQueue("59v7e54ook3cnxprabcdefgh",tv_StationTwo.getText().toString());
            }
        });

        // button for cancel the third queue
        bt_CancelQueueThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                tv_StationThree.setText("");
                tv_TimeThree.setText("");
                SynCancelQueue("59v7e54ook3cnxprabcdefgh",tv_StationThree.getText().toString());
            }
        });

        // button for Add queue
        bt_AddQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AddStation("rollercoaster","");
                SynAddQueue("59v7e54ook3cnxprabcdefgh","rollercoaster");
            }
        });


    }

    private void AddStation(String tv_StationName,String tv_EstimateWaitingTime)
    {
        if (tv_StationOne.getText().toString().equals("")) {
            tv_StationOne.setText(tv_StationName);
            tv_TimeOne.setText(tv_EstimateWaitingTime);
        }
        else if(tv_StationTwo.getText().toString().equals(""))
        {
            tv_StationTwo.setText(tv_StationName);
            tv_TimeTwo.setText(tv_EstimateWaitingTime);
        }
        else if(tv_StationThree.getText().toString().equals(""))
        {
            tv_StationThree.setText(tv_StationName);
            tv_TimeThree.setText(tv_EstimateWaitingTime);
        }
        else{
            System.out.print("You have reached limit three times at the moment");
        }
    }


    private void SynCancelQueue(String UserID, String Station) { //HTTP POST request
        int result = 0;
        id = UserID;
        station = Station;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    myConnection urlbase=new myConnection();
                    String surl= urlbase.getUrl()+"userlogin";
                    URL url = new URL(surl);
                    connection = (HttpURLConnection) url.openConnection();

                    connection.setDoOutput(true);// 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在; http正文内，因此需要设为true, 默认情况下是false;
                    connection.setDoInput(true);// 设置是否从httpUrlConnection读入，默认情况下是true;
                    connection.setUseCaches(false);// Post 请求不能使用缓存
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    connection.connect();
                    connection.setConnectTimeout(2 * 1000);//set connection timeout
                    connection.setReadTimeout(2 * 1000);//set reading data timeout


                    String body = "{\"id\":\"" + id + "\",\"facility\":\"" + station + "\"}";
                    System.out.println("body " + body);

                    JSONObject jsonObject = new JSONObject(body);

                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                    writer.write(String.valueOf(jsonObject));
                    writer.close();

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        //定义 BufferedReader输入流来读取URL的响应
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String re = "";
                        String line;
                        while ((line = in.readLine()) != null) {
                            re += line;

                        }
                        int res = Integer.parseInt(re.substring(8, 9));
                        if (res == 1) {
                            toast("Cancel queue successfully");

                        } else
                        {
                            toast("Error occured");
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }

            }
        }).start();
    }


    private void SynAddQueue(String UserId, String Station) { //HTTP POST request
        int result = 0;
        station = Station;
        id = UserId;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    myConnection urlbase=new myConnection();
                    String surl= urlbase.getUrl()+"userlogin";
                    URL url = new URL(surl);
                    connection = (HttpURLConnection) url.openConnection();

                    connection.setDoOutput(true);// 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在; http正文内，因此需要设为true, 默认情况下是false;
                    connection.setDoInput(true);// 设置是否从httpUrlConnection读入，默认情况下是true;
                    connection.setUseCaches(false);// Post 请求不能使用缓存
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    connection.connect();
                    connection.setConnectTimeout(2 * 1000);//set connection timeout
                    connection.setReadTimeout(2 * 1000);//set reading data timeout

                    String body = "{\"id\":\"" + id + "\",\"facility\":\"" + station + "\"}";
                    JSONObject jsonObject = new JSONObject(body);

                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                    writer.write(String.valueOf(jsonObject));
                    writer.close();

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        //定义 BufferedReader输入流来读取URL的响应
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String re = "";
                        String line;
                        while ((line = in.readLine()) != null) {
                            re += line;

                        }
                        int res = Integer.parseInt(re.substring(8, 9));
                        if (res == 1) {
                            toast("Join queue successfully");

                        } else if(res==0)
                        {
                            toast("You have already joined this queue");
                        }
                        else
                        {
                            toast("Error occured");
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }

            }
        }).start();
    }


    private String SynWaitingTime(String UserID,String Station) {//HTTP GET Request
        int result = 0;
        station = Station;
        id= UserID;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    myConnection urlbase = new myConnection();
                    String surl = urlbase.getUrl() + "userlogin";
                    System.out.println("surl " + surl);
                    URL url = new URL(surl);
                    connection = (HttpURLConnection) url.openConnection();


                    connection.setDoOutput(true);// 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在; http正文内，因此需要设为true, 默认情况下是false;
                    connection.setDoInput(true);// 设置是否从httpUrlConnection读入，默认情况下是true;
                    connection.setUseCaches(false);// Post 请求不能使用缓存
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    connection.connect();
                    connection.setConnectTimeout(2 * 1000);//set connection timeout
                    connection.setReadTimeout(2 * 1000);//set reading data timeout

                    String body = "{\"id\":\"" + id + "\",\"facility\":\"" + station + "\"}";
                    System.out.println("body " + body);

                    JSONObject jsonObject = new JSONObject(body);

                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                    writer.write(String.valueOf(jsonObject));
                    writer.close();

                    InputStream inputStream = connection.getInputStream();
                    //读取输入流
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    message = result.toString(); //变成String数据
                    inputStream.close();
                    connection.disconnect();


                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }

            }
        }).start();
        return message;
    }


    private void toast(String s) {
        Toast.makeText(getApplication(), s, Toast.LENGTH_SHORT).show();
    }








    public void main(String[] args)
    {
        while(true) {
            if (!tv_StationOne.getText().toString().equals(""))
            {
               SynWaitingTime("59v7e54ook3cnxprabcdefgh",tv_StationOne.getText().toString());
            }
            if(!tv_StationTwo.getText().toString().equals("")) {
                SynWaitingTime("59v7e54ook3cnxprabcdefgh",tv_StationTwo.getText().toString());
            }
            if(!tv_StationThree.getText().toString().equals("")) {
                SynWaitingTime("59v7e54ook3cnxprabcdefgh",tv_StationThree.getText().toString());
            }
            System.out.println(tv_StationOne.getText().toString()+","+tv_StationTwo.getText().toString()+","+tv_StationThree.getText().toString());
        }
    }

}
