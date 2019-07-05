package com.example.teammanagement.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Announcement;

public class AddAnnouncementDialog extends AppCompatDialogFragment {

    private EditText iet_message;
    private AlertDialog.Builder builder;
    private LayoutInflater inflater;
    private AddAnnouncementDialog.AddAnnouncementDialogListner listener;
    private AlertDialog dialog;
    private View view;
    private Announcement announcement;

    private int ok=0;


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

    public void createBuilder(){
        builder.setView(view)
                .setTitle(R.string.add_announcement_title_hint)
                .setNegativeButton(R.string.add_announcement_negativeButton_hint, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(R.string.add_announcement_positiveButton_hint, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String message=iet_message.getText().toString();
                        listener.applyTexts(message,ok);
                    }
                });
    }

    private void initComponents(){
        builder = new AlertDialog.Builder(getActivity(),R.style.BlackTextDialog);
        inflater = getActivity().getLayoutInflater();
        view =inflater.inflate(R.layout.add_announcement_dialog,null);

        announcement= (Announcement) getArguments().get("announcement");


        iet_message=view.findViewById(R.id.add_announcement_tv_message);

        if(announcement != null) {
            iet_message.setText(announcement.getMessage());
            ok=1;
        }

        iet_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(iet_message.getText().length()>2){
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        });

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener=(AddAnnouncementDialog.AddAnnouncementDialogListner) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+getString(R.string.addSportDialog_error_classCastException));
        }
    }

    public interface AddAnnouncementDialogListner{
        void applyTexts( String message, int ok);
    }
}
