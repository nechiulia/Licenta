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
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Teammate;
import com.example.teammanagement.Utils.User;

import java.util.ArrayList;
import java.util.List;

public class TeammatesAdapter extends ArrayAdapter<Teammate> {
    private Context context;
    private int resource;
    private List<Teammate> users;
    private ArrayList<Teammate> users_toDisplay;
    private LayoutInflater inflater;

    public TeammatesAdapter(@NonNull Context context,
                               int resource,
                               @NonNull List<Teammate> objects,
                               LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.users =  objects;
        this.inflater = inflater;
        users_toDisplay = new ArrayList<>();
        users_toDisplay.addAll(objects);
    }

    public List<Teammate> getUsers() {
        return users;
    }

    public void setUsers(List<Teammate> users) {
        this.users = users;
    }


    @SuppressLint({"ResourceType", "SetTextI18n"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView,
                        @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") View row = inflater.inflate(resource, parent, false);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_teammates,parent,false);
        }
        TextView tv_userName = row.findViewById(R.id.list_item_teammates_tv_userName);
        TextView tv_role = row.findViewById(R.id.list_item_teammates_tv_role);
        ImageView iv_userProfile = row.findViewById(R.id.list_item_teammates_iv_userPicture);


        Teammate teammate = users.get(position);

        if(teammate.getUserProfilePicture()!=null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(teammate.getUserProfilePicture(), 0, teammate.getUserProfilePicture().length);
            iv_userProfile.setImageBitmap(Bitmap.createBitmap(bmp));
        }
        tv_userName.setText(teammate.getUserName());
        tv_role.setText(teammate.getTeamRole());

        return row;
    }

}