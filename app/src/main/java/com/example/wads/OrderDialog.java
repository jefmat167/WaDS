package com.example.wads;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


public class OrderDialog extends DialogFragment {

    public interface OrderDialogListener {
        void onDialogPositiveClick(String number);
        void onDialogNegativeClick(int position);
    }

    OrderDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        String area = getArguments().getString("area");
        String address = getArguments().getString("address");
        String quantity = getArguments().getString("quantity");
        String number = getArguments().getString("number");
        int position = getArguments().getInt("position");

        builder.setTitle("Order from: " + area)
                .setMessage("Address: " + address + "\n" + "Quantity: " + quantity + "\n" + "Client number: " + number)
                .setPositiveButton("Call client", (dialogInterface, i) -> listener.onDialogPositiveClick(number))
                .setNegativeButton("Attend", (dialogInterface, i) -> listener.onDialogNegativeClick(position));
        return builder.create();
    }

    public static OrderDialog newInstance(String area, String address, String quantity, String clientNumber, int position) {
        OrderDialog frag = new OrderDialog();
        Bundle args = new Bundle();
        args.putString("area", area);
        args.putString("address", address);
        args.putString("quantity", quantity);
        args.putString("number", clientNumber);
        args.putInt("position", position);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OrderDialog.OrderDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + " must implement OrderDialogListener");
        }
    }
}
