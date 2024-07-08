package com.example.finalproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Guide_video_Activity extends AppCompatActivity implements VideoAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private TextView textView13;
    private VideoView videoView;
    private VideoAdapter adapter;
    private List<VideoItem> videoList;
    private ImageView homeButton;
    private boolean isNoResultsDisplayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_video);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        textView13 = findViewById(R.id.textView13);
        videoView = findViewById(R.id.videoView);
        homeButton = findViewById(R.id.homebtn);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        videoList = new ArrayList<>();
        videoList.add(new VideoItem("Video 1", "android.resource://" + getPackageName() + "/" + R.raw.video1, "thumbnail_url_1"));
        videoList.add(new VideoItem("Video 2", "android.resource://" + getPackageName() + "/" + R.raw.video2, "thumbnail_url_2"));
        videoList.add(new VideoItem("Video 3", "android.resource://" + getPackageName() + "/" + R.raw.video3, "thumbnail_url_3"));
        adapter = new VideoAdapter(this, videoList);

        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        // Set up SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText, new Filter.FilterListener() {
                    @Override
                    public void onFilterComplete(int count) {
                        if (count == 0) {
                            // Check if the newText is not empty to avoid showing error on empty input
                            if (!newText.isEmpty() && !isNoResultsDisplayed) {
                                Toast.makeText(Guide_video_Activity.this, "No search results found", Toast.LENGTH_SHORT).show();
                                isNoResultsDisplayed = true;
                            }
                        } else {
                            isNoResultsDisplayed = false;
                        }
                    }
                });
  return true;
            }
});

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to home activity
                Intent intent = new Intent(Guide_video_Activity.this, HomepageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Find the ImageViews and set OnClickListeners
        ImageView imageView6 = findViewById(R.id.imageView6);
        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start Guide_video_Activity
                Intent intent = new Intent(Guide_video_Activity.this, Guide_video_Activity.class);
                startActivity(intent);
            }
        });

        ImageView imageView7 = findViewById(R.id.imageView7);
        imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start LeaderboardActivity
                Intent intent = new Intent(Guide_video_Activity.this, LeaderboardActivity.class);
                startActivity(intent);
            }
        });

        ImageView imageView8 = findViewById(R.id.imageView8);
        imageView8.setOnClickListener(v -> {
            // Pass the rank to ViewProfileActivity
            Intent intent = new Intent(Guide_video_Activity.this, ViewProfileActivity.class);
            intent.putExtra("userRank", 6); // Replace 6 with the actual rank from your leaderboard logic
            startActivity(intent);
        });

        ImageView insertVideo = findViewById(R.id.insertvideo);
        insertVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start PopupUpload
                Intent intent = new Intent(Guide_video_Activity.this, PopupUpload.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        VideoItem selectedItem = adapter.getItem(position);
        if (selectedItem != null) {
            Toast.makeText(this, "Playing video: " + selectedItem.getTitle(), Toast.LENGTH_SHORT).show();
            Uri videoUri = Uri.parse(selectedItem.getVideoUri());
            videoView.setVideoURI(videoUri);
            videoView.setMediaController(new MediaController(this));
            videoView.requestFocus();
            videoView.start();
        }
    }
}