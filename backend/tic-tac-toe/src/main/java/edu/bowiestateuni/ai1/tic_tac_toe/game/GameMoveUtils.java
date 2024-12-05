package edu.bowiestateuni.ai1.tic_tac_toe.game;

import java.util.Map;

public class GameMoveUtils {
    public static final Map<Integer, String> MOVE_MAP = Map.of(
            0, "00", 1, "01", 2, "02",
            3, "10", 4, "11", 5, "12",
            6, "20", 7, "21", 8, "22"
    );
    public static final Map<String, Integer> MOVE_MAP_REVERSE = Map.of(
            "00", 0, "01", 1, "02", 2,
            "10", 3, "11", 4, "12", 5,
            "20", 6, "21", 7, "22", 8
    );
}
