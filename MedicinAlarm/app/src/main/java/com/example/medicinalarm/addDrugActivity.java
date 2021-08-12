package com.example.medicinalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class addDrugActivity extends AppCompatActivity {

    TimePicker timePicker;
    Button apply_btn;
    EditText medicineNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drug);


        timePicker = findViewById(R.id.timepicker);
        timePicker.setIs24HourView(true);

        medicineNameEditText = findViewById(R.id.medicineName_edittext);


        apply_btn = findViewById(R.id.apply_btn);


        apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Medicine medicine = new Medicine(medicineNameEditText.getText().toString()
                        , timePicker.getHour(), timePicker.getMinute());


                Medicine.medicineList.add(medicine);

                Toast.makeText(addDrugActivity.this , "Medicine Added " + Medicine.medicineList.size() , Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }

}

