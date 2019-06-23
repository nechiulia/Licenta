package com.example.teammanagement.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import com.example.teammanagement.R;
import com.example.teammanagement.Utils.NewLocation;
import com.example.teammanagement.adapters.ExpandableListNewLocationAdminAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class NewLocationsFragment extends Fragment {
    ExpandableListView lv_newLocations;
    ImageButton ibtn_aprove;
    ImageButton ibtn_remove;
    ExpandableListNewLocationAdminAdapter listAdapter;
    private HashMap<String, NewLocation> mapLocations = new HashMap<>();
    private List<String> listUsersDate = new ArrayList<>();
    private List<NewLocation> listNewLocations = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newlocations,null);
        lv_newLocations =view.findViewById(R.id.fragment_newLocations_lv);
        ibtn_aprove=view.findViewById(R.id.fragment_newLocations_ibtn_aproveFeedback);
        ibtn_remove=view.findViewById(R.id.fragment_newLocations_ibtn_remove);

        ibtn_remove.setOnClickListener(clickRemove());
        ibtn_aprove.setOnClickListener(clickAprove());

        initData();

        if(listUsersDate.size() != 0 && mapLocations.size() !=0) {
            listAdapter = new ExpandableListNewLocationAdminAdapter(this.getContext(), listUsersDate, mapLocations);
            lv_newLocations.setAdapter(listAdapter);
        }

        return view;

    }

    private View.OnClickListener clickRemove() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listAdapter.getmCheckedItems().size() > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setTitle("Respinge raportare feedback")
                            .setMessage("Sunteți sigur că doriți să ștergeți aceast raport?")
                            .setNegativeButton("Nu", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
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

                    builder.setTitle("Aprobă raportare feedback")
                            .setMessage("Sunteți sigur că doriți să aprobați cererea și să ștergeți feedback-ul?")
                            .setNegativeButton("Nu", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
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

    public void getKeys(List<NewLocation> list_NewLocations){

        for(NewLocation l: list_NewLocations){
            listUsersDate.add(l.getUserName());
        }
    }

    public void initData(){
        NewLocation location1 = new NewLocation("Studio IDance București","office@idancestudio.ro","IDance Studio by Catalin & Andreea","031424","Traian Popovici 87A");
        NewLocation location2 = new NewLocation("Studio HappyFeet București","happyfeetstudio@gmail.com","Happy Feet Studio","030301","Șoseaua Mihai Bravu 223");
        listNewLocations.add(location1);
        listNewLocations.add(location2);

        getKeys(listNewLocations);

        mapLocations.put(location1.getUserName(), location1);
        mapLocations.put(location2.getUserName(),location2);
    }
}
