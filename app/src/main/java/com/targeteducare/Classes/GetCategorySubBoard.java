package com.targeteducare.Classes;

import java.io.Serializable;

public class GetCategorySubBoard implements Serializable {
    String id_subboard;
    String name_subboard;
    String deleted_subboard;
    String abbr_subboard;
    String description_subboard;
    String subjectid_subboard;
    String unitid_subboard;
    String chapterid_subboard;
    String parentid_subboard;

    public GetCategorySubBoard(String id_subboard, String name_subboard, String deleted_subboard, String abbr_subboard,
                               String description_subboard, String subjectid_subboard, String unitid_subboard, String chapterid_subboard,
                               String parentid_subboard){
        this.id_subboard = id_subboard;
        this.name_subboard = name_subboard;
        this.deleted_subboard = deleted_subboard;
        this.abbr_subboard = abbr_subboard;
        this.description_subboard = description_subboard;
        this.subjectid_subboard = subjectid_subboard;
        this.unitid_subboard = unitid_subboard;
        this.chapterid_subboard = chapterid_subboard;
        this.parentid_subboard = parentid_subboard;
    }

    public String getId_subboard() {
        return id_subboard;
    }

    public void setId_subboard(String id_subboard) {
        this.id_subboard = id_subboard;
    }

    public String getName_subboard() {
        return name_subboard;
    }

    public void setName_subboard(String name_subboard) {
        this.name_subboard = name_subboard;
    }

    public String getDeleted_subboard() {
        return deleted_subboard;
    }

    public void setDeleted_subboard(String deleted_subboard) {
        this.deleted_subboard = deleted_subboard;
    }

    public String getAbbr_subboard() {
        return abbr_subboard;
    }

    public void setAbbr_subboard(String abbr_subboard) {
        this.abbr_subboard = abbr_subboard;
    }

    public String getDescription_subboard() {
        return description_subboard;
    }

    public void setDescription_subboard(String description_subboard) {
        this.description_subboard = description_subboard;
    }

    public String getSubjectid_subboard() {
        return subjectid_subboard;
    }

    public void setSubjectid_subboard(String subjectid_subboard) {
        this.subjectid_subboard = subjectid_subboard;
    }

    public String getUnitid_subboard() {
        return unitid_subboard;
    }

    public void setUnitid_subboard(String unitid_subboard) {
        this.unitid_subboard = unitid_subboard;
    }

    public String getChapterid_subboard() {
        return chapterid_subboard;
    }

    public void setChapterid_subboard(String chapterid_subboard) {
        this.chapterid_subboard = chapterid_subboard;
    }

    public String getParentid_subboard() {
        return parentid_subboard;
    }

    public void setParentid_subboard(String parentid_subboard) {
        this.parentid_subboard = parentid_subboard;
    }
}
