package com.example.wads;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AdminRestoreAccountDialog extends DialogFragment {

    public interface AdminRestoreAccountDialogListener{
        void onRestoreDialogPositiveClick(String email);
        void onRestoreDialogNegativeClick();
    }

    AdminRestoreAccountDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        String vEmail = getArguments().getString("email");
        builder.setTitle("")
                .setMessage("This account is suspended")
                .setPositiveButton("Restore", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onRestoreDialogPositiveClick(vEmail);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });
        return builder.create();
    }

    public static AdminRestoreAccountDialog newInstance(String email){
        AdminRestoreAccountDialog arvd = new AdminRestoreAccountDialog();
        Bundle bundle = new Bundle();
        bundle.putString("email", email);

        arvd.setArguments(bundle);
        return arvd;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            listener = (AdminRestoreAccountDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + " must implement AdminRestoreAccountDialogListener");
        }
    }
}
