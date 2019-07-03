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
import com.example.teammanagement.Utils.SportUser;
import com.example.teammanagement.Utils.Team;

import java.util.List;

public class TeamsAdapter  extends ArrayAdapter<Team> {
    private Context context;
    private int resource;
    private List<Team> teams;
    private LayoutInflater inflater;

    public TeamsAdapter(@NonNull Context context,
                                  int resource,
                                  @NonNull List<Team> objects,
                                  LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.teams = objects;
        this.inflater = inflater;
    }


    @SuppressLint({"ResourceType", "SetTextI18n"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView,
                        @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") View row = inflater.inflate(resource, parent, false);

        TextView tv_teamName = row.findViewById(R.id.list_item_team_tv_teamName);
        TextView tv_sport = row.findViewById(R.id.list_item_team_tv_sport);

        Team team = teams.get(position);

        tv_teamName.setText(team.getTeamName());
        tv_sport.setText(team.getSport());
        return row;
    }
}