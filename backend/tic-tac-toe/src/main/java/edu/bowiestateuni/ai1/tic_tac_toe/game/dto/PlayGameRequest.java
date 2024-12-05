package edu.bowiestateuni.ai1.tic_tac_toe.game.dto;

import lombok.Data;

@Data
public class PlayGameRequest {
    private DifficultyTypeConstant difficulty;
    private Integer move;
}
