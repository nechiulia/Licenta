package com.example.teammanagement.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Activity;
import com.example.teammanagement.Utils.NewLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListLocationActivitiesAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> listParent;
    private HashMap<String, Activity> infoActivity;

    public ExpandableListLocationActivitiesAdapter(Context context, List<String> listParent,
                                                 HashMap<String, Activity> activities) {
        this._context = context;
        this.listParent = listParent;
        this.infoActivity = activities;
    }

    @Override
    public int getGroupCount() {
        return this.listParent.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listParent.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.infoActivity.get(this.listParent.get(groupPosition));
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String locationName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group_location_admin_activities, null);
        }

        TextView tvTitle = convertView
                .findViewById(R.id.list_group_location_admin_activities_tv);
        tvTitle.setTypeface(null, Typeface.BOLD);
        tvTitle.setText(locationName);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Activity activity = (Activity) getChild(groupPosition,childPosition);

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item_location_admin_activities,null);
        }

        TextView tv_activityName = convertView.findViewById(R.id.list_item_location_admin_activities_activityName);
        TextView tv_sport = convertView.findViewById(R.id.list_item_location_admin_activities_sport);
        TextView tv_trainer = convertView.findViewById(R.id.list_item_location_admin_activities_trainer);
        TextView tv_reservation = convertView.findViewById(R.id.list_item_location_admin_activities_reservation);
        TextView tv_difficultyLevel = convertView.findViewById(R.id.list_item_location_admin_activities_difficultyLevel);
        TextView tv_price = convertView.findViewById(R.id.list_item_location_admin_activities_price);


        tv_activityName.setText(activity.getActivityName());
        tv_sport.setText(activity.getSport());
        tv_trainer.setText(activity.getTrainer());
        tv_reservation.setText(String.valueOf(activity.getReservation()));
        tv_difficultyLevel.setText(String.valueOf(activity.getDifficultyLevel()));
        tv_price.setText(String.valueOf(activity.getPrice()));

        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;//true
    }

    public List<String> getListParent() {
        return listParent;
    }


    public HashMap<String, Activity> getInfoActivity() {
        return infoActivity;
    }

}
