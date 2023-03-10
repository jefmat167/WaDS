package com.example.wads;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class AdminDashboard extends AppCompatActivity {
    Button customer, vendor, complaint, signout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        customer = findViewById(R.id.customer);
        vendor = findViewById(R.id.vendors);
        complaint = findViewById(R.id.complaint);
        signout = findViewById(R.id.sign_out);

        complaint.setOnClickListener(view -> startActivity(new Intent(AdminDashboard.this, Complaints.class)));

        vendor.setOnClickListener(view -> startActivity(new Intent(AdminDashboard.this, ViewVendors.class)));

        customer.setOnClickListener(view -> startActivity(new Intent(AdminDashboard.this, ViewCustomers.class)));

        signout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(AdminDashboard.this, SignIn.class));
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(AdminDashboard.this, SignIn.class));
        finish();
    }
}