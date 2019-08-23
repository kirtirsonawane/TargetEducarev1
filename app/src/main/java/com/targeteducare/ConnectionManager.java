package com.targeteducare;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.targeteducare.Classes.Question;
import com.targeteducare.Classes.QuestionURL;
import com.targeteducare.database.DatabaseHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConnectionManager extends Activitycommon {
    private Context context;
    private static ConnectionManager instance;
    OkHttpClient client;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    public ConnectionManager(Context context) {
        this.context = context;
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(context.getCacheDir(), cacheSize);
        client = new OkHttpClient();
        client.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
        client.setReadTimeout(15, TimeUnit.SECONDS);
        client.setCache(cache);
    }

    public static synchronized ConnectionManager getInstance(Context context) {
        if (instance == null) {
            instance = new ConnectionManager(context);
        }
        return instance;
    }

    private void isDotNet() {
        GlobalValues.TEMP_STR = GlobalValues.TEMP_STR.trim();
        GlobalValues.TEMP_STR = GlobalValues.TEMP_STR.substring(1, GlobalValues.TEMP_STR.length() - 1);
        GlobalValues.TEMP_STR = GlobalValues.TEMP_STR.replace("\\n", " ");
        GlobalValues.TEMP_STR = GlobalValues.TEMP_STR.replace("\\t", " ");
        GlobalValues.TEMP_STR = GlobalValues.TEMP_STR.replace("\\r", " ");
        GlobalValues.TEMP_STR = GlobalValues.TEMP_STR.replace("\\", "");
    }
    private void isSpeacialChar(){
        GlobalValues.TEMP_STR = GlobalValues.TEMP_STR.replace("\\r", " ");
    }


    private String isDotNet(String str) {
        str = str.trim();
        str = str.substring(1, str.length() - 1);
        str = str.replace("\\", "");
        return str;
    }

    private void isDotNetSlash() {
        GlobalValues.TEMP_STR = GlobalValues.TEMP_STR.trim();
        GlobalValues.TEMP_STR = GlobalValues.TEMP_STR.substring(1,
                GlobalValues.TEMP_STR.length() - 1);
        GlobalValues.TEMP_STR = GlobalValues.TEMP_STR.replace("\\", "");
    }

    public void login(final String json) {
        // if (InternetUtils.getInstance(context).available()) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.LOGIN()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();
                    isDotNet();
                    final int code = response.code();
                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.LOGINEXCEPTION.ordinal());
                    } else
                        publishBroadcast(code, Connection.LOGIN.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.LOGINEXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.LOGINEXCEPTION.ordinal());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.LOGINEXCEPTION.ordinal());
                } catch (Exception e) {
                    return;
                }
            }
        });
        thread.start();
       /* } else {
            publishBroadcast(Constants.STATUS_OK, Connection.NO_INTERNET.ordinal());
        }*/
    }

    public void getexam(final String json, final String type) {
        Log.e("exam ", "exam " + json);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.GET_EXAMS()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();
                    isDotNet();
                    final int code = response.code();
                    try {
                        JSONObject obj1 = new JSONObject(GlobalValues.TEMP_STR).getJSONObject("root");
                        JSONArray array = new JSONArray();
                        if (obj1.optJSONArray("subroot") != null) {
                            array = obj1.getJSONArray("subroot");
                        } else if (obj1.optJSONObject("subroot") != null) {
                            if (obj1.getJSONObject("subroot").has("error")) {
                                publishBroadcast(Constants.STATUS_OK, Connection.NoExamAvailable.ordinal());
                                //  updatefragmentdata();
                                return;
                            } else {
                                array.put(obj1.getJSONObject("subroot"));
                            }
                        }

                        ContentValues c = new ContentValues();
                        c.put(DatabaseHelper.JSONDATA, array.toString());
                        c.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
                        c.put(DatabaseHelper.TYPE, type);
                        DatabaseHelper.getInstance(ConnectionManager.this).saveexaminationdata(c, type);

                        if (array.length() > 0)
                            DatabaseHelper.getInstance(ConnectionManager.this).deleteexaminationv1(type);

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            ContentValues c1 = new ContentValues();
                            c1.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
                            c1.put(DatabaseHelper.TYPE, type);
                            c1.put(DatabaseHelper.JSONDATA, obj.toString());
                            c1.put(DatabaseHelper.COURSENAME, obj.getString("coursename"));
                            c1.put(DatabaseHelper.COURSEID, obj.getString("courseid"));
                            c1.put(DatabaseHelper.COURSENAMEINMARATHI, obj.getString("coursename_inmarathi"));
                            c1.put(DatabaseHelper.EXAMID, obj.getInt("examid"));
                            DatabaseHelper.getInstance(context).saveexaminationdatav1(c1, obj.getInt("examid"), obj.getInt("courseid"));
                            //Log.e("updated data", "updated data " + c1.toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.GET_EXAMSEXCEPTION.ordinal());
                    } else
                        publishBroadcast(code, Connection.GET_EXAMS.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_EXAMSEXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_EXAMSEXCEPTION.ordinal());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_EXAMSEXCEPTION.ordinal());
                } catch (Exception e) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_EXAMSEXCEPTION.ordinal());
                    return;
                }
            }
        });
        thread.start();
       /* } else {
            publishBroadcast(Constants.STATUS_OK, Connection.NO_INTERNET.ordinal());
        }*/
    }


    public void getvalidcourse(final String json) {
        Log.e("getvalidcourse ", "getvalidcourse " + json + " url " + URLS.GET_VALIDCOURSE());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.GET_VALIDCOURSE()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();

                    //isDotNet();
                    final int code = response.code();

                    try {
                        JSONObject obj1 = new JSONObject(GlobalValues.TEMP_STR).optJSONObject("root");
                        JSONArray array = new JSONArray();

                        Object json = obj1.get("subroot");


                        if (json instanceof JSONArray) {
                            array = obj1.optJSONArray("subroot");
                            ContentValues c = new ContentValues();
                            c.put(DatabaseHelper.JSONDATA, array.toString());
                            c.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
                            DatabaseHelper.getInstance(ConnectionManager.this).save_courseandpeak(c);
                            //Log.e("array valid course ",array.toString() );

                        } else {
                            array.put(obj1.optJSONObject("subroot"));
                            ContentValues c1 = new ContentValues();
                            c1.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
                            c1.put(DatabaseHelper.JSONDATA, array.toString());
                            DatabaseHelper.getInstance(context).save_courseandpeak(c1);
                            /*for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                ContentValues c1 = new ContentValues();
                                c1.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
                                c1.put(DatabaseHelper.JSONDATA, obj.toString());
                                DatabaseHelper.getInstance(context).save_courseandpeak(c1);
                                //Log.e("updated data", "updated data " + c1.toString());
                            }*/
                            //Log.e("obj arr valid course ",array.toString() );
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.GET_VALIDCOURSE_EXCEPTION.ordinal());
                    } else
                        publishBroadcast(code, Connection.GET_VALIDCOURSE.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_VALIDCOURSE_EXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_VALIDCOURSE_EXCEPTION.ordinal());

                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_VALIDCOURSE_EXCEPTION.ordinal());

                } catch (Exception e) {

                    return;
                }
            }
        });
        thread.start();
    }

    public void getpracticetest(final String json, final String type) {
        Log.e("getprac ", "getprac " + json + " url " + URLS.GET_PracticeTest());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.GET_PracticeTest()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();

                    //isDotNet();
                    final int code = response.code();


                    try {
                        JSONObject obj1 = new JSONObject(GlobalValues.TEMP_STR).getJSONObject("root");
                        JSONArray array = new JSONArray();
                        if (obj1.optJSONArray("subroot") != null) {
                            array = obj1.getJSONArray("subroot");
                        } else if (obj1.optJSONObject("subroot") != null) {
                            if (obj1.getJSONObject("subroot").has("error")) {
                                publishBroadcast(Constants.STATUS_OK, Connection.NoExamAvailable.ordinal());
                                //  updatefragmentdata();
                                return;
                            } else {
                                array.put(obj1.getJSONObject("subroot"));
                            }
                        }

                        ContentValues c = new ContentValues();
                        c.put(DatabaseHelper.JSONDATA, array.toString());
                        c.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
                        c.put(DatabaseHelper.TYPE, type);
                        DatabaseHelper.getInstance(ConnectionManager.this).saveexaminationdata(c, type);

                        if (array.length() > 0)
                            DatabaseHelper.getInstance(ConnectionManager.this).deleteexaminationv1(type);

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            ContentValues c1 = new ContentValues();
                            c1.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
                            c1.put(DatabaseHelper.TYPE, type);
                            c1.put(DatabaseHelper.JSONDATA, obj.toString());
                            c1.put(DatabaseHelper.COURSENAME, obj.getString("coursename"));
                            c1.put(DatabaseHelper.COURSEID, obj.getString("courseid"));
                            c1.put(DatabaseHelper.COURSENAMEINMARATHI, obj.getString("coursename_inmarathi"));
                            c1.put(DatabaseHelper.EXAMID, obj.getInt("examid"));
                            DatabaseHelper.getInstance(context).saveexaminationdatav1(c1, obj.getInt("examid"), obj.getInt("courseid"));
                            //Log.e("updated data", "updated data " + c1.toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.GET_PracticeTest_EXCEPTION.ordinal());
                    } else
                        publishBroadcast(code, Connection.GET_PracticeTest.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_PracticeTest_EXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_PracticeTest_EXCEPTION.ordinal());

                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_PracticeTest_EXCEPTION.ordinal());

                } catch (Exception e) {

                    return;
                }
            }
        });
        thread.start();
    }

    public void getotp(final String msg, final String otp) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Request request = new Request.Builder().url(URLS.sendotp(msg, otp)).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();
                    isDotNet();
                    final int code = response.code();
                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.OTPEXCEPTION.ordinal());
                    } else
                        publishBroadcast(code, Connection.OTP.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.OTPEXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.OTPEXCEPTION.ordinal());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.OTPEXCEPTION.ordinal());
                } catch (Exception e) {
                    publishBroadcast(Constants.STATUS_OK, Connection.OTPEXCEPTION.ordinal());
                    return;
                }
            }
        });
        thread.start();
    }

  /*  public void getquestion(final String json, final int examid, final String language) {
        Log.e("json ", "json " + json + " " + URLS.GET_QUESTIONS());
        //if (InternetUtils.getInstance(context).available()) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.GET_QUESTIONS()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();
                    isDotNet();
                    Log.e("res ", "res " + GlobalValues.TEMP_STR);
                    final int code = response.code();
                    JSONObject obj = new JSONObject(GlobalValues.TEMP_STR).getJSONObject("root").getJSONObject("subroot");
                    Log.e("res abbbbbbbb", "res " + obj.toString());
                    if (obj.has("questions")) {
                        JSONArray array = obj.optJSONArray("questions");
                        ArrayList<Question> quesdata = new ArrayList<>();
                        final ArrayList<QuestionURL> qdata = new ArrayList<>();
                        if (array != null) {
                            for (int i = 0; i < array.length(); i++) {
                                quesdata.add(new Question(array.getJSONObject(i)));
                            }
                        } else {
                            JSONObject obj1 = obj.optJSONObject("questions");
                            quesdata.add(new Question(obj1));
                        }

                        for (int i = 0; i < quesdata.size(); i++) {
                            Pattern pattern = Pattern.compile("src=([^>]*)>");
                            Matcher matcher = pattern.matcher(quesdata.get(i).getName());
                            while (matcher.find()) {
                                qdata.add(new QuestionURL(matcher.group(1), "que", quesdata.get(i).getId()));
                            }

                            Matcher matcherexplanation = pattern.matcher(quesdata.get(i).getExplanation());
                            while (matcherexplanation.find()) {
                                qdata.add(new QuestionURL(matcherexplanation.group(1), "explanation", quesdata.get(i).getId()));
                            }

                            for (int j = 0; j < quesdata.get(i).getOptions().size(); j++) {
                                Matcher matcheropt = pattern.matcher(quesdata.get(i).getOptions().get(j).getName());
                                while (matcheropt.find()) {
                                    qdata.add(new QuestionURL(matcheropt.group(1), "ans", quesdata.get(i).getOptions().get(j).getId()));
                                }
                            }
                        }

                        try {
                            Intent intent = new Intent(Constants.BROADCAST_WIZARD);
                            intent.putExtra(Constants.BROADCAST_RESPONSE_CODE, 200);
                            intent.putExtra(Constants.BROADCAST_URL_ACCESS, Connection.GET_QUESTIONS.ordinal());
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.e("download error  ", "download error2 ");
                        ((Activitycommon) context).downloaddata(qdata, examid, language, obj.toString());

                     *//*   for (int i=0;i<qdata.size();i++)
                        {
                            Log.e("imagedata "," "+qdata.get(i).getImagemainsource()+" "+qdata.get(i).getType());

                        }*//*
                    }else {
                        publishBroadcast(Constants.STATUS_OK, Connection.GET_PARSINGEXCEPTION.ordinal());
                    }

                    //  getquestionurl(examid);
                } catch (ConnectException exception) {
                    Log.e("SocketTimeoutException ", "error " + exception.toString());
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_QUESTIONSEXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    Log.e("SocketTimeoutException ", "error " + exception.toString());
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_QUESTIONSEXCEPTION.ordinal());
                    Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_QUESTIONSEXCEPTION.ordinal());
                    Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (Exception e) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_PARSINGEXCEPTION.ordinal());

                    Log.e("error ", "error " + e.toString());
                    return;
                }
            }
        });
        thread.start();
       *//* } else {
            publishBroadcast(Constants.STATUS_OK, Connection.NO_INTERNET.ordinal());
        }*//*
    }*/


    public void getquestion(final String json, final int examid, final String language) {
        //if (InternetUtils.getInstance(context).available()) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.GET_QUESTIONS()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();
                    isDotNet();
                    final int code = response.code();
                    JSONObject obj = new JSONObject(GlobalValues.TEMP_STR).getJSONObject("root").getJSONObject("subroot");
                    if (obj.has("questions")) {
                        JSONArray array = obj.optJSONArray("questions");
                        ArrayList<Question> quesdata = new ArrayList<>();
                        final ArrayList<QuestionURL> qdata = new ArrayList<>();
                        if (array != null) {
                            for (int i = 0; i < array.length(); i++) {
                                quesdata.add(new Question(array.getJSONObject(i)));
                            }
                        } else {
                            JSONObject obj1 = obj.optJSONObject("questions");
                            quesdata.add(new Question(obj1));
                        }

                        for (int i = 0; i < quesdata.size(); i++) {
                            Pattern pattern = Pattern.compile("src=([^>]*)>");
                            Matcher matcher = pattern.matcher(quesdata.get(i).getName());
                            while (matcher.find()) {
                                qdata.add(new QuestionURL(matcher.group(1), "que", quesdata.get(i).getId()));

                            }

                            Matcher matcherexplanation = pattern.matcher(quesdata.get(i).getExplanation());
                            while (matcherexplanation.find()) {
                                qdata.add(new QuestionURL(matcherexplanation.group(1), "explanation", quesdata.get(i).getId()));
                            }

                            for (int j = 0; j < quesdata.get(i).getOptions().size(); j++) {
                                Matcher matcheropt = pattern.matcher(quesdata.get(i).getOptions().get(j).getName());
                                while (matcheropt.find()) {
                                    qdata.add(new QuestionURL(matcheropt.group(1), "ans", quesdata.get(i).getOptions().get(j).getId()));
                                }
                            }
                        }

                        ContentValues c = new ContentValues();
                        c.put(DatabaseHelper.JSONDATA, obj.toString());
                        String language = "";

                        if (lang.equals("mr")) {
                            language = "marathi";
                        } else {
                            language = "ENGLISH";
                        }

                        c.put(DatabaseHelper.LANGUAGE, language);
                        c.put(DatabaseHelper.EXAMID, examid);
                        c.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
                        long id = DatabaseHelper.getInstance(context).saveqdata(c, examid, language);

                        saveallqurldata(qdata, examid);
                        try {
                            Intent intent = new Intent(Constants.BROADCAST_WIZARD);
                            intent.putExtra(Constants.BROADCAST_RESPONSE_CODE, 200);
                            intent.putExtra(Constants.BROADCAST_URL_ACCESS, Connection.GET_QUESTIONS.ordinal());
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ((Activitycommon) context).downloaddata(qdata, examid, language, "");
                    } else {
                        publishBroadcast(Constants.STATUS_OK, Connection.GET_PARSINGEXCEPTION.ordinal());
                    }
                    //  getquestionurl(examid);
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_QUESTIONSEXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_QUESTIONSEXCEPTION.ordinal());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_QUESTIONSEXCEPTION.ordinal());
                } catch (Exception e) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_PARSINGEXCEPTION.ordinal());
                    return;
                }
            }
        });
        thread.start();
    }

    public void getanswer(final String json, final int examid, final String language) {
        //if (InternetUtils.getInstance(context).available()) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("json ", json + " url" + URLS.GET_Ans());
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.GET_Ans()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();
                    isDotNet();
                    final int code = response.code();
                    JSONObject obj = new JSONObject(GlobalValues.TEMP_STR).getJSONObject("root").getJSONObject("subroot");
                    if (obj.has("questions")) {
                        JSONArray array = obj.optJSONArray("questions");
                        ArrayList<Question> quesdata = new ArrayList<>();
                        final ArrayList<QuestionURL> qdata = new ArrayList<>();
                        if (array != null) {
                            for (int i = 0; i < array.length(); i++) {
                                quesdata.add(new Question(array.getJSONObject(i)));
                            }
                        } else {
                            JSONObject obj1 = obj.optJSONObject("questions");
                            quesdata.add(new Question(obj1));
                        }

                        for (int i = 0; i < quesdata.size(); i++) {
                            Pattern pattern = Pattern.compile("src=([^>]*)>");
                            Matcher matcher = pattern.matcher(quesdata.get(i).getName());
                            while (matcher.find()) {
                                qdata.add(new QuestionURL(matcher.group(1), "que", quesdata.get(i).getId()));

                            }

                            Matcher matcherexplanation = pattern.matcher(quesdata.get(i).getExplanation());
                            while (matcherexplanation.find()) {
                                qdata.add(new QuestionURL(matcherexplanation.group(1), "explanation", quesdata.get(i).getId()));
                            }

                            for (int j = 0; j < quesdata.get(i).getOptions().size(); j++) {
                                Matcher matcheropt = pattern.matcher(quesdata.get(i).getOptions().get(j).getName());
                                while (matcheropt.find()) {
                                    qdata.add(new QuestionURL(matcheropt.group(1), "ans", quesdata.get(i).getOptions().get(j).getId()));
                                }
                            }
                        }

                        ContentValues c = new ContentValues();
                        c.put(DatabaseHelper.JSONDATA, obj.toString());
                        String language = "";
                        if (lang.equals("mr")) {
                            language = "marathi";
                        } else {
                            language = "ENGLISH";
                        }

                        c.put(DatabaseHelper.LANGUAGE, language);
                        c.put(DatabaseHelper.EXAMID, examid);
                        c.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
                        long id = DatabaseHelper.getInstance(context).saveqdata(c, examid, language);

                        saveallqurldata(qdata, examid);
                        try {
                            Intent intent = new Intent(Constants.BROADCAST_WIZARD);
                            intent.putExtra(Constants.BROADCAST_RESPONSE_CODE, 200);
                            intent.putExtra(Constants.BROADCAST_URL_ACCESS, Connection.GET_QUESTIONS.ordinal());
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ((Activitycommon) context).downloaddata(qdata, examid, language, "");
                    } else {
                        publishBroadcast(Constants.STATUS_OK, Connection.GET_PARSINGEXCEPTION.ordinal());
                    }
                    //  getquestionurl(examid);
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_QUESTIONSEXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_QUESTIONSEXCEPTION.ordinal());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_QUESTIONSEXCEPTION.ordinal());
                } catch (Exception e) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_PARSINGEXCEPTION.ordinal());
                    return;
                }
            }
        });
        thread.start();
    }

    public void saveallqurldata(ArrayList<QuestionURL> qdata, int examid) {
        for (int i = 0; i < qdata.size(); i++) {
            String imageURL = qdata.get(i).getImagemainsource();
            QuestionURL qurldata = qdata.get(i);
            ContentValues c = new ContentValues();
            c.put(DatabaseHelper.ID, qurldata.getId());
            c.put(DatabaseHelper.IMAGESOURCE, imageURL);
            c.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
            c.put(DatabaseHelper.TYPE, qurldata.getType());
            c.put(DatabaseHelper.OFFLINEPATH, "");
            c.put(DatabaseHelper.EXAMID, examid);
            DatabaseHelper.getInstance(context).savequestionurl(c, qurldata.getId(), qurldata.getType(), imageURL);
        }
    }

    public void getquestionurl(final int examid) {
        JSONObject obj = new JSONObject();
        final JSONObject mainobj = new JSONObject();
        try {
            obj.put("examid", examid);
            obj.put("ip", "http://" + GlobalValues.IP);
            mainobj.put("FilterParameter", obj.toString());
        } catch (Exception e) {

        }
        //   if (InternetUtils.getInstance(context).available()) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(Constants.JSON, mainobj.toString());
                    Request request = new Request.Builder().url(URLS.QUESTION_URL()).post(body).build();
                    Response response = client.newCall(request).execute();
                    String res = response.body().string();
                    res = isDotNet(res);

                    final int code = response.code();
                    try {
                        JSONArray array = new JSONArray(res);
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<QuestionURL>>() {
                        }.getType();

                        ArrayList<QuestionURL> qurldata = gson.fromJson(array.toString(), type);
                        if (qurldata != null) {
                            for (int i = 0; i < qurldata.size(); i++) {
                                ContentValues c = new ContentValues();
                                c.put(DatabaseHelper.ID, qurldata.get(i).getId());
                                c.put(DatabaseHelper.IMAGESOURCE, qurldata.get(i).getImagemainsource());
                                c.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
                                c.put(DatabaseHelper.TYPE, qurldata.get(i).getType());
                                DatabaseHelper.getInstance(context).savequestionurl(c, qurldata.get(i).getId(), qurldata.get(i).getType(), qurldata.get(i).getImagemainsource());

                                /*ContentValues c1 = new ContentValues();
                                c1.put(EBookDatabaseHelper.PRACTICE_ID, qurldata.get(i).getId());
                                c1.put(EBookDatabaseHelper.PRACTICE_IMAGESOURCE, qurldata.get(i).getImagemainsource());
                                c1.put(EBookDatabaseHelper.PRACTICE_SAVEDTIME, DateUtils.getSqliteTime());
                                c1.put(EBookDatabaseHelper.PRACTICE_TYPE, qurldata.get(i).getType());
                                EBookDatabaseHelper.getInstance(context).savequestionurl(c1, qurldata.get(i).getId(), qurldata.get(i).getType(),qurldata.get(i).getImagemainsource());*/
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                        /*JSONObject obj = new JSONObject(GlobalValues.TEMP_STR).getJSONObject("root").getJSONObject("subroot");
                        ContentValues c = new ContentValues();
                        c.put(DatabaseHelper.JSONDATA, obj.toString());
                        c.put(DatabaseHelper.LANGUAGE, language);
                        c.put(DatabaseHelper.EXAMID, examid);
                        c.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
                        long id = DatabaseHelper.getInstance(context).saveqdata(c, examid, language);*/
                    //   Log.e("iddata", "idadata " + id);
                    publishBroadcast(code, Connection.GET_QUESTIONS.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_QUESTIONSEXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_QUESTIONSEXCEPTION.ordinal());

                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_QUESTIONSEXCEPTION.ordinal());

                } catch (Exception e) {

                    publishBroadcast(Constants.STATUS_OK, Connection.GET_QUESTIONSEXCEPTION.ordinal());

                    return;
                }
            }
        });
        thread.start();
       /* } else {
            publishBroadcast(Constants.STATUS_OK, Connection.NO_INTERNET.ordinal());
        }*/
    }

    public void SAVE_ANSWERSHEET(final String json, final String data, final String data1, final long id1) {
        Log.e("data0 ", "data " + json);
        Log.e("data1 ", "data " + data);
        Log.e("data2 ", "data " + data1);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body1 = RequestBody.create(Constants.JSON, data1);
                    Request request1 = new Request.Builder().url(URLS.Updatedata()).post(body1).build();
                    Response response1 = client.newCall(request1).execute();
                    GlobalValues.TEMP_STR = response1.body().string();
                    isDotNet();
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.SAVE_ANSWERSHEET()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();
                    isDotNet();
                    final int code = response.code();
                    Log.e("res ", "res " + GlobalValues.TEMP_STR);
                    DatabaseHelper.getInstance(context).updatesync(id1);
                    String id = new JSONObject(GlobalValues.TEMP_STR).getJSONObject("root").getJSONObject("subroot").getString("id");

                    JSONObject obj = new JSONObject(data);
                    obj.put("resultid", id);
                    UpdateOrder(obj.toString());

                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SAVE_ANSWEREXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SAVE_ANSWEREXCEPTION.ordinal());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SAVE_ANSWEREXCEPTION.ordinal());
                } catch (Exception e) {
                    return;
                }
            }
        });
        thread.start();
        /*} else {
            publishBroadcast(Constants.STATUS_OK, Connection.NO_INTERNET.ordinal());
        }*/
    }

    public void UpdateOrderv1(final String json) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.Updatedata()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();
                    isDotNet();
                    final int code = response.code();

                    DatabaseHelper.getInstance(context).deleteanswer();

                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.SAVE_ANSWEREXCEPTION.ordinal());
                    } else
                        publishBroadcast(code, Connection.SAVE_ANSWER.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SAVE_ANSWEREXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SAVE_ANSWEREXCEPTION.ordinal());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SAVE_ANSWEREXCEPTION.ordinal());

                } catch (Exception e) {
                    return;
                }
            }
        });
        thread.start();
      /*  } else {
            publishBroadcast(Constants.STATUS_OK, Connection.NO_INTERNET.ordinal());
        }*/
    }

    public void UpdateOrder(final String json) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.Updatedata()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();
                    isDotNet();
                    Log.e("res ", "res1 " + GlobalValues.TEMP_STR);
                    final int code = response.code();
                      /*  String id = new JSONObject(GlobalValues.TEMP_STR).getJSONObject("root").getJSONObject("subroot").getString("id");

                        JSONObject obj = new JSONObject(data);
                        obj.put("resultid", id);
                        Log.e("obj", "obj " + obj.toString());*/

                    DatabaseHelper.getInstance(context).deleteanswer();
                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.SAVE_ANSWEREXCEPTION.ordinal());
                    } else
                        publishBroadcast(code, Connection.SAVE_ANSWER.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SAVE_ANSWEREXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SAVE_ANSWEREXCEPTION.ordinal());

                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SAVE_ANSWEREXCEPTION.ordinal());

                } catch (Exception e) {
                    return;
                }
            }
        });
        thread.start();
      /*  } else {
            publishBroadcast(Constants.STATUS_OK, Connection.NO_INTERNET.ordinal());
        }*/
    }


    public void loginv1(final String json) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.loginv1()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();

                    isDotNet();
                    final int code = response.code();
                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.LOGINV1EXCEPTION.ordinal());
                    } else
                        publishBroadcast(code, Connection.LOGINV1.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.LOGINV1EXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.LOGINV1EXCEPTION.ordinal());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.LOGINV1EXCEPTION.ordinal());
                } catch (Exception e) {
                    return;
                }
            }
        });
        thread.start();
       /* } else {
            publishBroadcast(Constants.STATUS_OK, Connection.NO_INTERNET.ordinal());
        }*/
    }

    public void signup(final String json) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.signup()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();

                    isDotNet();
                    final int code = response.code();
                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.SIGNUPEXCEPTION.ordinal());
                    } else
                        publishBroadcast(code, Connection.SIGNUP.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SIGNUPEXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SIGNUPEXCEPTION.ordinal());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SIGNUPEXCEPTION.ordinal());
                } catch (Exception e) {
                    return;
                }
            }
        });
        thread.start();
    }

    public void getcategory(final String json) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("url ", "url " + URLS.getcategory() + " " + json);
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.getcategory()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();
                    Log.e("url ", "urlres " + GlobalValues.TEMP_STR);
                    isDotNet();
                    final int code = response.code();
                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.GETCATEGORYEXCEPTION.ordinal());
                    } else
                        publishBroadcast(code, Connection.GETCATEGORY.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETCATEGORYEXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETCATEGORYEXCEPTION.ordinal());

                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETCATEGORYEXCEPTION.ordinal());

                } catch (Exception e) {

                    return;
                }
            }
        });
        thread.start();
    }

    public void editprofile(final String json) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.editprofile()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();

                    isDotNet();
                    final int code = response.code();
                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.EDITPROFILEEXCPTION.ordinal());
                    } else
                        publishBroadcast(code, Connection.EDITPROFILE.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.EDITPROFILEEXCPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.EDITPROFILEEXCPTION.ordinal());

                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.EDITPROFILEEXCPTION.ordinal());

                } catch (Exception e) {

                    return;
                }
            }
        });
        thread.start();
    }

    public void getcity(final String json) {
        Log.e("json ", "json " + json + " " + URLS.getcity());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.getcity()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();

                    isDotNet();
                    final int code = response.code();
                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.GETCITYEXCPTION.ordinal());
                    } else
                        publishBroadcast(code, Connection.GETCITY.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETCITYEXCPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETCITYEXCPTION.ordinal());

                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETCITYEXCPTION.ordinal());

                } catch (Exception e) {

                    return;
                }
            }
        });
        thread.start();
    }

    public void updateprofile(final String json) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.updateprofile()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();

                    isDotNet();
                    final int code = response.code();
                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.UPDATEPROFILEEXCEPTION.ordinal());
                    } else
                        publishBroadcast(code, Connection.UPDATEPROFILE.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.UPDATEPROFILEEXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.UPDATEPROFILEEXCEPTION.ordinal());

                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.UPDATEPROFILEEXCEPTION.ordinal());

                } catch (Exception e) {
                    return;
                }
            }
        });
        thread.start();
    }

    public void getsanstha(final String json) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.getsanstha()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();

                    isDotNet();
                    final int code = response.code();
                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.GET_SANSTHAEXCEPTION.ordinal());
                    } else
                        publishBroadcast(code, Connection.GET_SANSTHA.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_SANSTHAEXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_SANSTHAEXCEPTION.ordinal());

                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_SANSTHAEXCEPTION.ordinal());

                } catch (Exception e) {

                    return;
                }
            }
        });
        thread.start();
    }

    public void getcenter(final String json) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.getcenter()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();

                    isDotNet();
                    final int code = response.code();
                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.GETCENTEREXCEPTION.ordinal());
                    } else
                        publishBroadcast(code, Connection.GETCENTER.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETCENTEREXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETCENTEREXCEPTION.ordinal());

                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETCENTEREXCEPTION.ordinal());

                } catch (Exception e) {

                    return;
                }
            }
        });
        thread.start();
    }

    public void getresultremark(final String json) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.getresultremark()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();

                    isDotNet();
                    final int code = response.code();
                    publishBroadcast(code, Connection.GETRESULTREMARK.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETRESULTREMARKEXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETRESULTREMARKEXCEPTION.ordinal());

                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETRESULTREMARKEXCEPTION.ordinal());

                } catch (Exception e) {

                    return;
                }
            }
        });
        thread.start();
    }

    public void getcourse(final String json) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.getcourse()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();

                    isDotNet();
                    final int code = response.code();
                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.GETCOURSEEXCEPTION.ordinal());
                    } else
                        publishBroadcast(code, Connection.GETCOURSE.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETCOURSEEXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETCOURSEEXCEPTION.ordinal());

                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETCOURSEEXCEPTION.ordinal());

                } catch (Exception e) {

                    return;
                }
            }
        });
        thread.start();
    }

    public void getsamplepapers(final String samplejson) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody body = RequestBody.create(Constants.JSON, samplejson);
                Request request = new Request.Builder().url(URLS.getsamplepaper()).post(body).build();

                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();

                    isDotNet();
                    final int code = response.code();
                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.SAMPLEPAPERSEXCEPTION.ordinal());
                    } else
                        publishBroadcast(code, Connection.SAMPLEPAPERS.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SAMPLEPAPERSEXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SAMPLEPAPERSEXCEPTION.ordinal());

                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SAMPLEPAPERSEXCEPTION.ordinal());

                } catch (Exception e) {

                    return;

                }
            }
        });
        thread.start();

    }

    public void getbilling(final String json) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.GET_EXAMS()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();

                    isDotNet();

                    final int code = response.code();
                    publishBroadcast(code, Connection.GET_EXAMS.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_EXAMSEXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_EXAMSEXCEPTION.ordinal());

                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_EXAMSEXCEPTION.ordinal());

                } catch (Exception e) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_EXAMSEXCEPTION.ordinal());

                    return;
                }
            }
        });
        thread.start();
       /* } else {
            publishBroadcast(Constants.STATUS_OK, Connection.NO_INTERNET.ordinal());
        }*/
    }


    public void getbillid(final String json, final int flag) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.getbillid()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();

                    isDotNet();
                    final int code = response.code();
                    if (flag == 1)
                        publishBroadcast(code, Connection.GetBillid.ordinal());
                    else
                        publishBroadcast(code, Connection.GetReceiptid.ordinal());

                } catch (ConnectException exception) {
                    if (flag == 1)
                        publishBroadcast(Constants.STATUS_OK, Connection.GetBillidException.ordinal());
                    else
                        publishBroadcast(Constants.STATUS_OK, Connection.GetReceiptidException.ordinal());
                } catch (UnknownHostException exception) {
                    if (flag == 1)
                        publishBroadcast(Constants.STATUS_OK, Connection.GetBillidException.ordinal());
                    else
                        publishBroadcast(Constants.STATUS_OK, Connection.GetReceiptidException.ordinal());

                } catch (SocketTimeoutException exception) {
                    if (flag == 1)
                        publishBroadcast(Constants.STATUS_OK, Connection.GetBillidException.ordinal());
                    else
                        publishBroadcast(Constants.STATUS_OK, Connection.GetReceiptidException.ordinal());
                } catch (Exception e) {
                    if (flag == 1)
                        publishBroadcast(Constants.STATUS_OK, Connection.GetBillidException.ordinal());
                    else
                        publishBroadcast(Constants.STATUS_OK, Connection.GetReceiptidException.ordinal());

                    return;
                }
            }
        });
        thread.start();
       /* } else {
            publishBroadcast(Constants.STATUS_OK, Connection.NO_INTERNET.ordinal());
        }*/
    }

    public void savepracticetest(final String samplejson) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                RequestBody body = RequestBody.create(Constants.JSON, samplejson);
                Request request = new Request.Builder().url(URLS.savepracticetest()).post(body).build();

                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();

                    isDotNet();
                    final int code = response.code();
                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.SavePracticeTestExcetion.ordinal());
                    } else
                        publishBroadcast(code, Connection.SavePracticeTest.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SavePracticeTestExcetion.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SavePracticeTestExcetion.ordinal());

                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SavePracticeTestExcetion.ordinal());

                } catch (Exception e) {

                    return;
                }
            }
        });
        thread.start();
    }

    public void getstudentpackage(final String json) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.getstudentpackage()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();

                    isDotNet();
                    final int code = response.code();
                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.GETPACKAGEEXCEPTION.ordinal());
                    } else
                        publishBroadcast(code, Connection.GETPACKAGE.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETPACKAGEEXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETPACKAGEEXCEPTION.ordinal());

                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETPACKAGEEXCEPTION.ordinal());

                } catch (Exception e) {

                    return;
                }
            }
        });
        thread.start();
    }

    public void getrewardpoints(final String json) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //Log.e("json ", json+ " url "+ URLS.reward_points());
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.reward_points()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();
                    isDotNet();
                    final int code = response.code();
                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.GETREWARDPOINTSEXCEPTION.ordinal());
                    } else
                        publishBroadcast(code, Connection.GETREWARDPOINTS.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETREWARDPOINTSEXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETREWARDPOINTSEXCEPTION.ordinal());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETREWARDPOINTSEXCEPTION.ordinal());
                } catch (Exception e) {
                    return;
                }
            }
        });
        thread.start();
    }

    public void getevent_feedback(final String json) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.getevent_feedback()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();
                    //isDotNet();
                    final int code = response.code();
                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.GETEVENTFEEDBACKEXCEPTION.ordinal());
                    } else
                        publishBroadcast(code, Connection.GETEVENTFEEDBACK.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETEVENTFEEDBACKEXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETEVENTFEEDBACKEXCEPTION.ordinal());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETEVENTFEEDBACKEXCEPTION.ordinal());
                } catch (Exception e) {
                    return;
                }
            }
        });
        thread.start();
    }

    public void getfill_feedback(final String json) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.getfill_feedback()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();
                    //isDotNet();
                    final int code = response.code();

                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.GET_FEEDBACKEXCEPTION.ordinal());
                    } else
                        publishBroadcast(code, Connection.GET_FEEDBACK.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_FEEDBACKEXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_FEEDBACKEXCEPTION.ordinal());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_FEEDBACKEXCEPTION.ordinal());
                } catch (Exception e) {
                    return;
                }
            }
        });
        thread.start();
    }

    public void save_questionFeedback(final String json) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.save_questionFeedback()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();
                    isDotNet();
                    final int code = response.code();
                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.SAVE_QUESTIONEXCEPTION.ordinal());
                    } else
                        publishBroadcast(code, Connection.SAVE_QUESTION.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SAVE_QUESTIONEXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SAVE_QUESTIONEXCEPTION.ordinal());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SAVE_QUESTIONEXCEPTION.ordinal());
                } catch (Exception e) {
                    return;
                }
            }
        });
        thread.start();
    }

    public void student_ebookget(final String json) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("json ", json + " url " + URLS.student_ebookget() );
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.student_ebookget()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();
                    isDotNet();

                    try {
                        JSONObject obj1 = new JSONObject(GlobalValues.TEMP_STR).optJSONObject("root");
                        JSONArray array = new JSONArray();

                        Object json = obj1.get("subroot");

                        if (json instanceof JSONArray) {
                            array = obj1.optJSONArray("subroot");
                            ContentValues c = new ContentValues();
                            c.put(DatabaseHelper.JSONDATA, array.toString());
                            c.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
                            DatabaseHelper.getInstance(ConnectionManager.this).save_subjects(c);
                            //Log.e("array valid course ",array.toString() );

                        } else {
                            array.put(obj1.optJSONObject("subroot"));
                            ContentValues c1 = new ContentValues();
                            c1.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
                            c1.put(DatabaseHelper.JSONDATA, array.toString());
                            DatabaseHelper.getInstance(ConnectionManager.this).save_subjects(c1);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    final int code = response.code();
                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.GET_STUDENTEBOOK_EXCEPTION.ordinal());
                    } else
                        publishBroadcast(code, Connection.GET_STUDENTEBOOK.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_STUDENTEBOOK_EXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_STUDENTEBOOK_EXCEPTION.ordinal());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_STUDENTEBOOK_EXCEPTION.ordinal());
                } catch (Exception e) {
                    return;
                }
            }
        });
        thread.start();
    }

    public void ebook_contentget(final String json, final int ebook_id, final String type) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("json ", json + " url " + URLS.ebook_contentget() );
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.ebook_contentget()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();
                    isDotNet();

                    try {
                        JSONObject obj1 = new JSONObject(GlobalValues.TEMP_STR).optJSONObject("root");
                        JSONArray array = new JSONArray();

                        Object json = obj1.get("subroot");

                        if (json instanceof JSONArray) {
                            array = obj1.optJSONArray("subroot");
                            ContentValues c = new ContentValues();
                            c.put(DatabaseHelper.JSONDATA, array.toString());
                            c.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
                            c.put(DatabaseHelper.EBOOK_ID, ebook_id);
                            c.put(DatabaseHelper.TYPE, type);
                            DatabaseHelper.getInstance(ConnectionManager.this).save_contentdetails(c);
                            //Log.e("array valid course ",array.toString() );

                        } else {
                            array.put(obj1.optJSONObject("subroot"));
                            ContentValues c1 = new ContentValues();
                            c1.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
                            c1.put(DatabaseHelper.JSONDATA, array.toString());
                            c1.put(DatabaseHelper.TYPE, type);
                            DatabaseHelper.getInstance(ConnectionManager.this).save_contentdetails(c1);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    final int code = response.code();
                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.GET_EBOOKCONTENT_EXCEPTION.ordinal());
                    } else
                        publishBroadcast(code, Connection.GET_EBOOKCONTENT.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_EBOOKCONTENT_EXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_EBOOKCONTENT_EXCEPTION.ordinal());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_EBOOKCONTENT_EXCEPTION.ordinal());
                } catch (Exception e) {
                    return;
                }
            }
        });
        thread.start();
    }

    public void report_error_forquestions(final String json) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.report_error_forquestion()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();

                    isDotNet();
                    final int code = response.code();
                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.REPORTERROREXCEPTION.ordinal());
                    } else
                        publishBroadcast(code, Connection.REPORTERROR.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.REPORTERROREXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.REPORTERROREXCEPTION.ordinal());

                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.REPORTERROREXCEPTION.ordinal());
                } catch (Exception e) {
                    return;
                }
            }
        });
        thread.start();
    }

    private void publishBroadcast(final int code, final int ordinal) {
        try {
            Intent intent = new Intent(Constants.BROADCAST_WIZARD);
            intent.putExtra(Constants.BROADCAST_RESPONSE_CODE, code);
            intent.putExtra(Constants.BROADCAST_URL_ACCESS, ordinal);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reporterror(final String json) {
        if (InternetUtils.getInstance(context).available()) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        RequestBody body = RequestBody.create(Constants.JSON, json);

                        Request request = new Request.Builder()
                                .url(URLS.report_error())
                                .post(body).build();

                        Response response = client.newCall(request).execute();


                    } catch (Exception e) {


                        return;
                    }
                }
            });
            thread.start();
        } else {

        }
    }

    public void getChat(final String samplejson) {

        Thread thread = new Thread(new Runnable() {


            @Override
            public void run() {
                RequestBody body = RequestBody.create(Constants.JSON, samplejson);

                Request request = new Request.Builder().url(URLS.getchatmessages()).post(body).build();
                // Log.e("Body :: ", body.toString());
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();
                    //  Log.e("GlobalValues:: ", GlobalValues.TEMP_STR);
                    isDotNet();
                    final int code = response.code();
                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.CHATMESSAGEEXCEPTION.ordinal());
                    } else
                        publishBroadcast(code, Connection.CHATMESSAGE.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.CHATMESSAGEEXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.CHATMESSAGEEXCEPTION.ordinal());
                    //  Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.CHATMESSAGEEXCEPTION.ordinal());
                    // Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (Exception e) {
                    // Log.e("error ", "error " + e.toString());
                    return;

                }
            }
        });
        thread.start();

    }

    public void saveChat(final String toString) {

        Thread thread = new Thread(new Runnable() {


            @Override
            public void run() {
                RequestBody body = RequestBody.create(Constants.JSON, toString);

                Request request = new Request.Builder().url(URLS.savechatmessages()).post(body).build();
                // Log.e("Body :: ", body.toString());
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();
                    // Log.e("GlobalValues:: ", GlobalValues.TEMP_STR);
                    isDotNet();
                    final int code = response.code();
                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.SAVECHATMESSAGEEXCEPTION.ordinal());
                    } else
                        publishBroadcast(code, Connection.SAVECHATMESSAGE.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SAVECHATMESSAGEEXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SAVECHATMESSAGEEXCEPTION.ordinal());
                    //  Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SAVECHATMESSAGEEXCEPTION.ordinal());
                    //  Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (Exception e) {
                    // Log.e("error ", "error " + e.toString());
                    return;

                }
            }
        });
        thread.start();


    }

    public void getChat1(final String toString) {


        Thread thread = new Thread(new Runnable() {


            @Override
            public void run() {
                RequestBody body = RequestBody.create(Constants.JSON, toString);

                Request request = new Request.Builder().url(URLS.tokendetailschatmessages()).post(body).build();
                // Log.e("Body :: ", body.toString());
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();
                    // Log.e("GlobalValues:: ", GlobalValues.TEMP_STR);
                    isDotNet();
                    final int code = response.code();

                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.TOKENDETAILSException.ordinal());
                    } else
                        publishBroadcast(code, Connection.TOKENDETAILS.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.TOKENDETAILSException.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.TOKENDETAILSException.ordinal());
                    //  Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.TOKENDETAILSException.ordinal());
                    //  Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (Exception e) {
                    // Log.e("error ", "error " + e.toString());
                    return;

                }
            }
        });
        thread.start();


    }


    public void getChat2(final String samplejson) {

        Thread thread = new Thread(new Runnable() {


            @Override
            public void run() {
                RequestBody body = RequestBody.create(Constants.JSON, samplejson);

                Request request = new Request.Builder().url(URLS.getchatmessages()).post(body).build();
                // Log.e("Body :: ", body.toString());
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();
                    //  Log.e("GlobalValues:: ", GlobalValues.TEMP_STR);
                    //  Log.e("GlobalValues:: "," kkkk"+ GlobalValues.TEMP_STR);
                    isDotNet();
                    final int code = response.code();
                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.CHATMESSAGEEXCEPTION1.ordinal());
                    } else
                        publishBroadcast(code, Connection.CHATMESSAGE1.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.CHATMESSAGEEXCEPTION1.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.CHATMESSAGEEXCEPTION1.ordinal());
                    // Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.CHATMESSAGEEXCEPTION1.ordinal());
                    //  Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (Exception e) {
                    //  Log.e("error ", "error " + e.toString());
                    return;

                }
            }
        });
        thread.start();

    }

    public void updatechat(final String toString) {

        Thread thread = new Thread(new Runnable() {


            @Override
            public void run() {
                RequestBody body = RequestBody.create(Constants.JSON, toString);

                Request request = new Request.Builder().url(URLS.savechatmessages()).post(body).build();
                // Log.e("Body :: ", body.toString());
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();
                    // Log.e("GlobalValues:: ", GlobalValues.TEMP_STR);
                    isDotNet();
                    final int code = response.code();

                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.UPDATEMESSAGEEXCEPTION.ordinal());
                    } else
                        publishBroadcast(code, Connection.UPDATEMESSAGE.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.UPDATEMESSAGEEXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.UPDATEMESSAGEEXCEPTION.ordinal());
                    //  Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.UPDATEMESSAGEEXCEPTION.ordinal());
                    //Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (Exception e) {
                    //    Log.e("error ", "error " + e.toString());
                    return;
                }
            }
        });
        thread.start();
    }

    public void getimages(final String toString) {
        //  Log.e("url ", "url " + toString + " " + URLS.splash_screen_images());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(Constants.JSON, toString);
                    Request request = new Request.Builder().url(URLS.splash_screen_images()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();

                    isDotNet();
                    final int code = response.code();
                    //   Log.e("data ", "data " + code);
                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.SPLASHSCREENEXCEPTION1.ordinal());
                    } else
                        publishBroadcast(code, Connection.SPLASHSCREEN1.ordinal());
                } catch (ConnectException exception) {
                    Log.e("ConnectException", "SPLASHSCREENEXCEPTION1");
                    publishBroadcast(Constants.STATUS_OK, Connection.SPLASHSCREENEXCEPTION1.ordinal());
                } catch (UnknownHostException exception) {
                    Log.e("UnknownHostException", "SPLASHSCREENEXCEPTION1");
                    publishBroadcast(Constants.STATUS_OK, Connection.SPLASHSCREENEXCEPTION1.ordinal());
                } catch (SocketTimeoutException exception) {
                    Log.e("SocketTimeoutException", "SPLASHSCREENEXCEPTION1");
                    publishBroadcast(Constants.STATUS_OK, Connection.SPLASHSCREENEXCEPTION1.ordinal());

                } catch (Exception e) {

                    return;
                }
            }
        });
        thread.start();
    }

    public void getcategorydata() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder().url(URLS.getcategoryurl()).build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();
                    //isDotNet();
                    final int code = response.code();
                    if (code != Constants.STATUS_OK) {
                        publishBroadcast(Constants.STATUS_OK, Connection.CATEGORYEXCEPTION.ordinal());
                    } else
                        publishBroadcast(code, Connection.GETCATEGORYDATA.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.CATEGORYEXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.CATEGORYEXCEPTION.ordinal());

                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.CATEGORYEXCEPTION.ordinal());

                } catch (Exception e) {

                    return;

                }
            }
        });
        thread.start();


    }
}
