package com.example.finalproject;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;

public class VideoProcessor {



    public interface TrimCallback {
        void onTrimCompleted(String outputPath);
    }

    public static void trim(Uri videoUri, int start, int end, String outputPath, Context context, TrimCallback callback) {
        String inputPath = FileUtils.getPath(context, videoUri);
        if (inputPath == null) {
            Log.e("VideoProcessor", "Failed to get input path");
            return;
        }

        String command = String.format("-i %s -ss %d -to %d -c copy %s", inputPath, start / 1000, end / 1000, outputPath);
        Log.d("VideoProcessor", "Executing FFmpeg command: " + command);

        int rc = FFmpeg.execute(command);
        if (rc == Config.RETURN_CODE_SUCCESS) {
            Log.i("VideoProcessor", "Command executed successfully");
            if (callback != null) {
                callback.onTrimCompleted(outputPath);
            }
        } else {
            Log.e("VideoProcessor", String.format("Command execution failed with return code %d.", rc));
        }
    }

    public static void split(Uri videoUri, int splitPoint, String outputPath1, String outputPath2, Context context) {
        String inputPath = FileUtils.getPath(context, videoUri);
        if (inputPath == null) {
            Log.e("VideoProcessor", "Failed to get input path");
            return;
        }

        String command1 = String.format("-i %s -t %d -c copy %s", inputPath, splitPoint / 1000, outputPath1);
        String command2 = String.format("-i %s -ss %d -c copy %s", inputPath, splitPoint / 1000, outputPath2);
        executeFFmpegCommand(command1);
        executeFFmpegCommand(command2);
    }

    private static void executeFFmpegCommand(String command) {
        int rc = FFmpeg.execute(command);
        if (rc == Config.RETURN_CODE_SUCCESS) {
            Log.i("VideoProcessor", "Command executed successfully");
        } else {
            Log.e("VideoProcessor", String.format("Command execution failed with return code %d.", rc));
        }
    }
}
