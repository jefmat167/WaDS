package com.example.wads;

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
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ViewVendors extends AppCompatActivity implements
        AdminVendorDetailsDialog.AdminVendorDetailsDialogListener,
        AdminRestoreAccountDialog.AdminRestoreAccountDialogListener{

    private Vendor vendor;
    private ArrayList<Vendor> vendorList;
    private RecyclerView recyclerView;
    private VendorAdapter adapter;

    private FirebaseFirestore db;

    private ProgressDialog dialog;

    DialogFragment dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_vendors);

        recyclerView = findViewById(R.id.ve_recycler);
        Toolbar toolbar = findViewById(R.id.tb_vendor);

        toolbar.setTitleTextColor(getColor(R.color.blue));
        setSupportActionBar(toolbar);

        db = FirebaseFirestore.getInstance();
        vendorList = new ArrayList<>();

        buildDialog();
        buildRecyclerView();
        getVendors();

    }

    private void buildRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VendorAdapter(vendorList);

        adapter.setOnItemClickListener(position -> {

            String name = vendorList.get(position).getFullName();
            String email = vendorList.get(position).getEmail();
            boolean status = vendorList.get(position).isSuspended();
            long number = vendorList.get(position).getPhone();

            String mNumber = String.valueOf(number);

            if (status){
                dialogFragment = AdminRestoreAccountDialog.newInstance(email);
                dialogFragment.show(getSupportFragmentManager(), "restore");

            }else {
                dialogFragment = AdminVendorDetailsDialog.newInstance(name, mNumber, email);
                dialogFragment.show(getSupportFragmentManager(), "view details");
            }
        });
        recyclerView.setAdapter(adapter);
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
                            if (dialog.isShowing()){
                                dialog.dismiss();
                            }
                        }
                    }
                }).addOnFailureListener(e -> {
                    if (dialog.isShowing()){
                        dialog.dismiss();
                    }
                    Toast.makeText(ViewVendors.this, "Failed to load Couriers", Toast.LENGTH_SHORT).show();
                });
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
            Toast.makeText(ViewVendors.this, s.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDialogNegativeClick(String email) {
        String subject = "";
        String body = "";

        Intent mailIntent = new Intent(Intent.ACTION_SEND);
        mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        mailIntent.putExtra(Intent.EXTRA_TEXT, body);

        mailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(mailIntent, "Choose an email client: "));
    }

    @Override
    public void onRestoreDialogPositiveClick(String email) {
        db.collection("users").document(email).update("suspended", false)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getApplicationContext(), "Account restored", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ViewVendors.this, AdminDashboard.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(ViewVendors.this, "Something went wrong" + "\n" + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onRestoreDialogNegativeClick() {

    }
}