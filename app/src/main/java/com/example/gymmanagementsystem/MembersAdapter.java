package com.example.gymmanagementsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MemberViewHolder> {

    List<Member> members;

    public MembersAdapter(List<Member> members) {
        this.members = members;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_recycler_layout, null);
        return new MemberViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        Member member = members.get(position);
        holder.tvName.setText(member.getName());
        holder.tvPhoneNumber.setText(member.getPhoneNumber());
        holder.tvAddress.setText(member.getAddress());
        holder.tvPlan.setText(member.getPlanName());
        holder.tvPlanAmount.setText(String.valueOf(member.getPlanAmount()));
        holder.tvPaidAmount.setText(String.valueOf(member.getPaidAmount()));
        holder.tvDueAmount.setText(String.valueOf(member.getPlanAmount() - member.getPaidAmount()));
        holder.tvStartDate.setText(member.getStartDate());
        holder.tvEndDate.setText(member.getEndDate());
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public static class MemberViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvPhoneNumber, tvAddress, tvStartDate, tvEndDate, tvPlanAmount, tvPaidAmount, tvPlan, tvDueAmount;
        ImageButton ibProfileButton, ibEditButton, ibDeleteButton;
        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvStartDate = itemView.findViewById(R.id.tvStartDate);
            tvEndDate = itemView.findViewById(R.id.tvEndDate);
            tvPlanAmount = itemView.findViewById(R.id.tvPlanAmount);
            tvPaidAmount = itemView.findViewById(R.id.tvPaidAmount);
            tvPlan = itemView.findViewById(R.id.tvPlan);
            tvDueAmount = itemView.findViewById(R.id.tvDueAmount);
            ibProfileButton = itemView.findViewById(R.id.ibProfileButton);
            ibEditButton = itemView.findViewById(R.id.ibEditButton);
            ibDeleteButton = itemView.findViewById(R.id.ibDeleteButton);
        }
    }
}
