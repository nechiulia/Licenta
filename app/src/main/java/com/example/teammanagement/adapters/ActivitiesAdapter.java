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
import com.example.teammanagement.Utils.Activity;
import com.example.teammanagement.Utils.Teammate;

import java.util.ArrayList;
import java.util.List;

public class ActivitiesAdapter extends ArrayAdapter<Activity> {
    private Context context;
    private int resource;
    private List<Activity> activities;
    private LayoutInflater inflater;
    private List<Activity> checkedActivities= new ArrayList<>();

    public ActivitiesAdapter(@NonNull Context context,
                        int resource,
                        @NonNull List<Activity> objects,
                        LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.activities = objects;
        this.inflater = inflater;
    }


    @SuppressLint({"ResourceType", "SetTextI18n"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView,
                        @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") View row = inflater.inflate(resource, parent, false);

        TextView tv_activityName = row.findViewById(R.id.list_item_edit_location_activities_tv_activityName);
        TextView tv_activityDifficulty = row.findViewById(R.id.list_item_edit_location_activities_tv_activityDifficulty);
        CheckBox ck_check = row.findViewById(R.id.list_item_edit_location_activities_ck);

        Activity activity = activities.get(position);

        ck_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CheckBox cb = (CheckBox)v;
                if(cb.isChecked()){
                    checkedActivities.add(activities.get(position));
                }
                else{
                    checkedActivities.remove(activities.get(position));
                }
            }
        });

        tv_activityName.setText(activity.getActivityName());
        tv_activityDifficulty.setText(activity.getDifficultyLevel());

        return row;
    }

    public List<Activity> getCheckedActivities() {
        return checkedActivities;
    }
}
