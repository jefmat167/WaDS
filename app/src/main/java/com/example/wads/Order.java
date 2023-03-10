package com.example.wads;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Order extends AppCompatActivity {
    private EditText quantity, address, area;
    private Button order, cancel;

    private ProgressDialog dialog;

    private FirebaseFirestore db;
    private FirebaseUser user;

    private Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        quantity = findViewById(R.id.et_qnty);
        area = findViewById(R.id.et_area);
        address = findViewById(R.id.et_addrss);
        order = findViewById(R.id.order_btn);
        cancel = findViewById(R.id.cancel);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        getCustomer();

        order.setOnClickListener(view -> placeOrder());

        cancel.setOnClickListener(view -> startActivity(new Intent(Order.this, CustomerDashboard.class)));
    }

    private void placeOrder() {
        String mQuantity = quantity.getText().toString();
        String mAddress = address.getText().toString();
        String mArea = area.getText().toString();

        if (mQuantity.isEmpty()) {
            quantity.setError("Specify how many gallons you want");
            quantity.requestFocus();
            return;
        }

        if (mAddress.isEmpty()) {
            address.setError("Please provide your address");
            address.requestFocus();
            return;
        }

        if (mArea.isEmpty()) {
            area.setError("Please specify you lodge name");
            area.requestFocus();
            return;
        }

        String phone = String.valueOf(customer.phone);
        buildDialog();

        OrderModel model = new OrderModel(mQuantity, mAddress, mArea, customer.fullName, phone, false);
        db.collection("orders").add(model)
                .addOnSuccessListener(documentReference -> {
                    dialog.dismiss();
                    Toast.makeText(Order.this, "Your order was successful", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    dialog.dismiss();
                    Toast.makeText(Order.this, "Your order failed", Toast.LENGTH_SHORT).show();
                });
    }

    private void getCustomer() {
        db.collection("users").document(user.getEmail()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.contains("active")) {
                        customer = documentSnapshot.toObject(Customer.class);
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(Order.this, "Something went wrong", Toast.LENGTH_SHORT).show());
    }

    private void buildDialog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Signing in...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Order.this, CustomerDashboard.class));
        finish();
    }
}