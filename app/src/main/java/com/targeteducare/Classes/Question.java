package com.targeteducare.Classes;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Question implements Serializable, Parcelable {
    private int displayid = 0;
    int srno = 0;
    int Id = 0;
    private String Name = "";
    String QuestionTypeId = "";
    int subjectid = 0;
    String unitid = "";
    String marks = "";
    String review = "";
    String selectedanswer = "";
    String isTabletag = "";
    String subjectname = "";
    String hint = "";
    String explanation = "";
    String isTabletagHint = "";
    String isTabletagexplanation = "";
    ArrayList<Options> Options = new ArrayList<>();
    String optiontypeId = "";
    String optiontypeisactive = "";
    String optiontypename = "";
    boolean isanswered = false;
    boolean isreview = false;
    private String NameInMarathi = "";
    long timeperquestion = 0;
    String ExplInMarathi = "";
    boolean issubmit = false;
    String Selected = "";



    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public boolean isIssubmit() {
        return issubmit;
    }

    public void setIssubmit(boolean issubmit) {
        this.issubmit = issubmit;
    }

    public String getExplInMarathi() {
        return ExplInMarathi;
    }

    public void setExplInMarathi(String explInMarathi) {
        ExplInMarathi = explInMarathi;
    }

    public String getNameInMarathi() {
        return NameInMarathi;
    }

    public void setNameInMarathi(String nameInMarathi) {
        NameInMarathi = nameInMarathi;
    }

    public long getTimeperquestion() {
        return timeperquestion;
    }

    public void setTimeperquestion(long timeperquetion) {
        this.timeperquestion = timeperquetion;
    }

    public boolean isIsskipped() {
        return isskipped;
    }

    public void setIsskipped(boolean isskipped) {
        this.isskipped = isskipped;
    }

    boolean isvisited = false;
    JSONObject QuestionType = new JSONObject();
    boolean IswrongAnswer = false;

    boolean isskipped = false;

    public JSONObject getQuestionType() {
        return QuestionType;
    }

    public void setQuestionType(JSONObject questionType) {
        QuestionType = questionType;
    }

    public boolean isIswrongAnswer() {
        return IswrongAnswer;
    }

    public void setIswrongAnswer(boolean iswrongAnswer) {
        IswrongAnswer = iswrongAnswer;
    }

    protected Question(Parcel in) {
        displayid = in.readInt();
        srno = in.readInt();
        Id = in.readInt();
        Name = in.readString();
        NameInMarathi = in.readString();
        QuestionTypeId = in.readString();
        subjectid = in.readInt();
        unitid = in.readString();
        marks = in.readString();
        review = in.readString();
        selectedanswer = in.readString();
        isTabletag = in.readString();
        subjectname = in.readString();
        hint = in.readString();
        explanation = in.readString();
        isTabletagHint = in.readString();
        isTabletagexplanation = in.readString();
        optiontypeId = in.readString();
        optiontypeisactive = in.readString();
        optiontypename = in.readString();
        isanswered = in.readByte() != 0;
        isreview = in.readByte() != 0;
        isvisited = in.readByte() != 0;
        isskipped = in.readByte() != 0;
        issubmit = in.readByte() != 0;
        timeperquestion = in.readInt();
    }

   /* public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };*/

    public boolean isIsvisited() {
        return isvisited;
    }

    public void setIsvisited(boolean isvisited) {
        this.isvisited = isvisited;
    }

    public boolean isIsreview() {
        return isreview;
    }

    public void setIsreview(boolean isreview) {
        this.isreview = isreview;
    }

    public boolean isIsanswered() {
        return isanswered;
    }

    public void setIsanswered(boolean isanswered) {
        this.isanswered = isanswered;
    }

    public Question() {

    }

    public Question(JSONObject obj) {
        try {

            if (obj.has("srno")) {
                this.srno = obj.getInt("srno");
            }

            if (obj.has("Id")) {
                this.Id = obj.getInt("Id");
            }
            if (obj.has("Name")) {
                this.Name = obj.getString("Name");
            }
            if (obj.has("subjectid")) {
                this.subjectid = obj.getInt("subjectid");
            }
            if (obj.has("unitid")) {
                this.unitid = obj.getString("unitid");
            }
            if (obj.has("QuestionTypeId")) {
                this.QuestionTypeId = obj.getString("QuestionTypeId");
            }
            if (obj.has("marks")) {
                this.marks = obj.getString("marks");
            }
            if (obj.has("review")) {
                this.review = obj.getString("review");
            }
            if (obj.has("selectedanswer")) {
                this.selectedanswer = obj.getString("selectedanswer");
            }
            if (obj.has("ExplInMarathi")) {
                this.ExplInMarathi = obj.getString("ExplInMarathi");
            }
            if (obj.has("isTabletag")) {
                this.isTabletag = obj.getString("isTabletag");
            }
            if (obj.has("subjectname")) {
                this.subjectname = obj.getString("subjectname");
            }
            if (obj.has("hint")) {
                this.hint = obj.getString("hint");
            }
            if (obj.has("isskipped")) {
                this.isskipped = obj.getBoolean("isskipped");
            }
            if (obj.has("explanation")) {
                this.explanation = obj.getString("explanation");
            }
            if (obj.has("isTabletagHint")) {
                this.isTabletagHint = obj.getString("isTabletagHint");
            }
            if (obj.has("isTabletagexplanation")) {
                this.isTabletagexplanation = obj.getString("isTabletagexplanation");
            }

            if (obj.has("NameInMarathi")) {
                this.NameInMarathi = obj.getString("NameInMarathi");
            }

            if (obj.has("isTabletagHint")) {
                this.isTabletagHint = obj.getString("isTabletagHint");
            }

            if (obj.has("isTabletagHint")) {
                this.isTabletagHint = obj.getString("isTabletagHint");
            }

            if (obj.has("issubmit")) {
                this.issubmit = obj.getBoolean("issubmit");
            } else {
            }
            if (obj.has("QuestionType")) {
                QuestionType = obj.getJSONObject("QuestionType");
                JSONObject obj1 = obj.getJSONObject("QuestionType").getJSONObject("QuestionType");
                if (obj1.has("Id")) {
                    this.optiontypeId = obj1.getString("Id");
                }

                if (obj1.has("Name")) {
                    this.optiontypename = obj1.getString("Name");
                }

                if (obj1.has("IsActive")) {
                    this.optiontypeisactive = obj1.getString("IsActive");
                }
            }

            if (obj.has("Options")) {
                JSONArray array = obj.getJSONObject("Options").optJSONArray("Options");
                //  Log.e("optarray", "optarray" + array.toString());
                if (array != null) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<Options>>() {
                    }.getType();
                    ArrayList<Options> optdata1 = gson.fromJson(array.toString(), type);
                    if (optdata1 != null) {
                        this.Options.addAll(optdata1);
                    }
                } else {
                    JSONObject obj1 = obj.getJSONObject("Options").optJSONObject("Options");
                    //Log.e("optobj", "optobj" + obj1.toString());
                    if (obj1 != null) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<Options>() {
                        }.getType();
                        Options optdata = gson.fromJson(obj1.toString(), type);
                        this.Options.add(optdata);
                    }
                }
            }

            if (obj.has("isanswered")) {
                this.isanswered = obj.getBoolean("isanswered");
            }

            if (obj.has("Selected")) {
                this.Selected = obj.getString("Selected");
            }

            if(this.Selected!=null)
            {
                if(this.Selected.length()>0)
                {
                    for (int i=0;i<getOptions().size();i++)
                    {
                        if(getOptions().get(i).getOptNumber().trim().equalsIgnoreCase(Selected))
                        {
                            getOptions().get(i).setSelected(true);
                           // Log.e("data ","data true "+Selected+" "+getOptions().get(i).getOptNumber()+" "+getOptions().get(i).getOptionalp()+" "+srno);
                        }else {
                           // Log.e("data ","data false "+Selected+" "+getOptions().get(i).getOptNumber()+" "+getOptions().get(i).getOptionalp()+" "+srno);
                        }
                    }
                }
            }
            // Log.e("parsed properly","p");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public JSONObject getOpttype() {
        return QuestionType;
    }

    public void setOpttype(JSONObject opttype) {
        this.QuestionType = opttype;
    }

    public JSONObject getJsonObject(Question q) {
        JSONObject obj = new JSONObject();
        try {

            Gson gson = new Gson();
            obj = new JSONObject(gson.toJson(q));
            Type type = new TypeToken<ArrayList<Options>>() {
            }.getType();
            String optdata = gson.toJson(q.getOptions());
            JSONObject opt = new JSONObject();

            JSONArray array1 = new JSONArray();
            array1.put(new JSONArray(optdata).getJSONObject(0));
            opt.put("Options", new JSONArray(optdata));
            obj.put("Options", opt);
            obj.put("QuestionType", getOpttype());
            // obj.put("QuestionType", );

            //Log.e("optdata ","optdata "+obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public ArrayList<Options> getOptions() {
      /*  for (int i = 0; i < this.options.size(); i++) {
            Log.e("optname ", "optname " + this.options.get(i).getName());
        }*/
        return Options;
    }

    public void setOptions(ArrayList<Options> options) {
        this.Options = options;
    }

    public String getOptiontypeId() {
        return optiontypeId;
    }

    public void setOptiontypeId(String optiontypeId) {
        this.optiontypeId = optiontypeId;
    }

    public String getOptiontypeisactive() {
        return optiontypeisactive;
    }

    public void setOptiontypeisactive(String optiontypeisactive) {
        this.optiontypeisactive = optiontypeisactive;
    }

    public String getOptiontypename() {
        return optiontypename;
    }

    public void setOptiontypename(String optiontypename) {
        this.optiontypename = optiontypename;
    }

    public int getDisplayid() {
        return displayid;
    }

    public void setDisplayid(int displayid) {
        this.displayid = displayid;
    }

    public int getSrno() {
        return srno;
    }

    public void setSrno(int srno) {
        this.srno = srno;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getQuestionTypeId() {
        return QuestionTypeId;
    }

    public void setQuestionTypeId(String questionTypeId) {
        QuestionTypeId = questionTypeId;
    }

    public int getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(int subjectid) {
        this.subjectid = subjectid;
    }

    public String getUnitid() {
        return unitid;
    }

    public void setUnitid(String unitid) {
        this.unitid = unitid;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getSelectedanswer() {
        return selectedanswer;
    }

    public void setSelectedanswer(String selectedanswer) {
        this.selectedanswer = selectedanswer;
    }

    public String getIsTabletag() {
        return isTabletag;
    }

    public void setIsTabletag(String isTabletag) {
        this.isTabletag = isTabletag;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getIsTabletagHint() {
        return isTabletagHint;
    }

    public void setIsTabletagHint(String isTabletagHint) {
        this.isTabletagHint = isTabletagHint;
    }

    public String getIsTabletagexplanation() {
        return isTabletagexplanation;
    }

    public void setIsTabletagexplanation(String isTabletagexplanation) {
        this.isTabletagexplanation = isTabletagexplanation;
    }


    public String getSelected() {
        return Selected;
    }

    public void setSelected(String selected) {
        Selected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(displayid);
        parcel.writeInt(srno);
        parcel.writeInt(Id);
        parcel.writeString(Name);
        parcel.writeString(QuestionTypeId);
        parcel.writeInt(subjectid);
        parcel.writeString(unitid);
        parcel.writeString(marks);
        parcel.writeString(review);
        parcel.writeString(selectedanswer);
        parcel.writeString(isTabletag);
        parcel.writeString(subjectname);
        parcel.writeString(hint);
        parcel.writeString(explanation);
        parcel.writeString(isTabletagHint);
        parcel.writeString(isTabletagexplanation);
        parcel.writeString(optiontypeId);
        parcel.writeString(optiontypeisactive);
        parcel.writeString(optiontypename);
        parcel.writeByte((byte) (isanswered ? 1 : 0));
        parcel.writeByte((byte) (isreview ? 1 : 0));
        parcel.writeByte((byte) (isvisited ? 1 : 0));
        parcel.writeByte((byte) (issubmit ? 1 : 0));
        parcel.writeByte((byte) (isskipped ? 1 : 0));
        parcel.writeLong(timeperquestion);
    }
}