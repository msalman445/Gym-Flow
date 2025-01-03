package com.example.gymmanagementsystem;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MemberViewHolder> {

    List<Member> members;
    IOnProfileClickListener iOnProfileClickListener;
    IOnEditClickListener iOnEditClickListener;
    IOnDeleteClickListener iOnDeleteClickListener;

    public MembersAdapter(List<Member> members) {
        this.members = members;
    }

    public void setIOnProfileClickListener(IOnProfileClickListener iOnProfileClickListener) {
        this.iOnProfileClickListener = iOnProfileClickListener;
    }

    public void setIOnEditClickListener(IOnEditClickListener iOnEditClickListener) {
        this.iOnEditClickListener = iOnEditClickListener;
    }

    public void setIOnDeleteClickListener(IOnDeleteClickListener iOnDeleteClickListener) {
        this.iOnDeleteClickListener = iOnDeleteClickListener;
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), MemberProfileActivity.class);
                intent.putExtra("MEMBER_ID", member.getMemberId());
                startActivity(v.getContext(), intent, null);
            }
        });

    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public class MemberViewHolder extends RecyclerView.ViewHolder{
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

            ibProfileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iOnProfileClickListener != null){
                        int position = getAdapterPosition();
                        iOnProfileClickListener.onProfileButtonClick(MemberViewHolder.this, position, ibProfileButton);
                    }
                }
            });

            ibEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iOnEditClickListener != null){
                        int position = getAdapterPosition();
                        iOnEditClickListener.onEditButtonClick(MemberViewHolder.this, position, ibEditButton);
                    }
                }
            });

            ibDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iOnDeleteClickListener != null){
                        int position = getAdapterPosition();
                        iOnDeleteClickListener.onDeleteButtonClick(MemberViewHolder.this, position, ibDeleteButton);
                    }
                }
            });
        }
    }

    public interface IOnProfileClickListener{
        void onProfileButtonClick(MemberViewHolder holder, int position, ImageButton ibProfileButton);
    }

    public interface IOnEditClickListener{
        void onEditButtonClick(MemberViewHolder holder, int position, ImageButton ibProfileButton);
    }

    public interface IOnDeleteClickListener{
        void onDeleteButtonClick(MemberViewHolder holder, int position, ImageButton ibProfileButton);
    }

}


