package com.example.gymmanagementsystem;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddMemberActivity extends AppCompatActivity {
    final Calendar calendar = Calendar.getInstance();
    RadioGroup radioGroup;
    RadioButton radioButtonMale, radioButtonFemale;
    EditText etStartDatePicker, etEndDatePicker,etName, etPhoneNumber,etPlanName, etAddress, etPlanAmount, etPaidAmount;
    Toolbar toolbar;
    Button btnSubmit;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        etStartDatePicker = findViewById(R.id.etStartDatePicker);
        etEndDatePicker = findViewById(R.id.etEndDatePicker);
        etName = findViewById(R.id.etName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etAddress = findViewById(R.id.etAddress);
        etPlanName = findViewById(R.id.etPlanName);
        etPlanAmount = findViewById(R.id.etPlanAmount);
        etPaidAmount = findViewById(R.id.etPaidAmount);

        radioGroup = findViewById(R.id.radioGroup);
        radioButtonMale = findViewById(R.id.radioButtonMale);
        radioButtonFemale = findViewById(R.id.radioButtonFemale);

        toolbar = findViewById(R.id.topAppBar);

        btnSubmit = findViewById(R.id.btnSubmit);

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
                Toast.makeText(AddMemberActivity.this, ""+selectedRadioButton.getText().toString() , Toast.LENGTH_SHORT).show();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
            Toast.makeText(this, "Wow Nice", Toast.LENGTH_SHORT).show();
        }
    }



}