package com.example.medicinalarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity{

    Ringtone ringtone;
    TextClock textClock;
    Button dis_btn;
    TextView medicinenameAlarm;
    ImageView img;
    Button addDrug;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textClock = findViewById(R.id.main_text_clock);
        medicinenameAlarm = findViewById(R.id.medicinename_alarm);
        dis_btn = findViewById(R.id.dis_btn);
        addDrug = findViewById(R.id.addDrug);
        img = findViewById(R.id.imageview);



        ringtone = RingtoneManager.getRingtone(getApplicationContext() ,
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));


        addDrug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this , addDrugActivity.class));

            }
        });

        dis_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ringtone.stop();
                textClock.setVisibility(View.INVISIBLE);
                medicinenameAlarm.setVisibility(View.INVISIBLE);
                dis_btn.setVisibility(View.INVISIBLE);
                img.setVisibility(View.VISIBLE);
                addDrug.setVisibility(View.VISIBLE);

            }
        });

        Handler handler = new Handler();



        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {


                if(Medicine.medicineList.size() > 0)
                {

                    for (int i=0 ; i< Medicine.medicineList.size() ; i++)
                    {

                       int medicine_hour = Medicine.medicineList.get(i).hour ;
                       int medicine_min = Medicine.medicineList.get(i).min;

                       if(medicine_hour > 12)
                           medicine_hour -= 12;

                        if(medicine_hour == Calendar.getInstance().get(Calendar.HOUR) &&
                                medicine_min == Calendar.getInstance().get(Calendar.MINUTE))
                        {
                            String name = Medicine.medicineList.get(i).name;

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    ringtone.play();
                                    textClock.setVisibility(View.VISIBLE);
                                    medicinenameAlarm.setVisibility(View.VISIBLE);
                                    medicinenameAlarm.setText(name);
                                    dis_btn.setVisibility(View.VISIBLE);
                                    img.setVisibility(View.INVISIBLE);
                                    addDrug.setVisibility(View.INVISIBLE);
                                }
                            });


                        }
                    }
                }

            }
        }  , 0 , 35000);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.main_menu , menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getTitle().equals("My Medicine")){

            startActivity(new Intent(MainActivity.this , MedicineListActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}