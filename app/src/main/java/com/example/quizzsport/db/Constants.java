package com.example.quizzsport.db;

public class Constants {
    public static final String DB_PATH = "/data/user/0/com.example.quizzsport/databases/";
    public static final String TABLE_NAME = "sport";
    public static final String _ID = "_id";
    public static final String COLUMN_NAME_QUESTION = "description";
    public static final String COLUMN_NAME_ANSWER1 = "answer1";
    public static final String COLUMN_NAME_ANSWER2 = "answer2";
    public static final String COLUMN_NAME_ANSWER3 = "answer3";
    public static final String COLUMN_NAME_ANSWER4 = "answer4";
    public static final int DB_VERSION = 2;
    public static final String DB_NAME = "quizz.db";
    public static final String TABLE_STRUCTURE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY,"
            + COLUMN_NAME_QUESTION + " TEXT,"
            + COLUMN_NAME_ANSWER1 + " TEXT,"
            + COLUMN_NAME_ANSWER2 + " TEXT,"
            + COLUMN_NAME_ANSWER3 + " TEXT,"
            + COLUMN_NAME_ANSWER4 + " TEXT)";
    public static final String DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}
