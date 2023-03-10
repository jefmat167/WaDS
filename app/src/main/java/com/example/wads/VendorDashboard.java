package com.example.wads;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@SuppressLint("UseSwitchCompatOrMaterialCode")
public class VendorDashboard extends AppCompatActivity {

    private TextView status, cName;
    private Switch statusSwitch;

    private FirebaseFirestore db;
    private FirebaseUser user;
    private String userEmail;
    private Vendor vendor;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_dashboard);

        status = findViewById(R.id.status);
        cName = findViewById(R.id.cName);
        statusSwitch = findViewById(R.id.statusSwitch);

        Button btn_logout = findViewById(R.id.logout);
        Button btn_changePassword = findViewById(R.id.change_password);
        Button btn_orders = findViewById(R.id.view_order);

        btn_changePassword.setOnClickListener(view -> startActivity(new Intent(VendorDashboard.this, ChangePassword.class)));

        btn_logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(VendorDashboard.this, SignIn.class));
            finish();
        });

        btn_orders.setOnClickListener(view -> startActivity(new Intent(VendorDashboard.this, Orders.class)));

        statusSwitch.setChecked(false);

        if (statusSwitch.isChecked()) {
            status.setText("Available");
            status.setTextColor(Color.parseColor("#00FF00"));
        } else {
            status.setText("Unavailable");
            status.setTextColor(Color.parseColor("#FF0000"));
        }

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userEmail = user.getEmail();

        getUserDetails();

        statusSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                available();
            } else
                unavailable();
        });
    }

    @SuppressLint("SetTextI18n")
    private void unavailable() {
        db.collection("users").document(userEmail)
                .update("active", false)
                .addOnSuccessListener(unused -> {
                    status.setText("Unavailable");
                    status.setTextColor(Color.parseColor("#FF0000"));
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(VendorDashboard.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    statusSwitch.setChecked(false);
                });
    }

    @SuppressLint("SetTextI18n")
    private void available() {
        vendor.setActive(true);
        db.collection("users").document(userEmail)
                .update("active", vendor.isActive())
                .addOnSuccessListener(unused -> {
                    status.setText("Available");
                    status.setTextColor(Color.parseColor("#00FF00"));

//                    String topic = "/topics/available";
//                    String title = "Availability";
//                    String message = cName + " from WaDS is available, you might want to order for water.";
//
//                    JSONObject notification = new JSONObject();
//                    JSONObject notificationBody = new JSONObject();
//                    try {
//                        notificationBody.put("title", title);
//                        notificationBody.put("message", message);
//
//                        notification.put("to", topic);
//                        notification.put("data", notificationBody);
//                    } catch (JSONException e) {
//                        Toast.makeText(VendorDashboard.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                    sendNotification(notification);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(VendorDashboard.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    statusSwitch.setChecked(false);
                });
    }

    private void getUserDetails() {
        db.collection("users").document(userEmail).get()
                .addOnSuccessListener(documentSnapshot -> {
                    vendor = documentSnapshot.toObject(Vendor.class);
                    cName.setText(vendor.getFullName());
                })
                .addOnFailureListener(e -> Log.d("", e.getMessage(), e));
    }

    private void sendNotification(JSONObject notification) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyUtils.FCM_API, notification, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(VendorDashboard.this, "Your Available", Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(VendorDashboard.this, "Failed: " + error,Toast.LENGTH_SHORT).show()){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", MyUtils.FCM_SERVER_KEY);
                params.put("Content-Type", MyUtils.CONTENT_TYPE);

                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
}