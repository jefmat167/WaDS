package com.example.wads;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText np, cp;
    private Button change;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        toolbar = findViewById(R.id.cp_toolbar);
        toolbar.setTitleTextColor(getColor(R.color.blue));
        setSupportActionBar(toolbar);

        np = findViewById(R.id.new_password);
        cp = findViewById(R.id.confirm_password);
        change = findViewById(R.id.btn_change);

        mAuth = FirebaseAuth.getInstance();

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPass = np.getText().toString().trim();
                String confirmPass = cp.getText().toString().trim();

                if(newPass.isEmpty()){
                    np.setError("Please set a new password");
                    np.requestFocus();
                    return;
                }
                if (confirmPass.isEmpty()){
                    cp.setError("Please confirm you password");
                    cp.requestFocus();
                    return;
                }

                if (!confirmPass.equals(newPass)){
                    Toast.makeText(ChangePassword.this, "Passwords don't match", Toast.LENGTH_LONG).show();
                    cp.setTextColor(getResources().getColor(R.color.red, null));
                    cp.requestFocus();
                    return;
                }

                FirebaseUser user = mAuth.getCurrentUser();
                user.updatePassword(newPass).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Password change successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ChangePassword.this, VendorDashboard.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Password change failed", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


    }
}