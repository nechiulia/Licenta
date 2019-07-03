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
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.User;

import java.util.ArrayList;
import java.util.List;

public class AddUsersAdapter extends ArrayAdapter<User> {
    private Context context;
    private int resource;
    private List<User> users;
    private LayoutInflater inflater;
    private List<Integer> mCheckedItems = new ArrayList<>();
    private List<User> checkedUsers = new ArrayList<>();

    public AddUsersAdapter(@NonNull Context context,
                           int resource,
                           @NonNull List<User> objects,
                           LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.users = new ArrayList<>(objects);
        this.inflater = inflater;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }



    @SuppressLint({"ResourceType", "SetTextI18n"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView,
                        @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") View row = inflater.inflate(resource, parent, false);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_add_user_to_team, parent, false);
        }
        TextView tv_userName = row.findViewById(R.id.list_item_add_user_to_team_tv_userName);
        ImageView iv_userProfile = row.findViewById(R.id.list_item_add_user_to_team_iv_userPicture);
        CheckBox ck = row.findViewById(R.id.list_item_add_user_to_team_ck);

        User user = users.get(position);

        if(user.getProfilePicture() != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(user.getProfilePicture(), 0, user.getProfilePicture().length);
            iv_userProfile.setImageBitmap(Bitmap.createBitmap(bmp));
        }
        ck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CheckBox cb = (CheckBox)v;
                if(cb.isChecked()){
                    mCheckedItems.add(position);
                    checkedUsers.add(users.get(position));
                }
                else{
                    mCheckedItems.remove(position);
                    checkedUsers.remove(users.get(position));
                }
            }
        });

        tv_userName.setText(user.getUserName());

        return row;
    }

    public List<Integer> getmCheckedItems() {
        return mCheckedItems;
    }

    public List<User> getCheckedUsers() {
        return checkedUsers;
    }

}