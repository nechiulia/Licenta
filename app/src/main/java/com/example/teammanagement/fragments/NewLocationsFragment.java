package com.example.teammanagement.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.NewLocation;
import com.example.teammanagement.Utils.SharedViewModel;
import com.example.teammanagement.activities.LoginActivity;
import com.example.teammanagement.adapters.ExpandableListNewLocationAdminAdapter;
import com.example.teammanagement.database.JDBCController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;



public class NewLocationsFragment extends Fragment  {
    private ExpandableListView lv_newLocations;
    private ImageButton ibtn_aprove;
    private ImageButton ibtn_remove;
    private ImageButton ibtn_logOut;

    private Intent intent;

    private ExpandableListNewLocationAdminAdapter listAdapter;

    private JDBCController jdbcController;
    private Connection c;

    private SharedViewModel model;

    private double lat;
    private int ok=0;
    private double longitude;
    private NewLocation selectedLocation;
    private HashMap<String, NewLocation> mapLocations = new HashMap<>();
    private List<String> listUsersDate = new ArrayList<>();
    private List<NewLocation> listNewLocations = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newlocations,null);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        jdbcController =JDBCController.getInstance();
        c=jdbcController.openConnection();

        lv_newLocations =view.findViewById(R.id.fragment_newLocations_lv);
        ibtn_aprove=view.findViewById(R.id.fragment_newLocations_ibtn_aproveFeedback);
        ibtn_remove=view.findViewById(R.id.fragment_newLocations_ibtn_remove);
        ibtn_logOut=view.findViewById(R.id.fragment_newLocation_ibtn_logout);

        ibtn_remove.setOnClickListener(clickRemove());
        ibtn_aprove.setOnClickListener(clickAprove());
        ibtn_logOut.setOnClickListener(clickLogOut());

        initData();

        if(listUsersDate.size() != 0 && mapLocations.size() !=0) {
            listAdapter = new ExpandableListNewLocationAdminAdapter(this.getContext(), listUsersDate, mapLocations);
            lv_newLocations.setAdapter(listAdapter);
        }


        model = ViewModelProviders.of(this.getActivity()).get(SharedViewModel.class);
        model.getSelected().observe(this, new Observer<NewLocation>() {
            @Override
            public void onChanged(@Nullable NewLocation location) {
                Log.d("CEVA", String.valueOf(model.getSelected().getValue()));
                selectedLocation=model.getSelected().getValue();
                if(selectedLocation.getLatitude() != 0.0 && selectedLocation.getLongitude() != 0.0){
                    for(NewLocation selectedLoc:listNewLocations){
                        selectLocationVerify(selectedLoc);
                        if(selectedLoc.getLocationID() == selectedLocation.getLocationID() && (selectedLocation.getLatitude()!= lat || selectedLocation.getLongitude() != longitude)){
                            selectedLoc.setLongitude(selectedLocation.getLongitude());
                            selectedLoc.setLatitude(selectedLocation.getLatitude());
                            listAdapter.notifyDataSetChanged();
                            updateLonLat(selectedLocation);
                        }
                    }
                }

            }
        });


        return view;

    }

    public void selectLocationVerify(NewLocation location){
        try(Statement s = c.createStatement()) {
            try (ResultSet r = s.executeQuery("SELECT LATITUDINE,LONGITUDINE FROM LOCATII WHERE STARE=2 AND ID="+location.getLocationID())) {
                if(r.next()){
                    lat=r.getDouble(1);
                    longitude=r.getDouble(2);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectLocations(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT * FROM LOCATII WHERE STARE=2")){
                while (r.next()){
                    NewLocation newLocation = new NewLocation();
                    newLocation.setLocationID(r.getInt(1));
                    newLocation.setLocationName(r.getString(2));
                    newLocation.setPostalCode(r.getString(3));
                    newLocation.setAddress(r.getString(4));
                    newLocation.setLatitude(r.getDouble(5));
                    newLocation.setLongitude(r.getDouble(6));
                    newLocation.setResevation(r.getByte(7));
                    newLocation.setState(r.getInt(8));
                    newLocation.setUserID(r.getInt(9));
                    listUsersDate.add(r.getString(2));
                    mapLocations.put(r.getString(2),newLocation);
                    listNewLocations.add(newLocation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectUsername(NewLocation location){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT NUMEUTILIZATOR,EMAIL FROM UTILIZATORI WHERE ID="+location.getUserID())){
                if (r.next()){
                    location.setUserName(r.getString(1));
                    location.setEmail(r.getString(2));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateLonLat(NewLocation selLocation){
        try(Statement s = c.createStatement()){
            s.executeUpdate("UPDATE LOCATII SET LATITUDINE ="+selLocation.getLatitude()+", LONGITUDINE="+selLocation.getLongitude()+" WHERE ID="+selLocation.getLocationID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStateLocation(NewLocation aprovedLocation){
        try(Statement s = c.createStatement()){
            s.executeUpdate("UPDATE LOCATII SET STARE=0 WHERE ID="+aprovedLocation.getLocationID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUserState(NewLocation aprovedLocation){
        try(Statement s = c.createStatement()){
            s.executeUpdate("UPDATE UTILIZATORI SET STARE=0 WHERE ID="+aprovedLocation.getUserID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteLocation(NewLocation location){
        try(Statement s = c.createStatement()){
            s.executeUpdate("DELETE FROM LOCATII WHERE ID="+location.getLocationID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(NewLocation location){
        try(Statement s = c.createStatement()){
            s.executeUpdate("DELETE FROM UTILIZATORI WHERE ID="+location.getUserID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteGroupsFromList(){
        int listDimenssion= listAdapter.getmCheckedItems().size();
        List<Long> list = new ArrayList<>(listAdapter.getmCheckedItems());
        Collections.sort(list);
        Collections.reverse(list);
        for(int i=0; i< listDimenssion;i++){
            mapLocations.remove(listAdapter.getListParent().get(list.get(i).intValue()));
            listUsersDate.remove(listAdapter.getListParent().get(list.get(i).intValue()));
        }
        listAdapter.getmCheckedItems().clear();
        list.clear();
        listAdapter.notifyDataSetChanged();
    }

    private View.OnClickListener clickRemove() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listAdapter.getmCheckedItems().size() > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setTitle(getString(R.string.newLocations_fragment_alertDialog_delete_title))
                            .setMessage(getString(R.string.newLocations_fragment_alertDialog_delete_message_hint))
                            .setNegativeButton(getString(R.string.newLocations_fragment_alertDialog_delete_negativeButton_hint), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton(getString(R.string.newLocations_fragment_alertDialog_delete_positiveButton_hint), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    int listDimenssion= listAdapter.getmCheckedItems().size();
                                    List<Long> list = new ArrayList<>(listAdapter.getmCheckedItems());
                                    for(int i=0; i< listDimenssion;i++) {
                                        deleteLocation(listNewLocations.get(list.get(i).intValue()));
                                        deleteUser(listNewLocations.get(list.get(i).intValue()));
                                    }
                                    deleteGroupsFromList();
                                }
                            });
                    Dialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                }
            }
        };
    }


    private View.OnClickListener clickAprove() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listAdapter.getmCheckedItems().size() > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setTitle(getString(R.string.newLocations_fragment_alertDialog_aprove_title))
                            .setMessage(getString(R.string.newLocations_fragment_alertDialog_aprove_message_hint))
                            .setNegativeButton(getString(R.string.newLocations_fragment_alertDialog_aprove_negativeButton_hint), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton(getString(R.string.newLocations_fragment_alertDialog_aprove_positiveButton_hint), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    int listDimenssion= listAdapter.getmCheckedItems().size();
                                    List<Long> list = new ArrayList<>(listAdapter.getmCheckedItems());
                                    for(int i=0; i< listDimenssion;i++) {
                                        if(listNewLocations.get(list.get(i).intValue()).getLongitude()!= 0.0 && listNewLocations.get(list.get(i).intValue()).getLatitude() !=0.0) {
                                            updateStateLocation(listNewLocations.get(list.get(i).intValue()));
                                            updateUserState(listNewLocations.get(list.get(i).intValue()));
                                        }
                                    }
                                    deleteGroupsFromList();
                                }
                            });
                    Dialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                }
            }
        };
    }



    public void initData(){
        selectLocations();

        for(NewLocation l: listNewLocations){
            selectUsername(l);
        }

    }

    private View.OnClickListener clickLogOut() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        };
    }

}
