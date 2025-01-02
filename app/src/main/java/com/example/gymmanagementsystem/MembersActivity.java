package com.example.gymmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MembersActivity extends AppCompatActivity {
    Toolbar topAppBar;
    RecyclerView rvMemberCards;
    List<Member> memberList;
    MembersAdapter membersAdapter;
    String currentUserId;
    FirebaseAuth firebaseAuth;
    int receivedData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
//        Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();


//        Find Views By Id's
        topAppBar = findViewById(R.id.topAppBar);
        rvMemberCards = findViewById(R.id.rvMemberCards);

        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//      Show Activity on the basis of click event
        Intent intent = getIntent();
        receivedData = intent.getIntExtra("CARD_INDEX", 0);

        Toast.makeText(this, ""+receivedData, Toast.LENGTH_SHORT).show();

        if (receivedData == 1){
            topAppBar.setTitle("Total Members");
        } else if (receivedData == 2) {
            topAppBar.setTitle("Live Members");
        }else if (receivedData == 3) {
            topAppBar.setTitle("Expired Members");
        }else if (receivedData == 4) {
            topAppBar.setTitle("Due Amount Members");
        }

//        memberList = getMembersData();
//        membersAdapter = new MembersAdapter(memberList);
        memberList = new ArrayList<Member>();
        membersAdapter = new MembersAdapter(memberList);

        rvMemberCards.setLayoutManager(new LinearLayoutManager(MembersActivity.this, LinearLayoutManager.VERTICAL, false));
        rvMemberCards.setHasFixedSize(true);
        rvMemberCards.setAdapter(membersAdapter);

        getMembersData();

    }

    public void getMembersData(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(FirebaseHelper.APP_USERS)
                .child(currentUserId)
                .child(FirebaseHelper.MEMBERS);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        Date currentDate = new Date();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                memberList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Member member = data.getValue(Member.class);
                        if (member != null) {
                            String endDateStr = member.getEndDate();
                            switch (receivedData){
                                case 1:
                                    memberList.add(member);
                                    break;

                                case 2:
                                    try {
                                        Date endDate = sdf.parse(endDateStr);
                                        if (currentDate.before(endDate)) {
                                            memberList.add(member);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;

                                case 3:
                                    try {
                                        Date endDate = sdf.parse(endDateStr);
                                        if (endDate.before(currentDate)) {
                                            memberList.add(member);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;

                                case 4:
                                    if(member.getPaidAmount() < member.getPlanAmount()){
                                        memberList.add(member);
                                    }
                                    break;



                            }
                        }
                    }
                }
                membersAdapter.notifyDataSetChanged();// Notify adapter about data changes
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("FirebaseError", error.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMembersData();
        membersAdapter = new MembersAdapter(memberList);
        rvMemberCards.setAdapter(membersAdapter);


    }
}