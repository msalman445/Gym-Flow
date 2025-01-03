package com.example.gymmanagementsystem;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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
import java.util.Calendar;
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


        if (receivedData == 1){
            topAppBar.setTitle("Total Members");
        } else if (receivedData == 2) {
            topAppBar.setTitle("Live Members");
        }else if (receivedData == 3) {
            topAppBar.setTitle("Expired Members");
        }else if (receivedData == 4) {
            topAppBar.setTitle("Expiring (1-3 days)");
        }
        else if (receivedData == 5) {
            topAppBar.setTitle("Expiring (4-7 days)");
        }else if (receivedData == 6) {
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


        profileClickListener();
        deleteClickListener();
        editClickListener();

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
                            member.setMemberId(data.getKey());
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
                                        Calendar tomorrowCal = (Calendar) currentCal.clone();
                                        tomorrowCal.add(Calendar.DAY_OF_YEAR, 1);

                                        Calendar dayAfterTomorrowCal = (Calendar) currentCal.clone();
                                        dayAfterTomorrowCal.add(Calendar.DAY_OF_YEAR, 3);

                                        // Check if the endDate is in the range of tomorrow to day after tomorrow
                                        Calendar endCal = Calendar.getInstance();
                                        endCal.setTime(endDate);
                                        endCal.set(Calendar.HOUR_OF_DAY, 0);
                                        endCal.set(Calendar.MINUTE, 0);
                                        endCal.set(Calendar.SECOND, 0);
                                        endCal.set(Calendar.MILLISECOND, 0);

                                        if (!endCal.before(tomorrowCal) && !endCal.after(dayAfterTomorrowCal)) {
                                            memberList.add(member); // Add member to the list
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;

                                case 5:
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
                                            memberList.add(member); // Add member to the list
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;



                                case 6:
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
        profileClickListener();
        editClickListener();
        deleteClickListener();



    }

    private void profileClickListener(){
        membersAdapter.setIOnProfileClickListener(new MembersAdapter.IOnProfileClickListener() {
            @Override
            public void onProfileButtonClick(MembersAdapter.MemberViewHolder holder, int position, ImageButton ibProfileButton) {
                Member member = memberList.get(position);
                Intent intent = new Intent(MembersActivity.this, MemberProfileActivity.class);
                intent.putExtra("MEMBER_ID", member.getMemberId());
                startActivity(intent);

            }
        });
    }

   private void editClickListener(){
        membersAdapter.setIOnEditClickListener(new MembersAdapter.IOnEditClickListener() {
            @Override
            public void onEditButtonClick(MembersAdapter.MemberViewHolder holder, int position, ImageButton ibProfileButton) {

                Member member = memberList.get(position);
                Intent intent = new Intent(MembersActivity.this, AddMemberActivity.class);
                intent.putExtra("MEMBER_ID", member.getMemberId());
                startActivity(intent);

            }
        });
   }

    private void deleteClickListener(){

        membersAdapter.setIOnDeleteClickListener(new MembersAdapter.IOnDeleteClickListener() {
            @Override
            public void onDeleteButtonClick(MembersAdapter.MemberViewHolder holder, int position, ImageButton ibProfileButton) {
                Member member = memberList.get(position);
                new AlertDialog.Builder(MembersActivity.this).setIcon(R.drawable.delete).setTitle("Delete").setMessage("Do you want to delete this member?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteFunctionality(member.getMemberId());
                        dialog.dismiss();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(false).create().show();
            }
        });
    }

    private void deleteFunctionality(String memberId){
        DatabaseReference memberRef = FirebaseDatabase.getInstance()
                .getReference()
                .child(FirebaseHelper.APP_USERS)
                .child(currentUserId) // Replace with your current user's ID
                .child(FirebaseHelper.MEMBERS)
                .child(memberId);

        // Perform the delete operation
        memberRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Member deleted successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to delete member: " + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}