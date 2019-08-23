package com.targeteducare;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.targeteducare.Adapter.Adapter_Start_Chat;
import com.targeteducare.Classes.ChatQuestionsNAnswers;
import com.targeteducare.Classes.MessagesModel;
import com.targeteducare.database.DatabaseHelper;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPReply;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class ActivityStartChat extends Activitycommon {
    RecyclerView recyclerView;
    TextView tokenView;
    TextView tokenView1;
    TextView Reopentoken;
    ImageView send, Attach_img;
    static String token = "";
    static String title = "";
    static String Status = "";
    private static final int GALLERY = 1;
    private static final int Addtext = 0;
    /*  MessagesModel messagesModel;*/
    ArrayList<MessagesModel> models = new ArrayList<>();
    ArrayList<ChatQuestionsNAnswers> list = new ArrayList<>();
    EditText editTextmsg;
    Adapter_Start_Chat adapterChatFaq;
    DatabaseHelper mydb;
    static boolean flag = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_start_chat);
            setmaterialDesign();
            toolbar.setTitleMargin(25, 10, 10, 10);
            setTitle("PEAK");
            back();
            genloading("loading...");
            try {
                send = (ImageView) findViewById(R.id.send_chat_start);
                Attach_img = (ImageView) findViewById(R.id.attach_start_chat);
                editTextmsg = (EditText) findViewById(R.id.edit_text_chat_start);
                Reopentoken = (TextView) findViewById(R.id.reopen_token);
                mydb = new DatabaseHelper((ActivityStartChat.this));
                Intent intent = getIntent();
                Bundle args = intent.getBundleExtra("bundle");
                ArrayList<ChatQuestionsNAnswers> chatQuestionsNAnswers = (ArrayList<ChatQuestionsNAnswers>) args.getSerializable("ARRAY1");
                ArrayList<MessagesModel> models1 = (ArrayList<MessagesModel>) args.getSerializable("ARRAY");
                // Log.e("array list", "mmmmm " + models1.get(0).getTokenId());
                token = intent.getStringExtra("token");
                title = intent.getStringExtra("title");
                Status = intent.getStringExtra("Status");
                final String msg = intent.getStringExtra("msg");
                String time = intent.getStringExtra("time");
                final int CategoryId = args.getInt("categoryid");
                final int positon = (args.getInt("position"));

                /* messagesModel = new MessagesModel();*/
                //  ChatQuestionsNAnswers questionsNAnswers = new ChatQuestionsNAnswers();


                // list = new ArrayList<>();
                //  Toast.makeText(ActivityStartChat.this, "Token is " + token, Toast.LENGTH_LONG).show();
                tokenView = (TextView) findViewById(R.id.token_start);
                tokenView1 = (TextView) findViewById(R.id.token_title);
                tokenView.setText("Token number : " + token);
                tokenView1.setText("Title : " + title);
                recyclerView = (RecyclerView) findViewById(R.id.recycle_chat_start);
                final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                linearLayoutManager.setStackFromEnd(true);


                if (Status.equalsIgnoreCase("Resolved") && token == token) {

                    send.setVisibility(View.GONE);
                    Attach_img.setVisibility(View.GONE);
                    editTextmsg.setVisibility(View.GONE);
                    Reopentoken.setVisibility(View.VISIBLE);
                }
                Reopentoken.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        send.setVisibility(View.VISIBLE);
                        editTextmsg.setVisibility(View.VISIBLE);
                        Attach_img.setVisibility(View.VISIBLE);
                        flag = false;
                        reopen();
                        Reopentoken.setVisibility(View.GONE);


                    }
                });


                Attach_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chooseFromGallary();
                    }
                });


                try {
                    // if (chattoken.equalsIgnoreCase(token)) {


                    JSONObject jsonObject = new JSONObject();
                    JSONObject jsonObject1 = new JSONObject();
                    try {
            /*jsonObject.put("", Constants.Packages_page_no);
            jsonObject.put("NoofRecords", Constants.Packages_no_of_records);
            jsonObject.put("CategoryId", GlobalValues.student.getCategoryId());*/
                        jsonObject.put("StudentId", GlobalValues.student.getId());
                        jsonObject1.put("FilterParameter", jsonObject.toString());

                        // ConnectionManager.getInstance(ActivityStartChat.this).getChat2(jsonObject1.toString());
                    } catch (Exception e) {
                        e.printStackTrace();


                    }

                    ConnectionManager.getInstance(ActivityStartChat.this).getChat2(jsonObject1.toString());


                    recyclerView.setLayoutManager(linearLayoutManager);
                    /*adapterChatFaq.notifyDataSetChanged();*/


                    //  }
                } catch (Exception e) {
                    e.printStackTrace();
                }






/*

        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {
            */
/*jsonObject.put("", Constants.Packages_page_no);
            jsonObject.put("NoofRecords", Constants.Packages_no_of_records);
            jsonObject.put("CategoryId", GlobalValues.student.getCategoryId());*//*

            jsonObject.put("StudentId",1*/
                /* GlobalValues.student.getId()*//*
);
            jsonObject1.put("FilterParameter", jsonObject.toString());
            Log.e("parameters ::  :: ", jsonObject1.toString());
            //  ConnectionManager.getInstance(ActivityChatFaq.this).getChat(jsonObject1.toString());
        } catch (Exception e) {
            e.printStackTrace();


        }

        ConnectionManager.getInstance(ActivityStartChat.this).getChat1(jsonObject1.toString());
*/


                try {

                    JSONArray jsonArray = new JSONArray();
                    jsonArray = (mydb.getdatastartchat(Integer.parseInt(token)));


                    for (int i = 0; i < jsonArray.length(); i++) {


                        ChatQuestionsNAnswers questionsNAnswers = new ChatQuestionsNAnswers();

                        JSONObject object = jsonArray.getJSONObject(i);

                        questionsNAnswers.setId(object.getString(DatabaseHelper.ID));
                        questionsNAnswers.setToken(object.getString(DatabaseHelper.TokenId));
                        //  questionsNAnswers.setDate(object.getString("SAVEDTIME"));


                        String data = object.getString(DatabaseHelper.JSONDATA);

                        JSONArray jsonArray1 = new JSONArray(data);
                        //  MessagesModel model1 = new MessagesModel();

                        for (int j = 0; j < jsonArray1.length(); j++) {
                            MessagesModel model = new MessagesModel();
                            JSONObject object3 = jsonArray1.getJSONObject(j);
                            model.setMessage(object3.getString("Message"));
                            model.setSenderId(object3.getString("SenderId"));
                            model.setTime(object.getString("Time"));
                  /*  model.setID(object.getString("TokenDetailId"));
                    model.setTokenId(object.getString("TokenId"));*/
                            models.add(model);

                        }
                        //   questionsNAnswers.setMessagesModels(models);
                        // Log.e("msg ", " msginlist obj " + questionsNAnswers.getMessagesModels().get(0).getMessage());
                        //   MessagesModel model=new MessagesModel();
                        //    questionsNAnswers.setSenderId("1");

                        //   models.add(model);
                  /*  for (int i1 = 0; i1 < models.size(); i1++) {
                        Log.e("list 11 ", "size msg " + models.get(i1).getMessage());
                        Log.e("list 11 ", "sender id " + models.get(i1).getSenderId());

                    }
*/
                        list.add(questionsNAnswers);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
/*
        Log.e("listsize ", "msg " + list.size());
        adapterChatFaq = new Adapter_Start_Chat(ActivityStartChat.this,models );
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterChatFaq);*/


                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (editTextmsg.getText().toString() == "" || editTextmsg.getText().toString() == null || editTextmsg.getText().toString().length() < 1) {
                            Toast.makeText(context, "pls enter msg", Toast.LENGTH_LONG).show();
                        } else {

                            sendrequest("", editTextmsg.getText().toString());

                    /*ChatQuestionsNAnswers questionsNAnswers = new ChatQuestionsNAnswers();


                    MessagesModel messagesModel = new MessagesModel();
                    messagesModel.setMessage(editTextmsg.getText().toString());
                    models.add(messagesModel);
                    Calendar cal = Calendar.getInstance();
                    String d = DateUtils.formatDate(cal.getTime(), "' date:' dd-MM-yyyy' Time:'HH:mm");
                    messagesModel.setTime(d);
                  //  models.add(messagesModel);
                    messagesModel.setSenderId(GlobalValues.student.getId());
                  //  models.add(messagesModel);
                    questionsNAnswers.setMessagesModels(models);*/

                   /* JSONObject jsonObject = new JSONObject();
                    JSONObject jsonObject1 = new JSONObject();
                    // ArrayList<MessagesModel> model_1 = new ArrayList<>();

                    ChatQuestionsNAnswers questionsNAnswers = new ChatQuestionsNAnswers();
                    Calendar cal = Calendar.getInstance();
                    String d = DateUtils.formatDate(cal.getTime(), "yyyy-MM-dd HH:mm");*/

                            //Log.e("log "," "+Id);



                    /* <TokenId>1</TokenId>
                          <ReplyAnswer>ABCDEFGHIJKL</ReplyAnswer>
                          <ReplyBy>1</ReplyBy>
                          <ReplyType>Enquiry</ReplyType>
                          <ReplyDate>2019-06-05</ReplyDate>
                          <FileUrl>/Abc/Por/Xyz</FileUrl>*/


                   /* try {
                        JSONObject jsonObject2 = new JSONObject();
                        JSONObject jsonObject3 = new JSONObject();
                        JSONObject jsonObject4 = new JSONObject();
                        JSONObject jsonObject5 = new JSONObject();
                        jsonObject.put("TokenId", token);
                        // Log.e("Categoryid "," id "+CategoryId);
                        jsonObject.put("ReplyAnswer", editTextmsg.getText().toString());
                        // Log.e("Categoryid "," id "+CategoryId);
                        jsonObject.put("ReplyBy", GlobalValues.student.getId());
                        jsonObject.put("ReplyDate", d);
                        *//*  jsonObject.put("StudentId", 1*//**//*GlobalValues.student.getId()*//**//*);*//*

                        jsonObject.put("FileUrl", URLS.savechatmessages());

                        jsonObject2.put("subroot", jsonObject);
                        jsonObject3.put("root", jsonObject2);
                        jsonObject4.put("xml", jsonObject3.toString());
                        jsonObject4.put("Flag", "Save");
                        jsonObject1.put("FilterParameter", jsonObject4.toString());
                        *//*jsonObject1.put("FilterParameter",jsonObject5);*//*

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                   /* Log.e("json ", "data " + jsonObject1);

                    Log.e("json ", "data " + jsonObject1);
                    MessagesModel messagesModel = new MessagesModel();
                    messagesModel.setMessage(editTextmsg.getText().toString());
                    messagesModel.setSenderId(GlobalValues.student.getId());
                    messagesModel.setTime(d);
                    Log.e("message model ", "sender id " + messagesModel.getSenderId());
                    // models.add(messagesModel);
                    questionsNAnswers.setMessagesModels(models);*/
                            //      Log.e("question ", " " + questionsNAnswers.getMessagesModels().get(0).getMessage());
                            // list.add(questionsNAnswers);
                            /* adapterChatFaq.notifyDataSetChanged();*/
                            // ConnectionManager.getInstance(ActivityStartChat.this).getChat1(jsonObject1.toString());

                            ///   modeldata(models, token);
                            // Log.e("list ", "sender id " + models.get(0).getSenderId());
                  /*  for (int i = 0; i < list.size(); i++) {
                     //   Log.e("id  ", "activity11 " + models.get(i).getMessage());
                    }
*/
                            // update(list,token);
                            /*   ConnectionManager.getInstance(ActivityStartChat.this).getChat1(jsonObject1.toString());*/

                            recyclerView.setLayoutManager(linearLayoutManager);
                            /* recyclerView.setAdapter(adapterChatFaq);*/
                            editTextmsg.setText("");
                        }
                    }

                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reopen() {


        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        JSONObject jsonObject3 = new JSONObject();
        JSONObject jsonObject4 = new JSONObject();

        try {
            jsonObject.put("TokenId", token);
            jsonObject.put("Status", "Pending");
            jsonObject2.put("subroot", jsonObject);
            jsonObject3.put("root", jsonObject2);
            jsonObject4.put("xml", jsonObject3.toString());
            jsonObject4.put("Flag", "Update");

            jsonObject1.put("FilterParameter", jsonObject4.toString());

            ConnectionManager.getInstance(ActivityStartChat.this).updatechat(jsonObject1.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*status,flag-update,tokenid.*/

    }

  /*  public void update(ArrayList<ChatQuestionsNAnswers> list, String token) {
        ContentValues c1 = new ContentValues();

        Log.e("id  ", "activity11 " + list.size());


        for (int i = 0; i < list.size(); i++) {
            Log.e("id  ", "activity11 " + list.get(i).getMessagesModels());


            Gson gson = new Gson();

            Log.e("content value", " " + list.size());
            String data = gson.toJson(list);

            c1.put(DatabaseChatHelper.JSONDATA, data);
            Log.e("content value check", "hii " + c1.toString());

            c1.put(DatabaseChatHelper.JSONDATA, data);


        }
        mydb.savechatmessage(c1, Integer.parseInt(token));


    }*/


    public void chooseFromGallary() {
        try {

            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(galleryIntent, GALLERY);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == GALLERY) {
                Bitmap bitmap;
                if (data != null) {
                    Uri contentURI = data.getData();
                    String[] proj = {MediaStore.Images.Media.DATA};

                    try {
                        String result = null;

                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                        Cursor cursor = context.getContentResolver().query(contentURI, proj, null, null, null);
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                                result = cursor.getString(column_index);
                            }
                            cursor.close();
                        }


                        //String path = saveImage(bitmap);
                        //    Log.e("path ",path);

                        Upload_image_adapter upload_image_adapter = new Upload_image_adapter();
                        upload_image_adapter.genloading1("loading....");
                        upload_image_adapter.execute(result);

                        /*profile_pic.setImageBitmap(bitmap);*/


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


            try {
                if (requestCode == Addtext) {

                    String url = data.getStringExtra("url");
                    String text = data.getStringExtra("text");
                    sendrequest(url, text);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void modeldata(ArrayList<MessagesModel> list, String t) {
        try {


            ArrayList<MessagesModel> models = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {
                MessagesModel model = new MessagesModel();
                //   t=list.get(i).getToken().toString();
                for (int j = 0; j < list.get(i).getMessage().length(); j++) {
                    String msg = " " + list.get(i).getMessage();
                    model.setTime(list.get(i).getTime());
                    model.setSenderId(list.get(i).getSenderId());
                    model.setFileUrl(list.get(i).getFileUrl());
                    model.setMessage(msg);
                    models.add(model);
                    //}

                }
                update1(models, t);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void update1(ArrayList<MessagesModel> list, String token) {
        ContentValues c1 = new ContentValues();
        try {


            for (int i = 0; i < list.size(); i++) {
                //  Log.e("id  ", "activity11 " + list.get(i).getMessage());


                Gson gson = new Gson();


                String data = gson.toJson(list);

                c1.put(DatabaseHelper.JSONDATA, data);


                // c1.put(DatabaseChatHelper.JSONDATA, data);


            }
            mydb.savechatmessage(c1, Integer.parseInt(token));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResponsed(int statuscode, int accesscode, String data) {
        super.onResponsed(statuscode, accesscode, data);
        try {


            if (statuscode == Constants.STATUS_OK) {
                if (accesscode == Connection.TOKENDETAILS.ordinal()) {
                    data = GlobalValues.TEMP_STR;

                    if (data != null) {
                        try {

                            MessagesModel questionsNAnswers = new MessagesModel();
                            JSONObject jsonObject2 = new JSONObject(data);
                            JSONObject jsonObject3 = jsonObject2.getJSONObject("root");

                            JSONObject jsonObject4 = jsonObject3.getJSONObject("subroot");
                            questionsNAnswers.setID(jsonObject4.getString("TokenDetailId"));
                            questionsNAnswers.setMessage(jsonObject4.getString("Reply"));
                            // questionsNAnswers.setMessage(jsonObject4.getString("Title"));
                            questionsNAnswers.setFileUrl(jsonObject4.getString("FileUrl"));
                            Calendar cal = Calendar.getInstance();
                            String d = DateUtils.formatDate(cal.getTime(), "yyyy-MM-dd HH:mm");
                            questionsNAnswers.setTime(d);
                            questionsNAnswers.setSenderId(GlobalValues.student.getId());
                            questionsNAnswers.setTokenId(token);

                            models.add(questionsNAnswers);
                            modeldata(models, token);
                            ChatQuestionsNAnswers questionsNAnswers1 = new ChatQuestionsNAnswers();
                            questionsNAnswers1.setMessagesModels(models);

                            list.add(questionsNAnswers1);

                            Adapter_Start_Chat adapterChatFaq = new Adapter_Start_Chat(ActivityStartChat.this, models, token);
                            //adapterChatFaq = new Adapter_Start_Chat(ActivityStartChat.this, models);
                            //  recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(adapterChatFaq);
                            //       Log.e("Array     ", list.size() + " " + list.toString()+" "+list.get(13).getToken());
                            adapterChatFaq.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //}

                } else if (accesscode == Connection.TOKENDETAILSException.ordinal()) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Connectiontimeout), Toast.LENGTH_LONG).show();
                } else if (accesscode == Connection.CHATMESSAGE1.ordinal()) {
                    data = GlobalValues.TEMP_STR;

                    if (data != null) {
                        try {

                            JSONObject jsonObject2 = new JSONObject(data);
                            JSONObject jsonObject3 = jsonObject2.getJSONObject("root");
                            //   Log.e("Array 1    ", /*list.size()*/  " " + models.size());
                            models.addAll(MessagesModel.getjson(jsonObject3, token));

                            adapterChatFaq = new Adapter_Start_Chat(ActivityStartChat.this, models, token);
                            recyclerView.setAdapter(adapterChatFaq);
                            adapterChatFaq.notifyDataSetChanged();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (accesscode == Connection.CHATMESSAGEEXCEPTION1.ordinal()) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Connectiontimeout), Toast.LENGTH_LONG).show();
                } else if (accesscode == Connection.UPDATEMESSAGE.ordinal()) {
                    data = GlobalValues.TEMP_STR;

                    if (data != null) {
                        try {

                            JSONObject jsonObject2 = new JSONObject(data);
                            JSONObject jsonObject3 = jsonObject2.getJSONObject("root");
                            JSONObject jsonObject = jsonObject3.getJSONObject("subroot");
                            String token1 = jsonObject.getString("TokenId");
                            //   Log.e("Array 1    ", /*list.size()*/  " " + models.size());
                       /* models.addAll(MessagesModel.getjson(jsonObject3, token));
                        Log.e("Array     ", *//*list.size()*//*  " " + models.size());*/
                            //   Log.e("ArrayUrl ", " " + models.get(12).getFileUrl());


                        /*adapterChatFaq = new Adapter_Start_Chat(ActivityStartChat.this, models, token);
                        recyclerView.setAdapter(adapterChatFaq);
                        adapterChatFaq.notifyDataSetChanged();*/

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (accesscode == Connection.UPDATEMESSAGEEXCEPTION.ordinal()) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Connectiontimeout), Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enlargeimage(int i, String fileUrl) {
        try {
            String i1 = String.valueOf(i);
            Intent intent = new Intent(ActivityStartChat.this, ActivityEnlargeImage.class);
            // intent.putExtra("i",i);
            intent.putExtra("url", fileUrl);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class Upload_image_adapter extends AsyncTask<String, Void, String> {
        long stime = System.currentTimeMillis();
        String ftpServerAddress = "103.241.181.144";
        String userName = "dentsoftinftechocom";
        String password = "Denso@44F";
        String filepath = "";
        String path = "";

        boolean isuploaded = true;

        @Override
        protected String doInBackground(String... url) {
            // TODO Auto-generated method stub
            // Log.e("Path in upload back",path);

            FTPClient ftpclient = new FTPClient();
            FileInputStream fis = null;
            boolean result;
            String status = "N";
            long millisec = System.currentTimeMillis();

            try {
                for (int i = 0; i < url.length; i++) {

                    path = url[i];
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                ftpclient.connect(ftpServerAddress, 2112);
                result = ftpclient.login(userName, password);
                ftpclient.setBufferSize(1024000);
                if (result == true) {
                } else {
                    return null;
                }

                ftpclient.setFileType(FTP.BINARY_FILE_TYPE);
                ftpclient.changeWorkingDirectory("www/EducationalProgram/Delegates/");
                File file = new File(path);

                Calendar cal = Calendar.getInstance();
                String d = DateUtils.formatDate(cal.getTime(), "yyyy-MM-ddHH:mm:ss");
                String testName = GlobalValues.student.getId() + millisec + "test.jpg";

                fis = new FileInputStream(file);
                ftpclient.enterLocalPassiveMode();
                result = ftpclient.storeFile(testName, fis);
                // Log.e("result ","file "+result);
                int reply = ftpclient.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    System.err.println("FTP server refused connection.");
                }
                if (result == true) {
                    status = "Y";
                    System.out.println("File is uploaded successfully " + testName);
                } else {
                    isuploaded = false;
                    System.out.println("File uploading failed");
                }
                ftpclient.disconnect();
                onResponsed1(1, 1, "ok");


            } catch (FTPConnectionClosedException e) {

                e.printStackTrace();
            } catch (Exception e) {

                e.printStackTrace();
            } finally {
                try {
                    if (ftpclient != null)
                        ftpclient.disconnect();
                } catch (FTPConnectionClosedException e) {

                    System.out.println(e);
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
            String urls = "http://www.densoftinfotech.com/EducationalProgram/Delegates/" + GlobalValues.student.getId() + millisec + "test.jpg";

            try {


                //sendrequest(urls);
                Intent intent = new Intent(ActivityStartChat.this, ActivitySelectNAddText.class);
                intent.putExtra("URL", urls);
                startActivityForResult(intent, Addtext);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return status;
        }


        public void genloading1(String msg) {
            try {
                if (!((ActivityStartChat.this).isFinishing())) {


                    dialog = ProgressDialog.show(ActivityStartChat.this, msg, getResources().getString(R.string.dialog_please_wait));
                    dialog.setCancelable(false);
                } else {

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        public void dismissLoading() {
            if (dialog != null) {
                dialog.dismiss();
            }
        }

        protected void onResponsed1(int statuscode, int accesscode, String data) {
            try {
                if (dialog != null) {
                    dialog.dismiss();
                }
                dismissLoading();
           /* if (statuscode == Constants.STATUS_OK) {
                if (accesscode == 1610) {

                } else if (accesscode == 1016) {

                } else if (accesscode == 1994) {

                }
            }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    private void sendrequest(String fileurl, String text) {

        try {


            JSONObject jsonObject = new JSONObject();
            JSONObject jsonObject1 = new JSONObject();
            // ArrayList<MessagesModel> model_1 = new ArrayList<>();

            ChatQuestionsNAnswers questionsNAnswers = new ChatQuestionsNAnswers();
            Calendar cal = Calendar.getInstance();
            String d = DateUtils.formatDate(cal.getTime(), "yyyy-MM-dd HH:mm");

            //Log.e("log "," "+Id);



                    /* <TokenId>1</TokenId>
                          <ReplyAnswer>ABCDEFGHIJKL</ReplyAnswer>
                          <ReplyBy>1</ReplyBy>
                          <ReplyType>Enquiry</ReplyType>
                          <ReplyDate>2019-06-05</ReplyDate>
                          <FileUrl>/Abc/Por/Xyz</FileUrl>*/


            try {
                JSONObject jsonObject2 = new JSONObject();
                JSONObject jsonObject3 = new JSONObject();
                JSONObject jsonObject4 = new JSONObject();
                JSONObject jsonObject5 = new JSONObject();
                jsonObject.put("TokenId", token);
                // Log.e("Categoryid "," id "+CategoryId);
                jsonObject.put("ReplyAnswer", text);

                jsonObject.put("ReplyBy", GlobalValues.student.getId());
                jsonObject.put("ReplyDate", d);
                //  jsonObject.put("StudentId", 1/*GlobalValues.student.getId()*/);

                jsonObject.put("FileUrl", fileurl);

                jsonObject2.put("subroot", jsonObject);
                jsonObject3.put("root", jsonObject2);
                jsonObject4.put("xml", jsonObject3.toString());

           /* if (flag==false){
                jsonObject4.put("Flag", "Update");
                jsonObject.put("Status","Pending");
            }
            else if (flag==true){*/
                jsonObject4.put("Flag", "Save");
                /* }*/

                jsonObject1.put("FilterParameter", jsonObject4.toString());
                // jsonObject1.put("FilterParameter",jsonObject5);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            MessagesModel messagesModel = new MessagesModel();
            messagesModel.setMessage(editTextmsg.getText().toString());
            messagesModel.setSenderId(GlobalValues.student.getId());

            messagesModel.setFileUrl(fileurl);
            messagesModel.setTime(d);

            // models.add(messagesModel);
            questionsNAnswers.setMessagesModels(models);
            /*}*//* catch (JSONException e) {
            e.printStackTrace();
        }*//*
             */

            ConnectionManager.getInstance(ActivityStartChat.this).getChat1(jsonObject1.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

