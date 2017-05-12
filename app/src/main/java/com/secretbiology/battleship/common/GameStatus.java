package com.secretbiology.battleship.common;

import com.secretbiology.battleship.arrange.BoardItem;

import java.util.List;

/**
 * Created by Dexter for Battleship .
 * Code is released under MIT license
 */

public class GameStatus {
    private String gameID;
    private String player1Grid;
    private String player2Grid;
    private String player1Ship;
    private String player2Ship;
    private boolean player1Ready;
    private boolean player2Ready;
    private int currentTurn;

    public GameStatus() {
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public String getPlayer1Grid() {
        return player1Grid;
    }

    public void setPlayer1Grid(String player1Grid) {
        this.player1Grid = player1Grid;
    }

    public String getPlayer2Grid() {
        return player2Grid;
    }

    public void setPlayer2Grid(String player2Grid) {
        this.player2Grid = player2Grid;
    }

    public boolean isPlayer1Ready() {
        return player1Ready;
    }

    public void setPlayer1Ready(boolean player1Ready) {
        this.player1Ready = player1Ready;
    }

    public boolean isPlayer2Ready() {
        return player2Ready;
    }

    public void setPlayer2Ready(boolean player2Ready) {
        this.player2Ready = player2Ready;
    }

    public String getPlayer1Ship() {
        return player1Ship;
    }

    public void setPlayer1Ship(String player1Ship) {
        this.player1Ship = player1Ship;
    }

    public String getPlayer2Ship() {
        return player2Ship;
    }

    public void setPlayer2Ship(String player2Ship) {
        this.player2Ship = player2Ship;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    public void updatePlayer1Grid(List<BoardItem> itemList) {
        player1Grid = Helper.convertCellType(itemList);
        player1Ship = Helper.convertShipType(itemList);
    }

    public void updatePlayer2Grid(List<BoardItem> itemList) {
        player2Grid = Helper.convertCellType(itemList);
        player2Ship = Helper.convertShipType(itemList);
    }

}
