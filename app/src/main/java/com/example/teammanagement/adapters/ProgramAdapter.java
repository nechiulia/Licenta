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
import com.example.teammanagement.Utils.Program;

import java.util.List;

public class ProgramAdapter extends ArrayAdapter<Program>  {
    private Context context;
    private int resource;
    private List<Program> program;
    private LayoutInflater inflater;

    private TextView tv_day;
    private TextView tv_hours;

    public ProgramAdapter(@NonNull Context context,
                          int resource,
                          @NonNull List<Program> objects,
                          LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.program= objects;
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
        tv_day = convertView.findViewById(R.id.list_item_program_location_marker_dialog_tv_day_hint);
        tv_hours=convertView.findViewById(R.id.list_item_program_location_marker_dialog_tv_hours);

        Program program = getItem(position);

        tv_day.setText(program.getDay());
        tv_hours.setText(program.getIntervalHours());

        return convertView;
    }

}