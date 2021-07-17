package com.example.hc_05;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    StringBuilder sb = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        System.out.println(btAdapter.getBondedDevices());

        BluetoothDevice hc05 = btAdapter.getRemoteDevice("78:D8:5D:10:1D:0C");
        System.out.println(hc05.getName());

        BluetoothSocket btSocket = null;
        int counter = 0;
        do {
            try {
                btSocket = hc05.createRfcommSocketToServiceRecord(mUUID);
                System.out.println(btSocket);
                btSocket.connect();
                System.out.println(btSocket.isConnected());
                System.out.println("Device is connected");
            } catch (IOException e) {
                e.printStackTrace();
            }
            counter++;
            System.out.println("Trying to connect");
        } while (!btSocket.isConnected() && counter < 3);


        try {
            OutputStream outputStream = btSocket.getOutputStream();
            outputStream.write(49);
            Toast.makeText(this , "Sending Data ....." , Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream inputStream = null;
        try {
            inputStream = btSocket.getInputStream();
            inputStream.skip(inputStream.available());
            Toast.makeText(this , "Receiving Data ....." , Toast.LENGTH_LONG).show();



            for (int i = 0; i < 12; i++) {

                byte b = (byte) inputStream.read();
                System.out.println((char) b);
                sb.append((char) b);
//                Toast.makeText(MainActivity.this , b , Toast.LENGTH_SHORT).show();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println();

        Toast.makeText(this , sb.toString() , Toast.LENGTH_LONG).show();;


        try {
            btSocket.close();
            System.out.println(btSocket.isConnected());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}