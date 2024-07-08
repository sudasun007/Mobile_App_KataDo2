package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ViewResultActivity extends AppCompatActivity implements OnItemClickListener {

    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private List<ResultData> resultsDataList;
    private ImageView homebtn;

    TextView kataname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_recycler_page);

        recyclerView = findViewById(R.id.recyclerView_result);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        resultsDataList = new ArrayList<>();
        // Add ResultData items to the list
        resultsDataList.add(new ResultData("90%", R.drawable.pose1, "motodachiageuke"));
        resultsDataList.add(new ResultData("90%", R.drawable.pose2, "motodachijodantsuki"));
        resultsDataList.add(new ResultData("90%", R.drawable.pose3, "shikodachigedanbarai"));
        resultsDataList.add(new ResultData("90%", R.drawable.pose4, "motodachiageuke"));
        resultsDataList.add(new ResultData("90%", R.drawable.pose5, "motodachijodantsuki"));
        resultsDataList.add(new ResultData("90%", R.drawable.pose6, "shikodachigedanbarai"));
        // Add more items...

        recyclerAdapter = new RecyclerAdapter(this, resultsDataList, this);
        recyclerView.setAdapter(recyclerAdapter);


        homebtn = findViewById(R.id.homebtn);
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewResultActivity.this, HomepageActivity.class);
                startActivity(intent);
            }
        });

        // Find the ImageViews and set OnClickListeners
        ImageView imageView6 = findViewById(R.id.imageView6);
        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start Guide_video_Activity
                Intent intent = new Intent(ViewResultActivity.this, Guide_video_Activity.class);
                startActivity(intent);
            }
        });

        ImageView imageView7 = findViewById(R.id.imageView7);
        imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start LeaderboardActivity
                Intent intent = new Intent(ViewResultActivity.this, LeaderboardActivity.class);
                startActivity(intent);
            }
        });

        ImageView imageView8 = findViewById(R.id.imageView8);
        imageView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start ViewProfileActivity
                Intent intent = new Intent(ViewResultActivity.this, ViewProfileActivity.class);
                startActivity(intent);
            }
        });
    }



    @Override
    public void onItemClick(ResultData resultData) {
        Intent intent = new Intent(ViewResultActivity.this, CorrectPoseActivity.class);
        intent.putExtra("pose_name", resultData.getClassname());
        startActivity(intent);
    }
}
