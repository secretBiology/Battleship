package com.secretbiology.battleship.debug;

import android.util.SparseArray;

import com.secretbiology.battleship.GameConstants;
import com.secretbiology.battleship.arrange.BoardItem;
import com.secretbiology.battleship.arrange.ShipModel;
import com.secretbiology.battleship.common.GameDetails;
import com.secretbiology.battleship.common.GameState;
import com.secretbiology.battleship.common.GameStatus;
import com.secretbiology.battleship.common.Helper;
import com.secretbiology.battleship.common.Player;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Dexter for Battleship .
 * Code is released under MIT license
 */

public class TestGame implements GameConstants {

    public void initializeTestGame() {

        Player p1 = new Player();
        p1.setName("Your Name");
        p1.setEmail("email@com");
        p1.setNo(0);
        p1.setGrid(createRandomBoard());

        Player p2 = new Player();
        p2.setName("Computer");
        p2.setEmail("secret@com");
        p2.setNo(1);
        p2.setGrid(createRandomBoard());

        GameStatus status = new GameStatus();
        status.setGameID("TestGame");
        status.setCurrentTurn(0);
        status.setPlayer1Grid(Helper.convertCellType(p1.getGrid()));
        status.setPlayer2Grid(Helper.convertCellType(p2.getGrid()));
        status.setPlayer1Ship(Helper.convertShipType(p1.getGrid()));
        status.setPlayer2Ship(Helper.convertShipType(p2.getGrid()));

        GameDetails details = new GameDetails(status.getGameID());
        details.setPlayer1(p1);
        details.setPlayer2(p2);
        details.setCreationTimestamp(General.timeStamp());
        details.setRunning(true);


        GameState.initialize(details, 0);
        GameState.getInstance().setGameStatus(status);

    }


    private List<BoardItem> createRandomBoard() {
        List<BoardItem> itemList = new ArrayList<>();
        for (int i = 0; i < BOARD_COLUMNS * BOARD_ROWS; i++) {
            itemList.add(new BoardItem(CELL_EMPTY));
        }

        SparseArray<ShipModel> map = Helper.getShips();
        for (int j = 0; j < map.size(); j++) {
            putRandomShip(itemList, map.valueAt(j).getSize(), map.valueAt(j).getId());
        }

        return itemList;

    }


    private static void putRandomShip(List<BoardItem> itemList, int shipSize, int shipID) {
        Random random = new Random();
        boolean isVertical = random.nextBoolean();
        int loc = General.randInt(0, BOARD_COLUMNS * BOARD_ROWS);
        ShipModel model = new ShipModel(shipID, shipSize);
        model.setVertical(isVertical);

        for (int i = 0; i < 100; i++) {
            if (isValidPosition(model, loc, itemList)) {
                for (Integer p : model.getLocations(loc)) {
                    itemList.get(p).setType(CELL_SHIP);
                    itemList.get(p).setShipID(model.getId());
                }
                return;
            } else {
                loc = General.randInt(0, BOARD_COLUMNS * BOARD_ROWS);
            }
        }

        Log.error("Unable to place ship. Skipping entry...");
    }


    private static boolean isValidPosition(ShipModel selectedShip, int pos, List<BoardItem> itemList) {
        for (Integer i : selectedShip.getLocations(pos)) {
            if (i >= BOARD_COLUMNS * BOARD_ROWS) {
                return false;
            } else if (itemList.get(i).getShipID() != -1) {
                return false;
            } else if (i % BOARD_ROWS < pos % BOARD_ROWS && !selectedShip.isVertical()) {
                return false;
            }
        }
        return true;
    }

}
