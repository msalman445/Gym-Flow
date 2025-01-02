package com.example.gymmanagementsystem;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    Toolbar topAppBar;
    View headerView;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    RecyclerView rvMainCards;
    TextView tvHeaderName, tvHeaderEmail;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    String currentUserId;
    long totalMembers = 0;
    List<MainCard> mainCardsList;
    MainCardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //        Firebase initialization
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        currentUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

//        Find Views By Id's
        topAppBar = findViewById(R.id.topAppBar);
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
        rvMainCards = findViewById(R.id.rvMainCards);

        headerView = navigationView.getHeaderView(0);

        tvHeaderName = headerView.findViewById(R.id.tvHeaderName);
        tvHeaderEmail = headerView.findViewById(R.id.tvHeaderEmail);

//        tvHeaderName.setText(firebaseAuth.getCurrentUser().get);
        tvHeaderEmail.setText(firebaseAuth.getCurrentUser().getEmail());




//        Set Action Bar
        setSupportActionBar(topAppBar);

//        Open Navigation View
       topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               drawerLayout.open();
           }
       });

//        Set Navigation Views
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.itmLogout){
                    firebaseAuth.signOut();
                    Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                }
                return true;
            }
        });

//       Main RecyclerView
//        int spanCount = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4:3;
        mainCardsList = getData();
        adapter = new MainCardAdapter(mainCardsList, MainActivity.this);
        rvMainCards.setAdapter(adapter);
        rvMainCards.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        rvMainCards.setHasFixedSize(true);
        rvMainCards.addItemDecoration(new GridSpacingItemDecoration(3, 22));

        adapter.setIOnMainCardClickListener(new MainCardAdapter.IOnMainCardClickListener() {
            @Override
            public void onMainCardClick(MainCardAdapter.MainCardViewHolder holder, int position) {
                Intent intent;
                Toast.makeText(MainActivity.this, "position" + position, Toast.LENGTH_SHORT).show();
                switch (position){
                    case 0:
                        intent = new Intent(MainActivity.this, AddMemberActivity.class);
                        break;
                    case 1:
                        intent = new Intent(MainActivity.this, MembersActivity.class);
                        intent.putExtra("CARD_INDEX", 1);
                        break;
                    case 2:
                        intent = new Intent(MainActivity.this, MembersActivity.class);
                        intent.putExtra("CARD_INDEX", 2);
                        break;
                    case 3:
                        intent = new Intent(MainActivity.this, MembersActivity.class);
                        intent.putExtra("CARD_INDEX", 3);
                        break;
                    case 4:
                        intent = new Intent(MainActivity.this, MembersActivity.class);
                        intent.putExtra("CARD_INDEX", 4);
                        break;
                    default:
                        intent = new Intent(MainActivity.this, MembersActivity.class);

                }
                startActivity(intent);

            }
        });







    }


//  Main RecyclerView Data
    private List<MainCard> getData(){
        List<MainCard> cards = new ArrayList<>();
        cards.add(new MainCard(R.drawable.group, "Add Members", 0));
        cards.add(new MainCard(R.drawable.group, "Total Members", 0));
        cards.add(new MainCard(R.drawable.group, "Live Members", 0));
        cards.add(new MainCard(R.drawable.group, "Expired Members", 0));
        cards.add(new MainCard(R.drawable.group, "Due Amount Members", 0));
        cards.add(new MainCard(R.drawable.group, "Today's Collection", 0));
        cards.add(new MainCard(R.drawable.group, "Mark Attendance", 0));
        cards.add(new MainCard(R.drawable.group, "Attendance Report", 0));

        return cards;
    }





    private void updateCardMemberCount(String userId, String cardTitle, List<MainCard> cards, RecyclerView.Adapter adapter) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("users").child(userId).child("members")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long count = 0;

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
                        Date currentDate = new Date();


                        // Calculate based on the card title
                        if (cardTitle.equals("Total Members")) {
                            count = snapshot.getChildrenCount();
                        }
                        else if (cardTitle.equals("Live Members")) {
                            for (DataSnapshot memberSnapshot : snapshot.getChildren()) {
                                String endDateStr = memberSnapshot.child("endDate").getValue(String.class);
                                try {
                                    Date endDate = sdf.parse(endDateStr);
                                    if (currentDate.before(endDate)) {
                                        count++;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else if (cardTitle.equals("Expired Members")) {
                            for (DataSnapshot memberSnapshot : snapshot.getChildren()) {
                                String endDateStr = memberSnapshot.child("endDate").getValue(String.class);
                                try {
                                    Date endDate = sdf.parse(endDateStr);
                                    if (endDate.before(currentDate)) {
                                        count++;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        else if (cardTitle.equals("Due Amount Members")) {
                            for (DataSnapshot memberSnapshot : snapshot.getChildren()) {
                                // Get the paid amount and plan amount from Firebase
                                Double paidAmount = memberSnapshot.child("paidAmount").getValue(Double.class);
                                Double planAmount = memberSnapshot.child("planAmount").getValue(Double.class);

                                // Check if paidAmount is less than planAmount
                                if (paidAmount != null && planAmount != null && paidAmount < planAmount) {
                                    count++;
                                }
                            }
                        } else if (cardTitle.equals("Today's Collection")) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            String currentDateStr = simpleDateFormat.format(new Date());  // Get today's date as a string

                            for (DataSnapshot memberSnapshot : snapshot.getChildren()) {
                                String startDateStr = memberSnapshot.child("startDate").getValue(String.class);

                                // Check if the startDate is today's date
                                if (startDateStr != null && startDateStr.equals(currentDateStr)) {
                                    count++;
                                }
                            }
                        }



                        // Update the relevant card
                        for (MainCard card : cards) {
                            if (card.getTitle().equals(cardTitle)) {
                                card.setMembers(count);
                                break;
                            }
                        }

                        // Notify the adapter of the change
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FirebaseError", error.getMessage());
                    }
                });
    }




    @Override
    protected void onResume() {
        updateCardMemberCount(currentUserId, "Total Members", mainCardsList, adapter);
        updateCardMemberCount(currentUserId, "Live Members", mainCardsList, adapter);
        updateCardMemberCount(currentUserId, "Expired Members", mainCardsList, adapter);
        updateCardMemberCount(currentUserId, "Expiring (1-3 Days)", mainCardsList, adapter);
        updateCardMemberCount(currentUserId, "Due Amount Members", mainCardsList, adapter);
        updateCardMemberCount(currentUserId, "Today's Collection", mainCardsList, adapter);
        super.onResume();
    }
}