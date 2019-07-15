package com.example.teammanagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Activity;
import com.example.teammanagement.Utils.NewLocation;
import com.example.teammanagement.Utils.ReservationExpList;
import com.example.teammanagement.Utils.ReservationItem;
import com.example.teammanagement.database.JDBCController;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamsReservationsAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<ReservationExpList> listParent;
    private Map<ReservationExpList, ReservationItem> maplist;

    private JDBCController jdbcController;
    private Connection c;


    public TeamsReservationsAdapter(Context context, List<ReservationExpList> listParent,
                                                   HashMap<ReservationExpList, ReservationItem> activities) {
        this._context = context;
        this.listParent = listParent;
        this.maplist= activities;

        jdbcController= JDBCController.getInstance();
        c=jdbcController.openConnection();

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
        return this.maplist.get(this.listParent.get(groupPosition));
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ReservationExpList reservationExpList = (ReservationExpList) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group_activity_team_reservations, null);
        }

        TextView tv_date=convertView.findViewById(R.id.list_group_activity_team_reservations_date);
        TextView tv_hours=convertView.findViewById(R.id.list_group_activity_team_reservations_tv_hours);

        tv_date.setText(reservationExpList.getReservationDate());
        tv_hours.setText(reservationExpList.getIntervalOrar());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

       ReservationItem reservationItem = (ReservationItem) getChild(groupPosition,childPosition);

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item_activity_team_reservations,null);
        }

        TextView tv_locationName=convertView.findViewById(R.id.list_item_activity_team_reservations_tv_locationName);
        TextView tv_activityName=convertView.findViewById(R.id.list_item_activity_team_reservations_tv_activityName);
        TextView tv_bookedDate=convertView.findViewById(R.id.list_item_activity_team_reservations_tv_bookedDate);

        tv_locationName.setText(reservationItem .getLocationName());
        tv_activityName.setText(reservationItem .getActivityName());
        tv_bookedDate.setText(reservationItem .getBookedDate());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
