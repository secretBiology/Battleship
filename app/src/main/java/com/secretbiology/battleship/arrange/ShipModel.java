package com.secretbiology.battleship.arrange;

import com.secretbiology.battleship.GameConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dexter for Battleship .
 * Code is released under MIT license
 */

public class ShipModel {

    private int id;
    private int size;
    private boolean isVertical;

    public ShipModel(int id, int size) {
        this.id = id;
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isVertical() {
        return isVertical;
    }

    public void setVertical(boolean vertical) {
        isVertical = vertical;
    }

    public void toggleDirection() {
        isVertical = !isVertical;
    }

    public List<Integer> getLocations(int index) {
        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (isVertical) {
                integers.add(index + i * GameConstants.BOARD_COLUMNS);
            } else {
                integers.add(index + i);
            }
        }
        return integers;
    }
}
