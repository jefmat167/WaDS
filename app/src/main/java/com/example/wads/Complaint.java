package com.example.wads;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

public class Complaint extends AppCompatActivity {
    private Button reportBtn, cancel;
    private EditText reportText;

    private ProgressDialog dialog;

    private FirebaseFirestore db;
    private ComplaintModel reportModel;

    private String vendorEmail;
    private String customerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        reportBtn = findViewById(R.id.report_btn);
        cancel = findViewById(R.id.re_cancel);
        reportText = findViewById(R.id.report_text);

        db = FirebaseFirestore.getInstance();

        getComplaintDetails();

        reportBtn.setOnClickListener(view -> {
            String mText = reportText.getText().toString();
            if (mText.isEmpty()){
                reportText.setError("Please type in your complaints");
                reportText.requestFocus();
                return;
            }


            reportModel = new ComplaintModel(mText, customerName, vendorEmail);
            buildDialog();

            db.collection("complaints").add(reportModel)
                    .addOnCompleteListener(task -> {
                        dialog.dismiss();
                        reportText.setText("");
                        Toast.makeText(Complaint.this, "Complaint sent successfully.", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        dialog.dismiss();
                        Toast.makeText(Complaint.this, "Failed to send complaint.", Toast.LENGTH_SHORT).show();
                    });
        });

        cancel.setOnClickListener(view -> {
            startActivity(new Intent(Complaint.this, CustomerDashboard.class));
            finish();
        });

    }

    private void getComplaintDetails() {
        Intent intent = getIntent();
        vendorEmail = intent.getStringExtra("email");
        customerName = intent.getStringExtra("name");
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
        startActivity(new Intent(Complaint.this, CustomerDashboard.class));
        finish();
    }

}