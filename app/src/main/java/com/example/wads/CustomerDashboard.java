package com.example.wads;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class CustomerDashboard extends AppCompatActivity implements VendorDetailsDialog.VendorDetailsDialogListener {
    private FirebaseFirestore db;
//    private FirebaseUser user;

    private Vendor vendor;
    private ArrayList<Vendor> vendorList;
    private RecyclerView recyclerView;
    private VendorAdapter adapter;

    private ProgressDialog dialog;
    private Toolbar toolbar;

    private String userName;
    //    private TextView dName, dStatus, dNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        db = FirebaseFirestore.getInstance();
        vendorList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler);
        toolbar = findViewById(R.id.toolbar);

        setToolbarTitle();
        buildDialog();
        buildRecyclerView();
        getVendors();

    }

    private void setToolbarTitle() {
        Intent intent = getIntent();
        userName = intent.getStringExtra("name");
        toolbar.setTitle(userName);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(CustomerDashboard.this, SignIn.class));
                finish();
                return true;

            case R.id.request:
                startActivity(new Intent(CustomerDashboard.this, Order.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void buildRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VendorAdapter(vendorList);

        adapter.setOnItemClickListener(position -> {

            String name = vendorList.get(position).getFullName();
            String email = vendorList.get(position).getEmail();
            long number = vendorList.get(position).getPhone();
            boolean status = vendorList.get(position).isSuspended();

            if (status) {
                Toast.makeText(CustomerDashboard.this, "This account is suspended", Toast.LENGTH_LONG).show();
                return;
            }

            String mNumber = String.valueOf(number);

            showCallDialog(name, mNumber, email);
        });
        recyclerView.setAdapter(adapter);
    }

    private void showCallDialog(String name, String mNumber, String email) {
        DialogFragment fragment = VendorDetailsDialog.newInstance(name, mNumber, email);
        fragment.show(getSupportFragmentManager(), "Details");
    }

    private void buildDialog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getVendors(){
        db.collection("users").get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot document : task.getResult()){
                            if(document.contains("active")){
                                vendor = document.toObject(Vendor.class);
                                vendorList.add(vendor);
                            }
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    }
                }).addOnFailureListener(e -> {
                    dialog.dismiss();
                    Toast.makeText(CustomerDashboard.this, "Failed to load Couriers", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onDialogPositiveClick(String number) {
        Uri uri = Uri.parse("tel:" + number);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        try {
            startActivity(intent);
        }catch (SecurityException s){
            Toast.makeText(CustomerDashboard.this, s.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDialogNegativeClick(String email) {
        Intent intent = new Intent(CustomerDashboard.this, Complaint.class);
        intent.putExtra("email", email);
        intent.putExtra("name", userName);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(CustomerDashboard.this, SignIn.class));
        finish();
    }
}