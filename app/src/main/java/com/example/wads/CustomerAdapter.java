package com.example.wads;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {
    private ArrayList<Customer> mCustomerList;

    private OnItemClickListener mListener;

    public CustomerAdapter (ArrayList<Customer> customer){
        this.mCustomerList = customer;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_cardview, parent, false);
        CustomerViewHolder cvh = new CustomerViewHolder(v, mListener);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Customer currentCustomer = mCustomerList.get(position);
        holder.cName.setText(currentCustomer.getFullName());
    }

    @Override
    public int getItemCount() {
        return mCustomerList.size();
    }

    public static class CustomerViewHolder extends RecyclerView.ViewHolder{
        public TextView cName;

        public CustomerViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            cName = itemView.findViewById(R.id.customerName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAbsoluteAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
