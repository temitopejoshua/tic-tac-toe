package edu.bowiestateuni.ai1.tic_tac_toe.game.dto;

import edu.bowiestateuni.ai1.tic_tac_toe.game.Board;
import edu.bowiestateuni.ai1.tic_tac_toe.game.GameMoveUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class PlayGameResponse {
    private Board board;
    private String message;
    private String winner;
    private boolean isGameOver;
    private CustomBoard customBoard;
    private Integer aiMove;
    public CustomBoard getCustomBoard() {
        return new CustomBoard(board);
    }

    @Data
    public static class CustomBoard {
        private int[][] customBoard;
        private int[] validMoves;

        public CustomBoard(Board board) {
            customBoard = new int[board.getBoard().length][board.getBoard()[0].length];
            validMoves = new int[board.getAvailableMoves().size()];
            for (int i = 0; i < board.getBoard().length; i++) {
                for (int j = 0; j < board.getBoard()[i].length; j++) {
                    if(board.getBoard()[i][j] == 'X'){
                        customBoard[i][j] = 1;
                    } else if (board.getBoard()[i][j] == 'O') {
                        customBoard[i][j] = 2;
                    }
                }
            }
            for (int i = 0; i < board.getAvailableMoves().size(); i++) {
                int[] move = board.getAvailableMoves().get(i);
                validMoves[i] = GameMoveUtils.MOVE_MAP_REVERSE.get(String.format("%d%d", move[0], move[1]));
            }

        }

    }
}
