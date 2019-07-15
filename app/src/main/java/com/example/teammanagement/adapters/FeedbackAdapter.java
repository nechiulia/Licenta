package com.example.teammanagement.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.teammanagement.Utils.Feedback;
import com.example.teammanagement.R;
import com.example.teammanagement.database.JDBCController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class FeedbackAdapter extends ArrayAdapter<Feedback> {
    private Context context;
    private int resource;
    private List<Feedback> feedbacks;
    private List<String> senderNames;
    private LayoutInflater inflater;

    private JDBCController jdbcController;
    private Connection c;

    public FeedbackAdapter(@NonNull Context context,
                           int resource,
                           @NonNull List<Feedback> objects,
                           List<String> senderNames,
                           LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.feedbacks = objects;
        this.senderNames=senderNames;
        this.inflater = inflater;

        jdbcController = JDBCController.getInstance();
        c=jdbcController.openConnection();
    }


    @SuppressLint({"ResourceType", "SetTextI18n"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView,
                        @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") View row = inflater.inflate(resource, parent, false);

        TextView tvUsername = row.findViewById(R.id.lv_feedback_tv_userName);
        RatingBar ratingBar = row.findViewById(R.id.lv_feedback_ratingbar);
        TextView tvComment= row.findViewById(R.id.lv_feedback_tv_comment);
        ImageButton ibtn_more=row.findViewById(R.id.lv_review_ibtn_more);
        TextView tv_date =row.findViewById(R.id.lv_feedback_tv_date);

        Feedback feedback = feedbacks.get(position);


        tvUsername.setText(senderNames.get(position));
        ratingBar.setRating(feedback.getRating());
        tvComment.setText(feedback.getComment());
        tv_date.setText(feedback.getDate());
        ibtn_more.setBackgroundResource(R.drawable.ic_more);

        return row;
    }


}
