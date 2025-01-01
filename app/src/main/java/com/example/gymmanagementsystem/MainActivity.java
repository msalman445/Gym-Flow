package com.example.gymmanagementsystem;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Toolbar topAppBar;
    View headerView;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    RecyclerView rvMainCards;
    Button btnClick;
    TextView tvHeaderName, tvHeaderEmail;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //        Firebase initialization
        firebaseAuth = FirebaseHelper.getFirebaseAuthInstance();

//        Find Views By Id's
        topAppBar = findViewById(R.id.topAppBar);
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
        rvMainCards = findViewById(R.id.rvMainCards);
        btnClick = findViewById(R.id.btnClick);

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
        rvMainCards.setAdapter(new MainCardAdapter(getData()));
        rvMainCards.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        rvMainCards.setHasFixedSize(true);
        rvMainCards.addItemDecoration(new GridSpacingItemDecoration(3, 22));

        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


    }


//  Main RecyclerView Data
    private List<MainCard> getData(){
        List<MainCard> cards = new ArrayList<>();
        cards.add(new MainCard(R.drawable.group, "All Members", 5));
        cards.add(new MainCard(R.drawable.group, "All Members", 5));
        cards.add(new MainCard(R.drawable.group, "All Members", 5));
        cards.add(new MainCard(R.drawable.group, "All Members", 5));

        return cards;
    }

}