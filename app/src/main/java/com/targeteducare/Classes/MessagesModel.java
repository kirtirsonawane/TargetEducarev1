package com.targeteducare.Classes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class MessagesModel implements Serializable {
    String ID;
    String Message;
    String SenderId;
    String Time;
    String FileUrl;
    String TokenId;
    int Count;

         /*   "TokenDetailId": "1",
                    "TokenId": "1",
                    "ReplyAnswer": "Test",
                    "ReplyBy": "1",
                    "ReplyType": "Student",
                    "ReplyDate": "2019-05-24",
                    "FileUrl": "http://192.168.1.59:8097/ExamRoute/Api_SaveSupportToken"*/


    public MessagesModel(String TokenDetailId, String ReplyAnswer, String ReplyBy, String ReplyDate, String FileUrl, String TokenId, int Count) {
        this.ID = TokenDetailId;
        this.Message = ReplyAnswer;
        this.SenderId = ReplyBy;
        this.Time = ReplyDate;
        this.FileUrl = FileUrl;
        this.TokenId = TokenId;
        this.Count = Count;
    }

    public MessagesModel() {
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTokenId() {
        return TokenId;
    }

    public void setTokenId(String tokenId) {
        TokenId = tokenId;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String senderId) {
        this.SenderId = senderId;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        this.Time = time;
    }

    public String getFileUrl() {
        return FileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.FileUrl = fileUrl;

    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        this.Count = count;
    }
/* public static Collection<? extends MessagesModel> getjson(JSONObject jsonObject3) {
        ArrayList<MessagesModel> list = new ArrayList<>();
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

                JSONObject data = object1.optJSONObject("Answer");
               // ArrayList<MessagesModel> models = new ArrayList<>();
                if (data != null) {
                    MessagesModel model = new MessagesModel();
                    model.setID(data.getString("TokenDetailId"));
                    model.setTokenId(data.getString("TokenId"));
                    model.setSenderId(data.getString("ReplyBy"));
                    model.setTime(data.getString("ReplyDate"));
                    model.setMessage(data.getString("ReplyAnswer"));
                    model.setFileUrl(data.getString("FileUrl"));
                    list.add(model);
                   // questionsNAnswers.setMessagesModels(list);
                } else if (data == null) {
                    JSONArray jsonArray1 = data.getJSONArray("Answer");
                    for (int j = 0; j < jsonArray1.length(); j++) {
                        MessagesModel model = new MessagesModel();
                        JSONObject object3 = jsonArray1.getJSONObject(j);
                        model.setID(data.getString("TokenDetailId"));
                        model.setTokenId(data.getString("TokenId"));
                        model.setSenderId(data.getString("ReplyBy"));
                        model.setTime(data.getString("ReplyDate"));
                        model.setMessage(data.getString("ReplyAnswer"));
                        model.setFileUrl(data.getString("FileUrl"));
                        list.add(model);

                    }
                    questionsNAnswers.setMessagesModels(list);
                }


            } else if (object1 == null) {
                JSONArray jsonArray = new JSONArray();
                jsonArray = jsonObject3.getJSONArray("subroot");

                for (int i = 0; i < jsonArray.length(); i++) {

                    Log.e("string ", "for 2 " + jsonArray.toString());
                    ChatQuestionsNAnswers questionsNAnswers = new ChatQuestionsNAnswers();

                    JSONObject object = jsonArray.getJSONObject(i);
//                    questionsNAnswers.setId(object.getString("StudentId"));
                    questionsNAnswers.setToken(object.getString("TokenId"));
                    questionsNAnswers.setDate(object.getString("SubmissionDate"));
                    questionsNAnswers.setStatus(object.getString("Status"));
                    questionsNAnswers.setTitle(object.getString("Title"));
                    questionsNAnswers.setCategoryId(object.getString("CategoryId"));

                    //  questionsNAnswers.setCategoryId();
                    Log.e("Id ", " id in obj " + questionsNAnswers.getToken());

                    JSONObject data = object.optJSONObject("Answer");
                    ArrayList<MessagesModel> models = new ArrayList<>();
                    if (data != null) {
                        JSONObject jsonObject = object.getJSONObject("Answer");
                        MessagesModel model = new MessagesModel();
                        model.setID(jsonObject.getString("TokenDetailId"));
                        model.setTokenId(jsonObject.getString("TokenId"));
                        model.setSenderId(jsonObject.getString("ReplyBy"));
                        model.setTime(jsonObject.getString("ReplyDate"));
                        model.setMessage(jsonObject.getString("ReplyAnswer"));
                        model.setFileUrl(jsonObject.getString("FileUrl"));
                        models.add(model);
                        questionsNAnswers.setMessagesModels(models);
                    } else if (data == null) {
                        Log.e("json array ",": "+object.toString());
                        if (object.has("Answer")) {
                            JSONArray jsonArray1 = object.getJSONArray("Answer");
                            for (int j = 0; j < jsonArray1.length(); j++) {
                                MessagesModel model = new MessagesModel();
                                JSONObject object3 = jsonArray1.getJSONObject(j);
                                model.setID(object3.getString("TokenDetailId"));
                                model.setTokenId(object3.getString("TokenId"));
                                model.setSenderId(object3.getString("ReplyBy"));
                                model.setTime(object3.getString("ReplyDate"));
                                model.setMessage(object3.getString("ReplyAnswer"));
                                model.setFileUrl(object3.getString("FileUrl"));
                                models.add(model);

                            }


                        } else {
                            Log.e("in else ===","kkkk ");
                            MessagesModel model = new MessagesModel();
                            model.setID("");
                            model.setTokenId("");
                            model.setSenderId("");
                            model.setTime("");
                            model.setMessage("");
                            model.setFileUrl("");
                            models.add(model);
                            models.add(model);
                        }
                        questionsNAnswers.setMessagesModels(models);
                    }

                    // questionsNAnswers.setMessagesModels(models);
                    list.add(questionsNAnswers);
                    //  Log.e("msg ", " msginlist " + list.get(i).getMessagesModels().get(0).getMessage());

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return list}*/


    public static Collection<? extends MessagesModel> getjson(JSONObject jsonObject3, String token) {
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
                String tokenid = object1.getString("TokenId");

                if (tokenid.equalsIgnoreCase(token)) {
                    questionsNAnswers.setToken(object1.getString("TokenId"));

                    questionsNAnswers.setDate(object1.getString("SubmissionDate"));
                    questionsNAnswers.setStatus(object1.getString("Status"));
                    questionsNAnswers.setTitle(object1.getString("Title"));

                    JSONObject data = object1.optJSONObject("Answer");

                    if (data != null) {

                        MessagesModel model = new MessagesModel();
                        model.setID(object1.getString("TokenDetailId"));
                        model.setTokenId(object1.getString("TokenId"));
                        model.setSenderId(object1.getString("ReplyBy"));
                        model.setTime(object1.getString("Replydate"));
                        model.setMessage(object1.getString("ReplyAnswer"));
                        //  model.setFileUrl(data.getString("FileUrl"));
                   /* String s=object1.optString("FileUrl");
                    if (s==null){
                       // questionsNAnswers.setFileUrl(null);
                        Log.e("fileurl","if1jjj"+object1.getString("FileUrl"));
                    }
                    else{
                       // questionsNAnswers.setFileUrl(object1.getString("FileUrl"));
                        Log.e("fileurl","else1jjj1"+object1.getString("FileUrl"));
                    }*/

                        models.add(model);



                        questionsNAnswers.setMessagesModels(models);
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
                            //model.setFileUrl(data.getString("FileUrl"));

                       /* String s=object1.optString("FileUrl");
                        if (s==null){
                        //    model.setFileUrl(null);
                            Log.e("fileurl","if2jjj"+object1.getString("FileUrl"));
                        }
                        else{
                      //      model.setFileUrl(object1.getString("FileUrl"));
                            Log.e("fileurl","else2jjj1"+object1.getString("FileUrl"));
                        }
                        model.setCount(jsonArray1.length());*/
                            models.add(model);
                            //   for (int i=0;i<models.size();i++){

                            // }


                        }
                        questionsNAnswers.setMessagesModels(models);
                    }


                } else {

                }

            } else if (object1 == null) {


                JSONArray jsonArray = new JSONArray();
                jsonArray = jsonObject3.getJSONArray("subroot");

                for (int i = 0; i < jsonArray.length(); i++) {


                    ChatQuestionsNAnswers questionsNAnswers = new ChatQuestionsNAnswers();
                    JSONObject object = jsonArray.getJSONObject(i);

                    String tokenid = object.getString("TokenId");

                    if (tokenid.equalsIgnoreCase(token)) {
                 questionsNAnswers.setToken(object.getString("TokenId"));

                        questionsNAnswers.setDate(object.getString("SubmissionDate"));
                        questionsNAnswers.setStatus(object.getString("Status"));
                        questionsNAnswers.setTitle(object.getString("Title"));
                        questionsNAnswers.setCategoryId(object.getString("CategoryId"));

                        //  questionsNAnswers.setCategoryId();


                        JSONObject data = object.optJSONObject("Answer");
                        /* ArrayList<MessagesModel> models = new ArrayList<>();*/
                        if (data != null) {
                            JSONObject jsonObject = object.getJSONObject("Answer");
                            MessagesModel model = new MessagesModel();
                            model.setID(jsonObject.getString("TokenDetailId"));
                            model.setTokenId(jsonObject.getString("TokenId"));
                            model.setSenderId(jsonObject.getString("ReplyBy"));
                            model.setTime(jsonObject.getString("Replydate"));
                            model.setMessage(jsonObject.getString("ReplyAnswer"));
                            //  model.setFileUrl(jsonObject.getString("FileUrl"));


                        String s=jsonObject.optString("FileUrl");
                        if (s==null){
                            model.setFileUrl(null);

                        }
                        else{
                            model.setFileUrl(jsonObject.getString("FileUrl"));

                        }


                            models.add(model);

                            questionsNAnswers.setMessagesModels(models);
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
                                    }else {

                                    }

                                    if (object3.has("FileUrl")){
                                        String s = object3.optString("FileUrl");

                                        if (s.equalsIgnoreCase("")) {
                                            model.setFileUrl(null);
                                            //     Log.e("fileurl","if2jjj"+object1.getString("FileUrl"));
                                        } else {
                                            model.setFileUrl(object3.getString("FileUrl"));

                                        }
                                    }
                                    else {

                                    }








/*

                                String s=object3.optString("FileUrl");


                                if (s==null){
                                    model.setFileUrl(null);
                                    Log.e("fileurl","if3jjj1"+object3.getString("FileUrl"));
                                }
                                else{
                                    model.setFileUrl(object.getString("FileUrl"));
                                    Log.e("fileurl","else3 jjj1"+object3.getString("FileUrl"));
                                    Log.e("fileurl","else3jjj1"+model.getFileUrl());
                                }
*/


                                    //  model.setFileUrl(object3.getString("FileUrl"));
                                    model.setCount(jsonArray1.length());

                                    models.add(model);

                                }
                                questionsNAnswers.setMessagesModels(models);


                            } else {


                            }

                        }


                    } else {

                    }
                    list.add(questionsNAnswers);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return models;

    }


    @Override
    public String toString() {


        ArrayList list = new ArrayList();

        //list.add(Id) ;
        list.add(Message);
        //list.add(question);
        //list.add (answers);
        list.add(TokenId);
        list.add(Time);
        //list.add (SenderId);
        //list.add (StudentId);
        list.add(SenderId);
        // list.add (FileUrl);


        //list.add(FileUrl);


        return String.valueOf(list);


    }
}
