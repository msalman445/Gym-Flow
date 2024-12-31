package com.example.gymmanagementsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    List<Attendance> attendances;
    public AttendanceAdapter(List<Attendance> attendances){
        this.attendances = attendances;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_member_recyclerview, null);
        return new AttendanceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        Attendance attendance = attendances.get(position);
        holder.tvName.setText(attendance.getName());
        holder.switchAttendance.setChecked(attendance.isPresent());

        holder.switchAttendance.setOnCheckedChangeListener((buttonView, isChecked) -> {
            attendance.setPresent(isChecked); // Update the data model
        });
    }

    @Override
    public int getItemCount() {
        return attendances.size();
    }

    public static class AttendanceViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        SwitchMaterial switchAttendance;
        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            switchAttendance = itemView.findViewById(R.id.switchAttendance);

        }
    }
}
