package com.example.wads;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>{

    private ArrayList<OrderModel> orderList;

    private OnItemClickListener mListener;

    public OrdersAdapter(ArrayList<OrderModel> list){
        this.orderList = list;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_cardview, parent, false);
        OrdersViewHolder ordersViewHolder = new OrdersViewHolder(v, mListener);
        return ordersViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {
        OrderModel order = orderList.get(position);
        holder.area.append(order.getArea());
        holder.quantity.append(order.getQuantity());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrdersViewHolder extends RecyclerView.ViewHolder {
        public TextView area, quantity;

        public OrdersViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            area = itemView.findViewById(R.id.tv_area);
            quantity = itemView.findViewById(R.id.tv_qnty);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAbsoluteAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
