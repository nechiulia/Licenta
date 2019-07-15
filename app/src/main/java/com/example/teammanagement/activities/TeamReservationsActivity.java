package com.example.teammanagement.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.ReservationExpList;
import com.example.teammanagement.Utils.ReservationItem;
import com.example.teammanagement.adapters.ReservationAdapter;
import com.example.teammanagement.adapters.TeamsReservationsAdapter;
import com.example.teammanagement.database.JDBCController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamReservationsActivity extends AppCompatActivity {

    private ExpandableListView elv_reservations;

    private Intent intent;

    private TeamsReservationsAdapter adapter;

    private JDBCController jdbcController;
    private Connection c;

    private int currentTeamID;
    private int reservationID;
    private HashMap<ReservationExpList, ReservationItem> mapReservation = new HashMap<>();
    private List<ReservationExpList> listParent = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_reservations);

        initComponents();
    }

    public void initComponents() {
        jdbcController = JDBCController.getInstance();
        c = jdbcController.openConnection();

        intent = getIntent();

        elv_reservations = findViewById(R.id.activity_team_reservations_elv_reservations);

        if (intent.hasExtra(Constants.CURRENT_TEAM_ID)) {
            currentTeamID = (Integer) intent.getSerializableExtra(Constants.CURRENT_TEAM_ID);
        }

        selectReservations();

        adapter = new TeamsReservationsAdapter(getApplicationContext(), listParent, mapReservation);
        elv_reservations.setAdapter(adapter);

        if (mapReservation.size() != 0) {

            int count = adapter.getGroupCount();
            for (int position = 1; position <= count; position++) {
                elv_reservations.expandGroup(position - 1);
            }
            elv_reservations.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TeamReservationsActivity.this);
                    builder.setTitle("Anulează rezervarea")
                            .setMessage("Sunteți sigur că doriți să ștergeți rezervarea?")
                            .setCancelable(false)
                            .setNegativeButton("nu", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int count = adapter.getGroupCount();
                                    for (int position = 1; position <= count; position++) {
                                        elv_reservations.expandGroup(position - 1);
                                    }
                                }
                            })
                            .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int isOk=isOkToDelete(listParent.get(position).getReservationID());
                                    if (isOk == 0) {
                                        mapReservation.remove(listParent.get(position));
                                        deleteReservation(listParent.get(position).getReservationID());
                                        listParent.remove(listParent.get(position));
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(TeamReservationsActivity.this);
                                        builder.setTitle("Anulează rezervarea")
                                                .setMessage("Rezervarea nu poate fi anulată deoarece sunt mai puțin de 24 de ore până la aceasta.")
                                                .setCancelable(false)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {


                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                });
                                    }
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                    return false;
                }
            });
        } else {
            elv_reservations.setVisibility(View.GONE);
            AlertDialog.Builder builder = new AlertDialog.Builder(TeamReservationsActivity.this);
            builder.setTitle("Gestiune rezervări")
                    .setMessage("Nu există rezervări pentru perioada următoare!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getApplicationContext(),TeamProfileActivity.class);
                            intent.putExtra(Constants.CURRENT_TEAM_ID,currentTeamID);
                            startActivity(intent);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }


    public void selectReservations(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT R.ID,R.DATA,R.DATAREALIZARE,R.ORAINCEPERE,R.ORAINCHEIERE,A.DENUMIRE,L.DENUMIRE FROM REZERVARI R, ACTIVITATI A, LOCATII L" +
                    " WHERE R.IDACTIVITATE=A.ID AND A.IDLOCATIE=L.ID AND IDECHIPA="+currentTeamID+" AND CONVERT(DATETIME,DATA,103) >GETDATE();")){
                while(r.next()){
                    ReservationExpList reservationExpList = new ReservationExpList();
                    reservationExpList.setReservationID(r.getInt(1));
                    reservationExpList.setReservationDate(r.getString(2));
                    String interval = r.getString(4)+"-"+r.getString(5);
                    reservationExpList.setIntervalOrar(interval);
                    listParent.add(reservationExpList);
                    ReservationItem reservationItem = new ReservationItem();
                    reservationItem.setBookedDate(r.getString(3));
                    reservationItem.setActivityName(r.getString(6));
                    reservationItem.setLocationName(r.getString(7));
                    mapReservation.put(reservationExpList,reservationItem);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteReservation(int reservationID){
        try(Statement s = c.createStatement()){
            s.executeUpdate("DELETE FROM REZERVARI WHERE ID="+reservationID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int isOkToDelete(int reservationID){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("  select * from Rezervari" +
                    "  where datediff(day, GETDATE(), convert(datetime, Data ,103)) = 0 AND ID="+reservationID)){
               if(r.next()) {
                   return 1;
               }
               return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intent=new Intent(getApplicationContext(),TeamProfileActivity.class);
        intent.putExtra(Constants.CURRENT_TEAM_ID,currentTeamID);
        startActivity(intent);
    }
}
