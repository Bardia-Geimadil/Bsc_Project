package com.example.medicinalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

public class MedicineListActivity extends AppCompatActivity {


    ListView medicineListView;

    MedicineAdapter medicineAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_list);


        medicineListView = findViewById(R.id.medicine_listview);

        medicineAdapter = new MedicineAdapter(this , Medicine.medicineList);

        medicineListView.setAdapter(medicineAdapter);

    }
}