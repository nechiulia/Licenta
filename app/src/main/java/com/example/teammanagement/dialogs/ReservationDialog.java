package com.example.teammanagement.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.adapters.SpinnerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReservationDialog extends AppCompatDialogFragment {

    AlertDialog.Builder builder;
    LayoutInflater inflater;
    AlertDialog dialog;
    View view;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        initComponents();
        createBuilder();


        dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        return dialog;
    }

    private void initComponents(){
        builder = new AlertDialog.Builder(getActivity(), R.style.BlackTextDialog);
        inflater = getActivity().getLayoutInflater();
        view =inflater.inflate(R.layout.reservation_dialog,null);




    }

    public void createBuilder(){
        builder.setView(view)
                .setTitle(R.string.addSportDialog_title_hint)
                .setNegativeButton(R.string.addSportDialog_cancel_hint, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(R.string.addSportDialog_addSport_hint, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
    }

}