package edu.bowiestateuni.ai1.tic_tac_toe.game;


import edu.bowiestateuni.ai1.tic_tac_toe.game.dto.DifficultyTypeConstant;
import edu.bowiestateuni.ai1.tic_tac_toe.game.dto.PlayGameRequest;
import edu.bowiestateuni.ai1.tic_tac_toe.game.dto.PlayGameResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/game/tic-tac-toe", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class TicTacToeGameController {

    private Board board = new Board();
    private AIPlayer ai = new AIPlayer('O', 'X', DifficultyTypeConstant.EASY);
    private PlayGameResponse lastResponse = null;

    @PutMapping("/reset")
    public ResponseEntity<APIResponse> resetGame( @RequestParam("difficultyLevel")  DifficultyTypeConstant difficultyLevel) {
        log.info("Reset game request with difficulty level {} ", difficultyLevel);
        resetBoardAndAI(difficultyLevel);
        return ResponseEntity.ok(new APIResponse("Game reset successfully with difficulty level " + difficultyLevel));
    }

    @PostMapping(value = "/play", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlayGameResponse> playGame(@RequestBody PlayGameRequest request) {
        log.info("Play game request {} ", request);
        if (isGameOver()) {
            return ResponseEntity.ok(lastResponse);
        }
        PlayGameResponse response = processMove(request.getMove());
        if (response.isGameOver()) {
            lastResponse = response;
        }
        return ResponseEntity.ok(response);
    }

    private void resetBoardAndAI(DifficultyTypeConstant difficultyLevel) {
        board = new Board();
        ai = new AIPlayer('O', 'X', difficultyLevel);
        lastResponse = null;
    }

    private boolean isGameOver() {
        return lastResponse != null && lastResponse.isGameOver();
    }

    private PlayGameResponse processMove(int moveNumber) {
        int row = extractRowFromMove(moveNumber);
        int col = extractColFromMove(moveNumber);

        if (!board.makeMove(row, col, 'X')) {
            return buildInvalidMoveResponse();
        }

        if (board.checkWinner() == 'X') {
            return buildGameOverResponse("You win!", "Human");
        }

        if (board.isFull()) {
            return buildGameOverResponse("It's a draw!", "No winner");
        }

        return processAIMove();
    }

    private int extractRowFromMove(int moveNumber) {
        return Integer.parseInt(GameMoveUtils.MOVE_MAP.get(moveNumber).substring(0, 1));
    }

    private int extractColFromMove(int moveNumber) {
        return Integer.parseInt(GameMoveUtils.MOVE_MAP.get(moveNumber).substring(1, 2));
    }

    private PlayGameResponse processAIMove() {
        int[] bestMove = ai.findBestMove(board);
        board.makeMove(bestMove[0], bestMove[1], 'O');

        if (board.checkWinner() == 'O') {
            return buildGameOverResponse("Computer wins!", "Computer AI", bestMove);
        }

        if (board.isFull()) {
            return buildGameOverResponse("It's a draw!", "No winner");
        }

        return buildValidMoveResponse(bestMove);
    }

    private PlayGameResponse buildGameOverResponse(String message, String winner) {
        return PlayGameResponse.builder()
                .isGameOver(true)
                .message(message)
                .winner(winner)
                .board(board)
                .build();
    }

    private PlayGameResponse buildGameOverResponse(String message, String winner, int[] aiMove) {
        return PlayGameResponse.builder()
                .isGameOver(true)
                .message(message)
                .winner(winner)
                .board(board)
                .aiMove(convertAIMoveToIndex(aiMove))
                .build();
    }

    private PlayGameResponse buildValidMoveResponse(int[] aiMove) {
        return PlayGameResponse.builder()
                .isGameOver(false)
                .message("Valid move")
                .winner("Game still on")
                .board(board)
                .aiMove(convertAIMoveToIndex(aiMove))
                .build();
    }

    private PlayGameResponse buildInvalidMoveResponse() {
        return PlayGameResponse.builder()
                .isGameOver(false)
                .message("Invalid move, try again")
                .winner("Game still on")
                .board(board)
                .build();
    }

    private int convertAIMoveToIndex(int[] aiMove) {
        return GameMoveUtils.MOVE_MAP_REVERSE.get(String.format("%d%d", aiMove[0], aiMove[1]));
    }
    public record APIResponse(String message){}
}
