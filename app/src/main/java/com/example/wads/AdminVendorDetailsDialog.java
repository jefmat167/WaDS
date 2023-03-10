package com.example.wads;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AdminVendorDetailsDialog extends DialogFragment {

    public interface AdminVendorDetailsDialogListener{
        void onDialogPositiveClick(String number);
        void onDialogNegativeClick(String email);
    }

    AdminVendorDetailsDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String vName = getArguments().getString("name");
        String vNumber = getArguments().getString("number");
        String vEmail = getArguments().getString("email");

        String detailString = "Name: " + vName + "\n" + "Email: " + vEmail + "\n" + "Phone: " + vNumber;

        builder.setTitle("Details: ")
                .setMessage(detailString)
                .setPositiveButton("Call", (dialogInterface, i) -> listener.onDialogPositiveClick(vNumber))
                .setNegativeButton("Email", (dialogInterface, i) -> listener.onDialogNegativeClick(vEmail))
                .setNeutralButton("Cancel", (dialogInterface, i) -> dismiss());

        return builder.create();
    }

    public static AdminVendorDetailsDialog newInstance(String name, String number, String email){
        AdminVendorDetailsDialog avd = new AdminVendorDetailsDialog();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("number", number);
        bundle.putString("email", email);

        avd.setArguments(bundle);
        return avd;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try{
            listener = (AdminVendorDetailsDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + " must implement AdminVendorDetailsDialogListener");
        }
    }
}
