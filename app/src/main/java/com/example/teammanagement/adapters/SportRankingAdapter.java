package com.example.teammanagement.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.SportRanking;

import java.util.ArrayList;
import java.util.List;

public class SportRankingAdapter  extends ArrayAdapter<SportRanking> {
    private Context context;
    private int resource;
    private List<SportRanking> sports;
    private LayoutInflater inflater;

    public SportRankingAdapter(@NonNull Context context,
                               int resource,
                               @NonNull List<SportRanking> objects,
                               LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.sports = new ArrayList<>(objects);
        this.inflater = inflater;
    }

    public List<SportRanking> getSports() {
        return sports;
    }

    public void setSports(List<SportRanking> sports) {
        this.sports = sports;
    }



    @SuppressLint({"ResourceType", "SetTextI18n"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView,
                        @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") View row = inflater.inflate(resource, parent, false);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_fragment_ranking_sports, parent, false);
        }

        TextView tv_sportName = row.findViewById(R.id.list_item_fragment_ranking_sports_tv_sportName);
        TextView tv_nrUsers = row.findViewById(R.id.list_item_fragment_ranking_sports_tv_nrUsers);
        TextView tv_nrcrt = row.findViewById(R.id.list_item_fragment_ranking_sports_tv_nrcrt_hint);


        SportRanking sportRanking = sports.get(position);


        tv_sportName.setText(sportRanking.getSportName());
        tv_nrUsers.setText(String.valueOf(sportRanking.getNrUsers()));
        tv_nrcrt.setText(String.valueOf(sportRanking.getNrCrt()));

        return row;
    }
}
