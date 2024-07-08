package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class HomepageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homepage);

        // Find the ImageViews and set OnClickListeners
        ImageView imageView6 = findViewById(R.id.imageView6);
        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start Guide_video_Activity
                Intent intent = new Intent(HomepageActivity.this, Guide_video_Activity.class);
                startActivity(intent);
            }
        });

        ImageView imageView7 = findViewById(R.id.imageView7);
        imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start LeaderboardActivity
                Intent intent = new Intent(HomepageActivity.this, LeaderboardActivity.class);
                startActivity(intent);
            }
        });

        ImageView imageView8 = findViewById(R.id.imageView8);
        imageView8.setOnClickListener(v -> {
            // Pass the rank to ViewProfileActivity
            Intent intent = new Intent(HomepageActivity.this, ViewProfileActivity.class);
            intent.putExtra("userRank", 6); // Replace 6 with the actual rank from your leaderboard logic
            startActivity(intent);
        });

        ImageView insertVideo = findViewById(R.id.insertvideo);
        insertVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start PopupUpload
                Intent intent = new Intent(HomepageActivity.this, PopupUpload.class);
                startActivity(intent);
            }
        });
    }
}
