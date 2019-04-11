package com.targeteducare.Classes;

import java.util.ArrayList;

public class GetCategoryBoard {

    String totalrecord_board;
    String id_board;
    String name_board;
    String abbr_board;
    String description_board;
    String parentid_board;
    String deleted_board;
    String subjectid_board;
    String unitid_board;
    String chapterid_board;

    ArrayList<GetCategorySubBoard> getCategorySubBoard=new ArrayList<>();

    public GetCategoryBoard(String totalrecord_board, String id_board, String name_board, String abbr_board, String description_board,
                            String parentid_board, String deleted_board, String subjectid_board, String unitid_board, String chapterid_board){
        this.totalrecord_board = totalrecord_board;
        this.id_board = id_board;
        this.name_board = name_board;
        this.abbr_board = abbr_board;
        this.description_board = description_board;
        this.parentid_board = parentid_board;
        this.deleted_board = deleted_board;
        this.subjectid_board = subjectid_board;
        this.unitid_board = unitid_board;
        this.chapterid_board = chapterid_board;
    }


    public ArrayList<GetCategorySubBoard> getGetCategorySubBoard() {
        return getCategorySubBoard;
    }

    public void setGetCategorySubBoard(ArrayList<GetCategorySubBoard> getCategorySubBoard) {
        this.getCategorySubBoard = getCategorySubBoard;
    }

    public String getTotalrecord_board() {
        return totalrecord_board;
    }

    public void setTotalrecord_board(String totalrecord_board) {
        this.totalrecord_board = totalrecord_board;
    }

    public String getId_board() {
        return id_board;
    }

    public void setId_board(String id_board) {
        this.id_board = id_board;
    }

    public String getName_board() {
        return name_board;
    }

    public void setName_board(String name_board) {
        this.name_board = name_board;
    }

    public String getAbbr_board() {
        return abbr_board;
    }

    public void setAbbr_board(String abbr_board) {
        this.abbr_board = abbr_board;
    }

    public String getDescription_board() {
        return description_board;
    }

    public void setDescription_board(String description_board) {
        this.description_board = description_board;
    }

    public String getParentid_board() {
        return parentid_board;
    }

    public void setParentid_board(String parentid_board) {
        this.parentid_board = parentid_board;
    }

    public String getDeleted_board() {
        return deleted_board;
    }

    public void setDeleted_board(String deleted_board) {
        this.deleted_board = deleted_board;
    }

    public String getSubjectid_board() {
        return subjectid_board;
    }

    public void setSubjectid_board(String subjectid_board) {
        this.subjectid_board = subjectid_board;
    }

    public String getUnitid_board() {
        return unitid_board;
    }

    public void setUnitid_board(String unitid_board) {
        this.unitid_board = unitid_board;
    }

    public String getChapterid_board() {
        return chapterid_board;
    }

    public void setChapterid_board(String chapterid_board) {
        this.chapterid_board = chapterid_board;
    }
}
