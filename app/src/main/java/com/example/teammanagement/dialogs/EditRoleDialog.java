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

public class EditRoleDialog extends AppCompatDialogFragment {

    private Spinner spn_role;
    private AlertDialog.Builder builder;
    private LayoutInflater inflater;
    private EditRoleDialogListener listener;
    private AlertDialog dialog;
    private View view;
    private List<String> spn_items = new ArrayList<>();

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        initComponents();
        createBuilder();


        dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        spn_role.setOnItemSelectedListener(spn_role_change());

        return dialog;
    }

    private AdapterView.OnItemSelectedListener spn_role_change(){
        return new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    public void createBuilder(){
        builder.setView(view)
                .setTitle(R.string.editRoleDialog_title_alertDialog_hint)
                .setNegativeButton(R.string.editRoleDialog_negativebutton_hint, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(R.string.editRoleDialog_positivebutton_hint, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String role=spn_role.getSelectedItem().toString();
                        listener.applyTexts(role);
                    }
                });
    }

    private void initComponents(){
        builder = new AlertDialog.Builder(getActivity(),R.style.BlackTextDialog);
        inflater = getActivity().getLayoutInflater();
        view =inflater.inflate(R.layout.edit_role_dialog,null);


        spn_items=Arrays.asList(getResources().getStringArray(R.array.team_role));

        spn_role=view.findViewById(R.id.editRoleDialog_spn_sport);

        SpinnerAdapter adapter=new SpinnerAdapter(getContext(),R.layout.spinner_item,spn_items,inflater);
        spn_role.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener=(EditRoleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+getString(R.string.addSportDialog_error_classCastException));
        }
    }

    public interface EditRoleDialogListener{
        void applyTexts(String role);
    }
}
