package com.example.amusementpark;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.ArrayList;


public class parking extends AppCompatActivity {


    private TextView tv_account;
    private TextView tv_back;
    private EditText et_plate_number;
    private Button bt_confirm;
    private String username;
    private String sessionID;
    private static String plateNumber;
    private TextView tv_scan_qr;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private static final int SUCCESS = 1;
    private static final int FAIL = 0;
    private static final int INITIAL=2;
    private static final int INITIALFAIL=3;
    private boolean PASS = false;
    private TextView tv_message;
    private String message;



    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case SUCCESS:
                    PASS = true;
                    Intent intent = new Intent(parking.this, parking.class);
                    toast("confirmed");
                    intent.putExtra("username", username);
                    intent.putExtra("sessionID", sessionID);
                    intent.putExtra("plateNumber",msg.obj.toString());
                    System.out.println("message -> session ID = "+ msg.obj);
                    message ="successful submitted plate number!";
                    intent.putExtra("message",message);
                    startActivity(intent);
                    break;
                case FAIL:
                    toast("failure");

                    PASS = false;
                    break;
                case INITIAL:
                    if (msg.obj!=null){
                        bt_confirm.setText("update");
                        et_plate_number.setText(msg.obj.toString());
                    }

                    break;
                case INITIALFAIL:
                    bt_confirm.setText("confirm");
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parking);
        setTitle("Parking");

        // elements initialization
        tv_account = (TextView) findViewById(R.id.tv_account);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_scan_qr = (TextView) findViewById(R.id.tv_scan_qr);

        et_plate_number = (EditText) findViewById(R.id.et_plate_number);
        bt_confirm = (Button) findViewById(R.id.bt_confirm);
        tv_message=(TextView) findViewById(R.id.tv_message);


        // receive parameter from previous page
        Intent getIntent = getIntent();
        username = getIntent.getStringExtra("username");
        sessionID=getIntent.getStringExtra("sessionID");
        plateNumber=getIntent.getStringExtra("plateNumber");
        message=getIntent.getStringExtra("message");


        //initial plate number
        if(plateNumber!=null){
            et_plate_number.setText(plateNumber);
        }

        //initial message
        if(message!=""){
            tv_message.setText(message);
        }

        System.out.println("session ID from previous page = "+ sessionID);
        System.out.println("username from previous page = " + username);

        // hide keyboard
        ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftInput(view.getWindowToken());
            }
        });

        //listener for textview account
        tv_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parking.this, account.class);
                intent.putExtra("username", username);
                intent.putExtra("sessionID", sessionID);
                startActivity(intent);
            }
        });

        //listener for textview back
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parking.this, functions.class);
                intent.putExtra("username", username);
                intent.putExtra("sessionID", sessionID);
                startActivity(intent);
            }
        });

        //edit text listener for plate number
        et_plate_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(et_plate_number.getText())) {
                    bt_confirm.setEnabled(Boolean.FALSE);
                } else {
                    plateNumber = et_plate_number.getText().toString();
                    bt_confirm.setEnabled(Boolean.TRUE);

                }
            }
        });

        //listener for textview scan QR code
        tv_scan_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start the qr scan activity
                Intent intent = new Intent(parking.this, QrCodeActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("sessionID", sessionID);
                startActivityForResult(intent, REQUEST_CODE_QR_SCAN);
            }
        });


        // button for confirm plate number
        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePlateNumber(username, plateNumber);
                System.out.println("user name = " + username);
                System.out.println("plate number = " + plateNumber);
                //toast("confirm plate number");


            }
        });

        // initial plate number edit text
        getPlateNumber();

    }

    private void getPlateNumber() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                Message mess = handler.obtainMessage();
                try {

                    myConnection urlbase = new myConnection();
                    String surl = urlbase.getUrl() + "queryparkingspot";
                    System.out.println("surl " + surl);
                    URL url = new URL(surl);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.addRequestProperty("Cookie", sessionID);
                    connection.setRequestMethod("GET");//设置连接超时时间（毫秒）
                    connection.setConnectTimeout(5000);//设置读取超时时间（毫秒）
                    connection.setReadTimeout(5000);//返回输入流
                    InputStream in = connection.getInputStream();

                    //读取输入流
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    System.out.println("==result: "+result);
                    String [] temp;
                    temp=result.toString().split(",");

                    for(int i =0; i < temp.length ; i++){
                        System.out.println(temp[i]);
                        System.out.println("");
                    }

                    System.out.println("last item: "+temp[temp.length-1]);

                    //get last plate number
                    if (temp[temp.length-1].length()>13) {
                        String[] getLastPlateNum = temp[temp.length - 1].split(":");
                        int length = getLastPlateNum[getLastPlateNum.length - 1].length();
                        String lastPlateNum = getLastPlateNum[getLastPlateNum.length - 1].substring(1, length - 4);

                        System.out.println("last plate number: " + lastPlateNum);


                        mess.what = INITIAL;
                        mess.obj = lastPlateNum;
                    }
                    else{
                        mess.what=INITIALFAIL;
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                handler.sendMessage(mess);

            }
        }).start();


    }



    private void updatePlateNumber(final String username, final String plateNumber) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                Message message = handler.obtainMessage();
                try {

                    myConnection urlbase = new myConnection();
                    String surl = urlbase.getUrl() + "buyparkingspot";
                    System.out.println("surl " + surl);
                    URL url = new URL(surl);
                    connection = (HttpURLConnection) url.openConnection();

                    connection.setDoOutput(true);// 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在; http正文内，因此需要设为true, 默认情况下是false;
                    connection.setDoInput(true);// 设置是否从httpUrlConnection读入，默认情况下是true;
                    connection.setUseCaches(false);// Post 请求不能使用缓存
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    connection.addRequestProperty("Cookie", sessionID);
                    connection.connect();
                    connection.setConnectTimeout(2 * 1000);//set connection timeout
                    connection.setReadTimeout(2 * 1000);//set reading data timeout

                    String body = "{\"plate\":\"" + plateNumber + "\"}";
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
                        System.out.println("whole results: "+re);
                        int res = Integer.parseInt(re.substring(8, 9));

                        System.out.println("result code from server: "+ res);
                        if (res == 0) {
                            message.what = FAIL;
                        } else if (res == 1) {
                            message.obj=plateNumber;
                            message.what = SUCCESS;
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


    //hide keyboard
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void toast(String s) {
        Toast.makeText(getApplication(), s, Toast.LENGTH_SHORT).show();
    }

}
