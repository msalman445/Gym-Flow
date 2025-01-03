package com.example.gymmanagementsystem;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    Toolbar topAppBar;
    View headerView;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    RecyclerView rvMainCards;
    TextView tvHeaderEmail;
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

//        tvHeaderName = headerView.findViewById(R.id.tvHeaderName);
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
                    Toast.makeText(MainActivity.this, "Log Out", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.itmShare) {
                    String appLink = "https://play.google.com/store/apps/details?id=" + getPackageName();

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this amazing app!");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey! Download this app: " + appLink);

                    startActivity(Intent.createChooser(shareIntent, "Share App via"));
                }
                return true;
            }
        });

//       Main RecyclerView
        int spanCount = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 5:3;
        mainCardsList = getData();
        adapter = new MainCardAdapter(mainCardsList, MainActivity.this);
        rvMainCards.setAdapter(adapter);
        rvMainCards.setLayoutManager(new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL));
        rvMainCards.setHasFixedSize(true);
        rvMainCards.addItemDecoration(new GridSpacingItemDecoration(spanCount, 22));

        adapter.setIOnMainCardClickListener(new MainCardAdapter.IOnMainCardClickListener() {
            @Override
            public void onMainCardClick(MainCardAdapter.MainCardViewHolder holder, int position) {
                Intent intent;
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
                    case 5:
                        intent = new Intent(MainActivity.this, MembersActivity.class);
                        intent.putExtra("CARD_INDEX", 5);
                        break;
                    case 6:
                        intent = new Intent(MainActivity.this, MembersActivity.class);
                        intent.putExtra("CARD_INDEX", 6);
                        break;

                    case 7:
                        intent = new Intent(MainActivity.this, TodaysCollectionActivity.class);
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
        cards.add(new MainCard(R.drawable.add_user, "Add Members", 0));
        cards.add(new MainCard(R.drawable.group, "Total Members", 0));
        cards.add(new MainCard(R.drawable.live, "Live Members", 0));
        cards.add(new MainCard(R.drawable.expired, "Expired Members", 0));
        cards.add(new MainCard(R.drawable.expiring, "Expiring (1-3 days)", 0));
        cards.add(new MainCard(R.drawable.expiring, "Expiring (4-7 days)", 0));
        cards.add(new MainCard(R.drawable.due_amount, "Due Amount Members", 0));
        cards.add(new MainCard(R.drawable.money, "Today's Collection", 0));


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
                        else if (cardTitle.equals("Expiring (1-3 days)")) {
                            for (DataSnapshot memberSnapshot : snapshot.getChildren()) {
                                String endDateStr = memberSnapshot.child("endDate").getValue(String.class);
                                try {
                                    Date endDate = sdf.parse(endDateStr);

                                    // Normalize current date
                                    Calendar currentCal = Calendar.getInstance();
                                    currentCal.setTime(currentDate);
                                    currentCal.set(Calendar.HOUR_OF_DAY, 0);
                                    currentCal.set(Calendar.MINUTE, 0);
                                    currentCal.set(Calendar.SECOND, 0);
                                    currentCal.set(Calendar.MILLISECOND, 0);

                                    // Create range for 1 to 3 days from now
                                    Calendar dayAfterTomorrowCal = (Calendar) currentCal.clone();
                                    dayAfterTomorrowCal.add(Calendar.DAY_OF_YEAR, 3);

                                    Calendar tomorrowCal = (Calendar) currentCal.clone();
                                    tomorrowCal.add(Calendar.DAY_OF_YEAR, 1);

                                    // Check if the endDate is in the range of tomorrow to day after tomorrow
                                    Calendar endCal = Calendar.getInstance();
                                    endCal.setTime(endDate);
                                    endCal.set(Calendar.HOUR_OF_DAY, 0);
                                    endCal.set(Calendar.MINUTE, 0);
                                    endCal.set(Calendar.SECOND, 0);
                                    endCal.set(Calendar.MILLISECOND, 0);

                                    if (!endCal.before(tomorrowCal) && !endCal.after(dayAfterTomorrowCal)) {
                                        count++;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else if (cardTitle.equals("Expiring (4-7 days)")) {
                            for (DataSnapshot memberSnapshot : snapshot.getChildren()) {
                                String endDateStr = memberSnapshot.child("endDate").getValue(String.class);
                                try {
                                    Date endDate = sdf.parse(endDateStr);

                                    // Normalize current date
                                    Calendar currentCal = Calendar.getInstance();
                                    currentCal.setTime(currentDate);
                                    currentCal.set(Calendar.HOUR_OF_DAY, 0);
                                    currentCal.set(Calendar.MINUTE, 0);
                                    currentCal.set(Calendar.SECOND, 0);
                                    currentCal.set(Calendar.MILLISECOND, 0);

                                    // Create range for 4 to 7 days from now
                                    Calendar fourDaysFromNowCal = (Calendar) currentCal.clone();
                                    fourDaysFromNowCal.add(Calendar.DAY_OF_YEAR, 4);

                                    Calendar sevenDaysFromNowCal = (Calendar) currentCal.clone();
                                    sevenDaysFromNowCal.add(Calendar.DAY_OF_YEAR, 7);

                                    // Check if the endDate is in the range of 4 to 7 days
                                    Calendar endCal = Calendar.getInstance();
                                    endCal.setTime(endDate);
                                    endCal.set(Calendar.HOUR_OF_DAY, 0);
                                    endCal.set(Calendar.MINUTE, 0);
                                    endCal.set(Calendar.SECOND, 0);
                                    endCal.set(Calendar.MILLISECOND, 0);

                                    if (!endCal.before(fourDaysFromNowCal) && !endCal.after(sevenDaysFromNowCal)) {
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
        updateCardMemberCount(currentUserId, "Expiring (1-3 days)", mainCardsList, adapter);
        updateCardMemberCount(currentUserId, "Expiring (4-7 days)", mainCardsList, adapter);
        super.onResume();
    }
}