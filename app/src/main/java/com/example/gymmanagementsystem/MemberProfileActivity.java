package com.example.gymmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MemberProfileActivity extends AppCompatActivity {
    TextView tvName, tvPlanName, tvPhoneNumber, tvAddress, tvGender, tvPlanAmount, tvPaidAmount, tvStartDate, tvEndDate;
    Toolbar appbar;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String currentUserId, memberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_profile);

//        Firebase initialization
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();


        tvName = findViewById(R.id.tvName);
        tvPlanName = findViewById(R.id.tvPlanName);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvAddress = findViewById(R.id.tvAddress);
        tvGender = findViewById(R.id.tvGender);
        tvPlanAmount = findViewById(R.id.tvPlanAmount);
        tvPaidAmount = findViewById(R.id.tvPaidAmount);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        appbar = findViewById(R.id.topAppBar);

        appbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        memberId = intent.getStringExtra("MEMBER_ID");
        fetchData();
    }

    private void fetchData(){
        databaseReference = FirebaseDatabase.getInstance().getReference().child(FirebaseHelper.APP_USERS).child(currentUserId).child(FirebaseHelper.MEMBERS).child(memberId);
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful() && task.getResult().exists()){
                    DataSnapshot snapshot = task.getResult();

                    // Extract data
                    String name = snapshot.child("name").getValue(String.class);
                    String phoneNumber = snapshot.child("phoneNumber").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);
                    String gender = snapshot.child("gender").getValue(String.class);
                    String planName = snapshot.child("planName").getValue(String.class);
                    double planAmount = snapshot.child("planAmount").getValue(Double.class);
                    double paidAmount = snapshot.child("paidAmount").getValue(Double.class);
                    String startDate = snapshot.child("startDate").getValue(String.class);
                    String endDate = snapshot.child("endDate").getValue(String.class);

                    // Set values to TextViews
                    tvName.setText(name);
                    tvPlanName.setText(planName);
                    tvPhoneNumber.setText(phoneNumber);
                    tvAddress.setText(address);
                    tvGender.setText(gender);
                    tvPlanAmount.setText(String.valueOf(planAmount));
                    tvPaidAmount.setText(String.valueOf(paidAmount));
                    tvStartDate.setText(startDate);
                    tvEndDate.setText(endDate);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchData();

    }
}