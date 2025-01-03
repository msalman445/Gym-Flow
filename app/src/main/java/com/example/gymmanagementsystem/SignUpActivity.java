package com.example.gymmanagementsystem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    TextView tvLogin;
    EditText etName, etEmail, etPassword;
    Button btnSignUp;
    private FirebaseAuth mFirebaseAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

//        Find View By Id
        tvLogin = findViewById(R.id.tvLogin);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignUp = findViewById(R.id.btnSignUp);

//        Firebase Initialization
        mFirebaseAuth = FirebaseAuth.getInstance();


//        Click Listeners
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();

            }
        });


    }

    private void validate(){
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if ((name.length() < 3) || !(name.matches("^[a-zA-Z\\s]+$"))){
            Toast.makeText(this, "Please enter a valid username", Toast.LENGTH_SHORT).show();
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(SignUpActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 8) {
            Toast.makeText(this, "Password length should be equal or greater than 8", Toast.LENGTH_SHORT).show();
        }else{
            signUpUser(name, email, password);
        }
    }

    private void signUpUser(String name, String email, String password) {
        // You can replace this with Firebase Authentication or other logic
        mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()){
                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                if (user != null){
                    saveUserName(user.getUid(), name);
                }
                Toast.makeText(SignUpActivity.this, "To Continue, Sign in Please", Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(SignUpActivity.this, "Authentication Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) ;
    }

    private void saveUserName(String userId, String name) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference(FirebaseHelper.APP_USERS)
                .child(userId);

        // Create a HashMap for user data
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", name);

        databaseReference.setValue(userMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        finish(); // Close the activity
                    } else {
                        Toast.makeText(this, "Failed to save user: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}