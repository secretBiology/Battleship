package com.secretbiology.battleship.common;

import com.secretbiology.battleship.arrange.BoardItem;

import java.util.List;

/**
 * Created by Dexter for Battleship .
 * Code is released under MIT license
 */

public class Player {

    private String uid;
    private String name;
    private String email;
    private boolean isCreator;
    private int points;
    private int no;
    private List<BoardItem> grid;
    private boolean isReady;

    public Player() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isCreator() {
        return isCreator;
    }

    public void setCreator(boolean creator) {
        isCreator = creator;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public List<BoardItem> getGrid() {
        return grid;
    }

    public void setGrid(List<BoardItem> grid) {
        this.grid = grid;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }
}
