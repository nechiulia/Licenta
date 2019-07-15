package com.example.teammanagement.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.SportRanking;
import com.example.teammanagement.Utils.UserRanking;
import com.example.teammanagement.adapters.RankingUsersAdapter;
import com.example.teammanagement.adapters.SportRankingAdapter;
import com.example.teammanagement.database.JDBCController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SportsRankingFragment extends Fragment {

    private ListView lv_sports;
    private JDBCController jdbcController;
    private Connection c;

    private SportRankingAdapter adapter;

    private List<SportRanking> list_sports = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking_sports,null);

        jdbcController = JDBCController.getInstance();
        c=jdbcController.openConnection();

        lv_sports=view.findViewById(R.id.fragment_ranking_sports_lv_sports);

        selectTop();

        adapter= new SportRankingAdapter(this.getContext(),R.layout.list_item_fragment_ranking_sports,list_sports,getLayoutInflater());
        lv_sports.setAdapter(adapter);

        return view;
    }

    public void selectTop(){
        int i=1;
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("select TOP 5 count(IDUTILIZATOR) NRUTILIZATORI,SP.IDSport, S.DENUMIRE\n" +
                    "\t FROM SportUtilizator SP, Sporturi S WHERE SP.IDSport=S.ID\n" +
                    "\t GROUP BY IDSport,Denumire\n" +
                    "\t  ORDER BY NRUTILIZATORI DESC")){
                while(r.next()){
                        SportRanking sportRanking = new SportRanking();
                        sportRanking.setNrCrt(i);
                        sportRanking.setNrUsers(r.getInt(1));
                        sportRanking.setSportID(r.getInt(2));
                        sportRanking.setSportName(r.getString(3));
                        list_sports.add(sportRanking);
                        i++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
