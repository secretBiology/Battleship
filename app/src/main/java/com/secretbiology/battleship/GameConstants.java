package com.secretbiology.battleship;

/**
 * Created by Dexter for Battleship .
 * Code is released under MIT license
 */

public interface GameConstants {

    String BASE_DATABASE_NODE = "games/battleship";
    String GAME_LOBBY = "games/battleship/lobby";

    int BOARD_COLUMNS = 10;
    int BOARD_ROWS = 10;

    int CELL_EMPTY = 0;
    int CELL_SHIP = 1;
    int CELL_DAMAGED_SHIP = 2;
    int CELL_MISSED = 3;
}
