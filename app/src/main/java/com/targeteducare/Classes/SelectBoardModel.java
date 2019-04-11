package com.targeteducare.Classes;

import java.io.Serializable;
import java.util.ArrayList;

public class SelectBoardModel implements Serializable {

    String board;
    ArrayList<SubBoardDataModel> subBoards = new ArrayList<>();

    public ArrayList<SubBoardDataModel> getSubBoards() {
        return subBoards;
    }

    public void setSubBoards(ArrayList<SubBoardDataModel> subBoards) {
        this.subBoards = subBoards;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public SelectBoardModel(String board){
        this.board = board;
    }
}
