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

public class AddSportDialog extends AppCompatDialogFragment {

    private Spinner spn_sport;
    private Spinner spn_level;
    private AddSportDialogListener listener;
    private ArrayList<String> spnSport_items=new ArrayList<>();
    private List<String> spnLevel_items = new ArrayList<>();
    AlertDialog.Builder builder;
    LayoutInflater inflater;
    AlertDialog dialog;
    View view;
    int ok1 = 0;
    int ok2 = 0;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        initComponents();
        verifyListSize();
        createBuilder();


        dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        spn_sport.setOnItemSelectedListener(spn_sport_change());
        spn_level.setOnItemSelectedListener(spn_level_change());

        return dialog;
    }

    private AdapterView.OnItemSelectedListener spn_sport_change(){
        return new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    ok1 = 1;
                }
                if(ok1 ==1 && ok2 ==1){
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    private AdapterView.OnItemSelectedListener spn_level_change(){
        return new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    ok2=1;
                }
                if(ok1 ==1 && ok2 ==1){
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    private void initComponents(){
        builder = new AlertDialog.Builder(getActivity(),R.style.BlackTextDialog);
        inflater = getActivity().getLayoutInflater();
        view =inflater.inflate(R.layout.add_sport_dialog,null);


        spnSport_items = getArguments().getStringArrayList(Constants.SEND_SPORTSLIST);
        spnLevel_items = Arrays.asList(getResources().getStringArray(R.array.dialog_level));

        spn_level=view.findViewById(R.id.addSportDialog_spn_level);
        spn_sport=view.findViewById(R.id.addSportDialog_spn_sport);

        SpinnerAdapter sport_adapter=new SpinnerAdapter(getContext(),R.layout.spinner_item,spnSport_items,inflater);
        spn_sport.setAdapter(sport_adapter);
        SpinnerAdapter level_adapter=new SpinnerAdapter(getContext(),R.layout.spinner_item,spnLevel_items,inflater);
        spn_level.setAdapter(level_adapter);
    }

    public void verifyListSize(){
        if(spnSport_items.size() <= 1){
            spn_sport.setBackground(ContextCompat.getDrawable(getContext(),R.color.white));
            spn_sport.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
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
                        String sport=spn_sport.getSelectedItem().toString();
                        String level=spn_level.getSelectedItem().toString();
                        listener.applyTexts(sport,level);
                    }
                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener=(AddSportDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+getString(R.string.addSportDialog_error_classCastException));
        }
    }

    public interface AddSportDialogListener{
        void applyTexts(String sport, String level);
    }
}
