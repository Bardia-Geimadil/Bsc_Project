package com.example.medicinalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;

public class ShowAlarmActivity extends AppCompatActivity {


    TextClock textClock;
    TextView medicineName ;
    Button ok_btn;

    Ringtone myRingtone;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_alarm);


        textClock = findViewById(R.id.textClock);
        medicineName = findViewById(R.id.medicinename_dialog);
        ok_btn = findViewById(R.id.okDialog);




        Bundle extras = getIntent().getExtras();

        textClock.getFormat24Hour();
        medicineName.setText(extras.getString("name"));

        myRingtone = RingtoneManager.getRingtone(getApplicationContext() ,
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));

        myRingtone.play();


        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myRingtone.stop();
                finish();

            }
        });




    }
}