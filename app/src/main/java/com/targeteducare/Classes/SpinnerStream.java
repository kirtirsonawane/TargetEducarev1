package com.targeteducare.Classes;

import java.io.Serializable;

public class SpinnerStream implements Serializable {
    String board_name;

    public SpinnerStream(String name_board) {
        this.board_name = name_board;
    }

    public String getBoard_name() {
        return board_name;
    }

    public void setBoard_name(String board_name) {
        this.board_name = board_name;
    }

    @Override
    public String toString() {
        return this.board_name;
    }
}
