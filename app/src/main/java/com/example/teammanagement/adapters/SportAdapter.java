package com.example.teammanagement.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Sport;

import java.util.List;

public class SportAdapter extends ArrayAdapter<Sport> {
    private Context context;
    private int resource;
    private List<Sport> sports;
    private LayoutInflater inflater;

    public SportAdapter(@NonNull Context context,
                        int resource,
                        @NonNull List<Sport> objects,
                        LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.sports = objects;
        this.inflater = inflater;
    }


    @SuppressLint({"ResourceType", "SetTextI18n"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView,
                        @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") View row = inflater.inflate(resource, parent, false);

        TextView tv_Sport = row.findViewById(R.id.list_item_sports_tv_sport);
        TextView tv_Level = row.findViewById(R.id.list_item_sports_tv_level);
        CheckBox ck_check = row.findViewById(R.id.list_item_sports_ck);

        Sport sport=sports.get(position);

        tv_Sport.setText(sport.getSportName());
        tv_Level.setText(sport.getSportLevel());
        return row;
    }




}
