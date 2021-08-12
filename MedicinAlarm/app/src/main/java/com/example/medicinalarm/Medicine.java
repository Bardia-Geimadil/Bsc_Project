package com.example.medicinalarm;

import java.util.ArrayList;
import java.util.List;

public class Medicine {

    String name;
    int hour;
    int min;


    public Medicine(String name, int hour, int min) {
        this.name = name;
        this.hour = hour;
        this.min = min;
    }

    public Medicine() {

    }


    public static List<Medicine> medicineList = new ArrayList<>();

}
