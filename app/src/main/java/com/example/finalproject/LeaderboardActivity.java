package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    private boolean isLeaderboard1Visible = true;
    private RecyclerView recyclerViewSinglePerformance;
    private RecyclerView recyclerViewGlobalRank;
    private LeaderboardAdapter adapterSinglePerformance;
    private Button switchButton;
    private ImageView homeButton;
    private Spinner timePeriodSpinner;
    private ViewSwitcher leaderboardSwitcher;
    private String selectedTimePeriod = "All Time";
    private List<LeaderboardData> leaderboardDataList;
    private DBHelper dbHelper;
    private String currentUserEmail;
    private GlobalLeaderboardAdapter adapterGlobalRank;
    private TextView textViewSinglePerformance;
    private TextView textViewGlobalRank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leadboard);
        initializeViews();
        dbHelper = new DBHelper(this);
        currentUserEmail = getCurrentUserEmail();

        leaderboardDataList = new ArrayList<>();
        setupRecyclerView();

        // Set initial colors
        textViewSinglePerformance.setTextColor(getResources().getColor(R.color.active_color));
        textViewGlobalRank.setTextColor(getResources().getColor(R.color.inactive_color));

        switchButton.setOnClickListener(v -> {
            switchLeaderboards();
            updateTextViewColors();
        });

        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(LeaderboardActivity.this, HomepageActivity.class);
            startActivity(intent);
            finish();
        });

        ImageView imageView6 = findViewById(R.id.imageView6);
        imageView6.setOnClickListener(v -> {
            Intent intent = new Intent(LeaderboardActivity.this, Guide_video_Activity.class);
            startActivity(intent);
        });

        ImageView imageView7 = findViewById(R.id.imageView7);
        imageView7.setOnClickListener(v -> {
            Intent intent = new Intent(LeaderboardActivity.this, LeaderboardActivity.class);
            startActivity(intent);
        });

        ImageView imageView8 = findViewById(R.id.imageView8);
        imageView8.setOnClickListener(v -> {
            // Pass the rank to ViewProfileActivity
            Intent intent = new Intent(LeaderboardActivity.this, ViewProfileActivity.class);
            intent.putExtra("userRank", 6); // Replace 6 with the actual rank from your leaderboard logic
            startActivity(intent);
        });

        ImageView insertVideo = findViewById(R.id.insertvideo);
        insertVideo.setOnClickListener(v -> {
            Intent intent = new Intent(LeaderboardActivity.this, PopupUpload.class);
            startActivity(intent);
        });

        timePeriodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTimePeriod = parent.getItemAtPosition(position).toString();
                setupRecyclerView(); // Reload data based on selected time period
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void initializeViews() {
        recyclerViewSinglePerformance = findViewById(R.id.recyclerViewSinglePerformance);
        recyclerViewGlobalRank = findViewById(R.id.recyclerViewGlobalRank);
        switchButton = findViewById(R.id.switchButton);
        homeButton = findViewById(R.id.homebtn);
        timePeriodSpinner = findViewById(R.id.timePeriodSpinner);
        leaderboardSwitcher = findViewById(R.id.leaderboardSwitcher);
        textViewSinglePerformance = findViewById(R.id.textView15);
        textViewGlobalRank = findViewById(R.id.textView16);
    }

    private void setupRecyclerView() {
        leaderboardDataList.clear();
        switch (selectedTimePeriod) {
            case "Last 24 Hours":
                leaderboardDataList.add(new LeaderboardData("Video 10", 6.7));
                leaderboardDataList.add(new LeaderboardData("Video 9", 6.5));
                break;
            case "Last 7 Days":
                leaderboardDataList.add(new LeaderboardData("Video 10", 6.7));
                leaderboardDataList.add(new LeaderboardData("Video 9", 6.5));
                leaderboardDataList.add(new LeaderboardData("Video 8", 8.0));
                leaderboardDataList.add(new LeaderboardData("Video 7", 9.0));
                break;
            case "Last 30 Days":
                leaderboardDataList.add(new LeaderboardData("Video 10", 6.7));
                leaderboardDataList.add(new LeaderboardData("Video 9", 6.5));
                leaderboardDataList.add(new LeaderboardData("Video 8", 8.0));
                leaderboardDataList.add(new LeaderboardData("Video 7", 9.0));
                leaderboardDataList.add(new LeaderboardData("Video 6", 7.0));
                leaderboardDataList.add(new LeaderboardData("Video 5", 7.8));
                break;
            case "All Time":
            default:
                leaderboardDataList.add(new LeaderboardData("Video 10", 6.7));
                leaderboardDataList.add(new LeaderboardData("Video 9", 6.5));
                leaderboardDataList.add(new LeaderboardData("Video 8", 8.0));
                leaderboardDataList.add(new LeaderboardData("Video 7", 9.0));
                leaderboardDataList.add(new LeaderboardData("Video 6", 7.0));
                leaderboardDataList.add(new LeaderboardData("Video 5", 7.8));
                leaderboardDataList.add(new LeaderboardData("Video 4", 8.4));
                leaderboardDataList.add(new LeaderboardData("Video 3", 8.1));
                leaderboardDataList.add(new LeaderboardData("Video 2", 8.3));
                leaderboardDataList.add(new LeaderboardData("Video 1", 8.2));
                break;
        }

        adapterSinglePerformance = new LeaderboardAdapter(this, leaderboardDataList);
        recyclerViewSinglePerformance.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSinglePerformance.setAdapter(adapterSinglePerformance);

        // Setup RecyclerView for global leaderboard
        List<GlobalLeaderboardData> globalLeaderboardList = createStaticGlobalLeaderboardData();
        adapterGlobalRank = new GlobalLeaderboardAdapter(this, globalLeaderboardList);
        recyclerViewGlobalRank.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewGlobalRank.setAdapter(adapterGlobalRank);
    }

    private List<GlobalLeaderboardData> createStaticGlobalLeaderboardData() {
        List<GlobalLeaderboardData> globalLeaderboardList = new ArrayList<>();
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);

        String firstName = preferences.getString("f_name", null);
        String lastName = preferences.getString("l_name", null);
        // Add static data for top 10 players
        globalLeaderboardList.add(new GlobalLeaderboardData(1, "Sudheera", "Dasun", 9.8));
        globalLeaderboardList.add(new GlobalLeaderboardData(2, "Lasith", "Dilshan", 9.7));
        globalLeaderboardList.add(new GlobalLeaderboardData(3, "Sandali", "Sithumini", 9.5));
        globalLeaderboardList.add(new GlobalLeaderboardData(4, "Dinithi", "Herath", 9.3));
        globalLeaderboardList.add(new GlobalLeaderboardData(5, "Tharushi", "Kavindya", 9.2));
        globalLeaderboardList.add(new GlobalLeaderboardData(6, firstName, lastName, 9.0));
        globalLeaderboardList.add(new GlobalLeaderboardData(7, "Amal", "Perera", 8.9));
        globalLeaderboardList.add(new GlobalLeaderboardData(8, "Dinuka", "Sandeepa", 8.8));
        globalLeaderboardList.add(new GlobalLeaderboardData(9, "Nuwan", "Perera", 8.7));
        globalLeaderboardList.add(new GlobalLeaderboardData(10, "Mahesh", "Pradeep", 8.7));

        return globalLeaderboardList;
    }

    private String getCurrentUserEmail() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return preferences.getString("user_email", null); // Return null if email not found
    }

    private void switchLeaderboards() {
        isLeaderboard1Visible = !isLeaderboard1Visible;
        leaderboardSwitcher.showNext();
    }

    private void updateTextViewColors() {
        if (leaderboardSwitcher.getDisplayedChild() == 0) {
            textViewSinglePerformance.setTextColor(getResources().getColor(R.color.active_color));
            textViewGlobalRank.setTextColor(getResources().getColor(R.color.inactive_color));
        } else {
            textViewSinglePerformance.setTextColor(getResources().getColor(R.color.inactive_color));
            textViewGlobalRank.setTextColor(getResources().getColor(R.color.active_color));
        }
    }
}