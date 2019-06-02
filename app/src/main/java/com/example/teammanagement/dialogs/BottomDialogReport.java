package com.example.teammanagement.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.teammanagement.R;

public class BottomDialogReport extends BottomSheetDialogFragment {
    /*private BottomDialogListener mListener;*/
    Button btn_report;
    Button btn_cancel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_dialog_report,container,false);
        btn_report = view.findViewById(R.id.bottom_dialog_report_btn_report);
        btn_cancel = view.findViewById(R.id.bottom_dialog_report_btn_cancel);

        btn_report.setOnClickListener(clickReport());
        btn_cancel.setOnClickListener(clickCancel());

        return view;
    }

    public View.OnClickListener clickReport(){
        return new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getString(R.string.bottomDialogReport_title_report))
                        .setMessage(getString(R.string.bottomDialogReport_message_alertDialog_report))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.bottomDialogReport_positiveBtn_alertDialog_hint), new DialogInterface.OnClickListener() {
                            @Override

                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                dismiss();
            }
        };
    }

    public View.OnClickListener clickCancel(){
        return new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                dismiss();
            }
        };
    }

    /*public interface BottomDialogListener{
        void onButtonClicked(String text);
    }*/

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomDialogListener) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString()+getString(R.string.addSportDialog_error_classCastException));
        }
    }*/
}
