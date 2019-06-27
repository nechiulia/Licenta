package com.example.teammanagement.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Feedback;
import com.example.teammanagement.Utils.User;
import com.example.teammanagement.activities.SearchUserActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExploreUsersAdapter extends ArrayAdapter<User> {
    private Context context;
    private int resource;
    private List<User> users;
    private ArrayList<User> users_toDisplay;
    private LayoutInflater inflater;

    public ExploreUsersAdapter(@NonNull Context context,
                           int resource,
                           @NonNull List<User> objects,
                           LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.users =  objects;
        this.inflater = inflater;
        users_toDisplay = new ArrayList<>();
        users_toDisplay.addAll(objects);
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }


    @NonNull
    @Override
    public Filter getFilter() {
        return userNameFilter;
    }

    @SuppressLint({"ResourceType", "SetTextI18n"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView,
                        @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") View row = inflater.inflate(resource, parent, false);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_users_explorer,parent,false);
        }
        TextView tv_userName = row.findViewById(R.id.list_item_usersExplorer_tv_userName);
        ImageView iv_userProfile = row.findViewById(R.id.list_item_usersExplorer_iv_userPicture);


        User user = users.get(position);

        Bitmap bmp = BitmapFactory.decodeByteArray(user.getProfilePicture(),0,user.getProfilePicture().length);

        tv_userName.setText(user.getUserName());
        iv_userProfile.setImageBitmap(Bitmap.createBitmap(bmp));

        return row;
    }

    public Filter userNameFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<User> suggestions = new ArrayList<>();
            if(constraint == null || constraint.length() ==0){
                suggestions.addAll(users_toDisplay);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(User user : users_toDisplay){
                    if(user.getUserName().toLowerCase().contains(filterPattern)){
                        suggestions.add(user);
                    }
                }
            }
            results.values =suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            users.clear();
            users.addAll((List)results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return((User)resultValue).getUserName();
        }
    };

}
