package com.example.wads;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.flutterwave.raveandroid.RavePayActivity;
import com.flutterwave.raveandroid.RaveUiManager;
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants;

import java.util.UUID;

public class Index extends AppCompatActivity implements AccountDecisionDialog.AccountDecisionDialogListener{

    private final Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        Button signup = findViewById(R.id.sign_up);
        Button sign_in = findViewById(R.id.sign_in);

        signup.setOnClickListener(view -> showAccountDecisionDialog());

        sign_in.setOnClickListener(view -> {
            Intent intent = new Intent(Index.this, SignIn.class);
            startActivity(intent);
            finish();
        });
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
                startActivity(new Intent(Index.this, CustomerSignUp.class));
                break;
        }
    }

    @Override
    public void onDialogNegativeClick() {

    }

    public void showAccountDecisionDialog() {
        DialogFragment dialog = new AccountDecisionDialog();
        dialog.show(getSupportFragmentManager(), "AccountDecisionFragment");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            String message = data.getStringExtra("response");
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                Toast.makeText(this, "Payment Successful " + message, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Index.this, VendorSignUp.class);
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