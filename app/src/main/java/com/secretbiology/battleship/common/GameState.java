package com.secretbiology.battleship.common;

import com.secretbiology.helpers.general.Log;

/**
 * Created by Dexter for Battleship .
 * Code is released under MIT license
 */

public class GameState {

    private static GameState instance;
    private GameDetails gameDetails;
    private int currentPlayer;

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
}
