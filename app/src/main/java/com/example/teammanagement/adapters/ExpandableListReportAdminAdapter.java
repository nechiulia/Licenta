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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListReportAdminAdapter extends BaseExpandableListAdapter{
    private Context _context;
    private List<String> listParent;
    private HashMap<String, List<String>> report;
    private List<Long> mCheckedItems = new ArrayList<>();

    public ExpandableListReportAdminAdapter(Context context, List<String> listParent,
                                            HashMap<String, List<String>> report) {
        this._context = context;
        this.listParent = listParent;
        this.report = report;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.report.get(this.listParent.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.report.get(this.listParent.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listParent.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listParent.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String answer = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_report_admin, null);
        }

        TextView tvAnswer = convertView
                .findViewById(R.id.list_item_report_tv);

        tvAnswer.setText(answer);
        return convertView;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String title = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group_report_admin, null);
        }

        TextView tvTitle = convertView
                .findViewById(R.id.list_group_report_tv);
        tvTitle.setTypeface(null, Typeface.BOLD);
        tvTitle.setText(title);
        CheckBox checkBox = convertView.findViewById(R.id.list_group_report_cb);
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

    public List<String> getListParent() {
        return listParent;
    }

    public void setListParent(List<String> listParent) {
        this.listParent = listParent;
    }

    public HashMap<String, List<String>> getReport() {
        return report;
    }

    public void setReport(HashMap<String, List<String>> report) {
        this.report = report;
    }

    public List<Long> getmCheckedItems() { return mCheckedItems; }

    public void setmCheckedItems(List<Long> mCheckedItems) {
        this.mCheckedItems = mCheckedItems;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
