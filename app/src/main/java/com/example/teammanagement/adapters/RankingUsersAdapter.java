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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.User;
import com.example.teammanagement.Utils.UserRanking;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RankingUsersAdapter extends ArrayAdapter<UserRanking> {
    private Context context;
    private int resource;
    private List<UserRanking> users;
    private LayoutInflater inflater;

    public RankingUsersAdapter(@NonNull Context context,
                           int resource,
                           @NonNull List<UserRanking> objects,
                           LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.users = new ArrayList<>(objects);
        this.inflater = inflater;
    }

    public List<UserRanking> getUsers() {
        return users;
    }

    public void setUsers(List<UserRanking> users) {
        this.users = users;
    }



    @SuppressLint({"ResourceType", "SetTextI18n"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView,
                        @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") View row = inflater.inflate(resource, parent, false);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_fragment_ranking_users, parent, false);
        }

        TextView tv_nrcrt = row.findViewById(R.id.list_item_fragment_ranking_users_tv_nrcrt);
        TextView tv_userName = row.findViewById(R.id.list_item_fragment_ranking_user_tv_userName);
        ImageView iv_userProfile = row.findViewById(R.id.list_item_fragment_ranking_users_iv_userPicture);
        TextView tv_score = row.findViewById(R.id.list_item_fragment_ranking_user_tv_score);

        UserRanking user = users.get(position);

        if(user.getProfilePicture() != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(user.getProfilePicture(), 0, user.getProfilePicture().length);
            iv_userProfile.setImageBitmap(Bitmap.createBitmap(bmp));
        }

        float x = user.getScore();
        BigDecimal result;
        result=round(x,2);
        tv_userName.setText(user.getUserName());
        tv_score.setText(result.toString());
        tv_nrcrt.setText(String.valueOf(user.getNrCrt()));

        return row;
    }

    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }


}