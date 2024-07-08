package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.database.Cursor;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {

    private Context context;
    private List<LeaderboardData> leaderboardDataList;
    private Cursor cursor;

    // Constructor for List<LeaderboardData>
    public LeaderboardAdapter(Context context, List<LeaderboardData> leaderboardDataList) {
        this.context = context;
        this.leaderboardDataList = leaderboardDataList;
    }

    // Constructor for Cursor
    public LeaderboardAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_item, parent, false);
        return new LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        if (cursor != null) {
            if (cursor.moveToPosition(position)) {
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
                double result = cursor.getDouble(cursor.getColumnIndexOrThrow("final_result"));
                int rank = cursor.getInt(cursor.getColumnIndexOrThrow("rank"));

                holder.firstNameTextView.setText(firstName);
                holder.lastNameTextView.setText(lastName);
                holder.resultTextView.setText(String.valueOf(result));
                holder.rankTextView.setText(String.valueOf(rank));
            }
        } else {
            LeaderboardData data = leaderboardDataList.get(position);
            holder.firstNameTextView.setText(data.getVideoname());
            holder.resultTextView.setText(String.valueOf(data.getScore()));
            // You can set more fields if needed
        }
    }

    @Override
    public int getItemCount() {
        if (cursor != null) {
            return cursor.getCount();
        } else {
            return leaderboardDataList.size();
        }
    }

    public static class LeaderboardViewHolder extends RecyclerView.ViewHolder {
        TextView firstNameTextView, lastNameTextView, resultTextView, rankTextView;

        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);
            firstNameTextView = itemView.findViewById(R.id.video_name);
            resultTextView = itemView.findViewById(R.id.score);
        }
    }
}
