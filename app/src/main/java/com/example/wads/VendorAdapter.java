package com.example.wads;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.CourierViewHolder> {
    private ArrayList<Vendor> mVendorList;

    private OnItemClickListener mListener;

    public VendorAdapter(ArrayList<Vendor> vendor){
        this.mVendorList = vendor;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public CourierViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.courier_cardview, parent, false);
        CourierViewHolder cvh = new CourierViewHolder(v, mListener);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CourierViewHolder holder, int position) {
        Vendor currentVendor = mVendorList.get(position);
        holder.cName.setText(currentVendor.getFullName());
        if (!currentVendor.isSuspended()){
            if (currentVendor.isActive()){
                holder.cStatus.setText("Available");
                holder.imageView.setImageResource(R.drawable.ic_online);
            }else {
                holder.cStatus.setText("Unavailable");
                holder.imageView.setImageResource(R.drawable.ic_offline);
            }
        }else{
            holder.cStatus.setText("Suspended");
            holder.imageView.setImageResource(R.drawable.suspend);
        }
    }

    @Override
    public int getItemCount() {
        return mVendorList.size();
    }

    public static class CourierViewHolder extends RecyclerView.ViewHolder{
        public TextView cName, cStatus;
        public ImageView imageView;

        public CourierViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            cName = itemView.findViewById(R.id.name);
            cStatus = itemView.findViewById(R.id.status);
            imageView = itemView.findViewById(R.id.iv_status);

            itemView.setOnClickListener(view -> {
                if (listener != null){
                    int position = getAbsoluteAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}
