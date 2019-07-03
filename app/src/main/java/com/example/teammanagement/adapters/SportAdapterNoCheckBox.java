package com.example.teammanagement.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.SportUser;

import java.util.List;

public class SportAdapterNoCheckBox extends ArrayAdapter<SportUser> {
    private Context context;
    private int resource;
    private List<SportUser> sports;
    private LayoutInflater inflater;

    public SportAdapterNoCheckBox(@NonNull Context context,
                         int resource,
                         @NonNull List<SportUser> objects,
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

        TextView tv_Sport = row.findViewById(R.id.list_item_sports_nocheckbox_tv_sport);
        TextView tv_Level = row.findViewById(R.id.list_item_sports_nocheckbox_tv_level);

        SportUser sport=sports.get(position);

        tv_Sport.setText(sport.getSportName());
        tv_Level.setText(sport.getLevel());
        return row;
    }




}