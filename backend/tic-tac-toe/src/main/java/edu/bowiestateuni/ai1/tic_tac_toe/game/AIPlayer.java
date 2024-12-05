package edu.bowiestateuni.ai1.tic_tac_toe.game;

import edu.bowiestateuni.ai1.tic_tac_toe.game.dto.DifficultyTypeConstant;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
@Slf4j
public class AIPlayer {
    private char aiMarker; // AI's marker ('O')
    private char humanMarker; // Human's marker ('X')
    private DifficultyTypeConstant difficultyLevel = DifficultyTypeConstant.EASY;

    public AIPlayer(char aiMarker, char humanMarker) {
        this.aiMarker = aiMarker;
        this.humanMarker = humanMarker;
    }

    public AIPlayer(char aiMarker, char humanMarker, DifficultyTypeConstant difficultyLevel) {
        this.aiMarker = aiMarker;
        this.humanMarker = humanMarker;
        this.difficultyLevel = difficultyLevel;
    }

    public int[] findBestMove(Board board) {
        log.info("Finding best move for AI player: {} with difficulty level: {}", aiMarker, difficultyLevel);
        if (difficultyLevel == DifficultyTypeConstant.EASY) {
            return findRandomMove(board);
        } else {
            return findBestMoveAlphaBetaPruning(board);
        }
    }

    //Find random move
    private int[] findRandomMove(Board board) {
        List<int[]> availableMoves = board.getAvailableMoves();
        int randomIndex = (int) (Math.random() * availableMoves.size());
        return availableMoves.get(randomIndex);
    }

    public int[] findBestMoveAlphaBetaPruning(Board board) {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = {-1, -1};

        // Loop through all possible moves
        for (int[] move : board.getAvailableMoves()) {
            // Make the move on the board
            board.makeMove(move[0], move[1], aiMarker);

            // Evaluate the move using minimax
            int score = minimax(board, 0, false, Integer.MIN_VALUE, Integer.MAX_VALUE);

            // Undo the move
            board.undoMove(move[0], move[1]);

            // Update the best move if the current move's score is better
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove;
    }

    private int minimax(Board board, int depth, boolean isMaximizing, int alpha, int beta) {
        // Check for terminal states (win, lose, draw)
        char winner = board.checkWinner();
        if (winner == aiMarker) return 10 - depth; // AI wins
        if (winner == humanMarker) return depth - 10; // Human wins
        if (board.isFull()) return 0; // Draw

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;

            // Loop through all possible moves
            for (int[] move : board.getAvailableMoves()) {
                board.makeMove(move[0], move[1], aiMarker);
                int score = minimax(board, depth + 1, false, alpha, beta);
                board.undoMove(move[0], move[1]);
                bestScore = Math.max(bestScore, score);
                alpha = Math.max(alpha, bestScore);
                if (beta <= alpha) break; // Alpha-beta pruning
            }

            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;

            // Loop through all possible moves
            for (int[] move : board.getAvailableMoves()) {
                board.makeMove(move[0], move[1], humanMarker);
                int score = minimax(board, depth + 1, true, alpha, beta);
                board.undoMove(move[0], move[1]);
                bestScore = Math.min(bestScore, score);
                beta = Math.min(beta, bestScore);
                if (beta <= alpha) break; // Alpha-beta pruning
            }

            return bestScore;
        }
    }
}
