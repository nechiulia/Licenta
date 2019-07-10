package com.example.teammanagement.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.teammanagement.Utils.Report;
import com.example.teammanagement.activities.LoginActivity;
import com.example.teammanagement.adapters.ExpandableListReportAdminAdapter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ReportsFragment extends Fragment {

    private ExpandableListView lv_report;
    private ImageButton ibtn_aprove;
    private ImageButton ibtn_remove;
    private ImageButton ibtn_logOut;

    private ExpandableListReportAdminAdapter listAdapter;

    private Intent intent;

    private HashMap<String,List<String>> mapReport = new HashMap<>();
    private List<String> listUsersDate = new ArrayList<>();
    private List<Report> listReports=new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports,null);
        lv_report = view.findViewById(R.id.fragment_reports_lv);
        ibtn_aprove=view.findViewById(R.id.fragment_reports_ibtn_aproveFeedback);
        ibtn_remove=view.findViewById(R.id.fragment_reports_ibtn_remove);
        ibtn_logOut=view.findViewById(R.id.fragment_reports_ibtn_logout);

        ibtn_remove.setOnClickListener(clickRemove());
        ibtn_aprove.setOnClickListener(clickAprove());
        ibtn_logOut.setOnClickListener(clickLogOut());

        initData();

        if(listUsersDate.size() != 0 && mapReport.size() !=0) {
            listAdapter = new ExpandableListReportAdminAdapter(this.getContext(), listUsersDate, mapReport);
            lv_report.setAdapter(listAdapter);
        }

        return view;

    }

    public void getKeys(List<Report> list_Reports){

        for(Report r: list_Reports){
            listUsersDate.add(new StringBuilder(r.getUserSender()+"  ->  "+ r.getUserReceiver()+"   "+"15-10-2019 10:00:10").toString());
        }
    }

    public void deleteGroupsFromList(){
        int listDimenssion= listAdapter.getmCheckedItems().size();
        List<Long> list = new ArrayList<>(listAdapter.getmCheckedItems());
        Collections.sort(list);
        Collections.reverse(list);
        for(int i=0; i< listDimenssion;i++){
            mapReport.remove(listAdapter.getListParent().get(list.get(i).intValue()));
            listUsersDate.remove(listAdapter.getListParent().get(list.get(i).intValue()));
        }
        listAdapter.getmCheckedItems().clear();
        list.clear();
        listAdapter.notifyDataSetChanged();
    }

    public void initData(){
        try {
            listReports.add(new Report("Marcel","Ionsael","Cel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucatorCel mai slab jucator",new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse("15-10-2019 10:00:10")));
            listReports.add(new Report("Mirel","Igoter","Comentariu nepotrivit",new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse("15-10-2019 10:00:10")));
            listReports.add(new Report("Mirel","Igoer","Comentariu nepotrivit",new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse("15-10-2019 10:00:10")));
            listReports.add(new Report("Mirel","Igorr","Comentariu nepotrivit",new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse("15-10-2019 10:00:10")));
            listReports.add(new Report("Mirel","Igodr","Comentariu nepotrivit",new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse("15-10-2019 10:00:10")));
            listReports.add(new Report("Mirel","Igofr","Comentariu nepotrivit",new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse("15-10-2019 10:00:10")));
            listReports.add(new Report("Mirel","Igord","Comentariu nepotrivit",new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse("15-10-2019 10:00:10")));
            listReports.add(new Report("Mirel","Igorg","Comentariu nepotrivit",new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse("15-10-2019 10:00:10")));
            listReports.add(new Report("Mirel","Igorgg","Comentariu nepotrivit",new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse("15-10-2019 10:00:10")));
            listReports.add(new Report("Mirel","Igg","Comentariu nepotrivit",new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse("15-10-2019 10:00:10")));
            listReports.add(new Report("Mirel","Ma","Comentariu nepotrivit",new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse("15-10-2019 10:00:10")));
            listReports.add(new Report("Mirel","Marcela","Comentariu nepotrivit",new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse("15-10-2019 10:00:10")));
            listReports.add(new Report("Mirel","or","Comentariu nepotrivit",new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse("15-10-2019 10:00:10")));
            listReports.add(new Report("Mirel","Igr","Comentariu nepotrivit",new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse("15-10-2019 10:00:10")));
            listReports.add(new Report("Mirel","Ior","Comentariu nepotrivit",new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse("15-10-2019 10:00:10")));
            listReports.add(new Report("Mirel","Igor","Comentariu nepotrivit",new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse("15-10-2019 10:00:10")));
            listReports.add(new Report("Georgel","Ig","Comentariu nepotrivit",new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse("15-10-2019 10:00:10")));
            listReports.add(new Report("Miresl","or","Comentariu nepotrivit",new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse("15-10-2019 10:00:10")));
            listReports.add(new Report("Miredasl","Igr","Comentariu nepotrivit",new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse("15-10-2019 10:00:10")));
            listReports.add(new Report("Miredsadal","Ior","Comentariu nepotrivit",new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse("15-10-2019 10:00:10")));
            listReports.add(new Report("Mirefsfal","Igor","Comentariu nepotrivit",new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse("15-10-2019 10:00:10")));
            listReports.add(new Report("Georsfagel","Ig","Comentariu nepotrivit",new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse("15-10-2019 10:00:10")));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        getKeys(listReports);

        mapReport.put(listUsersDate.get(0), Arrays.asList(listReports.get(0).getFeedback()));
        mapReport.put(listUsersDate.get(1),Arrays.asList(listReports.get(1).getFeedback()));
        mapReport.put(listUsersDate.get(2),Arrays.asList(listReports.get(2).getFeedback()));
        mapReport.put(listUsersDate.get(3),Arrays.asList(listReports.get(3).getFeedback()));
        mapReport.put(listUsersDate.get(4),Arrays.asList(listReports.get(4).getFeedback()));
        mapReport.put(listUsersDate.get(5),Arrays.asList(listReports.get(5).getFeedback()));
        mapReport.put(listUsersDate.get(6),Arrays.asList(listReports.get(6).getFeedback()));
        mapReport.put(listUsersDate.get(7),Arrays.asList(listReports.get(7).getFeedback()));
        mapReport.put(listUsersDate.get(8),Arrays.asList(listReports.get(8).getFeedback()));
        mapReport.put(listUsersDate.get(9),Arrays.asList(listReports.get(9).getFeedback()));
        mapReport.put(listUsersDate.get(10),Arrays.asList(listReports.get(10).getFeedback()));
        mapReport.put(listUsersDate.get(11),Arrays.asList(listReports.get(11).getFeedback()));
        mapReport.put(listUsersDate.get(12),Arrays.asList(listReports.get(12).getFeedback()));
        mapReport.put(listUsersDate.get(13),Arrays.asList(listReports.get(13).getFeedback()));
        mapReport.put(listUsersDate.get(14),Arrays.asList(listReports.get(14).getFeedback()));
        mapReport.put(listUsersDate.get(15),Arrays.asList(listReports.get(15).getFeedback()));
        mapReport.put(listUsersDate.get(16),Arrays.asList(listReports.get(16).getFeedback()));
        mapReport.put(listUsersDate.get(17),Arrays.asList(listReports.get(17).getFeedback()));
        mapReport.put(listUsersDate.get(18),Arrays.asList(listReports.get(18).getFeedback()));
        mapReport.put(listUsersDate.get(19),Arrays.asList(listReports.get(19).getFeedback()));
        mapReport.put(listUsersDate.get(20),Arrays.asList(listReports.get(20).getFeedback()));
        mapReport.put(listUsersDate.get(21),Arrays.asList(listReports.get(21).getFeedback()));

    }

    private View.OnClickListener clickRemove() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listAdapter.getmCheckedItems().size() > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setTitle(getString(R.string.reports_fragment_alertDialog_removeFeedback_title))
                            .setMessage(getString(R.string.reports_fragment_alertDialog_removeFeedback_message))
                            .setNegativeButton(getString(R.string.reports_fragment_alertDialog_removeFeedback_negativeButton_hint), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton(getString(R.string.reports_fragment_alertDialog_removeFeedback_positiveButton_hint), new DialogInterface.OnClickListener() {
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

                    builder.setTitle(getString(R.string.reports_fragment_alertDialog_aproveFeedback_title))
                            .setMessage(getString(R.string.reports_fragment_alertDialog_aproveFeedback_message_hint))
                            .setNegativeButton(getString(R.string.reports_fragment_alertDialog_aproveFeedback_negativeButton_hint), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton(getString(R.string.reports_fragment_alertDialog_aproveFeedback_positiveButton_hint), new DialogInterface.OnClickListener() {
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
