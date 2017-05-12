package com.secretbiology.battleship.common;

import com.secretbiology.helpers.general.Log;

/**
 * Created by Dexter for Battleship .
 * Code is released under MIT license
 */

public class GameState {

    private static GameState instance;
    private GameDetails gameDetails;

    private GameState(GameDetails gameDetails) {
        this.gameDetails = gameDetails;
    }

    public static GameState getInstance() {
        if (instance == null) {
            Log.error("============================");
            Log.error("GameState is not initialized");
            Log.error("============================");
        }
        return instance;
    }

    public static void initialize(GameDetails details) {
        instance = new GameState(details);
    }

    public GameDetails getGameDetails() {
        return gameDetails;
    }
}
