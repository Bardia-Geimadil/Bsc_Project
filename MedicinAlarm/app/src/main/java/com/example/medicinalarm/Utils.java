package com.example.medicinalarm;

import android.content.Context;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLOutput;

import javax.xml.transform.Source;

public class Utils {


    public static String file_content = "";


    public static void createFileContent(Context context)
    {

        StringBuilder sb = new StringBuilder();

        for (int i=0 ; i<Medicine.medicineList.size() ; i++){

            sb.append(Medicine.medicineList.get(i).name).append("\n");
            sb.append(Medicine.medicineList.get(i).hour).append("\n");
            sb.append(Medicine.medicineList.get(i).min).append("\n");
        }

        file_content = sb.toString();

        writeFile(context);

    }


    public static void fillItems(Context context)
    {

        readFile(context);

        String [] result = file_content.split("\n");


//       System.out.println("Result content is ...............");
//
//        for (int i=0;i<result.length ; i++)
//        {
//            System.out.println(result[i] +"\n");
//        }

       Medicine.medicineList.clear();

  //      System.out.println(result.length);

        for(int i=0 ; i<=result.length-3 ; i+=3)
        {
            Medicine currentMedicine = new Medicine();

            currentMedicine.name = result[i];
            currentMedicine.hour = Integer.parseInt(result[i+1]);
            currentMedicine.min = Integer.parseInt(result[i+2]);
            Medicine.medicineList.add(currentMedicine);
        }
//        int i =0;
//
//
//
//
//        while (i<result.length-1 )
//        {
//
//            Medicine currentMedicine = new Medicine();
//            currentMedicine.name = result[i];
//            currentMedicine.hour = Integer.parseInt(result[i+1]);
//            currentMedicine.min = Integer.parseInt(result[i+2]);
//            Medicine.medicineList.add(currentMedicine);
//            i +=3;
//            System.out.println("I was in whileeee");
//        }
//
//        System.out.println("Now the items are filled");
//        for (int j=0 ; j< Medicine.medicineList.size() ; j++)
//        {
//            System.out.println(Medicine.medicineList.get(j).name +"\n"+Medicine.medicineList.get(j).hour+"\n" + Medicine.medicineList.get(j).min+"\n");
//        }
    }

    public static void writeFile(Context context )
    {


        context.deleteFile("medicineFile.txt" );

        try {

            FileOutputStream fileOutputStream = context.openFileOutput( "medicineFile.txt" , Context.MODE_PRIVATE);
            fileOutputStream.write(file_content.getBytes());
            fileOutputStream.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }

    }


    public static void readFile(Context context)
    {

        try {

            FileInputStream fileInputStream = context.openFileInput("medicineFile.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();

            String line;

            while ((line=bufferedReader.readLine()) != null)
            {
                stringBuffer.append(line + "\n");
            }

            file_content = stringBuffer.toString();

            //System.out.println(file_content);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }



    public static void print_medicine()
    {


        for (int j=0 ; j< Medicine.medicineList.size() ; j++)
        {
            System.out.println(Medicine.medicineList.get(j).name +"\n"+Medicine.medicineList.get(j).hour+"\n" + Medicine.medicineList.get(j).min+"\n");
        }

    }



}
