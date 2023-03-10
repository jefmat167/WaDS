package com.example.wads;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ViewCustomers extends AppCompatActivity implements AdminVendorDetailsDialog.AdminVendorDetailsDialogListener{
    private FirebaseFirestore db;

    private Customer customer;
    private ArrayList<Customer> customerList;
    private RecyclerView recyclerView;
    private CustomerAdapter adapter;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customers);

        recyclerView = findViewById(R.id.cu_recycler);
        Toolbar toolbar = findViewById(R.id.tb_customer);

        toolbar.setTitleTextColor(getColor(R.color.blue));
        setSupportActionBar(toolbar);

        db = FirebaseFirestore.getInstance();
        customerList = new ArrayList<>();

        buildDialog();
        buildRecyclerView();
        getCustomers();
    }

    private void getCustomers(){

        db.collection("users").get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot document : task.getResult()){
                            if(!document.contains("active") && !document.contains("admin")){
                                customer = document.toObject(Customer.class);
                                customerList.add(customer);
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
                    Toast.makeText(ViewCustomers.this, "Failed to load Couriers", Toast.LENGTH_SHORT).show();
                });
    }

    private void buildRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomerAdapter(customerList);

        adapter.setOnItemClickListener(position -> {

            String name = customerList.get(position).getFullName();
            String email = customerList.get(position).getEmail();
            long number = customerList.get(position).getPhone();

            String mNumber = String.valueOf(number);

            DialogFragment dialogFragment = AdminVendorDetailsDialog.newInstance(name, mNumber, email);
            dialogFragment.show(getSupportFragmentManager(), "customer detail");

        });
        recyclerView.setAdapter(adapter);
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
            Toast.makeText(ViewCustomers.this, s.getMessage(), Toast.LENGTH_SHORT).show();
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
}