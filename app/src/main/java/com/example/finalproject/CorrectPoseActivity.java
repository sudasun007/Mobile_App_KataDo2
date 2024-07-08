package com.example.finalproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CorrectPoseActivity extends AppCompatActivity {

    ImageButton closeButton;
    TextView correctPoseDescriptionTextView;
    TextView poseNameTextView;

    ImageView poseImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correct_pose_details);

        closeButton = findViewById(R.id.closebtn);
        correctPoseDescriptionTextView = findViewById(R.id.poseDesc);
        poseNameTextView = findViewById(R.id.posename);
        poseImageView = findViewById(R.id.imageview);

        // Retrieve the pose_name passed from ViewResultActivity
        Intent intent = getIntent();
        String poseName = intent.getStringExtra("pose_name");

        if (poseName != null) {
            // Fetch the details from the database
            DBHelper dbHelper = new DBHelper(this);
            Cursor cursor = dbHelper.getPoseDetails(poseName);

            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range")
                String poseDescription = cursor.getString(cursor.getColumnIndex("pose_description"));
                @SuppressLint("Range")
                int imageData = cursor.getInt(cursor.getColumnIndex("image_url"));

                poseNameTextView.setText(poseName);
                correctPoseDescriptionTextView.setText(poseDescription);
                // Set the Bitmap to the ImageView
                poseImageView.setImageResource(imageData);
                cursor.close();
            } else {
                poseNameTextView.setText("Pose not found");
                correctPoseDescriptionTextView.setText("No description available");
            }
        } else {
            poseNameTextView.setText("Pose name not provided");
            correctPoseDescriptionTextView.setText("No description available");
        }

        closeButton.setOnClickListener(v -> finish());
    }
}
