package com.example.finalproject;

import static com.example.finalproject.R.layout.activity_edit_video;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.MediaController;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.ContentValues;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;

public class EditVideoActivity extends AppCompatActivity {

    private static final long MAX_VIDEO_SIZE_MB = 600;
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 123;

    private VideoView videoView;
    private SeekBar trimSeekBarStart, trimSeekBarEnd;
    private Button trimButton, splitButton, uploadButton;
    private Uri videoUri;
    private ImageView homeButton;
    private int startTrim = 0; // Initialize startTrim
    private int endTrim = 0;
    private static final int REQUEST_CODE_PERMISSIONS = 101;
    private static final int REQUEST_STORAGE_PERMISSION = 100;
    private static final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_edit_video);

        videoView = findViewById(R.id.videoView);
        trimSeekBarStart = findViewById(R.id.trimSeekBarStart);
        trimSeekBarEnd = findViewById(R.id.trimSeekBarEnd);
        trimButton = findViewById(R.id.trimButton);
        splitButton = findViewById(R.id.splitButton);
        uploadButton = findViewById(R.id.upload);
        homeButton = findViewById(R.id.homebtn);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

        videoView.setVideoURI(videoUri);
        videoView.start();

        videoView.setOnCompletionListener(mp -> videoView.start());

        trimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trimVideo();
            }
        });

        splitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                splitVideo();
            }
        });


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_EXTERNAL_STORAGE);
        } else {
            handleVideo();
        }

        findViewById(R.id.editTextText).setOnClickListener(v -> {
            if (videoUri != null) {
                validateVideoFile(videoUri);
            } else {
                Toast.makeText(EditVideoActivity.this, "No video to validate", Toast.LENGTH_LONG).show();
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to home activity
                Intent intent = new Intent(EditVideoActivity.this, HomepageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditVideoActivity.this, ProcessingActivity.class);
                startActivity(intent);
                finish();
                String trimmedVideoPath = getOutputFilePath("trimmed_video.mp4");
                saveVideoToGallery(trimmedVideoPath);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                handleVideo();
            } else {
                Toast.makeText(this, "Permission denied to read external storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleVideo() {
        Intent intent = getIntent();
        videoUri = intent.getData();
        if (videoUri != null) {
            playVideo(videoUri);
        } else {
            Toast.makeText(this, "No video URI received", Toast.LENGTH_LONG).show();
        }
    }

    private void validateVideoFile(Uri videoUri) {
        try {
            File videoFile = new File(getCacheDir(), "temp_video.mp4");
            try (InputStream inputStream = getContentResolver().openInputStream(videoUri);
                 OutputStream outputStream = new FileOutputStream(videoFile)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            if (videoFile.exists()) {
                if (isMp4File(videoFile)) {
                    long fileSizeInMB = videoFile.length() / (1024 * 1024);
                    if (fileSizeInMB <= MAX_VIDEO_SIZE_MB) {
                        Toast.makeText(EditVideoActivity.this, "Video file is valid and under 600MB.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(EditVideoActivity.this, "Video file is larger than 600MB.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(EditVideoActivity.this, "Video file is not an MP4 file.", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(EditVideoActivity.this, "Video file does not exist.", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            Toast.makeText(EditVideoActivity.this, "Failed to validate video file.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isMp4File(File file) {
        String fileName = file.getName();
        return fileName.toLowerCase().endsWith(".mp4");
    }

    private void playVideo(Uri videoUri) {
        videoView.setVideoURI(videoUri);
        videoView.start();
    }

    protected void onPause() {
        super.onPause();
        if (videoView.isPlaying()) {
            videoView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (videoUri != null) {
            videoView.setVideoURI(videoUri);
            videoView.start();
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void trimVideo() {
        if (videoUri != null) {
            String outputPath = getOutputFilePath("trimmed_video.mp4");
            Log.d("EditVideoActivity", "Trimming video from " + startTrim + " to " + endTrim);
            VideoProcessor.trim(videoUri, startTrim, endTrim, outputPath, this, new VideoProcessor.TrimCallback() {
                @Override
                public void onTrimCompleted(String outputPath) {
                    runOnUiThread(() -> {
                        Log.d("EditVideoActivity", "Trim completed, setting video path to: " + outputPath);
                        videoView.setVideoPath(outputPath);
                        videoView.setOnPreparedListener(mp -> {
                            Log.d("EditVideoActivity", "Video prepared, starting playback");
                            videoView.start();
                        });
                        videoView.setOnErrorListener((mp, what, extra) -> {
                            Log.e("EditVideoActivity", "Error occurred while playing video: " + what + ", " + extra);
                            return true;
                        });
                    });
                }
            });
            Toast.makeText(this, "Trimming...", Toast.LENGTH_SHORT).show();
        } else {
            Log.e("EditVideoActivity", "Video URI is null");
        }
    }

    private void splitVideo() {
        if (videoUri != null) {
            String outputPath1 = getOutputFilePath("split_video_part1.mp4");
            String outputPath2 = getOutputFilePath("split_video_part2.mp4");
            VideoProcessor.split(videoUri, startTrim, outputPath1, outputPath2, this);
            Toast.makeText(this, "Splitting...", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveVideoToGallery(String videoPath) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/TrimmedVideos");
        values.put(MediaStore.Video.Media.TITLE, "Trimmed Video");
        values.put(MediaStore.Video.Media.DISPLAY_NAME, "trimmed_video.mp4");
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        values.put(MediaStore.Video.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Video.Media.DATE_TAKEN, System.currentTimeMillis());

        Uri uri = getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
        try (OutputStream out = getContentResolver().openOutputStream(uri)) {
            FileInputStream fis = new FileInputStream(new File(videoPath));
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            fis.close();
            Toast.makeText(this, "Video saved to gallery", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(this, "Failed to save video to gallery", Toast.LENGTH_SHORT).show();
        }
    }

    private String getOutputFilePath(String fileName) {
        File dir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES), "MyApp");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, fileName).getAbsolutePath();
    }




}
