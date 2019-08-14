package com.targeteducare.Classes;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;

public class Options implements Serializable , Parcelable {
    int Id = 0;
    int QuestionId = 0;
    String Name = "";
    boolean IsAnswer = false;
    boolean Selected = false;
    int isTabletag = 0;
    String optionalp = "";
    int Color_Backround;
    String NameInMarathi = "";
    String ExplInMarathi = "";
    String  OptNumber="";

    public static final Creator<Options> CREATOR = new Creator<Options>() {
        @Override
        public Options createFromParcel(Parcel in) {
            return new Options(in);
        }

        @Override
        public Options[] newArray(int size) {
            return new Options[size];
        }
    };

    public String getOptNumber() {
        return OptNumber;
    }

    public void setOptNumber(String optNumber) {
        OptNumber = optNumber;
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

    public String getOptionalp() {
        return optionalp;
    }

    public void setOptionalp(String optionalp) {
        this.optionalp = optionalp;
    }

    public Options() {

    }

 /*   public Options(JSONObject obj) {
        try {
            if (obj.has("Id")) {
                this.Id = obj.getInt("Id");
            }

            if (obj.has("QuestionId")) {
                this.QuestionId = obj.getInt("QuestionId");
            }

            if (obj.has("Name")) {
                this.Name = obj.getString("Name");
            }

            if (obj.has("IsAnswer")) {
                this.IsAnswer = obj.getBoolean("IsAnswer");
            }

            if (obj.has("Selected")) {
                this.Selected = obj.getBoolean("Selected");
            }

            if (obj.has("isTabletag")) {
                this.isTabletag = obj.getInt("isTabletag");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    protected Options(Parcel in) {
        Id = in.readInt();
        QuestionId = in.readInt();
        Name = in.readString();
        OptNumber = in.readString();
        IsAnswer = in.readByte() != 0;
        Selected = in.readByte() != 0;
        isTabletag = in.readInt();
        Color_Backround = in.readInt();
    }


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getQuestionId() {
        return QuestionId;
    }

    public void setQuestionId(int questionId) {
        QuestionId = questionId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean isAnswer() {
        return IsAnswer;
    }

    public void setAnswer(boolean answer) {
        IsAnswer = answer;
    }

    public boolean isSelected() {
        return Selected;
    }

    public void setSelected(boolean selected) {
        Selected = selected;
    }

    public int getIsTabletag() {
        return isTabletag;
    }

    public void setIsTabletag(int isTabletag) {
        this.isTabletag = isTabletag;
    }

    public int getColor_Backround() {
        return Color_Backround;
    }

    public void setColor_Backround(int color_Backround) {
        Color_Backround = color_Backround;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(Id);
        parcel.writeInt(QuestionId);
        parcel.writeString(Name);
        parcel.writeByte((byte) (IsAnswer ? 1 : 0));
        parcel.writeByte((byte) (Selected ? 1 : 0));
        parcel.writeInt(isTabletag);
        parcel.writeInt(Color_Backround);
        parcel.writeString(OptNumber);
    }
}
