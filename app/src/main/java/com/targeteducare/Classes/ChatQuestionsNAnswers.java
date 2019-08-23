package com.targeteducare.Classes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class ChatQuestionsNAnswers implements Serializable {


    /*"TokenId": "1",*/
         /*   "Title": "Test",
            "CategoryId": "1",
            "SubmissionDate": "2019-05-24",
            "StudentId": "1",
            "Status": "Confirm",
            "CreatedDate": "2019-06-05",
            "FileUrl": "http://192.168.1.59:8097/ExamRoute/Api_SaveSupportToken",
            "Answer": [*/



    String Id;
    String CategoryId;
    String question;
    String answers;
    String Token;
    String date;
    String SenderId;
    String StudentId;
    String Title;
    String FileUrl;
    String Status;
    String UpdatedDate;
    ArrayList<MessagesModel> messagesModels;
    int Count;


    public ChatQuestionsNAnswers(String Id, String question, String answers, String TokenId,String CategoryId, String date,String SenderId,ArrayList<MessagesModel> messagesModels,String StudentId,String Title,String FileUrl,String Status,String UpdatedDate,int Count) {
        this.Id = Id;
        this.question = question;
        this.answers = answers;
        this.Token = TokenId;
        this.date = date;
        this.CategoryId=CategoryId;
        this.SenderId=SenderId;
        this.messagesModels=messagesModels;
        this.StudentId=StudentId;
        this.FileUrl=FileUrl;
        this.Title=Title;
        this.Status=Status;
        this.UpdatedDate=UpdatedDate;
        this.Count=Count;
    }

    public ChatQuestionsNAnswers() {
    }




    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public String getToken() {
        return Token;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public void setToken(String time) {
        this.Token = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String senderId) {
        SenderId = senderId;
    }

    public ArrayList<MessagesModel> getMessagesModels() {
        return messagesModels;
    }

    public void setMessagesModels(ArrayList<MessagesModel> messagesModels) {
        this.messagesModels = messagesModels;
    }

    public String getStudentId() {
        return StudentId;
    }

    public void setStudentId(String studentId) {
        StudentId = studentId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getFileUrl() {
        return FileUrl;
    }

    public void setFileUrl(String fileUrl) {
        FileUrl = fileUrl;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public static Collection<? extends ChatQuestionsNAnswers> getjson(JSONObject jsonObject3) {
        ArrayList<MessagesModel> models = new ArrayList<>();
        ArrayList<ChatQuestionsNAnswers> list = new ArrayList<>();
        try {


            //  jsonArray = (mydb.getdata());


            JSONObject object1 = jsonObject3.optJSONObject("subroot");

            if (object1 != null) {
                object1.getJSONObject("subroot");
                ChatQuestionsNAnswers questionsNAnswers = new ChatQuestionsNAnswers();

                //JSONObject object = jsonArray.getJSONObject(i);
              //  questionsNAnswers.setId(object1.getString("id"));
                questionsNAnswers.setToken(object1.getString("TokenId"));
                questionsNAnswers.setDate(object1.getString("SubmissionDate"));
                questionsNAnswers.setStatus(object1.getString("Status"));
                questionsNAnswers.setTitle(object1.getString("Title"));
                questionsNAnswers.setStudentId(object1.getString("StudentId"));
                questionsNAnswers.setUpdatedDate(object1.getString("UpdatedDate"));
                questionsNAnswers.setFileUrl(object1.getString("FileUrl"));
                JSONObject data = object1.optJSONObject("Answer");

                if (data != null) {
                    MessagesModel model = new MessagesModel();
                    model.setID(data.getString("TokenDetailId"));
                    model.setTokenId(data.getString("TokenId"));
                    model.setSenderId(data.getString("ReplyBy"));
                    model.setTime(data.getString("Replydate"));
                    model.setMessage(data.getString("ReplyAnswer"));
                    model.setFileUrl(data.getString("FileUrl"));
                    models.add(model);
                  //  questionsNAnswers.setMessagesModels(models);
                } else if (data == null) {
                    JSONArray jsonArray1 = data.getJSONArray("Answer");
                    for (int j = 0; j < jsonArray1.length(); j++) {
                        MessagesModel model = new MessagesModel();
                        JSONObject object3 = jsonArray1.getJSONObject(j);
                        model.setID(data.getString("TokenDetailId"));
                        model.setTokenId(data.getString("TokenId"));
                        model.setSenderId(data.getString("ReplyBy"));
                        model.setTime(data.getString("Replydate"));
                        model.setMessage(data.getString("ReplyAnswer"));
                        model.setFileUrl(data.getString("FileUrl"));
                        model.setCount(jsonArray1.length());
                        models.add(model);

                    }
                 //   questionsNAnswers.setMessagesModels(models);
                }


            } else if (object1 == null) {
                JSONArray jsonArray = new JSONArray();
                jsonArray = jsonObject3.getJSONArray("subroot");

                for (int i = 0; i < jsonArray.length(); i++) {


                    ChatQuestionsNAnswers questionsNAnswers = new ChatQuestionsNAnswers();

                    JSONObject object = jsonArray.getJSONObject(i);
//                    questionsNAnswers.setId(object.getString("StudentId"));
                    if (object.has("TokenId")){
                        questionsNAnswers.setToken(object.getString("TokenId"));
                    }
                    else {
                        questionsNAnswers.setToken((""));
                    }
                    if (object.has("SubmissionDate")){
                        questionsNAnswers.setDate(object.getString("SubmissionDate"));
                    }
                    else {
                        questionsNAnswers.setToken((""));
                    }
                    if (object.has("Status")){
                        questionsNAnswers.setStatus(object.getString("Status"));
                    }
                    else {
                        questionsNAnswers.setToken((""));
                    }
                    if (object.has("Title")){
                        questionsNAnswers.setTitle(object.getString("Title"));
                    }
                    else {
                        questionsNAnswers.setToken((""));
                    }
                    if (object.has("CategoryId")){
                        questionsNAnswers.setCategoryId(object.getString("CategoryId"));
                    }
                    else {
                        questionsNAnswers.setToken((""));
                    }
                    if (object.has("StudentId")){
                        questionsNAnswers.setStudentId(object.getString("StudentId"));
                    }
                    else {
                        questionsNAnswers.setToken((""));
                    }
                    if (object.has("UpdatedDate")){
                        questionsNAnswers.setUpdatedDate(object.getString("UpdatedDate"));
                    }
                    else {
                        questionsNAnswers.setToken((""));
                    }

                   JSONObject jsonObject1= object.optJSONObject("FileUrl");

                    if (object.has("FileUrl")){
                        String s=object.optString("FileUrl");
                        if (s==null){
                            questionsNAnswers.setFileUrl(null);

                        }
                        else{
                            questionsNAnswers.setFileUrl(object.getString("FileUrl"));

                        }
                    }
                    else{

                    }

                  /*  if (jsonObject1==null){
                        questionsNAnswers.setFileUrl("");
                        Log.e("fileurl"," jjj"+object.getString("FileUrl"));
                    }
                    else if (jsonObject1!=null){
                        questionsNAnswers.setFileUrl(object.getString("FileUrl"));
                        Log.e("fileurl"," jjj1"+object.getJSONObject("FileUrl"));

                    }*/


                    JSONObject data = object.optJSONObject("Answer");
                  /*  ArrayList<MessagesModel> models = new ArrayList<>();*/
                    if (data != null) {
                        JSONObject jsonObject = object.getJSONObject("Answer");
                        MessagesModel model = new MessagesModel();
                        model.setID(jsonObject.getString("TokenDetailId"));
                        model.setTokenId(jsonObject.getString("TokenId"));
                        model.setSenderId(jsonObject.getString("ReplyBy"));
                        model.setTime(jsonObject.getString("Replydate"));
                        model.setMessage(jsonObject.getString("ReplyAnswer"));
                        model.setFileUrl(jsonObject.getString("FileUrl"));
                        models.add(model);
                        questionsNAnswers.setCount(1);
                        /*questionsNAnswers.setCount(model.getMessage().length());*/
                    } else if (data == null) {

                        if (object.has("Answer")) {
                            JSONArray jsonArray1 = object.getJSONArray("Answer");
                            for (int j = 0; j < jsonArray1.length(); j++) {
                                MessagesModel model = new MessagesModel();
                                JSONObject object3 = jsonArray1.getJSONObject(j);
                                model.setID(object3.getString("TokenDetailId"));
                                model.setTokenId(object3.getString("TokenId"));
                                model.setSenderId(object3.getString("ReplyBy"));
                                model.setTime(object3.getString("Replydate"));
                                if (object3.has("ReplyAnswer")){
                                    model.setMessage(object3.getString("ReplyAnswer"));
                                }
                                else{

                                }
                                if (object3.has("FileUrl")){
                                    model.setFileUrl(object3.getString("FileUrl"));
                                }
                                else{

                                }


                                models.add(model);

                            }
                            questionsNAnswers.setCount(jsonArray1.length());

                        } else {

                            questionsNAnswers.setCount(0);
                         /*   MessagesModel model = new MessagesModel();
                            model.setID("");
                            model.setTokenId("");
                            model.setSenderId("");
                            model.setTime("");
                            model.setMessage("");
                            model.setFileUrl("");
                            models.add(model);
                            models.add(model);*/
                        }
                      //  questionsNAnswers.setMessagesModels(models);
                    }

                     questionsNAnswers.setMessagesModels(models);

                    list.add(questionsNAnswers);

//                    Log.e("msg ", " msginlist " + list.get(i).getMessagesModels().get(0).getMessage()+" i "+i);

                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }


        return list;
    }

           /* for (int i = 0; i < jsonArray.length(); i++) {

                Log.e("string ", "for 2 " + jsonArray.toString());
                ChatQuestionsNAnswers questionsNAnswers = new ChatQuestionsNAnswers();

                JSONObject object = jsonArray.getJSONObject(i);
                questionsNAnswers.setId(object.getString("id"));
                questionsNAnswers.setToken(object.getString("token"));
                questionsNAnswers.setDate(object.getString("CreatedDate"));
                questionsNAnswers.setStatus(object.getString("Status"));
                questionsNAnswers.setTitle(object.getString("Title"));
              //  questionsNAnswers.setCategoryId();
                Log.e("Id ", " id in obj " + questionsNAnswers.getId());

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
                Log.e("msg ", " msginlist " + list.get(i).getMessagesModels().get(0).getMessage());

            }
         *//*   AdapterChatFaq adapterChatFaq = new AdapterChatFaq(ActivityChatFaq.this, list);
            recyclerView.setLayoutManager(linearLayoutManager);

            recyclerView.setAdapter(adapterChatFaq);*/
 /*          public  void insert(ChatQuestionsNAnswers chat) {

               // for (int i = 0; i < list.size(); i++) {
               ContentValues c = new ContentValues();
               c.put(DatabaseChatHelper.TokenId, chat.getToken());

               Gson gson = new Gson();
               String data = gson.toJson(chat.getMessagesModels());
               //c.put(DatabaseChatHelper.JSONDATA, questionsNAnswers.getMessagesModels());
               c.put(DatabaseChatHelper.JSONDATA, data);
               Log.e("content value check", "hii " + c.toString());
               c.put(DatabaseChatHelper.SubmissionDate, DateUtils.getSqliteTime());
               c.put(DatabaseChatHelper.ID, chat.getId());
               c.put(DatabaseChatHelper.TokenId, chat.getToken());
               c.put(DatabaseChatHelper.CategoryId, chat.getCategoryId());
               c.put(DatabaseChatHelper.CreatedDate, chat.getDate());
               c.put(DatabaseChatHelper.Status, chat.getStatus());
               c.put(DatabaseChatHelper.FileUrl, chat.getFileUrl());
               c.put(DatabaseChatHelper.StudentId, chat.getStudentId());


               DatabaseChatHelper.insertdata(c);
           }
*/


    @Override
    public String toString() {

        ArrayList list = new ArrayList();

      //list.add(Id) ;
        list.add(CategoryId) ;
      //list.add(question);
      //list.add (answers);
        list.add (Token);
        list.add (date);
      //list.add (SenderId);
      //list.add (StudentId);
        list.add (Title);
        list.add (FileUrl);
        list.add (Status);
        list.add(UpdatedDate);
        list.add(Count);
      //list.add(FileUrl);


        return String.valueOf(list);
    }
}
