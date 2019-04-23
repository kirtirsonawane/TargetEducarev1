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

public class ConnectionManager {
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
        GlobalValues.TEMP_STR = GlobalValues.TEMP_STR.substring(1,
                GlobalValues.TEMP_STR.length() - 1);
        GlobalValues.TEMP_STR = GlobalValues.TEMP_STR.replace("\\n", " ");
        GlobalValues.TEMP_STR = GlobalValues.TEMP_STR.replace("\\t", " ");
        GlobalValues.TEMP_STR = GlobalValues.TEMP_STR.replace("\\r", " ");
        GlobalValues.TEMP_STR = GlobalValues.TEMP_STR.replace("\\", "");
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
        Log.e("json ", "json " + json);
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
                    Log.e("res ", "res " + GlobalValues.TEMP_STR);
                    final int code = response.code();
                    publishBroadcast(code, Connection.LOGIN.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.LOGINEXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.LOGINEXCEPTION.ordinal());
                    Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.LOGINEXCEPTION.ordinal());
                    Log.e("SocketTimeoutException ", "error " + exception.toString());
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

    public void getexam(final String json) {
        Log.e("json ", "json " + json + " " + URLS.GET_EXAMS());
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
                    Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_EXAMSEXCEPTION.ordinal());
                    Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (Exception e) {
                    Log.e("error ", "error " + e.toString());
                    return;
                }
            }
        });
        thread.start();
       /* } else {
            publishBroadcast(Constants.STATUS_OK, Connection.NO_INTERNET.ordinal());
        }*/
    }

    public void getquestion(final String json, final int examid, final String language) {
        Log.e("json ", "json " + json + " " + URLS.GET_EXAMS());
        // if (InternetUtils.getInstance(context).available()) {
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
                                qdata.add(new QuestionURL(matcher.group(1),"que",quesdata.get(i).getId()));
                            }

                            Matcher matcherexplanation = pattern.matcher(quesdata.get(i).getExplanation());
                            while (matcherexplanation.find()) {
                                qdata.add(new QuestionURL(matcherexplanation.group(1),"explanation",quesdata.get(i).getId()));
                            }

                            for (int j=0;j<quesdata.get(i).getOptions().size();j++)
                            {
                                Matcher matcheropt = pattern.matcher(quesdata.get(i).getOptions().get(j).getName());
                                while (matcheropt.find()) {
                                    qdata.add(new QuestionURL(matcheropt.group(1),"ans",quesdata.get(i).getOptions().get(j).getId()));
                                }
                            }
                        }
                        ((Activitycommon)context).downloaddata(qdata,examid,language,obj.toString());

                     /*   for (int i=0;i<qdata.size();i++)
                        {
                            Log.e("imagedata "," "+qdata.get(i).getImagemainsource()+" "+qdata.get(i).getType());

                        }*/

                    }


                    //  getquestionurl(examid);
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_QUESTIONSEXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_QUESTIONSEXCEPTION.ordinal());
                    Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_QUESTIONSEXCEPTION.ordinal());
                    Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (Exception e) {
                    Log.e("error ", "error " + e.toString());
                    return;
                }
            }
        });
        thread.start();
       /* } else {
            publishBroadcast(Constants.STATUS_OK, Connection.NO_INTERNET.ordinal());
        }*/
    }


    public void getquestionurl(final int examid) {
        JSONObject obj = new JSONObject();
        final JSONObject mainobj = new JSONObject();
        try {
            obj.put("examid", examid);
            obj.put("ip", "http://" + GlobalValues.IP);
            mainobj.put("FilterParameter", obj.toString());
        } catch (Exception e) {
            Log.e("error ", "error ");
        }
        //   if (InternetUtils.getInstance(context).available()) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("mainobj ", "mainobj " + mainobj.toString());
                    RequestBody body = RequestBody.create(Constants.JSON, mainobj.toString());
                    Request request = new Request.Builder().url(URLS.QUESTION_URL()).post(body).build();
                    Response response = client.newCall(request).execute();
                    String res = response.body().string();
                    res = isDotNet(res);
                    Log.e("res url ", "res url " + res.toString());
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
                                DatabaseHelper.getInstance(context).savequestionurl(c, qurldata.get(i).getId(), qurldata.get(i).getType(),qurldata.get(i).getImagemainsource());
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
                    Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GET_QUESTIONSEXCEPTION.ordinal());
                    Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (Exception e) {
                    Log.e("error ", "error " + e.toString());
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
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("json1 ", "json1 " + data1 + " " + URLS.Updatedata());
                    RequestBody body1 = RequestBody.create(Constants.JSON, data1);
                    Request request1 = new Request.Builder().url(URLS.Updatedata()).post(body1).build();
                    Response response1 = client.newCall(request1).execute();
                    GlobalValues.TEMP_STR = response1.body().string();
                    isDotNet();
                    Log.e("resUpdatedata ", "res " + GlobalValues.TEMP_STR);
                    ;
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.SAVE_ANSWERSHEET()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();
                    isDotNet();
                    Log.e("res ", "res " + GlobalValues.TEMP_STR);
                    final int code = response.code();
                    DatabaseHelper.getInstance(context).updatesync(id1);
                    String id = new JSONObject(GlobalValues.TEMP_STR).getJSONObject("root").getJSONObject("subroot").getString("id");

                    JSONObject obj = new JSONObject(data);
                    obj.put("resultid", id);
                    Log.e("answerdataaaa11111", "obj " + obj.toString());
                    UpdateOrder(obj.toString());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SAVE_ANSWEREXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SAVE_ANSWEREXCEPTION.ordinal());
                    Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SAVE_ANSWEREXCEPTION.ordinal());
                    Log.e("SocketTimeoutException ", "error " + exception.toString());
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
                    Log.e("res ", "res " + GlobalValues.TEMP_STR);
                    final int code = response.code();

                    DatabaseHelper.getInstance(context).deleteanswer();
                    publishBroadcast(code, Connection.SAVE_ANSWER.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SAVE_ANSWEREXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SAVE_ANSWEREXCEPTION.ordinal());
                    Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SAVE_ANSWEREXCEPTION.ordinal());
                    Log.e("SocketTimeoutException ", "error " + exception.toString());
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
                    Log.e("json2 ", "json2 " + json + " " + URLS.Updatedata());
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.Updatedata()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();
                    isDotNet();
                    Log.e("res ", "res " + GlobalValues.TEMP_STR);
                    final int code = response.code();
                      /*  String id = new JSONObject(GlobalValues.TEMP_STR).getJSONObject("root").getJSONObject("subroot").getString("id");

                        JSONObject obj = new JSONObject(data);
                        obj.put("resultid", id);
                        Log.e("obj", "obj " + obj.toString());*/

                    DatabaseHelper.getInstance(context).deleteanswer();
                    publishBroadcast(code, Connection.SAVE_ANSWER.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SAVE_ANSWEREXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SAVE_ANSWEREXCEPTION.ordinal());
                    Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SAVE_ANSWEREXCEPTION.ordinal());
                    Log.e("SocketTimeoutException ", "error " + exception.toString());
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
        Log.e("json ", "json " + json + " " + URLS.loginv1());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.loginv1()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();
                    Log.e("Globalvalues",GlobalValues.TEMP_STR);
                    isDotNet();
                    final int code = response.code();
                    publishBroadcast(code, Connection.LOGINV1.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.LOGINV1EXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.LOGINV1EXCEPTION.ordinal());
                    Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.LOGINV1EXCEPTION.ordinal());
                    Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (Exception e) {
                    Log.e("error ", "error " + e.toString());
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
        Log.e("json ", "json " + json + " " + URLS.signup());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.signup()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();
                    Log.e("Globalvalues",GlobalValues.TEMP_STR);
                    isDotNet();
                    final int code = response.code();
                    publishBroadcast(code, Connection.SIGNUP.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SIGNUPEXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SIGNUPEXCEPTION.ordinal());
                    Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.SIGNUPEXCEPTION.ordinal());
                    Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (Exception e) {
                    Log.e("error ", "error " + e.toString());
                    return;
                }
            }
        });
        thread.start();
    }

    public void getcategory(final String json) {
        Log.e("json ", "json " + json + " " + URLS.getcategory());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.getcategory()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();
                    Log.e("Globalvalues",GlobalValues.TEMP_STR);
                    isDotNet();
                    final int code = response.code();
                    publishBroadcast(code, Connection.GETCATEGORY.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETCATEGORYEXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETCATEGORYEXCEPTION.ordinal());
                    Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETCATEGORYEXCEPTION.ordinal());
                    Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (Exception e) {
                    Log.e("error ", "error " + e.toString());
                    return;
                }
            }
        });
        thread.start();
    }

    public void editprofile(final String json) {
        Log.e("json ", "json " + json + " " + URLS.editprofile());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.editprofile()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();
                    Log.e("Globalvalues",GlobalValues.TEMP_STR);
                    isDotNet();
                    final int code = response.code();
                    publishBroadcast(code, Connection.EDITPROFILE.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.EDITPROFILEEXCPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.EDITPROFILEEXCPTION.ordinal());
                    Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.EDITPROFILEEXCPTION.ordinal());
                    Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (Exception e) {
                    Log.e("error ", "error " + e.toString());
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
                    Log.e("Globalvalues",GlobalValues.TEMP_STR);
                    isDotNet();
                    final int code = response.code();
                    publishBroadcast(code, Connection.GETCITY.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETCITYEXCPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETCITYEXCPTION.ordinal());
                    Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETCITYEXCPTION.ordinal());
                    Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (Exception e) {
                    Log.e("error ", "error " + e.toString());
                    return;
                }
            }
        });
        thread.start();
    }

    public void updateprofile(final String json) {
        Log.e("json ", "json " + json + " " + URLS.updateprofile());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.updateprofile()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();
                    Log.e("Globalvalues",GlobalValues.TEMP_STR);
                    isDotNet();
                    final int code = response.code();
                    publishBroadcast(code, Connection.UPDATEPROFILE.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.UPDATEPROFILEEXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.UPDATEPROFILEEXCEPTION.ordinal());
                    Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.UPDATEPROFILEEXCEPTION.ordinal());
                    Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (Exception e) {
                    Log.e("error ", "error " + e.toString());
                    return;
                }
            }
        });
        thread.start();
    }
    public void getcourse(final String json) {
        Log.e("json ", "json " + json + " " + URLS.getcourse());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(Constants.JSON, json);
                    Request request = new Request.Builder().url(URLS.getcourse()).post(body).build();
                    Response response = client.newCall(request).execute();
                    GlobalValues.TEMP_STR = response.body().string();
                    Log.e("Globalvalues",GlobalValues.TEMP_STR);
                    isDotNet();
                    final int code = response.code();
                    publishBroadcast(code, Connection.GETCOURSE.ordinal());
                } catch (ConnectException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETCOURSEEXCEPTION.ordinal());
                } catch (UnknownHostException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETCOURSEEXCEPTION.ordinal());
                    Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (SocketTimeoutException exception) {
                    publishBroadcast(Constants.STATUS_OK, Connection.GETCOURSEEXCEPTION.ordinal());
                    Log.e("SocketTimeoutException ", "error " + exception.toString());
                } catch (Exception e) {
                    Log.e("error ", "error " + e.toString());
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
}
