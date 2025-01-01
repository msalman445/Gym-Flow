package com.example.gymmanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainCardAdapter extends RecyclerView.Adapter<MainCardAdapter.MainCardViewHolder> {
    private final List<MainCard> mainCards;
    private final Context context;
    IOnMainCardClickListener iOnMainCardClickListener;

    public MainCardAdapter(List<MainCard> mainCards, Context context){
        this.mainCards = mainCards;
        this.context = context;

    }

    public void setIOnMainCardClickListener(IOnMainCardClickListener iOnMainCardClickListener) {
        this.iOnMainCardClickListener = iOnMainCardClickListener;
    }

    @NonNull
    @Override
    public MainCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recycler_layout,  null);
        return new MainCardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MainCardViewHolder holder, int position) {
        MainCard mainCard = mainCards.get(position);
        holder.ivMainCardIcon.setImageResource(mainCard.getIconId());
        holder.tvMainCardTitle.setText(mainCard.getTitle());
        holder.tvMainCardMembers.setText(String.valueOf(mainCard.getMembers()));

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent;
//                switch (holder.getAdapterPosition()){
//                    case 0:
//                        intent = new Intent(context, AddMemberActivity.class);
//                        break;
//                    default:
//                        intent = new Intent(context, MembersActivity.class);
//                }
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mainCards.size();
    }

    public class MainCardViewHolder extends RecyclerView.ViewHolder{
        ImageView ivMainCardIcon;
        TextView tvMainCardTitle, tvMainCardMembers;

        public MainCardViewHolder(@NonNull View itemView) {
            super(itemView);

            ivMainCardIcon = itemView.findViewById(R.id.ivMainCardIcon);
            tvMainCardTitle = itemView.findViewById(R.id.tvMainCardTitle);
            tvMainCardMembers = itemView.findViewById(R.id.tvMainCardMembers);

            itemView.setOnClickListener(v -> {
                if (iOnMainCardClickListener != null){
                    int position = getAdapterPosition();
                    iOnMainCardClickListener.onMainCardClick(MainCardViewHolder.this, position);
                }

            });
        }
    }

    public interface IOnMainCardClickListener{
        void onMainCardClick(MainCardViewHolder holder, int position);
    }
}
