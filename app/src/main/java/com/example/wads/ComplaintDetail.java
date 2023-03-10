package com.example.wads;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ComplaintDetail extends AppCompatActivity implements ComplaintDecisionDialog.ComplaintDecisionDialogListener{

    private Button ignore, react;
    private TextView de_from, de_about, de_content;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    String from, about, content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_detail);

        initViews();
        initViewsWithIntent();

        ignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ComplaintDetail.this, Complaints.class));
            }
        });

        react.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showComplaintDecisionDialog();
            }
        });
    }

    private void initViewsWithIntent() {
        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        about = intent.getStringExtra("about");
        content = intent.getStringExtra("content");

        de_from.setText(from);
        de_about.setText(about);
        de_content.append("\n" + content);
    }

    private void initViews() {
        ignore = findViewById(R.id.ignore);
        react = findViewById(R.id.react);
        de_from = findViewById(R.id.de_from);
        de_about = findViewById(R.id.de_about);
        de_content = findViewById(R.id.content);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    private void showComplaintDecisionDialog() {
        DialogFragment dialog = new ComplaintDecisionDialog();
        dialog.show(getSupportFragmentManager(), "sanction");
    }

    @Override
    public void onPositiveClick(String sanction) {
        switch (sanction){
            case "Email":
                String subject = "";
                String body = "";

                Intent mailIntent = new Intent(Intent.ACTION_SEND);
                mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{about});
                mailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                mailIntent.putExtra(Intent.EXTRA_TEXT, body);

                mailIntent.setType("message/rfc822");
                startActivity(Intent.createChooser(mailIntent, "Choose an email client: "));

                break;
            case "Suspend":
                db.collection("users").document(about)
                        .update("suspended", true)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(ComplaintDetail.this, "Account suspended", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ComplaintDetail.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
        }
    }

    @Override
    public void onNegativeClick() {

    }
}