package com.targeteducare.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper mInstance = null;
    public static final String DATABASE_NAME = "com.eduexam";
    public static final String TABLE_MSTEXAMINATION = "mst_examination";
    public static final String TABLE_QUESTION = "mst_question";
    public static final String TABLE_QUESTION1 = "mst_question1";
    public static final String TABLE_QUESTIONURL = "mst_questionurl";
    public static final String TABLE_ANSWER = "mst_answer";
    public static final String ID = "id";
    public static final String ID1 = "id1";
    public static final String SAVEDTIME = "SAVEDTIME";
    public static final String JSONDATA = "JSONDATA";
    public static final String ANSWERDATA = "ANSWERDATA";
    public static final String STARTTIMEOBJ = "STARTTIMEOBJ";
    public static final String LANGUAGE = "LANGUAGE";
    public static final String EXAMID = "EXAMID";
    public static final String SYNC = "sync";
    public static final String TYPE = "type";
    public static final String OFFLINEPATH = "offlinepath";
    public static final String IMAGESOURCE = "imagemainsource";

    static Context mcontext;

    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, 1);
        mcontext = context;
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        mcontext = context;
        if (mInstance == null) {
            mInstance = new DatabaseHelper(context);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query_examination = "CREATE TABLE IF NOT EXISTS " + TABLE_MSTEXAMINATION + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSONDATA + " TEXT," + SAVEDTIME + " TEXT)";
        db.execSQL(query_examination);

        String query_question = "CREATE TABLE IF NOT EXISTS " + TABLE_QUESTION + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + LANGUAGE + " String," + EXAMID + " INTEGER," + JSONDATA + " TEXT," + SAVEDTIME + " TEXT)";
        db.execSQL(query_question);

        String query_question1 = "CREATE TABLE IF NOT EXISTS " + TABLE_QUESTION1 + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + LANGUAGE + " TEXT," + EXAMID + " INTEGER," + JSONDATA + " TEXT," + SAVEDTIME + " TEXT)";
        db.execSQL(query_question1);

        String query_answer = "CREATE TABLE IF NOT EXISTS " + TABLE_ANSWER + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + ANSWERDATA + " TEXT," + STARTTIMEOBJ + " TEXT," + LANGUAGE + " TEXT," + EXAMID + " INTEGER," + JSONDATA + " TEXT," + SYNC + " INTEGER," + SAVEDTIME + " TEXT)";
        db.execSQL(query_answer);

        String query_questionurl = "CREATE TABLE IF NOT EXISTS " + TABLE_QUESTIONURL + "(" + ID1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + ID + " INTEGER," + TYPE + " TEXT," + IMAGESOURCE + " TEXT," + OFFLINEPATH + " TEXT," + SYNC + " INTEGER," + SAVEDTIME + " TEXT)";
        db.execSQL(query_questionurl);
    }

    public void createtablequestionurl() {
        SQLiteDatabase db = getWritableDatabase();
        String query_questionurl = "CREATE TABLE IF NOT EXISTS " + TABLE_QUESTIONURL + "(" + ID1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + ID + " INTEGER," + TYPE + " TEXT," + IMAGESOURCE + " TEXT," + OFFLINEPATH + " TEXT," + SYNC + " INTEGER," + SAVEDTIME + " TEXT)";
        db.execSQL(query_questionurl);
    }

    public JSONArray getquestionurl(int qid, String type) {
        createtablequestionurl();
        SQLiteDatabase db = getWritableDatabase();
        String searchQuery = "SELECT  * FROM " + TABLE_QUESTIONURL + " where " + ID + " = " + qid + " and " + TYPE + " = '" + type + "'";
        Log.e("query ", "query " + searchQuery);
        Cursor cursor = db.rawQuery(searchQuery, null);

        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();
        Log.e("resultSet ", "resultSet " + cursor.getCount() + " " + qid + " " + type);
        resultSet = convertcursorvaltoJSOnArray(cursor);
        cursor.close();
        return resultSet;
    }

    public void savequestionurl(ContentValues c, int id1, String type, String imageurl) {
        Log.e("qurldata ", "qurldata " + c.toString());
        createtablequestionurl();
        //deleteexamination();
        SQLiteDatabase db = getWritableDatabase();

        String query = "DELETE FROM " + TABLE_QUESTIONURL + " where " + ID + " = " + id1 + " and " + TYPE + " = '" + type + "' and " + IMAGESOURCE + " = '" + imageurl + "'";
        db.execSQL(query);
        long id = db.insertWithOnConflict(TABLE_QUESTIONURL, null, c, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void createtablemstexamination() {
        SQLiteDatabase db = getWritableDatabase();
        String query_examination = "CREATE TABLE IF NOT EXISTS " + TABLE_MSTEXAMINATION + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSONDATA + " TEXT," + SAVEDTIME + " TEXT)";
        db.execSQL(query_examination);
    }

    public void createtablequestion() {
        SQLiteDatabase db = getWritableDatabase();
        String query_question = "CREATE TABLE IF NOT EXISTS " + TABLE_QUESTION + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + LANGUAGE + " TEXT," + EXAMID + " INTEGER," + JSONDATA + " TEXT," + SAVEDTIME + " TEXT)";
        db.execSQL(query_question);
    }

    public void createtablequestion1() {
        SQLiteDatabase db = getWritableDatabase();
        String query_question = "CREATE TABLE IF NOT EXISTS " + TABLE_QUESTION1 + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + LANGUAGE + " TEXT," + EXAMID + " INTEGER," + JSONDATA + " TEXT," + SAVEDTIME + " TEXT)";
        db.execSQL(query_question);
    }

    public void createtableanswer() {
        SQLiteDatabase db = getWritableDatabase();
        String query_answer = "CREATE TABLE IF NOT EXISTS " + TABLE_ANSWER + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + ANSWERDATA + " TEXT," + STARTTIMEOBJ + " TEXT," + LANGUAGE + " TEXT," + EXAMID + " INTEGER," + JSONDATA + " TEXT," + SYNC + " INTEGER," + SAVEDTIME + " TEXT)";
        db.execSQL(query_answer);
    }

    public JSONArray getexaminationdata() {
        createtablemstexamination();
        SQLiteDatabase db = getWritableDatabase();
        String searchQuery = "SELECT  * FROM " + TABLE_MSTEXAMINATION;
        Log.e("query ", "query " + searchQuery);
        Cursor cursor = db.rawQuery(searchQuery, null);

        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();
        Log.e("data ", "data " + cursor.getCount());
        resultSet = convertcursorvaltoJSOnArray(cursor);
        cursor.close();
        return resultSet;
    }

    public JSONArray getqdata(int examid, String language) {
        createtablequestion();
        SQLiteDatabase db = getWritableDatabase();
        String searchQuery = "SELECT  * FROM " + TABLE_QUESTION + " where " + EXAMID + "=" + examid + " and " + LANGUAGE + " ='" + language + "'";
        Log.e("query ", "query " + searchQuery);
        Cursor cursor = db.rawQuery(searchQuery, null);
        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();
        Log.e("data ", "data " + cursor.getCount());
        resultSet = convertcursorvaltoJSOnArray(cursor);
        cursor.close();
        return resultSet;
    }

    public int getqdatacount(int examid, String language) {
        int count = 0;
        createtablequestion();
        SQLiteDatabase db = getWritableDatabase();
        String searchQuery = "SELECT  * FROM " + TABLE_QUESTION + " where " + EXAMID + "=" + examid + " and " + LANGUAGE + " ='" + language + "'";
        Log.e("query ", "query " + searchQuery);
        Cursor cursor = db.rawQuery(searchQuery, null);
        count = cursor.getCount();
        cursor.close();
        return count;
    }

    public JSONArray getqdata1(int examid, String language) {
        createtablequestion();
        SQLiteDatabase db = getWritableDatabase();
        String searchQuery = "SELECT  * FROM " + TABLE_QUESTION1 + " where " + EXAMID + "=" + examid + " and " + LANGUAGE + " ='" + language + "'";
        Log.e("query ", "query " + searchQuery);
        Cursor cursor = db.rawQuery(searchQuery, null);
        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();
        Log.e("data ", "data " + cursor.getCount());
        resultSet = convertcursorvaltoJSOnArray(cursor);
        cursor.close();
        return resultSet;
    }

    public int getqdata1count(int examid, String language) {
        createtablequestion();
        SQLiteDatabase db = getWritableDatabase();
        String searchQuery = "SELECT  * FROM " + TABLE_QUESTION1 + " where " + EXAMID + "=" + examid + " and " + LANGUAGE + " ='" + language + "'";
        Log.e("query ", "query " + searchQuery);
        Cursor cursor = db.rawQuery(searchQuery, null);
        int count = cursor.getCount();
        Log.e("count", "count " + count);
        cursor.close();
        return count;
    }

    public JSONArray getansdata(int examid, String language) {
        createtableanswer();
        SQLiteDatabase db = getWritableDatabase();
        String searchQuery = "SELECT  * FROM " + TABLE_ANSWER + " where " + EXAMID + "=" + examid + " and " + SYNC + "=" + 0 + " and " + LANGUAGE + " ='" + language + "'";
        Log.e("query ", "query " + searchQuery);
        Cursor cursor = db.rawQuery(searchQuery, null);
        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();
        Log.e("data ", "data " + cursor.getCount());
        resultSet = convertcursorvaltoJSOnArray(cursor);
        cursor.close();
        return resultSet;
    }

    public JSONArray getansdata1(int examid, String language) {
        createtableanswer();
        SQLiteDatabase db = getWritableDatabase();
        String searchQuery = "SELECT  * FROM " + TABLE_ANSWER + " where " + EXAMID + "=" + examid + " and " + LANGUAGE + " ='" + language + "'";
        Log.e("query ", "query " + searchQuery);
        Cursor cursor = db.rawQuery(searchQuery, null);
        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();
        Log.e("data ", "data " + cursor.getCount());
        resultSet = convertcursorvaltoJSOnArray(cursor);
        cursor.close();
        return resultSet;
    }

    public int getasyncdatacount(int examid, String language) {
        createtableanswer();
        SQLiteDatabase db = getWritableDatabase();
        String searchQuery = "SELECT  * FROM " + TABLE_ANSWER + " where " + EXAMID + "=" + examid + " and " + SYNC + "=" + 0 + " and " + LANGUAGE + " ='" + language + "'";
        Log.e("query ", "query " + searchQuery);
        Cursor cursor = db.rawQuery(searchQuery, null);
        int count = cursor.getCount();
        return count;
    }

    public void saveexaminationdata(ContentValues c) {
        createtablemstexamination();
        deleteexamination();
        SQLiteDatabase db = getWritableDatabase();
        long id = db.insertWithOnConflict(TABLE_MSTEXAMINATION, null, c, SQLiteDatabase.CONFLICT_REPLACE);
        Log.e("saved ", "saved " + id);
        getexaminationdata();
    }

    public void deleteexamination() {
        createtablemstexamination();
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_MSTEXAMINATION;
        db.execSQL(query);
    }

    public void deleteanswer() {
        createtableanswer();
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_ANSWER;
        db.execSQL(query);
    }

    public long saveqdata(ContentValues c, int examid, String language) {
        createtablequestion();
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_QUESTION + " where " + EXAMID + "=" + examid + " and " + LANGUAGE + " ='" + language + "'";
        db.execSQL(query);
        long id = db.insertWithOnConflict(TABLE_QUESTION, null, c, SQLiteDatabase.CONFLICT_REPLACE);
        return id;
    }

    public long saveqdata1(ContentValues c, int examid, String language) {
        Log.e("examid", "examid " + examid + " " + language);
        createtablequestion();
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_QUESTION1 + " where " + EXAMID + "=" + examid + " and " + LANGUAGE + " ='" + language + "'";
        db.execSQL(query);
        long id = db.insertWithOnConflict(TABLE_QUESTION1, null, c, SQLiteDatabase.CONFLICT_REPLACE);
        return id;
    }

    public void updateqdata(ContentValues c, int examid, String language) {
        Log.e("examid ", "" + examid + " " + language);
        createtablequestion();
        SQLiteDatabase db = getWritableDatabase();
        int i = db.update(TABLE_QUESTION, c, EXAMID + " = ? AND " + LANGUAGE + " = ? ", new String[]{Integer.toString(examid), language});
/*
        long id = db.insertWithOnConflict(TABLE_QUESTION, null, c, SQLiteDatabase.CONFLICT_REPLACE);
        return  id;*/
    }

    public void updatesync(long id) {
        createtableanswer();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(SYNC, 1);
        db.update(TABLE_ANSWER, c, ID + " = ? ", new String[]{Long.toString(id)});
    }

    public long saveanswerdata(ContentValues c) {
        createtableanswer();
        SQLiteDatabase db = getWritableDatabase();
        long id = db.insertWithOnConflict(TABLE_ANSWER, null, c, SQLiteDatabase.CONFLICT_REPLACE);
        Log.e("id ", "id " + c.toString());
        return id;
    }

    public JSONArray convertcursorvaltoJSOnArray(Cursor cursor) {
        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();
            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        if (cursor.getString(i) != null) {
                            Log.d("TAG_NAME", cursor.getString(i));
                            rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                        } else {
                            rowObject.put(cursor.getColumnName(i), "");
                        }
                    } catch (Exception e) {
                        Log.d("TAG_NAME", e.getMessage());
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }
        cursor.close();
        Log.e("data ", resultSet.toString());
        return resultSet;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long getsize() {
        File dbpath = mcontext.getDatabasePath(DATABASE_NAME);
        SQLiteDatabase db = getWritableDatabase();
        Log.e("dbpath ", "dbpath " + dbpath + "  " + db.getPath());
        return new File(db.getPath()).length();
    }

}
