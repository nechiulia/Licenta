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
import com.example.teammanagement.Utils.NewLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListNewLocationAdminAdapter  extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> listParent;
    private HashMap<String, NewLocation> infoLocation;
    private List<Long> mCheckedItems = new ArrayList<>();

    public ExpandableListNewLocationAdminAdapter(Context context, List<String> listParent,
                                            HashMap<String, NewLocation> report) {
        this._context = context;
        this.listParent = listParent;
        this.infoLocation = report;
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
        NewLocation location = (NewLocation) getChild(groupPosition,childPosition);

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item_location_admin,null);
        }

        TextView tv_userName = convertView.findViewById(R.id.list_item_location_userName);
        TextView tv_email = convertView.findViewById(R.id.list_item_location_email);
        TextView tv_postalCode = convertView.findViewById(R.id.list_item_location_postalCode);
        TextView tv_address = convertView.findViewById(R.id.list_item_location_locationAddress);

        tv_userName.setText(location.getUserName());
        tv_email.setText(location.getEmail());
        tv_postalCode.setText(location.getPostalCode());
        tv_address.setText(location.getAddress());

        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;//true
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

}
