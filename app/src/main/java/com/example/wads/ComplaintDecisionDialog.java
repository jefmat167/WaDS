package com.example.wads;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class ComplaintDecisionDialog extends DialogFragment {

    String sanction;

    ComplaintDecisionDialogListener listener;

    public interface ComplaintDecisionDialogListener{
        void onPositiveClick(String sanction);
        void onNegativeClick();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        sanction = "";
        builder
                .setTitle("How do you want to sanction the defaulter?")
                .setSingleChoiceItems(R.array.sanctions, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                sanction = "Email";
                                break;
                            case 1:
                                sanction = "Suspend";
                                break;
                            default:
                                dismiss();
                        }
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onPositiveClick(sanction);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onNegativeClick();
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ComplaintDecisionDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + " must implement ComplaintDecisionDialogListener");
        }
    }
}
