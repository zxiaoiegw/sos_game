package sprint4;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;


public class TestSimpleMode {
	/**
     * Test that selecting a computer player disables the S/O selection buttons.
     */
    @Test
    public void testComputerPlayerSelection() {
        GUI gui = new GUI();
        Board board = new Board(gui);
        gui.setVisible(false);

        // Select simple mode and computer for blue player
        gui.getSimpleGame().setSelected(true);
        gui.getBlueComputer().setSelected(true);
        
        gui.getBoardSizeField().setText("3");

        // Manually call updatePlayerControls to simulate disabling buttons
        gui.updatePlayerControls("Blue");

        // Act
        gui.getBtnNewGame().doClick();

        // Assert
        assertNotNull(board.getBlueComputer(), "Blue computer player should be initialized in simple mode");
        assertNull(board.getRedComputer(), "Red computer player should not be initialized in simple mode");

        // Verify that blue S/O buttons are disabled
        assertFalse(gui.getBlueS().isEnabled(), "Blue S button should be disabled when blue player is Computer");
        assertFalse(gui.getBlueO().isEnabled(), "Blue O button should be disabled when blue player is Computer");
    }

    /**
     * Test that the computer player can make a move in simple game mode.
     */
    @Test
    public void testComputerPlayerMakesMoveInSimpleMode() {
        // Arrange
        SimpleGameMode gameMode = new SimpleGameMode();
        ComputerPlayer computerPlayer = new ComputerPlayer('B', gameMode, true);
        char[][] boardState = {
            {' ', ' ', ' '},
            {' ', ' ', ' '},
            {' ', ' ', ' '}
        };

        ComputerPlayer.Move move = computerPlayer.makeMove(boardState);

        assertNotNull(move, "Computer should make a move");
        assertTrue(move.row >= 0 && move.row < 3, "Move row should be within board");
        assertTrue(move.col >= 0 && move.col < 3, "Move col should be within board");
        assertTrue(move.letter == 'S' || move.letter == 'O', "Move letter should be 'S' or 'O'");
    }

    /**
     * Test a simple game where human plays against computer in simple mode.
     */
    @Test
    public void testComputerVsHumanGameplayInSimpleMode() {
        SimpleGameMode gameMode = new SimpleGameMode();
        ComputerPlayer redComputer = new ComputerPlayer('R', gameMode, true);
        char[][] boardState = new char[3][3];
        for (char[] row : boardState) {
            Arrays.fill(row, ' ');
        }

        // Blue (human) makes a move
        boolean sosFormed = gameMode.makeMove(0, 0, 'S');
        assertFalse(sosFormed, "No SOS should be formed yet");
        gameMode.switchPlayer();
        boardState[0][0] = 'S';

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
     * Test a simple game where two computer players play against each other.
     */
    @Test
    public void testComputerVsComputerGameplayInSimpleMode() {
        SimpleGameMode gameMode = new SimpleGameMode();
        ComputerPlayer blueComputer = new ComputerPlayer('B', gameMode, true);
        ComputerPlayer redComputer = new ComputerPlayer('R', gameMode, true);
        char[][] boardState = new char[3][3];
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
                } else {
                    // In simple mode, game ends immediately when an SOS is formed
                    break;
                }
            } else {
                break; // No valid moves left
            }
        }

        assertTrue(gameMode.isGameOver(), "Game should be over");
    }

    /**
     * Test that the simple game ends when the board is full and no SOS is formed.
     */
    @Test
    public void testSimpleGameOverWhenBoardIsFull() {
        SimpleGameMode gameMode = new SimpleGameMode();

        char[][] moves = {
            {'S', 'O', 'O'},
            {'O', 'S', 'S'},
            {'O', 'S', 'O'}
        };

        boolean sosFormed = false;
        for (int i = 0; i < moves.length; i++) {
            for (int j = 0; j < moves[i].length; j++) {
                sosFormed = gameMode.makeMove(i, j, moves[i][j]);
                assertFalse(sosFormed, "No SOS should be formed");
                if (!gameMode.isGameOver()) {
                    gameMode.switchPlayer();
                }
            }
        }

        assertTrue(gameMode.isGameOver(), "Game should be over when board is full");
    }
}