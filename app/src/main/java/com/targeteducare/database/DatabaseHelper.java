package com.targeteducare.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.targeteducare.Classes.Course;
import com.targeteducare.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper mInstance = null;
    public static final String DATABASE_NAME = "com.eduexam";
    public static final String TABLE_MSTEXAMINATION = "mst_examination";

    public static final String TABLE_COURSE_PEAK = "mst_courseandpeak";

    public static final String TABLE_MSTEXAMINATION_PRACTICETEST = "mst_examination_practicetest";
    public static final String TABLE_MSTEXAMINATIONv1 = "mst_examinationv1";
    public static final String TABLE_MSTSELECTEDANS = "mst_nselectedans";
    public static final String TABLE_MSTEXAMINATIONDETAILS = "mst_examinationdetails";
    public static final String TABLE_QUESTION = "mst_question";
    public static final String TABLE_OROGINALQUESTIONDATA = "mst_questionoriginaldata";
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
    public static final String QID = "qid";
    public static final String COURSENAME = "course_name";
    public static final String COURSENAMEINMARATHI = "coursename_inmarathi";
    public static final String PEAKNAME = "peakname";
    public static final String COURSEID = "course_id";
    public static final String SYNC = "sync";
    public static final String TYPE = "type";
    public static final String OFFLINEPATH = "offlinepath";
    public static final String IMAGESOURCE = "imagemainsource";
    public static final String PROGRESS = "progress";
    public static final String ANSWERED = "answered";
    public static final String CORRECT = "correct";
    public static final String WRONG = "wrong";
    public static final String SKIPP = "skipp";
    public static final String SPEED = "speed";
    public static final String TIMETAKEN = "timetaken";
    public static final String EXAMTYPE = "examtype";
    public static final String QUESTION = "question";
    public static final String ANS = "ans";
    public static final String FEEDBACK = "feedback";
    public static final String EVENT_ID = "event_id";
    public static final String IS_SUBMIT = "submit";
    static Context mcontext;
    public static final String SPLASH_DATA = "SPLASHDATA";
    //public static final String TIMETAKENPERQUESTION = "timetakenperquestion";
    public static final String TokenId = "TokenId";
    public static final String StudentId = "StudentId";
    public static final String Table = "Chat";
    public static final String Table_Splash = "Splash";
    public static final String SubmissionDate = "SubmissionDate";
    public static final String Title = "Title";
    public static final String Status = "Status";
    public static final String CreatedDate = "CreatedDate";
    public static final String FileUrl = "FileUrl";
    public static final String CategoryId = "CategoryId";
    public static final String TABLE_NOTIFICATION = "table_notification";
    public static final String TABLE_CHATNOTIFICATION = "table_chatnotification";
    public static final String READ = "read";

    public static final String TABLE_PROMOTION = "table_promotion";
    public static final String PROMOTION_ID = "promotion_id";
    public static final String LAST_QID = "lastqid";

    public static final String TABLE_SUBJECTS = "ebook_subjects";

    public static final String TABLE_EBOOKCONTENTDETAILS = "ebookcontentdetails";

    public static final String TABLE_EBOOKPAGES = "ebook_pagedetails";
    public static final String EBOOK_ID = "ebookid";
    public static final String PAGE_ID = "pageid";
    public static final String BOOKMARK =  "bookmark";
    public static final String FAVORITE =  "favorite";
    public static final String LAST_VISITED_PAGE= "lastvisitedpage";



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
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSONDATA + " TEXT," + TYPE + " TEXT," + COURSENAME + " TEXT, " + COURSEID + " TEXT, " + COURSENAMEINMARATHI + " TEXT, " + SAVEDTIME + " TEXT)";
        db.execSQL(query_examination);

        String query_examination_practicetest = "CREATE TABLE IF NOT EXISTS " + TABLE_MSTEXAMINATION_PRACTICETEST + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSONDATA + " TEXT," + TYPE + " TEXT," + COURSENAME + " TEXT, " + COURSEID + " TEXT, " + COURSENAMEINMARATHI + " TEXT, " + EXAMID + " INTEGER, " + PEAKNAME + " TEXT, " + SAVEDTIME + " TEXT)";
        db.execSQL(query_examination_practicetest);

        String query_examination1 = "CREATE TABLE IF NOT EXISTS " + TABLE_MSTEXAMINATIONv1 + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSONDATA + " TEXT," + TYPE + " TEXT," + COURSENAME + " TEXT, " + COURSEID + " TEXT, " + COURSENAMEINMARATHI + " TEXT, " + EXAMID + " INTEGER, " + SAVEDTIME + " TEXT)";
        db.execSQL(query_examination1);

        /*String query_examinationdetails = "CREATE TABLE IF NOT EXISTS " + TABLE_MSTEXAMINATIONDETAILS + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + EXAMID + " TEXT,"+ PROGRESS + " TEXT," +ANSWERED + " TEXT," +CORRECT + " TEXT,"+ WRONG + " TEXT," +SKIPP + " TEXT," +SPEED + " TEXT," +TIMETAKEN + " TEXT,"+EXAMTYPE + " TEXT," +SAVEDTIME + " TEXT," + TIMETAKENPERQUESTION + " TEXT)";
        db.execSQL(query_examinationdetails);*/

        String query_examinationdetails = "CREATE TABLE IF NOT EXISTS " + TABLE_MSTEXAMINATIONDETAILS + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + EXAMID + " TEXT," + PROGRESS + " TEXT," + ANSWERED + " TEXT," + CORRECT + " TEXT," + WRONG + " TEXT," + SKIPP + " TEXT," + SPEED + " TEXT," + TIMETAKEN + " TEXT, " + EXAMTYPE + " TEXT, " + QUESTION + " TEXT, " + LAST_QID + " INTEGER, " + SAVEDTIME + " TEXT)";
        db.execSQL(query_examinationdetails);


        String query_question = "CREATE TABLE IF NOT EXISTS " + TABLE_QUESTION + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + LANGUAGE + " String," + EXAMID + " INTEGER," + JSONDATA + " TEXT," + SAVEDTIME + " TEXT)";
        db.execSQL(query_question);

        String query_question1 = "CREATE TABLE IF NOT EXISTS " + TABLE_OROGINALQUESTIONDATA + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + LANGUAGE + " TEXT," + EXAMID + " INTEGER," + JSONDATA + " TEXT," + SAVEDTIME + " TEXT)";
        db.execSQL(query_question1);

        String query_answer = "CREATE TABLE IF NOT EXISTS " + TABLE_ANSWER + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + ANSWERDATA + " TEXT," + STARTTIMEOBJ + " TEXT," + LANGUAGE + " TEXT," + EXAMID + " INTEGER," + JSONDATA + " TEXT," + SYNC + " INTEGER," + SAVEDTIME + " TEXT)";
        db.execSQL(query_answer);

        String query_questionurl = "CREATE TABLE IF NOT EXISTS " + TABLE_QUESTIONURL + "(" + ID1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + ID + " INTEGER," + TYPE + " TEXT," + IMAGESOURCE + " TEXT," + OFFLINEPATH + " TEXT," + SYNC + " INTEGER," + EXAMID + " INTEGER," + SAVEDTIME + " TEXT)";
        db.execSQL(query_questionurl);

        String query_questionansdata = "CREATE TABLE IF NOT EXISTS " + TABLE_MSTSELECTEDANS + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + QID + " INTEGER," + ANS + " TEXT," + EXAMID + " TEXT," + SAVEDTIME + " TEXT)";
        db.execSQL(query_questionansdata);

        String feedback = "CREATE TABLE IF NOT EXISTS " + FEEDBACK + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSONDATA + " TEXT," + EVENT_ID + " TEXT, " + IS_SUBMIT + " TEXT, " + SAVEDTIME + " TEXT)";
        db.execSQL(feedback);

        String query_chat = "CREATE TABLE IF NOT EXISTS   " + Table + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TokenId + " TEXT," + JSONDATA + " VARCHAR2," + SubmissionDate + " TEXT," + Title + " TEXT," + Status + " TEXT," + CreatedDate + " TEXT," + FileUrl + " TEXT," + StudentId + " TEXT," + CategoryId + " TEXT)";
        db.execSQL(query_chat);

        String query_splash = "CREATE TABLE IF NOT EXISTS " + Table_Splash + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + SPLASH_DATA + " TEXT)";
        db.execSQL(query_splash);

        String query_notification = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTIFICATION + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSONDATA
                + " TEXT," + READ + " INTEGER," + SAVEDTIME + " TEXT)";

        db.execSQL(query_notification);
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_PROMOTION + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + /*JSONDATA + " TEXT,"+*/ PROMOTION_ID + " TEXT)";
        db.execSQL(query);

        String query_coursepeak = "CREATE TABLE IF NOT EXISTS " + TABLE_COURSE_PEAK + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSONDATA + " TEXT," + SAVEDTIME + " TEXT)";
        db.execSQL(query_coursepeak);

        String query_subjects = "CREATE TABLE IF NOT EXISTS " + TABLE_SUBJECTS + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + JSONDATA + " TEXT, " + SAVEDTIME + " TEXT)";
        db.execSQL(query_subjects);

        String query_ebookcontentdetails = "CREATE TABLE IF NOT EXISTS " + TABLE_EBOOKCONTENTDETAILS + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + JSONDATA + " TEXT, "+ EBOOK_ID + " INTEGER, " + TYPE + " TEXT, " + SAVEDTIME + " TEXT)";
        db.execSQL(query_ebookcontentdetails);

        String query_ebookpages = "CREATE TABLE IF NOT EXISTS " + TABLE_EBOOKPAGES + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + JSONDATA + " TEXT, " + PAGE_ID + " INTEGER, " + BOOKMARK + " INTEGER, "
                + FAVORITE + " INTEGER, " + SAVEDTIME + " TEXT)";
        db.execSQL(query_ebookpages);


    }

    public void createtable_ebookpages(){
        try {
            SQLiteDatabase db = getWritableDatabase();
            String query_ebookpages = "CREATE TABLE IF NOT EXISTS " + TABLE_EBOOKPAGES + "("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + JSONDATA + " TEXT, " + PAGE_ID + " INTEGER, " + BOOKMARK + " INTEGER, "
                    + FAVORITE + " INTEGER, " + SAVEDTIME + " TEXT)";
            db.execSQL(query_ebookpages);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createtable_contentdetails(){
        try {
            SQLiteDatabase db = getWritableDatabase();
            String query_ebookcontentdetails = "CREATE TABLE IF NOT EXISTS " + TABLE_EBOOKCONTENTDETAILS + "("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + JSONDATA + " TEXT, "+ EBOOK_ID + " INTEGER, " + TYPE + " TEXT, " + SAVEDTIME + " TEXT)";
            db.execSQL(query_ebookcontentdetails);
            Log.e("query ebook ", query_ebookcontentdetails);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void altercontent() {
        try {
            SQLiteDatabase db = getWritableDatabase();
            String sql = "ALTER TABLE " + TABLE_EBOOKCONTENTDETAILS + " ADD COLUMN " + EBOOK_ID + " INTEGER DEFAULT 0 NOT NULL";
            db.execSQL(sql);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    public void createtable_subjects(){
        try {
            SQLiteDatabase db = getWritableDatabase();
            String query_subjects = "CREATE TABLE IF NOT EXISTS " + TABLE_SUBJECTS + "("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + JSONDATA + " TEXT, " + SAVEDTIME + " TEXT)";
            db.execSQL(query_subjects);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save_ebookpages(ContentValues c, int page_id) {

        try {
            createtable_ebookpages();
            delete_ebookpages();
            SQLiteDatabase db = getWritableDatabase();
            long id = db.insertWithOnConflict(TABLE_EBOOKPAGES, null, c, SQLiteDatabase.CONFLICT_REPLACE);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //get_ebookpages(page_id);

    }

    public void save_contentdetails(ContentValues c) {

        try {
            createtable_contentdetails();
            checkcol();
            //delete_contentdetails();
            SQLiteDatabase db = getWritableDatabase();
            long id = db.insertWithOnConflict(TABLE_EBOOKCONTENTDETAILS, null, c, SQLiteDatabase.CONFLICT_REPLACE);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void checkcol() {
        try {
            if (!isColumnExists(TABLE_EBOOKCONTENTDETAILS, EBOOK_ID)) {

                altercontent();
            }else{
                Log.e("here","here");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save_subjects(ContentValues c) {

        try {
            createtable_subjects();
            delete_subjects();
            SQLiteDatabase db = getWritableDatabase();
            long id = db.insertWithOnConflict(TABLE_SUBJECTS, null, c, SQLiteDatabase.CONFLICT_REPLACE);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public JSONArray get_ebookpages(int page_id) {
        JSONArray resultSet = new JSONArray();
        try {
            createtable_ebookpages();
            SQLiteDatabase db = getWritableDatabase();
            String search_page = "SELECT * FROM " + TABLE_EBOOKPAGES + " where " + PAGE_ID + " = '" + page_id + "'";
            Cursor cursor = db.rawQuery(search_page, null);
            cursor.moveToFirst();
            resultSet = convertcursorvaltoJSOnArray(cursor);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public JSONArray get_contentdetails(String type_ebook, int ebook_id) {
        JSONArray resultSet = new JSONArray();
        try {
            createtable_contentdetails();
            SQLiteDatabase db = getWritableDatabase();

            //String searchQuery = "SELECT * FROM "+ TABLE_EBOOKCONTENTDETAILS + " where " + TYPE + " = '" + type_ebook + "'";
            String searchQuery = "SELECT * FROM " + TABLE_EBOOKCONTENTDETAILS + " where " + EBOOK_ID + " = '" + ebook_id + "' AND " + TYPE + " = '" + type_ebook + "'";
            Cursor cursor = db.rawQuery(searchQuery, null);
            cursor.moveToFirst();
            resultSet = convertcursorvaltoJSOnArray(cursor);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public JSONArray get_subjects() {
        JSONArray resultSet = new JSONArray();
        try {
            createtable_subjects();
            SQLiteDatabase db = getWritableDatabase();
            String search_page = "SELECT * FROM " + TABLE_SUBJECTS;
            Cursor cursor = db.rawQuery(search_page, null);
            cursor.moveToFirst();
            resultSet = convertcursorvaltoJSOnArray(cursor);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void delete_ebookpages() {

        try {
            createtable_ebookpages();
            SQLiteDatabase db = getWritableDatabase();
            String query = "DELETE FROM " + TABLE_EBOOKPAGES;
            db.execSQL(query);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete_contentdetails() {

        try {
            createtable_contentdetails();
            SQLiteDatabase db = getWritableDatabase();
            String query = "DELETE FROM " + TABLE_EBOOKCONTENTDETAILS;
            Log.e("query ebook ", query);
            db.execSQL(query);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete_subjects() {

        try {
            createtable_subjects();
            SQLiteDatabase db = getWritableDatabase();
            String query = "DELETE FROM " + TABLE_SUBJECTS;
            db.execSQL(query);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void createtablenotification() {
        try {
            SQLiteDatabase db = getWritableDatabase();
            String query_notification = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTIFICATION + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSONDATA + " TEXT," + READ + " INTEGER," + SAVEDTIME + " TEXT)";
            db.execSQL(query_notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public long savenotificationData(final String tableName, String data) {
        long index = 0;
        try {
            createtablenotification();
            SQLiteDatabase db = getWritableDatabase();
            ContentValues c = new ContentValues();
            //c.put(ID, 1);
            c.put(JSONDATA, data);
            c.put(READ, 0);
            c.put(SAVEDTIME, DateUtils.getSqliteTime());
            index = db.insertWithOnConflict(tableName, null, c,
                    SQLiteDatabase.CONFLICT_REPLACE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return index;
    }

    public ArrayList<String> getnotificationdata(final String tableName) {
        createtablenotification();
        ArrayList<String> list = new ArrayList<String>();
        try {
            SQLiteDatabase db = getReadableDatabase();
            //String query = "SELECT * FROM " + tableName + " WHERE " + READ + " =0";
            String query = "SELECT * FROM " + tableName;
            Cursor c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                do {
                    JSONObject obj;
                    try {
                        obj = new JSONObject(c.getString(1));
                        obj.put("id", c.getLong(0));
                        Log.e("objdata ", "obj " + obj.toString());
                        list.add(obj.toString());
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Log.e("error1", "error1" + e);
                    }
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public JSONArray get_courseandpeak() {
        JSONArray resultSet = new JSONArray();
        try {
            createtable_courseandpeak();
            SQLiteDatabase db = getWritableDatabase();
            String searchQuery = "SELECT JSONDATA FROM " + TABLE_COURSE_PEAK;
            Cursor cursor = db.rawQuery(searchQuery, null);
            cursor.moveToFirst();
            resultSet = convertcursorvaltoJSOnArray(cursor);
            Log.e("searchquery ", "resultset " + resultSet.toString());
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void save_courseandpeak(ContentValues c) {

            try {
                createtable_courseandpeak();
                deleteexamination_courseandpeak();
                SQLiteDatabase db = getWritableDatabase();
                long id = db.insertWithOnConflict(TABLE_COURSE_PEAK, null, c, SQLiteDatabase.CONFLICT_REPLACE);

            } catch (Exception e) {
                e.printStackTrace();
            }

            get_courseandpeak();

    }

    public void deleteexamination_courseandpeak() {

        try {
            createtable_courseandpeak();
            SQLiteDatabase db = getWritableDatabase();
            String query = "DELETE FROM " + TABLE_COURSE_PEAK;
            db.execSQL(query);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createtable_courseandpeak() {
        try {
            SQLiteDatabase db = getWritableDatabase();
            String coursepeak = "CREATE TABLE IF NOT EXISTS " + TABLE_COURSE_PEAK + "("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSONDATA + " TEXT," + SAVEDTIME + " TEXT)";
            db.execSQL(coursepeak);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONArray get_feedback_details(String event_id) {
        JSONArray resultSet = new JSONArray();
        try {
            createtablefeedback();
            SQLiteDatabase db = getWritableDatabase();
            String searchQuery = "SELECT  * FROM " + FEEDBACK + " where " + EVENT_ID + " = '" + event_id + "'";
            Cursor cursor = db.rawQuery(searchQuery, null);
            cursor.moveToFirst();
            resultSet = convertcursorvaltoJSOnArray(cursor);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void save_feedback_Details(ContentValues c, String event_id) {

        try {
            createtablefeedback();
            int id1 = 0;
            SQLiteDatabase db = getWritableDatabase();
            String query = "Select * from " + FEEDBACK + " where " + EVENT_ID + " = '" + event_id + "'";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                id1 = db.update(FEEDBACK, c, EVENT_ID + " =?", new String[]{event_id});
            } else {
                long id = db.insertWithOnConflict(FEEDBACK, null, c, SQLiteDatabase.CONFLICT_REPLACE);
            }

            get_feedback_details(event_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createtablefeedback() {
        try {
            SQLiteDatabase db = getWritableDatabase();
            String feedback = "CREATE TABLE IF NOT EXISTS " + FEEDBACK + "("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSONDATA + " TEXT," + EVENT_ID + " TEXT, " + IS_SUBMIT + " INTEGER, " + SAVEDTIME + " TEXT)";
            db.execSQL(feedback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void createtableansdata() {
        try {
            SQLiteDatabase db = getWritableDatabase();
            String query_questionansdata = "CREATE TABLE IF NOT EXISTS " + TABLE_MSTSELECTEDANS + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + QID + " INTEGER," + ANS + " TEXT," + EXAMID + " TEXT," + SAVEDTIME + " TEXT)";
            db.execSQL(query_questionansdata);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveansdata(int qid, ContentValues c) {
        try {
            createtableansdata();
            SQLiteDatabase db = getWritableDatabase();
            String query = "DELETE FROM " + TABLE_MSTSELECTEDANS + " where " + QID + " = " + qid;
            db.execSQL(query);
            long id = db.insertWithOnConflict(TABLE_MSTSELECTEDANS, null, c, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONArray getans(int qid) {
        JSONArray resultSet = new JSONArray();
        try {
            createtableansdata();
            SQLiteDatabase db = getWritableDatabase();
            String searchQuery = "SELECT  * FROM " + TABLE_MSTSELECTEDANS + " where " + QID + " = " + qid + "";// and " + EXAMTYPE + " = '" + type + "'";

            Cursor cursor = db.rawQuery(searchQuery, null);

            cursor.moveToFirst();

            resultSet = convertcursorvaltoJSOnArray(cursor);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    /*public  void createtableexaminationdetails(){
        try {
            SQLiteDatabase db = getWritableDatabase();

            String query_examinationdetails = "CREATE TABLE IF NOT EXISTS " + TABLE_MSTEXAMINATIONDETAILS + "("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + EXAMID + " TEXT," + PROGRESS + " TEXT," + ANSWERED + " TEXT," + CORRECT + " TEXT," + WRONG + " TEXT," + SKIPP + " TEXT," + SPEED + " TEXT," + TIMETAKEN + " TEXT," + EXAMTYPE + " TEXT," + SAVEDTIME + " TEXT," + TIMETAKENPERQUESTION + " TEXT)";
            db.execSQL(query_examinationdetails);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }*/


    public void createtableexaminationdetails() {
        try {

            SQLiteDatabase db = getWritableDatabase();
            String query_examinationdetails = "CREATE TABLE IF NOT EXISTS " + TABLE_MSTEXAMINATIONDETAILS + "("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + EXAMID + " TEXT," + PROGRESS + " TEXT," + ANSWERED + " TEXT," + CORRECT + " TEXT," + WRONG + " TEXT," + SKIPP + " TEXT," + SPEED + " TEXT," + TIMETAKEN + " TEXT," + EXAMTYPE + " TEXT," + COURSENAME + " TEXT, " + COURSEID + " TEXT, " + COURSENAMEINMARATHI + " TEXT, " + QUESTION + " TEXT, " + LAST_QID + " INTEGER, " + SAVEDTIME + " TEXT)";
            db.execSQL(query_examinationdetails);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveexaminationdetails(ContentValues c, int examid) {

        try {
            Log.e("data ", "data " + c.toString() + " " + examid);
            createtableexaminationdetails();
            checklastq();
            SQLiteDatabase db = getWritableDatabase();
            String query = "Select * from " + TABLE_MSTEXAMINATIONDETAILS + " where " + EXAMID + " = '" + examid + "'";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                int id = db.update(TABLE_MSTEXAMINATIONDETAILS, c, EXAMID + " =?", new String[]{Integer.toString(examid)});
            } else {
                long id = db.insertWithOnConflict(TABLE_MSTEXAMINATIONDETAILS, null, c, SQLiteDatabase.CONFLICT_REPLACE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // getexaminationdata();
    }

    public JSONArray getexamdetails(int examid, String type) {
        JSONArray resultSet = new JSONArray();
        try {
            createtablequestionurl();
            checklastq();
            SQLiteDatabase db = getWritableDatabase();
            String searchQuery = "SELECT  * FROM " + TABLE_MSTEXAMINATIONDETAILS + " where " + EXAMID + " = '" + examid + "'";// and " + EXAMTYPE + " = '" + type + "'";
            //String searchQuery = "SELECT  * FROM " + TABLE_MSTEXAMINATIONDETAILS + " where " + EXAMID + " = '" + examid + "' and " + EXAMTYPE + " = '" + type + "'";
            Cursor cursor = db.rawQuery(searchQuery, null);

            cursor.moveToFirst();
            resultSet = convertcursorvaltoJSOnArray(cursor);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void createtablequestionurl() {
        try {
            SQLiteDatabase db = getWritableDatabase();
            String query_questionurl = "CREATE TABLE IF NOT EXISTS " + TABLE_QUESTIONURL + "(" + ID1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + ID + " INTEGER," + TYPE + " TEXT," + IMAGESOURCE + " TEXT," + OFFLINEPATH + " TEXT," + SYNC + " INTEGER," + EXAMID + " INTEGER," + SAVEDTIME + " TEXT)";
            db.execSQL(query_questionurl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONArray getquestionurl(int qid, String type) {

        JSONArray resultSet = new JSONArray();
        try {
            createtablequestionurl();
            SQLiteDatabase db = getWritableDatabase();
            String searchQuery = "SELECT  * FROM " + TABLE_QUESTIONURL + " where " + ID + " = " + qid + " and " + TYPE + " = '" + type + "'";
            Cursor cursor = db.rawQuery(searchQuery, null);

            cursor.moveToFirst();
            resultSet = convertcursorvaltoJSOnArray(cursor);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void checkqurl() {

        try {
            if (!isColumnExists(TABLE_QUESTIONURL, EXAMID)) {
                alterqurldata();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void savequestionurl(ContentValues c, int id1, String type, String imageurl) {

        try {
            createtablequestionurl();

            if (!isColumnExists(TABLE_QUESTIONURL, EXAMID)) {

                alterqurldata();
            }

            SQLiteDatabase db = getWritableDatabase();
            String query = "DELETE FROM " + TABLE_QUESTIONURL + " where " + ID + " = " + id1 + " and " + TYPE + " = '" + type + "' and " + IMAGESOURCE + " = '" + imageurl + "'";
            db.execSQL(query);
            long id = db.insertWithOnConflict(TABLE_QUESTIONURL, null, c, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void alterqurldata() {
        try {
            SQLiteDatabase db = getWritableDatabase();
            String sql = "ALTER TABLE " + TABLE_QUESTIONURL + " ADD COLUMN " + EXAMID + " INTEGER  DEFAULT 0 NOT NULL";

            db.execSQL(sql);

        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    public JSONArray getqurldata(int examid) {
        JSONArray resultSet = new JSONArray();
        try {
            SQLiteDatabase db = getWritableDatabase();
            String searchQuery = "SELECT  * FROM " + TABLE_QUESTIONURL + " where (" + EXAMID + " = " + examid + " OR " + EXAMID + " = " + 0 + " ) AND " + OFFLINEPATH + " =''";
            Cursor cursor = db.rawQuery(searchQuery, null);
            cursor.moveToFirst();
            resultSet = convertcursorvaltoJSOnArray(cursor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void createtablemstexamination() {

        try {
            SQLiteDatabase db = getWritableDatabase();
            String query_examination = "CREATE TABLE IF NOT EXISTS " + TABLE_MSTEXAMINATION + "("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSONDATA + " TEXT," + TYPE + " TEXT," + COURSENAME + " TEXT, " + COURSEID + " TEXT, " + COURSENAMEINMARATHI + " TEXT, " + SAVEDTIME + " TEXT)";
            db.execSQL(query_examination);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void createtablequestion() {

        try {
            SQLiteDatabase db = getWritableDatabase();
            String query_question = "CREATE TABLE IF NOT EXISTS " + TABLE_QUESTION + "("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + LANGUAGE + " TEXT," + EXAMID + " INTEGER," + JSONDATA + " TEXT," + SAVEDTIME + " TEXT)";
            db.execSQL(query_question);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createtableoriginalquestiondata() {

        try {
            SQLiteDatabase db = getWritableDatabase();
            String query_question = "CREATE TABLE IF NOT EXISTS " + TABLE_OROGINALQUESTIONDATA + "("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + LANGUAGE + " TEXT," + EXAMID + " INTEGER," + JSONDATA + " TEXT," + SAVEDTIME + " TEXT)";
            db.execSQL(query_question);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createtableanswer() {

        try {
            SQLiteDatabase db = getWritableDatabase();
            String query_answer = "CREATE TABLE IF NOT EXISTS " + TABLE_ANSWER + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + ANSWERDATA + " TEXT," + STARTTIMEOBJ + " TEXT," + LANGUAGE + " TEXT," + EXAMID + " INTEGER," + JSONDATA + " TEXT," + SYNC + " INTEGER," + SAVEDTIME + " TEXT)";
            db.execSQL(query_answer);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONArray getexaminationdata(String type) {
        JSONArray resultSet = new JSONArray();
        try {
            createtablemstexamination();
            SQLiteDatabase db = getWritableDatabase();
            String searchQuery = "SELECT  * FROM " + TABLE_MSTEXAMINATION + " where " + TYPE + " = '" + type + "'";
            Cursor cursor = db.rawQuery(searchQuery, null);

            cursor.moveToFirst();
            resultSet = convertcursorvaltoJSOnArray(cursor);
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }



    public JSONArray getexaminationdatav1(String type, int courseid) {
        JSONArray resultSet = new JSONArray();
        try {
            createtablemstexaminationv1();
            SQLiteDatabase db = getWritableDatabase();
            String searchQuery = "SELECT  * FROM " + TABLE_MSTEXAMINATIONv1 + " where " + TYPE + " = '" + type + "' AND " + COURSEID + " = '" + courseid + "'";
            if (courseid == -1) {
                searchQuery = "SELECT  * FROM " + TABLE_MSTEXAMINATIONv1 + " where " + TYPE + " = '" + type + "' ORDER BY " + COURSEID;
            }
            Cursor cursor = db.rawQuery(searchQuery, null);


            cursor.moveToFirst();

            resultSet = convertcursorvaltoJSOnArray(cursor);
            Log.e("searchquery ", "searchquery " + searchQuery);
            Log.e("searchquery ", "result  " + resultSet.toString());
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }


    public JSONArray getqdata(int examid, String language) {
        JSONArray resultSet = new JSONArray();
        try {
            createtablequestion();
            SQLiteDatabase db = getWritableDatabase();
            String searchQuery = "SELECT  * FROM " + TABLE_QUESTION + " where " + EXAMID + "=" + examid;//+ " and " + LANGUAGE + " ='" + language + "'";
            //String searchQuery = "SELECT  * FROM " + TABLE_QUESTION + " where " + EXAMID + "=" + examid + " and " + LANGUAGE + " ='" + language + "'";

            Cursor cursor = db.rawQuery(searchQuery, null);

            cursor.moveToFirst();

            resultSet = convertcursorvaltoJSOnArray(cursor);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public int getqdatacount(int examid, String language) {
        int count = 0;
        try {
            createtablequestion();
            SQLiteDatabase db = getWritableDatabase();
            String searchQuery = "SELECT  * FROM " + TABLE_QUESTION + " where " + EXAMID + "=" + examid + " and " + LANGUAGE + " ='" + language + "'";

            Cursor cursor = db.rawQuery(searchQuery, null);
            count = cursor.getCount();
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

   /* public JSONArray getqdata1(int examid, String language) {
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
    }*/

    public int getqdata1count(int examid, String language) {
        int count = 0;
        try {
            createtableoriginalquestiondata();
            SQLiteDatabase db = getWritableDatabase();
            String searchQuery = "SELECT  * FROM " + TABLE_OROGINALQUESTIONDATA + " where " + EXAMID + "=" + examid + " and " + LANGUAGE + " ='" + language + "'";

            Cursor cursor = db.rawQuery(searchQuery, null);
            count = cursor.getCount();

            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public JSONArray getcoursedata() {
        JSONArray resultSet = new JSONArray();
        try {
            createtablemstexaminationv1();
            SQLiteDatabase db = getWritableDatabase();
            String searchQuery = "SELECT " + COURSENAME + " , " + COURSEID + " , " + COURSENAMEINMARATHI + " FROM " + TABLE_MSTEXAMINATIONv1 + " GROUP BY " + COURSENAME;
            ;//+ " where " + EXAMID + "=" + examid + " and " + SYNC + "=" + 0 + " and " + LANGUAGE + " ='" + language + "'";
            Cursor cursor = db.rawQuery(searchQuery, null);

            cursor.moveToFirst();

            resultSet = convertcursorvaltoJSOnArray(cursor);
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public JSONArray getansdata(int examid, String language) {
        JSONArray resultSet = new JSONArray();
        try {
            createtableanswer();
            SQLiteDatabase db = getWritableDatabase();
            String searchQuery = "SELECT  * FROM " + TABLE_ANSWER + " where " + EXAMID + "=" + examid + " and " + SYNC + "=" + 0;// + "'";//+ " and " + LANGUAGE + " ='" + language
            Log.e("query ", "query " + searchQuery);
            Cursor cursor = db.rawQuery(searchQuery, null);

            cursor.moveToFirst();

            resultSet = convertcursorvaltoJSOnArray(cursor);
            Log.e("query result ", "query result " + resultSet.toString());
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public JSONArray getansdata1(int examid, String language) {
        JSONArray resultSet = new JSONArray();
        try {
            createtableanswer();
            SQLiteDatabase db = getWritableDatabase();
            String searchQuery = "SELECT  * FROM " + TABLE_ANSWER + " where " + EXAMID + "=" + examid + " and " + LANGUAGE + " ='" + language + "'";

            Cursor cursor = db.rawQuery(searchQuery, null);

            cursor.moveToFirst();

            resultSet = convertcursorvaltoJSOnArray(cursor);
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public int getasyncdatacount(int examid, String language) {
        int count = 0;
        try {
            createtableanswer();
            SQLiteDatabase db = getWritableDatabase();
            String searchQuery = "SELECT  * FROM " + TABLE_ANSWER + " where " + EXAMID + "=" + examid + " and " + SYNC + "=" + 0;//+ " and " + LANGUAGE + " ='" + language + "'";

            Cursor cursor = db.rawQuery(searchQuery, null);
            count = cursor.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public void saveexaminationdatav1(ContentValues c, int examid, int courseid) {

        try {
            createtablemstexaminationv1();
            deleteexaminationv1(examid, courseid);
            SQLiteDatabase db = getWritableDatabase();
            long id = db.insertWithOnConflict(TABLE_MSTEXAMINATIONv1, null, c, SQLiteDatabase.CONFLICT_REPLACE);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //getexaminationdata();
    }

    public void deleteexaminationv1(int examid, int courseid) {

        try {
            createtablemstexaminationv1();
            SQLiteDatabase db = getWritableDatabase();
            String query = "DELETE FROM " + TABLE_MSTEXAMINATIONv1 + " where " + EXAMID + " = '" + examid + " ' AND " + COURSEID + " = '" + courseid + "'";
            db.execSQL(query);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void createtablemstexaminationv1() {

        try {
            SQLiteDatabase db = getWritableDatabase();
            String query_examination = "CREATE TABLE IF NOT EXISTS " + TABLE_MSTEXAMINATIONv1 + "("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSONDATA + " TEXT," + TYPE + " TEXT," + COURSENAME + " TEXT, " + COURSEID + " TEXT, " + COURSENAMEINMARATHI + " TEXT, " + EXAMID + " INTEGER, " + SAVEDTIME + " TEXT)";
            db.execSQL(query_examination);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveexaminationdata(ContentValues c, String type) {

        try {
            createtablemstexamination();
            deleteexamination(type);
            SQLiteDatabase db = getWritableDatabase();
            long id = db.insertWithOnConflict(TABLE_MSTEXAMINATION, null, c, SQLiteDatabase.CONFLICT_REPLACE);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //getexaminationdata();
    }



    public void deleteexamination(String type) {

        try {
            createtablemstexamination();
            SQLiteDatabase db = getWritableDatabase();
            String query = "DELETE FROM " + TABLE_MSTEXAMINATION + " where " + TYPE + " = '" + type + "'";
            db.execSQL(query);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteexaminationv1(String type) {

        try {
            createtablemstexamination();
            SQLiteDatabase db = getWritableDatabase();
            String query = "DELETE FROM " + TABLE_MSTEXAMINATIONv1 + " where " + TYPE + " = '" + type + "'";
            db.execSQL(query);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteanswer() {

        try {
            createtableanswer();
            SQLiteDatabase db = getWritableDatabase();
            String query = "DELETE FROM " + TABLE_ANSWER;
            db.execSQL(query);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletedata(String tablename) {

        try {
            createtableanswer();
            SQLiteDatabase db = getWritableDatabase();
            String query = "DELETE FROM " + tablename;
            db.execSQL(query);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public long saveqdata(ContentValues c, int examid, String language) {
        long id = 0;
        try {
            createtablequestion();
            SQLiteDatabase db = getWritableDatabase();
            String query = "DELETE FROM " + TABLE_QUESTION + " where " + EXAMID + "=" + examid + " and " + LANGUAGE + " ='" + language + "'";
            db.execSQL(query);
            id = db.insertWithOnConflict(TABLE_QUESTION, null, c, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

/*
    public long saveqdata1(ContentValues c, int examid, String language) {
        Log.e("examid", "examid " + examid + " " + language);
        createtablequestion();
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_QUESTION1 + " where " + EXAMID + "=" + examid + " and " + LANGUAGE + " ='" + language + "'";
        db.execSQL(query);
        long id = db.insertWithOnConflict(TABLE_QUESTION1, null, c, SQLiteDatabase.CONFLICT_REPLACE);
        return id;
    }
*/

    public void updateqdata(ContentValues c, int examid, String language) {

        try {

            createtablequestion();
            SQLiteDatabase db = getWritableDatabase();
            int i = db.update(TABLE_QUESTION, c, EXAMID + " = ? AND " + LANGUAGE + " = ? ", new String[]{Integer.toString(examid), language});

        } catch (Exception e) {
            e.printStackTrace();
        }

/*
        long id = db.insertWithOnConflict(TABLE_QUESTION, null, c, SQLiteDatabase.CONFLICT_REPLACE);
        return  id;*/
    }

    public void updatesync(long id) {

        try {
            createtableanswer();
            SQLiteDatabase db = getWritableDatabase();
            ContentValues c = new ContentValues();
            c.put(SYNC, 1);
            db.update(TABLE_ANSWER, c, ID + " = ? ", new String[]{Long.toString(id)});

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long saveanswerdata(ContentValues c) {
        long id = 0;
        try {
            createtableanswer();
            SQLiteDatabase db = getWritableDatabase();
            id = db.insertWithOnConflict(TABLE_ANSWER, null, c, SQLiteDatabase.CONFLICT_REPLACE);

        } catch (Exception e) {
            e.printStackTrace();
        }

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

                            rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                        } else {
                            rowObject.put(cursor.getColumnName(i), "");
                        }
                    } catch (Exception e) {

                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }
        cursor.close();

        return resultSet;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


    }

    public long getsize() {
        File dbpath = mcontext.getDatabasePath(DATABASE_NAME);
        SQLiteDatabase db = getWritableDatabase();

        return new File(db.getPath()).length();
    }

    public void checklastq() {
        try {
            if (!isColumnExists(TABLE_MSTEXAMINATIONDETAILS, LAST_QID)) {
                altermstexaminationdetails();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void altermstexaminationdetails() {
        try {
            SQLiteDatabase db = getWritableDatabase();
            String sql = "ALTER TABLE " + TABLE_MSTEXAMINATIONDETAILS + " ADD COLUMN " + LAST_QID + " INTEGER  DEFAULT 0 NOT NULL";
            db.execSQL(sql);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    public boolean isColumnExists(String tableName, String columnToFind) {
        Cursor cursor = null;
        try {
            SQLiteDatabase sqliteDatabase = getWritableDatabase();
            cursor = sqliteDatabase.rawQuery(
                    "PRAGMA table_info(" + tableName + ")",
                    null
            );

            int nameColumnIndex = cursor.getColumnIndexOrThrow("name");

            while (cursor.moveToNext()) {
                String name = cursor.getString(nameColumnIndex);

                if (name.equals(columnToFind)) {
                    return true;
                }
            }

            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public JSONArray getdata() {
        JSONArray resultSet = new JSONArray();
        try {
            createtablechat();
            SQLiteDatabase db = getWritableDatabase();
            String searchQuery = "SELECT  * FROM " + Table;
            // Log.e("query ", "query " + searchQuery);
            Cursor cursor = db.rawQuery(searchQuery, null);

            cursor.moveToFirst();
            //Log.e("data ", "data " + cursor.getCount());
            resultSet = convertcursorvaltoJSOnArray(cursor);
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void insertdata(ContentValues c) {

        try {
            createtablechat();
            SQLiteDatabase db = getWritableDatabase();
            long id = db.insertWithOnConflict(Table, null, c, SQLiteDatabase.CONFLICT_REPLACE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createtablechat() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "CREATE TABLE IF NOT EXISTS   " + Table + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TokenId + " TEXT," + JSONDATA + " VARCHAR2," + SubmissionDate + " TEXT," + Title + " TEXT," + Status + " TEXT," + CreatedDate + " TEXT," + FileUrl + " TEXT," + StudentId + " TEXT," + CategoryId + " TEXT)";
        db.execSQL(query);
    }

    public JSONArray loaddata() {
        JSONArray resultSet = new JSONArray();
        try {
            SQLiteDatabase db = getWritableDatabase();
            createtablesplash();

            String searchQuery = "SELECT  * FROM " + Table_Splash;
            Cursor cursor = db.rawQuery(searchQuery, null);

            cursor.moveToFirst();
            resultSet = convertcursorvaltoJSOnArray(cursor);
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void createtablesplash() {

        try {
            SQLiteDatabase db = getWritableDatabase();
            String query = "CREATE TABLE IF NOT EXISTS " + Table_Splash + "("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + SPLASH_DATA + " TEXT)";
            db.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONArray getdatastartchat(int token) {
        JSONArray resultSet = new JSONArray();
        try {
            createtablechat();
            SQLiteDatabase db = getWritableDatabase();
            String searchQuery = "Select * from " + Table + " where " + TokenId + " = '" + token + "'";

            Cursor cursor = db.rawQuery(searchQuery, null);

            cursor.moveToFirst();
            resultSet = convertcursorvaltoJSOnArray(cursor);
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void insertsplashdata(ContentValues c) {
        try {


            Droptablesplash();
            createtablesplash();
            SQLiteDatabase db = getWritableDatabase();
            long id = db.insertWithOnConflict(Table_Splash, null, c, SQLiteDatabase.CONFLICT_REPLACE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Droptablesplash() {
        createtablesplash();
        SQLiteDatabase db = getWritableDatabase();
        try {
            String query = "DELETE FROM " + Table_Splash;
            db.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*long l = db.delete(Table_Splash, ID + "=" + 1, null);
        Log.e("saved ", "saved " + l);*/
    }


    public void savechatmessage(ContentValues c, int id) {

        try {
            createtablechat();
            SQLiteDatabase db = getWritableDatabase();
            String query = "Select * from " + Table + " where " + TokenId + " = '" + id + "'";
            Log.e("query ", "query " + query);
            Cursor cursor = db.rawQuery(query, null);
            Log.e("data ", "data " + cursor.getCount());
            if (cursor.getCount() > 0) {
                db.update(Table, c, TokenId + " =?", new String[]{Integer.toString(id)});
                Log.e("data ", "data " + cursor.getCount());
            } else {
                long id1 = db.insertWithOnConflict(Table, null, c, SQLiteDatabase.CONFLICT_REPLACE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletebyid(final String tableName, Long id) {
        try {
            createtablenotification();
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("DELETE FROM " + tableName + " WHERE ID = " + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatenotification(final String tableName) {
        try {
            createtablenotification();
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("UPDATE " + tableName +
                    " SET " + READ + " =1 WHERE " + READ + " =0");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void savedata(ContentValues contentValues) {

        try {
            createtablepromotion();
            SQLiteDatabase db = getWritableDatabase();
            long id = db.insertWithOnConflict(TABLE_PROMOTION, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);

            Log.e("id ", "count : " + id);
            Log.e("content values ", "count : " + contentValues.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createtablepromotion() {

        try {
            SQLiteDatabase db = getWritableDatabase();
            String query = "CREATE TABLE IF NOT EXISTS " + TABLE_PROMOTION + "("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + /*JSONDATA + " TEXT,"+*/ PROMOTION_ID + " TEXT)";
            db.execSQL(query);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONArray getpromotion() {
        JSONArray resultSet = new JSONArray();
        try {
            SQLiteDatabase db = getWritableDatabase();
            createtablepromotion();

            String searchQuery = "SELECT  * FROM " + TABLE_PROMOTION;
            Cursor cursor = db.rawQuery(searchQuery, null);

            cursor.moveToFirst();
            resultSet = convertcursorvaltoJSOnArray(cursor);
            cursor.close();
            Log.e("promotion ", "table " + resultSet);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }


    public void deletpromotion(String id) {

        try {

            createtablepromotion();
            SQLiteDatabase db = getWritableDatabase();
            String query = "DELETE FROM " + TABLE_PROMOTION + " where " + PROMOTION_ID + " = '" + id + "'";
            db.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
