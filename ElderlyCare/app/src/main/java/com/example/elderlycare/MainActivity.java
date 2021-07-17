package com.example.elderlycare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {


    public final static int REQ_BT_ENABLE = 1212 ;
    private static final int PERM_REQ_CODE = 1234;


    BluetoothAdapter bluetoothAdapter;

    ArrayAdapter<String> discoveredDeviceAdapter;

    BroadcastReceiver discoveryReceiver;


    Dialog devicesDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    if(bluetoothAdapter == null)
    {
         finish();
    }


    initReceiver();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERM_REQ_CODE);
            }
        }


    }


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
                Toast.makeText(MainActivity.this , "BT is active" , Toast.LENGTH_LONG).show();
            }
            else
            {
                finish();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERM_REQ_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this , "Permission granted" , Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Permission denied, closing application", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void initReceiver() {

        discoveryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

              String action  = intent.getAction();
              if(BluetoothDevice.ACTION_FOUND.equals(action))
              {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    discoveredDeviceAdapter.add(device.getName());

              }
              else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
              {
                  Toast.makeText(MainActivity.this ,discoveredDeviceAdapter.getCount() +
                          "Devices Found" , Toast.LENGTH_LONG ).show();

              }

            }
        };
    }

    public void showDevicesDialog() {

        devicesDialog = new Dialog(this);
        devicesDialog.setContentView(R.layout.devices_dialog);

        ListView devices_listView = devicesDialog.findViewById(R.id.devices_listView);

        if(bluetoothAdapter.isDiscovering())
            bluetoothAdapter.cancelDiscovery();


        bluetoothAdapter.startDiscovery();

        discoveredDeviceAdapter = new ArrayAdapter<String>(this , android.R.layout.simple_list_item_1);
        devices_listView.setAdapter(discoveredDeviceAdapter);


        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(discoveryReceiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(discoveryReceiver, filter);

        resizeDialog(devicesDialog);
        devicesDialog.show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.mainmenu , menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if(item.getTitle().equals("Refresh"))
        {
            showDevicesDialog();
        }


        return super.onOptionsItemSelected(item);
    }



    private void resizeDialog(Dialog dialog) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout((int) (0.9 * metrics.widthPixels), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}