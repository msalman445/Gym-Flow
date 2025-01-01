package com.example.gymmanagementsystem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class AddMemberActivity extends AppCompatActivity {
    final Calendar calendar = Calendar.getInstance();
    RadioGroup radioGroup;
    RadioButton radioButtonMale, radioButtonFemale;
    EditText etStartDatePicker, etEndDatePicker,etName, etPhoneNumber,etPlanName, etAddress, etPlanAmount, etPaidAmount;
    Toolbar toolbar;
    Button btnSubmit;



    private FirebaseAuth firebaseAuth;
    private String appUserId;
    private String gender = "Male";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

//        Firebase initialization
        firebaseAuth = FirebaseAuth.getInstance();
        appUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

//        Find Views By id's
        etName = findViewById(R.id.etName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etAddress = findViewById(R.id.etAddress);
        etPlanName = findViewById(R.id.etPlanName);
        etPlanAmount = findViewById(R.id.etPlanAmount);
        etPaidAmount = findViewById(R.id.etPaidAmount);
        etStartDatePicker = findViewById(R.id.etStartDatePicker);
        etEndDatePicker = findViewById(R.id.etEndDatePicker);

        radioGroup = findViewById(R.id.radioGroup);
        radioButtonMale = findViewById(R.id.radioButtonMale);
        radioButtonFemale = findViewById(R.id.radioButtonFemale);

        toolbar = findViewById(R.id.topAppBar);

        btnSubmit = findViewById(R.id.btnSubmit);

//        Click Listeners
        etStartDatePicker.setText(getCurrentDate());
        etStartDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(etStartDatePicker);

            }
        });

        etEndDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(etEndDatePicker);
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);
                gender = selectedRadioButton.getText().toString();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//      Form Submission
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });



    }

//    Date Picker Dialog for picking date

    private void showDatePicker(EditText editText){
        new DatePickerDialog(AddMemberActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateTable(editText);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


//    Function to format date
    private void updateTable(EditText editText){
        String myFormat = "dd/MM/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(dateFormat.format(calendar.getTime()));
    }
//  Form Validation
    private void validateForm(){
        String name = etName.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String planName = etPlanName.getText().toString().trim();
        String planAmount = etPlanAmount.getText().toString().trim();
        String paidAmount = etPaidAmount.getText().toString().trim();
        String startDate = etStartDatePicker.getText().toString().trim();
        String endDate = etEndDatePicker.getText().toString().trim();


        if ((name.length() < 3) || !(name.matches("^[a-zA-Z\\s]+$"))){
            Toast.makeText(this, "Please enter a valid username", Toast.LENGTH_SHORT).show();
        } else if ( !phoneNumber.matches("^(\\+\\d{1,3}\\d{9}|\\d{11})$")){
            Toast.makeText(this, "Please enter a valid Phone Number", Toast.LENGTH_SHORT).show();
        } else if (address.isEmpty()){
            Toast.makeText(this, "Please enter address", Toast.LENGTH_SHORT).show();
        }  else if(planName.isEmpty()){
            Toast.makeText(this, "Please enter plan name", Toast.LENGTH_SHORT).show();
        } else if (planAmount.isEmpty()) {
            Toast.makeText(this, "Please enter plan amount", Toast.LENGTH_SHORT).show();
        }else if (paidAmount.isEmpty()){
            Toast.makeText(this, "Please enter paid amount", Toast.LENGTH_SHORT).show();
        }else if (startDate.isEmpty()){
            Toast.makeText(this, "Please select start date", Toast.LENGTH_SHORT).show();
        } else if (endDate.isEmpty()) {
            Toast.makeText(this, "Please select end date", Toast.LENGTH_SHORT).show();
        }else{
            long timestamp = System.currentTimeMillis();  // Store the current timestamp
            Member member = new Member(timestamp ,gender ,endDate, startDate, Double.parseDouble(paidAmount), Double.parseDouble(planAmount), planName, address, phoneNumber, name);
            saveMemberToFirebase(member);
        }
    }
//      Save data to firebase
    private void saveMemberToFirebase(Member member){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

//        Generate a unique id for each member
        String memberId = databaseReference.child(FirebaseHelper.APP_USERS).child(appUserId).child(FirebaseHelper.MEMBERS).push().getKey();

//        Save member under the generated id
        if (memberId != null){
            databaseReference.child(FirebaseHelper.APP_USERS).child(appUserId).child(FirebaseHelper.MEMBERS).child(memberId).setValue(member).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    clearTextFields();
                    showAlertDialog("Success", "Member Added Successfully", "OK", R.drawable.checked);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showAlertDialog("Failure", "Member Not Inserted: " + e.getMessage(), "Retry", R.drawable.cancel);

                }
            });
        }
    }

    private void showAlertDialog(String title, String message, String buttonText, int iconId){
        new AlertDialog.Builder(AddMemberActivity.this).setTitle(title).setMessage(message).setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setCancelable(false).setIcon(iconId).show();
    }

    private void clearTextFields(){
        etName.setText("");
        etPhoneNumber.setText("");
        etAddress.setText("");
        etPlanName.setText("");
        etPlanAmount.setText("");
        etPaidAmount.setText("");
        etStartDatePicker.setText(getCurrentDate());
        etEndDatePicker.setText("");
        gender = "Male"; // Reset default gender

    }

    private String getCurrentDate(){
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH); // Month is 0-based, so January is 0
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Format the date as "dd/MM/yyyy"
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return simpleDateFormat.format(calendar.getTime());
    }



}