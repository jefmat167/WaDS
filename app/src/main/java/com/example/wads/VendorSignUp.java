package com.example.wads;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class VendorSignUp extends AppCompatActivity {
    private EditText fName, email, phone, password;
    private TextView tv_signin;
    private Button signup;
    private ProgressBar bar;

    private Vendor vendor;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_sign_up);

        initViews();

        tv_signin.setOnClickListener(view -> {
            startActivity(new Intent(VendorSignUp.this, SignIn.class));
            finish();
        });

        signup.setOnClickListener(view -> {

            String _fName = fName.getText().toString();
            String _email = email.getText().toString();
            String _phone = phone.getText().toString();
            String _password = password.getText().toString();

            if (_fName.isEmpty()){
                fName.setError("Please provide your name");
                fName.requestFocus();
                return;
            }
            if (_email.isEmpty()){
                email.setError("Please provide your email address");
                email.requestFocus();
                return;
            }
            if (_phone.isEmpty()){
                phone.setError("Please provide your phone number");
                phone.requestFocus();
                return;
            }

            if (_password.isEmpty()){
                password.setError("Please set a password");
                password.requestFocus();
                return;
            }

            long phoneNumber = Long.parseLong(_phone);
            boolean isActive = false;
            boolean suspended = false;

            bar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(_email, _password)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            vendor = new Vendor(_fName, _email, phoneNumber, _password, isActive, suspended);
                            db.collection("users").document(vendor.getEmail()).set(vendor)
                                    .addOnSuccessListener(unused -> {
                                        bar.setVisibility(View.GONE);
                                        Toast.makeText(VendorSignUp.this, "Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(VendorSignUp.this, SignIn.class);
                                        intent.putExtra("email", vendor.getEmail());
                                        startActivity(intent);
                                    })
                                    .addOnFailureListener(e -> {
                                        bar.setVisibility(View.GONE);
                                        Toast.makeText(VendorSignUp.this, "Failed", Toast.LENGTH_SHORT).show();
                                    });

                        }else{
                            bar.setVisibility(View.GONE);
                            Log.d("", task.getException().getMessage(), task.getException());
                            Toast.makeText(VendorSignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void initViews() {
        fName = findViewById(R.id.cr_fullname);
        email = findViewById(R.id.cr_email);
        phone = findViewById(R.id.cr_phone);
        password = findViewById(R.id.cr_password);

        tv_signin = findViewById(R.id.tv_signin);
        bar = findViewById(R.id.pg_bar);
        signup = findViewById(R.id.cr_signup);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(VendorSignUp.this, Index.class));
        finish();
    }
}