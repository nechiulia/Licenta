package com.example.teammanagement.adapters;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.NewLocation;
import com.example.teammanagement.Utils.SharedViewModel;
import com.example.teammanagement.activities.HomeAdminActivity;
import com.example.teammanagement.fragments.NewLocationsFragment;
import com.example.teammanagement.fragments.SearchLocationFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListNewLocationAdminAdapter  extends BaseExpandableListAdapter  {
    private Context _context;
    private List<String> listParent;
    private HashMap<String, NewLocation> infoLocation;
    private List<Long> mCheckedItems = new ArrayList<>();
    private NewLocation selectedLocation = new NewLocation();

    private SharedViewModel model;


    public ExpandableListNewLocationAdminAdapter(Context context, List<String> listParent,
                                            HashMap<String, NewLocation> locations) {
        this._context = context;
        this.listParent = listParent;
        this.infoLocation = locations;
        model = ViewModelProviders.of((FragmentActivity) context).get(SharedViewModel.class);
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
        return this.infoLocation.get(this.listParent.get(groupPosition));
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
            convertView = inflater.inflate(R.layout.list_group_location_admin, null);
        }

        TextView tvTitle = convertView
                .findViewById(R.id.list_group_locationName_tv);
        tvTitle.setTypeface(null, Typeface.BOLD);
        tvTitle.setText(locationName);
        CheckBox checkBox = convertView.findViewById(R.id.list_group_location_cb);
        final Long tag = getGroupId(groupPosition);
        checkBox.setTag(tag);
        checkBox.setChecked(mCheckedItems.contains(tag));
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CheckBox cb = (CheckBox)v;
                final Long tag = (Long)v.getTag();
                if(cb.isChecked()){
                    mCheckedItems.add(tag);
                }
                else{
                    mCheckedItems.remove(tag);
                }
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final NewLocation location = (NewLocation) getChild(groupPosition,childPosition);

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item_location_admin,null);
        }


        TextView tv_userName = convertView.findViewById(R.id.list_item_location_userName);
        TextView tv_email = convertView.findViewById(R.id.list_item_location_email);
        TextView tv_postalCode = convertView.findViewById(R.id.list_item_location_postalCode);
        TextView tv_address = convertView.findViewById(R.id.list_item_location_locationAddress);
        TextView tv_reservation=convertView.findViewById(R.id.list_item_location_reservation);
        TextView tv_latitude=convertView.findViewById(R.id.list_item_location_latitude);
        TextView tv_longitude=convertView.findViewById(R.id.list_item_location_longitude);
        Button btn_verify =convertView.findViewById(R.id.list_item_location_admin_btn_verifyInfo);

        tv_userName.setText(location.getUserName());
        tv_email.setText(location.getEmail());
        tv_postalCode.setText(location.getPostalCode());
        tv_address.setText(location.getAddress());
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ceva","altceva");
                model.setNewLocation(location);
                ((FragmentActivity)_context).getSupportFragmentManager().beginTransaction().replace(R.id.home_admin_fragment_containerAdmin, new SearchLocationFragment()).
                        addToBackStack(null).commit();

            }
        });
        if(location.getLatitude() != 0.0) {
            tv_latitude.setText(String.valueOf(location.getLatitude()));
        }
        if(location.getLongitude() != 0.0) {
            tv_longitude.setText(String.valueOf(location.getLongitude()));
        }
        if(location.getResevation() == 0){
            tv_reservation.setText(_context.getString(R.string.reservation_no));
        }
        else{
            tv_reservation.setText(_context.getString(R.string.reservations_yes));
        }

        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public List<Long> getmCheckedItems() {
        return mCheckedItems;
    }

    public List<String> getListParent() {
        return listParent;
    }

    public HashMap<String, NewLocation> getInfoLocation() {
        return infoLocation;
    }

    public NewLocation getSelectedLocation() {
        return selectedLocation;
    }

    public void setSelectedLocation(NewLocation selectedLocation) {
        this.selectedLocation = selectedLocation;
    }
}
