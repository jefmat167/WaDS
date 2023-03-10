package com.example.wads;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class Complaints extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ComplaintAdapter adapter;
    private ComplaintModel complaintModel;
    private ArrayList<ComplaintModel> complaintModels;

    private FirebaseFirestore db;

    private ProgressDialog dialog;
    private TextView cp_placeholder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints);

        cp_placeholder = findViewById(R.id.cp_placeholder);
        cp_placeholder.setVisibility(TextView.GONE);
        recyclerView = findViewById(R.id.cp_recyclerview);
        complaintModels = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        buildDialog();
        buildRecyclerView();
        getComplaints();

    }

    private void buildRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ComplaintAdapter(complaintModels);

        adapter.setOnItemClickListener(position -> {
            String from = complaintModels.get(position).getFromName();
            String about = complaintModels.get(position).getAboutEmail();
            String content = complaintModels.get(position).getContent();

            Intent intent = new Intent(Complaints.this, ComplaintDetail.class);
            intent.putExtra("from", from);
            intent.putExtra("about", about);
            intent.putExtra("content", content);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    private void getComplaints(){
        db.collection("complaints").get()
                .addOnCompleteListener(task -> {
                                        if(task.isSuccessful()){
                                            if (task.getResult().isEmpty()){
                                                cp_placeholder.setText("No complaints at this moment");
                                                cp_placeholder.setVisibility(TextView.VISIBLE);
                                                dialog.dismiss();
                                            }else{
                                                for(QueryDocumentSnapshot document : task.getResult()){
                                                    complaintModel = document.toObject(ComplaintModel.class);
                                                    complaintModels.add(complaintModel);
                                                    adapter.notifyDataSetChanged();
                                                    dialog.dismiss();
                                                }
                                            }
                                        }
                                    })
                .addOnFailureListener(e -> {
                    dialog.dismiss();
                    Toast.makeText(Complaints.this, "Failed to get complaints", Toast.LENGTH_SHORT).show();
                });
    }

    private void buildDialog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.show();
    }
}