package com.secretbiology.battleship.common;

import android.util.SparseArray;

import com.secretbiology.battleship.GameConstants;
import com.secretbiology.battleship.arrange.BoardItem;
import com.secretbiology.battleship.arrange.ShipModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dexter for Battleship .
 * Code is released under MIT license
 */

public class Helper implements GameConstants {

    public static String convertCellType(List<BoardItem> boardItems) {
        String s = "";
        for (BoardItem item : boardItems) {
            s += String.valueOf(item.getType()) + ",";
        }
        return s.substring(0, s.length() - 1);
    }

    public static String convertShipType(List<BoardItem> boardItems) {
        String s = "";
        for (BoardItem item : boardItems) {
            s += String.valueOf(item.getShipID()) + ",";
        }
        return s.substring(0, s.length() - 1);
    }

    public static String playerGrid(int playerNo) {
        if (playerNo == 0) {
            return NETWORK_PLAYER_1_GRID;
        } else {
            return NETWORK_PLAYER_2_GRID;
        }
    }

    public static String playerReady(int playerNo) {
        if (playerNo == 0) {
            return NETWORK_PLAYER_1_READY;
        } else {
            return NETWORK_PLAYER_2_READY;
        }
    }

    public static String playerShip(int playerNo) {
        if (playerNo == 0) {
            return NETWORK_PLAYER_1_SHIP;
        } else {
            return NETWORK_PLAYER_2_SHIP;
        }
    }

    public static List<BoardItem> convertToGrid(String type, String shipID) {
        List<BoardItem> itemList = new ArrayList<>();
        String[] convertedTypes = type.split(",");
        String[] convertedShips = shipID.split(",");
        for (int i = 0; i < convertedShips.length; i++) {
            itemList.add(new BoardItem(Integer.valueOf(convertedTypes[i]), Integer.valueOf(convertedShips[i])));
        }
        return itemList;
    }

    public static SparseArray<ShipModel> getShips() {
        SparseArray<ShipModel> shipMap = new SparseArray<>();
        shipMap.put(0, new ShipModel(0, 5));
        shipMap.put(1, new ShipModel(1, 4));
        shipMap.put(2, new ShipModel(2, 3));
        shipMap.put(3, new ShipModel(3, 3));
        shipMap.put(4, new ShipModel(4, 2));
        return shipMap;
    }

    public static int countScore(List<BoardItem> itemList) {
        int i = 0;
        for (BoardItem item : itemList) {
            if (item.getType() == GameConstants.CELL_DAMAGED_SHIP) {
                i++;
            }
        }
        return i;
    }

    public static int getMaxScore() {
        int p = 0;
        SparseArray<ShipModel> map = getShips();
        for (int i = 0; i < map.size(); i++) {
            p = p + map.valueAt(i).getSize();
        }
        return p;
    }
}
