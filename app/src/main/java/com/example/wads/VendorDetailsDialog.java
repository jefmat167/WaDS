package com.example.wads;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class VendorDetailsDialog extends DialogFragment {

    String number;

    public interface VendorDetailsDialogListener{
        void onDialogPositiveClick(String number);
        void onDialogNegativeClick(String email);
    }

    VendorDetailsDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String dName = getArguments().getString("name");
        String dNumber = getArguments().getString("number");
        String dEmail = getArguments().getString("email");
        builder.setTitle(dName)
                .setMessage("Call " + dNumber + "?")
                .setPositiveButton("Call", (dialogInterface, i) -> {
                    number = dNumber;
                    listener.onDialogPositiveClick(number);
                })
                .setNegativeButton("Report", (dialogInterface, i) -> listener.onDialogNegativeClick(dEmail))
                .setNeutralButton("Cancel", (dialogInterface, i) -> dismiss());
        return builder.create();
    }

    public static VendorDetailsDialog newInstance(String name, String number, String email){
        VendorDetailsDialog vd = new VendorDetailsDialog();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("number", number);
        bundle.putString("email", email);

        vd.setArguments(bundle);
        return vd;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try{
            listener = (VendorDetailsDialogListener) context;

        }catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + " must implement VendorDetailsDialogListener");
        }
    }
}
