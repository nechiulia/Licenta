package com.example.teammanagement.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.teammanagement.R;

import java.util.List;

public class SpinnerWhiteAdapter extends ArrayAdapter<String> implements android.widget.SpinnerAdapter {
    private Context context;
    private int resource;
    private List<String> spn_list;
    private LayoutInflater inflater;
    TextView tv_item;

    public SpinnerWhiteAdapter(@NonNull Context context,
                          @NonNull List<String> objects) {
        super(context, 0,objects);
        this.context = context;
        this.spn_list= objects;
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item_sports_white,parent, false);
        }
        tv_item = convertView.findViewById(R.id.spn_white_item_tv_text);
        String item = getItem(position);
        if(item != null) {
            tv_item.setText(item);
        }
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
