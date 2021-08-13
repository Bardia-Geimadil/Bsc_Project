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

    ImageView img;
    Button addDrug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        addDrug = findViewById(R.id.addDrug);
        img = findViewById(R.id.imageview);


        Utils.fillItems(this);



        addDrug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this , addDrugActivity.class));

            }
        });


        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                System.out.println("In timerrrrrrrrr");


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

                            Intent intent = new Intent(MainActivity.this , ShowAlarmActivity.class);
                            intent.putExtra("name" , Medicine.medicineList.get(i).name);

                            startActivity(intent);
                        }
                    }
                }

            }
        }  , 0 , 55000);


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