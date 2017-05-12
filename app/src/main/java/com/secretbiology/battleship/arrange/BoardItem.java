package com.secretbiology.battleship.arrange;

/**
 * Created by Dexter for Battleship .
 * Code is released under MIT license
 */

public class BoardItem {

    private int type;
    private int shipID = -1; //Default

    public BoardItem(int type) {
        this.type = type;
    }

    public BoardItem(int type, int shipID) {
        this.type = type;
        this.shipID = shipID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getShipID() {
        return shipID;
    }

    public void setShipID(int shipID) {
        this.shipID = shipID;
    }
}
