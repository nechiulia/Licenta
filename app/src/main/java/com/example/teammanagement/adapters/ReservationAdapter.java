package com.example.teammanagement.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Activity;
import com.example.teammanagement.Utils.Orar;

import java.util.ArrayList;
import java.util.List;

public class ReservationAdapter extends ArrayAdapter<Orar> {
    private Context context;
    private int resource;
    private List<Orar> orareZilnice;
    private LayoutInflater inflater;

    public ReservationAdapter(@NonNull Context context,
                             int resource,
                             @NonNull List<Orar> objects,
                             LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.orareZilnice = objects;
        this.inflater = inflater;
    }


    @SuppressLint({"ResourceType", "SetTextI18n"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView,
                        @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") View row = inflater.inflate(resource, parent, false);

        TextView tv_startHour = row.findViewById(R.id.list_item_location_reservation_dialog_tv_startHour);
        TextView tv_finishHour = row.findViewById(R.id.list_item_location_reservation_dialog_tv_finishHour);
        TextView tv_userName = row.findViewById(R.id.list_item_location_reservation_dialog_tv_userName);
        TextView tv_separator=row.findViewById(R.id.list_item_location_reservation_dialog_tv_separator);

        Orar orar = orareZilnice.get(position);

        tv_startHour.setText(orar.getStartHour());
        tv_finishHour.setText(orar.getFinishHour());
        tv_userName.setText(orar.getName());

        if(tv_userName.getText().length()!= 0){
            tv_startHour.setBackgroundColor(Color.LTGRAY);
            tv_finishHour.setBackgroundColor(Color.LTGRAY);
            tv_userName.setBackgroundColor(Color.LTGRAY);
            tv_separator.setBackgroundColor(Color.LTGRAY);
            tv_userName.setClickable(false);
            tv_finishHour.setClickable(false);
            tv_startHour.setClickable(false);
            tv_finishHour.setFocusable(false);
            tv_startHour.setFocusable(false);
            tv_separator.setFocusable(false);
            tv_separator.setClickable(false);
            row.setClickable(false);
            row.setFocusable(false);
        }

        return row;
    }
}