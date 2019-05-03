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
import java.lang.reflect.Type;
import java.util.ArrayList;

public class PracticeDatabaseHelper extends SQLiteOpenHelper {

    private static PracticeDatabaseHelper mInstance = null;
    public static final String PRACTICE_DATABASE_NAME = "com.eduexampractice";
    public static final String PRACTICE_TABLE_MSTEXAMINATION = "mst_examinationpractice";
    public static final String PRACTICE_TABLE_MSTEXAMINATIONDETAILS = "mst_examinationdetailspractice";
    public static final String PRACTICE_TABLE_QUESTION = "mst_questionpractice";
    public static final String PRACTICE_TABLE_QUESTION1 = "mst_question1practice";
    public static final String PRACTICE_TABLE_QUESTIONURL = "mst_questionurlpractice";
    public static final String PRACTICE_TABLE_ANSWER = "mst_answerpractice";
    public static final String PRACTICE_ID = "idpractice";
    public static final String PRACTICE_ID1 = "id1practice";
    public static final String PRACTICE_SAVEDTIME = "PRACTICE_SAVEDTIME";
    public static final String PRACTICE_JSONDATA = "PRACTICE_JSONDATA";
    public static final String PRACTICE_ANSWERDATA = "PRACTICE_ANSWERDATA";
    public static final String PRACTICE_STARTTIMEOBJ = "PRACTICE_STARTTIMEOBJ";
    public static final String PRACTICE_LANGUAGE = "PRACTICE_LANGUAGE";
    public static final String EXAMPRACTICE_ID = "EXAMPRACTICE_ID";
    public static final String PRACTICE_SYNC = "syncpractice";
    public static final String PRACTICE_TYPE = "typepractice";
    public static final String PRACTICE_OFFLINEPATH = "offlinepathpractice";
    public static final String PRACTICE_IMAGESOURCE = "imagemainsourcepractice";

    public static final String PRACTICE_PROGRESS = "progresspractice";
    public static final String PRACTICE_ANSWERED = "answeredpractice";
    public static final String PRACTICE_CORRECT = "correctpractice";
    public static final String PRACTICE_WRONG = "wrongpractice";
    public static final String PRACTICE_SKIPP = "skipppractice";
    public static final String PRACTICE_SPEED = "speedpractice";
    public static final String PRACTICE_TIMETAKEN = "timetakenpractice";
    public static final String EXAMPRACTICE_TYPE = "examtypepractice";
    public static final String PRACTICE_QUESTION = "questionpractice";


    static Context mcontext;

    public PracticeDatabaseHelper(Context context) {

        super(context, PRACTICE_DATABASE_NAME, null, 1);
        mcontext = context;
    }

    public static synchronized PracticeDatabaseHelper getInstance(Context context) {
        mcontext = context;
        if (mInstance == null) {
            mInstance = new PracticeDatabaseHelper(context);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query_examination = "CREATE TABLE IF NOT EXISTS " + PRACTICE_TABLE_MSTEXAMINATION + "("
                + PRACTICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + PRACTICE_JSONDATA + " TEXT," + PRACTICE_SAVEDTIME + " TEXT)";
        db.execSQL(query_examination);


        String query_examinationdetails = "CREATE TABLE IF NOT EXISTS " + PRACTICE_TABLE_MSTEXAMINATIONDETAILS + "("
                + PRACTICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + EXAMPRACTICE_ID + " TEXT,"+ PRACTICE_PROGRESS + " TEXT," +PRACTICE_ANSWERED + " TEXT," +PRACTICE_CORRECT + " TEXT,"+ PRACTICE_WRONG + " TEXT," +PRACTICE_SKIPP + " TEXT," +PRACTICE_SPEED + " TEXT," +PRACTICE_TIMETAKEN + " TEXT,"+EXAMPRACTICE_TYPE + " TEXT," +PRACTICE_SAVEDTIME + " TEXT)";
        db.execSQL(query_examinationdetails);


        String query_question = "CREATE TABLE IF NOT EXISTS " + PRACTICE_TABLE_QUESTION + "("
                + PRACTICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + PRACTICE_LANGUAGE + " String," + EXAMPRACTICE_ID + " INTEGER," + PRACTICE_JSONDATA + " TEXT," + PRACTICE_SAVEDTIME + " TEXT)";
        db.execSQL(query_question);

        String query_question1 = "CREATE TABLE IF NOT EXISTS " + PRACTICE_TABLE_QUESTION1 + "("
                + PRACTICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + PRACTICE_LANGUAGE + " TEXT," + EXAMPRACTICE_ID + " INTEGER," + PRACTICE_JSONDATA + " TEXT," + PRACTICE_SAVEDTIME + " TEXT)";
        db.execSQL(query_question1);

        String query_answer = "CREATE TABLE IF NOT EXISTS " + PRACTICE_TABLE_ANSWER + "(" + PRACTICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + PRACTICE_ANSWERDATA + " TEXT," + PRACTICE_STARTTIMEOBJ + " TEXT," + PRACTICE_LANGUAGE + " TEXT," + EXAMPRACTICE_ID + " INTEGER," + PRACTICE_JSONDATA + " TEXT," + PRACTICE_SYNC + " INTEGER," + PRACTICE_SAVEDTIME + " TEXT)";
        db.execSQL(query_answer);

        String query_questionurl = "CREATE TABLE IF NOT EXISTS " + PRACTICE_TABLE_QUESTIONURL + "(" + PRACTICE_ID1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + PRACTICE_ID + " INTEGER," + PRACTICE_TYPE + " TEXT," + PRACTICE_IMAGESOURCE + " TEXT," + PRACTICE_OFFLINEPATH + " TEXT," + PRACTICE_SYNC + " INTEGER," + PRACTICE_SAVEDTIME + " TEXT)";
        db.execSQL(query_questionurl);
    }

    public  void createtableexaminationdetails(){
        try {
            SQLiteDatabase db = getWritableDatabase();

            String query_examinationdetails = "CREATE TABLE IF NOT EXISTS " + PRACTICE_TABLE_MSTEXAMINATIONDETAILS + "("
                    + PRACTICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + EXAMPRACTICE_ID + " TEXT," + PRACTICE_PROGRESS + " TEXT," + PRACTICE_ANSWERED + " TEXT," + PRACTICE_CORRECT + " TEXT," + PRACTICE_WRONG + " TEXT," + PRACTICE_SKIPP + " TEXT," + PRACTICE_SPEED + " TEXT," + PRACTICE_TIMETAKEN + " TEXT," + EXAMPRACTICE_TYPE + " TEXT," + PRACTICE_SAVEDTIME + " TEXT)";
            db.execSQL(query_examinationdetails);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void saveexaminationdetails(ContentValues c,int examid){
        createtableexaminationdetails();
        SQLiteDatabase db = getWritableDatabase();
        String query="Select * from "+PRACTICE_TABLE_MSTEXAMINATIONDETAILS+" where "+EXAMPRACTICE_ID+" = '"+examid+"'";
        Cursor cursor=db.rawQuery(query,null);
        if(cursor.getCount()>0)
        {
            db.update(PRACTICE_TABLE_MSTEXAMINATIONDETAILS, c, EXAMPRACTICE_ID + " =?", new String[]{Integer.toString(examid)});
        }else {
            long id = db.insertWithOnConflict(PRACTICE_TABLE_MSTEXAMINATIONDETAILS, null, c, SQLiteDatabase.CONFLICT_REPLACE);
        }

        getexaminationdata();
    }

    public JSONArray getexamdetails(int examid, String type) {
        createtablequestionurl();
        SQLiteDatabase db = getWritableDatabase();
        String searchQuery = "SELECT  * FROM " + PRACTICE_TABLE_MSTEXAMINATIONDETAILS + " where " + EXAMPRACTICE_ID + " = '" + examid + "' and " + EXAMPRACTICE_TYPE + " = '" + type + "'";
        Log.e("query ", "query " + searchQuery);
        Cursor cursor = db.rawQuery(searchQuery, null);

        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();
        Log.e("resultSet ", "resultSet " + cursor.getCount() + " " + examid + " " + type);
        resultSet = convertcursorvaltoJSOnArray(cursor);
        cursor.close();
        return resultSet;
    }

    public void createtablequestionurl() {
        SQLiteDatabase db = getWritableDatabase();
        String query_questionurl = "CREATE TABLE IF NOT EXISTS " + PRACTICE_TABLE_QUESTIONURL + "(" + PRACTICE_ID1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + PRACTICE_ID + " INTEGER," + PRACTICE_TYPE + " TEXT," + PRACTICE_IMAGESOURCE + " TEXT," + PRACTICE_OFFLINEPATH + " TEXT," + PRACTICE_SYNC + " INTEGER," + PRACTICE_SAVEDTIME + " TEXT)";
        db.execSQL(query_questionurl);
    }

    public JSONArray getquestionurl(int qid, String type) {
        createtablequestionurl();
        SQLiteDatabase db = getWritableDatabase();
        String searchQuery = "SELECT  * FROM " + PRACTICE_TABLE_QUESTIONURL + " where " + PRACTICE_ID + " = " + qid + " and " + PRACTICE_TYPE + " = '" + type + "'";
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

        String query = "DELETE FROM " + PRACTICE_TABLE_QUESTIONURL + " where " + PRACTICE_ID + " = " + id1 + " and " + PRACTICE_TYPE + " = '" + type + "' and " + PRACTICE_IMAGESOURCE + " = '" + imageurl + "'";
        db.execSQL(query);
        long id = db.insertWithOnConflict(PRACTICE_TABLE_QUESTIONURL, null, c, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void createtablemstexamination() {
        SQLiteDatabase db = getWritableDatabase();
        String query_examination = "CREATE TABLE IF NOT EXISTS " + PRACTICE_TABLE_MSTEXAMINATION + "("
                + PRACTICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + PRACTICE_JSONDATA + " TEXT," + PRACTICE_SAVEDTIME + " TEXT)";
        db.execSQL(query_examination);
    }

    public void createtablequestion() {
        SQLiteDatabase db = getWritableDatabase();
        String query_question = "CREATE TABLE IF NOT EXISTS " + PRACTICE_TABLE_QUESTION + "("
                + PRACTICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + PRACTICE_LANGUAGE + " TEXT," + EXAMPRACTICE_ID + " INTEGER," + PRACTICE_JSONDATA + " TEXT," + PRACTICE_SAVEDTIME + " TEXT)";
        db.execSQL(query_question);
    }

    public void createtablequestion1() {
        SQLiteDatabase db = getWritableDatabase();
        String query_question = "CREATE TABLE IF NOT EXISTS " + PRACTICE_TABLE_QUESTION1 + "("
                + PRACTICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + PRACTICE_LANGUAGE + " TEXT," + EXAMPRACTICE_ID + " INTEGER," + PRACTICE_JSONDATA + " TEXT," + PRACTICE_SAVEDTIME + " TEXT)";
        db.execSQL(query_question);
    }

    public void createtableanswer() {
        SQLiteDatabase db = getWritableDatabase();
        String query_answer = "CREATE TABLE IF NOT EXISTS " + PRACTICE_TABLE_ANSWER + "(" + PRACTICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + PRACTICE_ANSWERDATA + " TEXT," + PRACTICE_STARTTIMEOBJ + " TEXT," + PRACTICE_LANGUAGE + " TEXT," + EXAMPRACTICE_ID + " INTEGER," + PRACTICE_JSONDATA + " TEXT," + PRACTICE_SYNC + " INTEGER," + PRACTICE_SAVEDTIME + " TEXT)";
        db.execSQL(query_answer);
    }

    public JSONArray getexaminationdata() {
        createtablemstexamination();
        SQLiteDatabase db = getWritableDatabase();
        String searchQuery = "SELECT  * FROM " + PRACTICE_TABLE_MSTEXAMINATION;
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
        String searchQuery = "SELECT  * FROM " + PRACTICE_TABLE_QUESTION + " where " + EXAMPRACTICE_ID + "=" + examid + " and " + PRACTICE_LANGUAGE + " ='" + language + "'";
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
        String searchQuery = "SELECT  * FROM " + PRACTICE_TABLE_QUESTION + " where " + EXAMPRACTICE_ID + "=" + examid + " and " + PRACTICE_LANGUAGE + " ='" + language + "'";
        Log.e("query ", "query " + searchQuery);
        Cursor cursor = db.rawQuery(searchQuery, null);
        count = cursor.getCount();
        cursor.close();
        return count;
    }

    public JSONArray getqdata1(int examid, String language) {
        createtablequestion();
        SQLiteDatabase db = getWritableDatabase();
        String searchQuery = "SELECT  * FROM " + PRACTICE_TABLE_QUESTION1 + " where " + EXAMPRACTICE_ID + "=" + examid + " and " + PRACTICE_LANGUAGE + " ='" + language + "'";
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
        String searchQuery = "SELECT  * FROM " + PRACTICE_TABLE_QUESTION1 + " where " + EXAMPRACTICE_ID + "=" + examid + " and " + PRACTICE_LANGUAGE + " ='" + language + "'";
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
        String searchQuery = "SELECT  * FROM " + PRACTICE_TABLE_ANSWER + " where " + EXAMPRACTICE_ID + "=" + examid + " and " + PRACTICE_SYNC + "=" + 0 + " and " + PRACTICE_LANGUAGE + " ='" + language + "'";
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
        String searchQuery = "SELECT  * FROM " + PRACTICE_TABLE_ANSWER + " where " + EXAMPRACTICE_ID + "=" + examid + " and " + PRACTICE_LANGUAGE + " ='" + language + "'";
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
        String searchQuery = "SELECT  * FROM " + PRACTICE_TABLE_ANSWER + " where " + EXAMPRACTICE_ID + "=" + examid + " and " + PRACTICE_SYNC + "=" + 0 + " and " + PRACTICE_LANGUAGE + " ='" + language + "'";
        Log.e("query ", "query " + searchQuery);
        Cursor cursor = db.rawQuery(searchQuery, null);
        int count = cursor.getCount();
        return count;
    }

    public void saveexaminationdata(ContentValues c) {
        createtablemstexamination();
        deleteexamination();
        SQLiteDatabase db = getWritableDatabase();
        long id = db.insertWithOnConflict(PRACTICE_TABLE_MSTEXAMINATION, null, c, SQLiteDatabase.CONFLICT_REPLACE);
        Log.e("saved ", "saved " + id);
        getexaminationdata();
    }

    public void deleteexamination() {
        createtablemstexamination();
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + PRACTICE_TABLE_MSTEXAMINATION;
        db.execSQL(query);
    }

    public void deleteanswer() {
        createtableanswer();
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + PRACTICE_TABLE_ANSWER;
        db.execSQL(query);
    }

    public long saveqdata(ContentValues c, int examid, String language) {
        createtablequestion();
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + PRACTICE_TABLE_QUESTION + " where " + EXAMPRACTICE_ID + "=" + examid + " and " + PRACTICE_LANGUAGE + " ='" + language + "'";
        db.execSQL(query);
        long id = db.insertWithOnConflict(PRACTICE_TABLE_QUESTION, null, c, SQLiteDatabase.CONFLICT_REPLACE);
        return id;
    }

    public long saveqdata1(ContentValues c, int examid, String language) {
        Log.e("examid", "examid " + examid + " " + language);
        createtablequestion();
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + PRACTICE_TABLE_QUESTION1 + " where " + EXAMPRACTICE_ID + "=" + examid + " and " + PRACTICE_LANGUAGE + " ='" + language + "'";
        db.execSQL(query);
        long id = db.insertWithOnConflict(PRACTICE_TABLE_QUESTION1, null, c, SQLiteDatabase.CONFLICT_REPLACE);
        return id;
    }

    public void updateqdata(ContentValues c, int examid, String language) {
        Log.e("examid ", "" + examid + " " + language);
        createtablequestion();
        SQLiteDatabase db = getWritableDatabase();
        int i = db.update(PRACTICE_TABLE_QUESTION, c, EXAMPRACTICE_ID + " = ? AND " + PRACTICE_LANGUAGE + " = ? ", new String[]{Integer.toString(examid), language});
/*
        long id = db.insertWithOnConflict(PRACTICE_TABLE_QUESTION, null, c, SQLiteDatabase.CONFLICT_REPLACE);
        return  id;*/
    }

    public void updatesync(long id) {
        createtableanswer();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(PRACTICE_SYNC, 1);
        db.update(PRACTICE_TABLE_ANSWER, c, PRACTICE_ID + " = ? ", new String[]{Long.toString(id)});
    }

    public long saveanswerdata(ContentValues c) {
        createtableanswer();
        SQLiteDatabase db = getWritableDatabase();
        long id = db.insertWithOnConflict(PRACTICE_TABLE_ANSWER, null, c, SQLiteDatabase.CONFLICT_REPLACE);
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
        File dbpath = mcontext.getDatabasePath(PRACTICE_DATABASE_NAME);
        SQLiteDatabase db = getWritableDatabase();
        Log.e("dbpath ", "dbpath " + dbpath + "  " + db.getPath());
        return new File(db.getPath()).length();
    }

}
