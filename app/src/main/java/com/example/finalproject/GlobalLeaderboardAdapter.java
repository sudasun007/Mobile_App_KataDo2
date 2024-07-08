package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GlobalLeaderboardAdapter extends RecyclerView.Adapter<GlobalLeaderboardAdapter.ViewHolder> {

    private Context context;
    private List<GlobalLeaderboardData> leaderboardDataList;

    public GlobalLeaderboardAdapter(Context context, List<GlobalLeaderboardData> leaderboardDataList) {
        this.context = context;
        this.leaderboardDataList = leaderboardDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.global_leaderboard_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GlobalLeaderboardData data = leaderboardDataList.get(position);
        holder.bind(data);
    }

    @Override
    public int getItemCount() {
        return leaderboardDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView rankTextView;
        TextView nameTextView;
        TextView scoreTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rankTextView = itemView.findViewById(R.id.rankTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            scoreTextView = itemView.findViewById(R.id.scoreTextView);
        }

        public void bind(GlobalLeaderboardData data) {
            rankTextView.setText(String.valueOf(data.getRank()));
            nameTextView.setText(data.getFirstName() + " " + data.getLastName());
            scoreTextView.setText(String.valueOf(data.getFinalScore()));
        }
    }
}
