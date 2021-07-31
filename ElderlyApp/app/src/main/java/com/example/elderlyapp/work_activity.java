package com.example.elderlyapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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
    Button fault_btn;

    public final static int REQ_BT_ENABLE = 1212 ;
    public final static int PHONE_CALL_PER = 1313 ;



    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    BluetoothDevice hc05;
    BluetoothSocket btSocket = null;

    boolean connectionFlag = false;
    boolean faultDetected;

    BluetoothAdapter bluetoothAdapter;



    /////////////////////////////Variables/////////////////////////////////
    StringBuilder sb = new StringBuilder();
    int time_to_show;




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


         heartImage.setOnClickListener(this);


         faultDetected = false;



         fault_btn = findViewById(R.id.fault_btn);
         fault_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 startActivity(new Intent(work_activity.this , FaultdetectedActivity.class));
             }
         });



        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_PER);
            }
        }




    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PHONE_CALL_PER){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(work_activity.this , "Phone Permission granted" , Toast.LENGTH_LONG).show();
            } else {
               // Toast.makeText(this, "Permission denied, closing application", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public void communicate(){


        try {
            OutputStream outputStream = btSocket.getOutputStream();
            outputStream.write(49);
        } catch (IOException e) {
            e.printStackTrace();
        }



        while (!faultDetected){

            time_to_show++;

            if(time_to_show == 60)
                time_to_show=0;

            InputStream inputStream = null;
            try {
                inputStream = btSocket.getInputStream();
                inputStream.skip(inputStream.available());
                // Toast.makeText(this , "Receiving Data ....." , Toast.LENGTH_LONG).show();
                for (int i = 0; i < 3; i++) {

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

                  connectionFlag = true;
              }

            } catch (IOException e) {
                e.printStackTrace();
            }
            counter++;
            System.out.println("Trying to connect");
        } while (!btSocket.isConnected() && counter < 10);
    }


    @Override
    public void onClick(View view) {

        if(view.getId() == heartImage.getId()) {

            new communicateAsyncTask().executeOnExecutor(communicateAsyncTask.THREAD_POOL_EXECUTOR);
        }

    }

    class communicateAsyncTask extends AsyncTask<String , String , String>
    {

        @Override
        protected String doInBackground(String... objects) {


            try {
                OutputStream outputStream = btSocket.getOutputStream();
                outputStream.write(49);
            } catch (IOException e) {
                e.printStackTrace();
            }



            while (!faultDetected){

                InputStream inputStream = null;
                try {
                    inputStream = btSocket.getInputStream();
                    inputStream.skip(inputStream.available());
                    // Toast.makeText(this , "Receiving Data ....." , Toast.LENGTH_LONG).show();
                    for (int i = 0; i < 3; i++) {

                        byte b = (byte) inputStream.read();
                        System.out.println((char) b);
                        sb.append((char) b);
//                Toast.makeText(MainActivity.this , b , Toast.LENGTH_SHORT).show();

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                //beatRate_tv.setText(sb.toString());

                if(sb.toString().contains("Bardia")) {
                    faultDetected = true;
                    startActivity(new Intent(work_activity.this , FaultdetectedActivity.class));
                }

                time_to_show++;

                if(time_to_show == 10)
                {
                    time_to_show = 0;
                    publishProgress(sb.toString());
                }

                sb.setLength(0);

            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {

            System.out.println("We are hereeeeeeeeeeeeeeeeeeeeeeeeeeeeee");

                 beatRate_tv.setText(values[0] + " BPM");


        }
    }


    class connectionAsyncTask extends AsyncTask{


        @Override
        protected Object doInBackground(Object[] objects) {

            connectBluetooth();

            return null;
        }

    @Override
    protected void onPostExecute(Object o) {

        new AlertDialog.Builder(work_activity.this)
                .setTitle("Connected!")
                .setIcon(R.drawable.greencheck)
                .setCancelable(true)
                .setMessage("Device Connected Successfully!").show();
    }
}




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.work_menu , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getTitle().equals("Connect"))
        {

            new connectionAsyncTask().executeOnExecutor(connectionAsyncTask.THREAD_POOL_EXECUTOR);

        }

        if(item.getTitle().equals("Setting"))
        {
            startActivity(new Intent(work_activity.this , PreferanceScreen.class));
        }

        return super.onOptionsItemSelected(item);
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


}