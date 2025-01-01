package com.example.gymmanagementsystem;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    TextView tvSignUp;
    EditText etEmail, etPassword;
    Button btnLogin;

    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        find view by ids
        tvSignUp = findViewById(R.id.tvSignUp);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

//        Firebase initialization
        firebaseAuth = FirebaseAuth.getInstance();

//        Click Listeners
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateEmailPassword();

            }
        });

    }

    void validateEmailPassword(){
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        System.out.println(email);

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(LoginActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 8) {
            Toast.makeText(this, "Password length should be equal or greater than 8", Toast.LENGTH_SHORT).show();
        }
        else {
            loginUser(email, password);
        }
    }

    private void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task ->{
            if (task.isSuccessful()){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Toast.makeText(this, "user" + user.getEmail(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}