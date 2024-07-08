package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProcessingActivity extends AppCompatActivity {

    ProgressBar progress;
    int mProgressStatus = 0;
    Handler handler = new Handler();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);

        progress = findViewById(R.id.progressBar);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mProgressStatus < 100) {
                    mProgressStatus++;
                    android.os.SystemClock.sleep(100); // Sleep for 100 milliseconds
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progress.setProgress(mProgressStatus);
                        }
                    });
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(ProcessingActivity.this, ViewResultActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
            }
        }).start(); // Don't forget to start the thread
    }
}
