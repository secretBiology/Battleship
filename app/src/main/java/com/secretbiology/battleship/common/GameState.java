package com.secretbiology.battleship.common;

import com.secretbiology.battleship.arrange.BoardItem;
import com.secretbiology.helpers.general.Log;

import java.util.List;

/**
 * Created by Dexter for Battleship .
 * Code is released under MIT license
 */

public class GameState {

    private static GameState instance;
    private GameDetails gameDetails;
    private int currentPlayer;
    private GameStatus gameStatus;

    private GameState(GameDetails gameDetails, int currentPlayer) {
        this.gameDetails = gameDetails;
        this.currentPlayer = currentPlayer;
    }

    public static GameState getInstance() {
        if (instance == null) {
            Log.error("============================");
            Log.error("GameState is not initialized");
            Log.error("============================");
        }
        return instance;
    }

    public static void initialize(GameDetails details, int currentPlayer) {
        instance = new GameState(details, currentPlayer);
    }

    public GameDetails getGameDetails() {
        return gameDetails;
    }

    public Player getPlayer() {
        if (currentPlayer == 0) {
            return gameDetails.getPlayer1();
        } else {
            return gameDetails.getPlayer2();
        }
    }

    public Player getEnemy() {
        if (currentPlayer == 0) {
            return gameDetails.getPlayer2();
        } else {
            return gameDetails.getPlayer1();
        }
    }

    public void updatePlayerStatus(GameDetails details) {
        if (details.getPlayer1() != null) {
            gameDetails.setPlayer1(details.getPlayer1());
        }
        if (details.getPlayer2() != null) {
            gameDetails.setPlayer2(details.getPlayer2());
        } else {
            gameDetails.getPlayer2().setUid("--");
            gameDetails.getPlayer2().setName("--");
            gameDetails.getPlayer2().setEmail("--");
            gameDetails.getPlayer2().setReady(false);
        }
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public List<BoardItem> getEnemyGrid() {
        if (currentPlayer == 0) {
            return Helper.convertToGrid(gameStatus.getPlayer2Grid(), gameStatus.getPlayer2Ship());
        } else {
            return Helper.convertToGrid(gameStatus.getPlayer1Grid(), gameStatus.getPlayer1Ship());
        }
    }

    public List<BoardItem> getPlayerGrid() {
        if (currentPlayer == 0) {
            return Helper.convertToGrid(gameStatus.getPlayer1Grid(), gameStatus.getPlayer1Ship());
        } else {
            return Helper.convertToGrid(gameStatus.getPlayer2Grid(), gameStatus.getPlayer2Ship());
        }
    }
}
