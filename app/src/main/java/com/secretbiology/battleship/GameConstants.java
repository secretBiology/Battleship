package com.secretbiology.battleship;

/**
 * Created by Dexter for Battleship .
 * Code is released under MIT license
 */

public interface GameConstants {

    String BASE_DATABASE_NODE = "games/battleship";
    String GAME_LOBBY = "games/battleship/lobby";
    String GAME_ARRANGE = "games/battleship/arrange";
    String GAME_RUN = "games/battleship/running";

    int BOARD_COLUMNS = 10;
    int BOARD_ROWS = 10;

    int CELL_EMPTY = 0;
    int CELL_SHIP = 1;
    int CELL_DAMAGED_SHIP = 2;
    int CELL_MISSED = 3;

    String NETWORK_PLAYER_1 = "player1";
    String NETWORK_PLAYER_2 = "player2";
    String NETWORK_PLAYER_1_READY = "player1Ready";
    String NETWORK_PLAYER_2_READY = "player2Ready";
    String NETWORK_PLAYER_1_GRID = "player1Grid";
    String NETWORK_PLAYER_2_GRID = "player2Grid";
    String NETWORK_PLAYER_1_SHIP = "player1Ship";
    String NETWORK_PLAYER_2_SHIP = "player2Ship";
    String NETWORK_GAME_ID = "gameID";
    String NETWORK_GAME_TURN = "currentTurn";
}
