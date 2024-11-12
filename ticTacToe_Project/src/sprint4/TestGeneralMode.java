package sprint4;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;

public class TestGeneralMode {
	/**
     * Test that selecting a computer player disables the S/O selection buttons in general mode.
     */
    @Test
    public void testComputerPlayerSelectionInGeneralMode() {
        GUI gui = new GUI();
        Board board = new Board(gui);
        gui.setVisible(false);

        gui.getGeneralGame().setSelected(true);
        gui.getBoardSizeField().setText("5");

        // Select Computer for Red player
        gui.getRedComputer().setSelected(true);
        gui.updatePlayerControls("Red");

        gui.getBtnNewGame().doClick();

        assertNotNull(board.getRedComputer(), "Red computer player should be initialized in general mode");
        assertNull(board.getBlueComputer(), "Blue computer player should not be initialized in general mode");
        assertFalse(gui.getRedS().isEnabled(), "Red S button should be disabled when red player is Computer");
        assertFalse(gui.getRedO().isEnabled(), "Red O button should be disabled when red player is Computer");
    }

    /**
     * Test that the computer player can make a move in general game mode.
     */
    @Test
    public void testComputerPlayerMakesMoveInGeneralMode() {
        GeneralGameMode gameMode = new GeneralGameMode(5);
        ComputerPlayer computerPlayer = new ComputerPlayer('B', gameMode, false);
        char[][] boardState = new char[5][5];
        for (char[] row : boardState) {
            Arrays.fill(row, ' ');
        }

        ComputerPlayer.Move move = computerPlayer.makeMove(boardState);

        assertNotNull(move, "Computer should make a move");
        assertTrue(move.row >= 0 && move.row < 5, "Move row should be within board");
        assertTrue(move.col >= 0 && move.col < 5, "Move col should be within board");
        assertTrue(move.letter == 'S' || move.letter == 'O', "Move letter should be 'S' or 'O'");
    }

    /**
     * Test a general game where human plays against computer.
     */
    @Test
    public void testComputerVsHumanGameplayInGeneralMode() {
        GeneralGameMode gameMode = new GeneralGameMode(5);
        ComputerPlayer redComputer = new ComputerPlayer('R', gameMode, false);
        char[][] boardState = new char[5][5];
        for (char[] row : boardState) {
            Arrays.fill(row, ' ');
        }

        // Blue (human) makes a move
        boolean sosFormed = gameMode.makeMove(0, 0, 'S');
        assertFalse(sosFormed, "No SOS should be formed yet");
        boardState[0][0] = 'S';
        if (!sosFormed) {
            gameMode.switchPlayer();
        }

        // Red (computer) makes a move
        ComputerPlayer.Move redMove = redComputer.makeMove(boardState);
        assertNotNull(redMove, "Red computer should make a move");
        sosFormed = gameMode.makeMove(redMove.row, redMove.col, redMove.letter);
        boardState[redMove.row][redMove.col] = redMove.letter;
        if (!sosFormed) {
            gameMode.switchPlayer();
        }

        assertFalse(gameMode.isGameOver(), "Game should not be over yet");
    }

    /**
     * Test a general game where two computer players play against each other.
     */
    @Test
    public void testComputerVsComputerGameplayInGeneralMode() {
        int boardSize = 5;
        GeneralGameMode gameMode = new GeneralGameMode(boardSize);
        ComputerPlayer blueComputer = new ComputerPlayer('B', gameMode, false);
        ComputerPlayer redComputer = new ComputerPlayer('R', gameMode, false);
        char[][] boardState = new char[boardSize][boardSize];
        for (char[] row : boardState) {
            Arrays.fill(row, ' ');
        }

        while (!gameMode.isGameOver()) {
            ComputerPlayer currentComputer = gameMode.getCurrentPlayer() == 'B' ? blueComputer : redComputer;
            ComputerPlayer.Move move = currentComputer.makeMove(boardState);
            if (move != null) {
                boolean sosFormed = gameMode.makeMove(move.row, move.col, move.letter);
                boardState[move.row][move.col] = move.letter;
                if (!sosFormed) {
                    gameMode.switchPlayer();
                }
                // In general mode, if SOS is formed, the player gets another turn
                // So we need to handle that
            } else {
                break; // No valid moves left
            }
        }

        assertTrue(gameMode.isGameOver(), "Game should be over");
        int blueScore = gameMode.getBlueScore();
        int redScore = gameMode.getRedScore();
        System.out.println("Blue Score: " + blueScore);
        System.out.println("Red Score: " + redScore);
    }

    /**
     * Test that the general game ends when the board is full.
     */
    @Test
    public void testGeneralGameOverWhenBoardIsFull() {
        // Use valid board size for general mode (4-8)
        int boardSize = 5;  
        GeneralGameMode gameMode = new GeneralGameMode(boardSize);

        // Fill the board with moves
        boolean sosFormed = false;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                // Alternate between S and O randomly to simulate gameplay
                char move = (Math.random() < 0.5) ? 'S' : 'O';
                sosFormed = gameMode.makeMove(i, j, move);
                if (!sosFormed) {
                    gameMode.switchPlayer();
                }
            }
        }

        assertTrue(gameMode.isGameOver(), "Game should be over when board is full");
        int blueScore = gameMode.getBlueScore();
        int redScore = gameMode.getRedScore();
        assertTrue(blueScore >= 0 && redScore >= 0, "Scores should be non-negative");
    }
}