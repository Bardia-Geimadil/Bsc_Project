package com.example.elderlyapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class work_activity extends AppCompatActivity implements View.OnClickListener {

    ImageView heartImage;
    TextView beatRate_tv , heartRateCon_tv;

    public final static int REQ_BT_ENABLE = 1212 ;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    StringBuilder sb = new StringBuilder();
    BluetoothDevice hc05;
    BluetoothSocket btSocket = null;

    boolean connectionFlag = false;
    boolean faultDetected;

    BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_activity);


         heartImage = findViewById(R.id.heartImage);
         beatRate_tv = findViewById(R.id.beatRate_tv);
         heartRateCon_tv = findViewById(R.id.heartRateCon_tv);

         heartImage.startAnimation(AnimationUtils.loadAnimation(this , R.anim.heart_beat));

         bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

         hc05 = bluetoothAdapter.getRemoteDevice("78:D8:5D:10:1D:0C"); // this is the unique mac of my hc_0c
        String aaa = "aaaaa";
        beatRate_tv.setText(aaa);

         heartImage.setOnClickListener(this);

         //connectBluetooth();

         faultDetected = false;




    }

    public void communicate(){


        try {
            OutputStream outputStream = btSocket.getOutputStream();
            outputStream.write(49);
            Toast.makeText(this , "Sending Data ....." , Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!faultDetected){

            InputStream inputStream = null;
            try {
                inputStream = btSocket.getInputStream();
                inputStream.skip(inputStream.available());
                // Toast.makeText(this , "Receiving Data ....." , Toast.LENGTH_LONG).show();
                for (int i = 0; i < 12; i++) {

                    byte b = (byte) inputStream.read();
                    System.out.println((char) b);
                    sb.append((char) b);
//                Toast.makeText(MainActivity.this , b , Toast.LENGTH_SHORT).show();

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            //beatRate_tv.setText(sb.toString());

            if(sb.toString().equals("alaki"))
                faultDetected=true;

        }

    }

    public void connectBluetooth() {

         btSocket = null;
        int counter = 0;
        do {
            try {
                btSocket = hc05.createRfcommSocketToServiceRecord(myUUID);
                btSocket.connect();
                System.out.println(btSocket.isConnected());


              if(btSocket.isConnected()) {


//                  final Handler handler = new Handler();
//                  handler.postDelayed(new Runnable() {
//                      @Override
//                      public void run() {
//                          communicate();
//                      }
//                  }, 2000);
                  connectionFlag = true;
                  communicate();
//                  new AlertDialog.Builder(work_activity.this)
//                          .setTitle("Connected!")
//                          .setIcon(R.drawable.greencheck)
//                          .setCancelable(true)
//                          .setMessage("Device Connected Successfully!").show();
              }

            } catch (IOException e) {
                e.printStackTrace();
            }
            counter++;
            System.out.println("Trying to connect");
        } while (!btSocket.isConnected() && counter < 10);
    }


    // Checking is bluetooth is enabled
    @Override
    protected void onResume() {
        super.onResume();

        if(!bluetoothAdapter.isEnabled()) {

            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent , REQ_BT_ENABLE);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==REQ_BT_ENABLE)
        {
            if(resultCode == RESULT_OK)
            {
                Toast.makeText(work_activity.this , "BLUETOOTH is active" , Toast.LENGTH_LONG).show();
            }
            else
            {
                finish();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onClick(View view) {

        if(view.getId() == heartImage.getId())
            connectBluetooth();

    }
}