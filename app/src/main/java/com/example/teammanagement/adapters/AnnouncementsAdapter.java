package com.example.teammanagement.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Announcement;
import java.util.List;

public class AnnouncementsAdapter extends ArrayAdapter<Announcement> {
    private Context context;
    private int resource;
    private List<Announcement> announcements;
    private LayoutInflater inflater;

    public AnnouncementsAdapter(@NonNull Context context,
                              int resource,
                              @NonNull List<Announcement> objects,
                              LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.announcements =  objects;
        this.inflater = inflater;
    }

    public List<Announcement> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(List<Announcement> announcements) {
        this.announcements = announcements;
    }


    @SuppressLint({"ResourceType", "SetTextI18n"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView,
                        @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") View row = inflater.inflate(resource, parent, false);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_annoucements,parent,false);
        }
        TextView tv_date = row.findViewById(R.id.list_item_announcements_tv_date);
        TextView tv_message = row.findViewById(R.id.list_item_announcements_tv_message);


        Announcement announcement = announcements.get(position);

        tv_date.setText(announcement.getDate());
        tv_message.setText(announcement.getMessage());

        return row;
    }

}
