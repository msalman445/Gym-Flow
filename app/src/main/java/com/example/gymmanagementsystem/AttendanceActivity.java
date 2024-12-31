package com.example.gymmanagementsystem;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AttendanceActivity extends AppCompatActivity {

    private RecyclerView rvAttendance;
    private AttendanceAdapter attendanceAdapter;
    private Attendance attendance;

    private TextView tvDate;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_activitiy);

        rvAttendance = findViewById(R.id.rvAttendance);
        tvDate = findViewById(R.id.tvDate);

//        Assign Current Date to TextView
        String currentDate = getCurrentDate();
        tvDate.setText("Today Attendance Date: " + currentDate);

        attendanceAdapter = new AttendanceAdapter(getData());
        rvAttendance.setAdapter(attendanceAdapter);
        rvAttendance.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvAttendance.setHasFixedSize(true);


    }

    private List<Attendance> getData(){
        List<Attendance> attendances = new ArrayList<Attendance>();
        attendances.add(new Attendance(false, "Muhammad Salman"));
        attendances.add(new Attendance(false, "Muhammad Ali"));
        attendances.add(new Attendance(true, "Muhammad Abu Bakar"));
        attendances.add(new Attendance(false, "Muhammad Burhan ul din Farooqi"));
        attendances.add(new Attendance(false, "Muhammad Ali"));
        return  attendances;
    }

    private String getCurrentDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Calendar calendar = Calendar.getInstance();
        return  simpleDateFormat.format(calendar.getTime());
    }
}