package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserDatabase.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_MEMBER = "Member";
    private static final String TABLE_CORRECT_POSE = "CorrectPose";
    private static final String TABLE_RESULT = "Result";

    // Columns for the Member table
    private static final String COLUMN_MEMBER_ID = "id";
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    // Columns for the Correct Pose table
    private static final String COLUMN_CORRECT_POSE_ID = "pose_id";
    private static final String COLUMN_CORRECT_POSE_NAME = "pose_name";
    private static final String COLUMN_CORRECT_POSE_DETAILS = "pose_details";
    private static final String COLUMN_CORRECT_POSE_IMAGE_LINK = "image_link";

    // Columns for the Result table
    private static final String COLUMN_RESULT_ID = "result_id";
    private static final String COLUMN_VIDEO_NAME = "video_name";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_FINAL_RESULT = "final_result";
    private static final String COLUMN_RANK = "rank";
    private static final String COLUMN_PLAYER_EMAIL = "player_email";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Member table
        String CREATE_MEMBER_TABLE = "CREATE TABLE " + TABLE_MEMBER + "("
                + COLUMN_MEMBER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_FIRST_NAME + " TEXT,"
                + COLUMN_LAST_NAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_MEMBER_TABLE);

        // Create Correct Pose table
        String CREATE_CORRECT_POSE_TABLE = "CREATE TABLE " + TABLE_CORRECT_POSE + "("
                + COLUMN_CORRECT_POSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CORRECT_POSE_NAME + " TEXT,"
                + COLUMN_CORRECT_POSE_DETAILS + " TEXT,"
                + COLUMN_CORRECT_POSE_IMAGE_LINK + " TEXT" + ")";
        db.execSQL(CREATE_CORRECT_POSE_TABLE);

        // Create Result table
        String CREATE_RESULT_TABLE = "CREATE TABLE " + TABLE_RESULT + "("
                + COLUMN_RESULT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_VIDEO_NAME + " TEXT,"
                + COLUMN_DATE + " DATE,"
                + COLUMN_FINAL_RESULT + " DECIMAL(5,2),"
                + COLUMN_RANK + " VARCHAR(255),"
                + COLUMN_PLAYER_EMAIL + " TEXT,"
                + "FOREIGN KEY (" + COLUMN_PLAYER_EMAIL + ") REFERENCES " + TABLE_MEMBER + "(" + COLUMN_EMAIL + ")"
                + ")";
        db.execSQL(CREATE_RESULT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CORRECT_POSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESULT);
        onCreate(db);
    }

    public boolean addNewMember(String sFname, String sLname, String sEmail, String sPassword) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (isEmailExists(sEmail)) {
            return false; // Email already exists
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, sFname);
        values.put(COLUMN_LAST_NAME, sLname);
        values.put(COLUMN_EMAIL, sEmail);
        values.put(COLUMN_PASSWORD, sPassword);

        long result = db.insert(TABLE_MEMBER, null, values);
        db.close();

        return result != -1;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MEMBER, new String[]{COLUMN_MEMBER_ID}, COLUMN_EMAIL + "=?", new String[]{email}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MEMBER, new String[]{COLUMN_MEMBER_ID}, COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?", new String[]{email, password}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public boolean addNewCorrectPose(String poseName, String poseDetails, String imageLink) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CORRECT_POSE_NAME, poseName);
        values.put(COLUMN_CORRECT_POSE_DETAILS, poseDetails);
        values.put(COLUMN_CORRECT_POSE_IMAGE_LINK, imageLink);

        long result = db.insert(TABLE_CORRECT_POSE, null, values);
        db.close();

        return result != -1;
    }

    public void addAllCorrectPoses() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Add all 19 pose names, details, and image links here
        String[] poseNames = {
                "zenkutsuDachi_awaseTsuki(leftLeg)",
                "shikoDachi_gedanBarai(front)",
                "zenkutsuDachi_empiUke(right)",
                "zenkitsuDahi_empiUke(left)",
                "zenkutsuDachi_chudanTsuki",
                "shikoDach_gedaiBarai(left)",
                "shikoDachi_GedanBarai(right)",
                "zenkutsuDachi_awaseTsuki(rightLeg)",
                "sotoUke_maeGeri(right)",
                "sotoUke_maeGeri(left)",
                "motoDachi_sotoUke(left2)",
                "motoDachi_sotoUke(right)",
                "motoDachi_sotoUke(left)",
                "motoDachi_jodanTsuki(left)",
                "motoDachi_jodanTsuki(right)",
                "motoDachi_ageUke(right)",
                "motoDachi_ageUke(left)",
                "hachijiDachi_jodanYoko(left)",
                "hachijiDachi_jidanYoko(right)"
        };

        String[] poseDetails = {
                "Begin in a front stance with your left leg forward. Simultaneously execute a punch with both fists towards the opponent's midsection.",
                "Transition into a sumo stance with your feet wider apart than shoulder-width. Perform a downward block with your arms to defend against low-level attacks.",
                "Return to a front stance, this time with your right leg forward. Execute an empi uke, a forearm block or strike, with your right arm.",
                "Maintain the front stance but switch to your left leg forward and execute an empi uke with your left arm.",
                "Stay in the front stance and deliver a chudan tsuki, a middle-level punch directed towards the opponent's torso.",
                "Switch to a sumo stance with the opposite leg forward. Perform a gedan barai, a downward block, with your arms to the left side.",
                "Switch to a sumo stance with the opposite leg forward. Perform a gedan barai, a downward block, with your arms to the right side.",
                "Return to the front stance, this time with your right leg forward, and execute a punch with both fists simultaneously.",
                "From the front stance, perform a soto uke, an outer block, followed by a mae geri, a front kick, using your right leg.",
                "From the front stance, perform a soto uke, an outer block, followed by a mae geri, a front kick, using your left leg.",
                "Transition to a natural stance and execute a soto uke, likely with your left arm.",
                "Transition to a natural stance and execute a soto uke, likely with your right arm.",
                "Another soto uke performed from the natural stance, likely with your left arm.",
                "From the natural stance, deliver a jodan tsuki, a high-level punch, using your left arm.",
                "Repeat the previous movement but with your right arm.",
                "Execute an age uke, an upper block, likely with your right arm from the natural stance.",
                "Similar to the previous movement but performed with your left arm.",
                "Transition to a stance called hachiji-dachi and perform a jodan yoko, likely a high-level side strike or block, directed to the left side.",
                "Similar to the previous pose but performed to the right side."
        };

        String[] imageLinks = {
                "hachiji_dachi_jidan_yoko_right",
                "hachiji_dachi_jodan_yoko_left",
                "moto_dachi_age_uke_left",
                "moto_dachi_age_uke_right",
                "moto_dachi_jodan_tsuki_left",
                "moto_dachi_jodan_tsuki_right",
                "moto_dachi_soto_uke_left",
                "moto_dachi_soto_uke_left2",
                "moto_dachi_soto_uke_right",
                "shiko_dach_gedai_barai_left",
                "shiko_dachi_gedan_barai_front",
                "shiko_dachi_gedan_barai_right",
                "soto_uke_mae_geri_left",
                "soto_uke_mae_geri_right",
                "zenkitsu_dahi_empi_uke_left",
                "zenkutsu_dachi_awase_tsuki_leftLeg",
                "zenkutsu_dachi_awase_tsuki_rightLeg",
                "zenkutsu_dachi_chudan_tsuki",
                "zenkutsu_dachi_empi_uke_right"
        };

        for (int i = 0; i < poseNames.length; i++) {
            addNewCorrectPose(poseNames[i], poseDetails[i], imageLinks[i]);
        }

        db.close();
    }

    public Cursor getResults() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_RESULT, null);
    }

    // Methods to add, retrieve, update, and delete records from the Correct Pose and Result tables
}
