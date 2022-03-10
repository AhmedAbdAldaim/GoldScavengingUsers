package com.example.goldscavengingusers.Ui.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;


import com.example.goldscavengingusers.Model.navigationModel;
import com.example.goldscavengingusers.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class navig_adapter extends ArrayAdapter<navigationModel> {
    private Context context;
    private int res;
    public navig_adapter(@NonNull Context context, int resource, @NonNull ArrayList<navigationModel> objects) {
        super(context, resource, objects);
        this.context=context;
        this.res= resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.activity_navig,null);
        TextView name = view.findViewById(R.id.name);
        TextView email = view.findViewById(R.id.email);
        name.setText(getItem(position).getName());
        email.setText(getItem(position).getEmail());

        return view;
    }
}
