package com.example.amusementpark;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
    private static TextView tv_StationOne;
    private static TextView tv_StationTwo;
    private static TextView tv_StationThree;
    private static TextView tv_TimeOne;
    private static TextView tv_TimeTwo;
    private static TextView tv_TimeThree;
    private TextView tv_scan_qr;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private String username;
    private static String station = null;


    private static String message;
    private static String id= null;
    private int i;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    toast("Cancel Successfully");
                    break;
                case 2:
                    toast("Cannot find infomation in this queue");
                    break;
                case 3:
                    toast("Add successfully");
                    break;
                case 4:
                    toast("already in this queue");
                    break;
                case 5:
                    toast("An error found");
                    break;

            }
        }
    };

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
                                                     SynCancelQueue("59v7e54ook3cnxprabcdefgh",tv_StationOne.getText().toString());
                                                     tv_StationOne.setText("");
                                                     tv_TimeOne.setText("");

                                                 }
                                             }
        );

        // button for cancel the second queue
        bt_CancelQueueTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SynCancelQueue("59v7e54ook3cnxprabcdefgh",tv_StationTwo.getText().toString());
                tv_StationTwo.setText("");
                tv_TimeTwo.setText("");
                }
            }
        );

        // button for cancel the third queue
        bt_CancelQueueThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SynCancelQueue("59v7e54ook3cnxprabcdefgh",tv_StationThree.getText().toString());
                tv_StationThree.setText("");
                tv_TimeThree.setText("");
            }
        });

        // button for Add queue
        bt_AddQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AddStation("rollercoaster"+ i,"1");
                i=i+1;
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
            toast("You have reached limit three times at the moment");
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
                Message message = handler.obtainMessage();
                try {
                    QueueConnection urlbase= new QueueConnection();
                    String surl= urlbase.getUrl()+"dequeueFROMwait";
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
                    System.out.println(body);

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
                            message.what = 1;
                        } else
                        {
                            message.what = 2;
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                handler.sendMessage(message);

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
                Message message = handler.obtainMessage();
                try {
                    QueueConnection urlbase= new QueueConnection();
                    String surl= urlbase.getUrl()+"enqueue";
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
                    System.out.println(body);
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
                            message.what = 3;
                        } else if(res==0) {
                            message.what = 4;
                        }
                        else {
                            message.what = 5;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }handler.sendMessage(message);
            }
        }).start();
    }


    private static String SynWaitingTime(String UserID,String Station) {//HTTP GET Request
        int result = 0;
        station = Station;
        id= UserID;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    QueueConnection urlbase = new QueueConnection();
                    String surl = urlbase.getUrl() + "updateposition";
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




    public static void main()
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
