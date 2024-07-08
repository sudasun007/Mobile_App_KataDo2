package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.AdapterViewHolder> {

    private Context context;
    private List<ResultData> resultsDataList;
    private OnItemClickListener onItemClickListener;

    public RecyclerAdapter(Context context, List<ResultData> resultsDataList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.resultsDataList = resultsDataList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerAdapter.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_results, parent, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.AdapterViewHolder holder, int position) {
        ResultData resultData = resultsDataList.get(position);

        holder.results.setText(resultData.getResult());
        holder.cardImage.setImageResource(resultData.getImageUrl());
        holder.className.setText(resultData.getClassname());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(resultData);
            }
        });
    }

    @Override
    public int getItemCount() {
        return resultsDataList.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        TextView className;
        TextView results;
        Button view;
        ImageView cardImage;

        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            results = itemView.findViewById(R.id.single_result_id);
            view = itemView.findViewById(R.id.cardbtn);
            cardImage = itemView.findViewById(R.id.cardImage);
            className = itemView.findViewById(R.id.classname);
        }
    }
}
