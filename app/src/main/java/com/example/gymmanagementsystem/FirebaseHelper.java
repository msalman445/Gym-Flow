package com.example.gymmanagementsystem;

import com.google.firebase.auth.FirebaseAuth;

public class FirebaseHelper {
    private static FirebaseAuth firebaseAuth;
//    private FirebaseD
    public static FirebaseAuth getFirebaseAuthInstance(){
        if (firebaseAuth == null){
            firebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }
}
