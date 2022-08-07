package com.example.quizzsport.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quizzsport.model.Question;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class MyDB extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "quizz.db";
    private static final int DATABASE_VERSION = 7;
    public static String TABLE_NAME = "sport";

    public MyDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade();
    }

    public List<Question> getQuestionsFromDB(){
        SQLiteDatabase db = getReadableDatabase();

        List<Question> questions = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME, null, null, null,
                null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(Constants.COLUMN_NAME_QUESTION));
            @SuppressLint("Range") String answer1 = cursor.getString(cursor.getColumnIndex(Constants.COLUMN_NAME_ANSWER1));
            @SuppressLint("Range") String answer2 = cursor.getString(cursor.getColumnIndex(Constants.COLUMN_NAME_ANSWER2));
            @SuppressLint("Range") String answer3 = cursor.getString(cursor.getColumnIndex(Constants.COLUMN_NAME_ANSWER3));
            @SuppressLint("Range") String answer4 = cursor.getString(cursor.getColumnIndex(Constants.COLUMN_NAME_ANSWER4));

            Question question = new Question(description, answer1, answer2, answer3, answer4);
            questions.add(question);

        }

        cursor.close();
        return questions;
    }

    public void setTable(String table){
        TABLE_NAME = table;
    }
}
