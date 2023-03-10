package com.example.wads;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class Orders extends AppCompatActivity implements OrderDialog.OrderDialogListener{
    private RecyclerView recyclerView;
    private OrdersAdapter adapter;
    private ArrayList<OrderModel> modelList;
    private OrderModel model;

    private FirebaseFirestore db;

    private TextView info;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        recyclerView = findViewById(R.id.order_recyclerview);
        info = findViewById(R.id.info);
        info.setVisibility(View.GONE);

        modelList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        buildDialog();
        getOrders();
        buildRecyclerView();

    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    private void getOrders() {
        db.collection("orders").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        if (task.getResult().isEmpty()){
                            info.setText("No Orders yet");
                            info.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                        }else {
                            for (QueryDocumentSnapshot document : task.getResult()){
                                model = document.toObject(OrderModel.class);
                                modelList.add(model);
                            }
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    dialog.dismiss();
                    info.setText("Something went wrong");
                    info.setVisibility(View.VISIBLE);
                });
    }

    private void buildRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrdersAdapter(modelList);

        adapter.setOnItemClickListener(position -> {
            String number = modelList.get(position).getFromNumber();
            String address = modelList.get(position).getAddress();
            String area = modelList.get(position).getArea();
            String qnty = modelList.get(position).getQuantity();

            String mNumber = String.valueOf(number);

            showOrderDialog(area, address, qnty, mNumber, position);

        });

        recyclerView.setAdapter(adapter);
    }

    private void showOrderDialog(String area, String address, String quantity, String number, int position) {
        DialogFragment fg = OrderDialog.newInstance(area, address, quantity, number, position);
        fg.show(getSupportFragmentManager(), "order");
    }

    private void buildDialog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onDialogPositiveClick(String number) {
        Uri uri = Uri.parse("tel:" + number);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        try {
            startActivity(intent);
        }catch (SecurityException s){
            Toast.makeText(Orders.this, s.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDialogNegativeClick(int position) {
        modelList.remove(position);
        adapter.notifyItemRemoved(position);
        startActivity(new Intent(Orders.this, VendorDashboard.class));
        finish();

    }
}