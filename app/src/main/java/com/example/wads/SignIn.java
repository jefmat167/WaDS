package com.example.wads;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.flutterwave.raveandroid.RavePayActivity;
import com.flutterwave.raveandroid.RaveUiManager;
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class SignIn extends AppCompatActivity implements AccountDecisionDialog.AccountDecisionDialogListener {
    private EditText email, password;

    private ProgressDialog dialog;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private final Activity activity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        email = findViewById(R.id.si_email);
        password = findViewById(R.id.si_password);
        TextView tv_signin = findViewById(R.id.tv_signup);

        tv_signin.setOnClickListener(view -> {
            DialogFragment dialogFragment = new AccountDecisionDialog();
            dialogFragment.show(getSupportFragmentManager(), "AccountDecisionFragment");
        });

        Button signin = findViewById(R.id.si_btn);

        String mail = getIntent().getStringExtra("email");
        email.setText(mail);

        signin.setOnClickListener(view -> {

            String _email = email.getText().toString().trim();
            String _password = password.getText().toString().trim();

            if (_email.isEmpty()) {
                email.setError("please provide your email");
                email.requestFocus();
                return;
            }

            if (_password.isEmpty()) {
                password.setError("please provide your password");
                password.requestFocus();
                return;
            }

            buildDialog();

            mAuth.signInWithEmailAndPassword(_email, _password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            db.collection("users").document(_email).get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            DocumentSnapshot document = task1.getResult();
                                            if (document.contains("active")) {
                                                dialog.dismiss();
                                                startActivity(new Intent(SignIn.this, VendorDashboard.class));
                                                finish();
                                            }else if(document.contains("admin")) {
                                                dialog.dismiss();
                                                startActivity(new Intent(SignIn.this, AdminDashboard.class));
                                                finish();
                                            }
                                            else {
                                                Customer customer = document.toObject(Customer.class);
                                                String fullName = null;
                                                if (customer != null) {
                                                    fullName = customer.getFullName();
                                                }
                                                Intent intent = new Intent(SignIn.this, CustomerDashboard.class);
                                                intent.putExtra("name", fullName);

                                                dialog.dismiss();
                                                startActivity(intent);
                                                finish();
                                            }
                                        } else {
                                            dialog.dismiss();
                                            Toast.makeText(SignIn.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        }

                                    });
                        } else {
                            dialog.dismiss();
                            Toast.makeText(SignIn.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void buildDialog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Signing in...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onDialogPositiveClick(String option) {
        switch (option){
            case "Vendor":
                UUID uuid = UUID.randomUUID();
                new RaveUiManager(activity)
                        .setAmount(MyUtils.AMOUNT)
                        .setCurrency(MyUtils.CURRENCY)
                        .setEmail("")
                        .setfName("")
                        .setlName("")
                        .setNarration("Vendor's account payment.")
                        .setPublicKey(MyUtils.FWAVE_PUBLIC_KEY)
                        .setEncryptionKey(MyUtils.FWAVE_ENCRYPTION_KEY)
                        .setTxRef(uuid.toString())
                        .acceptAccountPayments(true)
                        .acceptCardPayments(true)
                        .acceptMpesaPayments(true)
                        .acceptAchPayments(true)
                        .acceptGHMobileMoneyPayments(true)
                        .acceptUgMobileMoneyPayments(false)
                        .acceptZmMobileMoneyPayments(false)
                        .acceptRwfMobileMoneyPayments(false)
                        .acceptSaBankPayments(false)
                        .acceptUkPayments(false)
                        .acceptBankTransferPayments(true)
                        .acceptUssdPayments(true)
                        .acceptBarterPayments(false)
                        .allowSaveCardFeature(true)
                        .onStagingEnv(true)
                        .showStagingLabel(true)
                        .initialize();
                break;

            case "Customer":
                startActivity(new Intent(SignIn.this, CustomerSignUp.class));
                break;
        }
    }

    @Override
    public void onDialogNegativeClick() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SignIn.this, Index.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            String message = data.getStringExtra("response");
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                Toast.makeText(this, "Payment Successful " + message, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignIn.this, VendorSignUp.class);
                startActivity(intent);
            }
            else if (resultCode == RavePayActivity.RESULT_ERROR) {
                Toast.makeText(this, "ERROR " + message, Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
                Toast.makeText(this, "CANCELLED " + message, Toast.LENGTH_SHORT).show();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}