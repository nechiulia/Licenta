package com.example.teammanagement.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Activity;
import com.example.teammanagement.Utils.NewLocation;
import com.example.teammanagement.Utils.Orar;
import com.example.teammanagement.Utils.Program;
import com.example.teammanagement.database.JDBCController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpandableListLocationActivitiesAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> listParent;
    private HashMap<String, Activity> infoActivity;
    private List<Integer> listActivitiesID;
    private Map<Integer,List<Program>> mapOrare = new HashMap<>();
    private ProgramAdapter adapter;
    private NewLocation currentLocation;

    private JDBCController jdbcController;
    private Connection c;

    public ExpandableListLocationActivitiesAdapter(Context context, List<String> listParent,
                                                 HashMap<String, Activity> activities, List<Integer> objects,NewLocation currentL) {
        this._context = context;
        this.listParent = listParent;
        this.infoActivity = activities;
        this.listActivitiesID=objects;
        this.currentLocation = currentL;

        jdbcController= JDBCController.getInstance();
        c=jdbcController.openConnection();

        for(int i=0;i<listActivitiesID.size();i++) {
            selectOrar(listActivitiesID.get(i));
        }

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
        return 0;
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
        ListView lv_orar = convertView.findViewById(R.id.list_item_location_admin_lv_orar);

        List<Program> listOrar = mapOrare.get(listActivitiesID.get(groupPosition));

        adapter = new ProgramAdapter(_context,R.layout.list_item_program_location_marker_dialog,listOrar,(LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        lv_orar.setAdapter(adapter);
        tv_activityName.setText(activity.getActivityName());
        tv_sport.setText(activity.getSport());
        tv_trainer.setText(activity.getTrainer());
        String reservation;
        if(activity.getReservation() ==0){
            reservation=_context.getString(R.string.no);
        }
        else{
            reservation=_context.getString(R.string.yes);
        }
        tv_reservation.setText(reservation);
        tv_difficultyLevel.setText(String.valueOf(activity.getDifficultyLevel()));
        tv_price.setText(String.valueOf(activity.getPrice()+" " + _context.getString(R.string.lei)));

        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public List<String> getListParent() {
        return listParent;
    }


    public HashMap<String, Activity> getInfoActivity() {
        return infoActivity;
    }

    public String getDayString(int day){
        if(day == 0)return "L:";
        else if(day==1)return "M:";
        else if(day==2)return "M:";
        else if(day==3)return "J:";
        else if(day==4)return "V:";
        else if(day==5)return "S:";
        else return "D:";
    }


    public void selectOrar(int idActivity){
        List<Program> listOrar = new ArrayList<>();
        try(Statement s = c.createStatement()){
            try (ResultSet r = s.executeQuery("SELECT ZI,INTERVALORAR,IDACTIVITATE FROM ORAR WHERE IDACTIVITATE=" + idActivity)) {
                while(r.next()) {
                    Program orar = new Program();
                    int day = r.getInt(1);
                    String interval = r.getString(2);
                    orar.setDay(getDayString(day));
                    orar.setIntervalHours(interval);
                    listOrar.add(orar);
                }
                mapOrare.put(idActivity,listOrar);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
