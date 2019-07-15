package com.example.teammanagement.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.SportRanking;
import com.example.teammanagement.Utils.User;
import com.example.teammanagement.Utils.UserRanking;
import com.example.teammanagement.adapters.RankingUsersAdapter;
import com.example.teammanagement.database.JDBCController;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankingUsersFragment extends Fragment {

    private ListView lv_user;
    private JDBCController jdbcController;
    private Connection c;

    private RankingUsersAdapter adapter;

    private List<UserRanking> list_user = new ArrayList<>();
    private Map<Integer,byte[]> listUserPhotos = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking_users,null);

        jdbcController = JDBCController.getInstance();
        c=jdbcController.openConnection();

        lv_user=view.findViewById(R.id.fragment_ranking_users_lv_users);

        selectTop();
        Thread t1 = new Thread(){
            @Override
            public void run() {
                getUserProfilePicture();
            }
        };
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        adapter= new RankingUsersAdapter(getContext(),R.layout.list_item_fragment_ranking_users,list_user,getLayoutInflater());
        lv_user.setAdapter(adapter);

        return view;
    }

    public void getUserProfilePicture(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        for(UserRanking user: list_user) {
            DatabaseReference myRef = database.getReference(String.valueOf(user.getIdUser()));
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String valoare = dataSnapshot.getValue(String.class);
                    String key=dataSnapshot.getKey();
                    if( valoare != null) {
                        byte[] decodedString = Base64.decode(valoare, Base64.DEFAULT);
                        listUserPhotos.put(Integer.parseInt(key),decodedString);
                        for(UserRanking u:list_user ) {
                            if(Integer.parseInt(key) == u.getIdUser()) {
                                u.setProfilePicture(decodedString);
                                break;
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
        }
    }

    public void selectTop(){
        int i=1;
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("select top 5 f.IDReceptor, u.NumeUtilizator, (CAST(ROUND(AVG(f.NrStele),2) AS decimal(10,2))) NotaMedie, u.stare" +
                    "  from Feedbacks f JOIN Utilizatori u ON (f.IDReceptor = u.ID) " +
                    "  GROUP BY f.IDReceptor, u.NumeUtilizator, u.stare " +
                    "  ORDER BY NotaMedie DESC")){
                while(r.next()){
                    if(r.getInt(4) == 0) {
                        UserRanking userRanking = new UserRanking();
                        userRanking.setNrCrt(i);
                        userRanking.setIdUser(r.getInt(1));
                        userRanking.setUserName(r.getString(2));
                        userRanking.setScore(r.getFloat(3));
                        list_user.add(userRanking);
                        i++;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
