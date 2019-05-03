package com.targeteducare.Classes;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.jar.Attributes;

public class MyPackages {
    int TotalRecord;
    int Id;
    String Name;
    int Noofstudent;
    double Amount;
    String NameAmount;
    int MockTest;
    int EBook;
    int PBook;
    int VideoOnDemand;
    int LiveStreaming;
    double RenewAmount;
    int ValidFor;
    String ValidForType;
    int Deleted;
    int CategoryId;
    int SubCategoryId;
    String Description;
    String ExamassignedNames;
    String ExamassignedIds;
    String CategoryName;
    boolean isSelected;
    String Imageurl;

    public MyPackages(int totalRecord, int id, String name, int noofstudent, double amount, String nameAmount, int mockTest, int EBook, int PBook, int videoOnDemand, int liveStreaming, double renewAmount, int validFor, String validForType, int deleted, int categoryId, int subCategoryId, String description, String examassignedNames, String examassignedIds, String categoryName, String imageUrl) {
        this.TotalRecord = totalRecord;
        this.Id = id;
        this.Name = name;
        this.Noofstudent = noofstudent;
        this.Amount = amount;
        this.NameAmount = nameAmount;
        this.MockTest = mockTest;
        this.EBook = EBook;
        this.PBook = PBook;
        this.VideoOnDemand = videoOnDemand;
        this.LiveStreaming = liveStreaming;
        this.RenewAmount = renewAmount;
        this.ValidFor = validFor;
        this.ValidForType = validForType;
        this.Deleted = deleted;
        this.CategoryId = categoryId;
        this.SubCategoryId = subCategoryId;
        this.Description = description;
        this.ExamassignedNames = examassignedNames;
        this.ExamassignedIds = examassignedIds;
        this.CategoryName = categoryName;
        this.Imageurl = imageUrl;
    }

    public MyPackages() {
    }

    public MyPackages(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public static ArrayList<MyPackages> getjson(JSONObject jsonObject3) {
        ArrayList<MyPackages> list = new ArrayList<MyPackages>();

        try {

            JSONObject jsonObject4 = new JSONObject();


            JSONArray jsonArray = null;

            jsonArray = jsonObject3.getJSONArray("subroot");

            int i = 0;

            Log.e("json array len ", String.valueOf(jsonArray.length()));
            for (i = 0; i < jsonArray.length(); i++) {

                MyPackages myPackages = new MyPackages();
                Log.e("Array data  ::: ", jsonArray.toString());
                jsonObject4 = jsonArray.getJSONObject(i);
                Log.e("Msg  : ", jsonObject4.toString());

                myPackages.setId(Integer.parseInt(jsonObject4.getString("Id")));
                myPackages.setName(jsonObject4.getString("Name"));
                myPackages.setTotalRecord(Integer.parseInt(jsonObject4.getString("TotalRecord")));
                myPackages.setAmount(Double.parseDouble((jsonObject4.getString("Amount"))));
                myPackages.setNameAmount(jsonObject4.getString("NameAmount"));
                myPackages.setCategoryId(Integer.parseInt((jsonObject4.getString("CategoryId"))));
                myPackages.setCategoryName((jsonObject4.getString("CategoryName")));
                myPackages.setDeleted(Integer.parseInt(jsonObject4.getString("Deleted")));
                myPackages.setDescription((jsonObject4.getString("Description")));
                myPackages.setEBook(Integer.parseInt(jsonObject4.getString("EBook")));

                Log.e("ids;;;;;; ", String.valueOf(myPackages.getId()));
                //myPackages.setExamassignedIds(jsonObject4.getString("ExamassignedIds"));
                //  myPackages.setExamassignedNames((jsonObject4.getString("ExamassignedNames")));
                myPackages.setLiveStreaming(Integer.parseInt((jsonObject4.getString("LiveStreaming"))));
                myPackages.setMockTest(Integer.parseInt((jsonObject4.getString("MockTest"))));
                myPackages.setPBook(Integer.parseInt((jsonObject4.getString("PBook"))));
                myPackages.setValidFor(Integer.parseInt(jsonObject4.getString("ValidFor")));
                myPackages.setValidForType(jsonObject4.getString("ValidForType"));
                myPackages.setVideoOnDemand(Integer.parseInt(jsonObject4.getString("VideoOnDemand")));
                myPackages.setSubCategoryId(Integer.parseInt(jsonObject4.getString("SubCategoryId")));
                myPackages.setRenewAmount(Double.parseDouble((jsonObject4.getString("RenewAmount"))));
                myPackages.setImageurl(jsonObject4.getString("Imageurl"));

                Log.e("list 1 ::: ", myPackages.getName());

                Log.e("list 1 ::: ", list.toString());

                list.add(myPackages);
                Log.e("list  ::: ", String.valueOf(list.size()));
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return list;
    }

    public int getTotalRecord() {
        return TotalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        TotalRecord = totalRecord;
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

    public int getNoofstudent() {
        return Noofstudent;
    }

    public void setNoofstudent(int noofstudent) {
        Noofstudent = noofstudent;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getNameAmount() {
        return NameAmount;
    }

    public void setNameAmount(String nameAmount) {
        NameAmount = nameAmount;
    }

    public int getMockTest() {
        return MockTest;
    }

    public void setMockTest(int mockTest) {
        MockTest = mockTest;
    }

    public int getEBook() {
        return EBook;
    }

    public void setEBook(int EBook) {
        this.EBook = EBook;
    }

    public int getPBook() {
        return PBook;
    }

    public void setPBook(int PBook) {
        this.PBook = PBook;
    }

    public int getVideoOnDemand() {
        return VideoOnDemand;
    }

    public void setVideoOnDemand(int videoOnDemand) {
        VideoOnDemand = videoOnDemand;
    }

    public int getLiveStreaming() {
        return LiveStreaming;
    }

    public void setLiveStreaming(int liveStreaming) {
        LiveStreaming = liveStreaming;
    }

    public double getRenewAmount() {
        return RenewAmount;
    }

    public void setRenewAmount(double renewAmount) {
        RenewAmount = renewAmount;
    }

    public int getValidFor() {
        return ValidFor;
    }

    public void setValidFor(int validFor) {
        ValidFor = validFor;
    }

    public String getValidForType() {
        return ValidForType;
    }

    public void setValidForType(String validForType) {
        ValidForType = validForType;
    }

    public int getDeleted() {
        return Deleted;
    }

    public void setDeleted(int deleted) {
        Deleted = deleted;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public int getSubCategoryId() {
        return SubCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        SubCategoryId = subCategoryId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getExamassignedNames() {
        return ExamassignedNames;
    }

    public void setExamassignedNames(String examassignedNames) {
        ExamassignedNames = examassignedNames;
    }

    public String getExamassignedIds() {
        return ExamassignedIds;
    }

    public void setExamassignedIds(String examassignedIds) {
        ExamassignedIds = examassignedIds;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getImageurl() {
        return Imageurl;
    }

    public void setImageurl(String imageurl) {
        Imageurl = imageurl;
    }

    @Override
    public String toString() {
        ArrayList list = new ArrayList();

        list.add(Name);
        list.add(Description);
        list.add(Amount);
        return String.valueOf(list);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
