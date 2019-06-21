package com.example.teammanagement.adapters;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.teammanagement.R;


import java.util.HashMap;
import java.util.List;

public class ExpandableListFaqAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> questionsList;
    private HashMap<String, List<String>> answersList;

    public ExpandableListFaqAdapter(Context context, List<String> questionsList,
                                    HashMap<String, List<String>> answersList) {
        this._context = context;
        this.questionsList = questionsList;
        this.answersList = answersList;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.answersList.get(this.questionsList.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.answersList.get(this.questionsList.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.questionsList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.questionsList.size();
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
            convertView = inflater.inflate(R.layout.list_item_faq, null);
        }

        TextView tvAnswer = convertView
                .findViewById(R.id.list_item_faq_tv);

        tvAnswer.setText(answer);
        return convertView;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String title = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group_faq, null);
        }

        TextView tvTitle = convertView
                .findViewById(R.id.list_group_faq_tv);
        tvTitle.setTypeface(null, Typeface.BOLD);
        tvTitle.setText(title);

        return convertView;
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
