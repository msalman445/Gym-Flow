package com.example.gymmanagementsystem;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class TodaysCollectionActivity extends AppCompatActivity {
    TextView tvCollectionAmount;
    Toolbar topAppBar;

    FirebaseAuth firebaseAuth;
    String currentUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_collection);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();


        tvCollectionAmount = findViewById(R.id.tvCollectionAmount);
        topAppBar = findViewById(R.id.topAppBar);

        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fetchTodayCollection();
    }

    public void fetchTodayCollection() {
        // Firebase reference to "users/{userId}/members"
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference(FirebaseHelper.APP_USERS)
                .child(currentUser)
                .child(FirebaseHelper.MEMBERS);

        // Get today's date in "dd/MM/yy" format
        String todayDate = new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(new Date());

        databaseReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                double totalCollection = 0;
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    String startDate = snapshot.child("startDate").getValue(String.class);
                    Integer paidAmount = snapshot.child("paidAmount").getValue(Integer.class);

                    if (startDate != null && startDate.equals(todayDate) && paidAmount != null) {
                        totalCollection += paidAmount;
                    }
                }
                // Display the total collection (e.g., set it in a TextView)
                tvCollectionAmount.setText(String.valueOf(totalCollection));
            } else {
                System.err.println("Error fetching data: " + task.getException());
            }
        });
    }
}