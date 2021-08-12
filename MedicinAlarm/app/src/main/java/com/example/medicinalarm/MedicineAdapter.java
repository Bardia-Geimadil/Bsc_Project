package com.example.medicinalarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MedicineAdapter extends ArrayAdapter {


    List<Medicine> items;
    Context context;



    public MedicineAdapter(@NonNull Context context, List<Medicine> items) {
        super(context, R.layout.medicine_eachitem, items);

        this.items = items;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.medicine_eachitem, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }



        holder.fill(position);

        return convertView;

    }



    public class ViewHolder{

        TextView name , time;

        public ViewHolder(View v)
        {
            name = v.findViewById(R.id.eachName);
            time = v.findViewById(R.id.eachTime);

        }

        public  void  fill(final int position){

            name.setText(items.get(position).name);
            time.setText(items.get(position).hour+":"+items.get(position).min);

        }


    }
}
