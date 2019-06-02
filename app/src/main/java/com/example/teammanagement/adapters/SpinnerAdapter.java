package com.example.teammanagement.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.teammanagement.R;
import com.example.teammanagement.dialogs.AddSportDialog;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<String> implements android.widget.SpinnerAdapter {
    private Context context;
    private int resource;
    private List<String> spn_list;
    private LayoutInflater inflater;
    TextView tv_item;

    public SpinnerAdapter(@NonNull Context context,
                         int resource,
                         @NonNull List<String> objects,
                         LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.spn_list= objects;
        this.inflater = inflater;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(resource,parent, false);
        }
        tv_item = convertView.findViewById(R.id.spn_item_tv_text);
        String item = getItem(position);
        tv_item.setText(item);

        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        if(position == 0){
            return false;
        }
        else{
            return true;
        }
    }

}
