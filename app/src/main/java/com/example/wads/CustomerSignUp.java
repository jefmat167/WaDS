package com.example.wads;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class CustomerSignUp extends AppCompatActivity {

    private EditText fName, email, phone, password;
    TextView tv_signin;
    private Button signup;
    private ProgressDialog dialog;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_sign_in);

        fName = findViewById(R.id.et_fullName);
        email = findViewById(R.id.et_email);
        phone = findViewById(R.id.et_phone);
        password = findViewById(R.id.et_password);

        tv_signin = findViewById(R.id.tv_signin);

        signup = findViewById(R.id.btn_signup);

        tv_signin.setOnClickListener(view -> {
            startActivity(new Intent(CustomerSignUp.this, SignIn.class));
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
            buildDialog();

            mAuth.createUserWithEmailAndPassword(_email, _password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            customer = new Customer(_fName, _email, phoneNumber, _password);
                            db.collection("users").document(customer.getEmail()).set(customer)
                                    .addOnSuccessListener(unused -> {
                                        if (dialog.isShowing()) {
                                            dialog.dismiss();
                                        }
//                                        subscribeUser();
//                                        Toast.makeText(CustomerSignUp.this, "Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(CustomerSignUp.this, SignIn.class);
                                        intent.putExtra("email", customer.getEmail());
                                        startActivity(intent);
                                    })
                                    .addOnFailureListener(e -> {
                                        if (dialog.isShowing()) {
                                            dialog.dismiss();
                                        }
                                        Toast.makeText(CustomerSignUp.this, "Failed", Toast.LENGTH_SHORT).show();
                                    });

                        }else{
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            Toast.makeText(CustomerSignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    });
        });
    }

//    private void subscribeUser() {
//        FirebaseMessaging.getInstance().subscribeToTopic("/topics/available")
//                        .addOnSuccessListener(unused -> Toast.makeText(CustomerSignUp.this, "Subscribed", Toast.LENGTH_SHORT).show()).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(CustomerSignUp.this, "Failed: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

    private void buildDialog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Signing in...");
        dialog.setCancelable(false);
        dialog.show();
    }
}