package com.example.elderlyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class FaultdetectedActivity extends AppCompatActivity implements View.OnClickListener {

    Button fine_btn , call_btn;
    ImageView war_img;

    Thread thread;
    boolean callBoolean = false;

    SharedPreferences preferences;
    String number;
    String callingDelay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faultdetected);


        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        number = preferences.getString("numberKey" , null);
        callingDelay = preferences.getString( "delayKey" , "10000");


        war_img = findViewById(R.id.war_img);
        fine_btn = findViewById(R.id.fine_btn);
        call_btn = findViewById(R.id.call_btn);

        callBoolean = true;


        war_img.startAnimation(AnimationUtils.loadAnimation(this , R.anim.warning_anim));


        fine_btn.setOnClickListener(this);
        call_btn.setOnClickListener(this);



        final Handler handler = new Handler();

         thread = new Thread(new Runnable() {
            @Override
            public void run() {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(callBoolean) {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + number));
                            startActivity(callIntent);
                            finish();

                        }

                    }
                }, (Integer.parseInt(callingDelay))*1000);


            }
        });
         thread.start();


    }

    @Override
    public void onClick(View view) {

        if(view.getId() == fine_btn.getId())
        {
            callBoolean= false;
            finish();

        }

        if(view.getId() == call_btn.getId())
        {
            callBoolean= false;
            Toast.makeText(this , "Calling ... " ,Toast.LENGTH_SHORT ).show();
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+number));//change the number
            startActivity(callIntent);
            finish();

        }



    }
}