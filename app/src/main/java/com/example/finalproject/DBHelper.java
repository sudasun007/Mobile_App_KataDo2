package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "KarateApp";
    private static final int DB_VERSION = 8; // Incremented version
    private final Context context;
    private SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DBHelper", "onCreate called");
        db.execSQL("CREATE TABLE Member(" +
                "email TEXT PRIMARY KEY," +
                "first_name TEXT," +
                "last_name TEXT," +
                "password TEXT," +
                "active_status INTEGER DEFAULT 0);"); // Added active_status column

        db.execSQL("CREATE TABLE Correct_poses(" +
                "pose_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "pose_name TEXT," +
                "pose_description TEXT,"+
                "image_url Int);");

        db.execSQL("CREATE TABLE result(" +
                "result_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "mem_email TEXT," +
                "video_name TEXT," +
                "date_time DATETIME," +
                "final_result DOUBLE," +
                "rank INTEGER ," +
                "FOREIGN KEY(mem_email) REFERENCES Member(email));");

        addInitialCorrectPoses(db);
        addInitialResult(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DBHelper", "onUpgrade called from version " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS Member;");
        db.execSQL("DROP TABLE IF EXISTS Correct_poses;");
        db.execSQL("DROP TABLE IF EXISTS result;");
        onCreate(db);

    }

    private void addInitialCorrectPoses(SQLiteDatabase db) {
        addCorrectPose(db, "motodachiageuke", "Moto dachi is equal to Junzuki dachi, but it is a shorter stance. This stance is from Shito Ryu and is found in Bassai (movement 2,3,4 and 5) and in Niseishi (movement 1 till 5).",R.drawable.motodachiageukeright);
        addCorrectPose(db, "motodachijodantsuki", "Moto dachi  is a shorter stance. This stance is from Shito Ryu and is found in Bassai (movement 2,3,4 and 5) and in Niseishi (movement 1 till 5).",R.drawable.motodachijodantsukileft);
        addCorrectPose(db, "shikodachigedanbarai", "One shin length plus one to one and a half fist between the heels. Feet point outside. Bend your legs should with the knee above the ankle.",R.drawable.shikodachigedanbaraileft);

        Cursor cursor = db.rawQuery("SELECT * FROM Correct_poses", null);
        if (cursor.getCount() > 0) {
            Log.d("DBHelper1", "Initial data inserted successfully");
        } else {
            Log.d("DBHelper1", "Initial data insertion failed");
        }
        cursor.close();
    }

    private void addCorrectPose(SQLiteDatabase db, String pose_name, String pose_description, int imageUrl) {
        ContentValues values = new ContentValues();
        values.put("pose_name", pose_name);
        values.put("pose_description", pose_description);
        values.put("image_url", imageUrl);
        db.insert("Correct_poses", null, values);
    }

    public void insertInitialDataIfNeeded() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM Correct_poses", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        if (count == 0) {
            addInitialCorrectPoses(db);
        }
    }

    public ArrayList<String[]> getAllCorrectPoses() {
        ArrayList<String[]> poses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT pose_name, pose_description FROM Correct_poses", null);
        if (cursor.moveToFirst()) {
            do {
                String[] pose = {cursor.getString(0), cursor.getString(1)};
                poses.add(pose);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return poses;
    }

    public Boolean addNewMember(String email, String fname, String lname, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("first_name", fname);
        values.put("last_name", lname);
        values.put("password", password);
        long result = db.insert("Member", null, values);

        return result != -1;
    }

    public boolean isEmailRegistered(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Member WHERE email = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public boolean updatePassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", newPassword);
        int rows = db.update("Member", values, "email = ?", new String[]{email});

        return rows > 0;
    }


    public Boolean addResult(SQLiteDatabase db,String email, String video_name, double result, int rank) {
        ContentValues values = new ContentValues();
        values.put("mem_email", email);
        values.put("video_name", video_name);
        values.put("final_result", result);
        values.put("rank", rank);
        values.put("date_time", getDateTime());
        long inserted = db.insert("result", null, values);

        return inserted != -1;
    }

    private void addInitialResult(SQLiteDatabase db) {
        addResult(db, "sithumanisandali@gmail.com", "video1", 9.0, 1);
        addResult(db, "abc@gmail.com", "video2", 8.9, 2);
        addResult(db, "sandalisithumani@gmail.com", "video1", 8.7, 3);
        addResult(db, "lasithdilshan@gmail.com", "video5", 8.5, 4);
        addResult(db, "123email@gmail.com", "video9", 8.4, 5);
        addResult(db, "sithu@gmail.com", "video1", 8.1, 6);
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    public Cursor getData() {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from result", null);
        return cursor;
    }
    public boolean updateActiveStatus(String email, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("active_status", status);
        int rows = db.update("Member", values, "email = ?", new String[]{email});

        return rows > 0;

    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                "Member",
                new String[]{"email"},
                "email=? AND password=?",
                new String[]{email, password},
                null,
                null,
                null
        );
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }


    public Cursor getLeaderboardData(String timePeriod) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT Member.first_name, Member.last_name, result.final_result, result.rank " +
                "FROM Member " +
                "INNER JOIN result ON Member.email = result.mem_email ";

        // Append the WHERE clause based on the time period
        switch (timePeriod) {
            case "Last 24 Hours":
                query += "WHERE result.date_time >= datetime('now', '-1 day') ";
                break;
            case "Last 7 Days":
                query += "WHERE result.date_time >= datetime('now', '-7 days') ";
                break;
            case "Last 30 Days":
                query += "WHERE result.date_time >= datetime('now', '-30 days') ";
                break;
            default:
                // No additional WHERE clause for "All Time"
                break;
        }

        query += "ORDER BY result.rank ASC";

        return db.rawQuery(query, null);
    }

    public Cursor getPoseDetails(String poseName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Correct_poses WHERE pose_name = ?";
        return db.rawQuery(query, new String[]{poseName});
    }
    public boolean updateUserProfile(String email, String newFirstName, String newLastName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("first_name", newFirstName);
        values.put("last_name", newLastName);


        int rowsAffected = db.update("Member", values, "email = ?", new String[]{email});
        db.close();
        return rowsAffected > 0; // Return true if the update was successful
    }
    public boolean deleteUserByEmail(String s_email) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete("Member", "email = ?", new String[]{s_email});
        return rowsDeleted > 0;
    }

    public String[] getUserDetails(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] userDetails = new String[2]; // Array to hold first name and last name

        Cursor cursor = db.rawQuery("SELECT first_name, last_name FROM Member WHERE email = ?", new String[]{email});
        if (cursor.moveToFirst()) {
            userDetails[0] = cursor.getString(0); // First name
            userDetails[1] = cursor.getString(1); // Last name
        }
        cursor.close();

        return userDetails;
    }
    public Cursor getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("Member", null, "email = ?", new String[]{email}, null, null, null);
    }

    public Cursor getUserDataWithRank(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT u.email, u.first_name, u.last_name, r.rank " +
                "FROM Member u " +
                "LEFT JOIN result r ON u.email = r.mem_email " +
                "WHERE u.email = ?";
        return db.rawQuery(query, new String[]{email});
    }

    // Method to get user's rank from leaderboard based on email
    public int getUserRank(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        int rank = -1;

        // Query to fetch the rank of the user based on email
        Cursor cursor = db.rawQuery("SELECT rank FROM result WHERE mem_email = ?", new String[]{userEmail});
        if (cursor != null && cursor.moveToFirst()) {
            // Check if the column exists
            int columnIndex = cursor.getColumnIndex("rank");
            if (columnIndex != -1) {
                rank = cursor.getInt(columnIndex);
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        return rank;
    }

}