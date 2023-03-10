package com.example.wads;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ComplaintAdapter extends RecyclerView.Adapter <ComplaintAdapter.ComplaintViewHolder>{

    private ArrayList<ComplaintModel> complaintList;
    private OnItemClickListener mListener;

    public ComplaintAdapter(ArrayList<ComplaintModel> complaintList) {
        this.complaintList = complaintList;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public ComplaintViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.complaint_list, parent, false);
        ComplaintViewHolder vh = new ComplaintViewHolder(v, mListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintViewHolder holder, int position) {
        ComplaintModel current = complaintList.get(position);
        holder.from.append(current.getFromName());
        holder.about.append(current.getAboutEmail());
    }

    @Override
    public int getItemCount() {
        return complaintList.size();
    }

    public static class ComplaintViewHolder extends RecyclerView.ViewHolder{
        TextView from, about;

        public ComplaintViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            from = itemView.findViewById(R.id.from);
            about = itemView.findViewById(R.id.about);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAbsoluteAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
