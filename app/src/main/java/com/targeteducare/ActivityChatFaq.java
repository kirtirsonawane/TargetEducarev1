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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.targeteducare.Adapter.AdapterChatFaq;
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

public class ActivityChatFaq extends Activitycommon {
    private static final int GALLERY = 1;
    private static final int Addtext = 0;
    EditText msg_text;
    ImageView send_img;
    ImageView Attach_img;
    ArrayList<ChatQuestionsNAnswers> list;
    AdapterChatFaq adapterChatFaq;
    int Msg_id = 1;
    ArrayList<ChatQuestionsNAnswers> answ = new ArrayList<>();
    RecyclerView recyclerView;
    static int Id = 0;
    CheckBox Pending_Status;
    CheckBox Confirm_Status;
    CheckBox Cancelled_Status;
    public static String sdata = "";
    DatabaseHelper mydb;
    // ArrayList<String> array = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_chat_faq);
            setmaterialDesign();
            back();
            toolbar.setTitleMargin(25, 10, 10, 10);
            setTitle("PEAK");
            genloading("loading...");

            msg_text = (EditText) findViewById(R.id.edit_text_chat);
            send_img = (ImageView) findViewById(R.id.send_chat);
            Attach_img = (ImageView) findViewById(R.id.attach_chat);
            recyclerView = (RecyclerView) findViewById(R.id.recycle_chat);
            Cancelled_Status = (CheckBox) findViewById(R.id.chat_show_filter_cancelled);
            Pending_Status = (CheckBox) findViewById(R.id.chat_show_filter_pending);
            Confirm_Status = (CheckBox) findViewById(R.id.chat_show_filter_confirm);
            list = new ArrayList<>();

            mydb = new DatabaseHelper((ActivityChatFaq.this));
            Intent intent = getIntent();
            Id = Integer.parseInt(intent.getStringExtra("Id"));
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            linearLayoutManager.setStackFromEnd(true);
            JSONObject jsonObject = new JSONObject();
            JSONObject jsonObject1 = new JSONObject();
            //  loaddata1();
            // {"FilterParameter":"{\"PageNo\":\"1\",\"NoofRecords\":\"1000000\",\"CategoryId\":\"0\",\"SubCategoryId\":\"0\"}"}
            try {
            /*jsonObject.put("", Constants.Packages_page_no);
            jsonObject.put("NoofRecords", Constants.Packages_no_of_records);
            jsonObject.put("CategoryId", GlobalValues.student.getCategoryId());*/
                jsonObject.put("StudentId", GlobalValues.student.getId());
                jsonObject1.put("FilterParameter", jsonObject.toString());
                //  ConnectionManager.getInstance(ActivityChatFaq.this).getChat(jsonObject1.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            ConnectionManager.getInstance(ActivityChatFaq.this).getChat(jsonObject1.toString());
            recyclerView.setLayoutManager(linearLayoutManager);
            //adapterChatFaq.notifyDataSetChanged();
            //adapterChatFaq.notifyDataSetChanged();
       /* Log.e("list ", "display " + list.size());
        for (int i = 0; i < list.size(); i++) {
            Log.e("list ", "display msg " + list.get(i).getId()*//*getMessagesModels().get(i).getMessage()*//*);
        }*/

            try {
                send_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (msg_text.getText().toString() == "" || msg_text.getText().toString() == null || msg_text.getText().toString().length() < 1) {
                            Toast.makeText(context, "please" + " enter msg", Toast.LENGTH_LONG).show();
                        } else {
                            // ArrayList<ChatQuestionsNAnswers> list=new ArrayList<>();
                            sendrequest("", msg_text.getText().toString());
                       /* JSONObject jsonObject = new JSONObject();
                        JSONObject jsonObject1 = new JSONObject();
                        ArrayList<MessagesModel> model_1 = new ArrayList<>();
                        MessagesModel messagesModel = new MessagesModel();
                        ChatQuestionsNAnswers questionsNAnswers = new ChatQuestionsNAnswers();
                        Calendar cal = Calendar.getInstance();
                        String d = DateUtils.formatDate(cal.getTime(), "yyyy-MM-dd");

                        Log.e("log ", " " + Id);
                        try {
                            JSONObject jsonObject2 = new JSONObject();
                            JSONObject jsonObject3 = new JSONObject();
                            JSONObject jsonObject4 = new JSONObject();
                            jsonObject.put("Title", msg_text.getText().toString());
                            jsonObject.put("CategoryId", Id);
                            jsonObject.put("SubmissionDate", d);
                            jsonObject.put("StudentId", GlobalValues.student.getId());
                            jsonObject.put("Status", "pending");
                            jsonObject.put("FileUrl", "");
                            jsonObject.put("UpdatedDate", d);
                            jsonObject2.put("subroot", jsonObject);
                            jsonObject3.put("root", jsonObject2);
                            jsonObject4.put("xml", jsonObject3.toString());
                            jsonObject4.put("Flag", "Save");
                            jsonObject1.put("FilterParameter", jsonObject4.toString());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("json ", "data " + jsonObject1);

                        Log.e("json ", "data " + jsonObject1);

                        messagesModel.setMessage(msg_text.getText().toString());
                        messagesModel.setSenderId(GlobalValues.student.getId());
                        Log.e("message model ", "sender id " + messagesModel.getSenderId());
                        model_1.add(messagesModel);
                        questionsNAnswers.setMessagesModels(model_1);
                        Log.e("question ", " " + questionsNAnswers.getMessagesModels().get(0).getMessage());
                        *//* questionsNAnswers.setToken();*//*
                        questionsNAnswers.setDate(d);


                        questionsNAnswers.setStatus("pending");
                        questionsNAnswers.setTitle(msg_text.getText().toString());


                        //  list.add(questionsNAnswers);

                        *//* adapterChatFaq.notifyDataSetChanged();*//*

                             */
                   /* adapterChatFaq = new AdapterChatFaq(ActivityChatFaq.this, list);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(adapterChatFaq);*/
                            msg_text.setText("");
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Attach_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chooseFromGallary();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                Confirm_Status.setButtonDrawable(null);
                Pending_Status.setButtonDrawable(null);
                Cancelled_Status.setButtonDrawable(null);
                Confirm_Status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked == true) {
                            updatedata(Confirm_Status.getText().toString());
                            Confirm_Status.setBackgroundResource((R.drawable.border_for_token_list_green));
                        } else if (isChecked == false) {
                            Confirm_Status.setBackgroundResource((R.drawable.unchecked_token_list_green));
                            updatedata1(null);
                        }
                    }
                });

                Cancelled_Status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked == true) {

                            updatedata(Cancelled_Status.getText().toString());
                            Cancelled_Status.setBackgroundResource((R.drawable.border_for_token_list));
                        } else if (isChecked == false) {
                            updatedata1(null);
                            Cancelled_Status.setBackgroundResource((R.drawable.unchecked_token_list_red));
                        }
                    }
                });

                Pending_Status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked == true) {

                            updatedata(Pending_Status.getText().toString());
                            Pending_Status.setBackgroundResource(R.drawable.border_for_token_list_yellow);
                        } else if (isChecked == false) {
                            Pending_Status.setBackgroundResource(R.drawable.unchecked_token_list);
                            updatedata1(null);

                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }




    public void chooseFromGallary() {

    try {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);



    }catch (Exception e){
        e.printStackTrace();
    }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
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
                    upload_image_adapter.genloading1("loading...");
                    upload_image_adapter.execute(result);
                   // Toast.makeText(ActivityChatFaq.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    /*profile_pic.setImageBitmap(bitmap);*/


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (requestCode==Addtext){
            String url=data.getStringExtra("url");
            String text=data.getStringExtra("text");
            sendrequest(url,text);
        }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void gotonext(int i, ArrayList<ChatQuestionsNAnswers> answers) {
        try {


            Intent intent = new Intent(ActivityChatFaq.this, ActivityStartChat.class);
            Bundle bundle = new Bundle();
//        String model= String.valueOf(list.get(i).getMessagesModels().get(0).getMessage());
            // String chat= String.valueOf(list);
            // Log.e("model size"," "+model);
            bundle.putSerializable("ARRAY", answers.get(i).getMessagesModels());
            bundle.putSerializable("ARRAY1", answers);
            bundle.putInt("position", i);
            bundle.putInt("categoryid", Id);

            intent.putExtra("bundle", bundle);
            intent.putExtra("Status", answers.get(i).getStatus());
            intent.putExtra("token", answers.get(i).getToken());
            intent.putExtra("title",answers.get(i).getTitle());
//        intent.putExtra("msg", list.get(i).getMessagesModels().get(0).getMessage());
            intent.putExtra("time", answers.get(i).getDate());
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResponsed(int statuscode, int accesscode, String data) {
        super.onResponsed(statuscode, accesscode, data);
        try {

            if (statuscode == Constants.STATUS_OK) {
                if (accesscode == Connection.CHATMESSAGE.ordinal()) {
                    data = GlobalValues.TEMP_STR;

                    if (data != null) {
                        try {
                            JSONObject jsonObject2 = new JSONObject(data);
                            JSONObject jsonObject3 = jsonObject2.getJSONObject("root");
                            list.addAll(ChatQuestionsNAnswers.getjson(jsonObject3));

                        /*int Counter = 0;
                        ++Counter;
                        Log.e("Counter", " " + Counter);
                        Log.e("Array     ", list.size() + " " + list.toString());*/
                            adapterChatFaq = new AdapterChatFaq(ActivityChatFaq.this, list);
                            recyclerView.setAdapter(adapterChatFaq);
                            adapterChatFaq.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }else if(accesscode==Connection.CHATMESSAGEEXCEPTION.ordinal())
                {
                    Toast.makeText(getApplicationContext(),ActivityChatFaq.this.getResources().getString(R.string.Connectiontimeout),Toast.LENGTH_LONG).show();
                }
                else if (accesscode == Connection.SAVECHATMESSAGE.ordinal()) {
                    data = GlobalValues.TEMP_STR;

                    if (data != null) {
                        try {
                            ChatQuestionsNAnswers questionsNAnswers = new ChatQuestionsNAnswers();
                            JSONObject jsonObject2 = new JSONObject(data);
                            JSONObject jsonObject3 = jsonObject2.getJSONObject("root");

                            JSONObject jsonObject4 = jsonObject3.getJSONObject("subroot");
                            if (jsonObject4.has("TokenId")){
                                questionsNAnswers.setToken(jsonObject4.getString("TokenId"));
                            }
                            else {
                                questionsNAnswers.setToken((""));
                            }

                            if (jsonObject4.has("Title")){
                                questionsNAnswers.setTitle(jsonObject4.getString("Title"));
                            }
                            else {
                                questionsNAnswers.setToken((""));
                            }

                            if (jsonObject4.has("Status")){
                                questionsNAnswers.setStatus(jsonObject4.getString("Status"));
                            }
                            else {
                                questionsNAnswers.setToken((""));
                            }

                            questionsNAnswers.setStudentId(GlobalValues.student.getId());
                            questionsNAnswers.setCategoryId(String.valueOf(Id));
                            if (jsonObject4.has("FileUrl")){
                                questionsNAnswers.setFileUrl(jsonObject4.getString("FileUrl"));
                            }
                            else {
                                questionsNAnswers.setToken((""));
                            }

                            /* questionsNAnswers.setUpdatedDate(d);*/
                            Calendar cal = Calendar.getInstance();
                            String d = DateUtils.formatDate(cal.getTime(), "yyyy-MM-dd");
                            questionsNAnswers.setDate(d);
                            questionsNAnswers.setUpdatedDate(d);
                            insert(questionsNAnswers);
                            list.add(questionsNAnswers);

//                    Log.e("Array     ", list.size() + " " + list.toString() + " " + list.get(13).getToken());
                            adapterChatFaq = new AdapterChatFaq(ActivityChatFaq.this, list);
                            recyclerView.setAdapter(adapterChatFaq);
                            adapterChatFaq.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //}

                }else if(accesscode==Connection.SAVECHATMESSAGEEXCEPTION.ordinal())
                {
                    Toast.makeText(getApplicationContext(),ActivityChatFaq.this.getResources().getString(R.string.Connectiontimeout),Toast.LENGTH_LONG).show();
                }


        }
        }catch (Exception e){
            e.printStackTrace();
        }



            /* if (statuscode==Constants.STATUS_OK){*/


        }



    public void insert(ChatQuestionsNAnswers chat) {
        // for (int i = 0; i < list.size(); i++) {
        try {
            ContentValues c = new ContentValues();
            c.put(DatabaseHelper.TokenId, chat.getToken());

            Gson gson = new Gson();
            String data = gson.toJson(chat.getMessagesModels());
            //c.put(DatabaseChatHelper.JSONDATA, questionsNAnswers.getMessagesModels());
            c.put(DatabaseHelper.JSONDATA, "");

            c.put(DatabaseHelper.SubmissionDate, DateUtils.getSqliteTime());
            c.put(DatabaseHelper.ID, chat.getId());
            c.put(DatabaseHelper.TokenId, chat.getToken());
            c.put(DatabaseHelper.CategoryId, chat.getCategoryId());
            c.put(DatabaseHelper.CreatedDate, chat.getDate());
            c.put(DatabaseHelper.Status, chat.getStatus());
            c.put(DatabaseHelper.FileUrl, chat.getFileUrl());
            c.put(DatabaseHelper.StudentId, chat.getStudentId());


            mydb.insertdata(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


   /* public ArrayList<ChatQuestionsNAnswers> loaddata() {

        try {
            JSONArray jsonArray = new JSONArray();
            jsonArray = (mydb.getdata());
            Log.e("length ", " " + jsonArray.length());

            JSONObject jsonObject = new JSONObject();


            ArrayList<MessagesModel> models = new ArrayList<>();


            for (int i = 0; i < jsonArray.length(); i++) {

                ChatQuestionsNAnswers questionsNAnswers = new ChatQuestionsNAnswers();
                MessagesModel model = new MessagesModel();
                JSONArray array = jsonArray.optJSONArray(i);

                if (array != null) {
                    JSONArray array1 = jsonArray.getJSONArray(i);
                    JSONObject object = array1.getJSONObject(i);

                    Log.e("string ", "for 2 " + array.toString());

                    model.setMessage(object.getString("Id"));
                    models.add(model);
                    questionsNAnswers.setMessagesModels(models);
                    questionsNAnswers.setId(object.getString("Id"));
                    questionsNAnswers.setToken(object.getString("TokenId"));
                    questionsNAnswers.setDate(object.getString("Time"));
                    JSONObject object1 = jsonArray.getJSONObject(i);


                } else if (array == null) {

                    JSONObject object = jsonArray.getJSONObject(i);
                    questionsNAnswers.setId(object.getString("id"));
                    questionsNAnswers.setToken(object.getString("token"));
                    questionsNAnswers.setDate(object.getString("SAVEDTIME"));
                    Log.e("Id ", " id in obj " + questionsNAnswers.getId());

                    JSONObject object1 = jsonArray.getJSONObject(i);


                    JSONObject object2 = object1.optJSONObject("Json");
                    if (object2 != null) {
                        JSONArray jsonArray1 = object1.getJSONArray("Json");
                        JSONObject object3 = jsonArray1.getJSONObject(i);
                        model.setMessage(object3.getString("Message"));
                        models.add(model);
                        questionsNAnswers.setMessagesModels(models);
                        Log.e("msg  ", "list " + questionsNAnswers.getMessagesModels().get(i).getMessage().toString());
                    } else if (object2 == null) {

                        String data = object1.getString("JSONDATA");

                        JSONArray jsonArray1 = new JSONArray(data);
                        for (int j = 0; j < jsonArray1.length(); j++) {
                            MessagesModel model1 = new MessagesModel();
                            model = new MessagesModel();
                            JSONObject object3 = jsonArray1.getJSONObject(j);
                            model1.setMessage(object3.getString("Message"));
                            models.add(model1);

                        }
                        questionsNAnswers.setMessagesModels(models);

                    }


                }
                list.add(questionsNAnswers);
                Log.e("msg ", " msginlist " + list.get(i).getMessagesModels().get(i).getMessage());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
*/


    public void loaddata1() {
        try {
            //     ArrayList<ChatQuestionsNAnswers> list=new ArrayList<>();
            JSONArray jsonArray = new JSONArray();
            jsonArray = (mydb.getdata());

            for (int i = 0; i < jsonArray.length(); i++) {


                ChatQuestionsNAnswers questionsNAnswers = new ChatQuestionsNAnswers();

                JSONObject object = jsonArray.getJSONObject(i);
                questionsNAnswers.setId(object.getString(DatabaseHelper.ID));
                questionsNAnswers.setToken(object.getString(DatabaseHelper.TokenId));
                questionsNAnswers.setDate(object.getString(DatabaseHelper.CreatedDate));
                questionsNAnswers.setStatus(object.getString(DatabaseHelper.Status));
                questionsNAnswers.setTitle(object.getString(DatabaseHelper.Title));
                questionsNAnswers.setCategoryId(String.valueOf(Id));


                String data = object.getString("JSONDATA");
                ArrayList<MessagesModel> models = new ArrayList<>();
                JSONArray jsonArray1 = new JSONArray(data);
                for (int j = 0; j < jsonArray1.length(); j++) {
                    MessagesModel model = new MessagesModel();
                    JSONObject object3 = jsonArray1.getJSONObject(j);
                    model.setMessage(object3.getString("Message"));
                    models.add(model);

                }
                questionsNAnswers.setMessagesModels(models);
                list.add(questionsNAnswers);


            }
            /* AdapterChatFaq adapterChatFaq = new AdapterChatFaq(ActivityChatFaq.this, list);*/


            /* recyclerView.setAdapter(adapterChatFaq);*/
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updatedata(String searchkey) {
        try {


            if (Pending_Status.isChecked()) {
                sdata = Pending_Status.getText().toString();
            }

            if (Pending_Status.isChecked() == false) {

                sdata = sdata.replace(Pending_Status.getText().toString(), "");

            }


            if (Confirm_Status.isChecked()) {
                if (sdata.length() == 0) {
                    sdata = Confirm_Status.getText().toString();
                } else {
                    sdata = sdata + " " + Confirm_Status.getText().toString();

                }
            } else if (Confirm_Status.isChecked() == false) {

                sdata = sdata.replace(Confirm_Status.getText().toString(), "");


            }
            if (Cancelled_Status.isChecked()) {
                if (sdata.length() == 0) {
                    sdata = Cancelled_Status.getText().toString();
                } else {
                    sdata = sdata + " " + Cancelled_Status.getText().toString();

                }
            } else if (Cancelled_Status.isChecked() == false) {

                sdata = sdata.replace(Cancelled_Status.getText().toString(), "");


            }


            adapterChatFaq.getFilter().filter(searchkey);
            adapterChatFaq.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatedata1(String searchkey) {
        try {


        /*if (Pending_Status.isChecked()) {
            sdata = Pending_Status.getText().toString();
        }*/

            if (Pending_Status.isChecked() == false) {

                sdata = sdata.replace(Pending_Status.getText().toString(), "");

            }



     /*   if(Confirm_Status.isChecked())
        {
            if(sdata.length()==0)
            {
                sdata = Confirm_Status.getText().toString();
            }else {
                sdata =sdata+" "+ Confirm_Status.getText().toString();

            }
        }*/

            if (Confirm_Status.isChecked() == false) {

                sdata = sdata.replace(Confirm_Status.getText().toString(), "");


            }
        /*if(Cancelled_Status.isChecked())
        {
            if(sdata.length()==0)
            {
                sdata = Cancelled_Status.getText().toString();
            }else {
                sdata =sdata+" "+ Cancelled_Status.getText().toString();

            }
        }*/


            if (Cancelled_Status.isChecked() == false) {

                sdata = sdata.replace(Cancelled_Status.getText().toString(), "");


            }


            adapterChatFaq.getFilter().filter(searchkey);
            adapterChatFaq.notifyDataSetChanged();

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
            long millisec=System.currentTimeMillis();

            try {
                for (int i = 0; i < url.length; i++) {

                    path = url[i];
                }

            }catch (Exception e){
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
                String testName = GlobalValues.student.getId()+millisec+"test.jpg";

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
                onResponsed1(1,1,"ok");
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
            String urls="http://www.densoftinfotech.com/EducationalProgram/Delegates/"+GlobalValues.student.getId()+millisec+"test.jpg";
           /* sendrequest(urls);*/
            try {
                Intent intent =new Intent(ActivityChatFaq.this,ActivitySelectNAddText.class);
                intent.putExtra("URL",urls);
                startActivityForResult(intent,Addtext);
            }catch (Exception e){
                e.printStackTrace();
            }


            return status;
        }
        public void genloading1(String msg) {
            try {
                if (!((ActivityChatFaq.this).isFinishing()) ){
                    dialog = ProgressDialog.show(ActivityChatFaq.this, msg, getResources().getString(R.string.dialog_please_wait));
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


    private void sendrequest(String fileurl,String text) {

       try {
           JSONObject jsonObject = new JSONObject();

           JSONObject jsonObject1 = new JSONObject();
           ArrayList<MessagesModel> model_1 = new ArrayList<>();
           MessagesModel messagesModel = new MessagesModel();
           ChatQuestionsNAnswers questionsNAnswers = new ChatQuestionsNAnswers();
           Calendar cal = Calendar.getInstance();
           String d = DateUtils.formatDate(cal.getTime(), "yyyy-MM-dd");


           try {
               JSONObject jsonObject2 = new JSONObject();
               JSONObject jsonObject3 = new JSONObject();
               JSONObject jsonObject4 = new JSONObject();
               jsonObject.put("Title", text.toString());
               jsonObject.put("CategoryId", Id);
               jsonObject.put("SubmissionDate", d);
               jsonObject.put("StudentId", GlobalValues.student.getId());
               jsonObject.put("Status", "pending");
               jsonObject.put("FileUrl", fileurl);
               jsonObject.put("UpdatedDate", d);
               jsonObject2.put("subroot", jsonObject);
               jsonObject3.put("root", jsonObject2);
               jsonObject4.put("xml", jsonObject3.toString());
               jsonObject4.put("Flag", "Save");
               jsonObject1.put("FilterParameter", jsonObject4.toString());


           } catch (JSONException e) {
               e.printStackTrace();
           }


           ConnectionManager.getInstance(ActivityChatFaq.this).saveChat(jsonObject1.toString());
       }catch (Exception e){
           e.printStackTrace();
       }
    }

    public void enlargeimage(int i, String fileUrl) {
        try {
            String i1= String.valueOf(i);
            Intent intent=new Intent(ActivityChatFaq.this,ActivityEnlargeImage.class);
            // intent.putExtra("i",i);
            intent.putExtra("url",fileUrl);
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
